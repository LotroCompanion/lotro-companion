package delta.games.lotro.gui.quests;

import java.util.List;

import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.objectives.Objective;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;
import delta.games.lotro.lore.quests.objectives.ObjectivesManager;
import delta.games.lotro.lore.quests.objectives.QuestCompleteCondition;
import delta.games.lotro.utils.Proxy;

/**
 * Build for HTML code to display objectives.
 * @author DAM
 */
public class ObjectivesHtmlBuilder
{
  /**
   * Print HTML code for the objectives of an achievable.
   * @param sb Output.
   * @param achievable Achievable to display.
   */
  public static void buildHtml(StringBuilder sb, Achievable achievable)
  {
    // Objectives
    ObjectivesManager objectivesMgr=achievable.getObjectives();
    List<Objective> objectives=objectivesMgr.getObjectives();
    for(Objective objective : objectives)
    {
      int index=objective.getIndex();
      sb.append("<p><b>Objective #").append(index).append("</b></p>");
      String text=objective.getText();
      if (text.length()>0)
      {
        sb.append("<p>").append(toHtml(text)).append("</p>");
      }
      for(ObjectiveCondition condition : objective.getConditions())
      {
        handleCondition(sb,condition);
      }
    }
  }

  private static void handleCondition(StringBuilder sb, ObjectiveCondition condition)
  {
    if (condition instanceof QuestCompleteCondition)
    {
      QuestCompleteCondition questComplete=(QuestCompleteCondition)condition;
      //int count=questComplete.getCompletionCount();
      //String questCategory=questComplete.getQuestCategory();
      // Deed/quest
      Proxy<Achievable> proxy=questComplete.getProxy();
      if (proxy!=null)
      {
        Achievable achievable=proxy.getObject();
        if (achievable instanceof QuestDescription)
        {
          QuestDescription quest=(QuestDescription)achievable;
          sb.append("<p>Complete quest <b>");
          String text=quest.getName();
          String to="QUEST:"+quest.getIdentifier();
          printLink(sb,to,text);
          sb.append("</b></p>");
        }
        else if (achievable instanceof DeedDescription)
        {
          DeedDescription deed=(DeedDescription)achievable;
          sb.append("<p>Complete deed <b>");
          String text=deed.getName();
          String to="DEED:"+deed.getIdentifier();
          printLink(sb,to,text);
          sb.append("</b></p>");
        }
      }
    }
  }

  private static void printLink(StringBuilder sb, String to, String text)
  {
    sb.append("<a href=\"");
    sb.append(to);
    sb.append("\">");
    sb.append(text);
    sb.append("</a>");
  }

  private static String toHtml(String text)
  {
    text=text.trim();
    text=text.replace("\n\n","<br>");
    text=text.replace("\n","<br>");
    return text;
  }
}
