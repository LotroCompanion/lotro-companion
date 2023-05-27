package delta.games.lotro.gui.lore.items.legendary.imbued;

import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.classes.WellKnownCharacterClassKeys;
import delta.games.lotro.lore.items.EquipmentLocations;
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
    ClassDescription minstrel=ClassesManager.getInstance().getCharacterClassByKey(WellKnownCharacterClassKeys.MINSTREL);
    ImbuedLegacy legcy=ImbuedLegacyChooser.selectImbuedLegacy(null,minstrel,EquipmentLocations.CLASS_SLOT,null);
    System.out.println(legcy);
  }
}
