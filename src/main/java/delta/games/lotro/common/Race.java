package delta.games.lotro.common;

import java.util.HashMap;

/**
 * Character race.
 * @author DAM
 */
public class Race
{
  private static HashMap<String,Race> _instances=new HashMap<String,Race>();
  private String _label;
  private String _iconPath;
  
  /**
   * Dwarf.
   */
  public static final Race DWARF=new Race("Dwarf","dwarf");
  /**
   * Elf.
   */
  public static final Race ELF=new Race("Elf","elf");
  /**
   * Hobbit.
   */
  public static final Race HOBBIT=new Race("Hobbit","hobbit");
  /**
   * Man.
   */
  public static final Race MAN=new Race("Race of Man","man");

  private Race(String label, String iconPath)
  {
    _label=label;
    _iconPath=iconPath;
    _instances.put(label,this);
  }

  /**
   * Get the displayable name of this race.
   * @return A displayable label.
   */
  public String getLabel()
  {
    return _label;
  }

  /**
   * Get the name of the associated icon.
   * @return a icon path.
   */
  public String getIconPath()
  {
    return _iconPath;
  }

  /**
   * Get a character race instance by its label.
   * @param label Label to search.
   * @return A character race or <code>null</code> if not found.
   */
  public static Race getByLabel(String label)
  {
    Race ret=_instances.get(label);
    return ret;
  }

  @Override
  public String toString()
  {
    return _label;
  }
}
