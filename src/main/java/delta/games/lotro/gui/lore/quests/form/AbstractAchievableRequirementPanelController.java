package delta.games.lotro.gui.lore.quests.form;

import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.achievables.QuestRequirementStateComputer;

/**
 * Base class for achievable requirement panel controllers.
 * @author DAM
 */
public abstract class AbstractAchievableRequirementPanelController extends AbstractPanelController
{
  protected QuestRequirementStateComputer _stateComputer;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param computer State computer.
   */
  protected AbstractAchievableRequirementPanelController(WindowController parent, QuestRequirementStateComputer computer)
  {
    super(parent);
    _stateComputer=computer;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _stateComputer=null;
  }
}
