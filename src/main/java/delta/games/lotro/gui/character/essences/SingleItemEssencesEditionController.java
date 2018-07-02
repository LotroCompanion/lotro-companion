package delta.games.lotro.gui.character.essences;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.gui.character.gear.EquipmentSlotIconController;
import delta.games.lotro.gui.items.essences.SingleEssenceEditionController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.essences.EssencesSet;

/**
 * Controller for the edition of the essences of a single item.
 * @author DAM
 */
public class SingleItemEssencesEditionController
{
  // Data
  private Item _item;
  private CharacterSummary _character;
  // Controllers
  private WindowController _parent;
  private List<SingleEssenceEditionController> _controllers;
  // UI
  private EquipmentSlotIconController _iconController;
  private JLabel _icon;
  private MultilineLabel _itemName;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Character data.
   * @param slot Managed slot.
   */
  public SingleItemEssencesEditionController(WindowController parent, CharacterSummary character, EQUIMENT_SLOT slot)
  {
    _item=null;
    _character=character;
    _parent=parent;
    _controllers=new ArrayList<SingleEssenceEditionController>();
    _iconController=new EquipmentSlotIconController(slot);
    _icon=GuiFactory.buildIconLabel(_iconController.getIcon());
    _itemName=new MultilineLabel();
  }

  /**
   * Get the managed item.
   * @return the managed item or <code>null</code>.
   */
  public Item getItem()
  {
    return _item;
  }

  /**
   * Set the managed item.
   * @param item Item to set.
   */
  public void setItem(Item item)
  {
    _item=item;
    _controllers.clear();
    String label="-";
    if (item!=null)
    {
      // Label
      label=item.getName();
      // Essences
      EssencesSet essences=item.getEssences();
      int nbEssences=0;
      if (essences!=null)
      {
        nbEssences=essences.getSize();
      }
      int nbEssenceSlots=item.getEssenceSlots();
      int size=Math.max(nbEssences,nbEssenceSlots);
      for(int i=0;i<size;i++)
      {
        SingleEssenceEditionController controller=new SingleEssenceEditionController(_parent,2,_character);
        Item essence=null;
        if (essences!=null)
        {
          essence=essences.getEssence(i);
        }
        controller.setEssence(essence);
        _controllers.add(controller);
      }
    }
    _iconController.setItem(item);
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
  public List<SingleEssenceEditionController> getEssenceControllers()
  {
    return _controllers;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _item=null;
    // Controllers
    _parent=null;
    if (_controllers!=null)
    {
      for(SingleEssenceEditionController controller : _controllers)
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
