package delta.games.lotro.gui.maps;

import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.dat.data.PropertiesSet;
import delta.games.lotro.dat.utils.DatIconsUtils;
import delta.games.lotro.maps.ui.layers.radar.RadarImageProvider;

/**
 * Provider for radar images.
 * @author DAM
 */
public class DatRadarImageProvider implements RadarImageProvider
{
  private static final Logger LOGGER=LoggerFactory.getLogger(DatRadarImageProvider.class);

  private DataFacade _facade;

  /**
   * Constructor.
   * @param facade Data facade.
   */
  public DatRadarImageProvider(DataFacade facade)
  {
    _facade=facade;
  }

  /**
   * Get the radar image for the given landblock.
   * @param region Region.
   * @param blockX Landblock X.
   * @param blockY Landblock Y.
   * @return a buffered image or <code>null</code> if not found.
   */
  @Override
  public BufferedImage getImage(int region, int blockX, int blockY)
  {
    BufferedImage ret=null;
    try
    {
      Integer imageId=getRadarImageId(region,blockX,blockY);
      if (imageId!=null)
      {
        ret=DatIconsUtils.loadImage(_facade,imageId.intValue());
      }
    }
    catch(Exception e)
    {
      LOGGER.warn("Could not load radar image for region="+region+", bx="+blockX+", by="+blockY, e);
    }
    return ret;
  }

  private Integer getRadarImageId(int region, int blockX, int blockY)
  {
    long regionBaseDid=0x80400000L + region*0x10000;
    long did=regionBaseDid+(blockX<<8)+blockY;
    PropertiesSet props=_facade.loadProperties(did);
    if (props==null)
    {
      return null;
    }
    PropertiesSet gameMapProps=(PropertiesSet)props.getProperty("UI_Map_GameMap");
    if (gameMapProps==null)
    {
      return null;
    }
    int mapImageId=((Integer)gameMapProps.getProperty("UI_Map_MapImage")).intValue();
    return Integer.valueOf(mapImageId);
  }
}
