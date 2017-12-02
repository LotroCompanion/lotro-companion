package delta.games.lotro.gui.character.stats.curves;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.CharacterGenerationTools;
import delta.games.lotro.character.stats.CharacterGeneratorGiswald;
import delta.games.lotro.character.stats.CharacterGeneratorMeva;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.character.stats.STAT;
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
    CharacterGeneratorMeva generator=new CharacterGeneratorMeva(tools);
    CharacterData c=generator.buildCharacter();
    doIt(c);
  }

  private void doIt(CharacterData data)
  {
    // Load toon stats
    CharacterGenerationTools tools=new CharacterGenerationTools();
    CharacterGeneratorGiswald generator=new CharacterGeneratorGiswald(tools);
    CharacterData c=generator.buildCharacter();
    CharacterStatsComputer statsComputer=new CharacterStatsComputer();
    BasicStatsSet stats=statsComputer.getStats(c);
    int level=c.getLevel();

    RatingsMgr mgr=new RatingsMgr();
    List<StatCurvesChartConfiguration> configs=new ArrayList<StatCurvesChartConfiguration>();
    {
      // Damage
      RatingCurve damage=mgr.getDamage();
      // - Physical damage
      {
        StatCurvesChartConfiguration physicalDamageCfg=new StatCurvesChartConfiguration("Physical damage",level,0,200000,STAT.PHYSICAL_MASTERY);
        SingleStatCurveConfiguration physicalDamageCurveCfg=new SingleStatCurveConfiguration("Physical damage",damage);
        physicalDamageCurveCfg.addStat(STAT.MELEE_DAMAGE_PERCENTAGE);
        physicalDamageCurveCfg.addStat(STAT.RANGED_DAMAGE_PERCENTAGE);
        physicalDamageCfg.addCurve(physicalDamageCurveCfg);
        configs.add(physicalDamageCfg);
      }
      // - Tactical damage
      {
        StatCurvesChartConfiguration tacticalDamageCfg=new StatCurvesChartConfiguration("Tactical damage",level,0,200000,STAT.TACTICAL_MASTERY);
        SingleStatCurveConfiguration tacticalDamageCurveCfg=new SingleStatCurveConfiguration("Tactical damage",damage);
        tacticalDamageCurveCfg.addStat(STAT.TACTICAL_DAMAGE_PERCENTAGE);
        tacticalDamageCfg.addCurve(tacticalDamageCurveCfg);
        configs.add(tacticalDamageCfg);
      }
      // - Critical hit
      {
        // Cap ratings are: 22000, 22000, 35000 => 35000
        StatCurvesChartConfiguration critChart=new StatCurvesChartConfiguration("Critical Rating",level,0,35000,STAT.CRITICAL_RATING);
        // Magnitude
        RatingCurve critDevHitMagnitude=mgr.getCritAndDevastateHitMagnitudeCurve();
        SingleStatCurveConfiguration magnitudeCfg=new SingleStatCurveConfiguration("Critical/devastate magnitude %",critDevHitMagnitude);
        magnitudeCfg.addStat(STAT.CRIT_DEVASTATE_MAGNITUDE_MELEE_PERCENTAGE);
        magnitudeCfg.addStat(STAT.CRIT_DEVASTATE_MAGNITUDE_RANGED_PERCENTAGE);
        magnitudeCfg.addStat(STAT.CRIT_DEVASTATE_MAGNITUDE_TACTICAL_PERCENTAGE);
        critChart.addCurve(magnitudeCfg);
        // Critical hit chance
        RatingCurve critHit=mgr.getCriticalHitCurve();
        SingleStatCurveConfiguration critHitCfg=new SingleStatCurveConfiguration("Critical Hit Chance",critHit);
        critHitCfg.addStat(STAT.CRITICAL_MELEE_PERCENTAGE);
        critHitCfg.addStat(STAT.CRITICAL_RANGED_PERCENTAGE);
        critHitCfg.addStat(STAT.CRITICAL_TACTICAL_PERCENTAGE);
        critChart.addCurve(critHitCfg);
        // Devastate hit chance
        RatingCurve devHit=mgr.getDevastateHitCurve();
        SingleStatCurveConfiguration devastateHitCfg=new SingleStatCurveConfiguration("Devastate Hit Chance",devHit);
        devastateHitCfg.addStat(STAT.DEVASTATE_MELEE_PERCENTAGE);
        devastateHitCfg.addStat(STAT.DEVASTATE_RANGED_PERCENTAGE);
        devastateHitCfg.addStat(STAT.DEVASTATE_TACTICAL_PERCENTAGE);
        critChart.addCurve(devastateHitCfg);
        configs.add(critChart);
      }
      // - Finesse
      {
        RatingCurve finesse=mgr.getFinesse();
        StatCurvesChartConfiguration finesseCfg=new StatCurvesChartConfiguration("Finesse",level,0,400000,STAT.FINESSE);
        SingleStatCurveConfiguration finesseCurveCfg=new SingleStatCurveConfiguration("Finesse",finesse);
        finesseCurveCfg.addStat(STAT.FINESSE_PERCENTAGE);
        finesseCfg.addCurve(finesseCurveCfg);
        configs.add(finesseCfg);
      }
      // Healing
      // - Outgoing Healing (~= tactical mastery) => healing %
      {
        RatingCurve healing=mgr.getHealing();
        StatCurvesChartConfiguration healingCfg=new StatCurvesChartConfiguration("Healing",level,0,75000,STAT.OUTGOING_HEALING);
        SingleStatCurveConfiguration healingCurveCfg=new SingleStatCurveConfiguration("Healing",healing);
        healingCurveCfg.addStat(STAT.OUTGOING_HEALING_PERCENTAGE);
        healingCfg.addCurve(healingCurveCfg);
        configs.add(healingCfg);
      }
      // - Incoming Healing
      {
        RatingCurve incomingHealing=mgr.getIncomingHealing();
        StatCurvesChartConfiguration incomingHealingCfg=new StatCurvesChartConfiguration("Incoming Healing",level,0,20000,STAT.INCOMING_HEALING);
        SingleStatCurveConfiguration incomingHealingCurveCfg=new SingleStatCurveConfiguration("Incoming Healing",incomingHealing);
        incomingHealingCurveCfg.addStat(STAT.INCOMING_HEALING_PERCENTAGE);
        incomingHealingCfg.addCurve(incomingHealingCurveCfg);
        configs.add(incomingHealingCfg);
      }
      // Avoidance
      // Cap ratings are: 20000, 50000, 200000 => 200000
      // - Block
      {
        StatCurvesChartConfiguration blockChart=new StatCurvesChartConfiguration("Block Rating",level,0,200000,STAT.BLOCK);
        RatingCurve avoidance=mgr.getAvoidance();
        SingleStatCurveConfiguration fullBlockCfg=new SingleStatCurveConfiguration("Full Block",avoidance);
        fullBlockCfg.addStat(STAT.BLOCK_PERCENTAGE);
        blockChart.addCurve(fullBlockCfg);
        RatingCurve partialAvoidance=mgr.getPartialAvoidance();
        SingleStatCurveConfiguration partialBlockCfg=new SingleStatCurveConfiguration("Partial Block",partialAvoidance);
        partialBlockCfg.addStat(STAT.PARTIAL_BLOCK_PERCENTAGE);
        blockChart.addCurve(partialBlockCfg);
        RatingCurve partialMitigation=mgr.getPartialMitigation();
        SingleStatCurveConfiguration partialMitBlockCfg=new SingleStatCurveConfiguration("Partial Block Mitigation",partialMitigation);
        partialMitBlockCfg.addStat(STAT.PARTIAL_BLOCK_MITIGATION_PERCENTAGE);
        blockChart.addCurve(partialMitBlockCfg);
        configs.add(blockChart);
      }
      // - Parry
      {
        StatCurvesChartConfiguration parryChart=new StatCurvesChartConfiguration("Parry Rating",level,0,200000,STAT.PARRY);
        RatingCurve avoidance=mgr.getAvoidance();
        SingleStatCurveConfiguration fullParryCfg=new SingleStatCurveConfiguration("Full Parry",avoidance);
        fullParryCfg.addStat(STAT.PARRY_PERCENTAGE);
        parryChart.addCurve(fullParryCfg);
        RatingCurve partialAvoidance=mgr.getPartialAvoidance();
        SingleStatCurveConfiguration partialParryCfg=new SingleStatCurveConfiguration("Partial Parry",partialAvoidance);
        partialParryCfg.addStat(STAT.PARTIAL_PARRY_PERCENTAGE);
        parryChart.addCurve(partialParryCfg);
        RatingCurve partialMitigation=mgr.getPartialMitigation();
        SingleStatCurveConfiguration partialMitParryCfg=new SingleStatCurveConfiguration("Partial Parry Mitigation",partialMitigation);
        partialMitParryCfg.addStat(STAT.PARTIAL_PARRY_MITIGATION_PERCENTAGE);
        parryChart.addCurve(partialMitParryCfg);
        configs.add(parryChart);
      }
      // - Evade
      {
        StatCurvesChartConfiguration evadeChart=new StatCurvesChartConfiguration("Evade Rating",level,0,200000,STAT.EVADE);
        RatingCurve avoidance=mgr.getAvoidance();
        SingleStatCurveConfiguration fullEvadeCfg=new SingleStatCurveConfiguration("Full Evade",avoidance);
        fullEvadeCfg.addStat(STAT.EVADE_PERCENTAGE);
        evadeChart.addCurve(fullEvadeCfg);
        RatingCurve partialAvoidance=mgr.getPartialAvoidance();
        SingleStatCurveConfiguration partialEvadeCfg=new SingleStatCurveConfiguration("Partial Evade",partialAvoidance);
        partialEvadeCfg.addStat(STAT.PARTIAL_EVADE_PERCENTAGE);
        evadeChart.addCurve(partialEvadeCfg);
        RatingCurve partialMitigation=mgr.getPartialMitigation();
        SingleStatCurveConfiguration partialMitEvadeCfg=new SingleStatCurveConfiguration("Partial Evade Mitigation",partialMitigation);
        partialMitEvadeCfg.addStat(STAT.PARTIAL_EVADE_MITIGATION_PERCENTAGE);
        evadeChart.addCurve(partialMitEvadeCfg);
        configs.add(evadeChart);
      }
      // - Resistance
      {
        RatingCurve resistance=mgr.getResistance();
        StatCurvesChartConfiguration resistanceCfg=new StatCurvesChartConfiguration("Resistance",level,0,45000,STAT.RESISTANCE);
        SingleStatCurveConfiguration resistanceCurveCfg=new SingleStatCurveConfiguration("Resistance",resistance);
        resistanceCurveCfg.addStat(STAT.RESISTANCE_PERCENTAGE);
        resistanceCfg.addCurve(resistanceCurveCfg);
        configs.add(resistanceCfg);
      }

      // Mitigation
      // - Critical defence
      {
        RatingCurve critDefence=mgr.getCriticalDefence();
        StatCurvesChartConfiguration criticalDefenceCfg=new StatCurvesChartConfiguration("Critical Defence",level,0,12000,STAT.CRITICAL_DEFENCE);
        SingleStatCurveConfiguration criticalDefenceCurveCfg=new SingleStatCurveConfiguration("Critical Defence",critDefence);
        criticalDefenceCurveCfg.addStat(STAT.MELEE_CRITICAL_DEFENCE);
        criticalDefenceCurveCfg.addStat(STAT.RANGED_CRITICAL_DEFENCE);
        criticalDefenceCurveCfg.addStat(STAT.TACTICAL_CRITICAL_DEFENCE);
        criticalDefenceCfg.addCurve(criticalDefenceCurveCfg);
        configs.add(criticalDefenceCfg);
      }
      // Choose armor: maxRating=20000/17000/15000
      RatingCurve heavyArmorMitigation=mgr.getHeavyArmorMitigation();
      //RatingCurve mediumArmorMitigation=mgr.getMediumArmorMitigation();
      //RatingCurve lightArmorMitigation=mgr.getLightArmorMitigation();
      RatingCurve armorMitigation=heavyArmorMitigation;
      // - Physical Mitigation
      {
        StatCurvesChartConfiguration mitigationCfg=new StatCurvesChartConfiguration("Physical Mitigation",level,0,20000,STAT.PHYSICAL_MITIGATION);
        SingleStatCurveConfiguration mitigationCurveCfg=new SingleStatCurveConfiguration("Physical Mitigation",armorMitigation);
        mitigationCurveCfg.addStat(STAT.PHYSICAL_MITIGATION_PERCENTAGE);
        mitigationCfg.addCurve(mitigationCurveCfg);
        configs.add(mitigationCfg);
      }
      // - Tactical Mitigation
      {
        StatCurvesChartConfiguration mitigationCfg=new StatCurvesChartConfiguration("Tactical Mitigation",level,0,20000,STAT.TACTICAL_MITIGATION);
        SingleStatCurveConfiguration mitigationCurveCfg=new SingleStatCurveConfiguration("Tactical Mitigation",armorMitigation);
        mitigationCurveCfg.addStat(STAT.TACTICAL_MITIGATION_PERCENTAGE);
        mitigationCurveCfg.addStat(STAT.FIRE_MITIGATION_PERCENTAGE);
        mitigationCurveCfg.addStat(STAT.LIGHTNING_MITIGATION_PERCENTAGE);
        mitigationCurveCfg.addStat(STAT.FROST_MITIGATION_PERCENTAGE);
        mitigationCurveCfg.addStat(STAT.ACID_MITIGATION_PERCENTAGE);
        mitigationCurveCfg.addStat(STAT.SHADOW_MITIGATION_PERCENTAGE);
        mitigationCfg.addCurve(mitigationCurveCfg);
        configs.add(mitigationCfg);
      }
      // - Orc craft/Fell wrought Mitigation
      {
        StatCurvesChartConfiguration mitigationCfg=new StatCurvesChartConfiguration("Orc Craft/Fell Wrought Mitigation",level,0,20000,STAT.OCFW_MITIGATION);
        SingleStatCurveConfiguration mitigationCurveCfg=new SingleStatCurveConfiguration("Orc Craft/Fell Wrought Mitigation",armorMitigation);
        mitigationCurveCfg.addStat(STAT.OCFW_MITIGATION_PERCENTAGE);
        mitigationCfg.addCurve(mitigationCurveCfg);
        configs.add(mitigationCfg);
      }
    }
    for(StatCurvesChartConfiguration config : configs)
    {
      StatCurvesPanelController controller=new StatCurvesPanelController(config);
      controller.update(stats);
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
