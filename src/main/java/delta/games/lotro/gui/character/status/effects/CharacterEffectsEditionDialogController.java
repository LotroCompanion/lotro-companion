package delta.games.lotro.gui.character.status.effects;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.effects.CharacterEffectsManager;
import delta.games.lotro.character.status.effects.EffectInstance;

/**
 * Controller for the character effects edition dialog.
 * @author DAM
 */
public class CharacterEffectsEditionDialogController extends DefaultFormDialogController<List<EffectInstance>>
{
  private CharacterEffectsEditionPanelController _edtionPanel;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param effects Data to edit.
   */
  public CharacterEffectsEditionDialogController(WindowController parentController, List<EffectInstance> effects)
  {
    super(parentController,effects);
    _edtionPanel=new CharacterEffectsEditionPanelController(this,effects);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Effects edition"); // I18n
    dialog.setResizable(true);
    dialog.setMinimumSize(new Dimension(700,650));
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=_edtionPanel.getPanel();
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_edtionPanel!=null)
    {
      _edtionPanel.dispose();
      _edtionPanel=null;
    }
  }

  /**
   * Edit character effects.
   * @param parent Parent controller.
   * @param effectsMgr Effects to edit.
   * @return The edited effects or <code>null</code> if the window was closed or canceled.
   */
  public static CharacterEffectsManager editEffects(WindowController parent, CharacterEffectsManager effectsMgr)
  {
    List<EffectInstance> effects=effectsMgr.getEffects();
    CharacterEffectsEditionDialogController controller=new CharacterEffectsEditionDialogController(parent,effects);
    effects=controller.editModal();
    if (effects==null)
    {
      return null;
    }
    effectsMgr.setEffects(effects);
    return effectsMgr;
  }
}
