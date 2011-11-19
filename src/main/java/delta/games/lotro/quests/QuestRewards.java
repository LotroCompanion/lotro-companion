package delta.games.lotro.quests;

import delta.games.lotro.common.Money;
import delta.games.lotro.common.Reputation;

/**
 * Quest rewards description.
 * @author DAM
 */
public class QuestRewards
{
  private boolean _itemXP;
  private Money _money;
  private Reputation _reputation;

  /**
   * Constructor.
   */
  public QuestRewards()
  {
    _itemXP=false;
    _money=new Money();
    _reputation=new Reputation();
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
    sb.append("Rewards: ");
    if (!_money.isEmpty())
    {
      sb.append(_money);
    }
    if (!_reputation.isEmpty())
    {
      sb.append(" / ");
      sb.append(_reputation);
    }
    if (_itemXP)
    {
      sb.append(" / Item XP");
    }
    return sb.toString();
  }
}
