package delta.games.lotro.gui.items;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for an item edition window.
 * @author DAM
 */
public class ItemEditionWindowController extends DefaultFormDialogController<Item>
{
  // Controllers
  private ItemEditionPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Character data.
   * @param item Item.
   */
  public ItemEditionWindowController(WindowController parent, CharacterSummary character, Item item)
  {
    super(parent,item);
    _panelController=new ItemEditionPanelController(this,character,item);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(dialog.getPreferredSize());
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=_panelController.getPanel();
    return dataPanel;
  }

  @Override
  protected void okImpl()
  {
    _panelController.getItem();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
