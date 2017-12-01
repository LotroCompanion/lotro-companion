package delta.games.lotro.gui.character.stats.curves;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.BasicStatsSet;

/**
 * Controller for a stat curve display panel (chart+values).
 * @author DAM
 */
public class StatCurvesPanelController
{
  // Data
  private StatCurveChartPanelController _chartPanel;
  // Controllers
  private StatValuesPanelController _valuesPanel;
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
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel chartPanel=_chartPanel.getPanel();
    panel.add(chartPanel,BorderLayout.CENTER);
    JPanel valuesPanel=_valuesPanel.getPanel();
    panel.add(valuesPanel,BorderLayout.EAST);
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
}

