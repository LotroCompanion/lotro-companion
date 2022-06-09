package delta.games.lotro.gui.utils;

import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;

/**
 * Controller for the gadgets that show a skill.
 * @author DAM
 */
public class IconLinkLabelGadgetsController
{
  private HyperLinkController _link;
  private IconController _icon;
  private JLabel _complement;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param icon Icon to use.
   * @param text Link text.
   * @param pageId Link page.
   */
  public IconLinkLabelGadgetsController(WindowController parent, IconController icon, String text, PageIdentifier pageId)
  {
    _link=NavigationUtils.buildNavigationLink(parent,text,pageId);
    _icon=icon;
    _complement=GuiFactory.buildLabel("");
  }

  /**
   * Get the managed link.
   * @return a link.
   */
  public HyperLinkController getLink()
  {
    return _link;
  }

  /**
   * Get the managed icon controller.
   * @return an icon controller.
   */
  public IconController getIcon()
  {
    return _icon;
  }

  /**
   * Get the complement label.
   * @return a complement label.
   */
  public JLabel getComplement()
  {
    return _complement;
  }

  /**
   * Release all managed resources.
   */
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
    _complement=null;
  }
}
