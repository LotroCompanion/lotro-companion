package delta.games.lotro.gui.character.storage.wallet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.wallet.Wallet;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.paper.PaperItem;
import delta.games.lotro.lore.items.paper.PaperItemsManager;

/**
 * Controller for a panel to display a character wallet.
 * @author DAM
 */
public class WalletDisplayPanelController
{
  // Data
  private Wallet _wallet;
  private Wallet _sharedWallet;
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
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

  private JPanel buildPanel()
  {
    JPanel itemsPanel=buildItemsPanel();
    JScrollPane scrollPane=GuiFactory.buildScrollPane(itemsPanel);
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    ret.add(scrollPane,BorderLayout.CENTER);
    return ret;
  }

  private JPanel buildItemsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());

    PaperItemsManager paperItemsMgr=PaperItemsManager.getInstance();
    List<String> categories=paperItemsMgr.getCategories();
    int y=0;
    for(String category : categories)
    {
      JPanel categoryPanel=buildPanelForCategory(category);
      if (categoryPanel==null)
      {
        continue;
      }
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      categoryPanel.setBorder(GuiFactory.buildTitledBorder(category));
      ret.add(categoryPanel,c);
      y++;
    }
    return ret;
  }

  private JPanel buildPanelForCategory(String category)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    PaperItemsManager paperItemsMgr=PaperItemsManager.getInstance();
    List<PaperItem> paperItems=paperItemsMgr.getAllForCategory(category);
    int y=0;
    for(PaperItem paperItem : paperItems)
    {
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
      _iconControllers.add(iconCtrl);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      ret.add(iconCtrl.getIcon(),c);
      // Name
      String itemName=item.getName();
      JLabel nameLabel=GuiFactory.buildLabel(itemName);
      boolean shared=paperItem.isShared();
      if (shared)
      {
        nameLabel.setForeground(new Color(30,168,97));
      }
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      ret.add(nameLabel,c);
      // Count
      Integer cap=paperItem.getCap();
      String countText=String.valueOf(count);
      if (cap!=null)
      {
        countText=countText+"/"+cap.toString();
      }
      JLabel countLabel=GuiFactory.buildLabel(countText);
      if ((cap!=null) && (count==cap.intValue()))
      {
        countLabel.setForeground(Color.RED);
      }
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
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
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    _parent=null;
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
