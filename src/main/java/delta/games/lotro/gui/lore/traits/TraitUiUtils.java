package delta.games.lotro.gui.lore.traits;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;

/**
 * Utility methods for trait-related UIs.
 * @author DAM
 */
public class TraitUiUtils
{
  /**
   * Build an trait link controller.
   * @param parent Parent window.
   * @param trait Trait to use.
   * @param label Label to use.
   * @return a new controller.
   */
  public static HyperLinkController buildTraitLink(final WindowController parent, final TraitDescription trait, JLabel label)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showTraitWindow(parent,trait.getIdentifier());
      }
    };
    String text=(trait!=null)?trait.getName():"???";
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action,label);
    return controller;
  }

  /**
   * Show a trait display window.
   * @param parent Parent window.
   * @param traitID Trait identifier.
   */
  public static void showTraitWindow(WindowController parent, int traitID)
  {
    PageIdentifier ref=ReferenceConstants.getTraitReference(traitID);
    NavigationUtils.navigateTo(ref,parent);
  }
}
