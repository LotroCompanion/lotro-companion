package delta.games.lotro.gui.items;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterSummary;
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
   * @param character Character data.
   * @param itemInstance Item instance.
   */
  public ItemInstanceEditionWindowController(WindowController parent, CharacterSummary character, ItemInstance<? extends Item> itemInstance)
  {
    super(parent,itemInstance);
    _panelController=new ItemInstanceEditionPanelController(this,character,_data);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    return dialog;
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
    getDialog().setTitle(title);
  }

  @Override
  protected void okImpl()
  {
    //System.out.println(_data.dump());
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
