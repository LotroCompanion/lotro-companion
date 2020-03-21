package delta.games.lotro.gui.stats.deeds.statistics;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.deeds.DeedsStatusManager;
import delta.games.lotro.character.deeds.io.DeedsStatusIo;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.stats.deeds.DeedsStatistics;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a "deeds statistics" window.
 * @author DAM
 */
public class DeedStatisticsWindowController extends DefaultDialogController implements GenericEventsListener<CharacterEvent>
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="DEEDS_STATISTICS";

  // Data
  private CharacterFile _toon;
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
    EventsManager.addListener(CharacterEvent.class,this);
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

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.DEEDS_STATUS_UPDATED)
    {
      CharacterFile toon=event.getToonFile();
      if (toon==_toon)
      {
        updateStats();
      }
    }
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
    DeedsStatusManager deedsStatus=DeedsStatusIo.load(_toon);
    _panelController.updateStats(deedsStatus);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    EventsManager.removeListener(CharacterEvent.class,this);
    // Data
    _toon=null;
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
