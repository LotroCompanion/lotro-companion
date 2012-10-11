package delta.games.lotro.lore.deeds.index;

/**
 * Deed summary.
 * @author DAM
 */
public class DeedSummary
{
  private int _identifier;
  private String _key;
  private String _name;
  
  /**
   * Constructor.
   * @param identifier Deed identifier.
   * @param key Deed key.
   * @param name Deed name.
   */
  public DeedSummary(int identifier, String key, String name)
  {
    _identifier=identifier;
    _key=key;
    _name=name;
  }
  
  /**
   * Get the deed identifier.
   * @return the deed identifier.
   */
  public int getIdentifier()
  {
    return _identifier;
  }

  /**
   * Get the deed key.
   * @return the deed key.
   */
  public String getKey()
  {
    return _key;
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
    return _key+" ["+_identifier+"] ["+_name+"]";
  }
}
