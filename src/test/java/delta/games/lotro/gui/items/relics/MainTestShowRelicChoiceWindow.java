package delta.games.lotro.gui.items.relics;

import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicType;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;

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
    RelicsManager relicsMgr=RelicsManager.getInstance();
    Relic initialRelic=relicsMgr.getAllRelics().get(10);
    System.out.println(initialRelic);
    Relic relic=RelicChoiceWindowController.selectRelic(null,RelicType.SETTING,initialRelic);
    //Relic relic=RelicChoiceWindowController.selectRelic(null,null,initialRelic);
    System.out.println(relic);
  }
}
