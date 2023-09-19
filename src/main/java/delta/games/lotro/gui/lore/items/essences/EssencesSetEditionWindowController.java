package delta.games.lotro.gui.lore.items.essences;

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
public class EssencesSetEditionWindowController extends DefaultFormDialogController<ItemInstance<? extends Item>>
{
  // Controllers
  private EssencesEditionPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param attrs Attributes of toon to use.
   * @param itemInstance Item instance.
   */
  public EssencesSetEditionWindowController(WindowController parent, BasicCharacterAttributes attrs, ItemInstance<? extends Item> itemInstance)
  {
    super(parent,itemInstance);
    _panelController=new EssencesEditionPanelController(this,attrs,itemInstance.getReference());
    _panelController.init(itemInstance.getEssences());
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
  public void configureWindow()
  {
    Item reference=_data.getReference();
    String name=reference.getName();
    String title="Edit essences for "+name;
    getDialog().setTitle(title);
  }

  @Override
  protected void okImpl()
  {
    _panelController.getEssences(_data.getEssences());
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
