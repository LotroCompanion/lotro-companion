package delta.games.lotro.gui.character.storage.wallet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.wallet.Wallet;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.enums.PaperItemCategory;
import delta.games.lotro.gui.common.status.StatusMetadataPanelController;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.paper.PaperItem;
import delta.games.lotro.lore.items.paper.PaperItemsManager;
import delta.games.lotro.lore.items.paper.filters.PaperItemFilter;

/**
 * Controller for a panel to display a character wallet.
 * @author DAM
 */
public class WalletDisplayPanelController
{
  // Data
  private Wallet _wallet;
  private Wallet _sharedWallet;
  private List<PaperItem> _paperItems;
  // UI
  private JPanel _panel;
  private JPanel _elementsPanel;
  // Controllers
  private WindowController _parent;
  private StatusMetadataPanelController _status;
  private List<IconController> _iconControllers;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param wallet Own wallet.
   * @param sharedWallet Shared wallet.
   */
  public WalletDisplayPanelController(WindowController parent, Wallet wallet, Wallet sharedWallet)
  {
    _parent=parent;
    _wallet=wallet;
    _sharedWallet=sharedWallet;
    _paperItems=new ArrayList<PaperItem>();
    _paperItems.addAll(PaperItemsManager.getInstance().getAll());
    _iconControllers=new ArrayList<IconController>();
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Apply the given filter.
   * @param filter Filter to use.
   */
  public void applyFilter(PaperItemFilter filter)
  {
    _paperItems.clear();
    _paperItems.addAll(getPaperItems(filter));
    fillPanel();
    _panel.revalidate();
    _panel.repaint();
  }

  private List<PaperItem> getPaperItems(PaperItemFilter filter)
  {
    List<PaperItem> ret=new ArrayList<PaperItem>();
    PaperItemsManager paperItemsMgr=PaperItemsManager.getInstance();
    for(PaperItem paperItem : paperItemsMgr.getAll())
    {
      if (filter.accept(paperItem))
      {
        ret.add(paperItem);
      }
    }
    Collections.sort(ret,new NamedComparator());
    return ret;
  }

  private JPanel buildPanel()
  {
    // Status date
    _status=new StatusMetadataPanelController();
    _status.setData(_wallet.getStatusMetadata());
    JPanel statusPanel=_status.getPanel();
    statusPanel.setBorder(GuiFactory.buildTitledBorder("Status")); // I18n
    // Elements
    _elementsPanel=GuiFactory.buildPanel(new GridBagLayout());
    JScrollPane scrollPane=GuiFactory.buildScrollPane(_elementsPanel);

    // Assembly
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(statusPanel,c);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(scrollPane,c);

    // Set values
    fillPanel();
    return ret;
  }

  private void fillPanel()
  {
    _elementsPanel.removeAll();
    PaperItemsManager paperItemsMgr=PaperItemsManager.getInstance();
    List<PaperItemCategory> categories=paperItemsMgr.getCategories(_paperItems);
    int y=0;
    for(PaperItemCategory category : categories)
    {
      JPanel categoryPanel=buildPanelForCategory(category);
      if (categoryPanel==null)
      {
        continue;
      }
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      categoryPanel.setBorder(GuiFactory.buildTitledBorder(category.getLabel()));
      _elementsPanel.add(categoryPanel,c);
      y++;
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    _elementsPanel.add(GuiFactory.buildPanel(new BorderLayout()),c);
  }

  private JPanel buildPanelForCategory(PaperItemCategory category)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(PaperItem paperItem : _paperItems)
    {
      if (!category.equals(paperItem.getCategory()))
      {
        continue;
      }
      CountedItem<Item> countedItem=getPaperItem(paperItem);
      if (countedItem==null)
      {
        continue;
      }
      Item item=countedItem.getItem();
      int count=countedItem.getQuantity();
      if (count==0)
      {
        continue;
      }
      // Icon
      IconController iconCtrl=IconControllerFactory.buildItemIcon(_parent,item,1);
      JButton button=iconCtrl.getIcon();
      button.setBorder(BorderFactory.createEmptyBorder());
      _iconControllers.add(iconCtrl);
      int top=(y==0)?2:0;
      int bottom=2;
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(top,2,bottom,2),0,0);
      ret.add(iconCtrl.getIcon(),c);
      // Name
      String itemName=item.getName();
      JLabel nameLabel=GuiFactory.buildLabel(itemName);
      boolean shared=paperItem.isShared();
      if (shared)
      {
        nameLabel.setForeground(new Color(30,168,97));
      }
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(top,2,bottom,2),0,0);
      ret.add(nameLabel,c);
      // Count
      Integer cap=paperItem.getCap();
      String countText=String.valueOf(count);
      if (cap!=null)
      {
        countText=countText+"/"+cap.toString();
      }
      JLabel countLabel=GuiFactory.buildLabel(countText);
      countLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      if ((cap!=null) && (count==cap.intValue()))
      {
        countLabel.setForeground(Color.RED);
      }
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(top,2,bottom,2),0,0);
      ret.add(countLabel,c);
      y++;
    }
    if (y==0)
    {
      return null;
    }
    return ret;
  }

  private CountedItem<Item> getPaperItem(PaperItem paperItem)
  {
    boolean shared=paperItem.isShared();
    Wallet toUse=shared?_sharedWallet:_wallet;
    CountedItem<Item> ret=null;
    if (toUse!=null)
    {
      int id=paperItem.getIdentifier();
      ret=toUse.getById(id);
    }
    return ret;
  }

  /**
   * Release all resources.
   */
  public void dispose()
  {
    // Data
    _wallet=null;
    _paperItems=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_elementsPanel!=null)
    {
      _elementsPanel.removeAll();
      _elementsPanel=null;
    }
    // Controllers
    _parent=null;
    if (_status!=null)
    {
      _status.dispose();
      _status=null;
    }
    if (_iconControllers!=null)
    {
      for(IconController ctrl : _iconControllers)
      {
        ctrl.dispose();
      }
      _iconControllers=null;
    }
  }
}
