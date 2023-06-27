package delta.games.lotro.gui.lore.quests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.utils.misc.IntegerHolder;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.common.Interactable;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.agents.AgentDescription;
import delta.games.lotro.lore.agents.EntityClassification;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.geo.LandmarkDescription;
import delta.games.lotro.lore.items.Item;
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
import delta.games.lotro.utils.gui.TextSanitizer;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Build for HTML code to display objectives.
 * @author DAM
 */
public class ObjectivesDisplayBuilder
{
  private static final Logger LOGGER=Logger.getLogger(ObjectivesDisplayBuilder.class);

  private static final String COUNT_PATTERN="{***}/{***}";

  private boolean _html;
  private AreaController _controller;

  /**
   * Constructor.
   * @param controller Parent controller.
   * @param html Use HTML output, or raw output.
   */
  public ObjectivesDisplayBuilder(AreaController controller, boolean html)
  {
    _controller=controller;
    _html=html;
  }

  /**
   * Print HTML code for the objectives of an achievable.
   * @param sb Output.
   * @param achievable Achievable to display.
   */
  public void build(StringBuilder sb, Achievable achievable)
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
      String text=objective.getDescription();
      if (text.length()>0)
      {
        sb.append("<p>").append(HtmlUtils.toHtml(text)).append("</p>");
      }
      // Conditions
      for(ObjectiveCondition condition : objective.getConditions())
      {
        sb.append(getConditionDisplay(condition,isDeed));
        printLoreInfo(sb,condition);
      }
      // Dialogs
      for(DialogElement dialog : objective.getDialogs())
      {
        QuestsHtmlUtils.buildHtmlForDialog(sb,dialog);
      }
    }
  }

  /**
   * Get a displayable string for an objective condition.
   * @param condition Condition to use.
   * @param isDeed Is it part of a deed (or of a quest).
   * @return A displayable string (HTML or raw).
   */
  public String getConditionDisplay(ObjectiveCondition condition, boolean isDeed)
  {
    StringBuilder sb=new StringBuilder();
    handleCondition(sb,condition,isDeed);
    if (sb.length()>0)
    {
      StringBuilder sb2=new StringBuilder();
      if (_html)
      {
        sb2.append("<p>");
      }
      sb2.append(sb);
      if (_html)
      {
        sb2.append("</p>");
      }
      return sb2.toString();
    }
    return "";
  }

  private void handleCondition(StringBuilder sb, ObjectiveCondition condition, boolean isDeed)
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

  private void handleQuestCompleteCondition(StringBuilder sb, QuestCompleteCondition questComplete)
  {
    int count=questComplete.getCount();
    String progressOverride=getProgressOverrideWithCount(questComplete,count);
    Proxy<Achievable> proxy=questComplete.getProxy();
    String questCategory=questComplete.getQuestCategory();

    if (progressOverride==null)
    {
      if (proxy!=null)
      {
        String link=getAchievableLink(proxy,null);
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
    }
    else
    {
      if (proxy!=null)
      {
        String link=getAchievableLink(proxy,progressOverride);
        sb.append(link);
      }
      else
      {
        sb.append(HtmlUtils.toHtml(progressOverride));
      }
    }
  }

  private void handleMonsterDiedCondition(StringBuilder sb, MonsterDiedCondition monsterDied)
  {
    int count=monsterDied.getCount();
    boolean hasProgressOverride=printProgressOverrideWithCount(sb,monsterDied,count);
    if (!hasProgressOverride)
    {
      String mobName=monsterDied.getMobName();
      List<MobSelection> mobSelections=monsterDied.getMobSelections();
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
            EntityClassification what=mobSelection.getWhat();
            String where=mobSelection.getWhere();
            String whatStr=(what!=null)?what.getLabel():"Mob";
            if (index>0)
            {
              sb.append(" or ");
            }
            sb.append(whatStr).append(" in ").append(where);
            index++;
          }
        }
      }
      if (count>1)
      {
        sb.append(" (x").append(count).append(')');
      }
    }
  }

  private void handleLandmarkDetectionCondition(StringBuilder sb, LandmarkDetectionCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      Proxy<LandmarkDescription> landmark=condition.getLandmarkProxy();
      if (landmark!=null)
      {
        String name=landmark.getName();
        sb.append("Find ").append(name);
      }
    }
  }

  private void handleInventoryItemCondition(StringBuilder sb, InventoryItemCondition condition)
  {
    handleItemCondition(sb,condition,"Get");
  }

  private void handleItemUsedCondition(StringBuilder sb, ItemUsedCondition condition)
  {
    handleItemCondition(sb,condition,"Use");
  }

  private void handleExternalInventoryItemCondition(StringBuilder sb, ExternalInventoryItemCondition condition)
  {
    handleItemCondition(sb,condition,"Obtain");
  }

  private void handleItemTalkCondition(StringBuilder sb, ItemTalkCondition condition)
  {
    handleItemCondition(sb,condition,"Use");
  }

  private void handleItemCondition(StringBuilder sb, ItemCondition condition, String verb)
  {
    int count=condition.getCount();
    boolean hasProgressOverride=printProgressOverrideWithCount(sb,condition,count);
    if (!hasProgressOverride)
    {
      Item item=condition.getItem();
      if (item!=null)
      {
        sb.append(verb).append(' ');
        String name=item.getName();
        if (_html)
        {
          PageIdentifier to=ReferenceConstants.getItemReference(item.getIdentifier());
          String toStr=to.getFullAddress();
          HtmlUtils.printLink(sb,toStr,name);
        }
        else
        {
          sb.append(name);
        }
        if (count>1)
        {
          sb.append(" x").append(count);
        }
      }
    }
  }

  private void handleFactionLevelCondition(StringBuilder sb, FactionLevelCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      Faction faction=condition.getFaction();
      int tier=condition.getTier();
      if (faction!=null)
      {
        String name=faction.getName();
        String levelName="?";
        FactionLevel level=faction.getLevelByTier(tier);
        if (level!=null)
        {
          levelName=ContextRendering.render(_controller,level.getName());
        }
        sb.append("Reach reputation '").append(levelName);
        sb.append("' (tier ").append(tier).append(") with ").append(name);
      }
    }
  }

  private void handleSkillUsedCondition(StringBuilder sb, SkillUsedCondition condition)
  {
    int count=condition.getCount();
    boolean hasProgressOverride=printProgressOverrideWithCount(sb,condition,count);
    if (!hasProgressOverride)
    {
      SkillDescription skill=condition.getSkill();
      if (skill!=null)
      {
        String name=skill.getName();
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
      }
      else
      {
        sb.append("No skill and no progress override");
      }
    }
  }

  private void handleNpcTalkCondition(StringBuilder sb, NpcTalkCondition condition)
  {
    handleNpcCondition(sb,condition);
  }

  private void handleNpcUsedCondition(StringBuilder sb, NpcUsedCondition condition)
  {
    handleNpcCondition(sb,condition);
  }

  private void handleNpcCondition(StringBuilder sb, NpcCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      Interactable npc=condition.getNpc();
      if (npc!=null)
      {
        String name=npc.getName();
        String action=condition.getAction();
        sb.append(action).append(' ').append(name);
      }
      else
      {
        LOGGER.warn("No NPC and no progress override");
        sb.append("No NPC and no progress override");
      }
    }
  }

  private void handleLevelCondition(StringBuilder sb, LevelCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      int level=condition.getLevel();
      sb.append("Reach level ").append(level);
    }
  }

  private void handleQuestBestowedCondition(StringBuilder sb, QuestBestowedCondition questBestowed)
  {
    boolean hasProgressOverride=printProgressOverride(sb,questBestowed);
    if (!hasProgressOverride)
    {
      Proxy<Achievable> proxy=questBestowed.getProxy();
      if (proxy!=null)
      {
        String link=getAchievableLink(proxy,null);
        sb.append("Have ").append(link).append(" bestowed");
      }
    }
  }

  private void handleDetectingCondition(StringBuilder sb, DetectingCondition condition)
  {
    handleDetectionCondition(sb,condition);
  }

  private void handleEnterDetectionCondition(StringBuilder sb, EnterDetectionCondition condition)
  {
    handleDetectionCondition(sb,condition);
  }

  private void handleDetectionCondition(StringBuilder sb, DetectionCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      String target=getTarget(condition.getTarget());
      if (target!=null)
      {
        sb.append("Go near ").append(target);
      }
      else
      {
        LOGGER.warn("No NPC, no mob and no progress override");
      }
    }
  }

  private void handleEmoteCondition(StringBuilder sb, EmoteCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      EmoteDescription emote=condition.getEmote();
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
    }
  }

  private static String getTarget(ConditionTarget target)
  {
    String ret=null;
    if (target!=null)
    {
      AgentDescription agent=target.getAgent();
      if (agent!=null)
      {
        ret=agent.getName();
      }
    }
    return ret;
  }

  private String getAchievableLink(Proxy<Achievable> proxy, String text)
  {
    StringBuilder sb=new StringBuilder();
    Achievable achievable=proxy.getObject();
    if (achievable!=null)
    {
      if (text==null)
      {
        boolean isQuest=(achievable instanceof QuestDescription);
        String type=isQuest?"quest ":"deed ";
        sb.append(type);
        text=achievable.getName();
      }
      if (_html)
      {
        sb.append("<b>");
        PageIdentifier to=ReferenceConstants.getAchievableReference(achievable);
        HtmlUtils.printLink(sb,to.getFullAddress(),text);
        sb.append("</b>");
      }
      else
      {
        sb.append(text);
      }
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

  private void handleDefaultCondition(StringBuilder sb, DefaultObjectiveCondition condition)
  {
    boolean hasProgressOverride=printProgressOverride(sb,condition);
    if (!hasProgressOverride)
    {
      sb.append("Missing data!!");
      ConditionType type=condition.getType();
      IntegerHolder counter=_counters.get(type);
      if (counter==null)
      {
        counter=new IntegerHolder();
        _counters.put(type,counter);
      }
      counter.increment();
    }
  }

  private static boolean hasProgressOverride(ObjectiveCondition condition)
  {
    String progressOverride=condition.getProgressOverride();
    return ((progressOverride!=null) && (progressOverride.length()>0));
  }

  private boolean printProgressOverrideWithCount(StringBuilder sb, ObjectiveCondition condition, int count)
  {
    String progressOverride=getProgressOverrideWithCount(condition,count);
    if (progressOverride!=null)
    {
      if (_html)
      {
        sb.append(HtmlUtils.toHtml(progressOverride));
      }
      else
      {
        sb.append(progressOverride);
      }
      return true;
    }
    return false;
  }

  private String getProgressOverrideWithCount(ObjectiveCondition condition, int count)
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
    }
    return progressOverride;
  }

  private boolean printProgressOverride(StringBuilder sb, ObjectiveCondition condition)
  {
    String progressOverride=condition.getProgressOverride();
    if ((progressOverride!=null) && (progressOverride.length()>0))
    {
      if (_html)
      {
        sb.append(HtmlUtils.toHtml(progressOverride));
      }
      else
      {
        progressOverride=TextSanitizer.sanitize(progressOverride);
        sb.append(progressOverride);
      }
      return true;
    }
    return false;
  }

  private void printLoreInfo(StringBuilder sb, ObjectiveCondition condition)
  {
    String loreInfo=condition.getLoreInfo();
    if ((loreInfo!=null) && (loreInfo.trim().length()>0))
    {
      if (_html)
      {
        sb.append("<p><i>").append(HtmlUtils.toHtml(loreInfo)).append("</i></p>");
      }
      else
      {
        sb.append(loreInfo);
      }
    }
  }
}
