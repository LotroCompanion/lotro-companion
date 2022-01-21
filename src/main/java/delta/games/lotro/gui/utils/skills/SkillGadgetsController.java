package delta.games.lotro.gui.utils.skills;

import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.gui.utils.NavigationUtils;

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
    PageIdentifier pageId=ReferenceConstants.getSkillReference(skill.getIdentifier());
    String text=skill.getName();
    _link=NavigationUtils.buildNavigationLink(parent,text,pageId);
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
