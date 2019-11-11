package delta.games.lotro.gui.utils;

import java.text.DecimalFormat;
import java.util.List;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Utilities to display stat values.
 * @author DAM
 */
public class StatDisplayUtils
{
  /**
   * Get a displayable string for a stat value.
   * @param value Value to display.
   * @param percentage Indicates if the stat is a percentage or not.
   * @return A displayable string.
   */
  public static String getStatDisplay(FixedDecimalsInteger value, boolean percentage)
  {
    String valueStr;
    if (value!=null)
    {
      if (percentage)
      {
        valueStr=new DecimalFormat("#.#%").format(value.doubleValue()/100);
      }
      else
      {
        valueStr=String.format("%d",Integer.valueOf(value.intValue()));
      }
    }
    else
    {
      valueStr="-";
    }
    return valueStr;
  }

  /**
   * Get the display lines for some stats.
   * @param stats Stats to display.
   * @return A possibly empty but not <code>null</code> array of stat lines.
   */
  public static String[] getStatsDisplayLines(BasicStatsSet stats)
  {
    List<StatDescription> statDescriptions=stats.getSortedStats();
    int nbStats=statDescriptions.size();
    String[] lines=new String[nbStats];
    for(int i=0;i<nbStats;i++)
    {
      StatDescription stat=statDescriptions.get(i);
      String line=getStatDisplay(stat,stats);
      lines[i]=line;
    }
    return lines;
  }

  /**
   * Get the display line for a single stat.
   * @param stat Stat to display.
   * @param stats Stats to get value from.
   * @return A displayable line.
   */
  public static String getStatDisplay(StatDescription stat, BasicStatsSet stats)
  {
    String statName=stat.getName();
    FixedDecimalsInteger value=stats.getStat(stat);
    String valueStr=getStatDisplay(value,stat.isPercentage());
    String line=valueStr+" "+statName;
    return line;
  }
}
