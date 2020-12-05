package delta.games.lotro.gui.maps;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.trade.barter.BarterNpc;
import delta.games.lotro.lore.trade.barter.BarterersManager;
import delta.games.lotro.lore.trade.vendor.VendorNpc;
import delta.games.lotro.lore.trade.vendor.VendorsManager;
import delta.games.lotro.maps.data.MapPoint;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.controllers.SelectionListener;

/**
 * Listener for marker selection.
 * Shows available form for the selected marker, if any.
 * @author DAM
 */
public class MarkerSelectionListener implements SelectionListener
{
  private WindowController _parent;
  private WindowsManager _formWindows;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public MarkerSelectionListener(WindowController parent)
  {
    _parent=parent;
    _formWindows=new WindowsManager();
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
    if ((categoryCode==38) || (categoryCode==33)|| (categoryCode==42))
    {
      ref=getVendorRef(did);
    }
    else if ((categoryCode==58) || (categoryCode==61))
    {
      ref=getBartererRef(did);
    }
    else if ((categoryCode==72) || (categoryCode==73))
    {
      ref=getItemRef(did);
    }
    if (ref!=null)
    {
      int id=_formWindows.getAll().size();
      NavigatorWindowController window=NavigatorFactory.buildNavigator(_parent,id);
      window.navigateTo(ref);
      window.show(false);
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
}
