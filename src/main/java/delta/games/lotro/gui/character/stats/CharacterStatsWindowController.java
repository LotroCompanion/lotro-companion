package delta.games.lotro.gui.character.stats;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.utils.gui.DefaultWindowController;

/**
 * Controller for a "detailed character stats" window.
 * @author DAM
 */
public class CharacterStatsWindowController extends DefaultWindowController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="DETAILS";

  private CharacterStatsPanelController _statsPanelController;
  private CharacterData _toon;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterStatsWindowController(CharacterData toon)
  {
    _statsPanelController=new CharacterStatsPanelController();
    _toon=toon;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel = _statsPanelController.getPanel();
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServer();
    String title="Detailed stats for: "+name+" @ "+serverName;
    frame.setTitle(title);
    frame.pack();
    frame.setResizable(false);
    return frame;
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
  }

  /**
   * Update values.
   */
  public void update()
  {
    _statsPanelController.update();
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
