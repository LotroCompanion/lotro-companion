package delta.games.lotro.stats.warbands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.lore.warbands.WarbandDefinition;
import delta.games.lotro.lore.warbands.WarbandsRegistry;

/**
 * Warbands statistics for a single toon.
 * @author DAM
 */
public class WarbandsStats
{
  private static final Logger LOGGER=Logger.getLogger(WarbandsStats.class);

  private static final String WARBAND_SEED="Warband:";

  private String _name;
  private HashMap<String,WarbandStats> _stats;

  /**
   * Constructor.
   * @param log Character log to use.
   */
  public WarbandsStats(CharacterLog log)
  {
    _name=log.getName();
    _stats=new HashMap<String,WarbandStats>();
    List<CharacterLogItem> items=getWarbandItems(log);
    parseItems(items);
  }

  private List<CharacterLogItem> getWarbandItems(CharacterLog log)
  {
    List<CharacterLogItem> ret=new ArrayList<CharacterLogItem>();
    int nb=log.getNbItems();
    for(int i=0;i<nb;i++)
    {
      CharacterLogItem item=log.getLogItem(i);
      if (item!=null)
      {
        LogItemType type=item.getLogItemType();
        if (type==LogItemType.QUEST)
        {
          String label=item.getLabel();
          if (label.startsWith(WARBAND_SEED))
          {
            ret.add(item);
          }
        }
      }
    }
    Collections.reverse(ret);
    return ret;
  }

  /**
   * Get the statistics for a given warband.
   * @param warband Targeted warband.
   * @return A warband statistics object, or <code>null</code> if the toon does not
   * have stats for this warband.
   */
  public WarbandStats getWarbandStats(WarbandDefinition warband)
  {
    String warbandName=warband.getName();
    WarbandStats stat=_stats.get(warbandName);
    return stat;
  }

  /**
   * Get the statistics for a given warband.
   * @param warband Targeted warband.
   * @param createItIfNeeded Create the stats item if it does not exist.
   * @return A warband statistics object, or <code>null</code> if the toon does not
   * have stats for this warband and <code>createItIfNeeded</code> is false.
   */
  public WarbandStats getWarbandStats(WarbandDefinition warband, boolean createItIfNeeded)
  {
    String warbandName=warband.getName();
    WarbandStats stat=_stats.get(warbandName);
    if ((stat==null) && (createItIfNeeded))
    {
      stat=new WarbandStats(warband);
      _stats.put(warbandName,stat);
    }
    return stat;
  }

  private void parseItem(CharacterLogItem item)
  {
    String label=item.getLabel();
    String warbandName=label.substring(WARBAND_SEED.length()).trim();
    long date=item.getDate();
    WarbandsRegistry registry=WarbandsRegistry.getWarbandsRegistry();
    WarbandDefinition warband=registry.getByName(warbandName);
    if (warband==null)
    {
      LOGGER.warn("Unknown warband ["+warbandName+"]. Ignored.");
    }
    else
    {
      WarbandStats stat=getWarbandStats(warband,true);
      stat.add(date);
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
    ps.println("Warbands statistics for ["+_name+"]:");
    List<String> warbandNames=new ArrayList<String>(_stats.keySet());
    Collections.sort(warbandNames);
    WarbandsRegistry registry=WarbandsRegistry.getWarbandsRegistry();
    for(String warbandName : warbandNames)
    {
      WarbandDefinition warband=registry.getByName(warbandName);
      WarbandStats stat=getWarbandStats(warband);
      stat.dump(ps);
    }
  }
}
