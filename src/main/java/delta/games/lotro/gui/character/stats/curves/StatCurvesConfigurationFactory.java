package delta.games.lotro.gui.character.stats.curves;

import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.stats.ratings.RatingCurve;
import delta.games.lotro.character.stats.ratings.RatingCurveId;
import delta.games.lotro.character.stats.ratings.RatingsMgr;
import delta.games.lotro.common.global.CombatSystem;
import delta.games.lotro.common.stats.WellKnownStat;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.ArmourTypes;

/**
 * Factory for stat curves configurations.
 * @author DAM
 */
public class StatCurvesConfigurationFactory
{
  private RatingsMgr _ratingsMgr;
  private ClassDescription _characterClass;

  /**
   * Constructor.
   * @param characterClass Targeted character class.
   */
  public StatCurvesConfigurationFactory(ClassDescription characterClass)
  {
    _ratingsMgr=CombatSystem.getInstance().getRatingsMgr();
    _characterClass=characterClass;
  }

  /**
   * Build configuration for the 'physical damage' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildPhysicalDamageChart()
  {
    RatingCurve damage=_ratingsMgr.getCurve(RatingCurveId.DAMAGE);
    StatCurvesChartConfiguration physicalDamageCfg=new StatCurvesChartConfiguration("Physical damage",WellKnownStat.PHYSICAL_MASTERY); // I18n
    SingleStatCurveConfiguration physicalDamageCurveCfg=new SingleStatCurveConfiguration("Physical damage",damage); // I18n
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
    RatingCurve damage=_ratingsMgr.getCurve(RatingCurveId.DAMAGE);
    StatCurvesChartConfiguration tacticalDamageCfg=new StatCurvesChartConfiguration("Tactical damage",WellKnownStat.TACTICAL_MASTERY); // I18n
    SingleStatCurveConfiguration tacticalDamageCurveCfg=new SingleStatCurveConfiguration("Tactical damage",damage); // I18n
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
    StatCurvesChartConfiguration critChart=new StatCurvesChartConfiguration("Critical Rating",WellKnownStat.CRITICAL_RATING); // I18n
    // Magnitude
    RatingCurve critDevHitMagnitude=_ratingsMgr.getCurve(RatingCurveId.CRIT_DEVASTATE_MAGNITUDE);
    SingleStatCurveConfiguration magnitudeCfg=new SingleStatCurveConfiguration("Critical/devastate magnitude %",critDevHitMagnitude); // I18n
    magnitudeCfg.addStat(WellKnownStat.CRIT_DEVASTATE_MAGNITUDE_MELEE_PERCENTAGE);
    magnitudeCfg.addStat(WellKnownStat.CRIT_DEVASTATE_MAGNITUDE_RANGED_PERCENTAGE);
    magnitudeCfg.addStat(WellKnownStat.CRIT_DEVASTATE_MAGNITUDE_TACTICAL_PERCENTAGE);
    critChart.addCurve(magnitudeCfg);
    // Critical hit chance
    RatingCurve critHit=_ratingsMgr.getCurve(RatingCurveId.CRITICAL_HIT);
    SingleStatCurveConfiguration critHitCfg=new SingleStatCurveConfiguration("Critical Hit Chance",critHit); // I18n
    critHitCfg.addStat(WellKnownStat.CRITICAL_MELEE_PERCENTAGE);
    critHitCfg.addStat(WellKnownStat.CRITICAL_RANGED_PERCENTAGE);
    critHitCfg.addStat(WellKnownStat.CRITICAL_TACTICAL_PERCENTAGE);
    critChart.addCurve(critHitCfg);
    // Devastate hit chance
    RatingCurve devHit=_ratingsMgr.getCurve(RatingCurveId.DEVASTATE_HIT);
    SingleStatCurveConfiguration devastateHitCfg=new SingleStatCurveConfiguration("Devastate Hit Chance",devHit); // I18n
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
    RatingCurve finesse=_ratingsMgr.getCurve(RatingCurveId.FINESSE);
    StatCurvesChartConfiguration finesseCfg=new StatCurvesChartConfiguration("Finesse",WellKnownStat.FINESSE); // I18n
    SingleStatCurveConfiguration finesseCurveCfg=new SingleStatCurveConfiguration("Finesse",finesse); // I18n
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
    RatingCurve healing=_ratingsMgr.getCurve(RatingCurveId.HEALING);
    StatCurvesChartConfiguration healingCfg=new StatCurvesChartConfiguration("Healing",WellKnownStat.OUTGOING_HEALING); // I18n
    SingleStatCurveConfiguration healingCurveCfg=new SingleStatCurveConfiguration("Healing",healing); // I18n
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
    RatingCurve incomingHealing=_ratingsMgr.getCurve(RatingCurveId.INCOMING_HEALING);
    StatCurvesChartConfiguration incomingHealingCfg=new StatCurvesChartConfiguration("Incoming Healing",WellKnownStat.INCOMING_HEALING); // I18n
    SingleStatCurveConfiguration incomingHealingCurveCfg=new SingleStatCurveConfiguration("Incoming Healing",incomingHealing); // I18n
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
    StatCurvesChartConfiguration blockChart=new StatCurvesChartConfiguration("Block",WellKnownStat.BLOCK); // I18n
    RatingCurve avoidance=_ratingsMgr.getCurve(RatingCurveId.AVOIDANCE);
    SingleStatCurveConfiguration fullBlockCfg=new SingleStatCurveConfiguration("Full Block",avoidance); // I18n
    fullBlockCfg.addStat(WellKnownStat.BLOCK_PERCENTAGE);
    blockChart.addCurve(fullBlockCfg);
    RatingCurve partialAvoidance=_ratingsMgr.getCurve(RatingCurveId.PARTIAL_AVOIDANCE);
    SingleStatCurveConfiguration partialBlockCfg=new SingleStatCurveConfiguration("Partial Block",partialAvoidance); // I18n
    partialBlockCfg.addStat(WellKnownStat.PARTIAL_BLOCK_PERCENTAGE);
    blockChart.addCurve(partialBlockCfg);
    RatingCurve partialMitigation=_ratingsMgr.getCurve(RatingCurveId.PARTIAL_MITIGATION);
    SingleStatCurveConfiguration partialMitBlockCfg=new SingleStatCurveConfiguration("Partial Block Mitigation",partialMitigation); // I18n
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
    StatCurvesChartConfiguration parryChart=new StatCurvesChartConfiguration("Parry",WellKnownStat.PARRY); // I18n
    RatingCurve avoidance=_ratingsMgr.getCurve(RatingCurveId.AVOIDANCE);
    SingleStatCurveConfiguration fullParryCfg=new SingleStatCurveConfiguration("Full Parry",avoidance); // I18n
    fullParryCfg.addStat(WellKnownStat.PARRY_PERCENTAGE);
    parryChart.addCurve(fullParryCfg);
    RatingCurve partialAvoidance=_ratingsMgr.getCurve(RatingCurveId.PARTIAL_AVOIDANCE);
    SingleStatCurveConfiguration partialParryCfg=new SingleStatCurveConfiguration("Partial Parry",partialAvoidance); // I18n
    partialParryCfg.addStat(WellKnownStat.PARTIAL_PARRY_PERCENTAGE);
    parryChart.addCurve(partialParryCfg);
    RatingCurve partialMitigation=_ratingsMgr.getCurve(RatingCurveId.PARTIAL_MITIGATION);
    SingleStatCurveConfiguration partialMitParryCfg=new SingleStatCurveConfiguration("Partial Parry Mitigation",partialMitigation); // I18n
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
    StatCurvesChartConfiguration evadeChart=new StatCurvesChartConfiguration("Evade",WellKnownStat.EVADE); // I18n
    RatingCurve avoidance=_ratingsMgr.getCurve(RatingCurveId.AVOIDANCE);
    SingleStatCurveConfiguration fullEvadeCfg=new SingleStatCurveConfiguration("Full Evade",avoidance); // I18n
    fullEvadeCfg.addStat(WellKnownStat.EVADE_PERCENTAGE);
    evadeChart.addCurve(fullEvadeCfg);
    RatingCurve partialAvoidance=_ratingsMgr.getCurve(RatingCurveId.PARTIAL_AVOIDANCE);
    SingleStatCurveConfiguration partialEvadeCfg=new SingleStatCurveConfiguration("Partial Evade",partialAvoidance); // I18n
    partialEvadeCfg.addStat(WellKnownStat.PARTIAL_EVADE_PERCENTAGE);
    evadeChart.addCurve(partialEvadeCfg);
    RatingCurve partialMitigation=_ratingsMgr.getCurve(RatingCurveId.PARTIAL_MITIGATION);
    SingleStatCurveConfiguration partialMitEvadeCfg=new SingleStatCurveConfiguration("Partial Evade Mitigation",partialMitigation); // I18n
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
    RatingCurve resistance=_ratingsMgr.getCurve(RatingCurveId.RESISTANCE);
    StatCurvesChartConfiguration resistanceCfg=new StatCurvesChartConfiguration("Resistance",WellKnownStat.RESISTANCE); // I18n
    SingleStatCurveConfiguration resistanceCurveCfg=new SingleStatCurveConfiguration("Resistance",resistance); // I18n
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
    RatingCurve critDefence=_ratingsMgr.getCurve(RatingCurveId.CRITICAL_DEFENCE);
    StatCurvesChartConfiguration criticalDefenceCfg=new StatCurvesChartConfiguration("Critical Defence",WellKnownStat.CRITICAL_DEFENCE); // I18n
    SingleStatCurveConfiguration criticalDefenceCurveCfg=new SingleStatCurveConfiguration("Critical Defence",critDefence); // I18n
    criticalDefenceCurveCfg.addStat(WellKnownStat.MELEE_CRITICAL_DEFENCE);
    criticalDefenceCurveCfg.addStat(WellKnownStat.RANGED_CRITICAL_DEFENCE);
    criticalDefenceCurveCfg.addStat(WellKnownStat.TACTICAL_CRITICAL_DEFENCE);
    criticalDefenceCfg.addCurve(criticalDefenceCurveCfg);
    return criticalDefenceCfg;
  }

  private RatingCurve getArmorMitigationCurve()
  {
    ArmourType type=_characterClass.getProficiencies().getArmourTypeForMitigations();
    if (type==ArmourTypes.LIGHT) return _ratingsMgr.getCurve(RatingCurveId.LIGHT_MITIGATION);
    if (type==ArmourTypes.MEDIUM) return _ratingsMgr.getCurve(RatingCurveId.MEDIUM_MITIGATION);
    if (type==ArmourTypes.HEAVY) return _ratingsMgr.getCurve(RatingCurveId.HEAVY_MITIGATION);
    return null;
  }

  /**
   * Build configuration for the 'physical mitigation' chart.
   * @return A chart configuration.
   */
  public StatCurvesChartConfiguration buildPhysicalMitigationChart()
  {
    RatingCurve armorMitigation=getArmorMitigationCurve();
    StatCurvesChartConfiguration mitigationCfg=new StatCurvesChartConfiguration("Physical Mitigation",WellKnownStat.PHYSICAL_MITIGATION); // I18n
    SingleStatCurveConfiguration mitigationCurveCfg=new SingleStatCurveConfiguration("Physical Mitigation",armorMitigation); // I18n
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
    StatCurvesChartConfiguration mitigationCfg=new StatCurvesChartConfiguration("Tactical Mitigation",WellKnownStat.TACTICAL_MITIGATION); // I18n
    SingleStatCurveConfiguration mitigationCurveCfg=new SingleStatCurveConfiguration("Tactical Mitigation",armorMitigation); // I18n
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
    StatCurvesChartConfiguration mitigationCfg=new StatCurvesChartConfiguration("Orc Craft/Fell Wrought Mitigation",WellKnownStat.OCFW_MITIGATION); // I18n
    SingleStatCurveConfiguration mitigationCurveCfg=new SingleStatCurveConfiguration("Orc Craft/Fell Wrought Mitigation",armorMitigation); // I18n
    mitigationCurveCfg.addStat(WellKnownStat.OCFW_MITIGATION_PERCENTAGE);
    mitigationCfg.addCurve(mitigationCurveCfg);
    return mitigationCfg;
  }
}
