package delta.games.lotro.gui.character.essences;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventListener;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;

/**
 * Controller for a "essences summary" window.
 * @author DAM
 */
public class EssencesSummaryWindowController extends DefaultWindowController implements CharacterEventListener
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
    // Register to events
    CharacterEventsManager.addListener(this);
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

  public void eventOccured(CharacterEventType type, CharacterEvent event)
  {
    if (type==CharacterEventType.CHARACTER_DATA_UPDATED)
    {
      CharacterData data=event.getToonData();
      if (data==_toon)
      {
        _summaryController.update();
        getFrame().pack();
      }
    }
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    CharacterEventsManager.removeListener(this);
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    _toon=null;
    super.dispose();
  }
}
