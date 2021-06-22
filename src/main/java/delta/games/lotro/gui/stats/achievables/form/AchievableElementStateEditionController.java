package delta.games.lotro.gui.stats.achievables.form;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.TransparentIcon;
import delta.games.lotro.character.achievables.AchievableElementState;

/**
 * Controller for the display/edition of the state of an achievable element.
 * @author DAM
 */
public class AchievableElementStateEditionController
{
  private JButton _button;
  private Icon _icon;
  private Icon _grayedIcon;
  private Icon _transparentIcon;

  /**
   * Constructor.
   * @param icon Icon to use.
   */
  public AchievableElementStateEditionController(Icon icon)
  {
    _icon=icon;
    _grayedIcon=UIManager.getLookAndFeel().getDisabledIcon(null, icon);
    _transparentIcon=new TransparentIcon(icon,0.4f);
    _button=GuiFactory.buildIconButton();
    _button.setIcon(icon);
  }

  /**
   * Get the managed UI component.
   * @return the managed UI component.
   */
  public JButton getComponent()
  {
    return _button;
  }

  /**
   * Set the state to display.
   * @param state State to display.
   */
  public void setState(AchievableElementState state)
  {
    if (state==AchievableElementState.COMPLETED)
    {
      _button.setIcon(_icon);
    }
    else if (state==AchievableElementState.UNDERWAY)
    {
      _button.setIcon(_transparentIcon);
    }
    else if (state==AchievableElementState.UNDEFINED)
    {
      _button.setIcon(_grayedIcon);
    }
  }


  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _button=null;
    _icon=null;
    _grayedIcon=null;
    _transparentIcon=null;
  }
}
