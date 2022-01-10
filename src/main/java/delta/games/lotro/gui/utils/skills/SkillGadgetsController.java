package delta.games.lotro.gui.utils.skills;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;

/**
 * Controller for the gadgets that show a skill.
 * @author DAM
 */
public class SkillGadgetsController
{
  private HyperLinkController _link;
  private IconController _icon;
  private JLabel _complement;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param skill Skill to show.
   */
  public SkillGadgetsController(WindowController parent, SkillDescription skill)
  {
    _link=buildSkillLink(parent,skill);
    _icon=IconControllerFactory.buildSkillIcon(parent,skill);
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
   * Build a skill link controller.
   * @param parent Parent window.
   * @param skill Skill to use.
   * @return a new controller.
   */
  private HyperLinkController buildSkillLink(final WindowController parent, final SkillDescription skill)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showSkillForm(parent,skill);
      }
    };
    String text=(skill!=null)?skill.getName():"???";
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action);
    return controller;
  }

  /**
   * Show the form window for an item.
   * @param parent Parent window.
   * @param skill Skill to show.
   */
  private static void showSkillForm(WindowController parent, SkillDescription skill)
  {
    NavigatorWindowController window=null;
    if (parent instanceof NavigatorWindowController)
    {
      window=(NavigatorWindowController)parent;
    }
    else
    {
      WindowsManager windows=parent.getWindowsManager();
      int id=windows.getAll().size();
      window=NavigatorFactory.buildNavigator(parent,id);
      windows.registerWindow(window);
    }
    PageIdentifier ref=ReferenceConstants.getSkillReference(skill.getIdentifier());
    window.navigateTo(ref);
    window.show(false);
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
  }
}
