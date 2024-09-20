package delta.games.lotro.gui.maps.basemap;

import java.awt.image.BufferedImage;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.common.ui.ImageUtils;
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
  private static final Logger LOGGER=LoggerFactory.getLogger(DatBasemapImageProvider.class);

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
    File mapImageFile=basemap.getImageFile();
    if (mapImageFile.exists())
    {
      return ImageUtils.loadImage(mapImageFile);
    }
    int imageId=basemap.getImageId();
    return loadFromLocalClient(imageId);
  }

  private BufferedImage loadFromLocalClient(int imageId)
  {
    LOGGER.debug("Loading image: "+imageId);
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
