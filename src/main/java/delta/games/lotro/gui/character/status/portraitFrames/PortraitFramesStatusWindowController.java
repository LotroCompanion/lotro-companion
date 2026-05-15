package delta.games.lotro.gui.character.status.portraitFrames;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.portraitFrames.PortraitFramesStatus;
import delta.games.lotro.character.status.portraitFrames.io.PortraitFramesStatusIo;

/**
 * Controller for a window that shows the status of portrait frames.
 * @author DAM
 */
public class PortraitFramesStatusWindowController extends DefaultWindowController
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="PORTRAIT_FRAMES_STATUS_WINDOW";

  // Controllers
  private PortraitFramesStatusPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Character file.
   */
  public PortraitFramesStatusWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    PortraitFramesStatus status=PortraitFramesStatusIo.load(toon);
    _panelController=new PortraitFramesStatusPanelController(this,status);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Portrait frames status"); // I18n
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
