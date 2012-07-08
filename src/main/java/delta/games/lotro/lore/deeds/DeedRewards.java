package delta.games.lotro.lore.deeds;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.common.Money;
import delta.games.lotro.common.Reputation;
import delta.games.lotro.common.Skill;
import delta.games.lotro.common.Title;
import delta.games.lotro.common.Trait;
import delta.games.lotro.common.objects.ObjectsSet;

/**
 * Quest rewards description.
 * @author DAM
 */
public class DeedRewards
{
  private boolean _itemXP;
  private Money _money;
  private Reputation _reputation;
  private int _destinyPoints;
  private List<Trait> _traits;
  private List<Skill> _skills;
  private List<Title> _titles;
  private ObjectsSet _objects;
  private ObjectsSet _selectObjects;

  /**
   * Constructor.
   */
  public DeedRewards()
  {
    _itemXP=false;
    _money=new Money();
    _reputation=new Reputation();
    _destinyPoints=0;
    _traits=null;
    _skills=null;
    _titles=null;
    _objects=new ObjectsSet();
    _selectObjects=new ObjectsSet();
  }

  /**
   * Get the money reward.
   * @return the money reward.
   */
  public Money getMoney()
  {
    return _money;
  }
  
  /**
   * Get the reputation reward.
   * @return the reputation reward.
   */
  public Reputation getReputation()
  {
    return _reputation;
  }

  /**
   * Get the destiny points.
   * @return the destiny points.
   */
  public int getDestinyPoints()
  {
    return _destinyPoints;
  }

  /**
   * Set the destiny points.
   * @param destinyPoints Destiny points to set.
   */
  public void setDestinyPoints(int destinyPoints)
  {
    _destinyPoints=destinyPoints;
  }
  
  /**
   * Get the objects reward.
   * @return the objects reward.
   */
  public ObjectsSet getObjects()
  {
    return _objects;
  }

  /**
   * Get the objects reward.
   * @return the objects reward.
   */
  public ObjectsSet getSelectObjects()
  {
    return _selectObjects;
  }

  /**
   * Indicates if this reward includes item XP.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean hasItemXP()
  {
    return _itemXP;
  }

  /**
   * Set the 'item XP' flag.
   * @param itemXP Value to set.
   */
  public void setHasItemXP(boolean itemXP)
  {
    _itemXP=itemXP;
  }

  /**
   * Add a trait.
   * @param trait Trait to add.
   */
  public void addTrait(Trait trait)
  {
    if (_traits==null)
    {
      _traits=new ArrayList<Trait>();
    }
    _traits.add(trait);
  }

  /**
   * Get all traits.
   * @return An array of traits or <code>null</code> if there's none.
   */
  public Trait[] getTraits()
  {
    Trait[] ret=null;
    if (_traits!=null)
    {
      ret=new Trait[_traits.size()];
      ret=_traits.toArray(ret);
    }
    return ret;
  }

  /**
   * Add a skill.
   * @param skill Skill to add.
   */
  public void addSkill(Skill skill)
  {
    if (_skills==null)
    {
      _skills=new ArrayList<Skill>();
    }
    _skills.add(skill);
  }

  /**
   * Get all skills.
   * @return An array of skills or <code>null</code> if there's none.
   */
  public Skill[] getSkills()
  {
    Skill[] ret=null;
    if (_skills!=null)
    {
      ret=new Skill[_skills.size()];
      ret=_skills.toArray(ret);
    }
    return ret;
  }


  /**
   * Add a title.
   * @param title Title to add.
   */
  public void addTitle(Title title)
  {
    if (_titles==null)
    {
      _titles=new ArrayList<Title>();
    }
    _titles.add(title);
  }

  /**
   * Get all titles.
   * @return An array of titles or <code>null</code> if there's none.
   */
  public Title[] getTitles()
  {
    Title[] ret=null;
    if (_titles!=null)
    {
      ret=new Title[_titles.size()];
      ret=_titles.toArray(ret);
    }
    return ret;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    boolean firstDone=false;
    if (!_money.isEmpty())
    {
      sb.append(_money);
      firstDone=true;
    }
    if (!_reputation.isEmpty())
    {
      if (firstDone) sb.append(" / ");
      sb.append(_reputation);
      firstDone=true;
    }
    if (_destinyPoints>0)
    {
      if (firstDone) sb.append(" / ");
      sb.append(_destinyPoints).append(" destiny points");
      firstDone=true;
    }
    int nbObjects=_objects.getNbObjectItems();
    if (nbObjects>0)
    {
      if (firstDone) sb.append(" / ");
      sb.append(_objects);
      firstDone=true;
    }
    int nbSelectObjects=_selectObjects.getNbObjectItems();
    if (nbSelectObjects>0)
    {
      if (firstDone) sb.append(" / Select one of: ");
      sb.append(_selectObjects);
      firstDone=true;
    }
    if (_traits!=null)
    {
      if (firstDone) sb.append(" / ");
      sb.append("Traits: ");
      sb.append(_traits);
      firstDone=true;
    }
    if (_skills!=null)
    {
      if (firstDone) sb.append(" / ");
      sb.append("Skills: ");
      sb.append(_skills);
      firstDone=true;
    }
    if (_titles!=null)
    {
      if (firstDone) sb.append(" / ");
      sb.append("Titles: ");
      sb.append(_titles);
      firstDone=true;
    }
    if (_itemXP)
    {
      if (firstDone) sb.append(" / ");
      sb.append("Item XP");
      firstDone=true;
    }
    return sb.toString();
  }
}
