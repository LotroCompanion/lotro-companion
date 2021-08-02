package delta.games.lotro.gui.lore.items.legendary.imbued;

import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacy;

/**
 * Test for imbued legacy window.
 * @author DAM
 */
public class MainTestImbuedLegacyChoiceWindow
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    ImbuedLegacy legcy=ImbuedLegacyChooser.selectImbuedLegacy(null,CharacterClass.MINSTREL,EquipmentLocation.CLASS_SLOT,null);
    //Relic relic=RelicChoiceWindowController.selectRelic(null,null,initialRelic);
    System.out.println(legcy);
  }
}
