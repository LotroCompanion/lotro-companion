package delta.games.lotro.lore.items;

import java.util.HashMap;

/**
 * Item quality.
 * @author DAM
 */
public class ItemQuality
{
  private static HashMap<String,ItemQuality> _mapFromCode=new HashMap<String,ItemQuality>();
  private static HashMap<String,ItemQuality> _mapFromColor=new HashMap<String,ItemQuality>();
  private static HashMap<String,ItemQuality> _mapFromMeaning=new HashMap<String,ItemQuality>();

  /**
   * Common.
   */
  public static final ItemQuality COMMON=new ItemQuality("COMMON","Common","White");
  /**
   * Unommon.
   */
  public static final ItemQuality UNCOMMON=new ItemQuality("UNCOMMON","Uncommon","Yellow");
  /**
   * Rare.
   */
  public static final ItemQuality RARE=new ItemQuality("RARE","Rare","Purple");
  /**
   * Incomparable.
   */
  public static final ItemQuality INCOMPARABLE=new ItemQuality("INCOMPARABLE","Incomparable","Teal");
  /**
   * Epic.
   */
  public static final ItemQuality LEGENDARY=new ItemQuality("LEGENDARY","Epic","Orange");

  private String _code;
  private String _meaning;
  private String _color;

  private ItemQuality(String code, String meaning, String color)
  {
    _code=code;
    _meaning=meaning;
    _color=color;
    _mapFromCode.put(code,this);
    _mapFromMeaning.put(meaning,this);
    _mapFromColor.put(color,this);
  }

  /**
   * Get the code for this object.
   * @return an internal identifying code.
   */
  public String getCode()
  {
    return _code;
  }

  /**
   * Get the quality label.
   * @return the quality label.
   */
  public String getMeaning()
  {
    return _meaning;
  }

  /**
   * Get the associated color.
   * @return a color name.
   */
  public String getColor()
  {
    return _color;
  }

  /**
   * Get an item quality from its code.
   * @param code Code to use.
   * @return An item quality or <code>null</code> if not found.
   */
  public static ItemQuality fromCode(String code)
  {
    return _mapFromMeaning.get(code);
  }
  
  /**
   * Get an item quality from its color.
   * @param color Color to use.
   * @return An item quality or <code>null</code> if not found.
   */
  public static ItemQuality fromColor(String color)
  {
    return _mapFromColor.get(color);
  }
  
  @Override
  public String toString()
  {
    return _meaning;
  }
}
