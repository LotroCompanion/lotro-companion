package delta.games.lotro.gui.items;

import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Test for item instance main attributes edition panel.
 * @author DAM
 */
public class MainTestItemInstanceMainAttrsEditionPanelController
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    ItemInstance<? extends Item> itemInstance=ItemsTestUtils.loadItemInstance(ItemsTestUtils.class,"samples/"+ItemsTestUtils.TEST_SAMPLES[0]);

    // Build controller
    ItemInstanceMainAttrsEditionWindowController controller=new ItemInstanceMainAttrsEditionWindowController(null,itemInstance);
    ItemInstance<? extends Item> updatedInstance=controller.editModal();
    if (updatedInstance!=null)
    {
      System.out.println(updatedInstance.dump());
    }
    controller.dispose();
  }
}
