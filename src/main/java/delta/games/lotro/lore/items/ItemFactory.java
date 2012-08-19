package delta.games.lotro.lore.items;

/**
 * Item builder.
 * @author DAM
 */
public class ItemFactory
{
  /**
   * Build a new item of the right class.
   * @param category Item category.
   * @return An item. 
   */
  public static Item buildItem(ItemCategory category)
  {
    Item ret;
    if (category==ItemCategory.ARMOUR)
    {
      ret=new Armour();
    }
    else if (category==ItemCategory.WEAPON)
    {
      ret=new Weapon();
    }
    else
    {
      ret=new Item();
    }
    return ret;
  }
}
