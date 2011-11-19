package delta.games.lotro.common;

import java.util.HashMap;

/**
 * Represents a faction in the LOTRO world.
 * @author DAM
 */
public class Faction
{
  private static final HashMap<String,Faction> _registry=new HashMap<String,Faction>();

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
   * Get a faction instance by name.
   * @param name Name of the faction to get.
   * @return A faction instance or <code>null</code> if <code>name</code> is <code>null</code> or empty.
   */
  public static Faction getByName(String name)
  {
    Faction f=null;
    if ((name!=null) && (name.length()>0))
    {
      f=_registry.get(name);
      if (f==null)
      {
        f=new Faction(name);
        _registry.put(name,f);
      }
    }
    return f;
  }

  /**
   * Get the name of this faction.
   * @return the name of this faction.
   */
  public String getName()
  {
    return _name;
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
