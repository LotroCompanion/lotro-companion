package delta.games.lotro.crafting;

import java.util.HashSet;
import java.util.Set;

/**
 * Vocation.
 * @author DAM
 */
public class Vocation
{
  private String _name;
  private Set<String> _professions;
  
  /**
   * Constructor.
   * @param name
   * @param professions
   */
  public Vocation(String name, String... professions)
  {
    _name=name;
    _professions=new HashSet<String>();
    for(String profession : professions)
    {
      _professions.add(profession);
    }
  }

  /**
   * Get the name of this vocation. 
   * @return the name of this vocation.
   */
  public String getName()
  {
    return _name;
  }
  
  /**
   * Get the professions of this vocation. 
   * @return An array of profession names.
   */
  public String[] getProfessions()
  {
    return _professions.toArray(new String[_professions.size()]);
  }
}
