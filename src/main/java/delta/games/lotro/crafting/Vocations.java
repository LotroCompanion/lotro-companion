package delta.games.lotro.crafting;

import java.util.HashMap;

/**
 * Vocations registry.
 * @author DAM
 */
public class Vocations
{
  private static Vocations _instance=new Vocations();
  
  private HashMap<String,Vocation> _vocations;
  
  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static Vocations getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private Vocations()
  {
    _vocations=new HashMap<String,Vocation>();
    addVocation("Armourer","Prospector","Metalsmith","Tailor");
    addVocation("Armsman","Prospector","Weaponsmith","Woodworker");
    addVocation("Explorer","Forester","Prospector","Tailor");
    addVocation("Historian","Farmer","Scholar","Weaponsmith");
    addVocation("Tinker","Prospector","Cook","Jeweller");
    addVocation("Woodsman","Farmer","Forester","Woodworker");
    addVocation("Yeoman","Farmer","Cook","Tailor");
  }
  
  private void addVocation(String name, String... professions)
  {
    Vocation v=new Vocation(name,professions);
    _vocations.put(name,v);
  }

  /**
   * Get a vocation by its name. 
   * @param name Name of vocation to get.
   * @return A vocation or <code>null</code> if not found.
   */
  public Vocation getVocationByName(String name)
  {
    return _vocations.get(name);
  }
}
