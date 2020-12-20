package delta.games.lotro.gui.stats.deeds.form;

import delta.common.ui.swing.checkbox.ThreeState;
import delta.common.ui.swing.checkbox.ThreeStateCheckbox;
import delta.games.lotro.character.achievables.AchievableElementState;

/**
 * Controller for the display/edition of the state of an achievable element.
 * @author DAM
 */
public class AchievableElementStateEditionController
{
  private ThreeStateCheckbox _ctrl;

  /**
   * Constructor.
   */
  public AchievableElementStateEditionController()
  {
    _ctrl=new ThreeStateCheckbox();
  }

  /**
   * Get the managed UI component.
   * @return the managed UI component.
   */
  public ThreeStateCheckbox getComponent()
  {
    return _ctrl;
  }

  /**
   * Set the state to display.
   * @param state State to display.
   */
  public void setState(AchievableElementState state)
  {
    if (state==AchievableElementState.COMPLETED)
    {
      _ctrl.setState(ThreeState.SELECTED);
    }
    else if (state==AchievableElementState.UNDERWAY)
    {
      _ctrl.setState(ThreeState.HALF_SELECTED);
    }
    else if (state==AchievableElementState.UNDEFINED)
    {
      _ctrl.setState(ThreeState.NOT_SELECTED);
    }
  }

  /**
   * Get the currently displayed state.
   * @return the currently displayed state.
   */
  public AchievableElementState getState()
  {
    ThreeState state=_ctrl.getState();
    if (state==ThreeState.SELECTED) return AchievableElementState.COMPLETED;
    if (state==ThreeState.HALF_SELECTED) return AchievableElementState.UNDERWAY;
    if (state==ThreeState.NOT_SELECTED) return AchievableElementState.UNDEFINED;
    return null;
  }
}
