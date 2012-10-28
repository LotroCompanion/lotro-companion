package delta.games.lotro.stats.crafting;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

import delta.games.lotro.crafting.CraftingLevel;

/**
 * Statistics about a single profession on a single toon.
 * @author DAM
 */
public class ProfessionStat
{
  private String _professionName;
  private int _mastery;
  private int _proficiency;
  private Long _lastLogItemDate;
  private ArrayList<Long> _masteryUpdates;
  private ArrayList<Long> _proficiencyUpdates;

  /**
   * Constructor.
   * @param professionName
   */
  public ProfessionStat(String professionName)
  {
    _professionName=professionName;
    reset();
  }

  /**
   * Get the name of the managed profession.
   * @return the name of the managed profession.
   */
  public String getProfession()
  {
    return _professionName;
  }

  /**
   * Initialize profession.
   * @param date Event date.
   */
  public void initProfession(long date)
  {
    reset();
    addUpdate(0,0,date);
  }

  /**
   * Add a profession update event.
   * @param mastery New mastery tier.
   * @param proficiency New proficiency tier.
   * @param date Event date.
   */
  public void addUpdate(int mastery, int proficiency, long date)
  {
    if (mastery>_mastery)
    {
      _mastery=mastery;
    }
    registerTier(date,mastery,_masteryUpdates);
    if (proficiency>_proficiency)
    {
      _proficiency=proficiency;
    }
    registerTier(date,proficiency,_proficiencyUpdates);
  }

  /**
   * Get the date of the last item used to build this data.
   * @return A date or <code>null</code> if not set.
   */
  public Long getLastLogItemDate()
  {
    return _lastLogItemDate;
  }

  /**
   * Set the date of the last item used to build this data.
   * @param date Date to set or <code>null</code>.
   */
  public void setLastLogItemDate(long date)
  {
    _lastLogItemDate=Long.valueOf(date);
  }

  private void registerTier(long date, int level, ArrayList<Long> updates)
  {
    updates.ensureCapacity(level+1);
    while (level>=updates.size())
    {
      updates.add(null);
    }
    Long currentDate=updates.get(level);
    if ((currentDate==null) || (currentDate.longValue()>date))
    {
      updates.set(level,Long.valueOf(date));
    }
  }

  /**
   * Reset data.
   */
  private void reset()
  {
    _mastery=-1;
    _proficiency=-1;
    _masteryUpdates=new ArrayList<Long>();
    _proficiencyUpdates=new ArrayList<Long>();
  }

  /**
   * Get the proficiency tier.
   * @return the proficiency tier.
   */
  public int getProficiencyTier()
  {
    return _proficiency;
  }

  /**
   * Get the date of a proficiency tier acquisition.
   * @param tier Tier to use.
   * @return A date or <code>null</code< if not found.
   */
  public Long getProficiencyTierDate(int tier)
  {
    Long ret=null;
    if ((tier>=0) && (tier<_proficiencyUpdates.size()))
    {
      ret=_proficiencyUpdates.get(tier);
    }
    return ret;
  }

  /**
   * Get the mastery tier.
   * @return the mastery tier.
   */
  public int getMasteryTier()
  {
    return _mastery;
  }

  /**
   * Get the date of a mastery tier acquisition.
   * @param tier Tier to use.
   * @return A date or <code>null</code< if not found.
   */
  public Long getMasteryTierDate(int tier)
  {
    Long ret=null;
    if ((tier>=0) && (tier<_masteryUpdates.size()))
    {
      ret=_masteryUpdates.get(tier);
    }
    return ret;
  }

  /**
   * Dump the contents of this object to the given stream.
   * @param ps Output stream to use.
   */
  public void dump(PrintStream ps)
  {
    ps.println("History for profession ["+_professionName+"]:");
    if (_proficiency>=0)
    {
      ps.println("Proficiency:");
      for(int i=0;i<=_proficiency;i++)
      {
        Long date=getProficiencyTierDate(i);
        if (date!=null)
        {
          CraftingLevel level=CraftingLevel.getByTier(i);
          String label=(level!=null)?level.getProficiencyLabel():"???";
          ps.println("\t"+label+": "+new Date(date.longValue()));
        }
      }
    }
    if (_mastery>=0)
    {
      ps.println("Mastery:");
      for(int i=0;i<=_mastery;i++)
      {
        Long date=getMasteryTierDate(i);
        if (date!=null)
        {
          CraftingLevel level=CraftingLevel.getByTier(i);
          String label=(level!=null)?level.getMasteryLabel():"???";
          ps.println("\t"+label+": "+new Date(date.longValue()));
        }
      }
    }
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append(_professionName);
    sb.append(": mastery=");
    CraftingLevel masteryLevel=CraftingLevel.getByTier(_mastery);
    String masteryLabel=(masteryLevel!=null)?masteryLevel.getMasteryLabel():"???";
    sb.append(masteryLabel);
    sb.append(", proficiency=");
    CraftingLevel proficiencyLevel=CraftingLevel.getByTier(_proficiency);
    String proficiencyLabel=(proficiencyLevel!=null)?proficiencyLevel.getProficiencyLabel():"???";
    sb.append(proficiencyLabel);
    return sb.toString();
  }
}
