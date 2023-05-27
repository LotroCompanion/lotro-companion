package delta.games.lotro.gui.character.essences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.gear.CharacterGear;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.essences.EssencesSet;

/**
 * Essences summary.
 * @author DAM
 */
public class EssencesSummary
{
  private CharacterData _toon;
  private HashMap<Integer,EssenceCount> _count;
  private int _essencesCount;
  private int _slotsCount;
  private BasicStatsSet _stats;

  /**
   * Constructor.
   * @param toon Associated toon.
   */
  public EssencesSummary(CharacterData toon)
  {
    _toon=toon;
    _count=new HashMap<Integer,EssenceCount>();
    _stats=new BasicStatsSet();
  }

  /**
   * Get the total number of essences on the managed toon.
   * @return A count of essences.
   */
  public int getEssencesCount()
  {
    return _essencesCount;
  }

  /**
   * Get the total number of essence slots on the managed toon.
   * @return A count of essence slots.
   */
  public int getSlotsCount()
  {
    return _slotsCount;
  }

  /**
   * Get the total raw stats given by all the essences on the managed toon.
   * @return A set of stats.
   */
  public BasicStatsSet getStats()
  {
    return _stats;
  }

  /**
   * Get a list of all counts.
   * @return a list of counts.
   */
  public List<EssenceCount> getCounts()
  {
    List<EssenceCount> ret=new ArrayList<EssenceCount>(_count.values());
    return ret;
  }

  /**
   * Update counts.
   */
  public void update()
  {
    _count.clear();
    _essencesCount=0;
    _slotsCount=0;
    _stats.clear();
    CharacterGear equipment=_toon.getEquipment();
    for(GearSlot slot : GearSlot.getAll())
    {
      ItemInstance<? extends Item> itemInstance=equipment.getItemForSlot(slot);
      if (itemInstance!=null)
      {
        EssencesSet essences=itemInstance.getEssences();
        Item item=itemInstance.getReference();
        int nbSlots=item.getEssenceSlots();
        int nbEssences=(essences!=null)?essences.getSize():0;
        int slotsCount=Math.max(nbSlots,nbEssences);
        // Increment total essence slot count
        _slotsCount+=slotsCount;
        if (essences!=null)
        {
          for(int i=0;i<nbEssences;i++)
          {
            Item essence=essences.getEssence(i);
            if (essence!=null)
            {
              // Increment by-essence count
              Integer essenceId=Integer.valueOf(essence.getIdentifier());
              EssenceCount count=_count.get(essenceId);
              if (count==null)
              {
                count=new EssenceCount(essence);
                _count.put(essenceId,count);
              }
              count.increment();
              // Increment essences count
              _essencesCount++;
              BasicStatsSet essenceStats=essence.getStats();
              _stats.addStats(essenceStats);
            }
          }
        }
      }
    }
  }

  /**
   * Count for a single essence.
   * @author DAM
   */
  public static class EssenceCount
  {
    private Item _essence;
    private int _count;

    /**
     * Constructor.
     * @param essence Managed essence.
     */
    public EssenceCount(Item essence)
    {
      _essence=essence;
    }

    /**
     * Get the managed essence.
     * @return the managed essence.
     */
    public Item getEssence()
    {
      return _essence;
    }

    /**
     * Get the count of this essence.
     * @return an essences count.
     */
    public int getCount()
    {
      return _count;
    }

    /**
     * Increment the essences count.
     */
    public void increment()
    {
      _count++;
    }
  }
}
