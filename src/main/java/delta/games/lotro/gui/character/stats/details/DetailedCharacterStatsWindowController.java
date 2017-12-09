package delta.games.lotro.gui.character.stats.details;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.gui.character.stats.curves.StatCurvesWindowsManager;

/**
 * Controller for a "detailed character stats" window.
 * @author DAM
 */
public class DetailedCharacterStatsWindowController extends DefaultDialogController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="DETAILS";

  private DetailedCharacterStatsPanelController _statsPanelController;
  private CharacterData _toon;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toon Managed toon.
   */
  public DetailedCharacterStatsWindowController(WindowController parent, CharacterData toon)
  {
    super(parent);
    StatCurvesWindowsManager statCurvesMgr=new StatCurvesWindowsManager(parent,toon);
    _statsPanelController=new DetailedCharacterStatsPanelController(statCurvesMgr);
    _toon=toon;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel = _statsPanelController.getPanel();
    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServer();
    String title="Detailed stats for: "+name+" @ "+serverName;
    dialog.setTitle(title);
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
   * Set stats to display.
   * @param reference Reference stats (may be <code>null</code>).
   * @param current Current stats.
   */
  public void setStats(BasicStatsSet reference, BasicStatsSet current)
  {
    _statsPanelController.setStats(reference,current);
    getWindow().pack();
  }

  /**
   * Update values.
   */
  public void update()
  {
    _statsPanelController.update();
    getWindow().pack();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_statsPanelController!=null)
    {
      _statsPanelController.dispose();
      _statsPanelController=null;
    }
    _toon=null;
    super.dispose();
  }
}
