package delta.games.lotro.gui.character.stats;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.utils.gui.DefaultWindowController;

/**
 * Controller for a "detailed character stats" window.
 * @author DAM
 */
public class CharacterStatsWindowController extends DefaultWindowController
{
  private CharacterStatsPanelController _statsPanelController;
  private CharacterFile _toon;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterStatsWindowController(CharacterFile toon)
  {
    _statsPanelController=new CharacterStatsPanelController();
    _toon=toon;
  }

  /**
   * Get the window identifier for a given toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A window identifier.
   */
  public static String getIdentifier(String serverName, String toonName)
  {
    String id="DETAILED_STATS#"+serverName+"#"+toonName;
    id=id.toUpperCase();
    return id;
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
    String serverName=_toon.getServerName();
    String title="Detailed stats for: "+name+" @ "+serverName;
    frame.setTitle(title);
    frame.pack();
    frame.setResizable(false);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    String serverName=_toon.getServerName();
    String toonName=_toon.getName();
    String id=getIdentifier(serverName,toonName);
    return id;
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
  }
}
