package delta.games.lotro.gui.utils;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatUtils;

/**
 * Utilities to display stat values.
 * @author DAM
 */
public class StatDisplayUtils
{
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
        String line=StatUtils.getStatDisplay(stat,stats);
        sb.append(line).append(EndOfLine.NATIVE_EOL);
      }
    }
    String text=sb.toString().trim();
    String html="<html>"+text.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
    return html;
  }
}
