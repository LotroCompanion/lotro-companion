package delta.games.lotro.gui.recipes.form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.crafting.recipes.Recipe;

/**
 * Controller for a "recipe display" window.
 * @author DAM
 */
public class RecipeDisplayWindowController extends DefaultDialogController
{
  // Controllers
  private RecipeDisplayPanelController _controller;
  private int _id;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param id Identifier.
   */
  public RecipeDisplayWindowController(WindowController parent, int id)
  {
    super(parent);
    _id=id;
  }

  /**
   * Set recipe to display.
   * @param recipe Recipe to display.
   */
  public void setRecipe(Recipe recipe)
  {
    disposeContentsPanel();
    _controller=new RecipeDisplayPanelController(recipe);
    JDialog dialog=getDialog();
    Container container=dialog.getContentPane();
    container.removeAll();
    JPanel panel=_controller.getPanel();
    container.add(panel,BorderLayout.CENTER);
    dialog.setTitle("Recipe: "+recipe.getName());
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    dialog.setResizable(false);
  }

  @Override
  public String getWindowIdentifier()
  {
    return "RECIPE_DISPLAY#"+_id;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    disposeContentsPanel();
    super.dispose();
  }

  private void disposeContentsPanel()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
  }
}
