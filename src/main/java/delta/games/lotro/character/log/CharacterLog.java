package delta.games.lotro.characterLog;

import java.util.List;

/**
 * Activity log for a single character.
 * @author DAM
 */
public class CharacterLog
{
  /**
   * Name of the character.
   */
  private String _characterName;
  
  /**
   * List of log items.
   */
  private List<LotroLogItem> _logItems;
  
  /**
   * Constructor.
   * @param name Character name.
   */
  public CharacterLog(String name)
  {
    _characterName=name;
  }

  /**
   * Get the number of items in this log.
   * @return a positive integer.
   */
  public int getNbItems()
  {
    return _logItems.size();
  }

  /**
   * Add an item to this log.
   * @param item Item to add.
   */
  public void addLogItem(LotroLogItem item)
  {
    _logItems.add(item);
  }

  @Override
  public String toString()
  {
    return _characterName+" activity log ("+_logItems.size()+" items)";
  }
}

