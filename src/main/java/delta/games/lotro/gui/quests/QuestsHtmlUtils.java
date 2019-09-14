package delta.games.lotro.gui.quests;

import delta.games.lotro.lore.npc.NpcDescription;
import delta.games.lotro.lore.quests.dialogs.DialogElement;
import delta.games.lotro.utils.Proxy;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * HTML-related utils for quests.
 * @author DAM
 */
public class QuestsHtmlUtils
{
  /**
   * Build HTML for a dialog element.
   * @param sb Output stream.
   * @param dialog Data to show.
   */
  public static void buildHtmlForDialog(StringBuilder sb, DialogElement dialog)
  {
    sb.append("<br>");
    Proxy<NpcDescription> who=dialog.getWho();
    if (who!=null)
    {
      String name=who.getName();
      if (name!=null)
      {
        sb.append(name).append(": ");
      }
    }
    String what=dialog.getWhat();
    String htmlWhat=HtmlUtils.toHtml(what);
    sb.append(htmlWhat);
  }
}
