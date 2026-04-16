package delta.games.lotro.gui.utils.dates;

import delta.games.lotro.common.Duration;

/**
 * Controller for a label to display a date and an age.
 * @author DAM
 */
public class DecayLabelController extends AbstractTimedLabelController
{
  // Data
  private Long _endDecayDate;

  /**
   * Constructor.
   */
  public DecayLabelController()
  {
    super();
    _endDecayDate=null;
  }

  /**
   * Set the end decay date to use.
   * @param date A date or <code>null</code>.
   */
  public void setEndDecayDate(Long date)
  {
    _endDecayDate=date;
    updateDisplay();
  }

  protected String getDisplay()
  {
    if (_endDecayDate==null)
    {
      return "";
    }
    // Compute time left
    long now=System.currentTimeMillis();
    long timeLeft=_endDecayDate.longValue()-now;
    if (timeLeft>0)
    {
      int secondsLeft=(int)(timeLeft/1000);
      String durationStr=Duration.getSmartDurationString(secondsLeft);
      return "Expires in: "+durationStr; // I18n
    }
    return "Expired"; // I18n
  }
}
