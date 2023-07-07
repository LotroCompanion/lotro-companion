package delta.games.lotro.gui.maps.instances;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.maps.MarkerSelectionListener;
import delta.games.lotro.lore.instances.InstanceMapDescription;
import delta.games.lotro.lore.instances.PrivateEncounter;
import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.selection.SelectionManager;
import delta.games.lotro.utils.ContextPropertyNames;
import delta.games.lotro.utils.dat.DatInterface;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a window to show the maps of an instance.
 * @author DAM
 */
public class InstanceMapsWindowController extends DefaultWindowController
{
  private static final Logger LOGGER=Logger.getLogger(InstanceMapsWindowController.class);

  private DataFacade _facade;
  private PrivateEncounter _pe;
  private List<InstanceMapPanelController> _panels;

  /**
   * Constructor.
   * @param pe Private encounter to use.
   */
  public InstanceMapsWindowController(PrivateEncounter pe)
  {
    _pe=pe;
    setContextProperty(ContextPropertyNames.PRIVATE_ENCOUNTER,pe);
    _facade=DatInterface.getInstance().getFacade();
    _panels=new ArrayList<InstanceMapPanelController>();
  }

  @Override
  protected JComponent buildContents()
  {
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    GeoreferencedBasemapsManager basemapsMgr=Maps.getMaps().getMapsManager().getBasemapsManager();
    JLayeredPane mapPanel=null;
    for(InstanceMapDescription instanceMap : _pe.getMapDescriptions())
    {
      MapDescription basemap=instanceMap.getMap();
      if (basemap==null)
      {
        LOGGER.warn("No basemap. Skipping this instance map!");
        continue;
      }
      // Build map panel
      Integer mapId=basemap.getMapId();
      InstanceMapPanelController ctrl=new InstanceMapPanelController(_facade,_pe,instanceMap);
      MapPanelController panelCtrl=ctrl.getMapPanelController();
      // Setup selection manager
      SelectionManager selectionMgr=panelCtrl.getSelectionManager();
      selectionMgr.addListener(new MarkerSelectionListener(this));
      JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
      mapPanel=panelCtrl.getLayers();
      GridBagConstraints c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(mapPanel,c);
      // Compute the title of the tab
      String title=null;
      if (mapId!=null)
      {
        GeoreferencedBasemap geoBasemap=basemapsMgr.getMapById(mapId.intValue());
        title=geoBasemap.getName();
      }
      else
      {
        title="Landscape"; // I18n
      }
      tabbedPane.add(title,panel);
      _panels.add(ctrl);
    }
    return tabbedPane;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String title="Instance maps: "+_pe.getName(); // I18n
    frame.setTitle(title);
    frame.pack();
    frame.setResizable(false);
    return frame;
  }

  /**
   * Get the identifier for a window.
   * @param peId Identifier of the instance to show.
   * @return An identifier.
   */
  public static String getId(int peId)
  {
    return "PE_MAPS#"+peId;
  }

  @Override
  public String getWindowIdentifier()
  {
    return getId(_pe.getIdentifier());
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    _facade=null;
    _pe=null;
    if (_panels!=null)
    {
      for(InstanceMapPanelController panel : _panels)
      {
        panel.dispose();
      }
      _panels.clear();
      _panels=null;
    }
  }
}
