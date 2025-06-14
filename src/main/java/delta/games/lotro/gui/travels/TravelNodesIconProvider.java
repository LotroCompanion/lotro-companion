package delta.games.lotro.gui.travels;

import java.awt.image.BufferedImage;

import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.MarkerIconProvider;

/**
 * Icons provider for travel nodes.
 * @author DAM
 */
public class TravelNodesIconProvider implements MarkerIconProvider
{
  private BufferedImage _capitalEmpty;
  private BufferedImage _capital;
  private BufferedImage _normalEmpty;
  private BufferedImage _normal;

  /**
   * Constructor.
   */
  public TravelNodesIconProvider()
  {
    loadImages();
  }

  private void loadImages()
  {
    _capitalEmpty=IconsManager.getImage("/misc/travelsMap/capital-empty.png");
    _capital=IconsManager.getImage("/misc/travelsMap/capital.png");
    _normalEmpty=IconsManager.getImage("/misc/travelsMap/normal-empty.png");
    _normal=IconsManager.getImage("/misc/travelsMap/normal.png");
  }

  @Override
  public BufferedImage getImage(Marker marker)
  {
    int category=marker.getCategoryCode();
    if (category==TravelNodeMarkerConstants.CAPITAL_EMPTY) return _capitalEmpty;
    if (category==TravelNodeMarkerConstants.CAPITAL) return _capital;
    if (category==TravelNodeMarkerConstants.NORMAL_EMPTY) return _normalEmpty;
    if (category==TravelNodeMarkerConstants.NORMAL) return _normal;
    return null;
  }
}
