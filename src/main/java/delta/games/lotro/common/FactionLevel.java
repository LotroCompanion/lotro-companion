package delta.games.lotro.common;

/**
 * Represents a level in a faction.
 * @author DAM
 */
public class FactionLevel
{
  /**
   * Enemy.
   */
  public static final FactionLevel ENEMY=new FactionLevel("Enemy");
  /**
   * Outsider.
   */
  public static final FactionLevel OUTSIDER=new FactionLevel("Outsider");
  /**
   * Neutral.
   */
  public static final FactionLevel NEUTRAL=new FactionLevel("Neutral");
  /**
   * Acquaintance.
   */
  public static final FactionLevel ACQUAINTANCE=new FactionLevel("Acquaintance");
  /**
   * Friend.
   */
  public static final FactionLevel FRIEND=new FactionLevel("Friend");
  /**
   * Ally.
   */
  public static final FactionLevel ALLY=new FactionLevel("Ally");
  /**
   * Kindred.
   */
  public static final FactionLevel KINDRED=new FactionLevel("Kindred");

  private String _name;

  private FactionLevel(String name)
  {
    _name=name;
  }
  
  /**
   * Get the name of this level.
   * @return A name.
   */
  public String getName()
  {
    return _name;
  }
  
  public String toString()
  {
    return _name;
  }
}

/*

Enemy   10,000      * Only currently possible with The Ale Association and The Inn League
Outsider  10,000      * Starting level for Lossoth of Forochel
Neutral / Guild Initiate  0     0     * Starting level for most factions
Acquaintance / Apprentice of the Guild  10,000    10,000    Access to reputation areas & their vendors / Access to Guild Expert Recipes
Friend / Journeyman of the Guild  20,000    30,000    10% Travel discount from faction stables / Access to Guild Artisan Recipes
Ally / Expert of the Guild  25,000    55,000    Discounted repair costs (approx 20-25%) and discount (5%) from reputation vendors / Access to Guild Master Recipes
Kindred / Artisan of the Guild  30,000    85,000    Reputation mounts; bonus repair discount (22.5%) / Access to Guild Supreme Recipes
– / Master of the Guild   45,000    130,000     – / Access to Guild Westfold Recipes 
*/
