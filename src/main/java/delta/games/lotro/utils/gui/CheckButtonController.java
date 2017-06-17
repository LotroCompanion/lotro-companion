package delta.games.lotro.utils.gui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;

/**
 * Controller for a button that displays state icons.
 * @author DAM
 */
public class CheckButtonController
{
  private ImageIcon[] _icons;
  private JButton _button;
  private ActionListener _listener;

  /**
   * Constructor.
   * @param iconPaths Icons to use.
   */
  public CheckButtonController(String[] iconPaths)
  {
    _icons=new ImageIcon[iconPaths.length];
    int index=0;
    for(String iconPath : iconPaths)
    {
      String fullIconPath="/resources/gui/icons/"+iconPath+".png";
      ImageIcon icon=IconsManager.getIcon(fullIconPath);
      _icons[index]=icon;
      index++;
    }
    _button=buildButton();
  }

  /**
   * Get the managed button.
   * @return the managed button.
   */
  public JButton getButton()
  {
    return _button;
  }

  private Dimension getIconSize()
  {
    int width=0;
    int height=0;
    for(ImageIcon icon : _icons)
    {
      int iconHeight=icon.getIconHeight();
      if (iconHeight>height) height=iconHeight;
      int iconWidth=icon.getIconWidth();
      if (iconWidth>width) width=iconWidth;
    }
    return new Dimension(width,height);
  }

  private JButton buildButton()
  {
    JButton button=GuiFactory.buildButton("");
    button.setIcon(_icons[0]);
    button.setBorderPainted(false);
    button.setOpaque(false);
    button.setMargin(new Insets(0,0,0,0));
    Dimension size=getIconSize();
    button.setSize(size);
    return button;
  }

  /**
   * Set the state of this button.
   * @param index State index (starting at 0).
   */
  public void setState(int index)
  {
    _button.setIcon(_icons[index]);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _icons=null;
    if (_button!=null)
    {
      if (_listener!=null)
      {
        _button.removeActionListener(_listener);
      }
      _button=null;
    }
    _listener=null;
  }
}
