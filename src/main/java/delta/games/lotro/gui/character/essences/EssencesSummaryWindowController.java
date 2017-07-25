package delta.games.lotro.gui.character.essences;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterData;

/**
 * Controller for a "essences summary" window.
 * @author DAM
 */
public class EssencesSummaryWindowController extends DefaultWindowController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="ESSENCES_SUMMARY";

  private EssencesSummaryPanelController _summaryController;
  private CharacterData _toon;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public EssencesSummaryWindowController(CharacterData toon)
  {
    _summaryController=new EssencesSummaryPanelController(toon);
    _toon=toon;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel summaryPanel=_summaryController.getPanel();
    return summaryPanel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServer();
    String title="Essences summary for: "+name+" @ "+serverName;
    frame.setTitle(title);
    frame.pack();
    frame.setResizable(true);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    _toon=null;
    super.dispose();
  }
}
