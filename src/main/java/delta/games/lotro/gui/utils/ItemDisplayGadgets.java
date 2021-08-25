package delta.games.lotro.gui.utils;

import javax.swing.JButton;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.legendary.relics.RelicUiTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;

/**
 * Controller for the gadgets to display an item.
 * @author DAM
 */
public class ItemDisplayGadgets
{
  private NavigatorWindowController _parent;
  private IconController _icon; // Item icon, with optional count
  private HyperLinkController _name; // Item name
  private JLabel _comment; // Optional comment, either before or after icon+name

  /**
   * Constructor.
   * @param parent Parent window.
   * @param itemId Identifier of the item to show.
   * @param count Items count.
   * @param comment Comment.
   */
  public ItemDisplayGadgets(NavigatorWindowController parent, int itemId, int count, String comment)
  {
    _parent=parent;
    Item item=ItemsManager.getInstance().getItem(itemId); 
    ItemIconController icon=new ItemIconController(_parent);
    icon.setItem(item,count);
    _icon=icon;
    _name=ItemUiTools.buildItemLink(parent,item);
    _comment=GuiFactory.buildLabel(comment);
  }

  /**
   * Constructor.
   * @param parent Parent window.
   * @param relicId Identifier of the relic to show.
   * @param count Relics count.
   */
  public ItemDisplayGadgets(NavigatorWindowController parent, int relicId, int count)
  {
    _parent=parent;
    Relic relic=RelicsManager.getInstance().getById(relicId); 
    RelicIconController icon=new RelicIconController(_parent);
    icon.setRelic(relic,count);
    _icon=icon;
    _name=RelicUiTools.buildRelicLink(parent,relic);
    _comment=GuiFactory.buildLabel("");
  }

  /**
   * Get the managed icon button.
   * @return an icon button.
   */
  public JButton getIcon()
  {
    return _icon.getIcon();
  }

  /**
   * Get the managed name label.
   * @return a label.
   */
  public JLabel getName()
  {
    return _name.getLabel();
  }

  /**
   * Get the managed comment label.
   * @return a label.
   */
  public JLabel getComment()
  {
    return _comment;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_icon!=null)
    {
      _icon.dispose();
      _icon=null;
    }
    if (_name!=null)
    {
      _name.dispose();
      _name=null;
    }
    _name=null;
    _comment=null;
  }
}
