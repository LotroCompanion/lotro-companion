package delta.games.lotro.gui.lore.quests;

import java.util.List;

import delta.common.ui.swing.area.AreaController;
import delta.games.lotro.common.Interactable;
import delta.games.lotro.lore.quests.dialogs.DialogElement;
import delta.games.lotro.lore.quests.dialogs.QuestCompletionComment;
import delta.games.lotro.utils.gui.HtmlUtils;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * HTML-related utils for quests.
 * @author DAM
 */
public class QuestsHtmlUtils
{
  /**
   * Build HTML for a dialog element.
   * @param parent Parent controller.
   * @param sb Output stream.
   * @param dialog Data to show.
   */
  public static void buildHtmlForDialog(AreaController parent, StringBuilder sb, DialogElement dialog)
  {
    sb.append("<br>");
    Interactable who=dialog.getWho();
    if (who!=null)
    {
      String name=who.getName();
      if (name!=null)
      {
        sb.append(name).append(": ");
      }
    }
    String what=dialog.getWhat();
    what=ContextRendering.render(parent,what);
    String htmlWhat=HtmlUtils.toHtml(what);
    sb.append(htmlWhat);
  }

  /**
   * Build HTML for a quest completion comment.
   * @param parent Parent controller.
   * @param sb Output stream.
   * @param comment Data to show.
   */
  public static void buildHtmlForCompletionComment(AreaController parent, StringBuilder sb, QuestCompletionComment comment)
  {
    sb.append("<br>");
    List<Interactable> whos=comment.getWhos();
    if (!whos.isEmpty())
    {
      boolean first=true;
      for(Interactable who : whos)
      {
        String name=who.getName();
        if (name!=null)
        {
          if (!first) sb.append(", ");
          sb.append(name);
          first=false;
        }
      }
      sb.append(" say: "); // I18n
    }
    List<String> whats=comment.getWhats();
    for(String what : whats)
    {
      sb.append("<br>");
      what=ContextRendering.render(parent,what);
      String htmlWhat=HtmlUtils.toHtml(what);
      sb.append(htmlWhat);
    }
  }
}
