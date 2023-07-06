package delta.games.lotro.gui.character;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.common.stats.WellKnownStat;
import delta.games.lotro.gui.character.stats.contribs.StatContribsWindowController;
import delta.games.lotro.gui.character.stats.details.DetailedCharacterStatsWindowController;

/**
 * Controller for the character stats summary panel.
 * @author DAM
 */
public class CharacterStatsSummaryPanelController
{
  private JPanel _panel;
  private CharacterData _toon;
  private Map<StatDescription,JLabel> _statLabels;
  private Map<StatDescription,JLabel> _statValues;
  private WindowController _parent;
  private WindowsManager _childControllers;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param toon Toon to display.
   */
  public CharacterStatsSummaryPanelController(WindowController parent, CharacterData toon)
  {
    _toon=toon;
    _parent=parent;
    _statLabels=new HashMap<StatDescription,JLabel>();
    _statValues=new HashMap<StatDescription,JLabel>();
    _childControllers=new WindowsManager();
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
    // Morale, Power, Armor
    StatDescription[] main={WellKnownStat.MORALE,WellKnownStat.POWER,WellKnownStat.ARMOUR};
    // Might, Agility, Vitality, Will, Fate
    StatDescription[] mainStats={WellKnownStat.MIGHT,WellKnownStat.AGILITY,WellKnownStat.VITALITY,WellKnownStat.WILL,WellKnownStat.FATE};
    // Offence: Critical hit, finesse, Physical mastery Tactical Mastery
    StatDescription[] offence={WellKnownStat.CRITICAL_RATING,WellKnownStat.FINESSE,WellKnownStat.PHYSICAL_MASTERY,WellKnownStat.TACTICAL_MASTERY};
    // Defence: Light of Eärendil, Resistance, crit hit avoidance, incoming healing
    StatDescription[] defence={WellKnownStat.LIGHT_OF_EARENDIL,WellKnownStat.RESISTANCE,WellKnownStat.CRITICAL_DEFENCE,WellKnownStat.INCOMING_HEALING};
    // - Avoidance: block, parry, evade
    StatDescription[] avoidance={WellKnownStat.BLOCK,WellKnownStat.PARRY,WellKnownStat.EVADE};
    // - mitigations:
    // -- source: melee X , ranged X, tactical X
    // -- type: physical, tactical
    StatDescription[] mitigation={WellKnownStat.PHYSICAL_MITIGATION,WellKnownStat.TACTICAL_MITIGATION};

    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c1=new GridBagConstraints(0,0,1,2,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    StatDescription[][] statGroups1={main,mainStats,offence};
    String[] groupNames1={"Vitals","Main","Offence"}; // I18n
    JPanel p1=showStatsColumn(statGroups1,groupNames1,true);
    panel.add(p1,c1);
    StatDescription[][] statGroups2={defence,avoidance,mitigation};
    String[] groupNames2={"Defence","Avoidance","Mitigation"}; // I18n
    JPanel p2=showStatsColumn(statGroups2,groupNames2,false);
    GridBagConstraints c2=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(p2,c2);

    // Buttons
    JPanel buttonsPanel=GuiFactory.buildPanel(new FlowLayout());
    // Details button
    JButton details=GuiFactory.buildButton("Details..."); // I18n
    ActionListener alDetails=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doDetails();
      }
    };
    details.addActionListener(alDetails);
    buttonsPanel.add(details);
    // Contribs button
    JButton contribs=GuiFactory.buildButton("Contribs..."); // I18n
    ActionListener alContribs=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doContribs();
      }
    };
    contribs.addActionListener(alContribs);
    buttonsPanel.add(contribs);
    GridBagConstraints cButtons=new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    panel.add(buttonsPanel,cButtons);
    return panel;
  }

  private void doDetails()
  {
    DetailedCharacterStatsWindowController detailsStatsController=getDetailsController();
    if (detailsStatsController==null)
    {
      detailsStatsController=new DetailedCharacterStatsWindowController(_parent,_toon);
      _childControllers.registerWindow(detailsStatsController);
      BasicStatsSet referenceStats=new BasicStatsSet(_toon.getStats());
      detailsStatsController.setStats(referenceStats,_toon.getStats());
      detailsStatsController.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    detailsStatsController.bringToFront();
  }

  private DetailedCharacterStatsWindowController getDetailsController()
  {
    WindowController controller=_childControllers.getWindow(DetailedCharacterStatsWindowController.IDENTIFIER);
    return (DetailedCharacterStatsWindowController)controller;
  }

  private void doContribs()
  {
    StatContribsWindowController contribsController=getContribsController();
    if (contribsController==null)
    {
      contribsController=new StatContribsWindowController(_parent,_toon);
      _childControllers.registerWindow(contribsController);
      contribsController.update();
      contribsController.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    contribsController.bringToFront();
  }

  private StatContribsWindowController getContribsController()
  {
    WindowController controller=_childControllers.getWindow(StatContribsWindowController.IDENTIFIER);
    return (StatContribsWindowController)controller;
  }

  /**
   * Update contents.
   */
  public void update()
  {
    for(StatDescription stat : _statLabels.keySet())
    {
      String statValue="";
      if (_toon!=null)
      {
        BasicStatsSet characterStats=_toon.getStats();
        Number value=characterStats.getStat(stat);
        if (value!=null)
        {
          statValue=StatUtils.getStatDisplay(value,stat);
        }
        else
        {
          statValue="-";
        }
      }
      JLabel valueGadget=getStatValueGadget(stat);
      valueGadget.setText(statValue);
    }
    DetailedCharacterStatsWindowController detailsStatsController=getDetailsController();
    if (detailsStatsController!=null)
    {
      detailsStatsController.update();
    }
    StatContribsWindowController contribsController=getContribsController();
    if (contribsController!=null)
    {
      contribsController.update();
    }
  }

  private JPanel showStatsColumn(StatDescription[][] statGroups, String[] groupNames, boolean left)
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

  private JPanel showStatsGroup(StatDescription[] stats, String group)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c;
    for(int i=0;i<stats.length;i++)
    {
      c=new GridBagConstraints(0,i,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,5,2,5),0,0);
      JLabel labelGadget=getStatLabelGadget(stats[i]);
      panel.add(labelGadget,c);
      c=new GridBagConstraints(1,i,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      JLabel valueGadget=getStatValueGadget(stats[i]);
      valueGadget.setHorizontalAlignment(SwingConstants.RIGHT);
      panel.add(valueGadget,c);
    }
    TitledBorder titledBorder=GuiFactory.buildTitledBorder(group);
    panel.setBorder(titledBorder);
    return panel;
  }

  private JLabel getStatLabelGadget(StatDescription stat)
  {
    JLabel label=_statLabels.get(stat);
    if (label==null)
    {
      String statLabel=stat.getName()+":";
      label=GuiFactory.buildLabel(statLabel);
      _statLabels.put(stat,label);
    }
    return label;
  }

  private JLabel getStatValueGadget(StatDescription stat)
  {
    JLabel label=_statValues.get(stat);
    if (label==null)
    {
      label=GuiFactory.buildLabel("9999999");
      _statValues.put(stat,label);
    }
    return label;
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
    if (_childControllers!=null)
    {
      _childControllers.disposeAll();
      _childControllers=null;
    }
    _parent=null;
    _toon=null;
    _statLabels=null;
    _statValues=null;
  }
}
