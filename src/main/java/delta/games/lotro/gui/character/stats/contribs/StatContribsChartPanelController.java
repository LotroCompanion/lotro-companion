package delta.games.lotro.gui.character.stats.contribs;

import java.awt.Paint;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.contribs.ContribsByStat;
import delta.games.lotro.character.stats.contribs.StatContribution;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for a chart that show stat contributions.
 * @author DAM
 */
public class StatContribsChartPanelController
{
  // UI
  private JPanel _panel;
  // JFreeChart data
  private DefaultPieDataset _data;
  private JFreeChart _pieChart;

  /**
   * Constructor.
   */
  public StatContribsChartPanelController()
  {
    _data=new DefaultPieDataset();
    boolean legend=false;
    boolean urls=false;
    _pieChart = ChartFactory.createPieChart("", _data, legend, true, urls);
    Paint backgroundPaint=GuiFactory.getBackgroundPaint();
    _pieChart.setBackgroundPaint(backgroundPaint);
    PiePlot plot=(PiePlot)_pieChart.getPlot();
    // TODO L10n
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1} ({2})",new DecimalFormat("#0.#"),new DecimalFormat("#0.##%")));
    _panel = new ChartPanel(_pieChart);
    _panel.setOpaque(false);
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Set the contributions to display.
   * @param contribs Contributions to display.
   */
  public void setContributions(ContribsByStat contribs)
  {
    // Update data
    _data.clear();
    for(StatContribution contrib : contribs.getContribs())
    {
      String source=contrib.getSource().getLabel();
      FixedDecimalsInteger value=contrib.getValue();
      _data.setValue(source,value.doubleValue());
    }
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
    _data=null;
    _pieChart=null;
  }
}
