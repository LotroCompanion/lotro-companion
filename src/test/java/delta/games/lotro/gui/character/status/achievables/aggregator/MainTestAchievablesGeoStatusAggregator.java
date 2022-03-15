package delta.games.lotro.gui.character.status.achievables.aggregator;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.io.DeedsStatusIo;
import delta.games.lotro.gui.character.status.achievables.map.AchievableGeoPointsMapPanelController;
import delta.games.lotro.lore.maps.MapDescription;

/**
 * Test class for the achievables geo status aggregator.
 * @author DAM
 */
public class MainTestAchievablesGeoStatusAggregator
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    AggregatedGeoItemsManager mgr=new AggregatedGeoItemsManager();
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Utharr");
    AchievablesStatusManager deedsStatusMgr=DeedsStatusIo.load(toon);
    List<AchievableStatus> deedStatuses=deedsStatusMgr.getAll();
    for(AchievableStatus deedStatus : deedStatuses)
    {
      mgr.addAchievableStatus(deedStatus);
    }

    List<AggregatedGeoItemsMap> maps=mgr.getMaps();
    final JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    for(AggregatedGeoItemsMap aggregatedMap : maps)
    {
      MapDescription map=aggregatedMap.getMap();
      AchievableGeoPointsMapPanelController mapPanelCtrl=new AchievableGeoPointsMapPanelController(map,aggregatedMap.getPoints(),false,null);
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
