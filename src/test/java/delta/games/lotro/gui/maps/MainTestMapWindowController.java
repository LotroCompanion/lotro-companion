package delta.games.lotro.gui.maps;

import java.io.File;

import delta.games.lotro.gui.maps.global.MapWindowController;
import delta.games.lotro.maps.data.MapsManager;

/**
 * Simple test to show the maps explorer window.
 * @author DAM
 */
public class MainTestMapWindowController
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    File rootDir=new File("../lotro-maps-db");
    MapsManager mapsManager=new MapsManager(rootDir);
    MapWindowController mapWindow=new MapWindowController(mapsManager);
    mapWindow.show();
  }
}
