package delta.games.lotro.region;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.text.EndOfLine;

/**
 * Area.
 * @author DAM
 */
public class Area
{
  private String _identifier;
  private String _name;
  private List<String> _questIdentifiers;

  /**
   * Constructor.
   * @param identifier Internal identifier.
   * @param name Area's name.
   */
  public Area(String identifier, String name)
  {
    _identifier=identifier;
    _name=name;
    _questIdentifiers=new ArrayList<String>();
  }
  
  /**
   * Get the internal identifier.
   * @return the internal identifier.
   */
  public String getIdentifier()
  {
    return _identifier;
  }

  /**
   * Get the area's name.
   * @return the area's name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Add a quest in this area.
   * @param questIdentifier Identifier of quest to add.
   */
  public void addQuest(String questIdentifier)
  {
    if (questIdentifier!=null)
    {
      if (!_questIdentifiers.contains(questIdentifier))
      {
        _questIdentifiers.add(questIdentifier);
      }
    }
  }

  /**
   * Get all the quests of this area.
   * @return an array of quests.
   */
  public String[] getQuestIdentifiers()
  {
    String[] ret=new String[_questIdentifiers.size()];
    return _questIdentifiers.toArray(ret);
  }

  /**
   * Dump the contents of this area as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Area: ").append(_name);
    sb.append(" (").append(_identifier).append(')');
    sb.append(EndOfLine.NATIVE_EOL);
    for(String questIdentifier : _questIdentifiers)
    {
      sb.append("\tQuest: ").append(questIdentifier);
      sb.append(EndOfLine.NATIVE_EOL);
    }
    return sb.toString();
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
