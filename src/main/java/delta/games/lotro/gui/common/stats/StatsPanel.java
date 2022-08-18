package delta.games.lotro.gui.common.stats;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.StatsSetElement;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.common.stats.StatsProvider;

/**
 * Stats panel utilities.
 * @author DAM
 */
public class StatsPanel
{
  /**
   * Fill a panel with stats.
   * @param panel Panel to use.
   * @param stats Stats to show.
   * @param referenceStats Reference stats (to show a percentage).
   */
  public static void fillStatsPanel(JPanel panel, BasicStatsSet stats, BasicStatsSet referenceStats)
  {
    panel.removeAll();

    int rowIndex=0;
    GridBagConstraints strutConstraints=new GridBagConstraints(0,rowIndex,3,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    panel.add(Box.createHorizontalStrut(100),strutConstraints);
    rowIndex++;

    int statsCount=stats.getStatsCount();
    if (statsCount>0)
    {
      // Build display
      for(StatsSetElement element : stats.getStatElements())
      {
        StatDescription stat=element.getStat();
        if (!stat.isVisible())
        {
          continue;
        }
        Number value=stats.getStat(stat);
        if (value==null)
        {
          continue;
        }
        String valueStr=StatUtils.getStatDisplay(element);
        if (valueStr==null)
        {
          continue;
        }
        JLabel valueLabel=GuiFactory.buildLabel(valueStr);
        GridBagConstraints c=new GridBagConstraints(0,rowIndex,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
        panel.add(valueLabel,c);
        if (referenceStats!=null)
        {
          // Percentage
          Number statValue=referenceStats.getStat(stat);
          String percentageStr="";
          if (statValue!=null)
          {
            float percentage=100*(value.floatValue()/statValue.floatValue());
            percentageStr=L10n.getString(percentage,1)+"%";
          }
          JLabel percentageLabel=GuiFactory.buildLabel(percentageStr);
          c=new GridBagConstraints(2,rowIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
          panel.add(percentageLabel,c);
        }
        rowIndex++;
      }
    }
  }

  /**
   * Fill a panel with stats.
   * @param panel Panel to use.
   * @param stats Stats to show.
   */
  public static void fillStatsPanel(JPanel panel, BasicStatsSet stats)
  {
    fillStatsPanel(panel,stats,(StatsProvider)null);
  }

  /**
   * Fill a panel with stats.
   * @param panel Panel to use.
   * @param stats Stats to show.
   * @param provider Source provider (to display special effects).
   */
  public static void fillStatsPanel(JPanel panel, BasicStatsSet stats, StatsProvider provider)
  {
    panel.removeAll();

    int rowIndex=0;
    GridBagConstraints strutConstraints=new GridBagConstraints(0,rowIndex,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    panel.add(Box.createHorizontalStrut(100),strutConstraints);
    rowIndex++;

    int statsCount=stats.getStatsCount();
    if (statsCount>0)
    {
      String[] lines=StatUtils.getFullStatsDisplay(stats,provider);
      for(String line : lines)
      {
        JLabel label=GuiFactory.buildLabel(line);
        GridBagConstraints c=new GridBagConstraints(0,rowIndex,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
        panel.add(label,c);
        rowIndex++;
      }
    }
  }
}
