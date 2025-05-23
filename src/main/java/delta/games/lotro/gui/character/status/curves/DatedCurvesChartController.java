package delta.games.lotro.gui.character.status.curves;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.l10n.LocalizedFormats;
import delta.games.lotro.utils.Formats;
import delta.games.lotro.utils.charts.DatedCurve;
import delta.games.lotro.utils.charts.DatedCurveItem;

/**
 * Controller for a chart with a set of dated curves.
 * @author DAM
 */
public class DatedCurvesChartController
{
  private static final Logger LOGGER=LoggerFactory.getLogger(DatedCurvesChartController.class);

  // GUI
  private JPanel _panel;
  private JFreeChart _chart;
  // Data
  private DatedCurvesProvider _provider;
  // Configuration
  private DatedCurvesChartConfiguration _configuration;
  // Internal cooking
  private HashMap<String,Integer> _curveIds2SeriesIndex;

  /**
   * Constructor.
   * @param provider Data provider.
   * @param configuration Chart configuration.
   */
  public DatedCurvesChartController(DatedCurvesProvider provider, DatedCurvesChartConfiguration configuration)
  {
    _provider=provider;
    _configuration=configuration;
    _curveIds2SeriesIndex=new HashMap<String,Integer>();
  }

  /**
   * Get the configuration.
   * @return the configuration.
   */
  public DatedCurvesChartConfiguration getConfiguration()
  {
    return _configuration;
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
    String title=_configuration.getChartTitle();
    String timeAxisLabel=_configuration.getTimeAxisLabel();
    String valueAxisLabel=_configuration.getValueAxisLabel();

    XYDataset xydataset=createDataset();
    JFreeChart jfreechart=ChartFactory.createTimeSeriesChart(title, timeAxisLabel, valueAxisLabel, xydataset, true, true, false);

    Color foregroundColor=GuiFactory.getForegroundColor();
    Paint backgroundPaint=GuiFactory.getBackgroundPaint();
    jfreechart.setBackgroundPaint(backgroundPaint);

    TextTitle t=new TextTitle(title);
    t.setFont(t.getFont().deriveFont(24.0f));
    t.setPaint(foregroundColor);
    jfreechart.setTitle(t);

    XYPlot plot=jfreechart.getXYPlot();
    plot.setDomainPannable(false);

    // Tooltip
    configureTooltip(plot);

    // Time axis
    DateAxis axis = (DateAxis) plot.getDomainAxis();
    DateFormat sdf=LocalizedFormats.getDateFormat();
    axis.setDateFormatOverride(sdf);
    axis.setAxisLinePaint(foregroundColor);
    axis.setLabelPaint(foregroundColor);
    axis.setTickLabelPaint(foregroundColor);

    // Value axis
    configureValueAxis(plot);

    LegendTitle legend=jfreechart.getLegend();
    legend.setPosition(RectangleEdge.BOTTOM);
    legend.setItemPaint(foregroundColor);
    legend.setBackgroundPaint(backgroundPaint);
    return jfreechart;
  }

  private void configureTooltip(XYPlot plot)
  {
    ValueRenderer valueRenderer=_configuration.getValueRenderer();
    XYToolTipGenerator tooltip=new StandardXYToolTipGenerator()
    {
      @Override
      public String generateLabelString(XYDataset dataset, int series, int item)
      {
        String name=(String)((XYSeriesCollection)dataset).getSeriesKey(series);
        double value=dataset.getYValue(series,item);
        String valueDisplay;
        if (valueRenderer!=null)
        {
          valueDisplay=valueRenderer.render(value);
        }
        else
        {
          valueDisplay=String.valueOf((int)value);
        }
        double timestamp=dataset.getXValue(series,item);
        String date=Formats.getDateTimeString(new Date((long)timestamp));
        return name+" - "+valueDisplay+" ("+date+")";
      }
    };
    XYItemRenderer renderer=plot.getRenderer();
    renderer.setBaseToolTipGenerator(tooltip);
  }

  private void configureValueAxis(XYPlot plot)
  {
    NumberAxis valueAxis = (NumberAxis)plot.getRangeAxis();
    valueAxis.setAutoRange(true);
    Color foregroundColor=GuiFactory.getForegroundColor();
    valueAxis.setAxisLinePaint(foregroundColor);
    valueAxis.setLabelPaint(foregroundColor);
    valueAxis.setTickLabelPaint(foregroundColor);
    double[] valueAxisTicks=_configuration.getValueAxisTicks();
    if (valueAxisTicks!=null)
    {
      NumberFormat format=LocalizedFormats.getIntegerNumberFormat();
      TickUnits ticks=new TickUnits();
      for(double tick : valueAxisTicks)
      {
        ticks.add(new NumberTickUnit(tick,format));
      }
      valueAxis.setStandardTickUnits(ticks);
    }
    ValueRenderer valueRenderer=_configuration.getValueRenderer();
    if (valueRenderer!=null)
    {
      valueAxis.setNumberFormatOverride(buildValueNumberFormat(valueRenderer));
    }
    else
    {
      valueAxis.setNumberFormatOverride(null);
    }
    // Label
    String valueAxisLabel=_configuration.getValueAxisLabel();
    valueAxis.setLabel(valueAxisLabel);
  }

  private NumberFormat buildValueNumberFormat(ValueRenderer renderer)
  {
    NumberFormat nf=new NumberFormat()
    {
      private String formatImpl(double number)
      {
        return renderer.render(number);
      }

      @Override
      public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos)
      {
        return toAppendTo.append(formatImpl(number));
      }

      @Override
      public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos)
      {
        return toAppendTo.append(formatImpl(number));
      }

      @Override
      public Number parse(String source, ParsePosition parsePosition)
      {
        return null;
      }
    };
    return nf;
  }

  /**
   * Show hide the series associated with a curve.
   * @param curveId Curve identifier.
   * @param visible <code>true</code> to make it visible, <code>false</code> otherwise.
   */
  public void setVisible(String curveId, boolean visible)
  {
    Integer index=_curveIds2SeriesIndex.get(curveId);
    if (index!=null)
    {
      if (_chart!=null)
      {
        XYPlot plot=_chart.getXYPlot();
        XYItemRenderer renderer=plot.getRenderer();
        renderer.setSeriesVisible(index.intValue(),Boolean.valueOf(visible));
      }
    }
  }

  /**
   * Refresh chart data from the underlying statistics.
   */
  public void refresh()
  {
    if (_chart==null)
    {
      return;
    }
    XYSeriesCollection data=(XYSeriesCollection)_chart.getXYPlot().getDataset();
    List<String> curveIds=_provider.getCurveIds();

    Set<String> hiddenCurves=getHiddenCurves();

    _curveIds2SeriesIndex.clear();
    data.removeAllSeries();
    // Added new curves
    for(String curveID : curveIds)
    {
      addSeriesForCurve(data,curveID);
      boolean hidden=hiddenCurves.contains(curveID);
      setVisible(curveID,!hidden);
    }
    // Configuration
    XYPlot plot=(XYPlot)_chart.getPlot();
    configureTooltip(plot);
    configureValueAxis(plot);
  }

  private Set<String> getHiddenCurves()
  {
    Set<String> ret=new HashSet<String>();
    XYPlot plot=_chart.getXYPlot();
    XYItemRenderer renderer=plot.getRenderer();
    for(Map.Entry<String,Integer> entry : _curveIds2SeriesIndex.entrySet())
    {
      String curveId=entry.getKey();
      int index=entry.getValue().intValue();
      boolean visible=renderer.isSeriesVisible(index);
      if (!visible)
      {
        ret.add(curveId);
      }
    }
    return ret;
  }

  private XYSeriesCollection createDataset()
  {
    XYSeriesCollection data=new XYSeriesCollection();
    List<String> curveIds=_provider.getCurveIds();
    for(String curveId : curveIds)
    {
      addSeriesForCurve(data,curveId);
    }
    return data;
  }

  private void addSeriesForCurve(XYSeriesCollection data, String curveId)
  {
    boolean useSquareMoves=_configuration.useSquareMoves();
    DatedCurve<?> curve=_provider.getCurve(curveId);
    if (curve!=null)
    {
      String key=curve.getName();
      XYSeries series = new XYSeries(key);
      long lastDate=0;
      double lastValue=0;
      for(DatedCurveItem<?> point : curve.getPoints())
      {
        long date=point.getDate().longValue();
        Object objectValue=point.getValue();
        if (objectValue instanceof Number)
        {
          double value=((Number)objectValue).doubleValue();
          if (useSquareMoves)
          {
            if ((lastDate!=0) && (lastDate!=date))
            {
              series.add(date,lastValue);
            }
          }
          series.add(date,value);
          lastDate=date;
          lastValue=value;
        }
      }
      int index=data.getSeriesCount();
      _curveIds2SeriesIndex.put(curveId,Integer.valueOf(index));
      data.addSeries(series);
    }
    else
    {
      LOGGER.warn("Cannot find curve with id: {}",curveId);
    }
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
    _chart=null;
    // Data
    _provider=null;
  }
}
