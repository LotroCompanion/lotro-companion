package delta.games.lotro.utils.updates;

/**
 * Version description.
 * @author DAM
 */
public class Version
{
  private int _id;
  private String _name;
  private String _url;

  /**
   * Constructor.
   * @param id Version identifier.
   * @param name Version name.
   * @param url URL.
   */
  public Version(int id, String name, String url)
  {
    _id=id;
    _name=name;
    _url=url;
  }

  /**
   * Get the version identifier.
   * @return A version identifier.
   */
  public int getId()
  {
    return _id;
  }

  /**
   * Get the version name.
   * @return A version name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get the version URL.
   * @return A version URL.
   */
  public String getUrl()
  {
    return _url;
  }

  @Override
  public String toString()
  {
    return "ID: "+_id+", name:"+_name+", URL:"+_url;
  }
}
