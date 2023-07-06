package delta.games.lotro.gui.character.status.emotes;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.emotes.EmotesStatusManager;
import delta.games.lotro.character.status.emotes.io.EmotesStatusIo;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.emotes.EmotesManager;

/**
 * Controller for a window that shows the status of some emotes.
 * @author DAM
 */
public class EmotesStatusWindowController extends DefaultWindowController
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="EMOTES_STATUS_WINDOW";

  // Controllers
  private EmotesStatusPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Character file.
   */
  public EmotesStatusWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    EmotesStatusManager status=EmotesStatusIo.load(toon);
    List<EmoteDescription> emotes=EmotesManager.getInstance().getAll();
    _panelController=new EmotesStatusPanelController(this,emotes,status);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Emotes Status"); // I18n
    frame.pack();
    frame.setSize(frame.getWidth(),INITIAL_HEIGHT);
    frame.setMinimumSize(new Dimension(frame.getWidth(),MIN_HEIGHT));
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
