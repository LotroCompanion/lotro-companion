package delta.games.lotro.gui.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.items.ItemUiTools;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for an item icon.
 * @author DAM
 */
public class ItemIconController
{
  private static final Logger LOGGER=Logger.getLogger(ItemIconController.class);

  private WindowController _parent;
  private ActionListener _listener;
  private JButton _icon;
  private Item _item;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public ItemIconController(WindowController parent)
  {
    _parent=parent;
    _icon=GuiFactory.buildIconButton();
    _icon.setSize(32,32);
    _listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showItemForm();
      }
    };
    _icon.addActionListener(_listener);
  }

  /**
   * Get the managed item icon.
   * @return an icon.
   */
  public JButton getIcon()
  {
    return _icon;
  }

  /**
   * Set the displayed item.
   * @param item Item to display.
   * @param count Count to display.
   */
  public void setItem(Item item, int count)
  {
    Icon icon=ItemUiTools.buildItemIcon(item,count);
    if (icon!=null)
    {
      _icon.setIcon(icon);
      _icon.setSize(icon.getIconWidth(),icon.getIconHeight());
    }
    _item=item;
  }

  private void showItemForm()
  {
    if (_item!=null)
    {
      LOGGER.info("Display form for item: "+_item);
      if (_parent instanceof NavigatorWindowController)
      {
        NavigatorWindowController navigator=(NavigatorWindowController)_parent;
        PageIdentifier ref=ReferenceConstants.getItemReference(_item.getIdentifier());
        navigator.navigateTo(ref);
      }
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_icon!=null)
    {
      if (_listener!=null)
      {
        _icon.removeActionListener(_listener);
        _listener=null;
      }
      _icon=null;
    }
  }
}
