package delta.games.lotro.gui.utils;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;

/**
 * Controller for an icon that brings a page.
 * @author DAM
 */
public class AbstractIconController
{
  private static final int DEFAULT_SIZE=32;

  protected WindowController _parent;
  protected ActionListener _listener;
  protected JButton _icon;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public AbstractIconController(WindowController parent)
  {
    _parent=parent;
    _icon=GuiFactory.buildIconButton();
    _icon.setSize(DEFAULT_SIZE,DEFAULT_SIZE);
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
   * Set the icon.
   * @param icon Icon to set.
   */
  public void setIcon(Icon icon)
  {
    _icon.setIcon(icon);
    if (icon!=null)
    {
      _icon.setSize(icon.getIconWidth(),icon.getIconHeight());
    }
    _icon.setEnabled(true);
    _icon.setFocusable(true);
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
    _icon.setDisabledIcon(icon);
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
