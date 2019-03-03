package delta.games.lotro.gui.items.legendary.titles;

import delta.games.lotro.lore.items.legendary.titles.LegendaryTitle;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitlesManager;

/**
 * Test for legendary title choice window.
 * @author DAM
 */
public class MainTestShowLegendaryTitleChoiceWindow
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LegendaryTitlesManager titlesMgr=LegendaryTitlesManager.getInstance();
    LegendaryTitle initialTitle=titlesMgr.getAll().get(10);
    System.out.println(initialTitle);
    LegendaryTitle title=LegendaryTitleChooser.selectLegendaryTitle(null,initialTitle);
    System.out.println(title);
  }
}
