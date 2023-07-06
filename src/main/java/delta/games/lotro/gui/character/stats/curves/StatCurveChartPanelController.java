package delta.games.lotro.gui.character.stats.curves;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.ratings.RatingCurve;
import delta.games.lotro.common.stats.StatDescription;

/**
 * Controller for a stat curve chart.
 * @author DAM
 */
public class StatCurveChartPanelController
{
  private static final int NB_POINTS=100;
  /**
   * Colors.
   */
  public static final Color[] LINE_COLORS={ Color.GREEN, Color.BLUE, Color.RED };

  // GUI
  private JPanel _panel;
  private JFreeChart _chart;
  private List<XYSeries> _curvesSeries;
  private List<XYSeries> _currentValueSeries;
  // Data
  private StatCurvesChartConfiguration _config;
  private Number _statValue;

  /**
   * Constructor.
   * @param config Configuration.
   */
  public StatCurveChartPanelController(StatCurvesChartConfiguration config)
  {
    _config=config;
    _curvesSeries=new ArrayList<XYSeries>();
    _currentValueSeries=new ArrayList<XYSeries>();
  }

  /**
   * Full update.
   */
  public void update()
  {
    updateCurveLinesSeries();
    updateStatSeries();
  }

  /**
   * Update this panel with new stats.
   * @param stats Stats to display.
   */
  public void update(BasicStatsSet stats)
  {
    StatDescription baseStat=_config.getBaseStat();
    _statValue=stats.getStat(baseStat);
    updateStatSeries();
  }

  private void updateCurveLinesSeries()
  {
    List<SingleStatCurveConfiguration> curveConfigs=_config.getCurveConfigurations();
    int nbCurves=curveConfigs.size();
    for(int i=0;i<nbCurves;i++)
    {
      SingleStatCurveConfiguration curveConfig=curveConfigs.get(i);
      XYSeries curveLineSeries=_curvesSeries.get(i);
      updateCurveLineSeries(curveLineSeries,curveConfig);
    }
  }

  private void updateCurveLineSeries(XYSeries curveLineSeries, SingleStatCurveConfiguration curveConfiguration)
  {
    curveLineSeries.clear();
    double minRating=_config.getMinRating();
    double maxRating=_config.getMaxRating();
    double step=(maxRating-minRating)/NB_POINTS;
    int level=_config.getLevel();
    RatingCurve curve=curveConfiguration.getCurve();
    for(int i=0;i<NB_POINTS;i++)
    {
      double rating=minRating+i*step;
      Double percentage=curve.getPercentage(rating,level);
      if (percentage!=null)
      {
        curveLineSeries.add(rating,percentage.doubleValue());
      }
    }
  }

  private void updateStatSeries()
  {
    List<SingleStatCurveConfiguration> curveConfigs=_config.getCurveConfigurations();
    int nbCurves=curveConfigs.size();
    for(int i=0;i<nbCurves;i++)
    {
      XYSeries statSeries=_currentValueSeries.get(i);
      SingleStatCurveConfiguration curveConfiguration=curveConfigs.get(i);
      updateStatSeries(statSeries,curveConfiguration);
    }
  }

  private void updateStatSeries(XYSeries statSeries, SingleStatCurveConfiguration curveConfiguration)
  {
    int count=statSeries.getItemCount();
    if (count>0)
    {
      statSeries.remove(0);
    }
    if (_statValue!=null)
    {
      RatingCurve curve=curveConfiguration.getCurve();
      double rating=_statValue.doubleValue();
      int level=_config.getLevel();
      Double percentage=curve.getPercentage(rating,level);
      if (percentage!=null)
      {
        statSeries.add(rating,percentage.doubleValue());
      }
    }
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
    ChartPanel chartPanel=new ChartPanel(_chart);
    chartPanel.setDomainZoomable(true);
    chartPanel.setRangeZoomable(true);
    chartPanel.setHorizontalAxisTrace(false);
    chartPanel.setVerticalAxisTrace(false);
    chartPanel.setMinimumSize(new Dimension(550,330));
    chartPanel.setPreferredSize(new Dimension(550,330));
    chartPanel.setOpaque(false);
    return chartPanel;
  }

  private JFreeChart buildChart()
  {
    String title=_config.getTitle();
    String xAxisLabel="Rating"; // I18n
    String yAxisLabel="Percentage"; // I18n

    XYDataset xydataset=createDataset();
    JFreeChart jfreechart=ChartFactory.createXYLineChart(title,xAxisLabel,yAxisLabel,xydataset,PlotOrientation.VERTICAL,true,true,false);

    Color foregroundColor=GuiFactory.getForegroundColor();
    Paint backgroundPaint=GuiFactory.getBackgroundPaint();
    jfreechart.setBackgroundPaint(backgroundPaint);

    TextTitle t=new TextTitle(title);
    t.setFont(t.getFont().deriveFont(24.0f));
    t.setPaint(foregroundColor);
    jfreechart.setTitle(t);

    XYPlot plot = jfreechart.getXYPlot();
    plot.setDomainPannable(false);

    int nbCurves=_config.getCurveConfigurations().size();
    XYItemRenderer renderer=plot.getRenderer(0);
    if (renderer instanceof XYLineAndShapeRenderer)
    {
      XYLineAndShapeRenderer shapeRenderer=(XYLineAndShapeRenderer)renderer;
      for(int i=0;i<nbCurves;i++)
      {
        int lineSeriesIndex=2*i+0;
        int pointsSeriesIndex=lineSeriesIndex+1;
        shapeRenderer.setSeriesPaint(lineSeriesIndex,LINE_COLORS[i]);
        shapeRenderer.setSeriesShapesVisible(lineSeriesIndex, false);
        shapeRenderer.setSeriesPaint(pointsSeriesIndex,LINE_COLORS[i]);
        shapeRenderer.setSeriesShapesVisible(pointsSeriesIndex, true);
        Shape shape=ShapeUtilities.createDiamond(5);
        shapeRenderer.setSeriesShape(pointsSeriesIndex,shape);
        shapeRenderer.setSeriesVisibleInLegend(pointsSeriesIndex,Boolean.FALSE);
      }
    }
    return jfreechart;
  }

  private XYSeriesCollection createDataset()
  {
    XYSeriesCollection data=new XYSeriesCollection();
    buildCurves(data);
    updateCurveLinesSeries();
    return data;
  }

  private void buildCurves(XYSeriesCollection data)
  {
    for(SingleStatCurveConfiguration curveConfiguration : _config.getCurveConfigurations())
    {
      String key=curveConfiguration.getTitle();
      // Curve line series
      {
        XYSeries curveLineSeries = new XYSeries(key);
        data.addSeries(curveLineSeries);
        _curvesSeries.add(curveLineSeries);
      }
      // Current point series
      {
        XYSeries currentPointSeries=new XYSeries(key+" (Current)"); // I18n
        data.addSeries(currentPointSeries);
        _currentValueSeries.add(currentPointSeries);
      }
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
    _curvesSeries.clear();
    _currentValueSeries.clear();
    _statValue=null;
    _config=null;
    _chart=null;
  }
}
