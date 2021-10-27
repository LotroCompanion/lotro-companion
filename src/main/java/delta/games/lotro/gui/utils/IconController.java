package delta.games.lotro.gui.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.navigation.NavigatorFactory;

/**
 * Controller for an icon that brings a page.
 * @author DAM
 */
public class IconController
{
  private static final Logger LOGGER=Logger.getLogger(IconController.class);

  private static final int DEFAULT_SIZE=32;

  private WindowController _parent;
  private ActionListener _listener;
  protected JButton _icon;
  protected PageIdentifier _pageId;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public IconController(WindowController parent)
  {
    _parent=parent;
    _icon=GuiFactory.buildIconButton();
    _icon.setSize(DEFAULT_SIZE,DEFAULT_SIZE);
    _listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showForm();
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

  private void showForm()
  {
    if (_pageId!=null)
    {
      LOGGER.info("Display page: "+_pageId);
      NavigatorWindowController navigator=null;
      if (_parent instanceof NavigatorWindowController)
      {
        navigator=(NavigatorWindowController)_parent;
      }
      else
      {
        int id=_parent.getWindowsManager().getAll().size();
        navigator=NavigatorFactory.buildNavigator(_parent,id);
      }
      navigator.navigateTo(_pageId);
      navigator.bringToFront();
    }
  }

  protected void setIcon(Icon icon)
  {
    _icon.setIcon(icon);
    _icon.setSize(icon.getIconWidth(),icon.getIconHeight());
    _icon.setEnabled(true);
    _icon.setFocusable(true);
  }

  /**
   * Clear the contents.
   */
  public void clear()
  {
    BufferedImage image=new BufferedImage(DEFAULT_SIZE, DEFAULT_SIZE, BufferedImage.TYPE_INT_ARGB);
    ImageIcon icon=new ImageIcon(image);
    setIcon(icon);
    _icon.setFocusable(false);
    _icon.setEnabled(false);
    _icon.setToolTipText("");
    _pageId=null;
  }

  /**
   * Set the page to use.
   * @param pageId Page identifier.
   */
  public void setPageId(PageIdentifier pageId)
  {
    _pageId=pageId;
  }

  /**
   * Set the tooltip text.
   * @param text Text to set.
   */
  public void setTooltipText(String text)
  {
    _icon.setToolTipText(text);
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
      }
      _icon=null;
    }
    _listener=null;
    _pageId=null;
  }
}
