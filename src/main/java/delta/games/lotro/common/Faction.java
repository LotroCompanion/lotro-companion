package delta.games.lotro.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a faction in the LOTRO world.
 * @author DAM
 */
public class Faction
{
  private String _name;
  private Set<String> _aliases;
  private FactionLevel _initialLevel;
  private FactionLevel[] _levels;
  
  /**
   * Constructor.
   * @param name Name of faction.
   * @param levels Available levels.
   * @param initialLevel Initial level.
   */
  public Faction(String name, List<FactionLevel> levels, FactionLevel initialLevel)
  {
    _name=name;
    _aliases=new HashSet<String>();
    _levels=levels.toArray(new FactionLevel[levels.size()]);
    _initialLevel=initialLevel;
  }

  /**
   * Get the name of this faction.
   * @return the name of this faction.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get the aliases for this faction.
   * @return A possibly empty array of strings.
   */
  public String[] getAliases()
  {
    return _aliases.toArray(new String[_aliases.size()]);
  }

  /**
   * Add an alias to this faction.
   * @param alias Alias to add.
   */
  public void addAlias(String alias)
  {
    _aliases.add(alias);
  }

  /**
   * Get the initial reputation level in this faction.
   * @return a faction level;
   */
  public FactionLevel getInitialLevel()
  {
    return _initialLevel;
  }

  /**
   * Get the ordered levels for this faction.
   * @return an array of levels.
   */
  public FactionLevel[] getLevels()
  {
    return _levels;
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
