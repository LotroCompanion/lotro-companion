package delta.games.lotro.gui.lore.items.legendary;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;

/**
 * Controller for an item instance edition window.
 * @author DAM
 */
public class LegendaryInstanceEditionWindowController extends DefaultFormDialogController<ItemInstance<? extends Item>>
{
  // Controllers
  private LegendaryInstanceEditionPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param characterClass Character class to use.
   * @param itemInstance Item instance.
   */
  public LegendaryInstanceEditionWindowController(WindowController parent, ClassDescription characterClass, ItemInstance<? extends Item> itemInstance)
  {
    super(parent,itemInstance);
    EquipmentLocation slot=itemInstance.getReference().getEquipmentLocation();
    ClassAndSlot constraints=new ClassAndSlot(characterClass,slot);
    _panelController=new LegendaryInstanceEditionPanelController(this,itemInstance,constraints);
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
    String title="Edit legendary data for "+name;
    getDialog().setTitle(title);
  }

  @Override
  protected void okImpl()
  {
    LegendaryInstance legendaryInstance=(LegendaryInstance)_data;
    LegendaryInstanceAttrs attrs=legendaryInstance.getLegendaryAttributes();
    _panelController.getData(attrs);
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
