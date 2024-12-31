package delta.games.lotro.gui.lore.skills;

import java.util.Collections;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;

/**
 * Utility methods for skill-related UIs.
 * @author DAM
 */
public class SkillUiUtils
{
  /**
   * Show a skill display window.
   * @param parent Parent window.
   * @param skillID Skill identifier.
   */
  public static void showSkillWindow(WindowController parent, int skillID)
  {
    PageIdentifier ref=ReferenceConstants.getSkillReference(skillID);
    NavigationUtils.navigateTo(ref,parent);
  }

  /**
   * Build a combo-box controller to choose a skill.
   * @param skills Skills to show.
   * @param includeEmptyItem Include empty item.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<SkillDescription> buildSkillsCombo(List<SkillDescription> skills, boolean includeEmptyItem)
  {
    ComboBoxController<SkillDescription> ctrl=new ComboBoxController<SkillDescription>();
    if (includeEmptyItem)
    {
      ctrl.addEmptyItem("");
    }
    Collections.sort(skills,new NamedComparator());
    for(SkillDescription skill : skills)
    {
      String label=skill.getName();
      ctrl.addItem(skill,label);
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
