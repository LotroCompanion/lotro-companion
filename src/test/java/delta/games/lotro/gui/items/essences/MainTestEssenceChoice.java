package delta.games.lotro.gui.items.essences;

import delta.games.lotro.lore.items.Item;

/**
 * Test class for the essence choice UI.
 * @author DAM
 */
public class MainTestEssenceChoice
{
  private void doIt()
  {
    Item ret=EssenceChoice.chooseEssence(null);
    System.out.println(ret);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestEssenceChoice().doIt();
  }
}
