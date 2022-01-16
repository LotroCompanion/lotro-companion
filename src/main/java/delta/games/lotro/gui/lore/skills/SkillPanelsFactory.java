package delta.games.lotro.gui.lore.skills;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.SkillsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.skills.form.SkillDisplayPanelController;

/**
 * Factory for skill-related panels.
 * @author DAM
 */
public class SkillPanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public SkillPanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.SKILL_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildSkillPanel(id);
    }
    return ret;
  }

  private SkillDisplayPanelController buildSkillPanel(int skillID)
  {
    SkillsManager skillsMgr=SkillsManager.getInstance();
    SkillDescription skill=skillsMgr.getSkill(skillID);
    if (skill!=null)
    {
      SkillDisplayPanelController skillPanel=new SkillDisplayPanelController(_parent,skill);
      return skillPanel;
    }
    return null;
  }
}
