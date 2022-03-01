package delta.games.lotro.gui.lore.quests.form;

import javax.swing.JPanel;

import delta.common.ui.swing.misc.Disposable;

/**
 * Base class for achievable requirement panel controllers.
 * @author DAM
 */
public abstract class AbstractAchievableRequirementPanelController implements Disposable
{
  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public abstract JPanel getPanel();
}
