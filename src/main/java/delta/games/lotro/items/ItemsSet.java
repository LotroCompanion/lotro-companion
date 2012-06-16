package delta.games.lotro.items;

/**
 * Set of items.
 * <p>
 * Wearing more than one item of a set gives extra bonuses.
 * @author DAM
 */
public class ItemsSet
{
  private String _name;

  /**
   * Constructor.
   * @param name Name of set.
   */
  public ItemsSet(String name)
  {
    _name=name;
  }
  
  /**
   * Get the name of this set.
   * @return A name.
   */
  public String getName()
  {
    return _name;
  }
}
