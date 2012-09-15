package delta.games.lotro.gui.character;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterStat;
import delta.games.lotro.character.CharacterStat.STAT;

/**
 * Controller for character stats panel.
 * @author DAM
 */
public class ChararacterStatsPanelController
{
  private JPanel _panel;
  private CharacterFile _toon;
  private JLabel[] _statLabels;
  private JLabel[] _statValues;
  
  /**
   * Constructor.
   * @param toon Toon to display.
   */
  public ChararacterStatsPanelController(CharacterFile toon)
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
    // Show stats
    Character info=_toon.getLastCharacterInfo();
    STAT[] stats=STAT.values();
    int nbStats=stats.length;
    _statLabels=new JLabel[nbStats];
    _statValues=new JLabel[nbStats];
    for(int i=0;i<nbStats;i++)
    {
      String label=stats[i].getName()+":";
      _statLabels[i]=new JLabel(label);
      _statLabels[i].setForeground(Color.WHITE);
      //_statLabels[i].setBackground(Color.RED);
      //_statLabels[i].setOpaque(true);
      _statValues[i]=new JLabel();
      _statValues[i].setForeground(Color.WHITE);
      String statValue="";
      if (info!=null)
      {
        CharacterStat cStat=info.getStat(stats[i],false);
        if (cStat!=null)
        {
          Integer value=cStat.getValue();
          if (value!=null)
          {
            statValue=String.valueOf(value.intValue());
          }
          else
          {
            statValue="N/A";
          }
        }
      }
      _statValues[i].setText(statValue);
    }
    // Morale, Power, Armor
    STAT[] main={STAT.MORALE,STAT.POWER,STAT.ARMOUR};
    // Might, Agility, Vitality, Will, Fate
    STAT[] mainStats={STAT.MIGHT,STAT.AGILITY,STAT.VITALITY,STAT.WILL,STAT.FATE};
    // Offence: Critical hit, finesse, Physical mastery? Tactical Mastery?
    STAT[] offence={STAT.CRITICAL_HIT,STAT.FINESSE};
    // Defence: Resistance, crit hit avoidance, incoming healing?
    STAT[] defence={STAT.RESISTANCE,STAT.CRITICAL_AVOID};
    // - Avoidance: block, parry, evade
    STAT[] avoidance={STAT.BLOCK,STAT.PARRY,STAT.EVADE};
    // - mitigations:
    // -- source: melee X , ranged X, tactical X
    // -- type: physical, tactical
    STAT[] mitigation={STAT.PHYSICAL_MITIGATION,STAT.TACTICAL_MITIGATION};
    
    JPanel panel=new JPanel(new GridBagLayout());
    panel.setBackground(Color.BLACK);
    
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
  
  private JPanel showStatsColumn(STAT[][] statGroups, String[] groupNames, boolean left)
  {
    JPanel panel=new JPanel(new GridBagLayout());
    panel.setBackground(Color.BLACK);
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
    JPanel panel=new JPanel(new GridBagLayout());
    GridBagConstraints c;
    for(int i=0;i<stats.length;i++)
    {
      int index=stats[i].ordinal();
      c=new GridBagConstraints(0,i,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,5,2,5),0,0);
      panel.add(_statLabels[index],c);
      _statLabels[index].setBackground(Color.BLACK);
      _statLabels[index].setOpaque(true);
      c=new GridBagConstraints(1,i,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      panel.add(_statValues[index],c);
      _statValues[index].setBackground(Color.BLACK);
      _statValues[index].setOpaque(true);
      _statValues[index].setHorizontalAlignment(SwingConstants.RIGHT);
    }
    panel.setBackground(Color.BLACK);
    Border border=BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.WHITE,Color.GRAY);
    TitledBorder titledBorder=BorderFactory.createTitledBorder(border,group);
    titledBorder.setTitleColor(Color.WHITE);
    panel.setBorder(titledBorder);
    panel.setBackground(Color.BLACK);
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
