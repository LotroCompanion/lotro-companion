package delta.games.lotro.gui.common.binding;

import javax.swing.JComponent;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;
import delta.games.lotro.character.BaseCharacterSummary;
import delta.games.lotro.common.binding.BindingsManager;
import delta.games.lotro.common.id.InternalGameId;

/**
 * Controller for a label to display a binding info.
 * @author DAM
 */
public class BindingDisplayController implements Disposable
{
  private BaseCharacterSummary _currentCharacter;
  private JLabel _label;

  /**
   * Constructor.
   * @param currentCharacter Current character (if any).
   */
  public BindingDisplayController(BaseCharacterSummary currentCharacter)
  {
    super();
    _currentCharacter=currentCharacter;
    _label=GuiFactory.buildLabel("");
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JComponent getComponent()
  {
    return _label;
  }

  /**
   * Set the binding to show.
   * @param id Identifier.
   */
  public void setBinding(InternalGameId id)
  {
    String text=BindingsManager.getInstance().getDisplayableBindingInfo(id,_currentCharacter);
    _label.setText(text);
  }

  @Override
  public void dispose()
  {
    _currentCharacter=null;
    _label=null;
  }
}
