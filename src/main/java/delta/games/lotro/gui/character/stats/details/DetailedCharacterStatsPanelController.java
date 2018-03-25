package delta.games.lotro.gui.character.stats.details;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.gui.character.stats.StatLabels;
import delta.games.lotro.gui.character.stats.curves.StatCurvesChartConfiguration;
import delta.games.lotro.gui.character.stats.curves.StatCurvesWindowsManager;

/**
 * Controller for a panel that displays the stats of a single character.
 * @author DAM
 */
public class DetailedCharacterStatsPanelController
{
  // Controllers
  private HashMap<STAT,SingleStatWidgetsController> _ctrls;
  // Data
  private BasicStatsSet _reference;
  private BasicStatsSet _current;
  // UI
  private JPanel _panel;
  // Child windows manager
  private StatCurvesWindowsManager _statCurvesMgr;

  /**
   * Constructor.
   * @param statCurvesMgr Manager for stat curves.
   */
  public DetailedCharacterStatsPanelController(StatCurvesWindowsManager statCurvesMgr)
  {
    _ctrls=new HashMap<STAT,SingleStatWidgetsController>();
    _statCurvesMgr=statCurvesMgr;
    init();
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void init()
  {
    for(String key : StatLabels.getTranslatedStats())
    {
      STAT stat=STAT.getByName(key);
      addStat(stat);
    }
  }

  private void addStat(STAT stat)
  {
    SingleStatWidgetsController singleCtrl=new SingleStatWidgetsController(stat,stat.isPercentage());
    _ctrls.put(stat,singleCtrl);
  }

  /**
   * Update values.
   */
  public void update()
  {
    setStats(_reference,_current);
    _statCurvesMgr.update();
  }

  /**
   * Set stats to display.
   * @param reference Reference stats (may be <code>null</code>).
   * @param current Current stats.
   */
  public void setStats(BasicStatsSet reference, BasicStatsSet current)
  {
    _reference=reference;
    _current=current;
    for(SingleStatWidgetsController ctrl : _ctrls.values())
    {
      ctrl.updateStats(reference,current);
    }
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    JPanel mainStatsPanel=buildMainStatsPanel();
    JPanel offencePanel=buildOffencePanel();
    JPanel avoidancePanel=buildAvoidancePanel();
    JPanel mitigationPanel=buildMitigationsPanel();
    JPanel healingPanel=buildHealingPanel();

    GridBagConstraints c=new GridBagConstraints(0,0,1,3,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(mainStatsPanel,c);
    c=new GridBagConstraints(1,0,1,2,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0);
    panel.add(offencePanel,c);
    c=new GridBagConstraints(1,2,1,3,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,5,5),0,0);
    panel.add(avoidancePanel,c);
    c=new GridBagConstraints(0,4,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,5),0,0);
    panel.add(healingPanel,c);
    c=new GridBagConstraints(2,0,1,3,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,5),0,0);
    panel.add(mitigationPanel,c);
    return panel;
  }

  private JPanel buildMainStatsPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    STAT[] stats={STAT.MORALE, STAT.POWER, STAT.ARMOUR, STAT.MIGHT,
        STAT.AGILITY, STAT.VITALITY, STAT.WILL, STAT.FATE,
        STAT.OCMR, STAT.ICMR, STAT.OCPR, STAT.ICPR
    };
    int x=0;
    int y=0;
    for(STAT stat : stats)
    {
      addStatWidgets(panel,stat,x,y,true);
      y++;
    }
    TitledBorder border=GuiFactory.buildTitledBorder("Main");
    panel.setBorder(border);
    return panel;
  }

  private JPanel buildOffencePanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(null);
    panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
    panel.add(buildOffenceStatsPanel());
    panel.add(buildOffencePercentagePanel());

    TitledBorder border=GuiFactory.buildTitledBorder("Offence");
    panel.setBorder(border);
    return panel;
  }

  private JPanel buildOffenceStatsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int x=0;
    int y=0;
    addStatWidgets(panel,STAT.PHYSICAL_MASTERY,x,y,true);
    addStatWidgets(panel,STAT.FINESSE,x+3,y,true);
    y++;
    addStatWidgets(panel,STAT.TACTICAL_MASTERY,x,y,true);
    addStatWidgets(panel,STAT.FINESSE_PERCENTAGE,x+3,y,true);
    y++;
    addStatWidgets(panel,STAT.CRITICAL_RATING,x,y,true);

    TitledBorder border=GuiFactory.buildTitledBorder("Stats");
    panel.setBorder(border);
    return panel;
  }

  private JPanel buildOffencePercentagePanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    String[] labelsV=new String[]{"Melee", "Ranged", "Tactical"};
    STAT[] stats={STAT.PHYSICAL_MASTERY, STAT.PHYSICAL_MASTERY, STAT.TACTICAL_MASTERY};
    addHeaders(panel,0,1,labelsV,stats,false,1);
    int x=1;
    String[] labelsH=new String[]{"Damage %", "Critical %", "Devastate %", "Magnitude %"};
    addHeaders(panel,x,0,labelsH,null,true,2);
    int y=1;
    addStatWidgets(panel,STAT.MELEE_DAMAGE_PERCENTAGE,x,y,false);
    addStatWidgets(panel,STAT.CRITICAL_MELEE_PERCENTAGE,x+2,y,false);
    addStatWidgets(panel,STAT.DEVASTATE_MELEE_PERCENTAGE,x+4,y,false);
    addStatWidgets(panel,STAT.CRIT_DEVASTATE_MAGNITUDE_MELEE_PERCENTAGE,x+6,y,false);
    y++;
    addStatWidgets(panel,STAT.RANGED_DAMAGE_PERCENTAGE,x,y,false);
    addStatWidgets(panel,STAT.CRITICAL_RANGED_PERCENTAGE,x+2,y,false);
    addStatWidgets(panel,STAT.DEVASTATE_RANGED_PERCENTAGE,x+4,y,false);
    addStatWidgets(panel,STAT.CRIT_DEVASTATE_MAGNITUDE_RANGED_PERCENTAGE,x+6,y,false);
    y++;
    addStatWidgets(panel,STAT.TACTICAL_DAMAGE_PERCENTAGE,x,y,false);
    addStatWidgets(panel,STAT.CRITICAL_TACTICAL_PERCENTAGE,x+2,y,false);
    addStatWidgets(panel,STAT.DEVASTATE_TACTICAL_PERCENTAGE,x+4,y,false);
    addStatWidgets(panel,STAT.CRIT_DEVASTATE_MAGNITUDE_TACTICAL_PERCENTAGE,x+6,y,false);

    TitledBorder border=GuiFactory.buildTitledBorder("Damage");
    panel.setBorder(border);
    return panel;
  }

  private JPanel buildAvoidancePanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    String[] labelsV=new String[]{"Resistance", "Block", "Parry", "Evade"};
    STAT[] stats={STAT.RESISTANCE, STAT.BLOCK, STAT.PARRY, STAT.EVADE};
    addHeaders(panel,0,1,labelsV,stats,false,1);
    int x=1;
    String[] labelsH=new String[]{"Rating", "Full %", "Partial %", "Mitigation %"};
    addHeaders(panel,x,0,labelsH,null,true,2);

    int y=1;
    // Resist
    addStatWidgets(panel,STAT.RESISTANCE,x,y,false);
    addStatWidgets(panel,STAT.RESISTANCE_PERCENTAGE,x+2,y,false);
    y++;
    // Block
    addStatWidgets(panel,STAT.BLOCK,x,y,false);
    addStatWidgets(panel,STAT.BLOCK_PERCENTAGE,x+2,y,false);
    addStatWidgets(panel,STAT.PARTIAL_BLOCK_PERCENTAGE,x+4,y,false);
    addStatWidgets(panel,STAT.PARTIAL_BLOCK_MITIGATION_PERCENTAGE,x+6,y,false);
    y++;
    // Parry
    addStatWidgets(panel,STAT.PARRY,x,y,false);
    addStatWidgets(panel,STAT.PARRY_PERCENTAGE,x+2,y,false);
    addStatWidgets(panel,STAT.PARTIAL_PARRY_PERCENTAGE,x+4,y,false);
    addStatWidgets(panel,STAT.PARTIAL_PARRY_MITIGATION_PERCENTAGE,x+6,y,false);
    y++;
    // Evade
    addStatWidgets(panel,STAT.EVADE,x,y,false);
    addStatWidgets(panel,STAT.EVADE_PERCENTAGE,x+2,y,false);
    addStatWidgets(panel,STAT.PARTIAL_EVADE_PERCENTAGE,x+4,y,false);
    addStatWidgets(panel,STAT.PARTIAL_EVADE_MITIGATION_PERCENTAGE,x+6,y,false);

    TitledBorder border=GuiFactory.buildTitledBorder("Avoidance");
    panel.setBorder(border);
    return panel;
  }

  private JPanel buildMitigationsPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    String[] labelsV=new String[]{"Crit.Def (melee)",
        "Crit.Def (ranged)", "Crit.Def (tactical)",
        "Physical", "Orc-craft/Fell-wrought", "Tactical",
        "Fire", "Lightning", "Frost", "Acid", "Shadow"};
    STAT[] stats={STAT.CRITICAL_DEFENCE, STAT.CRITICAL_DEFENCE, STAT.CRITICAL_DEFENCE,
        STAT.PHYSICAL_MITIGATION, STAT.OCFW_MITIGATION, STAT.TACTICAL_MITIGATION,
        null,null,null,null,null};
    addHeaders(panel,0,1,labelsV,stats,false,1);
    int x=1;
    String[] labelsH=new String[]{"Rating", "Mitigation %"};
    addHeaders(panel,x,0,labelsH,null,true,2);

    int y=1;
    // Crit/dev defence
    addStatWidgets(panel,STAT.CRITICAL_DEFENCE,x,y,false);
    addStatWidgets(panel,STAT.MELEE_CRITICAL_DEFENCE,x+2,y,false);
    y++;
    addStatWidgets(panel,STAT.RANGED_CRITICAL_DEFENCE,x+2,y,false);
    y++;
    addStatWidgets(panel,STAT.TACTICAL_CRITICAL_DEFENCE,x+2,y,false);
    y++;
    // Physical Mitigation
    addStatWidgets(panel,STAT.PHYSICAL_MITIGATION,x,y,false);
    addStatWidgets(panel,STAT.PHYSICAL_MITIGATION_PERCENTAGE,x+2,y,false);
    y++;
    // Orc-craft/Fell-wrought Mitigation
    addStatWidgets(panel,STAT.OCFW_MITIGATION,x,y,false);
    addStatWidgets(panel,STAT.OCFW_MITIGATION_PERCENTAGE,x+2,y,false);
    y++;
    // Tactical Mitigation
    addStatWidgets(panel,STAT.TACTICAL_MITIGATION,x,y,false);
    addStatWidgets(panel,STAT.TACTICAL_MITIGATION_PERCENTAGE,x+2,y,false);
    y++;
    // Fire Mitigation
    addStatWidgets(panel,STAT.FIRE_MITIGATION_PERCENTAGE,x+2,y,false);
    y++;
    // Lightning Mitigation
    addStatWidgets(panel,STAT.LIGHTNING_MITIGATION_PERCENTAGE,x+2,y,false);
    y++;
    // Frost Mitigation
    addStatWidgets(panel,STAT.FROST_MITIGATION_PERCENTAGE,x+2,y,false);
    y++;
    // Acid Mitigation
    addStatWidgets(panel,STAT.ACID_MITIGATION_PERCENTAGE,x+2,y,false);
    y++;
    // Shadow Mitigation
    addStatWidgets(panel,STAT.SHADOW_MITIGATION_PERCENTAGE,x+2,y,false);
    y++;

    TitledBorder border=GuiFactory.buildTitledBorder("Mitigations");
    panel.setBorder(border);
    return panel;
  }

  private JPanel buildHealingPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    String[] labelsV={"Incoming", "Outgoing"};
    STAT[] stats={STAT.INCOMING_HEALING, STAT.OUTGOING_HEALING};
    addHeaders(panel,0,1,labelsV,stats,false,1);
    int x=1;
    String[] labelsH=new String[]{"Rating", "Healing %"};
    addHeaders(panel,x,0,labelsH,null,true,2);

    int y=1;
    // Incoming Healing
    addStatWidgets(panel,STAT.INCOMING_HEALING,x,y,false);
    addStatWidgets(panel,STAT.INCOMING_HEALING_PERCENTAGE,x+2,y,false);
    y++;
    addStatWidgets(panel,STAT.OUTGOING_HEALING,x,y,false);
    addStatWidgets(panel,STAT.OUTGOING_HEALING_PERCENTAGE,x+2,y,false);

    TitledBorder border=GuiFactory.buildTitledBorder("Healing");
    panel.setBorder(border);
    return panel;
  }

  private void addHeaders(JPanel panel, int x, int y, String[] labels, STAT[] stats, boolean horizontal, int gridwidth)
  {
    for(int i=0;i<labels.length;i++)
    {
      int cellX=x+(horizontal?(gridwidth*i):0);
      int cellY=y+(horizontal?0:i);
      GridBagConstraints c=new GridBagConstraints(cellX,cellY,gridwidth,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      STAT stat=(stats!=null)?stats[i]:null;
      JLabel label=buildLabelForStat(labels[i],stat);
      panel.add(label,c);
    }
  }

  private void addStatWidgets(JPanel panel, STAT stat, int x, int y, boolean showLabel)
  {
    GridBagConstraints c;
    SingleStatWidgetsController ctrl=_ctrls.get(stat);
    if (showLabel)
    {
      String text=StatLabels.getStatLabel(stat)+":";
      JLabel label=buildLabelForStat(text,stat);
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(label,c);
      x++;
    }
    c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    x++;
    JLabel valueLabel=ctrl.getValueLabel();
    panel.add(valueLabel,c);
    c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JLabel deltaValueLabel=ctrl.getDeltaValueLabel();
    panel.add(deltaValueLabel,c);
  }

  private JLabel buildLabelForStat(String text, STAT stat)
  {
    JLabel label=null;
    if (stat!=null)
    {
      final StatCurvesChartConfiguration config=(_statCurvesMgr!=null)?_statCurvesMgr.getConfigForStat(stat):null;
      if (config!=null)
      {
        ActionListener al=new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            _statCurvesMgr.showStatCurvesWindow(config);
          }
        };
        LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
        HyperLinkController controller=new HyperLinkController(action);
        label=controller.getLabel();
      }
    }
    if (label==null)
    {
      label=GuiFactory.buildLabel(text);
    }
    return label;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _ctrls.clear();
    _reference=null;
    _current=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_statCurvesMgr!=null)
    {
      _statCurvesMgr.dispose();
      _statCurvesMgr=null;
    }
  }
}
