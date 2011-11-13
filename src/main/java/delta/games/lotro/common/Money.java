package delta.games.lotro.common;

/**
 * Represents an amount of money.
 * @author DAM
 */
public class Money
{
  private int _goldCoins;
  private int _silverCoins;
  private int _copperCoins;

  /**
   * Default constructor.
   */
  public Money()
  {
    _goldCoins=0;
    _silverCoins=0;
    _copperCoins=0;
  }

  /**
   * Full constructor.
   * @param goldCoins Number of gold coins.
   * @param silverCoins Number of silver coins.
   * @param copperCoins Number of copper coins.
   */
  public Money(int goldCoins, int silverCoins, int copperCoins)
  {
    _goldCoins=goldCoins;
    _silverCoins=silverCoins;
    _copperCoins=copperCoins;
  }

  /**
   * Get the number of gold coins.
   * @return a number of coins.
   */
  public int getGoldCoins()
  {
    return _goldCoins;
  }

  /**
   * Set the number of gold coins.
   * @param goldCoins number of coins to set.
   */
  public void setGoldCoins(int goldCoins)
  {
    _goldCoins=goldCoins;
  }

  /**
   * Get the number of silver coins.
   * @return a number of coins.
   */
  public int getSilverCoins()
  {
    return _silverCoins;
  }

  /**
   * Set the number of silver coins.
   * @param silverCoins number of coins to set.
   */
  public void setSilverCoins(int silverCoins)
  {
    _silverCoins=silverCoins;
  }

  /**
   * Get the number of copper coins.
   * @return a number of coins.
   */
  public int getCopperCoins()
  {
    return _copperCoins;
  }

  /**
   * Set the number of copper coins.
   * @param copperCoins number of coins to set.
   */
  public void setCopperCoins(int copperCoins)
  {
    _copperCoins=copperCoins;
  }

  private void simplify()
  {
    while (_copperCoins>=100)
    {
      _copperCoins-=100;
      _silverCoins++;
    }
    while (_silverCoins>=100)
    {
      _silverCoins-=100;
      _goldCoins++;
    }
  }

  public static Money add(Money m1,Money m2)
  {
    int gold=m1.getGoldCoins()+m2.getGoldCoins();
    int silver=m1.getSilverCoins()+m2.getSilverCoins();
    int copper=m1.getCopperCoins()+m2.getCopperCoins();
    Money m=new Money(gold,silver,copper);
    m.simplify();
    return m;
  }

  @Override
  public String toString()
  {
    return _goldCoins+"gold, "+_silverCoins+" silver, "+_copperCoins+" copper";
  }
}
