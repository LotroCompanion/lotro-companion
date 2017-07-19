package delta.games.lotro.gui.character.essences;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.gui.items.essences.SingleEssenceEditionController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.essences.EssencesSet;

/**
 * @author dm
 */
public class SingleItemEssencesEditionController
{
  // Data
  private Item _item;
  private EQUIMENT_SLOT _slot;
  // Controllers
  private WindowController _parent;
  private List<SingleEssenceEditionController> _controllers;
  // UI
  private JLabel _itemName;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param slot Managed slot.
   */
  public SingleItemEssencesEditionController(WindowController parent, EQUIMENT_SLOT slot)
  {
    _item=null;
    _slot=slot;
    _parent=parent;
    _controllers=new ArrayList<SingleEssenceEditionController>();
    _itemName=GuiFactory.buildLabel("");
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
    String label=_slot.name();
    if (item!=null)
    {
      // Label
      label=item.getName();
      // Essences
      EssencesSet essences=item.getEssences();
      if (essences!=null)
      {
        int size=essences.getSize();
        for(int i=0;i<size;i++)
        {
          Item essence=essences.getEssence(i);
          SingleEssenceEditionController controller=new SingleEssenceEditionController(_parent);
          controller.setEssence(essence);
          _controllers.add(controller);
        }
      }
    }
    _itemName.setText(label);
  }

  /**
   * Get the label for the item.
   * @return the label for the item.
   */
  public JLabel getItemLabel()
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
    _slot=null;
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
    // UI
    _itemName=null;
  }
}
