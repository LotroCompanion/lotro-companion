package delta.games.lotro.gui.lore.items;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.comparators.ItemNameComparator;

/**
 * Controller for a panel to display a list of items.
 * @author DAM
 */
public class ItemsListDisplayPanelController
{
  // Data
  private List<Item> _items;
  // GUI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<ItemDisplayGadgets> _itemIcons;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public ItemsListDisplayPanelController(WindowController parent)
  {
    _parent=parent;
    _items=new ArrayList<Item>();
    _itemIcons=new ArrayList<ItemDisplayGadgets>();
    _panel=GuiFactory.buildPanel(new GridBagLayout());
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Set the items to show.
   * @param items
   */
  public void setItems(List<Item> items)
  {
    disposeItems();
    _items.addAll(items);
    Collections.sort(_items,new ItemNameComparator());
    setItemsUi();
    _panel.revalidate();
    _panel.repaint();
  }

  private void setItemsUi()
  {
    List<ItemDisplayGadgets> allItemGadgets=initItemsGadgets();
    int y=0;
    for(ItemDisplayGadgets itemGadgets : allItemGadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      // Icon
      _panel.add(itemGadgets.getIcon(),c);
      // Name
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      _panel.add(itemGadgets.getName(),c);
      y++;
    }
  }

  private List<ItemDisplayGadgets> initItemsGadgets()
  {
    List<ItemDisplayGadgets> ret=new ArrayList<ItemDisplayGadgets>();
    // Items
    for(Item item : _items)
    {
      int itemId=item.getIdentifier();
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,itemId,1,"");
      ret.add(gadgets);
      _itemIcons.add(gadgets);
    }
    return ret;
  }

  private void disposeItems()
  {
    _panel.removeAll();
    for(ItemDisplayGadgets itemIcon : _itemIcons)
    {
      itemIcon.dispose();
    }
    _itemIcons.clear();
    _items.clear();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _items=null;
    // Controllers
    _parent=null;
    if (_itemIcons!=null)
    {
      disposeItems();
      _itemIcons=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
