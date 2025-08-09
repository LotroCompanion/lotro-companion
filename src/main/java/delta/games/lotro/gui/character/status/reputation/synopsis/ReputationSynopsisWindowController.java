package delta.games.lotro.gui.character.status.reputation.synopsis;

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
import delta.games.lotro.character.utils.CharacterUtils;

/**
 * Controller for a "reputation synopsis" window.
 * @author DAM
 */
public class ReputationSynopsisWindowController extends DefaultWindowController
{
  /**
   * Window identifier.
   */
  public static final String WINDOW_IDENTIFIER="REPUTATION_SYNOPSIS";
  private static final String REPUTATION_PREFERENCES_NAME="reputationSynopsis";
  private static final String TOON_NAME_PREFERENCE="reputation.synopsis.registered.toon";

  private ReputationSynopsisPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window controller.
   */
  public ReputationSynopsisWindowController(WindowController parent)
  {
    super(parent);
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(REPUTATION_PREFERENCES_NAME);
    List<String> toonIds=props.getStringList(TOON_NAME_PREFERENCE);
    List<CharacterFile> toons=CharacterUtils.getToons(toonIds);
    _panelController=new ReputationSynopsisPanelController(this);
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
    String title="Reputation synopsis"; // I18n
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
    return WINDOW_IDENTIFIER;
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
      TypedProperties props=preferences.getPreferences(REPUTATION_PREFERENCES_NAME);
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
