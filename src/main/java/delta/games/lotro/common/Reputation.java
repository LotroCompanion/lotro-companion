package delta.games.lotro.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.common.utils.text.EndOfLine;

/**
 * Global reputation.
 * @author DAM
 */
public class Reputation
{
  private HashMap<String,ReputationItem> _reputations;

  /**
   * Constructor.
   */
  public Reputation()
  {
    _reputations=new HashMap<String,ReputationItem>();
  }

  /**
   * Indicates if this reputation is void.
   * @return <code>true</code> if it is.
   */
  public boolean isEmpty()
  {
    boolean ret=true;
    if (_reputations.size()>0)
    {
      for(ReputationItem item : _reputations.values())
      {
        if (item.getAmount()!=0)
        {
          ret=false;
          break;
        }
      }
    }
    return ret;
  }

  /**
   * Get the reputation for a given faction.
   * @param faction Faction to use.
   * @param createIfNeeded Indicates if a reputation item shall be created
   * if it does not exist. 
   * @return A reputation item or <code>null</code>.
   */
  public ReputationItem getReputation(Faction faction, boolean createIfNeeded)
  {
    ReputationItem ret=null;
    if (faction!=null)
    {
      String name=faction.getName();
      ret=_reputations.get(name);
      if ((ret==null) && (createIfNeeded))
      {
        ret=new ReputationItem(faction);
        _reputations.put(name,ret);
      }
    }
    return ret;
  }

  /**
   * Add a reputation item.
   * @param item Reputation item to add.
   */
  public void add(ReputationItem item)
  {
    if (item!=null)
    {
      Faction faction=item.getFaction();
      ReputationItem localItem=getReputation(faction,true);
      localItem.setAmount(localItem.getAmount()+item.getAmount());
    }
  }

  /**
   * Add reputation.
   * @param reputation Reputation to add.
   */
  public void add(Reputation reputation)
  {
    for(ReputationItem item : reputation._reputations.values())
    {
      add(item);
    }
  }

  /**
   * Get an array of all reputation items.
   * @return A possibly empty array or reputation items.
   */
  public ReputationItem[] getItems()
  {
    int nb=_reputations.size();
    ReputationItem[] ret=new ReputationItem[nb];
    ret=_reputations.values().toArray(ret);
    return ret;
  }

  @Override
  public String toString()
  {
    String ret="";
    if (_reputations.size()>0)
    {
      StringBuilder sb=new StringBuilder();
      List<String> factions=new ArrayList<String>(_reputations.keySet());
      Collections.sort(factions);
      for(String factionName : factions)
      {
        ReputationItem reputation=_reputations.get(factionName);
        if (reputation!=null)
        {
          sb.append(reputation);
          sb.append(EndOfLine.NATIVE_EOL);
        }
      }
      ret=sb.toString().trim();
    }
    return ret;
  }
}
