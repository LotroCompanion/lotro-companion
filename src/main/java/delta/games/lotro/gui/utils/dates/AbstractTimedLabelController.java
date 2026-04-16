package delta.games.lotro.gui.utils.dates;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;

/**
 * Controller for a label to display a date and an age.
 * @author DAM
 */
public abstract class AbstractTimedLabelController implements Disposable
{
  // UI
  private JLabel _display;
  private Timer _timer;

  /**
   * Constructor.
   */
  protected AbstractTimedLabelController()
  {
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
   * Update the display.
   */
  public void updateDisplay()
  {
    String display=getDisplay();
    _display.setText(display);
  }

  protected abstract String getDisplay();

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
