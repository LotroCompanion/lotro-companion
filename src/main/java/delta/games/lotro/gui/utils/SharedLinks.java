package delta.games.lotro.gui.utils;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.races.NationalityDescription;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;

/**
 * Factory for shared links.
 * @author DAM
 */
public class SharedLinks
{
  /**
   * Build a nationality link controller.
   * @param parent Parent window.
   * @param nationality Nationality to show.
   * @return A panel.
   */
  public static HyperLinkController buildNationalityLink(WindowController parent, NationalityDescription nationality)
  {
    if (nationality==null)
    {
      return null;
    }
    // Link
    PageIdentifier pageId=ReferenceConstants.getNationalityReference(nationality);
    String text=nationality.getName();
    return NavigationUtils.buildNavigationLink(parent,text,pageId);
  }
}
