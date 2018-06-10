package delta.games.lotro.stats.deeds.geo;

import java.util.Date;

/**
 * Status of a single deed for a single character.
 * @author DAM
 */
public class DeedGeoPointStatus
{
  private int _pointId;
  private Boolean _completed;
  private Long _completionDate;

  /**
   * Constructor.
   * @param pointId
   */
  public DeedGeoPointStatus(int pointId)
  {
    _pointId=pointId;
  }

  /**
   * Get the identifier of the targeted point.
   * @return A point identifier.
   */
  public int getPointId()
  {
    return _pointId;
  }

  /**
   * Indicates if this deed geo point is completed or not, or if it is unknown.
   * @return A 3-state boolean.
   */
  public Boolean isCompleted()
  {
    return _completed;
  }

  /**
   * Set the completion status.
   * @param completed Completion status to set.
   */
  public void setCompleted(Boolean completed)
  {
    _completed=completed;
  }

  /**
   * Get the completion date.
   * @return A date or <code>null</code> if not completed or completion date is not known.
   */
  public Long getCompletionDate()
  {
    return _completionDate;
  }

  /**
   * Set the completion date.
   * @param completionDate Completion date to set.
   */
  public void setCompletionDate(Long completionDate)
  {
    _completionDate=completionDate;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Point: ").append(_pointId).append(": ");
    if (_completed==Boolean.TRUE)
    {
      sb.append("completed");
      if (_completionDate!=null)
      {
        sb.append(" (").append(new Date(_completionDate.longValue())).append(')');
      }
    }
    else if (_completed==Boolean.FALSE)
    {
      sb.append("NOT completed");
    }
    else
    {
      sb.append("unknown status");
    }
    return sb.toString();
  }
}
