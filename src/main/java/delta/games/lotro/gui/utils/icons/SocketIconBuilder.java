package delta.games.lotro.gui.utils.icons;

import java.awt.image.BufferedImage;

import delta.common.ui.swing.icons.IconsManager;

/**
 * Builder for item icons.
 * @author DAM
 */
public class SocketIconBuilder
{
  /**
   * Get the buffered image that represents an empty socket.
   * @param socketType Socket type.
   * @return A buffered image.
   */
  public static BufferedImage getEmptySocketIcon(int socketType)
  {
    BufferedImage background=IconsManager.getImage("/sockets/background-"+socketType+".png");
    BufferedImage overlay=IconsManager.getImage("/sockets/overlay-"+socketType+".png");
    BufferedImage[] layers= {background,overlay};
    return ItemIconBuilder.buildImage(layers);
  }
}
