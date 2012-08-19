package delta.games.lotro.common;

import java.util.HashMap;

/**
 * Character class.
 * @author DAM
 */
public class CharacterClass
{
  private static HashMap<String,CharacterClass> _instances=new HashMap<String,CharacterClass>();
  private String _label;
  private String _iconPath;
  
  /**
   * Burglar.
   */
  public static final CharacterClass BURGLAR=new CharacterClass("Burglar","burglar");
  /**
   * Captain.
   */
  public static final CharacterClass CAPTAIN=new CharacterClass("Captain","captain");
  /**
   * Champion.
   */
  public static final CharacterClass CHAMPION=new CharacterClass("Champion","champion");
  /**
   * Guardian.
   */
  public static final CharacterClass GUARDIAN=new CharacterClass("Guardian","guardian");
  /**
   * Hunter.
   */
  public static final CharacterClass HUNTER=new CharacterClass("Hunter","hunter");
  /**
   * Lore master.
   */
  public static final CharacterClass LORE_MASTER=new CharacterClass("Lore-master","loremaster");
  /**
   * Minstrel.
   */
  public static final CharacterClass MINSTREL=new CharacterClass("Minstrel","minstrel");
  /**
   * Rune-keeper.
   */
  public static final CharacterClass RUNE_KEEPER=new CharacterClass("Rune-keeper","runekeeper");
  /**
   * Warden.
   */
  public static final CharacterClass WARDEN=new CharacterClass("Warden","warden");

  private CharacterClass(String label, String iconPath)
  {
    _label=label;
    _iconPath=iconPath;
    _instances.put(label,this);
  }

  /**
   * Get the displayable name of this class.
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
   * Get a character class instance by its label.
   * @param label Label to search.
   * @return A character class or <code>null</code> if not found.
   */
  public static CharacterClass getByLabel(String label)
  {
    CharacterClass ret=_instances.get(label);
    return ret;
  }

  @Override
  public String toString()
  {
    return _label;
  }
}
