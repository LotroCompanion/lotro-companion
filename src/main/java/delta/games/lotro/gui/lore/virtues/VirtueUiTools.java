package delta.games.lotro.gui.lore.virtues;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;

/**
 * Tools related to virtues UI.
 * @author DAM
 */
public class VirtueUiTools
{
  /**
   * Build a virtue link controller.
   * @param parent Parent window.
   * @param virtue Virtue to use.
   * @return a new controller.
   */
  public static HyperLinkController buildVirtueLink(final WindowController parent, final VirtueDescription virtue)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showVirtueForm(parent,virtue);
      }
    };
    String text=(virtue!=null)?virtue.getName():"???";
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action);
    return controller;
  }

  /**
   * Show the form window for an item.
   * @param parent Parent window.
   * @param virtue Virtue to show.
   */
  public static void showVirtueForm(WindowController parent, VirtueDescription virtue)
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
    PageIdentifier ref=ReferenceConstants.getVirtueReference(virtue.getIdentifier());
    window.navigateTo(ref);
    window.show(false);
  }
}
