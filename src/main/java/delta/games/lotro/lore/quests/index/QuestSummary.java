package delta.games.lotro.lore.quests.index;

/**
 * Quest summary.
 * @author DAM
 */
public class QuestSummary
{
  private int _identifier;
  private String _key;
  private String _name;
  
  /**
   * Constructor.
   * @param identifier Quest identifier.
   * @param key Quest key.
   * @param name Quest name.
   */
  public QuestSummary(int identifier, String key, String name)
  {
    _identifier=identifier;
    _key=key;
    _name=name;
  }
  
  /**
   * Get the quest identifier.
   * @return the quest identifier.
   */
  public int getIdentifier()
  {
    return _identifier;
  }

  /**
   * Get the quest key.
   * @return the quest key.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Get the quest name.
   * @return the quest name.
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
