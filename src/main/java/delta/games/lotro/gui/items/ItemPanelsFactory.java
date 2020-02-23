package delta.games.lotro.gui.items;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.items.form.ItemDisplayPanelController;
import delta.games.lotro.gui.items.legendary.relics.form.RelicDisplayPanelController;
import delta.games.lotro.gui.items.sets.form.ItemsSetDisplayPanelController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;
import delta.games.lotro.lore.items.sets.ItemsSet;
import delta.games.lotro.lore.items.sets.ItemsSetsManager;

/**
 * Factory for item-related panels.
 * @author DAM
 */
public class ItemPanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public ItemPanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.ITEM_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildItemPanel(id);
    }
    else if (address.equals(ReferenceConstants.ITEMS_SET_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildItemsSetPanel(id);
    }
    else if (address.equals(ReferenceConstants.RELIC_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildRelicPanel(id);
    }
    return ret;
  }

  private ItemDisplayPanelController buildItemPanel(int itemId)
  {
    ItemsManager itemsMgr=ItemsManager.getInstance();
    Item item=itemsMgr.getItem(itemId);
    if (item!=null)
    {
      ItemDisplayPanelController itemPanel=new ItemDisplayPanelController(_parent,item);
      return itemPanel;
    }
    return null;
  }

  private ItemsSetDisplayPanelController buildItemsSetPanel(int itemsSetId)
  {
    ItemsSetsManager itemsSetsMgr=ItemsSetsManager.getInstance();
    ItemsSet set=itemsSetsMgr.getSetById(itemsSetId);
    if (set!=null)
    {
      ItemsSetDisplayPanelController itemsSetPanel=new ItemsSetDisplayPanelController(_parent,set);
      return itemsSetPanel;
    }
    return null;
  }

  private RelicDisplayPanelController buildRelicPanel(int relicId)
  {
    RelicsManager relicsMgr=RelicsManager.getInstance();
    Relic relic=relicsMgr.getById(relicId);
    if (relic!=null)
    {
      RelicDisplayPanelController relicPanel=new RelicDisplayPanelController(_parent,relic);
      return relicPanel;
    }
    return null;
  }
}
