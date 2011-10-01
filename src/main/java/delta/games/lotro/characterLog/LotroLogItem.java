package delta.games.lotro.characterLog;

import java.util.Date;

/**
 * @author DAM
 */
public class LotroLogItem
{
  enum LogItemType
  {
    PROFESSION,
    LEVELUP,
    QUEST,
    DEED,
    UNKNOWN
  }

  private long _date;
  private LogItemType _type;
  private String _label;
  private String _resourceUrl;
  
  public LotroLogItem(long date, LogItemType type, String label, String resourceUrl)
  {
    _date=date;
    _type=type;
    _label=label;
    _resourceUrl=resourceUrl;
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
