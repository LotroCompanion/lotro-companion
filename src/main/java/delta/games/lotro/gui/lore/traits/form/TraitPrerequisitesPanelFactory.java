package delta.games.lotro.gui.lore.traits.form;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.traits.prerequisites.AbstractTraitPrerequisite;
import delta.games.lotro.character.traits.prerequisites.CompoundTraitPrerequisite;
import delta.games.lotro.character.traits.prerequisites.SimpleTraitPrerequisite;

/**
 * Factory for trait prerequisites panel controllers.
 * @author DAM
 */
public class TraitPrerequisitesPanelFactory
{
  /**
   * Build a panel controller to show a trait prerequisite.
   * @param parent Parent window.
   * @param prerequisite Prerequisite to display.
   * @return A panel controller.
   */
  public static AbstractTraitPrerequisitePanelController buildTraitRequisitePanelController(WindowController parent, AbstractTraitPrerequisite prerequisite)
  {
    AbstractTraitPrerequisitePanelController ctrl=null;
    if (prerequisite instanceof SimpleTraitPrerequisite)
    {
      SimpleTraitPrerequisite simplePrerequisite=(SimpleTraitPrerequisite)prerequisite;
      ctrl=new SimpleTraitPrerequisitePanelController(parent,simplePrerequisite);
    }
    else if (prerequisite instanceof CompoundTraitPrerequisite)
    {
      CompoundTraitPrerequisite compoundPrerequisite=(CompoundTraitPrerequisite)prerequisite;
      ctrl=new CompoundTraitPrerequisitePanelController(parent,compoundPrerequisite);
    }
    return ctrl;
  }
}
