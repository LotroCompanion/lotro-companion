package delta.games.lotro.gui.lore.quests.form;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.requirements.QuestRequirementStateComputer;
import delta.games.lotro.common.requirements.AbstractAchievableRequirement;
import delta.games.lotro.common.requirements.CompoundQuestRequirement;
import delta.games.lotro.common.requirements.QuestRequirement;

/**
 * Factory for achievable requirements panel controllers.
 * @author DAM
 */
public class AchievableRequirementsPanelFactory
{
  /**
   * Build a panel controller to show an achievable requirement.
   * @param parent Parent window.
   * @param computer State computer.
   * @param requirement Requirement to display.
   * @return A panel controller.
   */
  public static AbstractAchievableRequirementPanelController buildAchievableRequirementPanelController(WindowController parent, QuestRequirementStateComputer computer, AbstractAchievableRequirement requirement)
  {
    AbstractAchievableRequirementPanelController ctrl=null;
    if (requirement instanceof QuestRequirement)
    {
      QuestRequirement questRequirement=(QuestRequirement)requirement;
      ctrl=new SimpleAchievableRequirementPanelController(parent,computer,questRequirement);
    }
    else if (requirement instanceof CompoundQuestRequirement)
    {
      CompoundQuestRequirement questRequirement=(CompoundQuestRequirement)requirement;
      ctrl=new CompoundAchievableRequirementPanelController(parent,computer,questRequirement);
    }
    return ctrl;
  }
}
