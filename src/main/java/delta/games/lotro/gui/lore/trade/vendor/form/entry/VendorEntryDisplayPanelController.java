package delta.games.lotro.gui.lore.trade.vendor.form.entry;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.trade.vendor.ValuedItem;

/**
 * Controller for a panel to display a vendor entry.
 * @author DAM
 */
public class VendorEntryDisplayPanelController implements NavigablePanelController
{
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;
  private ItemDisplayGadgets _itemIcon;
  private MoneyDisplayController _money;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param entry Entry to show.
   */
  public VendorEntryDisplayPanelController(NavigatorWindowController parent, ValuedItem entry)
  {
    _parent=parent;
    _itemIcon=buildItemGadgets(entry);
    _money=new MoneyDisplayController();
    _money.setMoney(entry.getValue());
  }

  @Override
  public String getTitle()
  {
    return "Item to sell";
  }

  @Override
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Item
    JPanel toReceivePanel=buildItemPanel();
    toReceivePanel.setBorder(GuiFactory.buildTitledBorder("Item"));
    GridBagConstraints c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(toReceivePanel,c);

    // Price
    JPanel pricePanel=_money.getPanel();
    pricePanel.setBorder(GuiFactory.buildTitledBorder("Price"));
    c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(pricePanel,c);

    return panel;
  }

  private JPanel buildItemPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    // Icon
    panel.add(_itemIcon.getIcon(),c);
    // Name
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(_itemIcon.getName(),c);
    return panel;
  }

  private ItemDisplayGadgets buildItemGadgets(ValuedItem element)
  {
    int itemId=element.getId();
    ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,itemId,1,"");
    return gadgets;
  }

  @Override
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    _parent=null;
    if (_itemIcon!=null)
    {
      _itemIcon.dispose();
      _itemIcon=null;
    }
    if (_money!=null)
    {
      _money.dispose();
      _money=null;
    }
  }
}
