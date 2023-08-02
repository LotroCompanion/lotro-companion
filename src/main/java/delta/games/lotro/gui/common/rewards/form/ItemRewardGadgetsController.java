package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for the UI gadgets of an item reward.
 * @author DAM
 */
public class ItemRewardGadgetsController extends RewardGadgetsController
{
  private HyperLinkController _itemLink;
  private IconController _itemIcon;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param item Item to display.
   * @param count Count of that item.
   */
  public ItemRewardGadgetsController(WindowController parent, Item item, int count)
  {
    super(parent);
    // Label
    Color color=Color.WHITE;
    if (item!=null)
    {
      color=ItemUiTools.getColorForItem(item,color);
    }
    _label=new LabelWithHalo();
    _label.setOpaque(false);
    _label.setForeground(color);
    // Link
    _itemLink=ItemUiTools.buildItemLink(parent,item,_label);
    // Icon
    _itemIcon=IconControllerFactory.buildItemIcon(parent,item,count);
    _icon=_itemIcon.getIcon();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_itemLink!=null)
    {
      _itemLink.dispose();
      _itemLink=null;
    }
    if (_itemIcon!=null)
    {
      _itemIcon.dispose();
      _itemIcon=null;
    }
  }
}
