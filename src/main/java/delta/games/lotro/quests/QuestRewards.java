package delta.games.lotro.quests;

import delta.games.lotro.common.Money;
import delta.games.lotro.common.Reputation;
import delta.games.lotro.common.objects.ObjectsSet;

/**
 * Quest rewards description.
 * @author DAM
 */
public class QuestRewards
{
  private boolean _itemXP;
  private Money _money;
  private Reputation _reputation;
  private ObjectsSet _objects;
  private ObjectsSet _selectObjects;

  /**
   * Constructor.
   */
  public QuestRewards()
  {
    _itemXP=false;
    _money=new Money();
    _reputation=new Reputation();
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
    if (_itemXP)
    {
      if (firstDone) sb.append(" / ");
      sb.append("Item XP");
      firstDone=true;
    }
    return sb.toString();
  }
}
