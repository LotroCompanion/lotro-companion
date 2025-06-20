package delta.games.lotro.gui.travels;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;

/**
 * Controller for the travels map window.
 * @author DAM
 */
public class TravelsMapWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="TRAVELS_MAP";

  private TravelsMapPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Character to use.
   */
  public TravelsMapWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    _panelController=new TravelsMapPanelController(toon);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Travels map");
    frame.setMinimumSize(new Dimension(400,300));
    frame.setSize(950,700);
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
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
