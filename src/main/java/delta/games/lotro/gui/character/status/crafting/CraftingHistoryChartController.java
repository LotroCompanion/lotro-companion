package delta.games.lotro.gui.character.status.crafting;

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
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.l10n.LocalizedFormats;
import delta.games.lotro.character.status.crafting.ProfessionStatus;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.utils.Formats;

/**
 * Controller for crafting history chart.
 * @author DAM
 */
public class CraftingHistoryChartController
{
  private JPanel _panel;
  private JFreeChart _chart;
  private ProfessionStatus _stats;
  private boolean _showTitle;
  private XYSeriesCollection _data;

  /**
   * Constructor.
   * @param stats Data to display.
   * @param showTitle Show title or not.
   */
  public CraftingHistoryChartController(ProfessionStatus stats, boolean showTitle)
  {
    _stats=stats;
    _showTitle=showTitle;
    _data = new XYSeriesCollection();
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

  private JFreeChart buildChart(){
    final Profession profession=_stats.getProfession();
    String title="";
    if (_showTitle)
    {
      title=profession.getName();
    }
    updateData();
    JFreeChart jfreechart = ChartFactory.createXYStepChart(title,
                        "Time", // I18n
                        "Tier", // I18n
                        _data,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false);

    Color foregroundColor=GuiFactory.getForegroundColor();
    Paint backgroundPaint=GuiFactory.getBackgroundPaint();
    jfreechart.setBackgroundPaint(backgroundPaint);
    TextTitle t=new TextTitle(title);
    t.setFont(t.getFont().deriveFont(24.0f));
    t.setPaint(foregroundColor);
    jfreechart.setTitle(t);
    XYPlot xyplot = (XYPlot)jfreechart.getPlot();
    xyplot.setDomainPannable(false);
    XYStepAreaRenderer xysteparearenderer = new XYStepAreaRenderer(XYStepAreaRenderer.AREA_AND_SHAPES);
    XYToolTipGenerator tooltip=new StandardXYToolTipGenerator() {
      @Override
      public String generateLabelString(XYDataset dataset, int series, int item)
      {
        int tier=(int)dataset.getYValue(series,item);
        CraftingLevel level=profession.getByTier(tier);
        String label=level.getCraftTier().getLabel();
        double timestamp=dataset.getXValue(series,item);
        String date=Formats.getDateString(Long.valueOf((long)timestamp));
        return label+" ("+date+")";
      }
    };
    xysteparearenderer.setBaseToolTipGenerator(tooltip);
    xysteparearenderer.setSeriesPaint(0,new Color(255,235,31));
    xysteparearenderer.setSeriesPaint(1,new Color(130,80,57));
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
    CraftingLevel maxLevel=profession.getMaximumLevel();
    valueAxis.setRange(0,maxLevel.getTier());
    NumberFormat nf=new NumberFormat()
    {
      private String format(int number)
      {
        CraftingLevel level=profession.getByTier(number);
        String ret=(level!=null)?level.getCraftTier().getLabel():"???";
        return ret;
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

  /**
   * Update graph data.
   */
  public void updateData()
  {
    _data.removeAllSeries();
    Long lastItemDate=_stats.getValidityDate();
    long lastProficiency=0;
    long lastMastery=0;

    XYSeries proficiencySeries = new XYSeries("Proficiency"); // I18n
    Profession profession=_stats.getProfession();
    CraftingLevel maxLevel=profession.getMaximumLevel();
    int maxTier=maxLevel.getTier();
    for(int i=0;i<=maxTier;i++)
    {
      long date=_stats.getLevelStatus(i).getProficiency().getCompletionDate();
      if (date!=0)
      {
        proficiencySeries.add(date,i);
        if (lastProficiency<date)
        {
          lastProficiency=date;
        }
      }
    }
    XYSeries masterySeries = new XYSeries("Mastery"); // I18n
    for(int i=0;i<=maxTier;i++)
    {
      long date=_stats.getLevelStatus(i).getMastery().getCompletionDate();
      if (date!=0)
      {
        masterySeries.add(date,i);
        if (lastMastery<date)
        {
          lastMastery=date;
        }
      }
    }

    // Set last point
    if (lastItemDate!=null)
    {
      if (lastItemDate.longValue()>lastProficiency)
      {
        CraftingLevel currentProficiency=_stats.getProficiencyLevel();
        proficiencySeries.add(lastItemDate.longValue(),currentProficiency.getTier());
      }
      if (lastItemDate.longValue()>lastMastery)
      {
        CraftingLevel currentMastery=_stats.getMasteryLevel();
        masterySeries.add(lastItemDate.longValue(),currentMastery.getTier());
      }
    }

    _data.addSeries(masterySeries);
    _data.addSeries(proficiencySeries);
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
