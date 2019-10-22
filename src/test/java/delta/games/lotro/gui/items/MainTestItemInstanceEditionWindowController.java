package delta.games.lotro.gui.items;

import delta.games.lotro.gui.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Test for item instance edition window.
 * @author DAM
 */
public class MainTestItemInstanceEditionWindowController
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    for(String sample : ItemsTestUtils.TEST_SAMPLES)
    {
      ItemInstance<? extends Item> itemInstance=ItemsTestUtils.loadItemInstance(ItemsTestUtils.class,"samples/"+sample);
      ItemInstanceEditionWindowController ctrl=new ItemInstanceEditionWindowController(null,null,itemInstance);
      ctrl.show(false);
    }
    for(String sample : LegendariesTestUtils.TEST_SAMPLES)
    {
      ItemInstance<? extends Item> itemInstance=LegendariesTestUtils.loadItemInstance(sample);
      ItemInstanceEditionWindowController ctrl=new ItemInstanceEditionWindowController(null,null,itemInstance);
      ctrl.show(false);
    }
  }
}
