package delta.games.lotro.gui.items.legendary.shared;

import delta.games.lotro.gui.items.ItemsTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;

/**
 * Utility methods to test the edition of legendary item instances.
 * @author DAM
 */
public class LegendariesTestUtils
{
  /**
   * Test samples.
   */
  public static final String[] TEST_SAMPLES=
  {
    "CaptainEmblemSecondAge75NonImbued.xml",
    "CaptainGreatSwordFirstAgeImbued.xml",
    "GuardianBeltSecondAge75NonImbued.xml",
    "HunterAxeFirstAgeNonImbued.xml",
    "HunterAxeThirdAgeNonImbued.xml",
    "HunterCrossbowFirstAge75NonImbued.xml",
    "HunterCrossbowFirstAgeImbued.xml",
    "LoreMasterBookFirstAgeImbued.xml",
    "MinstrelClubFirstAgeImbued.xml",
    "RuneKeeperSatchelSecondAge85NonImbued.xml",
    "toringeSongbook.xml"
  };

  /**
   * Load an item instance for tests.
   * @param name Name of the test file.
   * @return the loaded item instance.
   */
  public static ItemInstance<? extends Item> loadItemInstance(String name)
  {
    return ItemsTestUtils.loadItemInstance(LegendariesTestUtils.class,name);
  }

  /**
   * Extract the legendary instance attributes from an item instance.
   * @param itemInstance Source item instance.
   * @return the extracted attributes or <code>null</code> if not found.
   */
  public static LegendaryInstanceAttrs getLegendaryAttrs(ItemInstance<? extends Item> itemInstance)
  {
    LegendaryInstanceAttrs legAttrs=null;
    if (itemInstance instanceof LegendaryInstance)
    {
      LegendaryInstance legInstance=(LegendaryInstance)itemInstance;
      legAttrs=legInstance.getLegendaryAttributes();
    }
    return legAttrs;
  }
}
