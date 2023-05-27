package delta.games.lotro.gui.character.gear;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import delta.common.ui.swing.icons.IconWithSmallIcon;
import delta.common.ui.swing.icons.IconsManager;
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
  private Icon _icon;
  private String _tooltip;

  /**
   * Constructor.
   * @param slot Managed slot.
   */
  public EquipmentSlotIconController(GearSlot slot)
  {
    _slot=slot;
    setItem((ItemInstance<? extends Item>)null);
  }

  /**
   * Set the associated item instance.
   * @param itemInstance Item instance to set (may be <code>null</code>).
   */
  public void setItem(ItemInstance<? extends Item> itemInstance)
  {
    Item item=(itemInstance!=null)?itemInstance.getReference():null;
    setIcon(item,null);
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
   * @param visible Indicate the "visible" icon to use (or no icon).
   */
  public void setItem(Item item, Boolean visible)
  {
    setIcon(item,visible);
    _tooltip=buildItemTooltip(item);
  }

  private void setIcon(Item item, Boolean visible)
  {
    ImageIcon baseIcon=null;
    if (item!=null)
    {
      String icon=item.getIcon();
      baseIcon=LotroIconsManager.getItemIcon(icon);
      if (baseIcon==null)
      {
        baseIcon=LotroIconsManager.getDefaultItemIcon();
      }
    }
    else
    {
      baseIcon=getDefaultIcon(_slot);
    }
    _icon=baseIcon;
    if (visible!=null)
    {
      String iconName=visible.booleanValue()?"visible":"invisible";
      Image visibilityImage=IconsManager.getImage("/resources/gui/outfits/icon_"+iconName+".png");
      IconWithSmallIcon iconWithVisibility=new IconWithSmallIcon(baseIcon,visibilityImage);
      _icon=iconWithVisibility;
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
      String name=item.getName();
      return "<html>"+name+"</html>";
    }
    return "";
  }

  /**
   * Get the icon to use.
   * @return an icon.
   */
  public Icon getIcon()
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
    String iconPath=BACKGROUND_ICONS_SEED+slot.getKey()+".png";
    Image backgroundIcon=IconsManager.getImage(iconPath);
    return new ImageIcon(backgroundIcon);
  }
}
