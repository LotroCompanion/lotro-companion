package delta.games.lotro.gui.character.status.crafting.synopsis;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Controller for a "crafting synopsis" window.
 * @author DAM
 */
public class CraftingSynopsisWindowController extends DefaultWindowController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="CRAFTING_SYNOPSIS";

  private static final String CRAFTING_PREFERENCES_NAME="craftingSynopsis";
  private static final String TOON_NAME_PREFERENCE="crafting.synopsis.registered.toon";

  private CraftingSynopsisPanelController _panelController;

  /**
   * Constructor.
   */
  public CraftingSynopsisWindowController()
  {
    List<CharacterFile> toons=new ArrayList<CharacterFile>();
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(CRAFTING_PREFERENCES_NAME);
    List<String> toonIds=props.getStringList(TOON_NAME_PREFERENCE);
    CharactersManager manager=CharactersManager.getInstance();
    if (toonIds!=null)
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
    _panelController=new CraftingSynopsisPanelController(this);
    _panelController.getTableController().setToons(toons);
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
    String title="Crafting synopsis"; // I18n
    frame.setTitle(title);
    // Minimum size
    frame.setMinimumSize(new Dimension(650,400));
    // Default size
    frame.pack();
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
    return IDENTIFIER;
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
      TypedProperties props=preferences.getPreferences(CRAFTING_PREFERENCES_NAME);
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
