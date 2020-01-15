package delta.games.lotro.gui.items;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.utils.ItemIconController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for an item instance identification panel.
 * @author DAM
 */
public class ItemIdentificationPanelController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  // GUI
  private JPanel _panel;
  // - Item identification (icon+name)
  private ItemIconController _itemIcon;
  private JLabel _name;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param itemInstance Item instance.
   */
  public ItemIdentificationPanelController(WindowController parent, ItemInstance<? extends Item> itemInstance)
  {
    _itemInstance=itemInstance;
    _panel=buildIdentificationPanel(parent);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildIdentificationPanel(WindowController parent)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(panelLine,c);
    c.gridy++;
    // Icon
    Item item=_itemInstance.getReference();
    _itemIcon=new ItemIconController(parent);
    _itemIcon.setItem(item,1);
    panelLine.add(_itemIcon.getIcon());
    // Name
    String name=item.getName();
    _name=GuiFactory.buildLabel(name);
    _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
    panelLine.add(_name);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _itemInstance=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _name=null;
    // Controllers
    if (_itemIcon!=null)
    {
      _itemIcon.dispose();
      _itemIcon=null;
    }
  }
}
