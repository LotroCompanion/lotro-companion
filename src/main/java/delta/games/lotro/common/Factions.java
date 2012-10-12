package delta.games.lotro.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import delta.common.utils.text.EncodingNames;
import delta.common.utils.text.TextUtils;
import delta.games.lotro.Config;

/**
 * Factions registry.
 * @author DAM
 */
public class Factions
{
  private static Factions _instance=new Factions();
  
  private static final String FOROCHEL="Lossoth of Forochel";
  private static final String ALE_ASSOCIATION="Ale Association";
  private static final String INN_LEAGUE="Inn League";

  private HashMap<String,Faction> _registry;
  private List<Faction> _factions;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static Factions getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private Factions()
  {
    _registry=new HashMap<String,Faction>();
    _factions=new ArrayList<Faction>();
    initFactions();
  }

  /**
   * Initialize and register all the factions.
   */
  private void initFactions()
  {
    List<Faction> factions=new ArrayList<Faction>();
    File cfgDir=Config.getInstance().getConfigDir();
    File factionFiles=new File(cfgDir,"factions.txt"); 
    List<String> lines=TextUtils.readAsLines(factionFiles,EncodingNames.UTF_8);
    if (lines!=null)
    {
      Faction faction=null;
      for(String line : lines)
      {
        if (!line.startsWith("\t"))
        {
          String factionName=line;
          faction=initFaction(factionName);
          factions.add(faction);
        }
        else
        {
          String alias=line.trim();
          if (faction!=null)
          {
            faction.addAlias(alias);
          }
        }
      }
    }
    for(Faction faction : factions)
    {
      registerFaction(faction);
    }
  }

  /**
   * Register a new faction.
   * @param faction Faction to register.
   */
  private void registerFaction(Faction faction)
  {
    String name=faction.getName();
    String[] aliases=faction.getAliases();
    _registry.put(name,faction);
    for(String alias : aliases)
    {
      _registry.put(alias,faction);
    }
    _factions.add(faction);
  }

  /**
   * Initialize a new faction.
   * @param name Faction name.
   * @return the newly built faction.
   */
  private Faction initFaction(String name)
  {
    List<FactionLevel> levels=new ArrayList<FactionLevel>();
    if (FOROCHEL.equals(name))
    {
      levels.add(FactionLevel.OUTSIDER);
    }
    else if ((ALE_ASSOCIATION.equals(name)) || (INN_LEAGUE.equals(name)))
    {
      levels.add(FactionLevel.ENEMY);
    }
    levels.add(FactionLevel.NEUTRAL);
    levels.add(FactionLevel.ACQUAINTANCE);
    levels.add(FactionLevel.FRIEND);
    levels.add(FactionLevel.ALLY);
    levels.add(FactionLevel.KINDRED);

    FactionLevel initialLevel;
    if (FOROCHEL.equals(name))
    {
      initialLevel=FactionLevel.OUTSIDER;
    }
    else
    {
      initialLevel=FactionLevel.NEUTRAL;
    }
    Faction faction=new Faction(name,levels,initialLevel);
    return faction;
  }
  
  /**
   * Get all known factions.
   * @return An array of factions.
   */
  public Faction[] getAll()
  {
    Faction[] ret=_factions.toArray(new Faction[_factions.size()]);
    return ret;
  }

  /**
   * Get a faction instance by name.
   * @param name Name of the faction to get.
   * @return A faction instance or <code>null</code> if <code>name</code> is <code>null</code> or empty.
   */
  public Faction getByName(String name)
  {
    Faction f=null;
    if ((name!=null) && (name.length()>0))
    {
      f=_registry.get(name);
    }
    return f;
  }
}
