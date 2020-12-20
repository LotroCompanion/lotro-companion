package delta.games.lotro.gui.items.containers.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.comparators.ItemNameComparator;
import delta.games.lotro.lore.items.containers.ContainerInspector;

/**
 * Controller for a panel to display the contents of a container.
 * @author DAM
 */
public class ContainerDisplayPanelController
{
  // Data
  private Item _item;
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;
  private List<ItemDisplayGadgets> _itemIcons;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param item Item to show.
   */
  public ContainerDisplayPanelController(NavigatorWindowController parent, Item item)
  {
    _parent=parent;
    _item=item;
    _itemIcons=new ArrayList<ItemDisplayGadgets>();
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
    List<ItemDisplayGadgets> ingredientsGadgets=initItemsGadgets();
    if (ingredientsGadgets.size()==0)
    {
      return null;
    }
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(ItemDisplayGadgets ingredientsGadget : ingredientsGadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      // Icon
      panel.add(ingredientsGadget.getIcon(),c);
      // Name
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(ingredientsGadget.getName(),c);
      y++;
    }
    return panel;
  }

  private List<ItemDisplayGadgets> initItemsGadgets()
  {
    List<ItemDisplayGadgets> ret=new ArrayList<ItemDisplayGadgets>();
    List<Item> items=ContainerInspector.getContainerContents(_item);
    Collections.sort(items,new ItemNameComparator());
    for(Item item : items)
    {
      int itemId=item.getIdentifier();
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,itemId,1,"");
      ret.add(gadgets);
      _itemIcons.add(gadgets);
    }
    return ret;
  }

  
  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _item=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    _parent=null;
    if (_itemIcons!=null)
    {
      for(ItemDisplayGadgets itemIcon : _itemIcons)
      {
        itemIcon.dispose();
      }
      _itemIcons.clear();
      _itemIcons=null;
    }
  }
}
