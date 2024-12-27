package delta.games.lotro.gui.common.effects;

import javax.swing.Icon;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.IconController;

/**
 * Controller for an effect icon.
 * @author DAM
 */
public class EffectIconController extends IconController
{
  private Effect _effect;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param effect Trait to use.
   * @param useNavigation Use navigation or not.
   */
  public EffectIconController(WindowController parent, Effect effect, boolean useNavigation)
  {
    super(parent,useNavigation);
    setEffect(effect);
  }

  /**
   * Get the managed effect.
   * @return the managed effect.
   */
  public Effect getEffect()
  {
    return _effect;
  }

  /**
   * Set the managed effect.
   * @param effect Effect to set or <code>null</code>.
   */
  public void setEffect(Effect effect)
  {
    _effect=effect;
    Icon icon=EffectIconUtils.buildEffectIcon(effect);
    setIcon(icon);
    if (effect!=null)
    {
      setPageId(ReferenceConstants.getTraitReference(effect.getIdentifier()));
    }
    else
    {
      setPageId(null);
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _effect=null;
  }
}
