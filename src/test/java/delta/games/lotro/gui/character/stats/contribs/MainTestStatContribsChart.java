package delta.games.lotro.gui.character.stats.contribs;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.CharacterGenerationTools;
import delta.games.lotro.character.stats.CharacterGeneratorGiswald;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.contribs.StatContribution;
import delta.games.lotro.character.stats.contribs.StatsContributionsManager;

/**
 * Test for the stat contributions chart.
 * @author DAM
 */
public class MainTestStatContribsChart
{
  private void doIt()
  {
    CharacterGenerationTools tools=new CharacterGenerationTools();
    CharacterGeneratorGiswald generator=new CharacterGeneratorGiswald(tools);
    CharacterData c=generator.buildCharacter();
    doIt(c);
  }

  private void doIt(CharacterData data)
  {
    StatsContributionsManager contribs=new StatsContributionsManager();
    CharacterStatsComputer statsComputer=new CharacterStatsComputer(contribs);
    BasicStatsSet stats=statsComputer.getStats(data);
    System.out.println(stats);
    Map<STAT,List<StatContribution>> sortedContribs=contribs.sortByStat();
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JTabbedPane tabs=GuiFactory.buildTabbedPane();
    panel.add(tabs,BorderLayout.CENTER);
    for(STAT stat : sortedContribs.keySet())
    {
      StatContribsChartPanelController chartController=new StatContribsChartPanelController(stat);
      chartController.setContributions(sortedContribs.get(stat));
      JPanel statPanel=chartController.getPanel();
      tabs.add(stat.getName(),statPanel);
    }
    DefaultWindowController w=new DefaultWindowController();
    w.getFrame().add(panel);
    w.getFrame().pack();
    w.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestStatContribsChart().doIt();
  }
}
