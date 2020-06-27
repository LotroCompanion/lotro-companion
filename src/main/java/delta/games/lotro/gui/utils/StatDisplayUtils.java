package delta.games.lotro.gui.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import delta.common.utils.text.EndOfLine;
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
    List<String> lines=new ArrayList<String>();
    for(StatDescription stat : stats.getSortedStats())
    {
      if (stat.isVisible())
      {
        String line=getStatDisplay(stat,stats);
        lines.add(line);
      }
    }
    String[] ret=lines.toArray(new String[lines.size()]);
    return ret;
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

  /**
   * Build a tooltip that shows stats.
   * @param firstLine Initial label.
   * @param stats Stats to show.
   * @return the computed HTML fragment for a tooltip.
   */
  public static String buildToolTip(String firstLine, BasicStatsSet stats)
  {
    StringBuilder sb=new StringBuilder();
    sb.append(firstLine).append(EndOfLine.NATIVE_EOL);
    if (stats!=null)
    {
      for(StatDescription stat : stats.getStats())
      {
        String line=StatDisplayUtils.getStatDisplay(stat,stats);
        sb.append(line).append(EndOfLine.NATIVE_EOL);
      }
    }
    String text=sb.toString().trim();
    String html="<html>"+text.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
    return html;
  }
}
