package delta.games.lotro.gui.recipes;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.recipes.form.RecipeDisplayPanelController;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.RecipesManager;

/**
 * Factory for recipe panels.
 * @author DAM
 */
public class RecipePanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public RecipePanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.RECIPE_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildRecipePanel(id);
    }
    return ret;
  }

  private RecipeDisplayPanelController buildRecipePanel(int itemId)
  {
    RecipesManager recipesMgr=RecipesManager.getInstance();
    Recipe recipe=recipesMgr.getRecipeById(itemId);
    if (recipe!=null)
    {
      RecipeDisplayPanelController itemPanel=new RecipeDisplayPanelController(_parent,recipe);
      return itemPanel;
    }
    return null;
  }
}
