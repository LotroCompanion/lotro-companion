package delta.games.lotro.gui.stats.achievables.statistics;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.achievables.AchievablesStatusManager;
import delta.games.lotro.gui.stats.achievables.AchievableUIMode;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.stats.achievables.AchievablesStatistics;

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
  private AchievablesStatistics _statistics;
  private AchievablesStatusManager _statusMgr;
  private List<T> _achievables;
  private Filter<T> _filter;
  // Controllers
  private AchievablesStatisticsPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toon Character file.
   * @param statusMgr Status manager.
   * @param achievables Achievables to use.
   * @param filter Current filter.
   * @param mode UI mode.
   */
  public AchievablesStatisticsWindowController(WindowController parent, CharacterFile toon, AchievablesStatusManager statusMgr, List<T> achievables, Filter<T> filter, AchievableUIMode mode)
  {
    super(parent);
    _statistics=new AchievablesStatistics();
    _panelController=new AchievablesStatisticsPanelController(this,_statistics,mode);
    _statusMgr=statusMgr;
    _achievables=new ArrayList<T>(achievables);
    _filter=filter;
    updateStats();
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
    String title="Statistics for "+name+" @ "+serverName;
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
   */
  public void updateStats()
  {
    // Update status
    List<T> selectedAchievables=new ArrayList<T>();
    for(T achievable : _achievables)
    {
      if (_filter.accept(achievable))
      {
        selectedAchievables.add(achievable);
      }
    }
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
    _achievables=null;
    _filter=null;
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    super.dispose();
  }
}
