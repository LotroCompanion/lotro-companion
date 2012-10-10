package delta.games.lotro.stats.virtues;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.Virtue;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Statistics on virtues on a single toon.
 * @author DAM
 */
public class VirtuesStats
{
  private String _name;
  private HashMap<String,List<String>> _virtues;

  /**
   * Constructor.
   * @param log Character log to use.
   */
  public VirtuesStats(CharacterLog log)
  {
    _name=log.getName();
    reset();
    List<CharacterLogItem> questItems=getLogItems(log,LogItemType.QUEST);
    parseQuestItems(questItems);
    List<CharacterLogItem> deedItems=getLogItems(log,LogItemType.DEED);
    parseDeedItems(deedItems);
  }

  /**
   * Get all virtues.
   * @return An possibly empty array of virtues.
   */
  public String[] getVirtues()
  {
    Set<String> virtues=_virtues.keySet();
    String[] ret=virtues.toArray(new String[virtues.size()]);
    return ret;
  }

  /**
   * Get quest/deed identifiers for a given virtue.
   * @param virtue Virtue to use. 
   * @return An array of quest/deed identifiers, or <code>null</code>.
   */
  public String[] getIDsForAVirtue(String virtue)
  {
    String[] ret=null;
    if ((virtue!=null) && (virtue.length()>0))
    {
      List<String> ids=_virtues.get(virtue);
      if (ids!=null)
      {
        ret=ids.toArray(new String[ids.size()]);
      }
    }
    return ret;
  }

  private void reset()
  {
    _virtues=new HashMap<String,List<String>>();
  }

  private List<CharacterLogItem> getLogItems(CharacterLog log, LogItemType type)
  {
    List<CharacterLogItem> ret=new ArrayList<CharacterLogItem>();
    int nb=log.getNbItems();
    for(int i=0;i<nb;i++)
    {
      CharacterLogItem item=log.getLogItem(i);
      if ((item!=null) && (item.getLogItemType()==type))
      {
        ret.add(item);
      }
    }
    return ret;
  }

  private void parseQuestItems(List<CharacterLogItem> items)
  {
    QuestsManager qm=QuestsManager.getInstance();
    for(CharacterLogItem item : items)
    {
      Integer id=item.getResourceIdentifier();
      if (id!=null)
      {
        QuestDescription quest=qm.getQuest(id.intValue());
        if (quest!=null)
        {
          Rewards rewards=quest.getQuestRewards();
          handleRewards("Quest:"+id,rewards);
        }
      }
    }
  }

  private void parseDeedItems(List<CharacterLogItem> items)
  {
    DeedsManager dm=DeedsManager.getInstance();
    for(CharacterLogItem item : items)
    {
      String id=item.getIdentifier();
      DeedDescription deed=dm.getDeed(id);
      if (deed!=null)
      {
        Rewards rewards=deed.getRewards();
        handleRewards("Deed:"+id,rewards);
      }
    }
  }

  private void handleRewards(String id, Rewards rewards)
  {
    Virtue[] virtues=rewards.getVirtues();
    if (virtues!=null)
    {
      for(Virtue virtue : virtues)
      {
        String name=virtue.getName();
        List<String> items=_virtues.get(name);
        if (items==null)
        {
          items=new ArrayList<String>();
          _virtues.put(name,items);
        }
        items.add(id);
      }
    }
  }

  /**
   * Dump the contents of this object to the given stream.
   * @param ps Output stream to use.
   * @param verbose Verbose output or not.
   */
  public void dump(PrintStream ps, boolean verbose)
  {
    ps.println("Virtues for ["+_name+"]:");
    
    Set<String> virtues=_virtues.keySet();
    List<String> sortedVirtues=new ArrayList<String>(virtues);
    Collections.sort(sortedVirtues);
    for(String virtue : sortedVirtues)
    {
      List<String> items=_virtues.get(virtue);
      int nb=items.size();
      ps.println(virtue+" ("+nb+"): "+items);
    }
  }
}
