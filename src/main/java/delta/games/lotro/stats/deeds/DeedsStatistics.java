package delta.games.lotro.stats.deeds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.common.Emote;
import delta.games.lotro.common.Reputation;
import delta.games.lotro.common.ReputationItem;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.Title;
import delta.games.lotro.common.Virtue;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Gather statistics about a collection of deeds for a single character.
 * @author DAM
 */
public class DeedsStatistics
{
  private int _completedCount;
  private int _total;
  private int _acquiredLP;
  private int _classPoints;
  private int _marksCount;
  private int _medallionsCount;
  private List<String> _titles;
  private List<String> _emotes;
  private Map<VirtueId,VirtueStatsFromDeeds> _virtues;
  private Map<String,FactionStatsFromDeeds> _reputation;

  /**
   * Constructor.
   */
  public DeedsStatistics()
  {
    _titles=new ArrayList<String>();
    _emotes=new ArrayList<String>();
    _virtues=new HashMap<VirtueId,VirtueStatsFromDeeds>();
    _reputation=new HashMap<String,FactionStatsFromDeeds>();
    reset();
  }

  /**
   * Reset contents.
   */
  public void reset()
  {
    _completedCount=0;
    _total=0;
    _acquiredLP=0;
    _classPoints=0;
    _marksCount=0;
    _medallionsCount=0;
    _titles.clear();
    _emotes.clear();
    _virtues.clear();
    _reputation.clear();
  }

  /**
   * Compue statistics using the given deeds status and deeds.
   * @param deedsStatus Deeds status.
   * @param deeds Deeds to use.
   */
  public void useDeeds(DeedsStatusManager deedsStatus, List<DeedDescription> deeds)
  {
    reset();
    for(DeedDescription deed : deeds)
    {
      DeedStatus deedStatus=deedsStatus.get(deed.getKey(),false);
      if (deedStatus!=null)
      {
        useDeed(deedStatus,deed);
      }
      _total++;
    }
    Collections.sort(_titles);
    Collections.sort(_emotes);
  }

  private void useDeed(DeedStatus deedStatus, DeedDescription deed)
  {
    Boolean completed=deedStatus.isCompleted();
    if (completed==Boolean.TRUE)
    {
      _completedCount++;
      Rewards rewards=deed.getRewards();
      // LOTRO points
      int lp=rewards.getLotroPoints();
      _acquiredLP+=lp;
      // Class points
      int classPoints=rewards.getClassPoints();
      _classPoints+=classPoints;
      // Items
      ObjectsSet objects=rewards.getObjects();
      int nbItems=objects.getNbObjectItems();
      for(int i=0;i<nbItems;i++)
      {
        ObjectItem itemReward=objects.getItem(i);
        int itemId=itemReward.getItemId();
        // Marks
        if (itemId==1879224343)
        {
          _marksCount+=objects.getQuantity(i);
        }
        // Medallions
        if (itemId==1879224344)
        {
          _medallionsCount+=objects.getQuantity(i);
        }
      }
      // Titles
      Title[] titles=rewards.getTitles();
      if (titles!=null)
      {
        for(Title title : titles)
        {
          _titles.add(title.getName());
        }
      }
      // Emotes
      Emote[] emotes=rewards.getEmotes();
      if (emotes!=null)
      {
        for(Emote emote : emotes)
        {
          _emotes.add(emote.getName());
        }
      }
      // Virtues
      Virtue[] virtueRewards=rewards.getVirtues();
      if (virtueRewards!=null)
      {
        for(Virtue virtueReward : virtueRewards)
        {
          VirtueId virtueId=virtueReward.getIdentifier();
          VirtueStatsFromDeeds virtueStats=_virtues.get(virtueId);
          if (virtueStats==null)
          {
            virtueStats=new VirtueStatsFromDeeds(virtueId);
            _virtues.put(virtueId,virtueStats);
          }
          int points=virtueReward.getCount();
          virtueStats.add(points);
        }
      }
      // Reputation
      Reputation reputation=rewards.getReputation();
      ReputationItem[] items=reputation.getItems();
      for(ReputationItem item : items)
      {
        Faction faction=item.getFaction();
        String factionKey=faction.getKey();
        FactionStatsFromDeeds factionStats=_reputation.get(factionKey);
        if (factionStats==null)
        {
          factionStats=new FactionStatsFromDeeds(faction);
          _reputation.put(faction.getKey(),factionStats);
        }
        int amount=item.getAmount();
        factionStats.add(amount);
      }
    }
  }

  /**
   * Get the number of completed deeds.
   * @return A number of deeds.
   */
  public int getCompletedCount()
  {
    return _completedCount;
  }

  /**
   * Get the total number of deeds.
   * @return A number of deeds.
   */
  public int getTotalCount()
  {
    return _total;
  }

  /**
   * Get the number of acquired LOTRO points.
   * @return A LOTRO points count.
   */
  public int getAcquiredLP()
  {
    return _acquiredLP;
  }

  /**
   * Get the number of acquired class points.
   * @return A class points count.
   */
  public int getClassPoints()
  {
    return _classPoints;
  }

  /**
   * Get the number of acquired marks.
   * @return A marks count.
   */
  public int getMarksCount()
  {
    return _marksCount;
  }

  /**
   * Get the number of acquired medallions.
   * @return A medallions count.
   */
  public int getMedallionsCount()
  {
    return _medallionsCount;
  }

  /**
   * Get the acquired titles.
   * @return A list of titles, sorted by name.
   */
  public List<String> getTitles()
  {
    return _titles;
  }

  /**
   * Get the acquired emotes.
   * @return A list of emotes, sorted by name.
   */
  public List<String> getEmotes()
  {
    return _emotes;
  }

  /**
   * Get the statistics about the acquired virtues.
   * @return A map of virtues statistics.
   */
  public Map<VirtueId,VirtueStatsFromDeeds> getVirtues()
  {
    return _virtues;
  }

  /**
   * Get the total number of virtue points.
   * @return A virtue points count.
   */
  public int getTotalVirtuePoints()
  {
    int total=0;
    for(VirtueStatsFromDeeds virtueStats : _virtues.values())
    {
      int points=virtueStats.getPoints();
      total+=points;
    }
    return total;
  }

  /**
   * Get the statistics about the acquired reputation.
   * @return A map of faction statistics.
   */
  public Map<String,FactionStatsFromDeeds> getReputation()
  {
    return _reputation;
  }

  /**
   * Get the total number of reputation points.
   * @return A reputation points count.
   */
  public int getTotalReputationPoints()
  {
    int total=0;
    for(FactionStatsFromDeeds factionStats : _reputation.values())
    {
      int points=factionStats.getPoints();
      total+=points;
    }
    return total;
  }

  /**
   * Show results.
   */
  public void showResults()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Completed: ").append(_completedCount).append(" / ").append(_total).append(EndOfLine.NATIVE_EOL);
    sb.append("LOTRO points: ").append(_acquiredLP).append(EndOfLine.NATIVE_EOL);
    sb.append("Class points: ").append(_classPoints).append(EndOfLine.NATIVE_EOL);
    sb.append("Marks: ").append(_marksCount).append(EndOfLine.NATIVE_EOL);
    sb.append("Medallions: ").append(_medallionsCount).append(EndOfLine.NATIVE_EOL);
    if (_titles.size()>0)
    {
      sb.append("Titles: (").append(_titles.size()).append(')').append(EndOfLine.NATIVE_EOL);
      for(String title : _titles)
      {
        sb.append('\t').append(title).append(EndOfLine.NATIVE_EOL);
      }
    }
    if (_emotes.size()>0)
    {
      sb.append("Emotes: (").append(_emotes.size()).append(')').append(EndOfLine.NATIVE_EOL);
      for(String emote : _emotes)
      {
        sb.append('\t').append(emote).append(EndOfLine.NATIVE_EOL);
      }
    }
    if (_virtues.size()>0)
    {
      sb.append("Virtues:").append(EndOfLine.NATIVE_EOL);
      for(VirtueId virtueId : VirtueId.values())
      {
        VirtueStatsFromDeeds virtueStats=_virtues.get(virtueId);
        if (virtueStats!=null)
        {
          int points=virtueStats.getPoints();
          sb.append('\t').append(virtueId.getLabel()).append(": ").append(points).append(EndOfLine.NATIVE_EOL);
        }
      }
    }
    if (_reputation.size()>0)
    {
      sb.append("Reputation:").append(EndOfLine.NATIVE_EOL);
      List<Faction> factions=FactionsRegistry.getInstance().getAll();
      for(Faction faction : factions)
      {
        FactionStatsFromDeeds factionStats=_reputation.get(faction.getKey());
        if (factionStats!=null)
        {
          int points=factionStats.getPoints();
          int deeds=factionStats.getDeedsCount();
          sb.append('\t').append(faction.getName()).append(": ").append(points).append(" (").append(deeds).append(" deeds)").append(EndOfLine.NATIVE_EOL);
        }
      }
    }
    String display=sb.toString();
    System.out.println(display);
  }
}
