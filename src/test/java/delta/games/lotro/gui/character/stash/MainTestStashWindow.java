package delta.games.lotro.gui.character.stash;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.gear.CharacterGear;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.character.storage.stash.ItemsStash;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Test for character stash window.
 * @author DAM
 */
public class MainTestStashWindow
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    CharacterFile toon=utils.getMainToon();
    // Copy gear to stash
    ItemsStash stash=toon.getStash();
    CharacterData data=toon.getInfosManager().getLastCharacterDescription();
    CharacterGear gear=data.getEquipment();
    for(GearSlot slot : GearSlot.getAll())
    {
      ItemInstance<? extends Item> item=gear.getItemForSlot(slot);
      if (item!=null)
      {
        ItemInstance<? extends Item> clone=ItemFactory.cloneInstance(item);
        clone.setWearer(null);
        stash.addItem(clone);
      }
    }
    StashWindowController controller=new StashWindowController(toon);
    controller.show();
  }
}
