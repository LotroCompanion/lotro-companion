package delta.games.lotro.gui.character.stash;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.character.storage.ItemsStash;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;

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
    CharacterEquipment gear=data.getEquipment();
    for(EQUIMENT_SLOT slot : EQUIMENT_SLOT.values())
    {
      SlotContents contents=gear.getSlotContents(slot,false);
      if (contents!=null)
      {
        Item item=contents.getItem();
        Item clone=ItemFactory.clone(item);
        stash.addItem(clone);
      }
    }
    StashWindowController controller=new StashWindowController(toon);
    controller.show();
  }
}
