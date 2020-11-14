package delta.games.lotro.stats.deeds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.deeds.DeedStatus;
import delta.games.lotro.character.deeds.DeedsStatusManager;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.character.virtues.VirtuesManager;
import delta.games.lotro.common.rewards.EmoteReward;
import delta.games.lotro.common.rewards.ItemReward;
import delta.games.lotro.common.rewards.ReputationReward;
import delta.games.lotro.common.rewards.RewardElement;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.TitleReward;
import delta.games.lotro.common.rewards.TraitReward;
import delta.games.lotro.common.rewards.VirtueReward;
import delta.games.lotro.gui.items.CountedItemNameComparator;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemProxy;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.WellKnownItems;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionsRegistry;
import delta.games.lotro.stats.deeds.statistics.EmoteEvent;
import delta.games.lotro.stats.deeds.statistics.EmoteEventNameComparator;
import delta.games.lotro.stats.deeds.statistics.TitleEvent;
import delta.games.lotro.stats.deeds.statistics.TitleEventNameComparator;
import delta.games.lotro.stats.deeds.statistics.TraitEvent;
import delta.games.lotro.stats.deeds.statistics.TraitEventNameComparator;
import delta.games.lotro.utils.Proxy;

/**
 * Gather statistics about a collection of deeds for a single character.
 * @author DAM
 */
public class DeedsStatistics
{
  private static final Logger LOGGER=Logger.getLogger(DeedsStatistics.class);

  private int _completedCount;
  private int _total;
  private int _acquiredLP;
  private int _classPoints;
  private int _marksCount;
  private int _medallionsCount;
  private List<TitleEvent> _titles;
  private List<EmoteEvent> _emotes;
  private List<TraitEvent> _traits;
  private Map<VirtueDescription,VirtueStatsFromDeeds> _virtues;
  private Map<String,FactionStatsFromDeeds> _reputation;
  private Map<Integer,CountedItem> _items;

  /**
   * Constructor.
   */
  public DeedsStatistics()
  {
    _titles=new ArrayList<TitleEvent>();
    _emotes=new ArrayList<EmoteEvent>();
    _traits=new ArrayList<TraitEvent>();
    _virtues=new HashMap<VirtueDescription,VirtueStatsFromDeeds>();
    _reputation=new HashMap<String,FactionStatsFromDeeds>();
    _items=new HashMap<Integer,CountedItem>();
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
    _traits.clear();
    _virtues.clear();
    _reputation.clear();
    _items.clear();
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
      DeedStatus deedStatus=deedsStatus.get(deed,false);
      if (deedStatus!=null)
      {
        useDeed(deedStatus,deed);
      }
      _total++;
    }
    Collections.sort(_titles,new TitleEventNameComparator());
    Collections.sort(_emotes,new EmoteEventNameComparator());
    Collections.sort(_traits,new TraitEventNameComparator());
  }

  private void useDeed(DeedStatus deedStatus, DeedDescription deed)
  {
    boolean completed=deedStatus.isCompleted();
    if (completed)
    {
      _completedCount++;
      Rewards rewards=deed.getRewards();
      // LOTRO points
      int lp=rewards.getLotroPoints();
      _acquiredLP+=lp;
      // Class points
      int classPoints=rewards.getClassPoints();
      _classPoints+=classPoints;
      for(RewardElement rewardElement : rewards.getRewardElements())
      {
        // Item
        if (rewardElement instanceof ItemReward)
        {
          ItemReward itemReward=(ItemReward)rewardElement;
          Proxy<Item> itemProxy=itemReward.getItemProxy();
          int itemId=itemProxy.getId();
          int itemsCount=itemReward.getQuantity();
          // Marks
          if (itemId==WellKnownItems.MARK)
          {
            _marksCount+=itemsCount;
          }
          // Medallions
          if (itemId==WellKnownItems.MEDALLION)
          {
            _medallionsCount+=itemsCount;
          }
          Item item=ItemsManager.getInstance().getItem(itemId);
          if (item!=null)
          {
            Integer itemIdInteger=Integer.valueOf(itemId);
            CountedItem count=_items.get(itemIdInteger);
            if (count==null)
            {
              ItemProxy proxy=new ItemProxy();
              proxy.setItem(item);
              count=new CountedItem(proxy,0);
              _items.put(itemIdInteger,count);
            }
            count.add(itemsCount);
          }
          else
          {
            LOGGER.warn("Item not found: "+itemId);
          }
        }
        // Title
        else if (rewardElement instanceof TitleReward)
        {
          TitleReward titleReward=(TitleReward)rewardElement;
          Long date=deedStatus.getCompletionDate();
          TitleEvent event=new TitleEvent(titleReward.getName(),date,deed);
          _titles.add(event);
        }
        // Emote
        else if (rewardElement instanceof EmoteReward)
        {
          EmoteReward emoteReward=(EmoteReward)rewardElement;
          Long date=deedStatus.getCompletionDate();
          EmoteEvent event=new EmoteEvent(emoteReward.getName(),date,deed);
          _emotes.add(event);
        }
        // Trait
        else if (rewardElement instanceof TraitReward)
        {
          TraitReward traitReward=(TraitReward)rewardElement;
          Long date=deedStatus.getCompletionDate();
          TraitEvent event=new TraitEvent(traitReward.getName(),date,deed);
          _traits.add(event);
        }
        // Virtue
        else if (rewardElement instanceof VirtueReward)
        {
          VirtueReward virtueReward=(VirtueReward)rewardElement;
          VirtueDescription virtue=virtueReward.getVirtue();
          VirtueStatsFromDeeds virtueStats=_virtues.get(virtue);
          if (virtueStats==null)
          {
            virtueStats=new VirtueStatsFromDeeds(virtue);
            _virtues.put(virtue,virtueStats);
          }
          int points=virtueReward.getCount();
          virtueStats.add(points);
        }
        // Reputation
        else if (rewardElement instanceof ReputationReward)
        {
          ReputationReward reputationReward=(ReputationReward)rewardElement;
          Faction faction=reputationReward.getFaction();
          String factionKey=faction.getIdentifyingKey();
          FactionStatsFromDeeds factionStats=_reputation.get(factionKey);
          if (factionStats==null)
          {
            factionStats=new FactionStatsFromDeeds(faction);
            _reputation.put(factionKey,factionStats);
          }
          int amount=reputationReward.getAmount();
          factionStats.add(amount);
        }
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
   * Get the title events.
   * @return A list of title events, sorted by name.
   */
  public List<TitleEvent> getTitles()
  {
    return _titles;
  }

  /**
   * Get the acquired emote events.
   * @return A list of emote events, sorted by name.
   */
  public List<EmoteEvent> getEmotes()
  {
    return _emotes;
  }

  /**
   * Get the trait events.
   * @return A list of trait events, sorted by name.
   */
  public List<TraitEvent> getTraits()
  {
    return _traits;
  }

  /**
   * Get the statistics about the acquired virtues.
   * @return A map of virtues statistics.
   */
  public Map<VirtueDescription,VirtueStatsFromDeeds> getVirtues()
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
   * Get the acquired items.
   * @return A list of counted items, sorted by name.
   */
  public List<CountedItem> getItems()
  {
    List<CountedItem> items=new ArrayList<CountedItem>(_items.values());
    Collections.sort(items,new CountedItemNameComparator());
    return items;
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
      for(TitleEvent title : _titles)
      {
        sb.append('\t').append(title).append(EndOfLine.NATIVE_EOL);
      }
    }
    if (_emotes.size()>0)
    {
      sb.append("Emotes: (").append(_emotes.size()).append(')').append(EndOfLine.NATIVE_EOL);
      for(EmoteEvent emote : _emotes)
      {
        sb.append('\t').append(emote).append(EndOfLine.NATIVE_EOL);
      }
    }
    if (_traits.size()>0)
    {
      sb.append("Traits: (").append(_traits.size()).append(')').append(EndOfLine.NATIVE_EOL);
      for(TraitEvent trait : _traits)
      {
        sb.append('\t').append(trait).append(EndOfLine.NATIVE_EOL);
      }
    }
    if (_virtues.size()>0)
    {
      sb.append("Virtues:").append(EndOfLine.NATIVE_EOL);
      List<VirtueDescription> virtues=VirtuesManager.getInstance().getAll();
      for(VirtueDescription virtue : virtues)
      {
        VirtueStatsFromDeeds virtueStats=_virtues.get(virtue);
        if (virtueStats!=null)
        {
          int points=virtueStats.getPoints();
          sb.append('\t').append(virtue.getName()).append(": ").append(points).append(EndOfLine.NATIVE_EOL);
        }
      }
    }
    if (_reputation.size()>0)
    {
      sb.append("Reputation:").append(EndOfLine.NATIVE_EOL);
      List<Faction> factions=FactionsRegistry.getInstance().getAll();
      for(Faction faction : factions)
      {
        FactionStatsFromDeeds factionStats=_reputation.get(faction.getIdentifyingKey());
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
