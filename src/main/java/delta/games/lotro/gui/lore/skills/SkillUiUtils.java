package delta.games.lotro.gui.lore.skills;

import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
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
}
