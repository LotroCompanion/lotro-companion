package delta.games.lotro.gui.items;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.gui.LotroIconsManager;
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
  private JLabel _icon;
  private JLabel _name;

  /**
   * Constructor.
   * @param itemInstance Item instance.
   */
  public ItemIdentificationPanelController(ItemInstance<? extends Item> itemInstance)
  {
    _itemInstance=itemInstance;
    _panel=buildIdentificationPanel();
    update();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildIdentificationPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(panelLine,c);
    c.gridy++;
    // Icon
    _icon=GuiFactory.buildIconLabel(null);
    panelLine.add(_icon);
    // Name
    _name=GuiFactory.buildLabel("");
    _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
    panelLine.add(_name);
    return panel;
  }

  /**
   * Update display.
   */
  private void update()
  {
    Item item=_itemInstance.getReference();
    // - icon
    String iconPath=item.getIcon();
    ImageIcon icon=LotroIconsManager.getItemIcon(iconPath);
    _icon.setIcon(icon);
    // - name
    String name=item.getName();
    _name.setText(name);
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
    _icon=null;
    _name=null;
  }
}
