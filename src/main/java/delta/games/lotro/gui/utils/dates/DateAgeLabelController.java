package delta.games.lotro.gui.utils.dates;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.text.dates.DateCodec;
import delta.games.lotro.common.Duration;
import delta.games.lotro.gui.utils.l10n.DateFormat;

/**
 * Controller for a label to display a date and an age.
 * @author DAM
 */
public class DateAgeLabelController
{
  // Data
  private Long _date;
  // UI
  private JLabel _display;
  private Timer _timer;

  /**
   * Constructor.
   */
  public DateAgeLabelController()
  {
    _date=null;
    _display=GuiFactory.buildLabel("");
    setupTimer();
  }

  /**
   * Get the managed label.
   * @return the managed label.
   */
  public JLabel getLabel()
  {
    return _display;
  }

  private void setupTimer()
  {
    ActionListener al=new ActionListener()
    {
      
      @Override
      public void actionPerformed(ActionEvent e)
      {
        updateDisplay();
      }
    };
    _timer=new Timer(1000,al);
    _timer.start();
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

  /**
   * Update the display.
   */
  public void updateDisplay()
  {
    String display=getDisplay();
    _display.setText(display);
  }

  private String getDisplay()
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
    return dateStr+" ("+durationStr+")";
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_timer!=null)
    {
      _timer.stop();
      _timer=null;
    }
  }
}
