package delta.games.lotro.gui.lore.emotes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.emotes.EmoteDescription;

/**
 * Utility methods for emote-related UIs.
 * @author DAM
 */
public class EmoteUiUtils
{
  /**
   * Build an emote link controller.
   * @param parent Parent window.
   * @param emote Emote to use.
   * @param label Label to use.
   * @return a new controller.
   */
  public static HyperLinkController buildEmoteLink(final WindowController parent, final EmoteDescription emote, JLabel label)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showEmoteWindow(parent,emote.getIdentifier());
      }
    };
    String text=(emote!=null)?emote.getName():"???";
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action,label);
    return controller;
  }

  /**
   * Show an emote display window.
   * @param parent Parent window.
   * @param emoteID Emote identifier.
   */
  public static void showEmoteWindow(WindowController parent, int emoteID)
  {
    PageIdentifier ref=ReferenceConstants.getEmoteReference(emoteID);
    NavigationUtils.navigateTo(ref,parent);
  }
}
