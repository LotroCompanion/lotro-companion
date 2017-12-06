package delta.games.lotro.gui.character.stats.curves;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.BasicStatsSet;

/**
 * Controller for a stat curve display panel (chart+values).
 * @author DAM
 */
public class StatCurvesPanelController
{
  // Controllers
  private StatCurveChartPanelController _chartPanel;
  private StatValuesPanelController _valuesPanel;
  private StatCurvesConfigurationPanel _configPanel;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param config Curves configuration.
   */
  public StatCurvesPanelController(StatCurvesChartConfiguration config)
  {
    _chartPanel=new StatCurveChartPanelController(config);
    _valuesPanel=new StatValuesPanelController(config);
    _configPanel=new StatCurvesConfigurationPanel(config,_chartPanel);
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

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Chart
    JPanel chartPanel=_chartPanel.getPanel();
    TitledBorder chartBorder=GuiFactory.buildTitledBorder("Chart");
    chartPanel.setBorder(chartBorder);
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(chartPanel,c);
    // Values
    JPanel valuesPanel=_valuesPanel.getPanel();
    TitledBorder valuesBorder=GuiFactory.buildTitledBorder("Values");
    valuesPanel.setBorder(valuesBorder);
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(valuesPanel,c);
    // Configuration
    JPanel configurationPanel=_configPanel.getPanel();
    TitledBorder configurationBorder=GuiFactory.buildTitledBorder("Configuration");
    configurationPanel.setBorder(configurationBorder);
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(configurationPanel,c);
    return panel;
  }

  /**
   * Update this panel with new stats.
   * @param stats Stats to display.
   */
  public void update(BasicStatsSet stats)
  {
    _valuesPanel.update(stats);
    _chartPanel.update(stats);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_chartPanel!=null)
    {
      _chartPanel.dispose();
      _chartPanel=null;
    }
    if (_valuesPanel!=null)
    {
      _valuesPanel.dispose();
      _valuesPanel=null;
    }
    if (_configPanel!=null)
    {
      _configPanel.dispose();
      _configPanel=null;
    }
  }
}
