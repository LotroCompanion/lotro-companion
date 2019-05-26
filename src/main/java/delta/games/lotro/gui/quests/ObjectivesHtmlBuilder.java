package delta.games.lotro.gui.quests;

import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.gui.common.navigator.ReferenceConstants;
import delta.games.lotro.lore.geo.LandmarkDescription;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.objectives.DefaultObjectiveCondition;
import delta.games.lotro.lore.quests.objectives.InventoryItemCondition;
import delta.games.lotro.lore.quests.objectives.LandmarkDetectionCondition;
import delta.games.lotro.lore.quests.objectives.MonsterDiedCondition;
import delta.games.lotro.lore.quests.objectives.MonsterDiedCondition.MobSelection;
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
  private static final Logger LOGGER=Logger.getLogger(ObjectivesHtmlBuilder.class);

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
    //sb.append("<p>").append(condition.getIndex()+": "+condition.getType()).append("</p>");
    if (condition instanceof QuestCompleteCondition)
    {
      QuestCompleteCondition questComplete=(QuestCompleteCondition)condition;
      handleQuestCompleteCondition(sb,questComplete);
    }
    else if (condition instanceof MonsterDiedCondition)
    {
      MonsterDiedCondition monsterDied=(MonsterDiedCondition)condition;
      handleMonsterDiedCondition(sb,monsterDied);
    }
    else if (condition instanceof LandmarkDetectionCondition)
    {
      LandmarkDetectionCondition landmarkDetection=(LandmarkDetectionCondition)condition;
      handleLandmarkDetectionCondition(sb,landmarkDetection);
    }
    else if (condition instanceof InventoryItemCondition)
    {
      InventoryItemCondition inventoryItem=(InventoryItemCondition)condition;
      handleInventoryItemCondition(sb,inventoryItem);
    }
    else if (condition instanceof DefaultObjectiveCondition)
    {
      DefaultObjectiveCondition defaultCondition=(DefaultObjectiveCondition)condition;
      handleDefaultCondition(sb,defaultCondition);
    }
  }

  private static void handleQuestCompleteCondition(StringBuilder sb, QuestCompleteCondition questComplete)
  {
    printSharedAttributes(sb,questComplete);
    int count=questComplete.getCompletionCount();
    String questCategory=questComplete.getQuestCategory();
    sb.append("<p>");
    Proxy<Achievable> proxy=questComplete.getProxy();
    if (proxy!=null)
    {
      Achievable achievable=proxy.getObject();
      if (achievable!=null)
      {
        boolean isQuest=(achievable instanceof QuestDescription);
        String type=isQuest?"quest":"deed";
        sb.append("Complete ").append(type).append(" <b>");
        String text=achievable.getName();
        String to=ReferenceConstants.getAchievableReference(achievable);
        printLink(sb,to,text);
        sb.append("</b>");
      }
      else
      {
        LOGGER.warn("Could not resolve deed/quest ID="+proxy.getId()+", name="+proxy.getName());
        sb.append("Complete quest/deed "+proxy.getId());
      }
    }
    else if (questCategory!=null)
    {
      sb.append("Complete quests in category ").append(questCategory);
    }
    if (count>1)
    {
      sb.append(" (x").append(count).append(')');
    }
    sb.append("</p>");
  }

  private static void handleMonsterDiedCondition(StringBuilder sb, MonsterDiedCondition monsterDied)
  {
    printSharedAttributes(sb,monsterDied);
    int count=monsterDied.getCount();
    String mobName=monsterDied.getMobName();
    List<MobSelection> mobSelections=monsterDied.getMobSelections();
    sb.append("<p>");
    if (mobName!=null)
    {
      sb.append("Kill ").append(mobName);
    }
    else
    {
      if (mobSelections.size()>0)
      {
        sb.append("Kill ");
        int index=0;
        for(MobSelection mobSelection : mobSelections)
        {
          String what=mobSelection.getWhat();
          String where=mobSelection.getWhere();
          if (what==null)
          {
            what="Mob";
          }
          if (index>0)
          {
            sb.append(" or ");
          }
          sb.append(what).append(" in ").append(where);
          index++;
        }
      }
    }
    if (count>1)
    {
      sb.append(" (x").append(count).append(')');
    }
    sb.append("</p>");
  }

  private static void handleLandmarkDetectionCondition(StringBuilder sb, LandmarkDetectionCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      Proxy<LandmarkDescription> landmark=condition.getLandmarkProxy();
      if (landmark!=null)
      {
        sb.append("<p>");
        String name=landmark.getName();
        sb.append("Find ").append(name);
        sb.append("</p>");
      }
    }
    printLoreInfo(sb,condition);
  }

  private static void handleInventoryItemCondition(StringBuilder sb, InventoryItemCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      Proxy<Item> itemProxy=condition.getProxy();
      if (itemProxy!=null)
      {
        sb.append("<p>");
        String name=itemProxy.getName();
        sb.append("Get ").append(name);
        sb.append("</p>");
      }
    }
    printLoreInfo(sb,condition);
  }

  private static void handleDefaultCondition(StringBuilder sb, DefaultObjectiveCondition condition)
  {
    printSharedAttributes(sb,condition);
  }

  private static void printSharedAttributes(StringBuilder sb, ObjectiveCondition condition)
  {
    printProgressOverride(sb,condition);
    printLoreInfo(sb,condition);
  }

  private static boolean printProgressOverride(StringBuilder sb, ObjectiveCondition condition)
  {
    String progressOverride=condition.getProgressOverride();
    if ((progressOverride!=null) && (progressOverride.length()>0))
    {
      sb.append("<p>").append(toHtml(progressOverride)).append("</p>");
      return true;
    }
    return false;
  }

  private static void printLoreInfo(StringBuilder sb, ObjectiveCondition condition)
  {
    String loreInfo=condition.getLoreInfo();
    if ((loreInfo!=null) && (loreInfo.length()>0))
    {
      sb.append("<p><i>").append(toHtml(loreInfo)).append("</i></p>");
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
