package delta.games.lotro.gui.character;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for character stats panel.
 * @author DAM
 */
public class ChararacterStatsPanelController
{
  private JPanel _panel;
  private CharacterData _toon;
  private JLabel[] _statLabels;
  private JLabel[] _statValues;
  
  /**
   * Constructor.
   * @param toon Toon to display.
   */
  public ChararacterStatsPanelController(CharacterData toon)
  {
    _toon=toon;
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    STAT[] stats=STAT.values();
    int nbStats=stats.length;
    _statLabels=new JLabel[nbStats];
    _statValues=new JLabel[nbStats];

    for(int i=0;i<nbStats;i++)
    {
      String label=stats[i].getName()+":";
      _statLabels[i]=GuiFactory.buildLabel(label);
      _statValues[i]=GuiFactory.buildLabel("");
    }
    update();

    // Morale, Power, Armor
    STAT[] main={STAT.MORALE,STAT.POWER,STAT.ARMOUR};
    // Might, Agility, Vitality, Will, Fate
    STAT[] mainStats={STAT.MIGHT,STAT.AGILITY,STAT.VITALITY,STAT.WILL,STAT.FATE};
    // Offence: Critical hit, finesse, Physical mastery? Tactical Mastery?
    STAT[] offence={STAT.CRITICAL_RATING,STAT.FINESSE};
    // Defence: Resistance, crit hit avoidance, incoming healing?
    STAT[] defence={STAT.RESISTANCE,STAT.CRITICAL_DEFENCE};
    // - Avoidance: block, parry, evade
    STAT[] avoidance={STAT.BLOCK,STAT.PARRY,STAT.EVADE};
    // - mitigations:
    // -- source: melee X , ranged X, tactical X
    // -- type: physical, tactical
    STAT[] mitigation={STAT.PHYSICAL_MITIGATION,STAT.TACTICAL_MITIGATION};

    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c1=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    STAT[][] statGroups1={main,mainStats,offence};
    String[] groupNames1={"Vitals","Main","Offence"};
    JPanel p1=showStatsColumn(statGroups1,groupNames1,true);
    panel.add(p1,c1);
    STAT[][] statGroups2={defence,avoidance,mitigation};
    String[] groupNames2={"Defence","Avoidance","Mitigation"};
    JPanel p2=showStatsColumn(statGroups2,groupNames2,false);
    GridBagConstraints c2=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(p2,c2);
    return panel;
  }

  /**
   * Set character to display.
   * @param toon Character to set.
   */
  public void setCharacter(CharacterData toon)
  {
    _toon=toon;
  }

  /**
   * Update contents.
   */
  public void update()
  {
    STAT[] stats=STAT.values();
    int nbStats=stats.length;
    for(int i=0;i<nbStats;i++)
    {
      String statValue="";
      if (_toon!=null)
      {
        BasicStatsSet characterStats=_toon.getStats();
        FixedDecimalsInteger value=characterStats.getStat(stats[i]);
        if (value!=null)
        {
          statValue=String.valueOf(value.intValue());
        }
        else
        {
          statValue="N/A";
        }
      }
      if (_statValues[i]!=null)
      {
        _statValues[i].setText(statValue);
      }
    }
  }

  private JPanel showStatsColumn(STAT[][] statGroups, String[] groupNames, boolean left)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    Insets insets=new Insets(2,left?5:2,2,left?2:5);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0);
    for(int i=0;i<statGroups.length;i++)
    {
      JPanel statsPanel=showStatsGroup(statGroups[i],groupNames[i]);
      c.gridy=i;
      panel.add(statsPanel,c);
    }
    return panel;
  }

  private JPanel showStatsGroup(STAT[] stats, String group)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c;
    for(int i=0;i<stats.length;i++)
    {
      int index=stats[i].ordinal();
      c=new GridBagConstraints(0,i,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,5,2,5),0,0);
      panel.add(_statLabels[index],c);
      c=new GridBagConstraints(1,i,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      panel.add(_statValues[index],c);
      _statValues[index].setHorizontalAlignment(SwingConstants.RIGHT);
    }
    TitledBorder titledBorder=GuiFactory.buildTitledBorder(group);
    panel.setBorder(titledBorder);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _toon=null;
    _statLabels=null;
    _statValues=null;
  }
}
