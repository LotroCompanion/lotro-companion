package delta.games.lotro.gui.stats.deeds.map;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.MapCanvas;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.layers.MarkersLayer;
import delta.games.lotro.maps.ui.layers.SimpleMarkersProvider;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a window to show the status of a geographic deed.
 * @author DAM
 */
public class GeoDeedMapWindowController extends DefaultDialogController
{
  private MapsManager _mapsManager;
  private MapPanelController _panel;
  private CompletedOrNotMarkerIconProvider _iconProvider;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param mapKey Key of map to show.
   * @param markers Markers to show.
   */
  public GeoDeedMapWindowController(WindowController parent, int mapKey, List<Marker> markers)
  {
    super(parent);
    _mapsManager=Maps.getMaps().getMapsManager();
    initMapPanel(mapKey,markers);
  }

  private void initMapPanel(int mapKey, List<Marker> markers)
  {
    // Build and configure map panel
    _panel=new MapPanelController(_mapsManager);
    _panel.setMap(mapKey);
    MapCanvas canvas=_panel.getCanvas();
    _iconProvider=new CompletedOrNotMarkerIconProvider();
    SimpleMarkersProvider markersProvider=new SimpleMarkersProvider();
    markersProvider.setMarkers(markers);
    MarkersLayer custom=new MarkersLayer(_iconProvider,markersProvider);
    canvas.addLayer(custom);
  }

  /**
   * Set the status of a single point.
   * @param pointId Point identifier.
   * @param completed Indicates if this point is completed or not.
   */
  public void setPointStatus(int pointId, boolean completed)
  {
    _iconProvider.setCompleted(pointId,completed);
    _panel.getCanvas().repaint();
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Center
    JLayeredPane layers=_panel.getLayers();
    panel.add(layers,BorderLayout.CENTER);
    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    // Title
    dialog.setTitle("Map");
    dialog.pack();
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return "GEODEED MAP";
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    _mapsManager=null;
    if (_panel!=null)
    {
      _panel.dispose();
      _panel=null;
    }
    _iconProvider=null;
  }
}
