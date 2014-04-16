package delta.games.lotro.gui.stats.levelling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnitSource;
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

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.level.LevelHistory;
import delta.games.lotro.character.level.MultipleToonsLevellingStats;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.utils.Formats;

/**
 * Controller for level chart.
 * @author DAM
 */
public class CharacterLevelChartController
{
  // GUI
  private JPanel _panel;
  private JFreeChart _chart;
  // Data
  private MultipleToonsLevellingStats _stats;
  // Configuration
  private boolean _fluent=false;
  // Internal cooking
  private HashMap<String,Integer> _toonID2SeriesIndex;

  /**
   * Constructor.
   * @param stats Data to display.
   */
  public CharacterLevelChartController(MultipleToonsLevellingStats stats)
  {
    _stats=stats;
    _toonID2SeriesIndex=new HashMap<String,Integer>();
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
    String title="Characters levelling";
    String timeAxisLabel="Time";
    String valueAxisLabel="Level";

    XYDataset xydataset=createDataset();
    JFreeChart jfreechart=ChartFactory.createTimeSeriesChart(title, timeAxisLabel, valueAxisLabel, xydataset, true, true, false);

    Color foregroundColor=GuiFactory.getForegroundColor();
    Paint backgroundPaint=GuiFactory.getBackgroundPaint();
    jfreechart.setBackgroundPaint(backgroundPaint);
    
    TextTitle t=new TextTitle(title);
    t.setFont(t.getFont().deriveFont(24.0f));
    t.setPaint(foregroundColor);
    jfreechart.setTitle(t);

    XYPlot plot = jfreechart.getXYPlot();
    plot.setDomainPannable(false);
    
    XYToolTipGenerator tooltip=new StandardXYToolTipGenerator() {
      @Override
      public String generateLabelString(XYDataset dataset, int series, int item)
      {
        String name=(String)((XYSeriesCollection)dataset).getSeriesKey(series);
        int level=(int)dataset.getYValue(series,item);
        double timestamp=dataset.getXValue(series,item);
        String date=Formats.getDateString(Long.valueOf((long)timestamp));
        return name+" - "+level+" ("+date+")";
      }
    };
    XYItemRenderer renderer=plot.getRenderer();
    renderer.setBaseToolTipGenerator(tooltip);

    // Time axis
    DateAxis axis = (DateAxis) plot.getDomainAxis();
    SimpleDateFormat sdf=Formats.getDateFormatter();
    axis.setDateFormatOverride(sdf);
    axis.setAxisLinePaint(foregroundColor);
    axis.setLabelPaint(foregroundColor);
    axis.setTickLabelPaint(foregroundColor);

    // Level axis
    NumberAxis valueAxis = (NumberAxis)plot.getRangeAxis();
    valueAxis.setAutoRange(true);
    valueAxis.setAxisLinePaint(foregroundColor);
    valueAxis.setLabelPaint(foregroundColor);
    valueAxis.setTickLabelPaint(foregroundColor);
    TickUnitSource ticks=getLevelTicks();
    valueAxis.setStandardTickUnits(ticks);

    LegendTitle legend=jfreechart.getLegend();
    legend.setPosition(RectangleEdge.BOTTOM);
    legend.setItemPaint(foregroundColor);
    legend.setBackgroundPaint(backgroundPaint);
    return jfreechart;
  }

  private TickUnitSource getLevelTicks()
  {
    TickUnits ret=new TickUnits();
    ret.add(new NumberTickUnit(1));
    ret.add(new NumberTickUnit(5));
    ret.add(new NumberTickUnit(10));
    return ret;
  }

  /**
   * Show hide the series associated with a toon.
   * @param toonId Toon identifier.
   * @param visible <code>true</code> to make it visible, <code>false</code> otherwise.
   */
  public void setVisible(String toonId, boolean visible)
  {
    Integer index=_toonID2SeriesIndex.get(toonId);
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
    XYSeriesCollection data=(XYSeriesCollection)_chart.getXYPlot().getDataset();
    List<CharacterFile> newToons=_stats.getToonsList();

    // Find added/removed toons
    ArrayList<Integer> indexesToRemove=new ArrayList<Integer>();
    HashSet<String> toonIDsToRemove=new HashSet<String>();
    for(String toonID : _toonID2SeriesIndex.keySet())
    {
      CharacterFile foundToon=null;
      for(CharacterFile toon : newToons)
      {
        if (toonID.equals(toon.getIdentifier()))
        {
          foundToon=toon;
          break;
        }
      }
      if (foundToon!=null)
      {
        newToons.remove(foundToon);
      }
      else
      {
        Integer index=_toonID2SeriesIndex.get(toonID);
        indexesToRemove.add(index);
        toonIDsToRemove.add(toonID);
      }
    }
    // Remove no more used toons
    for(String toonID : toonIDsToRemove)
    {
      _toonID2SeriesIndex.remove(toonID);
    }
    Collections.sort(indexesToRemove);
    for(int i=indexesToRemove.size()-1;i>=0;i--)
    {
      int index=indexesToRemove.get(i).intValue();
      data.removeSeries(index);
    }
    // Added new toons
    for(CharacterFile toon : newToons)
    {
      addSeriesForToon(data,toon);
      String toonID=toon.getIdentifier();
      setVisible(toonID,true);
    }
 }

  private XYSeriesCollection createDataset()
  {
    XYSeriesCollection data=new XYSeriesCollection();  
    List<CharacterFile> toons=_stats.getToonsList();
    for(CharacterFile toon : toons)
    {
      addSeriesForToon(data,toon);
    }
    return data;  
  }  

  private void addSeriesForToon(XYSeriesCollection data, CharacterFile toon)
  {
    String toonID=toon.getIdentifier();
    LevelHistory stats=_stats.getStatsForToon(toonID);
    if (stats!=null)
    {
      String key=stats.getName();
      XYSeries toonSeries = new XYSeries(key);
      int[] levels=stats.getLevels();
      long[] dates=stats.getDatesSortedByLevel();
      long lastDate=0;
      int lastLevel=0;
      for(int i=0;i<levels.length;i++)
      {
        long date=dates[i];
        int level=levels[i];
        if (!_fluent)
        {
          if ((lastDate!=0) && (lastDate!=date))
          {
            toonSeries.add(date,lastLevel);
          }
        }
        toonSeries.add(date,level);
        lastDate=date;
        lastLevel=level;
      }
      int index=data.getSeriesCount();
      _toonID2SeriesIndex.put(toonID,Integer.valueOf(index));
      data.addSeries(toonSeries);
    }
    else
    {
      // TODO error
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
    _stats=null;
  }
}
