package delta.games.lotro.gui.character.status.achievables;

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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.status.achievables.AchievableElementState;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.edition.AchievableGeoStatusManager;
import delta.games.lotro.character.status.achievables.edition.AchievableStatusGeoItem;
import delta.games.lotro.character.status.achievables.io.DeedsStatusIo;
import delta.games.lotro.gui.character.status.achievables.map.AchievableGeoPointsMapPanelController;
import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.lore.quests.Achievable;

/**
 * @author dm
 */
public class MainAchievablesGeoStatus
{

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    Map<Integer,MarkersMapDescriptor> pointsByMap=new HashMap<Integer,MarkersMapDescriptor>();
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Utharr");
    AchievablesStatusManager deedsStatusMgr=DeedsStatusIo.load(toon);
    List<AchievableStatus> deedStatuses=deedsStatusMgr.getAll();
    for(AchievableStatus deedStatus : deedStatuses)
    {
      Achievable achievable=deedStatus.getAchievable();
      AchievableElementState state=deedStatus.getState();
      if (state!=AchievableElementState.UNDERWAY)
      {
        continue;
      }
      AchievableGeoStatusManager geoStatusManager=new AchievableGeoStatusManager(deedStatus,null);
      List<AchievableStatusGeoItem> points=geoStatusManager.getPoints();
      List<MapDescription> maps=achievable.getMaps();
      List<MarkersMapDescriptor> otherDescriptors=new ArrayList<MarkersMapDescriptor>();
      for(AchievableStatusGeoItem point : points)
      {
        if (point.isCompleted())
        {
          continue;
        }
        MapDescription map=maps.get(point.getPoint().getMapIndex());
        Integer mapId=map.getMapId();
        MarkersMapDescriptor descriptor=null;
        if (mapId!=null)
        {
          descriptor=pointsByMap.get(mapId);
          if (descriptor==null)
          {
            descriptor=new MarkersMapDescriptor(map);
            pointsByMap.put(mapId,descriptor);
          }
        }
        else
        {
          descriptor=new MarkersMapDescriptor(map);
          otherDescriptors.add(descriptor);
        }
        descriptor.addPoint(point);
      }
    }
    List<Integer> mapIds=new ArrayList<Integer>(pointsByMap.keySet());
    Collections.sort(mapIds);

    final JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    for(Integer mapId : mapIds)
    {
      System.out.println("Map ID: "+mapId);
      MarkersMapDescriptor descriptor=pointsByMap.get(mapId);
      AchievableGeoPointsMapPanelController mapPanelCtrl=new AchievableGeoPointsMapPanelController(descriptor.getMap(),descriptor.getPoints(),false,null);
      JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
      GridBagConstraints c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(mapPanelCtrl.getMapComponent(),c);
      String title=mapPanelCtrl.getMapTitle();
      tabbedPane.add(title,panel);
    }
    DefaultDialogController ctrl=new DefaultDialogController(null)
    {
      protected JComponent buildContents()
      {
        JPanel panel=GuiFactory.buildPanel(new BorderLayout());
        // Center
        panel.add(tabbedPane,BorderLayout.CENTER);
        return panel;
      }
    };
    ctrl.show();
  }
}
