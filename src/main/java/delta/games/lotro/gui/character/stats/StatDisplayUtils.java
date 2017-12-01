package delta.games.lotro.gui.character.stats;

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
        valueStr=String.format("%.1f%%",Double.valueOf(value.doubleValue()));
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
}
