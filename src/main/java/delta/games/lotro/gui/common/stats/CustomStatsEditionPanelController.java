package delta.games.lotro.gui.common.stats;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.utils.NumericTools;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.CustomStatsMergeMode;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsManager;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for a panel to edit custom stats.
 * @author DAM
 */
public class CustomStatsEditionPanelController
{
  private static final int MAX_STATS = 10;
  // Data
  private StatsManager _statsManager;
  // GUI
  private JPanel _panel;
  // Controllers
  private ComboBoxController<CustomStatsMergeMode> _mode;
  private List<SingleStatsEditionGadgetsController> _statControllers;

  /**
   * Constructor.
   * @param statsManager Stats manager.
   */
  public CustomStatsEditionPanelController(StatsManager statsManager)
  {
    _statsManager=statsManager;
    _mode=buildModesCombo();
    _statControllers=new ArrayList<SingleStatsEditionGadgetsController>();
    _panel=buildPanel();
    init();
  }

  /**
   * Initialize the managed panel with the given data.
   */
  private void init()
  {
    // Mode
    CustomStatsMergeMode mode=_statsManager.getMode();
    _mode.selectItem(mode);
    // Stats
    BasicStatsSet statsSet=_statsManager.getCustom();
    List<StatDescription> usedStats=statsSet.getSortedStats();
    int nbStats=usedStats.size();
    for(int i=0;i<MAX_STATS;i++)
    {
      SingleStatsEditionGadgetsController ctrl=_statControllers.get(i);
      if (i<nbStats)
      {
        StatDescription stat=usedStats.get(i);
        FixedDecimalsInteger value=statsSet.getStat(stat);
        ctrl.setStat(stat,value);
      }
      else
      {
        ctrl.clear();
      }
    }
  }

  /**
   * Get the data from the UI.
   */
  public void getData()
  {
    // Mode
    CustomStatsMergeMode mode=_mode.getSelectedItem();
    _statsManager.setMode(mode);
    // Stats
    BasicStatsSet stats=getStats();
    BasicStatsSet customStats=_statsManager.getCustom();
    customStats.setStats(stats);
    _statsManager.apply();
  }

  /**
   * Get the current stats.
   * @return the current stats.
   */
  private BasicStatsSet getStats()
  {
    BasicStatsSet stats=new BasicStatsSet();
    for(SingleStatsEditionGadgetsController ctrl : _statControllers)
    {
      ComboBoxController<StatDescription> comboCtrl=ctrl.getStatComboController();
      StatDescription stat=comboCtrl.getSelectedItem();
      if (stat!=null)
      {
        String valueStr=ctrl.getValue().getText();
        Float value=NumericTools.parseFloat(valueStr,false);
        if (value!=null)
        {
          stats.addStat(stat,new FixedDecimalsInteger(value.floatValue()));
        }
      }
    }
    return stats;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Mode
    JPanel modePanel=buildModePanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,0,5),0,0);
    panel.add(modePanel,c);

    // Stats
    JPanel statsPanel=buildStatsPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(statsPanel,c);

    return panel;
  }

  private JPanel buildModePanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(GuiFactory.buildLabel("Mode:"));
    panel.add(_mode.getComboBox());
    return panel;
  }

  private JPanel buildStatsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    for(int i=0;i<MAX_STATS;i++)
    {
      SingleStatsEditionGadgetsController ctrl=new SingleStatsEditionGadgetsController();
      _statControllers.add(ctrl);

      GridBagConstraints c=new GridBagConstraints(0,i,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      // Value editor
      JTextField value=ctrl.getValue();
      panel.add(value,c);
      // Stat chooser
      c.gridx++;
      panel.add(ctrl.getStatComboController().getComboBox(),c);
      // Stat unit label
      JLabel unit=ctrl.getUnit();
      c.gridx++;
      panel.add(unit,c);
      // Delete button
      JButton deleteButton=ctrl.getDeleteButton();
      c.gridx++;
      panel.add(deleteButton,c);
    }
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_statControllers!=null)
    {
      for(SingleStatsEditionGadgetsController controller : _statControllers)
      {
        controller.dispose();
      }
      _statControllers=null;
    }
  }


  /**
   * Build a controller for a combo box to choose custom stats merge mode.
   * @return A new controller.
   */
  private ComboBoxController<CustomStatsMergeMode> buildModesCombo()
  {
    ComboBoxController<CustomStatsMergeMode> ctrl=new ComboBoxController<CustomStatsMergeMode>();
    ctrl.addItem(CustomStatsMergeMode.AUTO,"Disabled");
    ctrl.addItem(CustomStatsMergeMode.ADD,"Add");
    ctrl.addItem(CustomStatsMergeMode.SET,"Set");
    ctrl.addItem(CustomStatsMergeMode.MERGE,"Merge");
    ctrl.selectItem(CustomStatsMergeMode.AUTO);
    return ctrl;
  }
}
