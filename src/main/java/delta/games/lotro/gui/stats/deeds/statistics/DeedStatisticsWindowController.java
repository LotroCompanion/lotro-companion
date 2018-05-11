package delta.games.lotro.gui.stats.deeds.statistics;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.stats.deeds.DeedsStatistics;
import delta.games.lotro.stats.deeds.DeedsStatusManager;
import delta.games.lotro.stats.deeds.io.DeedsStatusIo;

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

  // Data
  private CharacterFile _toon;
  private DeedsStatusManager _deedsStatus;
  private DeedsStatistics _statistics;
  // Controllers
  private DeedStatisticsPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toon Character file.
   */
  public DeedStatisticsWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    _toon=toon;
    _statistics=new DeedsStatistics();
    _panelController=new DeedStatisticsPanelController(this,_statistics);
    updateStats();
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
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(700,700);
    dialog.setResizable(true);
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  private void loadDeedsStatus()
  {
    _deedsStatus=DeedsStatusIo.load(_toon);
  }

  /**
   * Update deeds statistics.
   */
  private void updateStats()
  {
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Deeds statistics for "+name+" @ "+serverName;
    getDialog().setTitle(title);
    // Update status
    loadDeedsStatus();
    _panelController.updateStats(_deedsStatus);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    // Data
    _toon=null;
    _deedsStatus=null;
    _statistics=null;
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    super.dispose();
  }
}
