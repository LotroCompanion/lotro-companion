package delta.games.lotro.gui.utils;

import javax.swing.Icon;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.legendary.relics.RelicUiTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Factory of icon controllers.
 * @author DAM
 */
public class IconControllerFactory
{
  /**
   * Build an item icon.
   * @param parent Parent window.
   * @param item Item to use.
   * @param count Count.
   * @return A new controller.
   */
  public static IconController buildItemIcon(WindowController parent, Item item, int count)
  {
    IconController ret=new IconController(parent);
    if (item!=null)
    {
      updateItemIcon(ret,item,count);
    }
    else
    {
      ret.clear();
    }
    return ret;
  }

  /**
   * Update an item icon.
   * @param iconController Targeted controller.
   * @param item Item to show.
   * @param count Count.
   */
  public static void updateItemIcon(IconController iconController, Item item, int count)
  {
    Icon icon=ItemUiTools.buildItemIcon(item,count);
    iconController.setIcon(icon);
    iconController.setPageId(ReferenceConstants.getItemReference(item.getIdentifier()));
    if (item!=null)
    {
      iconController.setTooltipText(item.getName());
    }
  }

  /**
   * Build a relic icon.
   * @param parent Parent window.
   * @param relic Relic to use.
   * @param count Count.
   * @return A new controller.
   */
  public static IconController buildRelicIcon(WindowController parent, Relic relic, int count)
  {
    IconController ret=new IconController(parent);
    Icon icon=RelicUiTools.buildRelicIcon(relic,count);
    ret.setIcon(icon);
    ret.setPageId(ReferenceConstants.getRelicReference(relic.getIdentifier()));
    if (relic!=null)
    {
      ret.setTooltipText(relic.getName());
    }
    return ret;
  }
}
