package delta.games.lotro.lore.items.index;

/**
 * Item summary.
 * @author DAM
 */
public class ItemSummary
{
  private String _id;
  private String _name;
  
  /**
   * Constructor.
   * @param id Item identifier.
   * @param name Item name.
   */
  public ItemSummary(String id, String name)
  {
    _id=id;
    _name=name;
  }
  
  /**
   * Get the item identifier.
   * @return the item identifier.
   */
  public String getId()
  {
    return _id;
  }

  /**
   * Get the item name.
   * @return the item name.
   */
  public String getName()
  {
    return _name;
  }

  @Override
  public String toString()
  {
    return _id+" ["+_name+"]";
  }
}
