package delta.games.lotro.gui.utils.items;

import javax.swing.Icon;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.lore.items.Item;

/**
 * Icon controller for an item.
 * @author DAM
 */
public class ItemIconController extends IconController
{
  private SaveItemIconController _saveIcon;

  /**
   * Controller.
   * @param parent
   */
  public ItemIconController(WindowController parent)
  {
    super(parent);
  }

  /**
   * Set the item to show.
   * @param item Item to show.
   * @param count Items count.
   */
  public void setItem(Item item, int count)
  {
    disposeMenu();
    if (item!=null)
    {
      Icon icon=ItemUiTools.buildItemIcon(item,count);
      setIcon(icon);
      setPageId(ReferenceConstants.getItemReference(item.getIdentifier()));
      setTooltipText(item.getName());
      _saveIcon=new SaveItemIconController(item,getIcon());
    }
    else
    {
      clear(LotroIconsManager.getDefaultItemIcon());
    }
  }

  @Override
  public void clear(Icon icon)
  {
    super.clear(icon);
    disposeMenu();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    disposeMenu();
  }

  private void disposeMenu()
  {
    if (_saveIcon!=null)
    {
      _saveIcon.dispose();
      _saveIcon=null;
    }
  }
}
