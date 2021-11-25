package delta.games.lotro.gui.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for an icon that brings a item instance display window.
 * @author DAM
 */
public class ItemInstanceIconController extends AbstractIconController
{
  /**
   * Constructor.
   * @param parent Parent window.
   * @param itemInstance Instance to show.
   * @param count Items count.
   */
  public ItemInstanceIconController(WindowController parent, final ItemInstance<? extends Item> itemInstance, int count)
  {
    super(parent);
    Item item=itemInstance.getItem();
    Icon icon=ItemUiTools.buildItemIcon(item,count);
    setIcon(icon);
    String tooltip=ItemUiTools.buildItemTooltip(itemInstance,true);
    setTooltipText(tooltip);
    _listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        ItemUiTools.showItemInstanceWindow(_parent,itemInstance);
      }
    };
    _icon.addActionListener(_listener);
  }
}
