package delta.games.lotro.gui.lore.traits;

import javax.swing.Icon;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.IconController;

/**
 * Controller for a trait icon.
 * @author DAM
 */
public class TraitIconController extends IconController
{
  private TraitDescription _trait;
  private int _level;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param trait Trait to use.
   * @param level Character level.
   * @param useNavigation Use navigation or not.
   */
  public TraitIconController(WindowController parent, TraitDescription trait, int level, boolean useNavigation)
  {
    super(parent,useNavigation);
    _level=level;
    setTrait(trait);
  }

  /**
   * Get the managed trait.
   * @return the managed trait.
   */
  public TraitDescription getTrait()
  {
    return _trait;
  }

  /**
   * Set the managed trait.
   * @param trait Trait to set or <code>null</code>.
   */
  public void setTrait(TraitDescription trait)
  {
    _trait=trait;
    Icon icon=TraitIconUtils.buildTraitIcon(trait);
    setIcon(icon);
    if (trait!=null)
    {
      setPageId(ReferenceConstants.getTraitReference(trait.getIdentifier()));
      String tooltip=TraitIconUtils.buildToolTip(trait,_level);
      setTooltipText(tooltip);
    }
    else
    {
      setPageId(null);
      setTooltipText("");
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _trait=null;
  }
}
