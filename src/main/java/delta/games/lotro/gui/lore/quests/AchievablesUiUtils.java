package delta.games.lotro.gui.lore.quests;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Utility methods for emote-related UIs.
 * @author DAM
 */
public class AchievablesUiUtils
{
  /**
   * Build an achievable link controller.
   * @param parent Parent window.
   * @param achievable Achievable to use.
   * @param label Label to use.
   * @return a new controller.
   */
  public static HyperLinkController buildAchievableLink(final WindowController parent, final Achievable achievable, JLabel label)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showAchievableWindow(parent,achievable);
      }
    };
    String text=(achievable!=null)?achievable.getName():"???";
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action,label);
    return controller;
  }

  /**
   * Show an achievable display window.
   * @param parent Parent window.
   * @param achievable Achievable.
   */
  public static void showAchievableWindow(WindowController parent, Achievable achievable)
  {
    PageIdentifier ref=ReferenceConstants.getAchievableReference(achievable);
    NavigationUtils.navigateTo(ref,parent);
  }
}
