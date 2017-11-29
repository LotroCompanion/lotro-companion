package delta.games.lotro.gui.character.stats.curves;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;

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
import org.jfree.ui.RectangleAnchor;
import org.jfree.util.ShapeUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.ratings.RatingCurve;

/**
 * Controller for a start curve chart.
 * @author DAM
 */
public class StatCurveChartPanelController
{
  private static final int NB_POINTS=100;
  private static final Color[] LINE_COLORS={ Color.GREEN, Color.BLUE, Color.RED };

  // GUI
  private JPanel _panel;
  private JFreeChart _chart;
  // Data
  private StatCurvesChartConfiguration _config;

  /**
   * Constructor.
   * @param config Configuration.
   */
  public StatCurveChartPanelController(StatCurvesChartConfiguration config)
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
    buildMainCurve(data);
    return data;
  }

  private void buildMainCurve(XYSeriesCollection data)
  {
    double minRating=_config.getMinRating();
    double step=(_config.getMaxRating()-minRating)/NB_POINTS;
    int level=_config.getLevel();
    for(SingleStatCurveConfiguration curveConfiguration : _config.getCurveConfigurations())
    {
      RatingCurve curve=curveConfiguration.getCurve();
      // Curve line series
      String key=curveConfiguration.getTitle();
      XYSeries curveLineSeries = new XYSeries(key);
      for(int i=0;i<NB_POINTS;i++)
      {
        double rating=minRating+i*step;
        Double percentage=curve.getPercentage(rating,level);
        if (percentage!=null)
        {
          curveLineSeries.add(rating,percentage.doubleValue());
        }
      }
      data.addSeries(curveLineSeries);
  
      // Current point series
      {
        XYSeries currentPointSeries = new XYSeries(key+" (Current)");
        double rating=minRating+(NB_POINTS/2)*step;
        Double percentage=curve.getPercentage(rating,level);
        if (percentage!=null)
        {
          currentPointSeries.add(rating,percentage.doubleValue());
        }
        data.addSeries(currentPointSeries);
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
    _chart=null;
  }
}
