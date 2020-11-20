package delta.games.lotro.gui.character.gear;

import java.awt.Image;

import javax.swing.ImageIcon;

import delta.common.ui.swing.icons.IconsManager;
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for the icon of an equipment slot.
 * @author DAM
 */
public class EquipmentSlotIconController
{
  private static final String BACKGROUND_ICONS_SEED="/resources/gui/equipment/";
  private static final String ITEM_WITH_NO_ICON="/resources/gui/equipment/itemNoIcon.png";

  private EQUIMENT_SLOT _slot;
  private ImageIcon _icon;
  private String _tooltip;

  /**
   * Constructor.
   * @param slot Managed slot.
   */
  public EquipmentSlotIconController(EQUIMENT_SLOT slot)
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
      String text=buildItemTooltip(itemInstance);
      _tooltip="<html>"+text.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
      if (_icon==null)
      {
        _icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
      }
    }
    else
    {
      _icon=getDefaultIcon(_slot);
      _tooltip="";
    }
  }

  private String buildItemTooltip(ItemInstance<? extends Item> itemInstance)
  {
    Item item=itemInstance.getReference();
    StringBuilder sb=new StringBuilder();
    String name=item.getName();
    sb.append("Name: ").append(name);
    Integer itemLevel=itemInstance.getEffectiveItemLevel();
    if (itemLevel!=null)
    {
      sb.append(" (item level ").append(itemLevel).append(')');
    }
    sb.append(EndOfLine.NATIVE_EOL);
    BasicStatsSet stats=itemInstance.getStats();
    String[] lines=StatUtils.getStatsDisplayLines(stats);
    for(String line : lines)
    {
      sb.append(line).append(EndOfLine.NATIVE_EOL);
    }
    return sb.toString().trim();
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

  private ImageIcon getDefaultIcon(EQUIMENT_SLOT slot)
  {
    String iconPath=BACKGROUND_ICONS_SEED+slot.name()+".png";
    Image backgroundIcon=IconsManager.getImage(iconPath);
    return new ImageIcon(backgroundIcon);
  }
}
