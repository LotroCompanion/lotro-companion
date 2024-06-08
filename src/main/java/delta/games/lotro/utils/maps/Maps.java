package delta.games.lotro.utils.maps;

import java.io.File;

import delta.games.lotro.Config;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.config.labels.LabelsConfiguration;
import delta.games.lotro.maps.data.MapsManager;

/**
 * Entry point for maps.
 * @author DAM
 */
public class Maps
{
  private static Maps _instance=new Maps();
  private MapsManager _maps;

  private Maps()
  {
    File mapsDir=Config.getInstance().getMapsDir();
    LabelsConfiguration cfg=LotroCoreConfig.getInstance().getLabelsConfiguration();
    String localeKey=cfg.getDataLabelsKey();
    _maps=new MapsManager(mapsDir,localeKey);
  }

  /**
   * Get the maps.
   * @return the maps.
   */
  public static Maps getMaps()
  {
    return _instance;
  }

  /**
   * Get the maps manager.
   * @return the maps manager.
   */
  public MapsManager getMapsManager()
  {
    return _maps;
  }
}
