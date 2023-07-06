package delta.games.lotro.gui.character.essences;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a "essences summary" window.
 * @author DAM
 */
public class EssencesSummaryWindowController extends DefaultDialogController implements GenericEventsListener<CharacterEvent>
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="ESSENCES_SUMMARY";

  private EssencesSummaryPanelController _summaryController;
  private CharacterData _toon;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toon Managed toon.
   */
  public EssencesSummaryWindowController(WindowController parent, CharacterData toon)
  {
    super(parent);
    _summaryController=new EssencesSummaryPanelController(toon);
    _toon=toon;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel summaryPanel=_summaryController.getPanel();
    // Register to events
    EventsManager.addListener(CharacterEvent.class,this);
    return summaryPanel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServer();
    String title="Essences summary for: "+name+" @ "+serverName; // I18n
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
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.CHARACTER_DATA_UPDATED)
    {
      CharacterData data=event.getToonData();
      if (data==_toon)
      {
        _summaryController.update();
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
    EventsManager.removeListener(CharacterEvent.class,this);
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    _toon=null;
    super.dispose();
  }
}
