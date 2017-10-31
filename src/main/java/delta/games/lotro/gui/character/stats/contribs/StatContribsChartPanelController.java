package delta.games.lotro.gui.character.stats.contribs;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.contribs.StatContribution;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for a chart that show stat contributions.
 * @author DAM
 */
public class StatContribsChartPanelController
{
  private JPanel _panel;
  private DefaultPieDataset _data;

  /**
   * Constructor.
   * @param stat Targeted stat.
   */
  public StatContribsChartPanelController(STAT stat)
  {
    _data=new DefaultPieDataset();
    JFreeChart pieChart = ChartFactory.createPieChart(stat.getName(), _data, true, true, true);
    PiePlot plot=(PiePlot)pieChart.getPlot();
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1} ({2})",new DecimalFormat("#0.#"),new DecimalFormat("#0.##%")));
    _panel = new ChartPanel(pieChart);
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
  public void setContributions(List<StatContribution> contribs)
  {
    _data.clear();
    for(StatContribution contrib : contribs)
    {
      String source=contrib.getSource().getLabel();
      FixedDecimalsInteger value=contrib.getValue();
      _data.setValue(source,value.doubleValue());
    }
  }
}
