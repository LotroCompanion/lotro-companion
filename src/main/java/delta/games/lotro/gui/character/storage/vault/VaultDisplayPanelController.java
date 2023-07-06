package delta.games.lotro.gui.character.storage.vault;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.vaults.Chest;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.gui.character.storage.StorageUiUtils;
import delta.games.lotro.gui.common.status.StatusMetadataPanelController;
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
  private JProgressBar _capacity;
  // Controllers
  private WindowController _parent;
  private StatusMetadataPanelController _status;
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
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Status date
    _status=new StatusMetadataPanelController();
    _status.setData(_vault.getStatusMetadata());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_status.getPanel(),c);
    // Capacity
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JPanel capacityPanel=buildCapacityPanel();
    ret.add(capacityPanel,c);
    // Contents
    c=new GridBagConstraints(0,2,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    JPanel itemsPanel=buildItemsPanel();
    JScrollPane scrollPane=GuiFactory.buildScrollPane(itemsPanel);
    ret.add(scrollPane,c);
    return ret;
  }

  private JPanel buildCapacityPanel()
  {
    _capacity=StorageUiUtils.buildProgressBar();
    int used=_vault.getUsed();
    int capacity=_vault.getCapacity();
    StorageUiUtils.updateProgressBar(_capacity,Integer.valueOf(used),Integer.valueOf(capacity));
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Capacity:"),c); // I18n
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,5),0,0);
    ret.add(_capacity,c);
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
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
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
    String label="Chest: "+chestName; // I18n
    ret.setBorder(GuiFactory.buildTitledBorder(label));
    GridBagConstraints c=new GridBagConstraints(0,0,width+1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(Box.createHorizontalStrut(width*32),c);

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
      icon.setBorder(BorderFactory.createEmptyBorder());
      int x=i%width;
      int y=1+(i/width);
      int top=(y==1)?0:2;
      int left=(x==0)?5:2;
      int bottom=2;
      int right=(x==(width-1))?5:0;
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(top,left,bottom,right),0,0);
      ret.add(icon,c);
      if (i==0)
      {
        c=new GridBagConstraints(width,y,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        ret.add(Box.createGlue(),c);
      }
      i++;
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
    _capacity=null;
    // Controllers
    _parent=null;
    if (_status!=null)
    {
      _status.dispose();
      _status=null;
    }
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
