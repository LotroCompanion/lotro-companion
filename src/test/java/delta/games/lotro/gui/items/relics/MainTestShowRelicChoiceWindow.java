package delta.games.lotro.gui.items.relics;

import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Test for relic choice window.
 * @author DAM
 */
public class MainTestShowRelicChoiceWindow
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    Relic relic=RelicChoiceWindowController.selectRelic(null);
    System.out.println(relic);
  }
}
