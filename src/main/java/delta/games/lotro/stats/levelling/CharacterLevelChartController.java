package delta.games.lotro.stats.levelling;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Controller for level chart.
 * @author DAM
 */
public class CharacterLevelChartController
{
  private JPanel _panel;
  private JFreeChart _chart;
  private List<LevellingStats> _stats;

  /**
   * Constructor.
   * @param stats Data to display.
   */
  public CharacterLevelChartController(List<LevellingStats> stats)
  {
    _stats=stats;
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
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
    _chart=buildChart();
    _panel=buildChartPanel();
    return _panel;
  }

  private JFreeChart buildChart()
  {
    ChartData xyData = new ChartData();

    String title="Level up";
    String timeAxisLabel="Time";
    String valueAxisLabel="Level";
    boolean legend=true;
    boolean tooltips=true;
    JFreeChart chart=ChartFactory.createTimeSeriesChart(title, timeAxisLabel, valueAxisLabel, xyData, legend, tooltips, false);
    chart.setBackgroundPaint(Color.white);
    
    XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);
    
    DateAxis axis = (DateAxis) plot.getDomainAxis();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    axis.setDateFormatOverride(sdf);
    return chart;
  }

  private JPanel buildChartPanel()
  {
    ChartPanel chartPanel = new ChartPanel(_chart, ChartPanel.DEFAULT_WIDTH,
        ChartPanel.DEFAULT_HEIGHT,
        ChartPanel.DEFAULT_MINIMUM_DRAW_WIDTH,
        ChartPanel.DEFAULT_MINIMUM_DRAW_HEIGHT,
        ChartPanel.DEFAULT_MAXIMUM_DRAW_WIDTH,
        ChartPanel.DEFAULT_MAXIMUM_DRAW_HEIGHT, true, false,
                                false, false, true, true);

    //((ChartPanel) chartPanel).setHorizontalZoom(true);
    //((ChartPanel) chartPanel).setVerticalZoom(true);
    chartPanel.setHorizontalAxisTrace(true);
    chartPanel.setVerticalAxisTrace(true);
    chartPanel.setFillZoomRectangle(true);
    return chartPanel;
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
    _chart=null;
    _stats.clear();
  }

  private class ChartData extends AbstractXYDataset implements XYDataset
  {
    /**
     * Get a label for a series.
     * @param series Targeted series.
     * @return a label for a series.
     */
    @Override
    public Comparable<?> getSeriesKey(int series)
    {
      return _stats.get(series).getName();
    }

    /**
     * Get the number of series.
     * @return the number of series.
     */
    public int getSeriesCount()
    {
      return _stats.size();
    }

    /**
     * Get the name of a series.
     * @param series Targeted series.
     * @return the name of the targeted series.
     */
    public String getSeriesName(int series)
    {
      return _stats.get(series).getName();
    }

    /**
     * Get the number of points in a series.
     * @param series Targeted series.
     * @return the number of points of the targeted series.
     */
    public int getItemCount(int series)
    {
      return _stats.get(series).getNumberOfItems();
    }

    /**
     * Get the X value for an <code>item</code> of a <code>series</code>.
     * @param series Targeted series.
     * @param item Targeted item.
     * @return a value.
     */
    public Number getX(int series, int item)
    {
      return _stats.get(series).getDate(item);
    }

    /**
     * Get the Y value for an <code>item</code> of a <code>series</code>.
     * @param series Targeted series.
     * @param item Targeted item.
     * @return a value.
     */
    public Number getY(int series, int item)
    {
      return _stats.get(series).getLevel(item);
    }
  }
}
