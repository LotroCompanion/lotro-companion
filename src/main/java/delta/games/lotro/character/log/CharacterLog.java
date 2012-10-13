package delta.games.lotro.character.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Activity log for a single character.
 * @author DAM
 */
public class CharacterLog
{
  /**
   * Name of the character.
   */
  private String _characterName;
  
  /**
   * List of log items.
   */
  private List<CharacterLogItem> _logItems;
  
  /**
   * Constructor.
   * @param name Character name.
   */
  public CharacterLog(String name)
  {
    _characterName=name;
    _logItems=new ArrayList<CharacterLogItem>();
  }

  /**
   * Get the name of the involved character.
   * @return a character name.
   */
  public String getName()
  {
    return _characterName;
  }

  /**
   * Set the character name.
   * @param name Character name.
   */
  public void setName(String name)
  {
    _characterName=name;
  }

  /**
   * Get the number of items in this log.
   * @return a positive integer.
   */
  public int getNbItems()
  {
    return _logItems.size();
  }

  /**
   * Get a log item.
   * @param index Index of log item.
   * @return A log item.
   */
  public CharacterLogItem getLogItem(int index)
  {
    return _logItems.get(index);
  }

  /**
   * Add an item to this log.
   * @param item Item to add.
   */
  public void addLogItem(CharacterLogItem item)
  {
    _logItems.add(item);
  }

  /**
   * Add an item to this log.
   * @param item Item to add.
   * @param position Insertion index.
   */
  public void addLogItem(CharacterLogItem item, int position)
  {
    _logItems.add(position,item);
  }

  /**
   * Get all the items for a given date.
   * @param date Date to use.
   * @return A possibly empty list of items.
   */
  public List<CharacterLogItem> getItemsOfDay(long date)
  {
    List<CharacterLogItem> ret=new ArrayList<CharacterLogItem>();
    for(Iterator<CharacterLogItem> it=_logItems.iterator();it.hasNext();)
    {
      CharacterLogItem item=it.next();
      if (item.getDate()==date)
      {
        ret.add(item);
      }
    }
    return ret;
  }

  /**
   * Remove all the items for a given date.
   * @param date Date to use.
   * @return Number of removed items.
   */
  public int removeItemsOfDay(long date)
  {
    int nbRemoved=0;
    for(Iterator<CharacterLogItem> it=_logItems.iterator();it.hasNext();)
    {
      CharacterLogItem item=it.next();
      if (item.getDate()==date)
      {
        it.remove();
        nbRemoved++;
      }
    }
    return nbRemoved;
  }

  /**
   * Replace items of a given date.
   * @param date Targeted date.
   * @param items Items that replace existing ones.
   */
  public void replaceItemsOfDate(long date, List<CharacterLogItem> items)
  {
    removeItemsOfDay(date);
    int index=0;
    for(CharacterLogItem item : _logItems)
    {
      long itemDate=item.getDate();
      if (itemDate<date) break;
      index++;
    }
    int nbItems=items.size();
    for(int i=nbItems-1;i>=0;i--)
    {
      addLogItem(items.get(i),index);
    }
  }

  /**
   * Get a sorted list of dates found in the managed log items. 
   * @return A possibly empty list of dates.
   */
  public List<Long> getDates()
  {
    List<Long> ret=new ArrayList<Long>();
    long lastDate=-1;
    for(Iterator<CharacterLogItem> it=_logItems.iterator();it.hasNext();)
    {
      CharacterLogItem item=it.next();
      long date=item.getDate(); 
      if (date!=lastDate)
      {
        ret.add(Long.valueOf(date));
        lastDate=date;
      }
    }
    Collections.sort(ret);
    return ret;

  }

  @Override
  public String toString()
  {
    return _characterName+" activity log ("+_logItems.size()+" items)";
  }
}

