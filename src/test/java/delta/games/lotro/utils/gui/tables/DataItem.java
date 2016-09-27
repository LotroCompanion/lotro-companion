package delta.games.lotro.utils.gui.tables;

/**
 * Sample data item.
 * @author DAM
 */
public class DataItem
{
  enum SEX
  {
    MALE,
    FEMALE
  }

  private long _id;
  private String _name;
  private SEX _sex;

  /**
   * Constructor.
   * @param id Technical identifier.
   * @param name Name.
   * @param sex Sex.
   */
  public DataItem(long id, String name, SEX sex)
  {
    _id=id;
    _name=name;
    _sex=sex;
  }

  /**
   * @return the id
   */
  public long getId()
  {
    return _id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id)
  {
    _id=id;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return _name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name)
  {
    _name=name;
  }

  /**
   * @return the sex
   */
  public SEX getSex()
  {
    return _sex;
  }

  /**
   * @param sex the sex to set
   */
  public void setSex(SEX sex)
  {
    _sex=sex;
  }

  @Override
  public String toString()
  {
    return _id + ": " + _name + " (" + _sex + ")";
  }
}
