package delta.games.lotro.gui.stats.achievables.map;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.achievables.edition.AchievableGeoStatusManager;
import delta.games.lotro.character.achievables.edition.AchievableStatusGeoItem;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a window to show the status of a geographic achievable.
 * @author DAM
 */
public class GeoAchievableMapWindowController extends DefaultDialogController
{
  private List<AchievableGeoPointsMapPanelController> _maps;
  private JTabbedPane _ui;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param mgr Geo status manager.
   */
  public GeoAchievableMapWindowController(WindowController parent, AchievableGeoStatusManager mgr)
  {
    super(parent);
    init(mgr);
  }

  private void init(AchievableGeoStatusManager mgr)
  {
    _maps=new ArrayList<AchievableGeoPointsMapPanelController>();
    List<AchievableStatusGeoItem> points=mgr.getPoints();
    Map<Integer,List<AchievableStatusGeoItem>> pointsMap=sortByMap(points);
    List<Integer> mapIds=new ArrayList<Integer>(pointsMap.keySet());
    Collections.sort(mapIds);
    for(Integer mapId : mapIds)
    {
      if (mapId.intValue()==0)
      {
        continue;
      }
      List<AchievableStatusGeoItem> mapPoints=pointsMap.get(mapId);
      AchievableGeoPointsMapPanelController panelCtrl=new AchievableGeoPointsMapPanelController(mapId.intValue(),mapPoints,mgr);
      _maps.add(panelCtrl);
    }
    _ui=buildUi();
  }

  private Map<Integer,List<AchievableStatusGeoItem>> sortByMap(List<AchievableStatusGeoItem> points)
  {
    Map<Integer,List<AchievableStatusGeoItem>> ret=new HashMap<Integer,List<AchievableStatusGeoItem>>();
    for(AchievableStatusGeoItem point : points)
    {
      Integer mapId=Integer.valueOf(point.getPoint().getMapId());
      List<AchievableStatusGeoItem> list=ret.get(mapId);
      if (list==null)
      {
        list=new ArrayList<AchievableStatusGeoItem>();
        ret.put(mapId,list);
      }
      list.add(point);
    }
    return ret;
  }

  private JTabbedPane buildUi()
  {
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    GeoreferencedBasemapsManager basemapsMgr=Maps.getMaps().getMapsManager().getBasemapsManager();
    for(AchievableGeoPointsMapPanelController mapPanelCtrl : _maps)
    {
      JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
      GridBagConstraints c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(mapPanelCtrl.getMapComponent(),c);
      int mapId=mapPanelCtrl.getMapId();
      GeoreferencedBasemap basemap=basemapsMgr.getMapById(mapId);
      String title=basemap.getName();
      tabbedPane.add(title,panel);
    }
    return tabbedPane;
  }

  /**
   * Update UI.
   */
  public void updateUi()
  {
    for(AchievableGeoPointsMapPanelController panelCtrl : _maps)
    {
      panelCtrl.updateUi();
    }
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Center
    panel.add(_ui,BorderLayout.CENTER);
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
    if (_maps!=null)
    {
      for(AchievableGeoPointsMapPanelController mapPanelCtrl : _maps)
      {
        mapPanelCtrl.dispose();
      }
      _maps.clear();
      _maps=null;
    }
    _ui=null;
  }
}
