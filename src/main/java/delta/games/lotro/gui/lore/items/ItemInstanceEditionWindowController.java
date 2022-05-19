package delta.games.lotro.gui.lore.items;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for an item instance edition window.
 * @author DAM
 */
public class ItemInstanceEditionWindowController extends DefaultFormDialogController<ItemInstance<? extends Item>>
{
  // Controllers
  private ItemInstanceEditionPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param attrs Attributes of toon to use.
   * @param itemInstance Item instance.
   */
  public ItemInstanceEditionWindowController(WindowController parent, BasicCharacterAttributes attrs, ItemInstance<? extends Item> itemInstance)
  {
    super(parent,itemInstance);
    _panelController=new ItemInstanceEditionPanelController(this,attrs,_data);
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=_panelController.getPanel();
    return dataPanel;
  }

  @Override
  public void configureWindow()
  {
    Item reference=_data.getReference();
    String name=reference.getName();
    String title="Edit "+name;
    JDialog dialog=getDialog();
    dialog.setTitle(title);
    dialog.pack();
    dialog.setMinimumSize(dialog.getSize());
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
