package delta.games.lotro.gui.character.storage.bags;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.bags.BagsManager;
import delta.games.lotro.character.storage.bags.SingleBagSetup;
import delta.games.lotro.gui.common.status.StatusMetadataPanelController;
import delta.games.lotro.gui.utils.ItemInstanceIconController;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for a panel to display a single bag.
 * @author DAM
 */
public class BagDisplayPanelController
{
  private static final int DEFAULT_WIDTH=10;

  // Data
  private SingleBagSetup _setup;
  private BagsManager _bagsMgr;
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private StatusMetadataPanelController _status;
  private List<ItemInstanceIconController> _iconControllers;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param bagsMgr Bags manager.
   * @param bag Bag to display.
   */
  public BagDisplayPanelController(WindowController parent, BagsManager bagsMgr, SingleBagSetup bag)
  {
    _parent=parent;
    _setup=bag;
    _bagsMgr=bagsMgr;
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
    _status.setData(_bagsMgr.getStatusMetadata());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_status.getPanel(),c);
    // Contents
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    JPanel contentsPanel=buildContentsPanel();
    ret.add(contentsPanel,c);
    return ret;
  }

  private JPanel buildContentsPanel()
  {
    int width=_setup.getWidth();
    if (width==0)
    {
      width=DEFAULT_WIDTH;
    }
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());

    int size=_setup.getSize();
    for(int i=0;i<size;i++)
    {
      Component component=buildComponent(i);
      int x=i%width;
      int y=i/width;
      int top=(y==0)?5:2;
      int left=(x==0)?5:2;
      int bottom=2;
      int right=(x==(width-1))?5:0;
      GridBagConstraints c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(top,left,bottom,right),0,0);
      ret.add(component,c);
    }
    return ret;
  }

  private Component buildComponent(int index)
  {
    Component ret=buildItemComponent(index);
    if (ret==null)
    {
      ret=Box.createRigidArea(new Dimension(32,32));
    }
    return ret;
  }

  private Component buildItemComponent(int index)
  {
    Integer itemIndex=_setup.getItemIndexAt(index);
    if (itemIndex==null)
    {
      return null;
    }
    CountedItem<ItemInstance<? extends Item>> countedItemInstance=_bagsMgr.getSlotContent(itemIndex.intValue());
    if (countedItemInstance==null)
    {
      return null;
    }
    ItemInstance<? extends Item> itemInstance=countedItemInstance.getManagedItem();
    int count=countedItemInstance.getQuantity();
    ItemInstanceIconController ctrl=new ItemInstanceIconController(_parent,itemInstance,count);
    _iconControllers.add(ctrl);
    JButton icon=ctrl.getIcon();
    icon.setBorder(BorderFactory.createEmptyBorder());
    return icon;
  }
  /**
   * Release all resources.
   */
  public void dispose()
  {
    // Data
    _setup=null;
    _bagsMgr=null;
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
