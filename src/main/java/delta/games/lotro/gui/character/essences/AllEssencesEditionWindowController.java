package delta.games.lotro.gui.character.essences;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventListener;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;

/**
 * Controller for a "all essences edition" window.
 * @author DAM
 */
public class AllEssencesEditionWindowController extends DefaultWindowController implements CharacterEventListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="ESSENCES";

  private AllEssencesEditionPanelController _editionController;
  private CharacterData _toon;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Managed toon.
   */
  public AllEssencesEditionWindowController(WindowController parent, CharacterData toon)
  {
    super(parent);
    _editionController=new AllEssencesEditionPanelController(this,toon);
    _toon=toon;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel editionPanel=_editionController.getPanel();
    // Register to events
    CharacterEventsManager.addListener(this);
    return editionPanel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServer();
    String title="Essences for: "+name+" @ "+serverName;
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
   * Handle character events.
   * @param type Event type.
   * @param event Source event.
   */
  public void eventOccurred(CharacterEventType type, CharacterEvent event)
  {
    if (type==CharacterEventType.CHARACTER_DATA_UPDATED)
    {
      CharacterData data=event.getToonData();
      if (data==_toon)
      {
        _editionController.update();
        getWindow().pack();
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
    if (_editionController!=null)
    {
      _editionController.dispose();
      _editionController=null;
    }
    _toon=null;
    super.dispose();
  }
}
