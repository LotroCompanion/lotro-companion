package delta.games.lotro.gui.maps.resources;

import java.awt.image.BufferedImage;

import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.MarkerIconProvider;

/**
 * Resource icons provider.
 * @author DAM
 */
public class ResourceIconProvider implements MarkerIconProvider
{
  private BufferedImage _icon;

  /**
   * Constructor.
   */
  public ResourceIconProvider()
  {
    _icon=IconsManager.getImage("/resources/gui/icons/resourcesMap-icon.png");
  }

  @Override
  public BufferedImage getImage(Marker marker)
  {
    return _icon;
  }
}
