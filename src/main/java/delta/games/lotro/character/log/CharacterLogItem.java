package delta.games.lotro.character.log;

import java.util.Date;

/**
 * Represents an item in a LOTRO character log.
 * @author DAM
 */
public class CharacterLogItem
{
  /**
   * Log item type.
   * @author DAM
   */
  public enum LogItemType
  {
    /**
     * Profession advance.
     */
    PROFESSION,
    /**
     * Level increase.
     */
    LEVELUP,
    /**
     * Quest.
     */
    QUEST,
    /**
     * Deed.
     */
    DEED,
    /**
     * Vocation.
     */
    VOCATION,
    /**
     * Player Versus Monster Player.
     */
    PVMP,
    /**
     * Unknown.
     */
    UNKNOWN
  }

  private long _date;
  private LogItemType _type;
  private String _label;
  private String _resourceUrl;
  private String _identifier;
  
  /**
   * Full constructor.
   * @param date Log item date.
   * @param type Log item type.
   * @param label Log item label.
   * @param resourceUrl Associated URL.
   */
  public CharacterLogItem(long date, LogItemType type, String label, String resourceUrl)
  {
    _date=date;
    _type=type;
    _label=label;
    _resourceUrl=resourceUrl;
    _identifier=null;
  }
  
  /**
   * Get log item date.
   * @return a date (milliseconds since Epoch).
   */
  public long getDate()
  {
    return _date;
  }

  /**
   * Get the type of this log item.
   * @return a log item type.
   */
  public LogItemType getLogItemType()
  {
    return _type;
  }

  /**
   * Get the label of this log item.
   * @return a label.
   */
  public String getLabel()
  {
    return _label;
  }

  /**
   * Get the URL associated with this log item.
   * @return An URL or <code>null</code> if there's none.
   */
  public String getAssociatedUrl()
  {
    return _resourceUrl;
  }

  /**
   * Get the identifier of this log item.
   * @return an identifier or <code>null</code> if there's none.
   */
  public String getIdentifier()
  {
    return _identifier;
  }

  /**
   * Set the identifier.
   * @param identifier Identifier to set.
   */
  public void setIdentifier(String identifier)
  {
    _identifier=identifier;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append(new Date(_date));
    sb.append(" [").append(_type).append("] [").append(_label).append("] [").append(_resourceUrl).append(']');
    return sb.toString();
  }
}
