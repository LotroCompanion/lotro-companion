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

  private void doIt(CharacterData data)
  {
    RatingsMgr mgr=new RatingsMgr();
    List<StatCurveConfiguration> configs=new ArrayList<StatCurveConfiguration>();
    {
      // Damage
      // - physical mastery => physical damage
      // - tactical mastery => tactical damage
      RatingCurve damage=mgr.getDamage();
      configs.add(new StatCurveConfiguration("Physical/tactical damage",damage,105,0,200000));
      // Critical hit
      // - critical rating => critical hit chance (melee/ranged/tactical)
      RatingCurve critHit=mgr.getCriticalHitCurve();
      configs.add(new StatCurveConfiguration("Critical hit chance",critHit,105,0,22000));
      // Devastate hit
      // - critical rating => devastate hit chance (melee/ranged/tactical)
      RatingCurve devHit=mgr.getDevastateHitCurve();
      configs.add(new StatCurveConfiguration("Devastate hit chance",devHit,105,0,22000));
      // Crit/devastate hit magnitude
      // - critical rating => crit/devastate hit magnitude % (melee/ranged/tactical)
      RatingCurve critDevHitMagnitude=mgr.getCritAndDevastateHitMagnitudeCurve();
      configs.add(new StatCurveConfiguration("Critical/devastate magnitude",critDevHitMagnitude,105,0,35000));
      // Finesse
      // - finesse => finesse %
      RatingCurve finesse=mgr.getFinesse();
      configs.add(new StatCurveConfiguration("Finesse",finesse,105,0,400000));
      // Healing
      // - outgoing healing (~= tactical mastery) => healing %
      RatingCurve healing=mgr.getHealing();
      configs.add(new StatCurveConfiguration("Healing",healing,105,0,75000));
      // Incoming healing
      // - incoming healing => incoming healing %
      RatingCurve incomingHealing=mgr.getIncomingHealing();
      configs.add(new StatCurveConfiguration("Incoming healing",incomingHealing,105,0,20000));
      // Avoidance
      // - block rating => block %
      // - parry rating => parry %
      // - evade rating => evade %
      RatingCurve avoidance=mgr.getAvoidance();
      configs.add(new StatCurveConfiguration("Block/parry/evade chance",avoidance,105,0,20000));
      // Partial avoidance
      // - block rating => partial block %
      // - parry rating => partial parry %
      // - evade rating => partial evade %
      RatingCurve partialAvoidance=mgr.getPartialAvoidance();
      configs.add(new StatCurveConfiguration("Partial block/parry/evade chance",partialAvoidance,105,0,50000));
      // Critical defence
      // - critical defence rating => critical defence % (melee/ranged/tactical)
      RatingCurve critDefence=mgr.getCriticalDefence();
      configs.add(new StatCurveConfiguration("Critical defence %",critDefence,105,0,12000));
      // Resistance
      // - resistance => resistance %
      RatingCurve resistance=mgr.getResistance();
      configs.add(new StatCurveConfiguration("Resistance %",resistance,105,0,45000));
      // Partial mitigation
      // - block rating => partial block mitigation %
      // - parry rating => partial parry mitigation %
      // - evade rating => partial evade mitigation %
      RatingCurve partialMitigation=mgr.getPartialMitigation();
      configs.add(new StatCurveConfiguration("Block/parry/evade partial mitigation",partialMitigation,105,0,200000));
      // Armour mitigation
      // - physical mitigation => physical mitigation %
      // - orc craft/fell wrought mitigation => orc craft/fell wrought mitigation %
      // - tactical mitigation => tactical mitigation %
      // 1) Heavy
      RatingCurve heavyArmorMitigation=mgr.getHeavyArmorMitigation();
      configs.add(new StatCurveConfiguration("Heavy armor mitigation",heavyArmorMitigation,105,0,20000));
      // 2) Medium
      RatingCurve mediumArmorMitigation=mgr.getMediumArmorMitigation();
      configs.add(new StatCurveConfiguration("Medium armor mitigation",mediumArmorMitigation,105,0,17000));
      // 3) Light
      RatingCurve lightArmorMitigation=mgr.getLightArmorMitigation();
      configs.add(new StatCurveConfiguration("Light armor mitigation",lightArmorMitigation,105,0,15000));
    }
    for(StatCurveConfiguration config : configs)
    {
      StatCurveChartPanelController chartController=new StatCurveChartPanelController(config);
      JPanel statPanel=chartController.getPanel();
      DefaultWindowController w=new DefaultWindowController();
      w.getFrame().add(statPanel);
      w.getFrame().pack();
      w.show();
    }

    /*
    mgr.getPartialMitigation();
    mgr.getHeavyArmorMitigation();
    mgr.getMediumArmorMitigation();
    mgr.getLightArmorMitigation();
     */
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
