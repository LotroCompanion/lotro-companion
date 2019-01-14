package delta.games.lotro.gui.character.stats.contribs;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.CharacterGenerationTools;
import delta.games.lotro.character.stats.CharacterGeneratorGiswald;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.character.stats.contribs.ContribsByStat;
import delta.games.lotro.character.stats.contribs.StatsContributionsManager;
import delta.games.lotro.common.stats.StatDescription;

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
    StatsContributionsManager contribs=new StatsContributionsManager(data.getCharacterClass());
    CharacterStatsComputer statsComputer=new CharacterStatsComputer(contribs);
    BasicStatsSet stats=statsComputer.getStats(data);
    System.out.println(stats);
    contribs.setResolveIndirectContributions(true);
    contribs.compute();
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JTabbedPane tabs=GuiFactory.buildTabbedPane();
    panel.add(tabs,BorderLayout.CENTER);
    for(StatDescription stat : contribs.getContributingStats())
    {
      ContribsByStat contribsForStat=contribs.getContribs(stat);
      StatContribsChartPanelController chartController=new StatContribsChartPanelController();
      chartController.setContributions(contribsForStat);
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
