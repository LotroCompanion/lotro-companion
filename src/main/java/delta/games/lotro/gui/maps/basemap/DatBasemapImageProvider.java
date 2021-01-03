package delta.games.lotro.gui.maps.basemap;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;

import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.dat.utils.DatIconsUtils;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.ui.layers.basemap.BasemapImageProvider;

/**
 * Provider for radar images.
 * @author DAM
 */
public class DatBasemapImageProvider implements BasemapImageProvider
{
  private static final Logger LOGGER=Logger.getLogger(DatBasemapImageProvider.class);

  private DataFacade _facade;

  /**
   * Constructor.
   * @param facade Data facade.
   */
  public DatBasemapImageProvider(DataFacade facade)
  {
    _facade=facade;
  }

  @Override
  public BufferedImage getImage(GeoreferencedBasemap basemap)
  {
    int imageId=basemap.getImageId();
    System.out.println("Loading image: "+imageId);
    try
    {
      return DatIconsUtils.loadImage(_facade,imageId);
    }
    catch(Exception e)
    {
      LOGGER.warn("Error when loading basemap image: "+imageId);
    }
    return null;
  }
}
