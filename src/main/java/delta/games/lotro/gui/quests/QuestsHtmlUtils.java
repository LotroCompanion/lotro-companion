package delta.games.lotro.gui.quests;

import java.util.List;

import delta.games.lotro.lore.agents.npcs.NpcDescription;
import delta.games.lotro.lore.quests.dialogs.DialogElement;
import delta.games.lotro.lore.quests.dialogs.QuestCompletionComment;
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

  /**
   * Build HTML for a quest completion comment.
   * @param sb Output stream.
   * @param comment Data to show.
   */
  public static void buildHtmlForCompletionComment(StringBuilder sb, QuestCompletionComment comment)
  {
    sb.append("<br>");
    List<Proxy<NpcDescription>> whos=comment.getWhos();
    if (whos.size()>0)
    {
      boolean first=true;
      for(Proxy<NpcDescription> who : whos)
      {
        String name=who.getName();
        if (name!=null)
        {
          if (!first) sb.append(", ");
          sb.append(name);
          first=false;
        }
      }
      sb.append(" say: ");
    }
    List<String> whats=comment.getWhats();
    for(String what : whats)
    {
      sb.append("<br>");
      String htmlWhat=HtmlUtils.toHtml(what);
      sb.append(htmlWhat);
    }
  }
}
