package delta.games.lotro.gui.utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.misc.Disposable;

/**
 * Controller for a panel that contains a icon controller and a link controller.
 * @author DAM
 */
public class IconAndLinkPanelController implements Disposable
{
  // Controllers
  private IconController _icon;
  private HyperLinkController _link;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param icon Icon controller.
   * @param link Link controller.
   */
  public IconAndLinkPanelController(IconController icon, HyperLinkController link)
  {
    _icon=icon;
    _link=link;
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    // Result panel;
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
    ret.add(_icon.getIcon(),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_link.getLabel(),c);
    return ret;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_link!=null)
    {
      _link.dispose();
      _link=null;
    }
    if (_icon!=null)
    {
      _icon.dispose();
      _icon=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
