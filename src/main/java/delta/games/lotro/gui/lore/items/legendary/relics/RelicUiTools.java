package delta.games.lotro.gui.lore.items.legendary.relics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import delta.common.ui.swing.icons.IconWithText;
import delta.common.ui.swing.icons.IconWithText.Position;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Tools related to relics UI.
 * @author DAM
 */
public class RelicUiTools
{
  /**
   * Build an icon for a relic count.
   * @param relic Relic to use.
   * @param count Count to display.
   * @return An icon.
   */
  public static Icon buildRelicIcon(Relic relic, int count)
  {
    Icon ret=LotroIconsManager.getRelicIcon(relic.getIconFilename());
    if (count>1)
    {
      IconWithText iconWithText=new IconWithText(ret,String.valueOf(count),Color.WHITE);
      iconWithText.setPosition(Position.BOTTOM_RIGHT);
      ret=iconWithText;
    }
    return ret;
  }

  /**
   * Build a relic link controller.
   * @param parent Parent window.
   * @param relic Relic to use.
   * @return a new controller.
   */
  public static HyperLinkController buildRelicLink(final WindowController parent, final Relic relic)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showRelicForm(parent,relic);
      }
    };
    String text=(relic!=null)?relic.getName():"???";
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action);
    return controller;
  }

  /**
   * Show the form window for a relic.
   * @param parent Parent window.
   * @param relic Relic to show.
   */
  public static void showRelicForm(WindowController parent, Relic relic)
  {
    PageIdentifier ref=ReferenceConstants.getRelicReference(relic.getIdentifier());
    NavigationUtils.navigateTo(ref,parent);
  }
}
