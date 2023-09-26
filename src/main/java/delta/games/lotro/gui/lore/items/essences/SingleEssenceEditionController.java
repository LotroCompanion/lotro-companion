package delta.games.lotro.gui.lore.items.essences;

import java.awt.Cursor;

import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.common.enums.SocketType;

/**
 * Controller for the UI items to edit a single essence.
 * @author DAM
 */
public class SingleEssenceEditionController extends SingleEssenceDisplayController
{
  private JButton _deleteButton;

  /**
   * Constructor.
   * @param type Socket type.
   */
  public SingleEssenceEditionController(SocketType type)
  {
    super(type);
    init();
  }

  private void init()
  {
    _icon.setEnabled(true);
    _icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

  @Override
  public void dispose()
  {
    super.dispose();
    _deleteButton=null;
  }
}
