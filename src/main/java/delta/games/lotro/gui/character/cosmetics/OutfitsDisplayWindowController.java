package delta.games.lotro.gui.character.cosmetics;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.cosmetics.OutfitsManager;
import delta.games.lotro.character.cosmetics.io.xml.OutfitsIO;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a "outfits" window.
 * @author DAM
 */
public class OutfitsDisplayWindowController extends DefaultDisplayDialogController<Void> implements GenericEventsListener<CharacterEvent>
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="OUTFITS";

  // Data
  private CharacterFile _toon;
  // Controllers
  private OutfitsDisplayPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toon Character to use.
   */
  public OutfitsDisplayWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent,null);
    _toon=toon;
    // Display
    _panelController=new OutfitsDisplayPanelController(this);
    updateContents();
    EventsManager.addListener(CharacterEvent.class,this);
  }

  @Override
  protected JPanel buildFormPanel()
  {
    return _panelController.getPanel();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Outfits for "+name+" @ "+serverName; // I18n
    dialog.setTitle(title);
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
    if (type==CharacterEventType.OUTFITS_UPDATED)
    {
      CharacterFile toon=event.getToonFile();
      if (toon==_toon)
      {
        updateContents();
      }
    }
  }

  /**
   * Update contents.
   */
  private void updateContents()
  {
    // Update contents
    OutfitsManager mgr=OutfitsIO.loadOutfits(_toon);
    if (mgr==null)
    {
      mgr=new OutfitsManager();
    }
    _panelController.updateContents(mgr);
    getDialog().pack();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    EventsManager.removeListener(CharacterEvent.class,this);
    // Data
    _toon=null;
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    super.dispose();
  }
}
