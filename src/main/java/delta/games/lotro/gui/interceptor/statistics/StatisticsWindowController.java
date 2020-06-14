package delta.games.lotro.gui.interceptor.statistics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Timer;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.interceptor.protocol.PacketsStatistics;

/**
 * Controller for a "synchronizer statistics" window.
 * @author DAM
 */
public class StatisticsWindowController extends DefaultDialogController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="SYNCHRONIZER_STATISTICS";

  // Contents controller
  private StatisticsPanelController _statisticsPanelController;
  // Timer
  private Timer _timer;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param statistics Statistics to show.
   */
  public StatisticsWindowController(WindowController parent, PacketsStatistics statistics)
  {
    super(parent);
    _statisticsPanelController=new StatisticsPanelController(statistics);
    start();
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel = _statisticsPanelController.getPanel();
    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    // Title
    String title="Synchronizer statistics";
    dialog.setTitle(title);
    dialog.pack();
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  /**
   * Start timer to refresh data.
   */
  public void start()
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        update();
      }
    };
    _timer=new Timer(2000,al);
    _timer.setRepeats(true);
    _timer.start();
  }

  /**
   * Stop timer to refresh data.
   */
  public void stop()
  {
    if (_timer!=null)
    {
      _timer.stop();
      _timer=null;
    }
  }

  /**
   * Update values.
   */
  public void update()
  {
    _statisticsPanelController.update();
    getWindow().pack();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_statisticsPanelController!=null)
    {
      _statisticsPanelController.dispose();
      _statisticsPanelController=null;
    }
    stop();
    super.dispose();
  }
}
