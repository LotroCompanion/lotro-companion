package delta.games.lotro.stats.crafting;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.common.utils.text.TextTools;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.crafting.Vocation;
import delta.games.lotro.crafting.Vocations;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Crafting status for a single toon.
 * @author DAM
 */
public class CraftingStats
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private String _name;
  private String _vocation;
  private HashMap<String,ProfessionStat> _stats;
  
  /**
   * Constructor.
   * @param toonName Character name.
   * @param log Character log to use.
   */
  public CraftingStats(String toonName, CharacterLog log)
  {
    _name=toonName;
    reset();
    List<CharacterLogItem> items=getCraftingItems(log);
    parseItems(items);
    if (log!=null)
    {
      int nbItems=log.getNbItems();
      if (nbItems>0)
      {
        CharacterLogItem lastItem=log.getLogItem(0);
        long date=lastItem.getDate();
        for(ProfessionStat stat : _stats.values())
        {
          stat.setLastLogItemDate(date);
        }
      }
    }
  }

  private void reset()
  {
    _stats=new HashMap<String,ProfessionStat>();
  }

  private List<CharacterLogItem> getCraftingItems(CharacterLog log)
  {
    List<CharacterLogItem> ret=new ArrayList<CharacterLogItem>();
    if (log!=null)
    {
      int nb=log.getNbItems();
      for(int i=0;i<nb;i++)
      {
        CharacterLogItem item=log.getLogItem(i);
        if (item!=null)
        {
          LogItemType type=item.getLogItemType();
          if ((type==LogItemType.PROFESSION) || (type==LogItemType.VOCATION))
          {
            ret.add(item);
          }
        }
      }
      Collections.reverse(ret);
    }
    return ret;
  }

  /**
   * Get the current vocation.
   * @return A vocation name.
   */
  public String getVocation()
  {
    return _vocation;
  }

  /**
   * Get all managed professions.
   * @return A array of sorted profession names.
   */
  public String[] getProfessions()
  {
    Set<String> professions=_stats.keySet();
    String[] ret=professions.toArray(new String[professions.size()]);
    Arrays.sort(ret);
    return ret;
  }

  /**
   * Get the statistics for a given profession.
   * @param profession Profession name.
   * @return A profession statistics object, or <code>null</code> if the toon does not
   * have the given profession.
   */
  public ProfessionStat getProfessionStat(String profession)
  {
    ProfessionStat stat=_stats.get(profession);
    return stat;
  }

  private ProfessionStat getProfessionStat(String profession, boolean createItIfNeeded)
  {
    ProfessionStat stat=_stats.get(profession);
    if ((stat==null) && (createItIfNeeded))
    {
      stat=new ProfessionStat(profession);
      _stats.put(profession,stat);
    }
    return stat;
  }

  private void parseProfessionItem(String label, long date)
  {
    // Advanced 'Woodworker' (Mastery 6 / Proficiency 5)
    try
    {
      String profession=TextTools.findBetween(label,"'","'").trim(); 
      String masteryStr=TextTools.findBetween(label,"Mastery","/").trim(); 
      String proficiencyStr=TextTools.findBetween(label,"Proficiency",")").trim();
      // Labels are wrong in the character log
      // Proficiency is the brown anvil, mastery is the yellow anvil
      // so that mastery<=proficiency.
      int mastery=NumericTools.parseInt(proficiencyStr,-1);
      int proficiency=NumericTools.parseInt(masteryStr,-1);
      if ((profession!=null) && (mastery!=-1) && (proficiency!=-1))
      {
        ProfessionStat stat=getProfessionStat(profession,true);
        stat.addUpdate(mastery,proficiency,date);
      }
    }
    catch(Exception e)
    {
      _logger.error("Error when parsing profession item ["+label+"]",e);
    }
  }

  private void parseVocationItem(String label, long date)
  {
    // Learned 'Woodsman'
    String vocationName=TextTools.findBetween(label,"'","'").trim();
    Vocation v=Vocations.getInstance().getVocationByName(vocationName);
    if (v!=null)
    {
      _vocation=v.getName();
      _stats.clear();
      String[] professions=v.getProfessions();
      for(String profession : professions)
      {
        ProfessionStat stat=getProfessionStat(profession,true);
        stat.initProfession(date);
      }
    }
  }

  private void parseItem(CharacterLogItem item)
  {
    LogItemType type=item.getLogItemType();
    if (type==LogItemType.PROFESSION)
    {
      String label=item.getLabel();
      long date=item.getDate();
      parseProfessionItem(label,date);
    }
    else if (type==LogItemType.VOCATION)
    {
      String label=item.getLabel();
      long date=item.getDate();
      parseVocationItem(label,date);
    }
  }

  private void parseItems(List<CharacterLogItem> items)
  {
    for(CharacterLogItem item : items)
    {
      parseItem(item);
    }
  }

  /**
   * Dump the contents of this object to the given stream.
   * @param ps Output stream to use.
   */
  public void dump(PrintStream ps)
  {
    ps.println("Crafting status for ["+_name+"]:");
    List<String> professionNames=new ArrayList<String>(_stats.keySet());
    Collections.sort(professionNames);
    for(String professionName : professionNames)
    {
      ProfessionStat stat=getProfessionStat(professionName);
      stat.dump(ps);
    }
  }
}
