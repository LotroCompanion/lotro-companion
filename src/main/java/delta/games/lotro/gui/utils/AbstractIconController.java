package delta.games.lotro.gui.utils;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;

/**
 * Base class for controllers of an icon that brings a page.
 * @author DAM
 */
public class AbstractIconController
{
  private static final int DEFAULT_SIZE=32;

  protected WindowController _parent;
  protected ActionListener _listener;
  protected JButton _icon;
  private boolean _useNavigation;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param useNavigation Use navigation of not.
   */
  public AbstractIconController(WindowController parent, boolean useNavigation)
  {
    _parent=parent;
    _useNavigation=useNavigation;
    _icon=GuiFactory.buildIconButton();
    _icon.setSize(DEFAULT_SIZE,DEFAULT_SIZE);
  }

  /**
   * Get the managed button.
   * @return a button.
   */
  public JButton getIcon()
  {
    return _icon;
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JComponent getComponent()
  {
    return _icon;
  }

  /**
   * Set the icon.
   * @param icon Icon to set.
   */
  public void setIcon(Icon icon)
  {
    _icon.setIcon(icon);
    _icon.setDisabledIcon(icon);
    if (icon!=null)
    {
      _icon.setSize(icon.getIconWidth(),icon.getIconHeight());
    }
    if (_useNavigation)
    {
      _icon.setEnabled(true);
      _icon.setFocusable(true);
    }
    else
    {
      _icon.setEnabled(false);
      _icon.setFocusable(false);
    }
  }

  /**
   * Clear the contents.
   * @param icon Default icon.
   */
  public void clear(Icon icon)
  {
    if (icon==null)
    {
      BufferedImage image=new BufferedImage(DEFAULT_SIZE, DEFAULT_SIZE, BufferedImage.TYPE_INT_ARGB);
      icon=new ImageIcon(image);
    }
    setIcon(icon);
    _icon.setFocusable(false);
    _icon.setEnabled(false);
    _icon.setToolTipText("");
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
  }
}
