package delta.games.lotro.gui.lore.trade.barter.form.entry;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconWithText;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.trade.barter.BarterEntry;
import delta.games.lotro.lore.trade.barter.BarterEntryElement;
import delta.games.lotro.lore.trade.barter.ItemBarterEntryElement;
import delta.games.lotro.lore.trade.barter.ReputationBarterEntryElement;
import delta.games.lotro.utils.Proxy;

/**
 * Controller for a panel to display a barter entry.
 * @author DAM
 */
public class BarterEntryDisplayPanelController implements NavigablePanelController
{
  // Data
  private BarterEntry _entry;
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;
  private List<ItemDisplayGadgets> _itemIcons;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param entry Entry to show.
   */
  public BarterEntryDisplayPanelController(NavigatorWindowController parent, BarterEntry entry)
  {
    _parent=parent;
    _entry=entry;
    _itemIcons=new ArrayList<ItemDisplayGadgets>();
  }

  @Override
  public String getTitle()
  {
    return "Barter entry";
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

    // To give
    JPanel toGivePanel=buildToGivePanel();
    toGivePanel.setBorder(GuiFactory.buildTitledBorder("To give"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(toGivePanel,c);

    // To receive
    JPanel toReceivePanel=buildToReceivePanel();
    toReceivePanel.setBorder(GuiFactory.buildTitledBorder("To receive"));
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(toReceivePanel,c);
    return panel;
  }

  private JPanel buildToGivePanel()
  {
    List<ItemDisplayGadgets> itemsToGiveGadgets=initItemsToGiveGadgets();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(ItemDisplayGadgets ingredientsGadget : itemsToGiveGadgets)
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

  private List<ItemDisplayGadgets> initItemsToGiveGadgets()
  {
    List<ItemDisplayGadgets> ret=new ArrayList<ItemDisplayGadgets>();
    List<ItemBarterEntryElement> elements=_entry.getElementsToGive();
    for(ItemBarterEntryElement element : elements)
    {
      ret.add(buildItemGadgets(element));
    }
    return ret;
  }

  private JPanel buildToReceivePanel()
  {
    BarterEntryElement toReceive=_entry.getElementToReceive();
    if (toReceive instanceof ItemBarterEntryElement)
    {
      ItemBarterEntryElement itemToReceive=(ItemBarterEntryElement)toReceive;
      return buildItemToReceivePanel(itemToReceive);
    }
    if (toReceive instanceof ReputationBarterEntryElement)
    {
      ReputationBarterEntryElement reputationToReceive=(ReputationBarterEntryElement)toReceive;
      return buildReputationToReceivePanel(reputationToReceive);
    }
    return null;
  }

  private JPanel buildItemToReceivePanel(ItemBarterEntryElement itemToReceive)
  {
    ItemDisplayGadgets gadgets=buildItemGadgets(itemToReceive);
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    // Icon
    panel.add(gadgets.getIcon(),c);
    // Name
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(gadgets.getName(),c);
    return panel;
  }

  private JPanel buildReputationToReceivePanel(ReputationBarterEntryElement reputationToReceive)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    // Icon
    int amount=reputationToReceive.getAmount();
    String iconName=(amount>0)?"reputation":"reputation-decrease";
    Icon icon=IconsManager.getIcon("/resources/gui/icons/"+iconName+".png");
    IconWithText iconWithText=new IconWithText(icon,String.valueOf(amount),Color.WHITE);
    JLabel iconLabel=GuiFactory.buildIconLabel(iconWithText);
    panel.add(iconLabel,c);
    // Name
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    Faction faction=reputationToReceive.getFaction();
    JLabel factionLabel=GuiFactory.buildLabel(faction.getName());
    panel.add(factionLabel,c);
    return panel;
  }

  private ItemDisplayGadgets buildItemGadgets(ItemBarterEntryElement element)
  {
    Proxy<Item> proxy=element.getItemProxy();
    int quantity=element.getQuantity();
    int itemId=proxy.getId();
    ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,itemId,quantity,"");
    _itemIcons.add(gadgets);
    return gadgets;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _entry=null;
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
