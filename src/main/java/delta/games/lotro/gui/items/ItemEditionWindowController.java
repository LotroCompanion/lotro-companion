package delta.games.lotro.gui.items;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for an item edition window.
 * @author DAM
 */
public class ItemEditionWindowController extends DefaultFormDialogController<Item>
{
  private ItemEditionPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param item Item.
   */
  public ItemEditionWindowController(WindowController parent, Item item)
  {
    super(parent,item);
    _panelController=new ItemEditionPanelController(this,item);
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
