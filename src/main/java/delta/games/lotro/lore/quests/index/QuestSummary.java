package delta.games.lotro.lore.quests.index;

/**
 * Quest summary.
 * @author DAM
 */
public class QuestSummary
{
  private String _id;
  private String _name;
  
  /**
   * Constructor.
   * @param id Quest identifier.
   * @param name Quest name.
   */
  public QuestSummary(String id, String name)
  {
    _id=id;
    _name=name;
  }
  
  /**
   * Get the quest identifier.
   * @return the quest identifier.
   */
  public String getId()
  {
    return _id;
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
    return _id+" ["+_name+"]";
  }
}
