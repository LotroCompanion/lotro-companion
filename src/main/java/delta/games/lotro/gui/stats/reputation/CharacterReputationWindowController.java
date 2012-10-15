package delta.games.lotro.gui.stats.reputation;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.stats.reputation.ReputationStats;
import delta.games.lotro.utils.gui.DefaultWindowController;

/**
 * Controller for a "character reputation" window.
 * @author DAM
 */
public class CharacterReputationWindowController extends DefaultWindowController
{
  private ReputationPanelController _reputationPanelController;
  private CharacterFile _toon;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterReputationWindowController(CharacterFile toon)
  {
    _toon=toon;
    CharacterLog log=toon.getLastCharacterLog();
    ReputationStats stats=new ReputationStats(log);
    _reputationPanelController=new ReputationPanelController(stats);
  }

  /**
   * Get the window identifier for a given toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A window identifier.
   */
  public static String getIdentifier(String serverName, String toonName)
  {
    String id="REPUTATION#"+serverName+"#"+toonName;
    id=id.toUpperCase();
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=_reputationPanelController.getPanel();
    return panel;
  }
  
  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Reputation for "+name+" @ "+serverName;
    frame.setTitle(title);
    frame.pack();
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
    super.dispose();
    if (_reputationPanelController!=null)
    {
      _reputationPanelController.dispose();
      _reputationPanelController=null;
    }
    _toon=null;
  }
}
