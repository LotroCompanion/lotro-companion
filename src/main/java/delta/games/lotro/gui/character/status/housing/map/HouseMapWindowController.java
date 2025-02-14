package delta.games.lotro.gui.character.status.housing.map;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.housing.HouseContents;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.utils.dat.DatInterface;

/**
 * Controller for a window to show the map of a house.
 * @author DAM
 */
public class HouseMapWindowController extends DefaultWindowController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="HOUSE-MAP";

  // Controllers
  private HouseMapPanelController _map;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param houseContents House to show.
   */
  public HouseMapWindowController(WindowController parent, HouseContents houseContents)
  {
    super(parent);
    DataFacade facade=DatInterface.getInstance().getFacade();
    _map=new HouseMapPanelController(parent,facade,houseContents);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("House Map");
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
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    panel.add(_map.getMapComponent(),BorderLayout.CENTER);
    return panel;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Controllers
    if (_map!=null)
    {
      _map.dispose();
      _map=null;
    }
  }
}
