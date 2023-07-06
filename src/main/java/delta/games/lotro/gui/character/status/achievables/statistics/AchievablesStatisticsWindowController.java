package delta.games.lotro.gui.character.status.achievables.statistics;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.statistics.GlobalAchievablesStatistics;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Controller for a "achievables statistics" window.
 * @author DAM
 * @param <T> Type of achievables to use
 */
public class AchievablesStatisticsWindowController<T extends Achievable> extends DefaultDialogController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="ACHIEVABLE_STATISTICS";

  // Data
  private GlobalAchievablesStatistics _statistics;
  private AchievablesStatusManager _statusMgr;
  // Controllers
  private AchievablesStatisticsPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toon Character file.
   * @param statusMgr Status manager.
   * @param mode UI mode.
   */
  public AchievablesStatisticsWindowController(WindowController parent, CharacterFile toon, AchievablesStatusManager statusMgr, AchievableUIMode mode)
  {
    super(parent);
    _statistics=new GlobalAchievablesStatistics();
    _panelController=new AchievablesStatisticsPanelController(this,_statistics,mode);
    _statusMgr=statusMgr;
    updateTitle(toon);
  }

  private void updateTitle(CharacterFile toon)
  {
    String title=buildTitle(toon);
    getDialog().setTitle(title);
  }

  private String buildTitle(CharacterFile toon)
  {
    String name=toon.getName();
    String serverName=toon.getServerName();
    String title="Statistics for "+name+" @ "+serverName; // I18n
    return title;
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
   * Update statistics.
   * @param selectedAchievables Achievables to use.
   */
  public void updateStats(List<T> selectedAchievables)
  {
    // Update status
    _panelController.updateStats(_statusMgr,selectedAchievables);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    // Data
    _statistics=null;
    _statusMgr=null;
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    super.dispose();
  }
}
