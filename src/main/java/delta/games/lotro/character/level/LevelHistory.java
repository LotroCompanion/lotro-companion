package delta.games.lotro.character.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Levelling statistics for a toon. 
 * @author DAM
 */
public class LevelHistory
{
  private String _name;
  private HashMap<Integer,Long> _dates;
  
  /**
   * Constructor.
   * @param name Character name.
   */
  public LevelHistory(String name)
  {
    _name=name;
    _dates=new HashMap<Integer,Long>();
  }

  /**
   * Set a level.
   * @param level Level to set.
   * @param date Date for this level.
   */
  public void setLevel(int level, long date)
  {
    Integer l=Integer.valueOf(level);
    Long currentDate=_dates.get(l);
    if ((currentDate==null) || (currentDate.longValue()>date))
    {
      _dates.put(l,Long.valueOf(date));
    }
  }

  /**
   * Get the name of the managed toon.
   * @return a toon name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get an ordered array of managed levels.
   * @return an array of levels.
   */
  public int[] getLevels()
  {
    List<Integer> levels=new ArrayList<Integer>(_dates.keySet());
    Collections.sort(levels);
    int nb=levels.size();
    int[] ret=new int[nb];
    for(int i=0;i<nb;i++) ret[i]=levels.get(i).intValue();
    return ret;
  }
  
  /**
   * Get an array of dates sorted by level.
   * @return an array of dates.
   */
  public long[] getDatesSortedByLevel()
  {
    int[] levels=getLevels();
    int nbLevels=levels.length;
    long[] dates=new long[nbLevels];
    for(int i=0;i<nbLevels;i++)
    {
      dates[i]=_dates.get(Integer.valueOf(levels[i])).longValue();
    }
    return dates;
  }

  /**
   * Get the date for a given level.
   * @param level Targeted level. 
   * @return A date or <code>null</code> if not known.
   */
  public Long getDate(int level)
  {
    return _dates.get(Integer.valueOf(level));
  }

  /**
   * Get the maximum level.
   * @return A level or <code>null</code> if no items in this history.
   */
  public Integer getMaxLevel()
  {
    Integer ret=null;
    for(Map.Entry<Integer,Long> item  : _dates.entrySet())
    {
      int level=item.getKey().intValue();
      if ((ret==null) || ((ret!=null) && (ret.intValue()<level)))
      {
        ret=Integer.valueOf(level);
      }
    }
    return ret;
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Levelling stats for ").append(_name);
    sb.append(": ");
    List<Integer> levels=new ArrayList<Integer>(_dates.keySet());
    Collections.sort(levels);
    int nbItems=levels.size();
    for(int i=0;i<nbItems;i++)
    {
      if (i>0)
      {
        sb.append(", ");
      }
      int level=levels.get(i).intValue();
      Long date=getDate(level);
      sb.append(level).append(": ").append(new Date(date.longValue()));
    }
    return sb.toString();
  }
}
