package delta.games.lotro.gui.quests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.utils.misc.IntegerHolder;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.gui.common.navigator.ReferenceConstants;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.geo.LandmarkDescription;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.mobs.MobDescription;
import delta.games.lotro.lore.npc.NpcDescription;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.dialogs.DialogElement;
import delta.games.lotro.lore.quests.objectives.ConditionTarget;
import delta.games.lotro.lore.quests.objectives.ConditionType;
import delta.games.lotro.lore.quests.objectives.DefaultObjectiveCondition;
import delta.games.lotro.lore.quests.objectives.DetectingCondition;
import delta.games.lotro.lore.quests.objectives.DetectionCondition;
import delta.games.lotro.lore.quests.objectives.EmoteCondition;
import delta.games.lotro.lore.quests.objectives.EnterDetectionCondition;
import delta.games.lotro.lore.quests.objectives.ExternalInventoryItemCondition;
import delta.games.lotro.lore.quests.objectives.FactionLevelCondition;
import delta.games.lotro.lore.quests.objectives.InventoryItemCondition;
import delta.games.lotro.lore.quests.objectives.ItemCondition;
import delta.games.lotro.lore.quests.objectives.ItemTalkCondition;
import delta.games.lotro.lore.quests.objectives.ItemUsedCondition;
import delta.games.lotro.lore.quests.objectives.LandmarkDetectionCondition;
import delta.games.lotro.lore.quests.objectives.LevelCondition;
import delta.games.lotro.lore.quests.objectives.MonsterDiedCondition;
import delta.games.lotro.lore.quests.objectives.MonsterDiedCondition.MobSelection;
import delta.games.lotro.lore.quests.objectives.NpcCondition;
import delta.games.lotro.lore.quests.objectives.NpcTalkCondition;
import delta.games.lotro.lore.quests.objectives.NpcUsedCondition;
import delta.games.lotro.lore.quests.objectives.Objective;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;
import delta.games.lotro.lore.quests.objectives.ObjectivesManager;
import delta.games.lotro.lore.quests.objectives.QuestBestowedCondition;
import delta.games.lotro.lore.quests.objectives.QuestCompleteCondition;
import delta.games.lotro.lore.quests.objectives.SkillUsedCondition;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.utils.Proxy;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Build for HTML code to display objectives.
 * @author DAM
 */
public class ObjectivesHtmlBuilder
{
  private static final Logger LOGGER=Logger.getLogger(ObjectivesHtmlBuilder.class);

  private static final String COUNT_PATTERN="{***}/{***}";

  /**
   * Print HTML code for the objectives of an achievable.
   * @param sb Output.
   * @param achievable Achievable to display.
   */
  public static void buildHtml(StringBuilder sb, Achievable achievable)
  {
    // Is deed?
    boolean isDeed=(achievable instanceof DeedDescription);
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
        sb.append("<p>").append(HtmlUtils.toHtml(text)).append("</p>");
      }
      // Conditions
      for(ObjectiveCondition condition : objective.getConditions())
      {
        handleCondition(sb,condition,isDeed);
      }
      // Dialogs
      for(DialogElement dialog : objective.getDialogs())
      {
        QuestsHtmlUtils.buildHtmlForDialog(sb,dialog);
      }
    }
  }

  private static void handleCondition(StringBuilder sb, ObjectiveCondition condition, boolean isDeed)
  {
    ConditionType type=condition.getType();
    if (isDeed)
    {
      if ((type==ConditionType.ENTER_DETECTION) || (type==ConditionType.WORLD_EVENT_CONDITION))
      {
        if (!hasProgressOverride(condition))
        {
          return;
        }
      }
    }
    //sb.append("<p>").append(condition.getIndex()+": "+type).append("</p>");
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
    else if (condition instanceof ItemUsedCondition)
    {
      ItemUsedCondition inventoryItem=(ItemUsedCondition)condition;
      handleItemUsedCondition(sb,inventoryItem);
    }
    else if (condition instanceof ExternalInventoryItemCondition)
    {
      ExternalInventoryItemCondition inventoryItem=(ExternalInventoryItemCondition)condition;
      handleExternalInventoryItemCondition(sb,inventoryItem);
    }
    else if (condition instanceof ItemTalkCondition)
    {
      ItemTalkCondition inventoryItem=(ItemTalkCondition)condition;
      handleItemTalkCondition(sb,inventoryItem);
    }
    else if (condition instanceof FactionLevelCondition)
    {
      FactionLevelCondition factionLevel=(FactionLevelCondition)condition;
      handleFactionLevelCondition(sb,factionLevel);
    }
    else if (condition instanceof SkillUsedCondition)
    {
      SkillUsedCondition skillUsed=(SkillUsedCondition)condition;
      handleSkillUsedCondition(sb,skillUsed);
    }
    else if (condition instanceof NpcTalkCondition)
    {
      NpcTalkCondition npcTalk=(NpcTalkCondition)condition;
      handleNpcTalkCondition(sb,npcTalk);
    }
    else if (condition instanceof NpcUsedCondition)
    {
      NpcUsedCondition npcUsed=(NpcUsedCondition)condition;
      handleNpcUsedCondition(sb,npcUsed);
    }
    else if (condition instanceof LevelCondition)
    {
      LevelCondition level=(LevelCondition)condition;
      handleLevelCondition(sb,level);
    }
    else if (condition instanceof QuestBestowedCondition)
    {
      QuestBestowedCondition questBestowed=(QuestBestowedCondition)condition;
      handleQuestBestowedCondition(sb,questBestowed);
    }
    else if (condition instanceof DetectingCondition)
    {
      DetectingCondition detecting=(DetectingCondition)condition;
      handleDetectingCondition(sb,detecting);
    }
    else if (condition instanceof EnterDetectionCondition)
    {
      EnterDetectionCondition enterDetection=(EnterDetectionCondition)condition;
      handleEnterDetectionCondition(sb,enterDetection);
    }
    else if (condition instanceof EmoteCondition)
    {
      EmoteCondition emoteCondition=(EmoteCondition)condition;
      handleEmoteCondition(sb,emoteCondition);
    }
    else if (condition instanceof DefaultObjectiveCondition)
    {
      DefaultObjectiveCondition defaultCondition=(DefaultObjectiveCondition)condition;
      handleDefaultCondition(sb,defaultCondition);
    }
  }

  private static void handleQuestCompleteCondition(StringBuilder sb, QuestCompleteCondition questComplete)
  {
    int count=questComplete.getCompletionCount();
    boolean hasProgressOverride=printProgressOverrideWithCount(sb,questComplete,count);
    if (!hasProgressOverride)
    {
      String questCategory=questComplete.getQuestCategory();
      sb.append("<p>");
      Proxy<Achievable> proxy=questComplete.getProxy();
      if (proxy!=null)
      {
        String link=getAchievableLink(proxy);
        sb.append("Complete ").append(link);
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
    printLoreInfo(sb,questComplete);
  }

  private static void handleMonsterDiedCondition(StringBuilder sb, MonsterDiedCondition monsterDied)
  {
    int count=monsterDied.getCount();
    boolean hasProgressOverride=printProgressOverrideWithCount(sb,monsterDied,count);
    if (!hasProgressOverride)
    {
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
    printLoreInfo(sb,monsterDied);
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
    handleItemCondition(sb,condition,"Get");
  }

  private static void handleItemUsedCondition(StringBuilder sb, ItemUsedCondition condition)
  {
    handleItemCondition(sb,condition,"Use");
  }

  private static void handleExternalInventoryItemCondition(StringBuilder sb, ExternalInventoryItemCondition condition)
  {
    handleItemCondition(sb,condition,"Obtain");
  }

  private static void handleItemTalkCondition(StringBuilder sb, ItemTalkCondition condition)
  {
    handleItemCondition(sb,condition,"Use");
  }

  private static void handleItemCondition(StringBuilder sb, ItemCondition condition, String verb)
  {
    int count=condition.getCount();
    boolean hasProgressOverride=printProgressOverrideWithCount(sb,condition,count);
    if (!hasProgressOverride)
    {
      Proxy<Item> itemProxy=condition.getProxy();
      if (itemProxy!=null)
      {
        sb.append("<p>");
        String name=itemProxy.getName();
        sb.append(verb).append(' ').append(name);
        if (count>1)
        {
          sb.append(" x").append(count);
        }
        sb.append("</p>");
      }
    }
    printLoreInfo(sb,condition);
  }

  private static void handleFactionLevelCondition(StringBuilder sb, FactionLevelCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      Proxy<Faction> factionProxy=condition.getProxy();
      int tier=condition.getTier();
      if (factionProxy!=null)
      {
        sb.append("<p>");
        String name=factionProxy.getName();
        String levelName="?";
        Faction faction=factionProxy.getObject();
        if (faction!=null)
        {
          FactionLevel level=faction.getLevelByTier(tier);
          levelName=(level!=null)?level.getName():levelName;
        }
        sb.append("Reach reputation '").append(levelName);
        sb.append("' (tier ").append(tier).append(") with ").append(name);
        sb.append("</p>");
      }
    }
    printLoreInfo(sb,condition);
  }

  private static void handleSkillUsedCondition(StringBuilder sb, SkillUsedCondition condition)
  {
    int count=condition.getCount();
    boolean hasProgressOverride=printProgressOverrideWithCount(sb,condition,count);
    if (!hasProgressOverride)
    {
      Proxy<SkillDescription> skillProxy=condition.getProxy();
      if (skillProxy!=null)
      {
        sb.append("<p>");
        String name=skillProxy.getName();
        sb.append("Use skill ").append(name);
        if (count>1)
        {
          sb.append(" x").append(count);
        }
        Integer maxPerDay=condition.getMaxPerDay();
        if (maxPerDay!=null)
        {
          sb.append(" (max ").append(maxPerDay).append("/day)");
        }
        sb.append("</p>");
      }
      else
      {
        sb.append("<p>No skill and no progress override</p>");
      }
    }
    printLoreInfo(sb,condition);
  }

  private static void handleNpcTalkCondition(StringBuilder sb, NpcTalkCondition condition)
  {
    handleNpcCondition(sb,condition);
  }

  private static void handleNpcUsedCondition(StringBuilder sb, NpcUsedCondition condition)
  {
    handleNpcCondition(sb,condition);
  }

  private static void handleNpcCondition(StringBuilder sb, NpcCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      Proxy<NpcDescription> npcProxy=condition.getProxy();
      if (npcProxy!=null)
      {
        sb.append("<p>");
        String name=npcProxy.getName();
        String action=condition.getAction();
        sb.append(action).append(' ').append(name);
        sb.append("</p>");
      }
      else
      {
        sb.append("<p>No NPC and no progress override</p>");
      }
    }
    printLoreInfo(sb,condition);
  }

  private static void handleLevelCondition(StringBuilder sb, LevelCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      int level=condition.getLevel();
      sb.append("<p>");
      sb.append("Reach level ").append(level);
      sb.append("</p>");
    }
    printLoreInfo(sb,condition);
  }

  private static void handleQuestBestowedCondition(StringBuilder sb, QuestBestowedCondition questBestowed)
  {
    boolean hasProgressOverride=printProgressOverride(sb,questBestowed);
    if (!hasProgressOverride)
    {
      sb.append("<p>");
      Proxy<Achievable> proxy=questBestowed.getProxy();
      if (proxy!=null)
      {
        String link=getAchievableLink(proxy);
        sb.append("Have ").append(link).append(" bestowed");
      }
      sb.append("</p>");
    }
    printLoreInfo(sb,questBestowed);
  }

  private static void handleDetectingCondition(StringBuilder sb, DetectingCondition condition)
  {
    handleDetectionCondition(sb,condition);
  }

  private static void handleEnterDetectionCondition(StringBuilder sb, EnterDetectionCondition condition)
  {
    handleDetectionCondition(sb,condition);
  }

  private static void handleDetectionCondition(StringBuilder sb, DetectionCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      String target=getTarget(condition.getTarget());
      if (target!=null)
      {
        sb.append("<p>");
        sb.append("Go near ").append(target);
        sb.append("</p>");
      }
      else
      {
        LOGGER.warn("No NPC, no mob and no progress override");
      }
    }
    printLoreInfo(sb,condition);
  }

  private static void handleEmoteCondition(StringBuilder sb, EmoteCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      sb.append("<p>");
      Proxy<EmoteDescription> emote=condition.getProxy();
      String command=emote.getName();
      sb.append("Perform emote ").append(command);
      int count=condition.getCount();
      if (count>1)
      {
        sb.append(" x").append(count);
      }
      Integer maxDaily=condition.getMaxDaily();
      if (maxDaily!=null)
      {
        sb.append(" (max ").append(maxDaily).append("/day");
      }
      ConditionTarget target=condition.getTarget();
      String targetLabel=getTarget(target);
      if (targetLabel!=null)
      {
        sb.append(" on ");
        sb.append(targetLabel);
      }
      sb.append("</p>");
    }
    printLoreInfo(sb,condition);
  }

  private static String getTarget(ConditionTarget target)
  {
    String ret=null;
    if (target!=null)
    {
      Proxy<NpcDescription> npcProxy=target.getNpcProxy();
      Proxy<MobDescription> mobProxy=target.getMobProxy();
      if (npcProxy!=null)
      {
        ret=npcProxy.getName();
      }
      else if (mobProxy!=null)
      {
        ret=mobProxy.getName();
      }
    }
    return ret;
  }

  private static String getAchievableLink(Proxy<Achievable> proxy)
  {
    StringBuilder sb=new StringBuilder();
    Achievable achievable=proxy.getObject();
    if (achievable!=null)
    {
      boolean isQuest=(achievable instanceof QuestDescription);
      String type=isQuest?"quest":"deed";
      sb.append(type).append(" <b>");
      String text=achievable.getName();
      PageIdentifier to=ReferenceConstants.getAchievableReference(achievable);
      String toStr=to.getFullAddress();
      HtmlUtils.printLink(sb,toStr,text);
      sb.append("</b>");
    }
    else
    {
      LOGGER.warn("Could not resolve deed/quest ID="+proxy.getId()+", name="+proxy.getName());
      sb.append("quest/deed "+proxy.getId());
    }
    return sb.toString();
  }

  /**
   * Counters.
   */
  public static Map<ConditionType,IntegerHolder> _counters=new HashMap<ConditionType,IntegerHolder>();

  private static void handleDefaultCondition(StringBuilder sb, DefaultObjectiveCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      sb.append("<p>Missing data!!</p>");
      ConditionType type=condition.getType();
      IntegerHolder counter=_counters.get(type);
      if (counter==null)
      {
        counter=new IntegerHolder();
        _counters.put(type,counter);
      }
      counter.increment();
    }
    printLoreInfo(sb,condition);
  }

  private static boolean hasProgressOverride(ObjectiveCondition condition)
  {
    String progressOverride=condition.getProgressOverride();
    return ((progressOverride!=null) && (progressOverride.length()>0));
  }

  private static boolean printProgressOverrideWithCount(StringBuilder sb, ObjectiveCondition condition, int count)
  {
    String progressOverride=condition.getProgressOverride();
    if ((progressOverride!=null) && (progressOverride.length()>0))
    {
      if (progressOverride.contains(COUNT_PATTERN))
      {
        String countStr="0/"+count;
        progressOverride=progressOverride.replace(COUNT_PATTERN,countStr);
      }
      else
      {
        if (count>1)
        {
          progressOverride=progressOverride+" (x"+count+")";
        }
      }
      sb.append("<p>").append(HtmlUtils.toHtml(progressOverride)).append("</p>");
      return true;
    }
    return false;
  }

  private static boolean printProgressOverride(StringBuilder sb, ObjectiveCondition condition)
  {
    String progressOverride=condition.getProgressOverride();
    if ((progressOverride!=null) && (progressOverride.length()>0))
    {
      sb.append("<p>").append(HtmlUtils.toHtml(progressOverride)).append("</p>");
      return true;
    }
    return false;
  }

  private static void printLoreInfo(StringBuilder sb, ObjectiveCondition condition)
  {
    String loreInfo=condition.getLoreInfo();
    if ((loreInfo!=null) && (loreInfo.length()>0))
    {
      sb.append("<p><i>").append(HtmlUtils.toHtml(loreInfo)).append("</i></p>");
    }
  }
}
