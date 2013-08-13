package delta.games.lotro.lore.items;

/**
 * Test for items loading from the items manager.
 * @author DAM
 */
public class MainTestItemsManager
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    ItemsManager manager=ItemsManager.getInstance();
    // Test for "ï" escape (object: "Egnïon-breichled")
    Item item=manager.getItem(Integer.valueOf(1879224554));
    String str=item.dump();
    System.out.println(str);
  }
}
