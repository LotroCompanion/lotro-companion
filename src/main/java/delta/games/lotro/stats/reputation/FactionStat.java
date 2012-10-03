package delta.games.lotro.stats.reputation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

import delta.games.lotro.common.Faction;
import delta.games.lotro.common.FactionLevel;

/**
 * Statistics about a single faction on a single toon.
 * @author DAM
 */
public class FactionStat
{
  private Faction _faction;
  private FactionLevel _level;
  private ArrayList<FactionLevel> _levels;
  private ArrayList<Long> _dates;

  /**
   * Constructor.
   * @param faction Faction.
   */
  public FactionStat(Faction faction)
  {
    _faction=faction;
    reset();
  }

  /**
   * Get the managed faction.
   * @return the managed faction.
   */
  public Faction getFaction()
  {
    return _faction;
  }

  /**
   * Add a reputation update event.
   * @param level New reputation level.
   * @param date Event date.
   */
  public void addUpdate(FactionLevel level, long date)
  {
    _level=level;
    _levels.add(level);
    _dates.add(Long.valueOf(date));
  }

  /**
   * Reset data.
   */
  private void reset()
  {
    _level=null;
    _levels=new ArrayList<FactionLevel>();
    _dates=new ArrayList<Long>();
  }

  /**
   * Get the number of registered reputation events.
   * @return A number of reputation events.
   */
  public int getNumberOfReputationEvents()
  {
    return _dates.size();
  }

  /**
   * Get the date for a given event.
   * @param index Index of event, starting at zero.
   * @return A date as a long.
   */
  public long getDateForEvent(int index)
  {
    return _dates.get(index).longValue();
  }

  /**
   * Get the faction level for a given event.
   * @param index Index of event, starting at zero.
   * @return A faction level.
   */
  public FactionLevel getLevelForEvent(int index)
  {
    return _levels.get(index);
  }

  /**
   * Get the current faction level.
   * @return the current faction level.
   */
  public FactionLevel getFactionLevel()
  {
    return _level;
  }

  /**
   * Dump the contents of this object to the given stream.
   * @param ps Output stream to use.
   */
  public void dump(PrintStream ps)
  {
    String factionName=_faction.getName();
    ps.println("Reputation history for faction ["+factionName+"]:");
    ps.println("\tLevel: "+_level);
    
    int nbEvents=getNumberOfReputationEvents();
    for(int i=0;i<nbEvents;i++)
    {
      Date date=new Date(_dates.get(i).longValue());
      FactionLevel level=_levels.get(i);
      ps.println("\t"+level+": "+date);
    }
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    String factionName=_faction.getName();
    sb.append(factionName);
    sb.append(": ");
    sb.append(_level);
    return sb.toString();
  }
}
