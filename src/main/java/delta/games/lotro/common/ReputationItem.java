package delta.games.lotro.common;

/**
 * Reputation for a single faction.
 * @author DAM
 */
public class ReputationItem
{
  private int _amount;
  private Faction _faction;
  
  /**
   * Constructor.
   * @param faction Associated faction.
   */
  public ReputationItem(Faction faction)
  {
    _amount=0;
    _faction=faction;
  }

  /**
   * Get the associated faction.
   * @return the associated faction.
   */
  public Faction getFaction()
  {
    return _faction;
  }

  /**
   * Get the reputation amount.
   * @return the reputation amount.
   */
  public int getAmount()
  {
    return _amount;
  }

  /**
   * Set the reputation amount.
   * @param amount the reputation to set.
   */
  public void setAmount(int amount)
  {
    _amount=amount;
  }

  @Override
  public String toString()
  {
    return _amount+" "+_faction;
  }
}
