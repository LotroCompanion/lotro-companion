package delta.games.lotro.gui.items;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import delta.common.utils.NumericTools;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.utils.gui.IconWithText;
import delta.games.lotro.utils.gui.IconWithText.Position;
import delta.games.lotro.utils.gui.combobox.ComboBoxController;

/**
 * Tools related to items UI.
 * @author DAM
 */
public class ItemUiTools
{
  private static final String ESSENCE_SEED="Essence:Tier";

  /**
   * Build an icon for an item.
   * @param item Item to use.
   * @return An icon.
   */
  public static Icon buildItemIcon(Item item)
  {
    Icon ret=null;
    String iconId=item.getProperty(ItemPropertyNames.ICON_ID);
    String backgroundIconId=item.getProperty(ItemPropertyNames.BACKGROUND_ICON_ID);
    ImageIcon icon=IconsManager.getItemIcon(iconId, backgroundIconId);
    ret=icon;
    String subCategory=item.getSubCategory();
    if ((subCategory!=null) && (subCategory.startsWith(ESSENCE_SEED)))
    {
      Integer tier=NumericTools.parseInteger(subCategory.substring(ESSENCE_SEED.length()));
      if (tier!=null)
      {
        IconWithText iconWithText=new IconWithText(icon,tier.toString(),Color.WHITE);
        iconWithText.setPosition(Position.TOP_LEFT);
        ret=iconWithText;
      }
    }
    return ret;
  }

  /**
   * Build a controller for a combo box to choose an item quality.
   * @return A new controller.
   */
  public static ComboBoxController<ItemQuality> buildQualityCombo()
  {
    JComboBox combo=GuiFactory.buildComboBox();
    ComboBoxController<ItemQuality> ctrl=new ComboBoxController<ItemQuality>(combo);
    ctrl.addEmptyItem("");
    for(ItemQuality quality : ItemQuality.getAll())
    {
      ctrl.addItem(quality,quality.getMeaning());
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
