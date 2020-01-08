package delta.games.lotro.gui.items;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.items.form.ItemDisplayPanelController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Factory for item panels.
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
}
