package delta.games.lotro.gui.lore.skills;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;

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
    NavigatorWindowController window=null;
    if (parent instanceof NavigatorWindowController)
    {
      window=(NavigatorWindowController)parent;
    }
    else
    {
      WindowsManager windowsMgr=parent.getWindowsManager();
      int id=windowsMgr.getAll().size();
      window=NavigatorFactory.buildNavigator(parent,id);
      windowsMgr.registerWindow(window);
    }
    PageIdentifier ref=ReferenceConstants.getSkillReference(skillID);
    window.navigateTo(ref);
    window.show(false);
  }
}
