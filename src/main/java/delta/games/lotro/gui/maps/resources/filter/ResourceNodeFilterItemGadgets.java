package delta.games.lotro.gui.maps.resources.filter;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.utils.ItemIconController;
import delta.games.lotro.lore.items.Item;

/**
 * Gather all the gadgets for a single resource node filter item.
 * @author DAM
 */
public class ResourceNodeFilterItemGadgets
{
  // Controllers
  private WindowController _parent;
  private CheckboxController _checkbox;
  private HyperLinkController _sourceItemLink;
  private ItemIconController _sourceItemIcon;
  private List<ItemIconController> _lootItemIcons;

  /**
   * Constructor.
   * @param parent Parent window controller.
   */
  public ResourceNodeFilterItemGadgets(WindowController parent)
  {
    _parent=parent;
    _checkbox=new CheckboxController("");
    _sourceItemLink=null;
    _sourceItemIcon=null;
    _lootItemIcons=new ArrayList<ItemIconController>();
  }

  /**
   * Set the data to show.
   * @param sourceItem Source item.
   * @param lootItems Loot items.
   */
  public void setData(Item sourceItem, List<Item> lootItems)
  {
    disposeItems();
    if (sourceItem!=null)
    {
      _sourceItemLink=ItemUiTools.buildItemLink(_parent,sourceItem);
      String icon=sourceItem.getIcon();
      if ((icon!=null) && (icon.length()>0))
      {
        _sourceItemIcon=new ItemIconController(_parent);
        _sourceItemIcon.setItem(sourceItem,1);
      }
    }
    for(Item lootItem : lootItems)
    {
      ItemIconController lootItemIcon=new ItemIconController(_parent);
      lootItemIcon.setItem(lootItem,1);
      _lootItemIcons.add(lootItemIcon);
    }
  }

  /**
   * Get the managed checkbox controller.
   * @return a checkbox controller.
   */
  public CheckboxController getCheckbox()
  {
    return _checkbox;
  }

  /**
   * Get the managed source item link.
   * @return an item link or <code>null</code> if not source item.
   */
  public HyperLinkController getSourceItemLink()
  {
    return _sourceItemLink;
  }

  /**
   * Get the managed source item icon.
   * @return an item icon or <code>null</code> if not source item.
   */
  public ItemIconController getSourceItemIcon()
  {
    return _sourceItemIcon;
  }

  /**
   * Get the list of loot item icons.
   * @return a list of item icons.
   */
  public List<ItemIconController> getLootItemIcons()
  {
    return _lootItemIcons;
  }

  private void disposeItems()
  {
    for(ItemIconController lootItemIcon : _lootItemIcons)
    {
      lootItemIcon.dispose();
    }
    _lootItemIcons.clear();
    if (_sourceItemLink!=null)
    {
      _sourceItemLink.dispose();
      _sourceItemLink=null;
    }
    if (_sourceItemIcon!=null)
    {
      _sourceItemIcon.dispose();
      _sourceItemIcon=null;
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    if (_checkbox!=null)
    {
      _checkbox.dispose();
      _checkbox=null;
    }
    disposeItems();
    _lootItemIcons=null;
  }
}
