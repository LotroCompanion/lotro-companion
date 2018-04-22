package delta.games.lotro.gui.stats.deeds.statistics;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.stats.deeds.DeedsStatistics;
import delta.games.lotro.stats.deeds.DeedsStatusManager;

/**
 * Controller for a "deeds statistics" window.
 * @author DAM
 */
public class DeedStatisticsWindowController extends DefaultDialogController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="DEEDS_STATISTICS";

  private DeedStatisticsPanelController _panelController;
  private DeedsStatistics _statistics;

  /**
   * Constructor.
   * @param parent Parent controller.
   */
  public DeedStatisticsWindowController(WindowController parent)
  {
    super(parent);
    _statistics=new DeedsStatistics();
    _panelController=new DeedStatisticsPanelController(_statistics);
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel summaryPanel=_panelController.getPanel();
    return summaryPanel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.pack();
    dialog.setResizable(true);
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  /**
   * Update statistics using the given deeds status.
   * @param status Deeds status to use.
   */
  public void updateStats(DeedsStatusManager status)
  {
    // Title
    String name=status.getCharacterName();
    String serverName=status.getServer();
    String title="Deeds statistics for "+name+" @ "+serverName;
    getDialog().setTitle(title);
    // Update status
    _panelController.updateStats(status);
    getDialog().pack();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    _statistics=null;
    super.dispose();
  }
}
