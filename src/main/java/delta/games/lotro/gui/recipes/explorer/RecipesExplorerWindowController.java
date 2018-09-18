package delta.games.lotro.gui.recipes.explorer;

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
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.recipes.RecipeFilter;
import delta.games.lotro.gui.recipes.RecipeFilterController;
import delta.games.lotro.gui.recipes.RecipesTableController;
import delta.games.lotro.lore.crafting.recipes.Recipe;

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
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
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
    // TODO
    System.out.println("Show recipe!");
    /*
    int id=_recipeWindows.getAll().size();
    DeedDisplayWindowController window=new DeedDisplayWindowController(RecipesExplorerWindowController.this,id);
    window.setDeed(deed);
    window.show(false);
    _recipeWindows.registerWindow(window);
    */
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
