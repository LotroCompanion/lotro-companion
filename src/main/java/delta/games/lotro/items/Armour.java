package delta.games.lotro.items;

import delta.common.utils.text.EndOfLine;

/**
 * Armour description.
 * @author DAM
 */
public class Armour extends Item
{
  private int _armourValue;
  
  /**
   * Constructor.
   */
  public Armour()
  {
    super();
    setCategory(ItemCategory.ARMOUR);
  }
  
  /**
   * Get the armour value for this item.
   * @return an armour value.
   */
  public int getArmourValue()
  {
    return _armourValue;
  }

  /**
   * Set the armour value for this item.
   * @param armourValue Armour value to set.
   */
  public void setArmourValue(int armourValue)
  {
    _armourValue=armourValue;
  }
  
  /**
   * Dump the contents of this quest as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    String itemDump=super.dump();
    sb.append(itemDump);
    sb.append(EndOfLine.NATIVE_EOL);
    sb.append("Armour value=");
    sb.append(_armourValue);
    return sb.toString();
  }
}
