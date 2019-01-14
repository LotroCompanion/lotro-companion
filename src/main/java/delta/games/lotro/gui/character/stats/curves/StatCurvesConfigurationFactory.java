package delta.games.lotro.gui.character.stats.curves;

import delta.games.lotro.character.CharacterProficiencies;
import delta.games.lotro.character.stats.ratings.RatingCurve;
import delta.games.lotro.character.stats.ratings.RatingsMgr;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.stats.WellKnownStat;
import delta.games.lotro.lore.items.ArmourType;

/**
 * Factory for stat curves configurations.
 * @author DAM
 */
public class StatCurvesConfigurationFactory
{
  private RatingsMgr _ratingsMgr;
  private CharacterClass _characterClass;

  /**
   * Constructor.
   * @param characterClass Targeted character class.
   */
  public StatCurvesConfigurationFactory(CharacterClass characterClass)
  {
    _ratingsMgr=new RatingsMgr();
    _characterClass=characterClass;
  }

  /**
   * Build configuration for the 'physical damage' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildPhysicalDamageChart()
  {
    RatingCurve damage=_ratingsMgr.getDamage();
    StatCurvesChartConfiguration physicalDamageCfg=new StatCurvesChartConfiguration("Physical damage",WellKnownStat.PHYSICAL_MASTERY);
    SingleStatCurveConfiguration physicalDamageCurveCfg=new SingleStatCurveConfiguration("Physical damage",damage);
    physicalDamageCurveCfg.addStat(WellKnownStat.MELEE_DAMAGE_PERCENTAGE);
    physicalDamageCurveCfg.addStat(WellKnownStat.RANGED_DAMAGE_PERCENTAGE);
    physicalDamageCfg.addCurve(physicalDamageCurveCfg);
    return physicalDamageCfg;
  }

  /**
   * Build configuration for the 'tactical damage' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildTacticalDamageChart()
  {
    RatingCurve damage=_ratingsMgr.getDamage();
    StatCurvesChartConfiguration tacticalDamageCfg=new StatCurvesChartConfiguration("Tactical damage",WellKnownStat.TACTICAL_MASTERY);
    SingleStatCurveConfiguration tacticalDamageCurveCfg=new SingleStatCurveConfiguration("Tactical damage",damage);
    tacticalDamageCurveCfg.addStat(WellKnownStat.TACTICAL_DAMAGE_PERCENTAGE);
    tacticalDamageCfg.addCurve(tacticalDamageCurveCfg);
    return tacticalDamageCfg;
  }

  /**
   * Build configuration for the 'criticals' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildCriticalsChart()
  {
    StatCurvesChartConfiguration critChart=new StatCurvesChartConfiguration("Critical Rating",WellKnownStat.CRITICAL_RATING);
    // Magnitude
    RatingCurve critDevHitMagnitude=_ratingsMgr.getCritAndDevastateHitMagnitudeCurve();
    SingleStatCurveConfiguration magnitudeCfg=new SingleStatCurveConfiguration("Critical/devastate magnitude %",critDevHitMagnitude);
    magnitudeCfg.addStat(WellKnownStat.CRIT_DEVASTATE_MAGNITUDE_MELEE_PERCENTAGE);
    magnitudeCfg.addStat(WellKnownStat.CRIT_DEVASTATE_MAGNITUDE_RANGED_PERCENTAGE);
    magnitudeCfg.addStat(WellKnownStat.CRIT_DEVASTATE_MAGNITUDE_TACTICAL_PERCENTAGE);
    critChart.addCurve(magnitudeCfg);
    // Critical hit chance
    RatingCurve critHit=_ratingsMgr.getCriticalHitCurve();
    SingleStatCurveConfiguration critHitCfg=new SingleStatCurveConfiguration("Critical Hit Chance",critHit);
    critHitCfg.addStat(WellKnownStat.CRITICAL_MELEE_PERCENTAGE);
    critHitCfg.addStat(WellKnownStat.CRITICAL_RANGED_PERCENTAGE);
    critHitCfg.addStat(WellKnownStat.CRITICAL_TACTICAL_PERCENTAGE);
    critChart.addCurve(critHitCfg);
    // Devastate hit chance
    RatingCurve devHit=_ratingsMgr.getDevastateHitCurve();
    SingleStatCurveConfiguration devastateHitCfg=new SingleStatCurveConfiguration("Devastate Hit Chance",devHit);
    devastateHitCfg.addStat(WellKnownStat.DEVASTATE_MELEE_PERCENTAGE);
    devastateHitCfg.addStat(WellKnownStat.DEVASTATE_RANGED_PERCENTAGE);
    devastateHitCfg.addStat(WellKnownStat.DEVASTATE_TACTICAL_PERCENTAGE);
    critChart.addCurve(devastateHitCfg);
    return critChart;
  }

  /**
   * Build configuration for the 'finesse' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildFinesseChart()
  {
    RatingCurve finesse=_ratingsMgr.getFinesse();
    StatCurvesChartConfiguration finesseCfg=new StatCurvesChartConfiguration("Finesse",WellKnownStat.FINESSE);
    SingleStatCurveConfiguration finesseCurveCfg=new SingleStatCurveConfiguration("Finesse",finesse);
    finesseCurveCfg.addStat(WellKnownStat.FINESSE_PERCENTAGE);
    finesseCfg.addCurve(finesseCurveCfg);
    return finesseCfg;
  }

  /**
   * Build configuration for the 'healing' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildHealingChart()
  {
    RatingCurve healing=_ratingsMgr.getHealing();
    StatCurvesChartConfiguration healingCfg=new StatCurvesChartConfiguration("Healing",WellKnownStat.OUTGOING_HEALING);
    SingleStatCurveConfiguration healingCurveCfg=new SingleStatCurveConfiguration("Healing",healing);
    healingCurveCfg.addStat(WellKnownStat.OUTGOING_HEALING_PERCENTAGE);
    healingCfg.addCurve(healingCurveCfg);
    return healingCfg;
  }

  /**
   * Build configuration for the 'incoming healing' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildIncomingHealingChart()
  {
    RatingCurve incomingHealing=_ratingsMgr.getIncomingHealing();
    StatCurvesChartConfiguration incomingHealingCfg=new StatCurvesChartConfiguration("Incoming Healing",WellKnownStat.INCOMING_HEALING);
    SingleStatCurveConfiguration incomingHealingCurveCfg=new SingleStatCurveConfiguration("Incoming Healing",incomingHealing);
    incomingHealingCurveCfg.addStat(WellKnownStat.INCOMING_HEALING_PERCENTAGE);
    incomingHealingCfg.addCurve(incomingHealingCurveCfg);
    return incomingHealingCfg;
  }

  /**
   * Build configuration for the 'block' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildBlockChart()
  {
    StatCurvesChartConfiguration blockChart=new StatCurvesChartConfiguration("Block",WellKnownStat.BLOCK);
    RatingCurve avoidance=_ratingsMgr.getAvoidance();
    SingleStatCurveConfiguration fullBlockCfg=new SingleStatCurveConfiguration("Full Block",avoidance);
    fullBlockCfg.addStat(WellKnownStat.BLOCK_PERCENTAGE);
    blockChart.addCurve(fullBlockCfg);
    RatingCurve partialAvoidance=_ratingsMgr.getPartialAvoidance();
    SingleStatCurveConfiguration partialBlockCfg=new SingleStatCurveConfiguration("Partial Block",partialAvoidance);
    partialBlockCfg.addStat(WellKnownStat.PARTIAL_BLOCK_PERCENTAGE);
    blockChart.addCurve(partialBlockCfg);
    RatingCurve partialMitigation=_ratingsMgr.getPartialMitigation();
    SingleStatCurveConfiguration partialMitBlockCfg=new SingleStatCurveConfiguration("Partial Block Mitigation",partialMitigation);
    partialMitBlockCfg.addStat(WellKnownStat.PARTIAL_BLOCK_MITIGATION_PERCENTAGE);
    blockChart.addCurve(partialMitBlockCfg);
    return blockChart;
  }

  /**
   * Build configuration for the 'parry' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildParryChart()
  {
    StatCurvesChartConfiguration parryChart=new StatCurvesChartConfiguration("Parry",WellKnownStat.PARRY);
    RatingCurve avoidance=_ratingsMgr.getAvoidance();
    SingleStatCurveConfiguration fullParryCfg=new SingleStatCurveConfiguration("Full Parry",avoidance);
    fullParryCfg.addStat(WellKnownStat.PARRY_PERCENTAGE);
    parryChart.addCurve(fullParryCfg);
    RatingCurve partialAvoidance=_ratingsMgr.getPartialAvoidance();
    SingleStatCurveConfiguration partialParryCfg=new SingleStatCurveConfiguration("Partial Parry",partialAvoidance);
    partialParryCfg.addStat(WellKnownStat.PARTIAL_PARRY_PERCENTAGE);
    parryChart.addCurve(partialParryCfg);
    RatingCurve partialMitigation=_ratingsMgr.getPartialMitigation();
    SingleStatCurveConfiguration partialMitParryCfg=new SingleStatCurveConfiguration("Partial Parry Mitigation",partialMitigation);
    partialMitParryCfg.addStat(WellKnownStat.PARTIAL_PARRY_MITIGATION_PERCENTAGE);
    parryChart.addCurve(partialMitParryCfg);
    return parryChart;
  }

  /**
   * Build configuration for the 'evade' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildEvadeChart()
  {
    StatCurvesChartConfiguration evadeChart=new StatCurvesChartConfiguration("Evade",WellKnownStat.EVADE);
    RatingCurve avoidance=_ratingsMgr.getAvoidance();
    SingleStatCurveConfiguration fullEvadeCfg=new SingleStatCurveConfiguration("Full Evade",avoidance);
    fullEvadeCfg.addStat(WellKnownStat.EVADE_PERCENTAGE);
    evadeChart.addCurve(fullEvadeCfg);
    RatingCurve partialAvoidance=_ratingsMgr.getPartialAvoidance();
    SingleStatCurveConfiguration partialEvadeCfg=new SingleStatCurveConfiguration("Partial Evade",partialAvoidance);
    partialEvadeCfg.addStat(WellKnownStat.PARTIAL_EVADE_PERCENTAGE);
    evadeChart.addCurve(partialEvadeCfg);
    RatingCurve partialMitigation=_ratingsMgr.getPartialMitigation();
    SingleStatCurveConfiguration partialMitEvadeCfg=new SingleStatCurveConfiguration("Partial Evade Mitigation",partialMitigation);
    partialMitEvadeCfg.addStat(WellKnownStat.PARTIAL_EVADE_MITIGATION_PERCENTAGE);
    evadeChart.addCurve(partialMitEvadeCfg);
    return evadeChart;
  }

  /**
   * Build configuration for the 'resistance' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildResistanceChart()
  {
    RatingCurve resistance=_ratingsMgr.getResistance();
    StatCurvesChartConfiguration resistanceCfg=new StatCurvesChartConfiguration("Resistance",WellKnownStat.RESISTANCE);
    SingleStatCurveConfiguration resistanceCurveCfg=new SingleStatCurveConfiguration("Resistance",resistance);
    resistanceCurveCfg.addStat(WellKnownStat.RESISTANCE_PERCENTAGE);
    resistanceCfg.addCurve(resistanceCurveCfg);
    return resistanceCfg;
  }

  /**
   * Build configuration for the 'critical defence' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildCriticalDefenceChart()
  {
    RatingCurve critDefence=_ratingsMgr.getCriticalDefence();
    StatCurvesChartConfiguration criticalDefenceCfg=new StatCurvesChartConfiguration("Critical Defence",WellKnownStat.CRITICAL_DEFENCE);
    SingleStatCurveConfiguration criticalDefenceCurveCfg=new SingleStatCurveConfiguration("Critical Defence",critDefence);
    criticalDefenceCurveCfg.addStat(WellKnownStat.MELEE_CRITICAL_DEFENCE);
    criticalDefenceCurveCfg.addStat(WellKnownStat.RANGED_CRITICAL_DEFENCE);
    criticalDefenceCurveCfg.addStat(WellKnownStat.TACTICAL_CRITICAL_DEFENCE);
    criticalDefenceCfg.addCurve(criticalDefenceCurveCfg);
    return criticalDefenceCfg;
  }

  private RatingCurve getArmorMitigationCurve()
  {
    ArmourType type=CharacterProficiencies.getArmourTypeForMitigations(_characterClass);
    if (type==ArmourType.LIGHT) return _ratingsMgr.getLightArmorMitigation();
    if (type==ArmourType.MEDIUM) return _ratingsMgr.getMediumArmorMitigation();
    if (type==ArmourType.HEAVY) return _ratingsMgr.getHeavyArmorMitigation();
    return null;
  }

  /**
   * Build configuration for the 'physical mitigation' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildPhysicalMitigationChart()
  {
    RatingCurve armorMitigation=getArmorMitigationCurve();
    StatCurvesChartConfiguration mitigationCfg=new StatCurvesChartConfiguration("Physical Mitigation",WellKnownStat.PHYSICAL_MITIGATION);
    SingleStatCurveConfiguration mitigationCurveCfg=new SingleStatCurveConfiguration("Physical Mitigation",armorMitigation);
    mitigationCurveCfg.addStat(WellKnownStat.PHYSICAL_MITIGATION_PERCENTAGE);
    mitigationCfg.addCurve(mitigationCurveCfg);
    return mitigationCfg;
  }

  /**
   * Build configuration for the 'tactical mitigation' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildTacticalMitigationChart()
  {
    RatingCurve armorMitigation=getArmorMitigationCurve();
    StatCurvesChartConfiguration mitigationCfg=new StatCurvesChartConfiguration("Tactical Mitigation",WellKnownStat.TACTICAL_MITIGATION);
    SingleStatCurveConfiguration mitigationCurveCfg=new SingleStatCurveConfiguration("Tactical Mitigation",armorMitigation);
    mitigationCurveCfg.addStat(WellKnownStat.TACTICAL_MITIGATION_PERCENTAGE);
    mitigationCurveCfg.addStat(WellKnownStat.FIRE_MITIGATION_PERCENTAGE);
    mitigationCurveCfg.addStat(WellKnownStat.LIGHTNING_MITIGATION_PERCENTAGE);
    mitigationCurveCfg.addStat(WellKnownStat.FROST_MITIGATION_PERCENTAGE);
    mitigationCurveCfg.addStat(WellKnownStat.ACID_MITIGATION_PERCENTAGE);
    mitigationCurveCfg.addStat(WellKnownStat.SHADOW_MITIGATION_PERCENTAGE);
    mitigationCfg.addCurve(mitigationCurveCfg);
    return mitigationCfg;
  }

  /**
   * Build configuration for the 'orc craft/fell wrought mitigation' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildOrcCraftFellWroughtMitigationChart()
  {
    RatingCurve armorMitigation=getArmorMitigationCurve();
    StatCurvesChartConfiguration mitigationCfg=new StatCurvesChartConfiguration("Orc Craft/Fell Wrought Mitigation",WellKnownStat.OCFW_MITIGATION);
    SingleStatCurveConfiguration mitigationCurveCfg=new SingleStatCurveConfiguration("Orc Craft/Fell Wrought Mitigation",armorMitigation);
    mitigationCurveCfg.addStat(WellKnownStat.OCFW_MITIGATION_PERCENTAGE);
    mitigationCfg.addCurve(mitigationCurveCfg);
    return mitigationCfg;
  }
}
