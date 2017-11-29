package delta.games.lotro.gui.character.stats.curves;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.CharacterGenerationTools;
import delta.games.lotro.character.stats.CharacterGeneratorGiswald;
import delta.games.lotro.character.stats.ratings.RatingCurve;
import delta.games.lotro.character.stats.ratings.RatingsMgr;

/**
 * Test for the stat curve chart.
 * @author DAM
 */
public class MainTestStatCurveChart
{
  private void doIt()
  {
    CharacterGenerationTools tools=new CharacterGenerationTools();
    CharacterGeneratorGiswald generator=new CharacterGeneratorGiswald(tools);
    CharacterData c=generator.buildCharacter();
    doIt(c);
  }

  private StatCurvesChartConfiguration build(String title, RatingCurve curve, int level, double minRating, double maxRating)
  {
    StatCurvesChartConfiguration chartCfg=new StatCurvesChartConfiguration(title,level,minRating,maxRating);
    SingleStatCurveConfiguration curveCfg=new SingleStatCurveConfiguration(title,curve);
    chartCfg.addCurve(curveCfg);
    return chartCfg;
  }

  private void doIt(CharacterData data)
  {
    RatingsMgr mgr=new RatingsMgr();
    List<StatCurvesChartConfiguration> configs=new ArrayList<StatCurvesChartConfiguration>();
    {
      // Damage
      // - physical mastery => physical damage
      // - tactical mastery => tactical damage
      RatingCurve damage=mgr.getDamage();
      configs.add(build("Physical/tactical damage",damage,105,0,200000));
      // Critical hit
      // Cap ratings are: 22000, 22000, 35000 => 35000
      {
        StatCurvesChartConfiguration critChart=new StatCurvesChartConfiguration("Critical Rating",105,0,35000);
        RatingCurve critDevHitMagnitude=mgr.getCritAndDevastateHitMagnitudeCurve();
        SingleStatCurveConfiguration magnitudeCfg=new SingleStatCurveConfiguration("Critical/devastate magnitude %",critDevHitMagnitude);
        critChart.addCurve(magnitudeCfg);
        RatingCurve critHit=mgr.getCriticalHitCurve();
        SingleStatCurveConfiguration critHitCfg=new SingleStatCurveConfiguration("Critical Hit Chance",critHit);
        critChart.addCurve(critHitCfg);
        RatingCurve devHit=mgr.getDevastateHitCurve();
        SingleStatCurveConfiguration devastateHitCfg=new SingleStatCurveConfiguration("Devastate Hit Chance",devHit);
        critChart.addCurve(devastateHitCfg);
        configs.add(critChart);
      }

      // Finesse
      // - finesse => finesse %
      RatingCurve finesse=mgr.getFinesse();
      configs.add(build("Finesse",finesse,105,0,400000));
      // Healing
      // - outgoing healing (~= tactical mastery) => healing %
      RatingCurve healing=mgr.getHealing();
      configs.add(build("Healing",healing,105,0,75000));
      // Incoming healing
      // - incoming healing => incoming healing %
      RatingCurve incomingHealing=mgr.getIncomingHealing();
      configs.add(build("Incoming healing",incomingHealing,105,0,20000));
      // Avoidance
      // Cap ratings are: 20000, 50000, 200000 => 200000
      // - Block
      {
        StatCurvesChartConfiguration blockChart=new StatCurvesChartConfiguration("Block Rating",105,0,200000);
        RatingCurve avoidance=mgr.getAvoidance();
        SingleStatCurveConfiguration fullBlockCfg=new SingleStatCurveConfiguration("Full Block",avoidance);
        blockChart.addCurve(fullBlockCfg);
        RatingCurve partialAvoidance=mgr.getPartialAvoidance();
        SingleStatCurveConfiguration partialBlockCfg=new SingleStatCurveConfiguration("Partial Block",partialAvoidance);
        blockChart.addCurve(partialBlockCfg);
        RatingCurve partialMitigation=mgr.getPartialMitigation();
        SingleStatCurveConfiguration partialMitBlockCfg=new SingleStatCurveConfiguration("Partial Block Mitigation",partialMitigation);
        blockChart.addCurve(partialMitBlockCfg);
        configs.add(blockChart);
      }
      // Same for Parry and Evade

      // Critical defence
      // - critical defence rating => critical defence % (melee/ranged/tactical)
      RatingCurve critDefence=mgr.getCriticalDefence();
      configs.add(build("Critical defence %",critDefence,105,0,12000));
      // Resistance
      // - resistance => resistance %
      RatingCurve resistance=mgr.getResistance();
      configs.add(build("Resistance %",resistance,105,0,45000));
      // Armour mitigation
      // - physical mitigation => physical mitigation %
      // - orc craft/fell wrought mitigation => orc craft/fell wrought mitigation %
      // - tactical mitigation => tactical mitigation %
      // 1) Heavy
      RatingCurve heavyArmorMitigation=mgr.getHeavyArmorMitigation();
      configs.add(build("Heavy armor mitigation",heavyArmorMitigation,105,0,20000));
      // 2) Medium
      RatingCurve mediumArmorMitigation=mgr.getMediumArmorMitigation();
      configs.add(build("Medium armor mitigation",mediumArmorMitigation,105,0,17000));
      // 3) Light
      RatingCurve lightArmorMitigation=mgr.getLightArmorMitigation();
      configs.add(build("Light armor mitigation",lightArmorMitigation,105,0,15000));
    }
    for(StatCurvesChartConfiguration config : configs)
    {
      StatCurveChartPanelController chartController=new StatCurveChartPanelController(config);
      JPanel statPanel=chartController.getPanel();
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
