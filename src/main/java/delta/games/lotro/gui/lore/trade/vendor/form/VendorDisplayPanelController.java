package delta.games.lotro.gui.lore.trade.vendor.form;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.utils.l10n.L10n;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.agents.npcs.NpcDescription;
import delta.games.lotro.lore.trade.vendor.ValuedItem;
import delta.games.lotro.lore.trade.vendor.VendorNpc;

/**
 * Controller for a vendor display panel.
 * @author DAM
 */
public class VendorDisplayPanelController implements NavigablePanelController
{
  // Data
  private VendorNpc _vendor;
  // Controllers
  private NavigatorWindowController _parent;
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
  public VendorDisplayPanelController(NavigatorWindowController parent, VendorNpc vendor)
  {
    _parent=parent;
    _vendor=vendor;
  }

  @Override
  public String getTitle()
  {
    return "Vendor: "+_vendor.getNpc().getName();
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

    // Items to sell table
    initVendorEntriesTable();
    // Scroll
    JScrollPane itemsPane=GuiFactory.buildScrollPane(_itemsToSell.getTable());
    itemsPane.setBorder(GuiFactory.buildTitledBorder("Items to sell"));
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(itemsPane,c);
    c.gridy++;

    // Preferred size
    panel.setPreferredSize(new Dimension(600,500));
    _panel=panel;
    setVendor();
    return _panel;
  }

  private void initVendorEntriesTable()
  {
    TypedProperties prefs=null;
    if (_parent!=null)
    {
      prefs=_parent.getUserProperties("VendorDisplay");
    }
    final List<ValuedItem> items=_vendor.getItemsList();
    _itemsToSell=new SellItemsTableController(prefs,null,items);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          ValuedItem entry=(ValuedItem)event.getSource();
          int index=items.indexOf(entry);
          showVendorEntry(entry,index);
        }
      }
    };
    _itemsToSell.addActionListener(al);
  }

  private void showVendorEntry(ValuedItem barterEntry, int index)
  {
    PageIdentifier ref=ReferenceConstants.getVendorEntryReference(_vendor.getIdentifier(),index);
    _parent.navigateTo(ref);
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
    String sellFactorStr=L10n.getString(sellFactor,1);
    _factor.setText(sellFactorStr);
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
