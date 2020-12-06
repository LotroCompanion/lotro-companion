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

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.maps.MarkerSelectionListener;
import delta.games.lotro.lore.instances.InstanceMapDescription;
import delta.games.lotro.lore.instances.PrivateEncounter;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.maps.ui.selection.SelectionManager;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a window to show the maps of an instance.
 * @author DAM
 */
public class InstanceMapsWindowController extends DefaultWindowController
{
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
    // TODO Reuse the global one
    _facade=new DataFacade();
    _panels=new ArrayList<InstanceMapPanelController>();
  }

  @Override
  protected JComponent buildContents()
  {
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    GeoreferencedBasemapsManager basemapsMgr=Maps.getMaps().getMapsManager().getBasemapsManager();
    JLayeredPane mapPanel=null;
    for(InstanceMapDescription map : _pe.getMapDescriptions())
    {
      // Build map panel
      Integer mapId=map.getMapId();
      InstanceMapPanelController ctrl=new InstanceMapPanelController(_facade,_pe,map);
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
        GeoreferencedBasemap basemap=basemapsMgr.getMapById(mapId.intValue());
        title=basemap.getName();
      }
      else
      {
        title="Landscape";
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
    String title="Instance maps: "+_pe.getName();
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
