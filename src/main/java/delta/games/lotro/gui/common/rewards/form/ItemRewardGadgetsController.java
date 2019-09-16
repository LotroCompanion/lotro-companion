package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.gui.items.ItemUiTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;

/**
 * Controller for the UI gadgets of an item reward.
 * @author DAM
 */
public class ItemRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param item Item to display.
   * @param count Count of that item.
   */
  public ItemRewardGadgetsController(Item item, int count)
  {
    // Label
    String text="???";
    Color color=Color.WHITE;
    if (item!=null)
    {
      text=item.getName();
      ItemQuality quality=item.getQuality();
      if (quality!=null)
      {
        color=getColorFromQuality(quality);
      }
    }
    //_label=GuiFactory.buildLabel(text);
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    Icon icon=ItemUiTools.buildItemIcon(item,count);
    _labelIcon=GuiFactory.buildIconLabel(icon);
    _labelIcon.setSize(icon.getIconWidth(),icon.getIconHeight());
  }

  private Color getColorFromQuality(ItemQuality quality)
  {
    Color ret=Color.WHITE;
    if (quality==ItemQuality.LEGENDARY) ret=new Color(219,175,86); // Gold
    if (quality==ItemQuality.INCOMPARABLE) ret=new Color(40,203,210); // Teal
    if (quality==ItemQuality.RARE) ret=new Color(255,114,255); // Mauve (Pink)
    if (quality==ItemQuality.UNCOMMON) ret=new Color(241,238,123); // Yellow
    if (quality==ItemQuality.COMMON) ret=Color.WHITE; // White
    return ret;
  }
}
