package delta.games.lotro.gui.lore.trade.vendor.form;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemProxy;
import delta.games.lotro.lore.items.comparators.ItemNameComparator;
import delta.games.lotro.lore.npc.NpcDescription;
import delta.games.lotro.lore.trade.vendor.SellList;
import delta.games.lotro.lore.trade.vendor.ValuedItem;
import delta.games.lotro.lore.trade.vendor.VendorNpc;
import delta.games.lotro.utils.DataProvider;
import delta.games.lotro.utils.Proxy;
import delta.games.lotro.utils.comparators.DelegatingComparator;

/**
 * Controller for a vendor display panel.
 * @author DAM
 */
public class VendorDisplayPanelController
{
  private static final Logger LOGGER=Logger.getLogger(VendorDisplayPanelController.class);

  // Data
  private VendorNpc _vendor;
  // Controllers
  private WindowController _parent;
  // GUI
  private JPanel _panel;

  private JLabel _name;
  private JLabel _buys;
  private JLabel _factor;
  private SellItemsTableController _itemsToSell;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param vendor Vendor to show.
   */
  public VendorDisplayPanelController(WindowController parent, VendorNpc vendor)
  {
    _parent=parent;
    _vendor=vendor;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
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

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Name
      _name=GuiFactory.buildLabel("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(_name);
    }
    // Buys
    _buys=buildLabelLine(panel,c,"Buys items: ");
    // Sell Factor
    _factor=buildLabelLine(panel,c,"Sell factor: ");

    // Items to sell
    TypedProperties prefs=null;
    if (_parent!=null)
    {
      prefs=_parent.getUserProperties("VendorDisplay");
    }
    List<ValuedItem> items=buildItemsList();
    _itemsToSell=new SellItemsTableController(prefs,null,items);
    JScrollPane itemsPane=GuiFactory.buildScrollPane(_itemsToSell.getTable());
    itemsPane.setBorder(GuiFactory.buildTitledBorder("Items to sell"));
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(itemsPane,c);
    c.gridy++;

    _panel=panel;
    setVendor();
    return _panel;
  }

  private JLabel buildLabelLine(JPanel parent, GridBagConstraints c, String fieldName)
  {
    // Build line panel
    JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // Build field label
    panelLine.add(GuiFactory.buildLabel(fieldName));
    // Build value label
    JLabel label=GuiFactory.buildLabel("");
    panelLine.add(label);
    // Add line panel to parent
    parent.add(panelLine,c);
    c.gridy++;
    return label;
  }

  /**
   * Set the vendor to display.
   */
  private void setVendor()
  {
    NpcDescription npc=_vendor.getNpc();
    // Name & title
    String fullName=npc.getName();
    String title=npc.getTitle();
    if (title.length()>0)
    {
      fullName=fullName+" ("+title+")";
    }
    _name.setText(fullName);
    // Buys items?
    String buys=_vendor.buys()?"Yes":"No";
    _buys.setText(buys);
    // Factor
    float sellFactor=_vendor.getSellFactor();
    _factor.setText(String.format("%.1f",Float.valueOf(sellFactor)));
  }

  private List<ValuedItem> buildItemsList()
  {
    List<ValuedItem> ret=new ArrayList<ValuedItem>();
    Set<Integer> knownItemIds=new HashSet<Integer>();
    List<SellList> lists=_vendor.getSellLists();
    float factor=_vendor.getSellFactor();
    for(SellList list : lists)
    {
      for(Proxy<Item> itemProxy : list.getItems())
      {
        Item item=itemProxy.getObject();
        if (item!=null)
        {
          Integer key=Integer.valueOf(item.getIdentifier());
          if (!knownItemIds.contains(key))
          {
            Money value=item.getValueAsMoney();
            int rawValue=value.getInternalValue();
            int sellRawValue=(int)(rawValue*factor);
            Money sellValue=new Money();
            sellValue.setRawValue(sellRawValue);
            ItemProxy proxy=new ItemProxy();
            proxy.setItem(item);
            ValuedItem valuedItem=new ValuedItem(proxy,sellValue);
            ret.add(valuedItem);
            knownItemIds.add(key);
          }
        }
        else
        {
          LOGGER.warn("Could not find item: "+itemProxy);
        }
      }
    }
    // Sort by name
    ItemNameComparator nameComparator=new ItemNameComparator();
    DataProvider<ValuedItem,Item> provider=new DataProvider<ValuedItem,Item>()
    {
      @Override
      public Item getData(ValuedItem p)
      {
        return p.getItem();
      }
    };
    DelegatingComparator<ValuedItem,Item> c=new DelegatingComparator<ValuedItem,Item>(provider,nameComparator);
    Collections.sort(ret,c);
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _vendor=null;
    // Controllers
    _parent=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _name=null;
    _buys=null;
    _factor=null;
    _itemsToSell=null;
  }
}
