package delta.games.lotro.gui.character.status.housing.map;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import delta.games.lotro.gui.utils.icons.ItemIconBuilder;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.MarkerIconProvider;

/**
 * Item icons provider.
 * @author DAM
 */
public class ItemIconProvider implements MarkerIconProvider
{
  private ItemIconBuilder _builder;
  private Map<Integer,BufferedImage> _icons;

  /**
   * Constructor.
   */
  public ItemIconProvider()
  {
    _builder=new ItemIconBuilder();
    _icons=new HashMap<Integer,BufferedImage>();
  }

  @Override
  public BufferedImage getImage(Marker marker)
  {
    Integer did=Integer.valueOf(marker.getDid());
    BufferedImage icon=_icons.get(did);
    if (icon==null)
    {
      Item item=ItemsManager.getInstance().getItem(did.intValue());
      if (item!=null)
      {
        icon=_builder.getItemIcon(item.getIcon());
      }
      _icons.put(did,icon);
    }
    return icon;
  }
}
