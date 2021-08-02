package delta.games.lotro.gui.character.status.achievables.form;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.TransparentIcon;
import delta.games.lotro.character.status.achievables.AchievableElementState;

/**
 * Controller for the display/edition of the state of an achievable element.
 * @author DAM
 */
public class AchievableElementStateEditionController
{
  private JButton _button;
  private JLabel _label;
  private Icon _icon;
  private Icon _grayedIcon;
  private Icon _transparentIcon;

  /**
   * Constructor.
   * @param icon Icon to use.
   * @param config UI configuration.
   */
  public AchievableElementStateEditionController(Icon icon, AchievableFormConfig config)
  {
    _icon=icon;
    _grayedIcon=UIManager.getLookAndFeel().getDisabledIcon(null, icon);
    _transparentIcon=new TransparentIcon(icon,0.4f);
    if (config.isEditable())
    {
      _button=GuiFactory.buildIconButton();
      _button.setIcon(icon);
    }
    else
    {
      _label=GuiFactory.buildIconLabel(icon);
    }
  }

  /**
   * Get the managed UI component.
   * @return the managed UI component.
   */
  public JComponent getComponent()
  {
    return (_button!=null)?_button:_label;
  }

  /**
   * Get the managed button.
   * @return A button or <code>null</code> if not editable.
   */
  public JButton getButton()
  {
    return _button;
  }

  /**
   * Set the state to display.
   * @param state State to display.
   */
  public void setState(AchievableElementState state)
  {
    Icon iconToSet=null;
    if (state==AchievableElementState.COMPLETED)
    {
      iconToSet=_icon;
    }
    else if (state==AchievableElementState.UNDERWAY)
    {
      iconToSet=_transparentIcon;
    }
    else if (state==AchievableElementState.UNDEFINED)
    {
      iconToSet=_grayedIcon;
    }
    if (_button!=null)
    {
      _button.setIcon(iconToSet);
    }
    if (_label!=null)
    {
      _label.setIcon(iconToSet);
    }
  }


  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _button=null;
    _label=null;
    _icon=null;
    _grayedIcon=null;
    _transparentIcon=null;
  }
}
