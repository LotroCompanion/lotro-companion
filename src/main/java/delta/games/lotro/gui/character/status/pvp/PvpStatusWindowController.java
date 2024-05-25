package delta.games.lotro.gui.character.status.pvp;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.pvp.PVPStatus;
import delta.games.lotro.character.pvp.io.PVPStatusIO;

/**
 * Controller for a window that shows the PVP status of a character.
 * @author DAM
 */
public class PvpStatusWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="PVP_STATUS_WINDOW";

  // Controllers
  private PvpStatusDisplayPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Character file.
   */
  public PvpStatusWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    PVPStatus status=PVPStatusIO.loadPVPStatus(toon);
    if (status==null)
    {
      status=new PVPStatus();
    }
    _panelController=new PvpStatusDisplayPanelController();
    _panelController.setData(status);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("PVP Status"); // I18n
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
    super.dispose();
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
