package delta.games.lotro.gui.lore.items.containers.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.items.Container;
import delta.games.lotro.lore.items.ContainersManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.comparators.ItemNameComparator;
import delta.games.lotro.lore.items.containers.ContainerBindingPolicy;
import delta.games.lotro.lore.items.containers.ContainerInspector;
import delta.games.lotro.lore.items.containers.ContainerOpenPolicy;
import delta.games.lotro.lore.items.containers.ItemsContainer;
import delta.games.lotro.lore.items.legendary.relics.Relic;

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
    List<ItemDisplayGadgets> allItemGadgets=initItemsGadgets();
    if (allItemGadgets.size()==0)
    {
      return null;
    }
    JPanel attributesPanel=buildAttributesPanel();
    JPanel itemsPanel=buildItemsPanel(allItemGadgets);
    // Assembly
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    if (attributesPanel!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      ret.add(attributesPanel,c);
    }
    GridBagConstraints c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,0,0,0),0,0);
    ret.add(itemsPanel,c);
    return ret;
  }

  private JPanel buildItemsPanel(List<ItemDisplayGadgets> allItemGadgets)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(ItemDisplayGadgets itemGadgets : allItemGadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      // Icon
      panel.add(itemGadgets.getIcon(),c);
      // Name
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(itemGadgets.getName(),c);
      y++;
    }
    return panel;
  }

  private JPanel buildAttributesPanel()
  {
    JPanel ret=null;
    Container container=ContainersManager.getInstance().getContainerById(_item.getIdentifier());
    if (container instanceof ItemsContainer)
    {
      ItemsContainer itemsContainer=(ItemsContainer)container;
      ContainerBindingPolicy bindingPolicy=itemsContainer.getBindingPolicy();
      ret=GuiFactory.buildPanel(new GridBagLayout());
      int y=0;
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,0,5),0,0);
      if (bindingPolicy!=ContainerBindingPolicy.NONE)
      {
        JLabel l=GuiFactory.buildLabel(buildBindingLabel(bindingPolicy));
        ret.add(l,c);
        c.gridy++;
      }
      ContainerOpenPolicy openPolicy=itemsContainer.getOpenPolicy();
      {
        JLabel l=GuiFactory.buildLabel(buildOpenPolicyLabel(openPolicy));
        ret.add(l,c);
        c.gridy++;
      }
      boolean useScalingOnCharacter=itemsContainer.isUseCharacterForMunging();
      if (useScalingOnCharacter)
      {
        JLabel l=GuiFactory.buildLabel("Contents scales using character level");
        ret.add(l,c);
        c.gridy++;
      }
    }
    return ret;
  }

  private String buildBindingLabel(ContainerBindingPolicy bindingPolicy)
  {
    String value="?";
    if (bindingPolicy==ContainerBindingPolicy.BIND_ON_ACCOUNT)
    {
      value="on account";
    }
    else if (bindingPolicy==ContainerBindingPolicy.BIND_ON_CHARACTER)
    {
      value="on character";
    }
    return "Contents binding: "+value;
  }

  private String buildOpenPolicyLabel(ContainerOpenPolicy openPolicy)
  {
    String value="?";
    if (openPolicy==ContainerOpenPolicy.USER_SELECTION)
    {
      value="user selection";
    }
    else if (openPolicy==ContainerOpenPolicy.AUTOMATIC)
    {
      value="deliver item(s)";
    }
    return "On open: "+value;
  }

  private List<ItemDisplayGadgets> initItemsGadgets()
  {
    List<ItemDisplayGadgets> ret=new ArrayList<ItemDisplayGadgets>();
    // Items
    List<Item> items=ContainerInspector.getContainerContents(_item);
    Collections.sort(items,new ItemNameComparator());
    for(Item item : items)
    {
      int itemId=item.getIdentifier();
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,itemId,1,"");
      ret.add(gadgets);
      _itemIcons.add(gadgets);
    }
    // Relics
    List<Relic> relics=ContainerInspector.getContainerRelics(_item);
    Collections.sort(relics,new NamedComparator());
    for(Relic relic : relics)
    {
      int relicId=relic.getIdentifier();
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,relicId,1);
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
