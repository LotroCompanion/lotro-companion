package delta.games.lotro.gui.lore.crafting.recipes.explorer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.crafting.recipes.RecipeFilterController;
import delta.games.lotro.gui.lore.crafting.recipes.RecipesTableController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeFilter;

/**
 * Controller for the recipes explorer window.
 * @author DAM
 */
public class RecipesExplorerWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="RECIPES_EXPLORER";

  private RecipeFilterController _filterController;
  private RecipeExplorerPanelController _panelController;
  private RecipesTableController _tableController;
  private RecipeFilter _filter;
  private WindowsManager _recipeWindows;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public RecipesExplorerWindowController(WindowController parent)
  {
    super(parent);
    _filter=new RecipeFilter();
    _recipeWindows=new WindowsManager();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Recipes explorer");
    frame.setMinimumSize(new Dimension(400,300));
    frame.setSize(950,700);
    return frame;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initRecipesTable();
    _panelController=new RecipeExplorerPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new RecipeFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);

    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(GuiFactory.buildPanel(null),c);
    c=new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private void initRecipesTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("RecipesExplorer");
    _tableController=new RecipesTableController(prefs,_filter);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          Recipe recipe=(Recipe)event.getSource();
          showRecipe(recipe);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showRecipe(Recipe recipe)
  {
    int id=_recipeWindows.getAll().size();
    NavigatorWindowController window=NavigatorFactory.buildNavigator(RecipesExplorerWindowController.this,id);
    PageIdentifier ref=ReferenceConstants.getRecipeReference(recipe.getIdentifier());
    window.navigateTo(ref);
    window.show(false);
    _recipeWindows.registerWindow(window);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    if (_recipeWindows!=null)
    {
      _recipeWindows.disposeAll();
      _recipeWindows=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
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
  }
}
