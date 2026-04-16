package delta.games.lotro.gui.utils.dates;

import delta.common.ui.swing.text.dates.DateCodec;
import delta.games.lotro.common.Duration;
import delta.games.lotro.gui.utils.l10n.DateFormat;

/**
 * Controller for a label to display a date and an age.
 * @author DAM
 */
public class DateAgeLabelController extends AbstractTimedLabelController
{
  // Data
  private Long _date;

  /**
   * Constructor.
   */
  public DateAgeLabelController()
  {
    super();
    _date=null;
  }

  /**
   * Set the date to use.
   * @param date A date or <code>null</code>.
   */
  public void setDate(Long date)
  {
    _date=date;
    updateDisplay();
  }

  protected String getDisplay()
  {
    if (_date==null)
    {
      return "-";
    }
    // Compute age (seconds)
    long now=System.currentTimeMillis();
    // Age in seconds
    int age=(int)((now-_date.longValue())/1000);
    // Date codec
    DateCodec codec;
    if (age<Duration.DAY)
    {
      codec=DateFormat.getDateTimeCodec();
    }
    else
    {
      codec=DateFormat.getDateCodec();
    }
    String dateStr=codec.formatDate(_date);
    String durationStr=Duration.getSmartDurationString(age);
    return dateStr+" ("+durationStr+")"; // I18n
  }
}
