package delta.games.lotro.stats.reputation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.common.Faction;
import delta.games.lotro.common.FactionLevel;
import delta.games.lotro.common.Factions;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Reputatution status for a single toon.
 * @author DAM
 */
public class ReputationStats
{
  private static final String ALE_ASSOCIATION_SEED="Ale Association";
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private String _name;
  private HashMap<Faction,FactionStat> _stats;
  private HashMap<String,FactionLevel> _seeds;
  
  /**
   * Constructor.
   * @param toonName Character name.
   * @param log Character log to use.
   */
  public ReputationStats(String toonName, CharacterLog log)
  {
    _name=toonName;
    initSeeds();
    reset();
    List<CharacterLogItem> items=getDeedItems(log);
    parseItems(items);
  }

  private void reset()
  {
    _stats=new HashMap<Faction,FactionStat>();
  }

  private List<CharacterLogItem> getDeedItems(CharacterLog log)
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
          if (type==LogItemType.DEED)
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
   * Get the reputation status for a given faction.
   * @param faction Targeted faction.
   * @return A reputation status object, or <code>null</code> if the toon does not
   * have the given faction status.
   */
  public FactionStat getFactionStat(Faction faction)
  {
    FactionStat stat=_stats.get(faction);
    return stat;
  }

  private void initSeeds()
  {
    _seeds=new HashMap<String,FactionLevel>();
    _seeds.put("Known to",FactionLevel.ACQUAINTANCE);
    _seeds.put("Friend to",FactionLevel.FRIEND);
    _seeds.put("Friend of",FactionLevel.FRIEND);
    _seeds.put("Ally to",FactionLevel.ALLY);
    _seeds.put("Ally of",FactionLevel.ALLY);
    _seeds.put("Kindred to",FactionLevel.KINDRED);
    _seeds.put("Kindred with",FactionLevel.KINDRED);
    _seeds.put("Kindred of",FactionLevel.KINDRED);
  }
  
  private void handleItem(long date, String label)
  {
    Faction faction=null;
    FactionLevel level=null;
    
    // Handle generic seeds
    for(Map.Entry<String,FactionLevel> entry : _seeds.entrySet())
    {
      String seed=entry.getKey();
      if (label.startsWith(seed))
      {
        String factionName=label.substring(seed.length()).trim();
        if (factionName.startsWith("the"))
        {
          factionName=factionName.substring(3).trim();
        }
        level=entry.getValue();
        faction=Factions.getInstance().getByName(factionName);
        if (faction!=null)
        {
          break;
        }
      }
    }
    
    // Handle "Ale Association" specificly 
    if (label.startsWith(ALE_ASSOCIATION_SEED))
    {
      faction=Factions.getInstance().getByName("Ale Association");
      String levelStr=label.substring(ALE_ASSOCIATION_SEED.length()).trim();
      if ("Acquaintance".equals(levelStr)) level=FactionLevel.ACQUAINTANCE;
      else if ("Ally".equals(levelStr)) level=FactionLevel.ALLY;
      else if ("Friend".equals(levelStr)) level=FactionLevel.FRIEND;
    }
    else if (label.equals("Kindred of Malevolence"))
    {
      faction=Factions.getInstance().getByName("Ale Association");
      level=FactionLevel.KINDRED;
    }
    // Kindred with Eglain is "Eglan" 
    else if (label.equals("Eglan"))
    {
      faction=Factions.getInstance().getByName("Eglain");
      level=FactionLevel.KINDRED;
    }
    else if (label.equals("Kindred to the Entwash"))
    {
      faction=Factions.getInstance().getByName("Entwash Vale");
      level=FactionLevel.KINDRED;
    }
    // TODO Inn League
    
    if ((faction!=null) && (level!=null))
    {
      FactionStat stat=_stats.get(faction);
      if (stat==null)
      {
        stat=new FactionStat(faction);
        _stats.put(faction,stat);
      }
      stat.addUpdate(level,date);
    }
  }

  private void parseDeedItem(String label, long date)
  {
    try
    {
      handleItem(date,label);
    }
    catch(Exception e)
    {
      _logger.error("Error when parsing deed item ["+label+"]",e);
    }
  }

  private void parseItem(CharacterLogItem item)
  {
    LogItemType type=item.getLogItemType();
    if (type==LogItemType.DEED)
    {
      String label=item.getLabel();
      long date=item.getDate();
      parseDeedItem(label,date);
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
    ps.println("Reputation status for ["+_name+"]:");
    List<Faction> factions=new ArrayList<Faction>(_stats.keySet());
    //Collections.sort(factions);
    for(Faction faction : factions)
    {
      FactionStat stat=getFactionStat(faction);
      stat.dump(ps);
    }
  }
}
