package delta.games.lotro.gui.character.gear;

import java.awt.Image;

import javax.swing.ImageIcon;

import delta.common.ui.swing.icons.IconsManager;
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for the icon of an equipment slot.
 * @author DAM
 */
public class EquipmentSlotIconController
{
  private static final String BACKGROUND_ICONS_SEED="/resources/gui/equipment/";

  private GearSlot _slot;
  private ImageIcon _icon;
  private String _tooltip;

  /**
   * Constructor.
   * @param slot Managed slot.
   */
  public EquipmentSlotIconController(GearSlot slot)
  {
    _slot=slot;
    setItem((Item)null);
  }

  /**
   * Set the associated item instance.
   * @param itemInstance Item instance to set (may be <code>null</code>).
   */
  public void setItem(ItemInstance<? extends Item> itemInstance)
  {
    Item item=(itemInstance!=null)?itemInstance.getReference():null;
    setIcon(item);
    if (itemInstance!=null)
    {
      _tooltip=ItemUiTools.buildItemTooltip(itemInstance,true);
    }
    else
    {
      _tooltip="";
    }
  }

  /**
   * Set the associated item.
   * @param item Item to set (may be <code>null</code>).
   */
  public void setItem(Item item)
  {
    setIcon(item);
    _tooltip=buildItemTooltip(item);
  }

  private void setIcon(Item item)
  {
    if (item!=null)
    {
      String icon=item.getIcon();
      _icon=LotroIconsManager.getItemIcon(icon);
      if (_icon==null)
      {
        _icon=LotroIconsManager.getDefaultItemIcon();
      }
    }
    else
    {
      _icon=getDefaultIcon(_slot);
    }
  }

  /**
   * Build a tooltip for the given item.
   * @param item Item.
   * @return A tooltip.
   */
  private String buildItemTooltip(Item item)
  {
    if (item!=null)
    {
      StringBuilder sb=new StringBuilder();
      String name=item.getName();
      sb.append("Name: ").append(name);
      String ret=sb.toString().trim();
      ret="<html>"+ret.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
      return ret;
    }
    return "";
  }

  /**
   * Get the icon to use.
   * @return an icon.
   */
  public ImageIcon getIcon()
  {
    return _icon;
  }

  /**
   * Get the tooltip to use.
   * @return a tooltip string.
   */
  public String getTooltip()
  {
    return _tooltip;
  }

  private ImageIcon getDefaultIcon(GearSlot slot)
  {
    String iconPath=BACKGROUND_ICONS_SEED+slot.name()+".png";
    Image backgroundIcon=IconsManager.getImage(iconPath);
    return new ImageIcon(backgroundIcon);
  }
}
