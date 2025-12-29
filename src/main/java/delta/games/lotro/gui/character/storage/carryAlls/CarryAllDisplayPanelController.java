package delta.games.lotro.gui.character.storage.carryAlls;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.carryAlls.CarryAllInstance;
import delta.games.lotro.gui.character.storage.StorageUiUtils;
import delta.games.lotro.gui.common.status.StatusMetadataPanelController;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.carryalls.CarryAll;
import delta.games.lotro.lore.items.carryalls.CarryAllFilter;
import delta.games.lotro.lore.items.comparators.CountedItemNameComparator;

/**
 * Controller for a panel to display a carry-all.
 * @author DAM
 */
public class CarryAllDisplayPanelController implements FilterUpdateListener
{
  // Data
  private CarryAllInstance _carryAllInstance;
  private CarryAllFilter _filter;
  private List<Item> _items;
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private StatusMetadataPanelController _status;
  private CarryAllFilterController _filterUI;
  // Elements
  private List<IconController> _iconControllers;
  private List<JLabel> _labels;
  private List<JProgressBar> _progressBars;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param carryAllInstance Carry-all.
   */
  public CarryAllDisplayPanelController(WindowController parent, CarryAllInstance carryAllInstance)
  {
    _parent=parent;
    _carryAllInstance=carryAllInstance;
    _items=new ArrayList<Item>();
    _iconControllers=new ArrayList<IconController>();
    _labels=new ArrayList<JLabel>();
    _progressBars=new ArrayList<JProgressBar>();
    _filter=new CarryAllFilter();
    _filterUI=new CarryAllFilterController(_filter);
    _filterUI.setFilterUpdateListener(this);
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
    int y=0;
    // Name
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,0,0),0,0);
    JLabel label=GuiFactory.buildLabel("Name: "+_carryAllInstance.getEffectiveName());
    ret.add(label,c);
    y++;
    // Status date
    _status=new StatusMetadataPanelController();
    _status.setData(_carryAllInstance.getStatusMetadata());
    c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_status.getPanel(),c);
    y++;
    // Capacity
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JPanel capacityPanel=buildCapacityPanel();
    ret.add(capacityPanel,c);
    y++;
    // Filter
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JPanel filterPanel=_filterUI.getPanel();
    ret.add(filterPanel,c);
    y++;
    // Contents
    JPanel itemsPanel=buildItemsPanel();
    c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    JScrollPane scrollPane=GuiFactory.buildScrollPane(itemsPanel);
    ret.add(scrollPane,c);
    return ret;
  }

  private JPanel buildCapacityPanel()
  {
    CarryAll carryAll=_carryAllInstance.getReference();
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Label
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel(Labels.getLabel("carryAll.field.capacity")),c);
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
      _items.add(item);
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
      _labels.add(nameLabel);
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(top,2,bottom,2),0,0);
      ret.add(nameLabel,c);
      // Count
      CarryAll carryAll=_carryAllInstance.getReference();
      int maxStack=carryAll.getItemStackMax();
      JProgressBar progressBar=buildProgressBar(count,maxStack);
      _progressBars.add(progressBar);
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(top,2,bottom,2),0,0);
      ret.add(progressBar,c);
      y++;
    }
    GridBagConstraints c=new GridBagConstraints(0,y,3,1,1.0,1.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(Box.createGlue(),c);
    return ret;
  }

  public void filterUpdated()
  {
    int nbElements=_items.size();
    for(int i=0;i<nbElements;i++)
    {
      Item item=_items.get(i);
      boolean visible=_filter.accept(item);
      IconController iconCtrl=_iconControllers.get(i);
      iconCtrl.getIcon().setVisible(visible);
      _labels.get(i).setVisible(visible);
      _progressBars.get(i).setVisible(visible);
    }
    if (_panel!=null)
    {
      _panel.revalidate();
      _panel.repaint();
    }
  }

  /**
   * Release all resources.
   */
  public void dispose()
  {
    // Data
    _carryAllInstance=null;
    _filter=null;
    _items=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    _parent=null;
    if (_status!=null)
    {
      _status.dispose();
      _status=null;
    }
    if (_filterUI!=null)
    {
      _filterUI.dispose();
      _filterUI=null;
    }
    if (_iconControllers!=null)
    {
      for(IconController ctrl : _iconControllers)
      {
        ctrl.dispose();
      }
      _iconControllers=null;
    }
    _labels=null;
    _progressBars=null;
  }
}
