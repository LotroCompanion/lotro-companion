package delta.games.lotro.gui.lore.items.legendary2.traceries;

import delta.games.lotro.character.gear.CharacterGear;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary2.LegendaryInstance2;
import delta.games.lotro.lore.items.legendary2.LegendaryInstanceAttrs2;
import delta.games.lotro.lore.items.legendary2.SocketEntry;
import delta.games.lotro.lore.items.legendary2.SocketEntryInstance;
import delta.games.lotro.lore.items.legendary2.SocketsSetupInstance;
import delta.games.lotro.lore.items.legendary2.Tracery;

/**
 * Utility methods for traceries.
 * @author DAM
 */
public class TraceryUtils
{
  /**
   * Build the constraints manager for the given equipment.
   * @param gear Gear to use.
   * @param currentSlot Slot to skip.
   * @return A constraints manager or <code>null</code> if no other LI slotted.
   */
  public static TraceriesConstraintsMgr build(CharacterGear gear, GearSlot currentSlot)
  {
    TraceriesConstraintsMgr ret=null;
    for(GearSlot slot : GearSlot.getAll())
    {
      if (slot==currentSlot)
      {
        continue;
      }
      ItemInstance<? extends Item> itemInstance=gear.getItemForSlot(slot);
      if (itemInstance instanceof LegendaryInstance2)
      {
        ret=buildFromItem(itemInstance,ret);
      }
    }
    return ret;
  }

  /**
   * Build the constraints manager for the given item.
   * @param itemInstance Item instance.
   * @param parent Parent manager.
   * @return A new constraints manager.
   */
  public static TraceriesConstraintsMgr buildFromItem(ItemInstance<? extends Item> itemInstance, TraceriesConstraintsMgr parent)
  {
    TraceriesConstraintsMgr ret=new TraceriesConstraintsMgr(parent);
    LegendaryInstance2 li=(LegendaryInstance2)itemInstance;
    LegendaryInstanceAttrs2 attrs=li.getLegendaryAttributes();
    SocketsSetupInstance sockets=attrs.getSocketsSetup();
    int nbSockets=sockets.getSocketsCount();
    for(int i=0;i<nbSockets;i++)
    {
      SocketEntryInstance entryInstance=sockets.getEntry(i);
      SocketEntry entry=entryInstance.getTemplate();
      boolean enabled=entry.isEnabled(itemInstance);
      if (enabled)
      {
        Tracery tracery=entryInstance.getTracery();
        ret.use(tracery);
      }
    }
    return ret;
  }
}
