package delta.games.lotro.gui.character.status.traits.skirmish;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.traits.skirmish.SkirmishTraitsStatus;
import delta.games.lotro.character.status.traits.skirmish.io.SkirmishTraitsStatusIo;

/**
 * Controller for a window that shows the status of skirmish traits.
 * @author DAM
 */
public class SkirmishTraitsStatusWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="SKIRMISH_TRAITS_STATUS_WINDOW";

  // Controllers
  private SkirmishTraitsStatusPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Character file.
   */
  public SkirmishTraitsStatusWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    SkirmishTraitsStatus status=SkirmishTraitsStatusIo.load(toon);
    _panelController=new SkirmishTraitsStatusPanelController(this,status);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Skirmish traits Status"); // I18n
    frame.pack();
    frame.setMinimumSize(frame.getPreferredSize());
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
