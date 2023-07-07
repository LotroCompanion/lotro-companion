package delta.games.lotro.gui.maps.resources.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.maps.resources.ResourceNodesLootManager;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a panel to control the resource nodes filter.
 * @author DAM
 */
public class ResourceNodeFilterPanelController
{
  // Controllers
  private WindowController _parent;
  private List<ResourceNodeFilterItemGadgets> _gadgets;
  // UI
  private JPanel _panel;
  // Data
  private ResourceNodesFilter _filter;

  /**
   * Constructor.
   * @param parent Parent window controller.
   */
  public ResourceNodeFilterPanelController(WindowController parent)
  {
    _parent=parent;
    _panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    _gadgets=new ArrayList<ResourceNodeFilterItemGadgets>();
    _filter=new ResourceNodesFilter();
  }

  /**
   * Get the resource nodes filter.
   * @return a filter.
   */
  public ResourceNodesFilter getFilter()
  {
    return _filter;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Set the data to display.
   * @param lootMgr Loot manager.
   * @param items Data to show.
   */
  public void setItems(ResourceNodesLootManager lootMgr, List<Item> items)
  {
    disposeGadgets();
    _filter.setItems(items);
    for(final Item sourceItem : items)
    {
      List<Item> lootItems=lootMgr.getLoots(sourceItem.getIdentifier());
      ResourceNodeFilterItemGadgets gadgets=new ResourceNodeFilterItemGadgets(_parent);
      gadgets.setData(sourceItem,lootItems);
      _gadgets.add(gadgets);
      final JCheckBox checkbox=gadgets.getCheckbox().getCheckbox();
      checkbox.setSelected(_filter.isSelected(sourceItem.getIdentifier()));
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          _filter.setSelected(sourceItem.getIdentifier(),checkbox.isSelected());
          _parent.getWindow().repaint();
        }
      };
      checkbox.addActionListener(al);
    }
    fillPanel();
    _panel.revalidate();
    _panel.repaint();
  }

  private void fillPanel()
  {
    _panel.removeAll();
    // Headers
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    _panel.add(GuiFactory.buildLabel("Show?"),c); // I18n
    c=new GridBagConstraints(1,0,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    _panel.add(GuiFactory.buildLabel("Source item"),c); // I18n
    boolean hasLootableItems=hasLootableItems();
    if (hasLootableItems)
    {
      c=new GridBagConstraints(3,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      _panel.add(GuiFactory.buildLabel("Lootable items"),c); // I18n
    }
    // Filter items
    int y=1;
    for(ResourceNodeFilterItemGadgets gadgets : _gadgets)
    {
      // Checkbox
      c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      _panel.add(gadgets.getCheckbox().getCheckbox(),c);
      // Source item
      IconController sourceItemIcon=gadgets.getSourceItemIcon();
      if (sourceItemIcon!=null)
      {
        c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
        _panel.add(sourceItemIcon.getIcon(),c);
      }
      HyperLinkController sourceItemLink=gadgets.getSourceItemLink();
      if (sourceItemLink!=null)
      {
        c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
        _panel.add(sourceItemLink.getLabel(),c);
      }
      // Loot items
      if (hasLootableItems)
      {
        JPanel lootItemsPanel=GuiFactory.buildPanel(new FlowLayout());
        for(IconController lootItemIcon : gadgets.getLootItemIcons())
        {
          lootItemsPanel.add(lootItemIcon.getIcon());
        }
        c=new GridBagConstraints(3,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
        _panel.add(lootItemsPanel,c);
      }
      y++;
    }
  }

  private boolean hasLootableItems()
  {
    for(ResourceNodeFilterItemGadgets gadgets : _gadgets)
    {
      if (gadgets.getLootItemIcons().size()>0)
      {
        return true;
      }
    }
    return false;
  }

  private void disposeGadgets()
  {
    if (_gadgets!=null)
    {
      for(ResourceNodeFilterItemGadgets gadgets : _gadgets)
      {
        gadgets.dispose();
      }
      _gadgets.clear();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    disposeGadgets();
    _gadgets=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
