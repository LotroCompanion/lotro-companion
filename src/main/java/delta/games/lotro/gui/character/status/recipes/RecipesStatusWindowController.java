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
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.recipes.RecipeStatus;
import delta.games.lotro.character.status.recipes.RecipesStatusManager;
import delta.games.lotro.character.status.recipes.filter.RecipeStatusFilter;
import delta.games.lotro.common.blacklist.Blacklist;
import delta.games.lotro.common.blacklist.io.BlackListIO;
import delta.games.lotro.gui.character.status.quests.BlacklistController;
import delta.games.lotro.gui.character.status.recipes.filter.RecipeStateFilterController;
import delta.games.lotro.gui.character.status.recipes.table.RecipeStatusTableController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.crafting.recipes.RecipeFilterController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.crafting.recipes.Recipe;

/**
 * Controller for a recipes status display window.
 * @author DAM
 */
public class RecipesStatusWindowController extends DefaultDisplayDialogController<RecipesStatusManager> implements FilterUpdateListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="RECIPES_STATUS";

  private static final int MAX_HEIGHT=800;

  // Data
  private RecipeStatusFilter _filter;
  private CharacterFile _toon;
  // Controllers
  private RecipeStateFilterController _statusFilterController;
  private RecipeFilterController _filterController;
  private RecipesStatusPanelController _panelController;
  private RecipeStatusTableController _tableController;
  private RecipesStatsDisplayPanelController _statsController;
  private BlacklistController<RecipeStatus> _blacklistController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Toon to use.
   * @param status Status to show.
   */
  public RecipesStatusWindowController(WindowController parent, CharacterFile toon, RecipesStatusManager status)
  {
    super(parent,status);
    _toon=toon;
    _filter=new RecipeStatusFilter();
    _statsController=new RecipesStatsDisplayPanelController();
    updateStatistics();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(1000,300));
    dialog.setTitle("Recipes status"); // I18n
    dialog.pack();
    Dimension size=dialog.getSize();
    if (size.height>MAX_HEIGHT)
    {
      dialog.setSize(size.width,MAX_HEIGHT);
    }
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
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
    Blacklist blacklist=BlackListIO.load(_toon,"recipes");
    initTable(blacklist);
    _panelController=new RecipesStatusPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Build child controllers
    _filterController=new RecipeFilterController(_data.getRecipes(),_filter.getRecipeFilter(),this);
    _statusFilterController=new RecipeStateFilterController(_filter.getStateFilter(),this);
    // Blacklist
    _filter.setBlacklist(blacklist);
    _blacklistController=new BlacklistController<RecipeStatus>(blacklist,_tableController.getTableController(),this,_filter.getBlacklistFilter());
    // Whole panel
    // - filter
    JPanel filterPanel=buildFilterPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    // - stats
    JPanel statsPanel=_statsController.getPanel();
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    statsPanel.setBorder(GuiFactory.buildTitledBorder("Statistics")); // I18n
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
    recipeFilterPanel.setBorder(GuiFactory.buildTitledBorder("Recipe Filter")); // I18n
    panel.add(recipeFilterPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Status filter
    JPanel statusFilterPanel=_statusFilterController.getPanel();
    statusFilterPanel.setBorder(GuiFactory.buildTitledBorder("Status Filter")); // I18n
    panel.add(statusFilterPanel,c);
    // Blacklist
    JPanel blacklistPanel=_blacklistController.getPanel();
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(blacklistPanel,c);
    // Glue
    c=new GridBagConstraints(2,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    return panel;
  }

  private void initTable(Blacklist blacklist)
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("RecipesStatus");
    _tableController=new RecipeStatusTableController(_data,prefs,_filter,blacklist);
    GenericTableController<RecipeStatus> tableController=_tableController.getTableController();
    JTable table=tableController.getTable();
    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
    _data.update(_filter);
    _statsController.updateUI(_data.getStatistics());
  }

  private void showRecipe(Recipe recipe)
  {
    PageIdentifier ref=ReferenceConstants.getRecipeReference(recipe.getIdentifier());
    NavigationUtils.navigateTo(ref,this);
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
    _toon=null;
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
    if (_blacklistController!=null)
    {
      _blacklistController.dispose();
      _blacklistController=null;
    }
  }
}
