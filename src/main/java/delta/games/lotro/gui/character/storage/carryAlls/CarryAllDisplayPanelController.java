package delta.games.lotro.gui.character.storage.carryAlls;

import java.awt.BorderLayout;
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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.character.storage.StorageUiUtils;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.carryalls.CarryAll;
import delta.games.lotro.lore.items.carryalls.CarryAllInstance;
import delta.games.lotro.lore.items.comparators.CountedItemNameComparator;

/**
 * Controller for a panel to display a carry-all.
 * @author DAM
 */
public class CarryAllDisplayPanelController
{
  // Data
  private CarryAllInstance _carryAllInstance;
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<IconController> _iconControllers;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param carryAllInstance Carry-all.
   */
  public CarryAllDisplayPanelController(WindowController parent, CarryAllInstance carryAllInstance)
  {
    _parent=parent;
    _carryAllInstance=carryAllInstance;
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
    JPanel panel=privateBuildPanel();
    JScrollPane scrollPane=GuiFactory.buildScrollPane(panel);
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    ret.add(scrollPane,BorderLayout.CENTER);
    return ret;
  }

  private JPanel privateBuildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JPanel capacityPanel=buildCapacityPanel();
    ret.add(capacityPanel,c);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    JPanel itemsPanel=buildItemsPanel();
    ret.add(itemsPanel,c);
    return ret;
  }

  private JPanel buildCapacityPanel()
  {
    CarryAll carryAll=_carryAllInstance.getReference();
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Label
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Capacity:"),c);
    // Progress bar
    int capacity=carryAll.getMaxItems();
    int used=_carryAllInstance.getItems().size();
    JProgressBar progressBar=buildProgressBar(used,capacity);
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,5),0,0);
    ret.add(progressBar,c);
    return ret;
  }

  private JProgressBar buildProgressBar(int used, int capacity)
  {
    JProgressBar progressBar=StorageUiUtils.buildProgressBar();
    StorageUiUtils.updateProgressBar(progressBar,Integer.valueOf(used),Integer.valueOf(capacity));
    return progressBar;
  }

  private JPanel buildItemsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());

    List<CountedItem<Item>> countedItems=_carryAllInstance.getItems();
    Collections.sort(countedItems,new CountedItemNameComparator<Item>());
    int y=0;
    for(CountedItem<Item> countedItem : countedItems)
    {
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
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(top,2,bottom,2),0,0);
      ret.add(nameLabel,c);
      // Count
      CarryAll carryAll=_carryAllInstance.getReference();
      int maxStack=carryAll.getItemStackMax();
      JProgressBar progressBar=buildProgressBar(count,maxStack);
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(top,2,bottom,2),0,0);
      ret.add(progressBar,c);
      y++;
    }
    if (y==0)
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
    _carryAllInstance=null;
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
