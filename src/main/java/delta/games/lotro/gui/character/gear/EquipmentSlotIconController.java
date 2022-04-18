package delta.games.lotro.gui.character.gear;

import java.awt.Image;

import javax.swing.ImageIcon;

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
  private ImageIcon _icon;
  private String _tooltip;

  /**
   * Constructor.
   * @param slot Managed slot.
   */
  public EquipmentSlotIconController(GearSlot slot)
  {
    _slot=slot;
    setItem(null);
  }

  /**
   * Set the associated item instance.
   * @param itemInstance Item instance to set (may be <code>null</code>).
   */
  public void setItem(ItemInstance<? extends Item> itemInstance)
  {
    if (itemInstance!=null)
    {
      Item item=itemInstance.getReference();
      String icon=item.getIcon();
      _icon=LotroIconsManager.getItemIcon(icon);
      _tooltip=ItemUiTools.buildItemTooltip(itemInstance,true);
      if (_icon==null)
      {
        _icon=LotroIconsManager.getDefaultItemIcon();
      }
    }
    else
    {
      _icon=getDefaultIcon(_slot);
      _tooltip="";
    }
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
