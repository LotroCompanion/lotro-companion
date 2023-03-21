package delta.games.lotro.gui.maps;

import delta.common.ui.swing.misc.Disposable;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.trade.barter.BarterNpc;
import delta.games.lotro.lore.trade.barter.BarterersManager;
import delta.games.lotro.lore.trade.vendor.VendorNpc;
import delta.games.lotro.lore.trade.vendor.VendorsManager;
import delta.games.lotro.maps.data.MapPoint;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.data.categories.CategoriesConstants;
import delta.games.lotro.maps.ui.controllers.SelectionListener;

/**
 * Listener for marker selection.
 * Shows available form for the selected marker, if any.
 * @author DAM
 */
public class MarkerSelectionListener implements SelectionListener,Disposable
{
  private WindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public MarkerSelectionListener(WindowController parent)
  {
    _parent=parent;
  }

  @Override
  public boolean handleSelection(MapPoint point)
  {
    if (point instanceof Marker)
    {
      return handleMarkerSelection((Marker)point);
    }
    return false;
  }

  private boolean handleMarkerSelection(Marker marker)
  {
    int categoryCode=marker.getCategoryCode();
    int did=marker.getDid();
    PageIdentifier ref=null;
    if ((categoryCode==CategoriesConstants.VENDOR) || (categoryCode==CategoriesConstants.CRAFTING_VENDOR)|| (categoryCode==CategoriesConstants.TRAINER))
    {
      ref=getVendorRef(did);
    }
    else if ((categoryCode==CategoriesConstants.TRADER) || (categoryCode==CategoriesConstants.FACTION_REPRESENTATIVE))
    {
      ref=getBartererRef(did);
    }
    else if ((categoryCode==CategoriesConstants.ITEM) || (categoryCode==CategoriesConstants.CONTAINER))
    {
      ref=getItemRef(did);
    }
    if (ref!=null)
    {
      NavigationUtils.navigateTo(ref,_parent);
    }
    return (ref!=null);
  }

  private PageIdentifier getVendorRef(int did)
  {
    VendorNpc vendor=VendorsManager.getInstance().getVendor(did);
    if (vendor!=null)
    {
      PageIdentifier ref=ReferenceConstants.getVendorReference(vendor.getIdentifier());
      return ref;
    }
    return null;
  }

  private PageIdentifier getBartererRef(int did)
  {
    BarterNpc barter=BarterersManager.getInstance().getBarterer(did);
    if (barter!=null)
    {
      PageIdentifier ref=ReferenceConstants.getBartererReference(barter.getIdentifier());
      return ref;
    }
    return null;
  }

  private PageIdentifier getItemRef(int did)
  {
    Item item=ItemsManager.getInstance().getItem(did);
    if (item!=null)
    {
      PageIdentifier ref=ReferenceConstants.getItemReference(item.getIdentifier());
      return ref;
    }
    return null;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
  }
}
