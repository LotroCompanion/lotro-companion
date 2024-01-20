package delta.games.lotro.gui.lore.traits.form;

import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;

/**
 * Base class for trait prerequisite panel controllers.
 * @author DAM
 */
public abstract class AbstractTraitPrerequisitePanelController extends AbstractPanelController
{
  /**
   * Constructor.
   * @param parent Parent window.
   */
  protected AbstractTraitPrerequisitePanelController(WindowController parent)
  {
    super(parent);
  }
}
