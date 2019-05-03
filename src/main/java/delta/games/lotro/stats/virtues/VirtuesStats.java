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
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.common.rewards.RewardElement;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.VirtueReward;
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
  private HashMap<VirtueId,List<String>> _virtues;

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
  public VirtueId[] getVirtues()
  {
    Set<VirtueId> virtues=_virtues.keySet();
    VirtueId[] ret=virtues.toArray(new VirtueId[virtues.size()]);
    return ret;
  }

  /**
   * Get quest/deed identifiers for a given virtue.
   * @param virtue Virtue to use.
   * @return An array of quest/deed identifiers, or <code>null</code>.
   */
  public String[] getIDsForAVirtue(VirtueId virtue)
  {
    String[] ret=null;
    if (virtue!=null)
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
    _virtues=new HashMap<VirtueId,List<String>>();
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
          Rewards rewards=quest.getRewards();
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
      Integer id=item.getResourceIdentifier();
      if (id!=null)
      {
        DeedDescription deed=dm.getDeed(id.intValue());
        if (deed!=null)
        {
          Rewards rewards=deed.getRewards();
          String name=deed.getName();
          handleRewards("Deed:"+name,rewards);
        }
      }
    }
  }

  private void handleRewards(String id, Rewards rewards)
  {
    for(RewardElement rewardElement : rewards.getRewardElements())
    {
      if (rewardElement instanceof VirtueReward)
      {
        VirtueReward virtueReward=(VirtueReward)rewardElement;
        VirtueId virtueId=virtueReward.getIdentifier();
        List<String> items=_virtues.get(virtueId);
        if (items==null)
        {
          items=new ArrayList<String>();
          _virtues.put(virtueId,items);
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

    Set<VirtueId> virtues=_virtues.keySet();
    List<VirtueId> sortedVirtues=new ArrayList<VirtueId>(virtues);
    Collections.sort(sortedVirtues);
    for(VirtueId virtue : sortedVirtues)
    {
      List<String> items=_virtues.get(virtue);
      int nb=items.size();
      ps.println(virtue+" ("+nb+"): "+items);
    }
  }
}
