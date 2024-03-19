package delta.games.lotro.gui.character.status.traits;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.common.enums.TraitGroup;

/**
 * Utility methods related to trait groups UI.
 * @author DAM
 */
public class TraitGroupsUtils
{
  private static final int[] SLOT_CODES={
      8,  // HEAD
      9,  // BODY
      12, // SADDLE,
      13, // GEAR
      10, // LEFS
      11, // TAIL
      14  // HIDE
  };

  /**
   * Get the list of trait groups to use for mounted appearances.
   * @return A list of trait groups.
   */
  public static List<TraitGroup> getTraitGroupsForSlots()
  {
    List<TraitGroup> ret=new ArrayList<TraitGroup>();
    LotroEnum<TraitGroup> traitGroupEnum=LotroEnumsRegistry.getInstance().get(TraitGroup.class);
    for(int code : SLOT_CODES)
    {
      TraitGroup group=traitGroupEnum.getEntry(code);
      ret.add(group);
    }
    return ret;
  }

  // UI order:
  /*
    9 // BODY
    13 // GEAR
    8 // HEAD
    14 // HIDE
    10 // LEGS
    12 // SADDLE
    11 // TAIL
   */
}
