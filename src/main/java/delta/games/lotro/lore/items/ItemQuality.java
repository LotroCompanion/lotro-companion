package delta.games.lotro.lore.items;

import java.util.HashMap;

/**
 * Item quality.
 * @author DAM
 */
public class ItemQuality
{
  private static HashMap<String,ItemQuality> _mapFromColor=new HashMap<String,ItemQuality>();
  private static HashMap<String,ItemQuality> _mapFromCode=new HashMap<String,ItemQuality>();

  /**
   * Common.
   */
  public static final ItemQuality COMMON=new ItemQuality("Common","White");
  /**
   * Unommon.
   */
  public static final ItemQuality UNCOMMON=new ItemQuality("Uncommon","Yellow");
  /**
   * Rare.
   */
  public static final ItemQuality RARE=new ItemQuality("Rare","Purple");
  /**
   * Incomparable.
   */
  public static final ItemQuality INCOMPARABLE=new ItemQuality("Incomparable","Teal");
  /**
   * Epic.
   */
  public static final ItemQuality EPIC=new ItemQuality("Epic","Orange");

  private String _meaning;
  private String _color;

  private ItemQuality(String meaning,String color)
  {
    _meaning=meaning;
    _color=color;
    _mapFromCode.put(meaning,this);
    _mapFromColor.put(color,this);
  }

  /**
   * Get the code for this object.
   * @return an internal identifying code.
   */
  public String getCode()
  {
    return _meaning;
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
    return _mapFromCode.get(code);
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
