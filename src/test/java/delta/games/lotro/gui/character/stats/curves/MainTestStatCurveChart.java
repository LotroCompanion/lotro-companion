package delta.games.lotro.gui.character.stats.curves;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.CharacterGenerationTools;
import delta.games.lotro.character.stats.CharacterGeneratorGiswald;
import delta.games.lotro.character.stats.CharacterStatsComputer;

/**
 * Test for the stat curve chart.
 * @author DAM
 */
public class MainTestStatCurveChart
{
  private void doIt()
  {
    // Load toon stats
    CharacterGenerationTools tools=new CharacterGenerationTools();
    CharacterGeneratorGiswald generator=new CharacterGeneratorGiswald(tools);
    CharacterData c=generator.buildCharacter();

    List<StatCurvesChartConfiguration> configs=new ArrayList<StatCurvesChartConfiguration>();
    StatCurvesConfigurationFactory factory=new StatCurvesConfigurationFactory(c.getCharacterClass());
    configs.add(factory.buildPhysicalDamageChart());
    configs.add(factory.buildTacticalDamageChart());
    configs.add(factory.buildCriticalsChart());
    configs.add(factory.buildFinesseChart());
    configs.add(factory.buildHealingChart());
    configs.add(factory.buildIncomingHealingChart());
    configs.add(factory.buildBlockChart());
    configs.add(factory.buildParryChart());
    configs.add(factory.buildEvadeChart());
    configs.add(factory.buildResistanceChart());
    configs.add(factory.buildCriticalDefenceChart());
    configs.add(factory.buildPhysicalMitigationChart());
    configs.add(factory.buildTacticalMitigationChart());
    configs.add(factory.buildOrcCraftFellWroughtMitigationChart());

    CharacterStatsComputer statsComputer=new CharacterStatsComputer();
    BasicStatsSet stats=statsComputer.getStats(c);
    c.getStats().setStats(stats);
    int level=c.getLevel();

    for(StatCurvesChartConfiguration config : configs)
    {
      config.setLevel(level);
      config.setMaxRating(config.getAutoMaxRating());
      StatCurvesPanelController controller=new StatCurvesPanelController(config);
      controller.update(c);
      JPanel statPanel=controller.getPanel();
      DefaultWindowController w=new DefaultWindowController();
      w.getFrame().add(statPanel);
      w.getFrame().pack();
      w.show();
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestStatCurveChart().doIt();
  }
}
