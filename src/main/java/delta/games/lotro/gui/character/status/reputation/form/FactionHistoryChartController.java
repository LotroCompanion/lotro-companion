package delta.games.lotro.gui.character.status.reputation.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.utils.l10n.LocalizedFormats;
import delta.games.lotro.character.status.reputation.FactionLevelStatus;
import delta.games.lotro.character.status.reputation.FactionStatus;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.utils.Formats;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for faction history chart.
 * @author DAM
 */
public class FactionHistoryChartController extends AbstractPanelController
{
  private JFreeChart _chart;
  private FactionStatus _stats;
  private XYSeriesCollection _data;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param stats Data to display.
   */
  public FactionHistoryChartController(AreaController parent, FactionStatus stats)
  {
    super(parent);
    _stats=stats;
    _data=new XYSeriesCollection();
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    JPanel panel=super.getPanel();
    if (panel==null)
    {
      panel=buildPanel();
      setPanel(panel);
    }
    return panel;
  }

  private JPanel buildPanel()
  {
    _chart=buildChart();
    JPanel panel=buildChartPanel();
    return panel;
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
    updateData();
    JFreeChart jfreechart = ChartFactory.createXYStepChart("",
                        "Time", // I18n
                        "Level", // I18n
                        _data,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false);

    Color foregroundColor=GuiFactory.getForegroundColor();
    Paint backgroundPaint=GuiFactory.getBackgroundPaint();
    jfreechart.setBackgroundPaint(backgroundPaint);
    XYPlot xyplot = (XYPlot)jfreechart.getPlot();
    xyplot.setDomainPannable(false);
    XYStepAreaRenderer xysteparearenderer = new XYStepAreaRenderer(XYStepAreaRenderer.AREA_AND_SHAPES);
    final Faction faction=_stats.getFaction();
    XYToolTipGenerator tooltip=new StandardXYToolTipGenerator()
    {
      @Override
      public String generateLabelString(XYDataset dataset, int series, int item)
      {
        int tier=(int)dataset.getYValue(series,item);
        FactionLevel level=faction.getLevelByTier(tier);
        String label=getTierName(level);
        double timestamp=dataset.getXValue(series,item);
        String date=Formats.getDateString(Long.valueOf((long)timestamp));
        return label+" ("+date+")";
      }
    };
    xysteparearenderer.setBaseToolTipGenerator(tooltip);
    xysteparearenderer.setSeriesPaint(0,new Color(0,0,255));
    xyplot.setRenderer(xysteparearenderer);

    DateAxis axis = (DateAxis) xyplot.getDomainAxis();
    DateFormat sdf=LocalizedFormats.getDateFormat();
    axis.setDateFormatOverride(sdf);
    axis.setAxisLinePaint(foregroundColor);
    axis.setLabelPaint(foregroundColor);
    axis.setTickLabelPaint(foregroundColor);
    NumberAxis valueAxis = (NumberAxis)xyplot.getRangeAxis();
    valueAxis.setAutoRange(false);
    valueAxis.setAxisLinePaint(foregroundColor);
    valueAxis.setLabelPaint(foregroundColor);
    valueAxis.setTickLabelPaint(foregroundColor);
    FactionLevel[] levels=faction.getLevels();
    final int min=levels[0].getTier();
    int max=levels[levels.length-1].getTier();
    valueAxis.setRange(min,max);
    NumberFormat nf=new NumberFormat()
    {
      private String format(int number)
      {
        FactionLevel level=faction.getLevelByTier(number);
        return getTierName(level);
      }

      @Override
      public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos)
      {
        return toAppendTo.append(format((int)number));
      }

      @Override
      public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos)
      {
        return toAppendTo.append(format((int)number));
      }

      @Override
      public Number parse(String source, ParsePosition parsePosition)
      {
        return null;
      }
    };
    valueAxis.setNumberFormatOverride(nf);
    LegendTitle legend=jfreechart.getLegend();
    legend.setItemPaint(foregroundColor);
    legend.setBackgroundPaint(backgroundPaint);

    return jfreechart;
  }

  private String getTierName(FactionLevel level)
  {
    String tierName=null;
    if (level!=null)
    {
      String rawName=level.getName();
      tierName=ContextRendering.render(this,rawName);
    }
    else
    {
      tierName="?";
    }
    return tierName;
  }

  /**
   * Update graph data.
   */
  public void updateData()
  {
    _data.removeAllSeries();

    XYSeries series = new XYSeries("History"); // I18n
    Faction faction=_stats.getFaction();
    FactionLevel[] levels=faction.getLevels();
    for(FactionLevel level : levels)
    {
      FactionLevelStatus levelStatus=_stats.getStatusForLevel(level);
      if (levelStatus!=null)
      {
        long date=levelStatus.getCompletionDate();
        if (date!=0)
        {
          series.add(date,level.getTier());
        }
      }
    }
    _data.addSeries(series);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    _chart=null;
    _stats=null;
  }
}
