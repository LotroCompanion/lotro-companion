package delta.games.lotro.gui.items.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.common.treasure.TrophyList;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.DisenchantmentResult;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a panel to display the disenchantment result of an item.
 * @author DAM
 */
public class DisenchantmentResultPanelController
{
  // Controllers
  private NavigatorWindowController _parent;
  private ItemDisplayGadgets _gadgets;
  // Data
  private DisenchantmentResult _result;
  // GUI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window (a navigator).
   * @param result Disenchantment result.
   */
  public DisenchantmentResultPanelController(NavigatorWindowController parent, DisenchantmentResult result)
  {
    _parent=parent;
    _result=result;
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c;
    int y=0;
    // Counted item?
    CountedItem<Item> countedItem=_result.getCountedItem();
    if (countedItem!=null)
    {
      JPanel itemPanel=buildItemToReceivePanel(countedItem);
      c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      ret.add(itemPanel,c);
      y++;
    }
    // Trophy list?
    TrophyList trophyList=_result.getTrophyList();
    if (trophyList!=null)
    {
      String description=trophyList.getDescription();
      Integer imageId=trophyList.getImageId();
      if ((description!=null) && (imageId!=null))
      {
        JPanel trophyListPanel=buildTrophyListPanel(description,imageId.intValue());
        c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
        ret.add(trophyListPanel,c);
      }
    }
    return ret;
  }

  private JPanel buildItemToReceivePanel(CountedItem<Item> countedItem)
  {
    _gadgets=buildItemGadgets(countedItem);
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    // Icon
    panel.add(_gadgets.getIcon(),c);
    // Name
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(_gadgets.getName(),c);
    return panel;
  }

  private ItemDisplayGadgets buildItemGadgets(CountedItem<Item> countedItem)
  {
    int quantity=countedItem.getQuantity();
    int itemId=countedItem.getId();
    ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,itemId,quantity,"");
    return gadgets;
  }

  private JPanel buildTrophyListPanel(String description, int imageId)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    // Icon
    Icon icon=IconsManager.getIcon("/trophyListIcons/"+imageId+".png");
    JLabel iconLabel=GuiFactory.buildIconLabel(icon);
    panel.add(iconLabel,c);
    // Description
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    JLabel label=GuiFactory.buildLabel(description);
    panel.add(label,c);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    _parent=null;
    if (_gadgets!=null)
    {
      _gadgets.dispose();
      _gadgets=null;
    }
    // Data
    _result=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
