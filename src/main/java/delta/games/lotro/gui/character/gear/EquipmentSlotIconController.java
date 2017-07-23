package delta.games.lotro.gui.character.gear;

import javax.swing.ImageIcon;

import delta.common.ui.swing.icons.IconsManager;
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemPropertyNames;

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
   * Set the associated item.
   * @param item Item to set (may be <code>null</code>).
   */
  public void setItem(Item item)
  {
    if (item!=null)
    {
      String iconId=item.getProperty(ItemPropertyNames.ICON_ID);
      String backgroundIconId=item.getProperty(ItemPropertyNames.BACKGROUND_ICON_ID);
      _icon=LotroIconsManager.getItemIcon(iconId,backgroundIconId);
      String dump=item.dump();
      _tooltip="<html>"+dump.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
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
    ImageIcon backgroundIcon=IconsManager.getIcon(iconPath);
    return backgroundIcon;
  }
}
