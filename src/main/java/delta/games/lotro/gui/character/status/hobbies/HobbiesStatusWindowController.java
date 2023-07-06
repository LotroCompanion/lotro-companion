package delta.games.lotro.gui.character.status.hobbies;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.hobbies.HobbiesStatusManager;
import delta.games.lotro.character.status.hobbies.io.HobbiesStatusIo;

/**
 * Controller for a window that shows the status of some hobbies.
 * @author DAM
 */
public class HobbiesStatusWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="HOBBIES_STATUS_WINDOW";

  // Controllers
  private HobbiesStatusPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Character file.
   */
  public HobbiesStatusWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    HobbiesStatusManager status=HobbiesStatusIo.load(toon);
    _panelController=new HobbiesStatusPanelController(this,status);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Hobbies Status"); // I18n
    frame.pack();
    frame.setResizable(false);
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

  @Override
  protected JPanel buildContents()
  {
    return _panelController.getPanel();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
