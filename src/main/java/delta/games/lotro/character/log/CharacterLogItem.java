package delta.games.lotro.character.log;

import java.util.Date;

import delta.common.utils.NumericTools;

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

  private static final String ID_SEED="?id=";

  /**
   * Get the resource identifier of this log item.
   * @return an integer identifier or <code>null</code> if there's none.
   */
  public Integer getResourceIdentifier()
  {
    Integer ret=null;
    if (_resourceUrl!=null)
    {
      int index=_resourceUrl.indexOf(ID_SEED);
      if (index!=-1)
      {
        String idStr=_resourceUrl.substring(index+ID_SEED.length());
        ret=NumericTools.parseInteger(idStr);
      }
    }
    return ret;
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
