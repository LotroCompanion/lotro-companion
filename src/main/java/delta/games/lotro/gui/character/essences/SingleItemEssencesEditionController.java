package delta.games.lotro.gui.character.essences;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.gui.character.gear.EquipmentSlotIconController;
import delta.games.lotro.gui.lore.items.essences.SimpleSingleEssenceEditionController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.essences.Essence;
import delta.games.lotro.lore.items.essences.EssencesSet;
import delta.games.lotro.lore.items.essences.EssencesSlotsSetup;
import delta.games.lotro.lore.items.essences.SocketTypes;

/**
 * Controller for the edition of the essences of a single item.
 * @author DAM
 */
public class SingleItemEssencesEditionController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  private BasicCharacterAttributes _attrs;
  // Controllers
  private WindowController _parent;
  private List<SimpleSingleEssenceEditionController> _controllers;
  // UI
  private EquipmentSlotIconController _iconController;
  private JLabel _icon;
  private MultilineLabel _itemName;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param attrs Attributes of toon to use.
   * @param slot Managed slot.
   */
  public SingleItemEssencesEditionController(WindowController parent, BasicCharacterAttributes attrs, GearSlot slot)
  {
    _itemInstance=null;
    _attrs=attrs;
    _parent=parent;
    _controllers=new ArrayList<SimpleSingleEssenceEditionController>();
    _iconController=new EquipmentSlotIconController(slot);
    _icon=GuiFactory.buildIconLabel(_iconController.getIcon());
    _itemName=new MultilineLabel();
  }

  /**
   * Get the managed item instance.
   * @return the managed item instance or <code>null</code>.
   */
  public ItemInstance<? extends Item> getItemInstance()
  {
    return _itemInstance;
  }

  /**
   * Set the managed item instance.
   * @param itemInstance Item instance to set.
   */
  public void setItem(ItemInstance<? extends Item> itemInstance)
  {
    _itemInstance=itemInstance;
    Item item=null;
    _controllers.clear();
    String label="-";
    if (itemInstance!=null)
    {
      item=_itemInstance.getReference();
      // Label
      label=itemInstance.getName();
      // Essences
      EssencesSet essences=itemInstance.getEssences();
      int nbEssences=0;
      if (essences!=null)
      {
        nbEssences=essences.getSize();
      }
      EssencesSlotsSetup setup=item.getEssenceSlotsSetup();
      if (setup!=null)
      {
        int nbEssenceSlots=setup.getSocketsCount();
        int size=Math.max(nbEssences,nbEssenceSlots);
        
        for(int i=0;i<size;i++)
        {
          SocketType type=(i<nbEssenceSlots)?setup.getSlotType(i):SocketTypes.CLASSIC;
          SimpleSingleEssenceEditionController controller=new SimpleSingleEssenceEditionController(_parent,2,_attrs,type);
          Essence essence=null;
          if (essences!=null)
          {
            essence=essences.getEssence(i);
          }
          controller.setEssence(essence);
          _controllers.add(controller);
        }
      }
    }
    _iconController.setItem(itemInstance);
    _icon.setIcon(_iconController.getIcon());
    _icon.setToolTipText(_iconController.getTooltip());
    _itemName.setText(label,2);
  }

  /**
   * Get the icon for the item.
   * @return the icon for the item.
   */
  public JLabel getItemIcon()
  {
    return _icon;
  }

  /**
   * Get the label for the item.
   * @return the label for the item.
   */
  public JPanel getItemLabel()
  {
    return _itemName;
  }

  /**
   * Get the managed controllers.
   * @return a list of essence edition controllers.
   */
  public List<SimpleSingleEssenceEditionController> getEssenceControllers()
  {
    return _controllers;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _itemInstance=null;
    // Controllers
    _parent=null;
    if (_controllers!=null)
    {
      for(SimpleSingleEssenceEditionController controller : _controllers)
      {
        controller.dispose();
      }
      _controllers.clear();
      _controllers=null;
    }
    _iconController=null;
    // UI
    _itemName=null;
  }
}
