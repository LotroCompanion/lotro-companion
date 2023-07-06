package delta.games.lotro.gui.character.status.emotes.synopsis;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Controller for a "emotes synopsis" window.
 * @author DAM
 */
public class EmotesSynopsisWindowController extends DefaultWindowController
{
  private static final String EMOTES_PREFERENCES_NAME="emotesSynopsis";
  private static final String TOON_NAME_PREFERENCE="emotes.synopsis.registered.toon";

  private EmotesSynopsisPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window controller.
   */
  public EmotesSynopsisWindowController(WindowController parent)
  {
    super(parent);
    List<CharacterFile> toons=new ArrayList<CharacterFile>();
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(EMOTES_PREFERENCES_NAME);
    List<String> toonIds=props.getStringList(TOON_NAME_PREFERENCE);
    CharactersManager manager=CharactersManager.getInstance();
    if ((toonIds!=null) && (toonIds.size()>0))
    {
      for(String toonID : toonIds)
      {
        CharacterFile toon=manager.getToonById(toonID);
        if (toon!=null)
        {
          toons.add(toon);
        }
      }
    }
    _panelController=new EmotesSynopsisPanelController(this);
    _panelController.getTableController().setToons(toons);
  }

  /**
   * Get the window identifier.
   * @return A window identifier.
   */
  public static String getIdentifier()
  {
    return "EMOTES_SYNOPSIS";
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=_panelController.getPanel();
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String title="Emotes synopsis"; // I18n
    frame.setTitle(title);
    // Minimum size
    frame.setMinimumSize(new Dimension(500,380));
    // Default size
    frame.setSize(new Dimension(600,700));
    return frame;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    if (_panelController!=null)
    {
      Preferences preferences=Config.getInstance().getPreferences();
      TypedProperties props=preferences.getPreferences(EMOTES_PREFERENCES_NAME);
      List<String> toonIds=new ArrayList<String>();
      for(CharacterFile toon : _panelController.getTableController().getToons())
      {
        toonIds.add(toon.getIdentifier());
      }
      props.setStringList(TOON_NAME_PREFERENCE,toonIds);
      _panelController.dispose();
      _panelController=null;
    }
  }
}
