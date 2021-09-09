package delta.games.lotro.gui.character.status.recipes;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.recipes.RecipeStatus;
import delta.games.lotro.character.status.recipes.RecipesStatusManager;
import delta.games.lotro.character.status.recipes.filter.RecipeStatusFilter;
import delta.games.lotro.gui.character.status.recipes.filter.RecipeStateFilterController;
import delta.games.lotro.gui.character.status.recipes.table.RecipeStatusTableController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.crafting.recipes.RecipeFilterController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.crafting.recipes.Recipe;

/**
 * Controller for a recipes status display window.
 * @author DAM
 */
public class RecipesStatusWindowController extends DefaultDisplayDialogController<RecipesStatusManager> implements FilterUpdateListener
{
  private static final int MAX_HEIGHT=800;

  // Data
  private RecipeStatusFilter _filter;
  // Controllers
  private RecipeStateFilterController _statusFilterController;
  private RecipeFilterController _filterController;
  private RecipesStatusPanelController _panelController;
  private RecipeStatusTableController _tableController;
  private RecipesStatsDisplayPanelController _statsController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public RecipesStatusWindowController(WindowController parent, RecipesStatusManager status)
  {
    super(parent,status);
    _filter=new RecipeStatusFilter();
    _statsController=new RecipesStatsDisplayPanelController();
    updateStatistics();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(1000,300));
    dialog.setTitle("Recipes status");
    dialog.pack();
    Dimension size=dialog.getSize();
    if (size.height>MAX_HEIGHT)
    {
      dialog.setSize(size.width,MAX_HEIGHT);
    }
    return dialog;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initTable();
    _panelController=new RecipesStatusPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Build child controllers
    _filterController=new RecipeFilterController(_data.getRecipes(),_filter.getRecipeFilter(),this);
    _statusFilterController=new RecipeStateFilterController(_filter.getStateFilter(),this);
    // Whole panel
    // - filter
    JPanel filterPanel=buildFilterPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    // - stats
    JPanel statsPanel=_statsController.getPanel();
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    statsPanel.setBorder(GuiFactory.buildTitledBorder("Statistics"));
    panel.add(statsPanel,c);
    // - table
    c=new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private JPanel buildFilterPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,3,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Recipe filter
    JPanel recipeFilterPanel=_filterController.getPanel();
    recipeFilterPanel.setBorder(GuiFactory.buildTitledBorder("Recipe Filter"));
    panel.add(recipeFilterPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Status filter
    JPanel statusFilterPanel=_statusFilterController.getPanel();
    statusFilterPanel.setBorder(GuiFactory.buildTitledBorder("Status Filter"));
    panel.add(statusFilterPanel,c);
    c=new GridBagConstraints(1,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    return panel;
  }

  private void initTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("RecipesStatus");
    _tableController=new RecipeStatusTableController(_data,prefs,_filter);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          RecipeStatus status=(RecipeStatus)event.getSource();
          showRecipe(status.getRecipe());
        }
      }
    };
    _tableController.getTableController().addActionListener(al);
  }

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
    updateStatistics();
  }

  private void updateStatistics()
  {
    _data.update(_filter.getRecipeFilter());
    _statsController.updateUI(_data.getStatistics());
  }

  private void showRecipe(Recipe recipe)
  {
    WindowsManager windowsMgr=getWindowsManager();
    int id=windowsMgr.getAll().size();
    NavigatorWindowController window=NavigatorFactory.buildNavigator(RecipesStatusWindowController.this,id);
    PageIdentifier ref=ReferenceConstants.getRecipeReference(recipe.getIdentifier());
    window.navigateTo(ref);
    window.show(false);
    windowsMgr.registerWindow(window);
  }


  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _data=null;
    _filter=null;
    // Controllers
    if (_statusFilterController!=null)
    {
      _statusFilterController.dispose();
      _statusFilterController=null;
    }
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    if (_statsController!=null)
    {
      _statsController.dispose();
      _statsController=null;
    }
  }
}
