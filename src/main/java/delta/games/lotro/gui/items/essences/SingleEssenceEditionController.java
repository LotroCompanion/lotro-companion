package delta.games.lotro.gui.items.essences;

import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;

/**
 * Controller for the UI items to edit a single essence.
 * @author DAM
 */
public class SingleEssenceEditionController extends SingleEssenceDisplayController
{
  private JButton _deleteButton;

  /**
   * Constructor.
   */
  public SingleEssenceEditionController()
  {
    super();
    init();
  }

  private void init()
  {
    // Delete button
    _deleteButton=GuiFactory.buildIconButton("/resources/gui/icons/cross.png");
  }

  /**
   * Get the managed 'delete button'.
   * @return a button.
   */
  public JButton getDeleteButton()
  {
    return _deleteButton;
  }

  public void dispose()
  {
    super.dispose();
    _deleteButton=null;
  }
}
