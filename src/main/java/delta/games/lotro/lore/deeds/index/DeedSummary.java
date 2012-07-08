package delta.games.lotro.lore.deeds.index;

/**
 * Deed summary.
 * @author DAM
 */
public class DeedSummary
{
  private String _id;
  private String _name;
  
  /**
   * Constructor.
   * @param id Deed identifier.
   * @param name Deed name.
   */
  public DeedSummary(String id, String name)
  {
    _id=id;
    _name=name;
  }
  
  /**
   * Get the deed identifier.
   * @return the deed identifier.
   */
  public String getId()
  {
    return _id;
  }

  /**
   * Get the deed name.
   * @return the deed name.
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
