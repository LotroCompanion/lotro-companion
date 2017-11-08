package delta.games.lotro.gui.character.stats.curves;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.ratings.RatingCurve;

/**
 * Controller for a start curve chart.
 * @author DAM
 */
public class StatCurveChartPanelController
{
  private static final int NB_POINTS=100;

  // GUI
  private JPanel _panel;
  private JFreeChart _chart;
  // Data
  private StatCurveConfiguration _config;

  /**
   * Constructor.
   * @param config Configuration.
   */
  public StatCurveChartPanelController(StatCurveConfiguration config)
  {
    _config=config;
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

  private JPanel buildChartPanel()
  {
    ChartPanel chartPanel=new ChartPanel(_chart);
    chartPanel.setDomainZoomable(true);
    chartPanel.setRangeZoomable(true);
    chartPanel.setHorizontalAxisTrace(false);
    chartPanel.setVerticalAxisTrace(false);
    chartPanel.setPreferredSize(new Dimension(500,300));
    chartPanel.setOpaque(false);
    return chartPanel;
  }

  private JFreeChart buildChart()
  {
    String title=_config.getTitle();
    String xAxisLabel="Rating";
    String yAxisLabel="Percentage";

    XYDataset xydataset=createDataset();
    JFreeChart jfreechart=ChartFactory.createXYLineChart(title,xAxisLabel,yAxisLabel,xydataset,PlotOrientation.VERTICAL,false,true,false);

    Color foregroundColor=GuiFactory.getForegroundColor();
    Paint backgroundPaint=GuiFactory.getBackgroundPaint();
    jfreechart.setBackgroundPaint(backgroundPaint);

    TextTitle t=new TextTitle(title);
    t.setFont(t.getFont().deriveFont(24.0f));
    t.setPaint(foregroundColor);
    jfreechart.setTitle(t);

    XYPlot plot = jfreechart.getXYPlot();
    plot.setDomainPannable(false);

    return jfreechart;
  }

  private XYSeriesCollection createDataset()
  {
    XYSeriesCollection data=new XYSeriesCollection();
    buildMainCurve(data);
    return data;
  }

  private void buildMainCurve(XYSeriesCollection data)
  {
    String key="main";
    XYSeries mainSeries = new XYSeries(key);
    double minRating=_config.getMinRating();
    double step=(_config.getMaxRating()-minRating)/NB_POINTS;
    int level=_config.getLevel();
    RatingCurve curve=_config.getCurve();
    for(int i=0;i<NB_POINTS;i++)
    {
      double rating=minRating+i*step;
      Double percentage=curve.getPercentage(rating,level);
      if (percentage!=null)
      {
        mainSeries.add(rating,percentage.doubleValue());
      }
    }
    data.addSeries(mainSeries);
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
  }
}
