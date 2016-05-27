package delta.games.lotro.gui.character.stats;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.utils.i18n.Translator;
import delta.common.utils.i18n.TranslatorsManager;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.gui.utils.GuiFactory;

/**
 * Controller for a panel that displays the stats of a single character.
 * @author DAM
 */
public class CharacterStatsPanelController
{
  private HashMap<STAT,SingleStatWidgetsController> _ctrls;

  private JPanel _panel;

  /**
   * Constructor.
   */
  public CharacterStatsPanelController()
  {
    _ctrls=new HashMap<STAT,SingleStatWidgetsController>();
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
    String name=CharacterStatsPanelController.class.getPackage().getName()+".statLabels";
    TranslatorsManager translatorsMgr=TranslatorsManager.getInstance();
    Translator translator=translatorsMgr.getTranslatorByName(name,true,false);
    for(String key : translator.getKeys())
    {
      STAT stat=STAT.getByName(key);
      String label=translator.translate(key);
      addStat(label,stat);
    }
  }

  private void addStat(String label, STAT stat)
  {
    SingleStatWidgetsController singleCtrl=new SingleStatWidgetsController(label,stat,stat.isPercentage());
    _ctrls.put(stat,singleCtrl);
  }

  /**
   * Set stats to display.
   * @param reference Reference stats (may be <code>null</code>).
   * @param current Current stats.
   */
  public void setStats(BasicStatsSet reference, BasicStatsSet current)
  {
    for(SingleStatWidgetsController ctrl : _ctrls.values())
    {
      ctrl.updateStats(reference,current);
    }
  }

  /*
  private JPanel buildPanel2()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JPanel mainStatsPanel=buildMainStatsPanel();
    JPanel offencePanel=buildOffencePanel();
    JPanel avoidancePanel=buildAvoidancePanel();
    JPanel mitigationPanel=buildMitigationsPanel();
    JPanel healingPanel=buildHealingPanel();
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    tabbedPane.add("Main",mainStatsPanel);
    tabbedPane.add("Offence",offencePanel);
    tabbedPane.add("Avoidance",avoidancePanel);
    tabbedPane.add("Mitigation",mitigationPanel);
    tabbedPane.add("Healing",healingPanel);
    panel.add(tabbedPane,BorderLayout.CENTER);
    return panel;
  }
  */

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
    //String[] labels=new String[]{"", "Value", "+/-"};
    //addHeaders(panel,0,0,labels,true,1);
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
    /*
    String[] labels=new String[]{"", "Value", "+/-"};
    for(int i=0;i<2;i++)
    {
      addHeaders(panel,3*i,0,labels,true,1);
    }
    */
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
    addHeaders(panel,0,1,labelsV,false,1);
    int x=1;
    String[] labelsH=new String[]{"Damage %", "Critical %", "Devastate %", "Magnitude %"};
    addHeaders(panel,x,0,labelsH,true,2);
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
    addHeaders(panel,0,1,labelsV,false,1);
    int x=1;
    String[] labelsH=new String[]{"Rating", "Full %", "Partial %", "Mitigation %"};
    addHeaders(panel,x,0,labelsH,true,2);

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
        "Lightning", "Frost", "Acid", "Shadow"};
    addHeaders(panel,0,1,labelsV,false,1);
    int x=1;
    String[] labelsH=new String[]{"Rating", "Mitigation %"};
    addHeaders(panel,x,0,labelsH,true,2);

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
    String[] labelsV=new String[]{"Incoming", "Outgoing"};
    addHeaders(panel,0,1,labelsV,false,1);
    int x=1;
    String[] labelsH=new String[]{"Rating", "Healing %"};
    addHeaders(panel,x,0,labelsH,true,2);

    int y=1;
    // Incoming Healing
    addStatWidgets(panel,STAT.INCOMING_HEALING,x,y,false);
    addStatWidgets(panel,STAT.INCOMING_HEALING_PERCENTAGE,x+2,y,false);
    y++;
    addStatWidgets(panel,STAT.OUTGOING_HEALING_PERCENTAGE,x+2,y,false);

    TitledBorder border=GuiFactory.buildTitledBorder("Healing");
    panel.setBorder(border);
    return panel;
  }

  private void addHeaders(JPanel panel, int x, int y, String[] labels, boolean horizontal, int gridwidth)
  {
    for(int i=0;i<labels.length;i++)
    {
      int cellX=x+(horizontal?(gridwidth*i):0);
      int cellY=y+(horizontal?0:i);
      GridBagConstraints c=new GridBagConstraints(cellX,cellY,gridwidth,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(new JLabel(labels[i]),c);
    }
  }

  private void addStatWidgets(JPanel panel, STAT stat, int x, int y, boolean showLabel)
  {
    GridBagConstraints c;
    SingleStatWidgetsController ctrl=_ctrls.get(stat);
    if (showLabel)
    {
      JLabel label=ctrl.getLabel();
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
}
