package delta.games.lotro.gui.character.status.birding;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.collections.birds.BirdsStatusManager;
import delta.games.lotro.character.status.collections.birds.io.BirdsStatusIo;

/**
 * Controller for a "birding status" window.
 * @author DAM
 */
public class BirdingStatusWindowController extends DefaultDisplayDialogController<Void>
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Window identifier.
   */
  public static final String WINDOW_IDENTIFIER="BIRDING_STATUS_WINDOW";
  // Controllers
  private BirdingStatusPanelController _birds;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param file Character to use.
   */
  public BirdingStatusWindowController(WindowController parent, CharacterFile file)
  {
    super(parent,null);
    BirdsStatusManager statusMgr=BirdsStatusIo.load(file);
    _birds=new BirdingStatusPanelController(this,statusMgr);
  }

  @Override
  protected JDialog build()
  {
    JDialog window=super.build();
    window.setTitle("Birds Status"); // I18n
    window.pack();
    window.setSize(window.getWidth(),INITIAL_HEIGHT);
    window.setMinimumSize(new Dimension(window.getWidth(),MIN_HEIGHT));
    return window;
  }

  @Override
  public String getWindowIdentifier()
  {
    return WINDOW_IDENTIFIER;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildFormPanel()
  {
    return _birds.getPanel();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_birds!=null)
    {
      _birds.dispose();
      _birds=null;
    }
  }
}
