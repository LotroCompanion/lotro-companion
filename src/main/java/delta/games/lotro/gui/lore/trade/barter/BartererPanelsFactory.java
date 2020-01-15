package delta.games.lotro.gui.lore.trade.barter;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.trade.barter.form.BarterDisplayPanelController;
import delta.games.lotro.gui.lore.trade.barter.form.entry.BarterEntryDisplayPanelController;
import delta.games.lotro.lore.trade.barter.BarterEntry;
import delta.games.lotro.lore.trade.barter.BarterNpc;
import delta.games.lotro.lore.trade.barter.BarterersManager;

/**
 * Factory for barterer panels.
 * @author DAM
 */
public class BartererPanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public BartererPanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.BARTERER_PAGE))
    {
      int bartererId=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildBartererPanel(bartererId);
    }
    if (address.equals(ReferenceConstants.BARTER_ENTRY_PAGE))
    {
      int bartererId=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      int index=pageId.getIntParameter("index").intValue();
      ret=buildBarterEntryPanel(bartererId,index);
    }
    return ret;
  }

  private BarterDisplayPanelController buildBartererPanel(int bartererId)
  {
    BarterersManager barterersMgr=BarterersManager.getInstance();
    BarterNpc barterer=barterersMgr.getBarterer(bartererId);
    if (barterer!=null)
    {
      BarterDisplayPanelController panel=new BarterDisplayPanelController(_parent,barterer);
      return panel;
    }
    return null;
  }

  private BarterEntryDisplayPanelController buildBarterEntryPanel(int bartererId, int index)
  {
    BarterersManager barterersMgr=BarterersManager.getInstance();
    BarterNpc barterer=barterersMgr.getBarterer(bartererId);
    if (barterer!=null)
    {
      BarterEntry entry=barterer.getEntries().get(index);
      BarterEntryDisplayPanelController panel=new BarterEntryDisplayPanelController(_parent,entry);
      return panel;
    }
    return null;
  }
}
