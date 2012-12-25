package delta.games.lotro.stats.warbands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import delta.games.lotro.lore.warbands.WarbandDefinition;

/**
 * Statistics for one warband for a single toon.
 * @author DAM
 */
public class WarbandStats
{
  private WarbandDefinition _definition;
  private Long _lastDate;
  private List<Long> _dates;
  private int _nbTimes;
  
  /**
   * Constructor.
   * @param definition Definition of the associated warband.
   */
  public WarbandStats(WarbandDefinition definition)
  {
    _definition=definition;
    _dates=new ArrayList<Long>();
  }

  /**
   * Get the associated warband.
   * @return A warband definition.
   */
  public WarbandDefinition getDefinition()
  {
    return _definition;
  }

  /**
   * Add a warband quest completion.
   * @param time Date of completion.
   */
  public void add(long time)
  {
    Long t=Long.valueOf(time);
    if (!_dates.contains(t))
    {
      _dates.add(t);
      Collections.sort(_dates);
      _lastDate=_dates.get(_dates.size()-1);
    }
    _nbTimes++;
  }

  /**
   * Get completion dates.
   * @return A possibly empty array of timestamps.
   */
  public long[] getDates()
  {
    long[] ret=new long[_dates.size()];
    int i=0;
    for(Long date : _dates)
    {
      ret[i]=date.longValue();
      i++;
    }
    return ret;
  }

  /**
   * Get the number of quest completion for this warband.
   * @return A number. 
   */
  public int getNumberOfTimes()
  {
    return _nbTimes;
  }

  /**
   * Get the most recent completion date.
   * @return A timestamp or <code>null</code> if no completion.
   */
  public Long getMostRecentDate()
  {
    return _lastDate;
  }

  /**
   * Dump the contents of this object to the given stream.
   * @param ps Output stream to use.
   */
  public void dump(PrintStream ps)
  {
    String warbandName=_definition.getName();
    ps.println("Warband statistics for warband ["+warbandName+"]: x"+_nbTimes);
    for(Long date : _dates)
    {
      ps.println("\t"+new Date(date.longValue()));
    }
  }
}
