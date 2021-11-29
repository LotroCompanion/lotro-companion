package delta.games.lotro.gui.character.storage.vault;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.vaults.Chest;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.gui.utils.ItemInstanceIconController;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for a panel to display a character wallet.
 * @author DAM
 */
public class VaultDisplayPanelController
{
  // Data
  private Vault _vault;
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<ItemInstanceIconController> _iconControllers;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param vault Wallet.
   */
  public VaultDisplayPanelController(WindowController parent, Vault vault)
  {
    _parent=parent;
    _vault=vault;
    _iconControllers=new ArrayList<ItemInstanceIconController>();
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
    List<Integer> chestIDs=_vault.getChestIds();
    int y=0;
    for(Integer chestID : chestIDs)
    {
      Chest chest=_vault.getChest(chestID.intValue());
      JPanel chestPanel=buildPanelForChest(chest);
      if (chestPanel==null)
      {
        continue;
      }
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(chestPanel,c);
      y++;
    }
    return ret;
  }

  private JPanel buildPanelForChest(Chest chest)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());

    int width=10;
    String chestName=chest.getName();
    JLabel nameLabel=GuiFactory.buildLabel("Chest: "+chestName);
    GridBagConstraints c=new GridBagConstraints(0,0,width,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    ret.add(nameLabel,c);

    int i=0;
    for(CountedItem<ItemInstance<? extends Item>> countedItem : chest.getAllItemInstances())
    {
      if (countedItem==null)
      {
        continue;
      }
      int count=countedItem.getQuantity();
      if (count==0)
      {
        continue;
      }
      ItemInstance<? extends Item> itemInstance=countedItem.getManagedItem();
      // Icon
      ItemInstanceIconController iconCtrl=new ItemInstanceIconController(_parent,itemInstance,count);
      _iconControllers.add(iconCtrl);
      JButton icon=iconCtrl.getIcon();
      int x=i%width;
      int y=1+(i/width);
      int top=(y==1)?0:2;
      int left=(x==0)?5:2;
      int bottom=2;
      int right=(x==(width-1))?5:0;
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(top,left,bottom,right),0,0);
      ret.add(icon,c);
      i++;
    }
    if (i==0)
    {
      return null;
    }
    return ret;
  }

  /**
   * Release all resources.
   */
  public void dispose()
  {
    // Data
    _vault=null;
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
      for(ItemInstanceIconController ctrl : _iconControllers)
      {
        ctrl.dispose();
      }
      _iconControllers=null;
    }
  }
}
