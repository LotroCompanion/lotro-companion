package delta.games.lotro.gui.stats.crafting;

import java.awt.Color;
import java.awt.Dimension;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

import delta.games.lotro.crafting.CraftingLevel;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.stats.crafting.ProfessionStat;

/**
 * Controller for level chart.
 * @author DAM
 */
public class CraftingHistoryChartController
{
  private JPanel _panel;
  private JFreeChart _chart;
  private ProfessionStat _stats;
  private boolean _showTitle;
  private SimpleDateFormat _datesFormatter;

  /**
   * Constructor.
   * @param stats Data to display.
   * @param showTitle Show title or not.
   */
  public CraftingHistoryChartController(ProfessionStat stats, boolean showTitle)
  {
    _stats=stats;
    _showTitle=showTitle;
    _datesFormatter=new SimpleDateFormat("yyyy-MM-dd");
    _datesFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
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
    ChartPanel chartPanel = new ChartPanel(_chart);
    chartPanel.setDomainZoomable(true);
    chartPanel.setRangeZoomable(true);
    chartPanel.setHorizontalAxisTrace(false);
    chartPanel.setVerticalAxisTrace(false);
    chartPanel.setPreferredSize(new Dimension(500,300));
    return chartPanel;
  }

  private JFreeChart buildChart(){
    String title="";
    if (_showTitle)
    {
      title=_stats.getProfession();
    }
    XYDataset xydataset=createDataset();
    JFreeChart jfreechart = ChartFactory.createXYStepChart(title,   
                        "Time",   
                        "Tier",   
                        xydataset,   
                        PlotOrientation.VERTICAL,   
                        true,   
                        true,   
                        false);  
      
    Color foregroundColor=GuiFactory.getForegroundColor();
    Color backgroundColor=GuiFactory.getBackgroundColor();
    jfreechart.setBackgroundPaint(backgroundColor);
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
        String label;
        int tier=(int)dataset.getYValue(series,item);
        if (tier==0)
        {
          label="Started profession";
        }
        else
        {
          CraftingLevel level=CraftingLevel.getByTier(tier);
          if (level!=null)
          {
            if (series==0) label=level.getMasteryLabel();
            else if (series==1) label=level.getProficiencyLabel();
            else label="???";
          }
          else
          {
            label="???";
          }
        }
        double timestamp=dataset.getXValue(series,item);
        String date=_datesFormatter.format(new Date((long)timestamp));
        return label+" ("+date+")";
      }
    };
    xysteparearenderer.setBaseToolTipGenerator(tooltip);
    xysteparearenderer.setSeriesPaint(0,new Color(255,235,31));
    xysteparearenderer.setSeriesPaint(1,new Color(130,80,57));
    xyplot.setRenderer(xysteparearenderer);  

    DateAxis axis = (DateAxis) xyplot.getDomainAxis();
    axis.setDateFormatOverride(_datesFormatter);
    axis.setAxisLinePaint(foregroundColor);
    axis.setLabelPaint(foregroundColor);
    axis.setTickLabelPaint(foregroundColor);
    NumberAxis valueAxis = (NumberAxis)xyplot.getRangeAxis();
    valueAxis.setAutoRange(false);
    valueAxis.setAxisLinePaint(foregroundColor);
    valueAxis.setLabelPaint(foregroundColor);
    valueAxis.setTickLabelPaint(foregroundColor);
    CraftingLevel maxLevel=CraftingLevel.getMaximumLevel();
    valueAxis.setRange(0,maxLevel.getTier());
    NumberFormat nf=new NumberFormat()
    {
      private String format(int number)
      {
        CraftingLevel level=CraftingLevel.getByTier(number);
        String ret=(level!=null)?level.getProficiencyLabel():"???";
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
    legend.setBackgroundPaint(backgroundColor);
    
    return jfreechart;
  }  

  private XYDataset createDataset()
  {
    XYSeriesCollection data = new XYSeriesCollection();  

    Long lastItemDate=_stats.getLastLogItemDate();
    
    XYSeries proficiencySeries = new XYSeries("Proficiency");
    CraftingLevel maxLevel=CraftingLevel.getMaximumLevel();
    int maxTier=maxLevel.getTier();
    for(int i=0;i<=maxTier;i++)
    {
      Long date=_stats.getProficiencyTierDate(i);
      if (date!=null)
      {
        proficiencySeries.add(date.longValue(),i);  
      }
    }
    XYSeries masterySeries = new XYSeries("Mastery");  
    for(int i=0;i<=maxTier;i++)
    {
      Long date=_stats.getMasteryTierDate(i);
      if (date!=null)
      {
        masterySeries.add(date.longValue(),i);  
      }
    }

    // Set last point
    if (lastItemDate!=null)
    {
      int currentProficiency=_stats.getProficiencyTier();
      proficiencySeries.add(lastItemDate.longValue(),currentProficiency);
      int currentMastery=_stats.getMasteryTier();
      masterySeries.add(lastItemDate.longValue(),currentMastery);
    }
    
    data.addSeries(masterySeries);
    data.addSeries(proficiencySeries);
    return data;  
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
