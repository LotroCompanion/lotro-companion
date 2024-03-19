package delta.games.lotro.gui.maps.instances;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.lore.instances.InstanceMapDescription;
import delta.games.lotro.lore.instances.PrivateEncounter;
import delta.games.lotro.lore.instances.PrivateEncountersManager;
import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.maps.ui.MapPanelController;
import delta.games.lotro.utils.maps.Maps;

/**
 * Test class for the instance map panel controller.
 * @author DAM
 */
public class MainTestInstanceMapPanelController
{
  private DataFacade _facade=new DataFacade();

  private void doIt()
  {
    /*
    doPeMap(1879389091); // Shadow-roost
    doPeMap(1879389963); // Eithel Gwaur, the Filth-well
    doPeMap(1879389964); // Gorthad Nûr, the Deep-barrow
    doPeMap(1879240540); // Public Instance: The Road to Thornhope
    doPeMap(1879198360); // Battle of the Way of Smiths
    doPeMap(1879197319); // Lost Temple
    doPeMap(1879108596); // Instance: The Forges of Khazad-Dûm
    doPeMap(1879102898); // Instance: Weapons of the Enemy
    doPeMap(1879154502); // Raid: Barad Guldur
    doPeMap(1879141205);
    doPeMap(1879196288); // Battle of the Deep-way
    doPeMap(1879196035); // Ost Dunhoth
    //doPeMap(1879264103); // Ost Dunhoth - Disease and Poison Wing
    //doPeMap(1879264104); // Ost Dunhoth - Gortheron Wing
    //doPeMap(1879264105); // Ost Dunhoth - Wound and Fear Wing
    doPeMap(1879093354); // Instance: Mordirith's Fall
    doPeMap(1879200727); // Instance: At the Stone of Erech
    doPeMap(1879185070);
    */

    PrivateEncountersManager peMgr=PrivateEncountersManager.getInstance();
    for(PrivateEncounter pe : peMgr.getPrivateEncounters())
    {
      if (doMap(pe))
      {
        doPeMap(pe.getIdentifier());
      }
    }
  }

  boolean doMap(PrivateEncounter pe)
  {
    for(InstanceMapDescription map : pe.getMapDescriptions())
    {
      Integer mapId=map.getMap().getMapId();
      if (mapId==null)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Build a window with the map(s) of a private encounter.
   * @param peId private encounter identifier.
   */
  public void doPeMap(int peId)
  {
    PrivateEncountersManager peManager=PrivateEncountersManager.getInstance();
    PrivateEncounter pe=peManager.getPrivateEncounterById(peId);
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    GeoreferencedBasemapsManager basemapsMgr=Maps.getMaps().getMapsManager().getBasemapsManager();
    JLayeredPane mapPanel=null;
    for(InstanceMapDescription instanceMap : pe.getMapDescriptions())
    {
      MapDescription map=instanceMap.getMap();
      Integer mapId=map.getMapId();
      InstanceMapPanelController ctrl=new InstanceMapPanelController(_facade,pe,instanceMap);
      MapPanelController panelCtrl=ctrl.getMapPanelController();
      JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
      mapPanel=panelCtrl.getLayers();
      GridBagConstraints c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(mapPanel,c);
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
    }
    JDialog f=new JDialog();
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    f.getContentPane().add(tabbedPane,BorderLayout.CENTER);
    f.setTitle(pe.getName());
    f.pack();
    f.setResizable(false);
    f.setModal(true);
    f.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestInstanceMapPanelController().doIt();
  }
}
