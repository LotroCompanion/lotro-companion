package delta.games.lotro.stats.levelling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import delta.common.utils.NumericTools;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;

/**
 * Levelling statistics for a toon. 
 * @author DAM
 */
public class LevellingStats
{
  private String _name;
  private Long[] _dates;
  private Integer[] _levels;
  
  /**
   * Constructor.
   * @param log Character log to use.
   */
  public LevellingStats(CharacterLog log)
  {
    List<CharacterLogItem> items=getLevelItems(log);
    parseLevelUpItems(items);
    _name=log.getName();
  }

  /**
   * Get the name of the managed toon.
   * @return a toon name.
   */
  public String getName()
  {
    return _name;
  }

  private void parseLevelUpItems(List<CharacterLogItem> items)
  {
    List<Long> dates=new ArrayList<Long>();
    List<Integer> levels=new ArrayList<Integer>();
    for(CharacterLogItem item : items)
    {
      if (item!=null)
      {
        String label=item.getLabel();
        int level=NumericTools.parseInt(label,0);
        long date=item.getDate();
        if ((level!=0) && (date!=0))
        {
          dates.add(Long.valueOf(date));
          levels.add(Integer.valueOf(level));
        }
      }
    }
    _dates=dates.toArray(new Long[dates.size()]);
    _levels=levels.toArray(new Integer[levels.size()]);
  }
  
  /**
   * Get the date for a given item.
   * @param item Targeted item. 
   * @return A date.
   */
  public Long getDate(int item)
  {
    return _dates[item];
  }

  /**
   * Get the level for a given item.
   * @param item Targeted item. 
   * @return A level.
   */
  public Integer getLevel(int item)
  {
    return _levels[item];
  }

  /**
   * Get the number of level change items.
   * @return a positive number.
   */
  public int getNumberOfItems()
  {
    return _dates.length;
  }

  private List<CharacterLogItem> getLevelItems(CharacterLog log)
  {
    List<CharacterLogItem> ret=new ArrayList<CharacterLogItem>();
    int nb=log.getNbItems();
    for(int i=0;i<nb;i++)
    {
      CharacterLogItem item=log.getLogItem(i);
      if ((item!=null) && (item.getLogItemType()==LogItemType.LEVELUP))
      {
        ret.add(item);
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
    int nbItems=getNumberOfItems();
    for(int i=0;i<nbItems;i++)
    {
      if (i>0)
      {
        sb.append(", ");
      }
      Long date=getDate(i);
      Integer level=getLevel(i);
      sb.append(level).append(": ").append(new Date(date.longValue()));
    }
    return sb.toString();
  }
}
