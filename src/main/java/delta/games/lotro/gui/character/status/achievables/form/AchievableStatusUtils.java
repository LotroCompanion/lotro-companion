package delta.games.lotro.gui.character.status.achievables.form;

import delta.common.ui.swing.area.AreaController;
import delta.common.utils.variables.VariablesResolver;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;
import delta.games.lotro.lore.quests.objectives.ObjectivesDisplayBuilder;
import delta.games.lotro.utils.strings.ContextRendering;
import delta.games.lotro.utils.strings.TextOutput;
import delta.games.lotro.utils.strings.TextSanitizer;

/**
 * Utility methods related to achievable status display/edition.
 * @author DAM
 */
public class AchievableStatusUtils
{
  private ObjectivesDisplayBuilder _builder;

  /**
   * Constructor.
   * @param controller Controller.
   */
  public AchievableStatusUtils(AreaController controller)
  {
    VariablesResolver resolver=ContextRendering.buildRenderer(controller);
    _builder=new ObjectivesDisplayBuilder(resolver,new TextOutput());
  }

  /**
   * Get the label to use for a condition of an objective.
   * @param condition Condition to use.
   * @return A displayable label.
   */
  public String getConditionLabel(ObjectiveCondition condition)
  {
    String label=condition.getProgressOverride();
    if ((label==null) || (label.length()==0))
    {
      label=_builder.getConditionDisplay(condition,true);
    }
    label=TextSanitizer.sanitize(label);
    return label;
  }
}
