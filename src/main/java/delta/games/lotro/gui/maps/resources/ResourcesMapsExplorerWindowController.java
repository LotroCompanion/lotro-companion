package delta.games.lotro.gui.maps.resources;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.dat.data.DataFacade;

/**
 * Controller for a resources maps explorer window.
 * @author DAM
 */
public class ResourcesMapsExplorerWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="RESOURCES_MAPS_EXPLORER";

  private ResourcesMapsExplorerPanelController _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param facade Data facade.
   */
  public ResourcesMapsExplorerWindowController(WindowController parent, DataFacade facade)
  {
    super(parent);
    _panelController=new ResourcesMapsExplorerPanelController(this,facade);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Resources maps explorer"); // I18n
    frame.setResizable(false);
    frame.pack();
    return frame;
  }

  @Override
  public void configureWindow()
  {
    // Center on screen
    getWindow().setLocationRelativeTo(null);
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
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
