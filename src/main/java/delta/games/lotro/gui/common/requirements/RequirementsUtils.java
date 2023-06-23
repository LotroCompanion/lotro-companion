package delta.games.lotro.gui.common.requirements;

import java.util.List;

import delta.common.ui.swing.area.AreaController;
import delta.games.lotro.character.classes.AbstractClassDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.enums.CraftTier;
import delta.games.lotro.common.requirements.ClassRequirement;
import delta.games.lotro.common.requirements.FactionRequirement;
import delta.games.lotro.common.requirements.ProfessionRequirement;
import delta.games.lotro.common.requirements.QuestRequirement;
import delta.games.lotro.common.requirements.RaceRequirement;
import delta.games.lotro.common.requirements.UsageRequirement;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Utility methods related to requirements.
 * @author DAM
 */
public class RequirementsUtils
{
  /**
   * Build a requirement string.
   * @param requirements Requirements to use.
   * @return A string, empty if no requirement.
   */
  public static String buildRequirementString(UsageRequirement requirements)
  {
    return buildRequirementString(null,requirements);
  }

  /**
   * Build a requirement string.
   * @param controller Parent controller.
   * @param requirements Requirements to use.
   * @return A string, empty if no requirement.
   */
  public static String buildRequirementString(AreaController controller, UsageRequirement requirements)
  {
    StringBuilder sb=new StringBuilder();
    // Class
    ClassRequirement classRequirements=requirements.getClassRequirement();
    if (classRequirements!=null)
    {
      if (sb.length()>0) sb.append(", ");
      List<AbstractClassDescription> classes=classRequirements.getAllowedClasses();
      for(int i=0;i<classes.size();i++)
      {
        if (i>0)
        {
          sb.append('/');
        }
        sb.append(classes.get(i).getName());
      }
    }
    // Race
    RaceRequirement raceRequirements=requirements.getRaceRequirement();
    if (raceRequirements!=null)
    {
      if (sb.length()>0) sb.append(", ");
      List<RaceDescription> races=raceRequirements.getAllowedRaces();
      for(int i=0;i<races.size();i++)
      {
        if (i>0)
        {
          sb.append('/');
        }
        sb.append(races.get(i).getName());
      }
    }
    // Minimum level
    Integer minLevel=requirements.getMinLevel();
    if (minLevel!=null)
    {
      if (sb.length()>0) sb.append(", ");
      if (minLevel.intValue()==1000)
      {
        sb.append("level cap");
      }
      else
      {
        sb.append("level>=").append(minLevel);
      }
    }
    // Maximum level
    Integer maxLevel=requirements.getMaxLevel();
    if (maxLevel!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("level<=").append(maxLevel);
    }
    // Faction
    FactionRequirement factionReq=requirements.getFactionRequirement();
    if (factionReq!=null)
    {
      if (sb.length()>0) sb.append(", ");
      String factionRequirementLabel=getFactionRequirementLabel(controller,factionReq);
      sb.append(factionRequirementLabel);
    }
    // Quest
    QuestRequirement questReq=requirements.getQuestRequirement();
    if (questReq!=null)
    {
      int questId=questReq.getQuestId();
      QuestDescription quest=QuestsManager.getInstance().getQuest(questId);
      if (quest!=null)
      {
        if (sb.length()>0) sb.append(", ");
        sb.append("quest ").append(quest.getName()).append(' ').append(questReq.getQuestStatus());
      }
      else
      {
        DeedDescription deed=DeedsManager.getInstance().getDeed(questId);
        if (deed!=null)
        {
          if (sb.length()>0) sb.append(", ");
          sb.append("deed ").append(deed.getName()).append(' ').append(questReq.getQuestStatus());
        }
      }
    }
    // Profession
    ProfessionRequirement professionReq=requirements.getProfessionRequirement();
    if (professionReq!=null)
    {
      Profession profession=professionReq.getProfession();
      CraftTier tier=professionReq.getTier();
      if (sb.length()>0) sb.append(", ");
      sb.append(profession.getName());
      if (tier!=null)
      {
        sb.append('/');
        sb.append(tier.getLabel());
      }
    }
    String ret=sb.toString().trim();
    return ret;
  }

  private static String getFactionRequirementLabel(AreaController controller, FactionRequirement factionReq)
  {
    Faction faction=factionReq.getFaction();
    if (faction!=null)
    {
      int tier=factionReq.getTier();
      FactionLevel level=faction.getLevelByTier(tier);
      if (level!=null)
      {
        String rawTierName=level.getName();
        String tierName=ContextRendering.render(controller,rawTierName);
        String rawFactionName=faction.getName();
        String factionName=ContextRendering.render(controller,rawFactionName);
        return factionName+":"+tierName;
      }
    }
    return "";
  }
}
