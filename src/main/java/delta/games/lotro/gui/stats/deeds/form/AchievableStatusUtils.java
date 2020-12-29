package delta.games.lotro.gui.stats.deeds.form;

import delta.games.lotro.gui.quests.ObjectivesDisplayBuilder;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;

/**
 * Utility methods related to achievable status display/edition.
 * @author DAM
 */
public class AchievableStatusUtils
{
  private static final ObjectivesDisplayBuilder builder=new ObjectivesDisplayBuilder(false);

  /**
   * Get the label to use for a condition of an objective.
   * @param condition Condition to use.
   * @return A displayable label.
   */
  public static String getConditionLabel(ObjectiveCondition condition)
  {
    String label=condition.getProgressOverride();
    if ((label==null) || (label.length()==0))
    {
      label=builder.getConditionDisplay(condition,true);
    }
    return label;
  }
}
