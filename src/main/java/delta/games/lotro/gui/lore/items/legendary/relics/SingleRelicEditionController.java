package delta.games.lotro.gui.lore.items.legendary.relics;

import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;

/**
 * Controller for the UI items to edit a single relic.
 * @author DAM
 */
public class SingleRelicEditionController extends SingleRelicDisplayController
{
  private JButton _deleteButton;

  /**
   * Constructor.
   */
  public SingleRelicEditionController()
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

  @Override
  public void dispose()
  {
    super.dispose();
    _deleteButton=null;
  }
}
