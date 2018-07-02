package delta.games.lotro.gui.items;

import java.util.List;

import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Test for item edition window.
 * @author DAM
 */
public class MainTestItemEditionWindowController
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CharacterSummary summary=new CharacterSummary();
    summary.setCharacterClass(CharacterClass.MINSTREL);
    summary.setLevel(105);
    ItemsManager itemsMgr=ItemsManager.getInstance();
    List<Item> items=itemsMgr.getAllItems();
    int nbWindows=0;
    int index=0;
    int nbItems=items.size();
    while ((index<nbItems) && (nbWindows<1))
    {
      //Item item=itemsMgr.getItem(1879313783); // Light Nadhin Hood (level 192)
      //Item item=itemsMgr.getItem(1879323547); // Bridge-warden's Shield (level 201)
      //Item item=itemsMgr.getItem(1879134849); // Javelin of Ire
      //Item item=itemsMgr.getItem(1879311750); // Reshaped Minstrel's Club of the First Age
      Item item=itemsMgr.getItem(1879279194); // Lesser Valourous Helm of Fate
      //Item item=items.get(index);
      //if ((item.getQuality()==ItemQuality.LEGENDARY) && (Integer.valueOf(192).equals(item.getItemLevel())))
      //if (item instanceof Armour)
      //if (item instanceof Weapon)
      {
        ItemEditionWindowController ctrl=new ItemEditionWindowController(null,summary,item);
        ctrl.show(true);
        nbWindows++;
      }
      index++;
    }
  }
}
