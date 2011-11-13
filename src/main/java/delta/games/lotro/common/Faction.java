package delta.games.lotro.common;

/**
 * Represents a faction in the LOTRO world.
 * @author DAM
 */
public class Faction
{
  private String _name;
  
  /**
   * Constructor.
   * @param name
   */
  public Faction(String name)
  {
    _name=name;
  }

  /**
   * Get the name of this faction.
   * @return the name of this faction.
   */
  public String getName()
  {
    return _name;
  }
}
