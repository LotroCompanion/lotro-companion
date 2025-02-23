package delta.games.lotro.gui.character.status.effects;

import delta.games.lotro.character.status.effects.EffectInstance;
import delta.games.lotro.gui.character.status.effects.EffectInstanceEditionPanelController.MODE;

/**
 * Test class for the effect instance edition window.
 * @author DAM
 */
class MainTestEffectInstanceEditionWindowController
{
  /**
   * Main method for this test.
   * @param args
   */
  public static void main(String[] args)
  {
    EffectInstance e=new EffectInstance();
    System.out.println("Input: "+e);
    EffectInstanceEditionWindowController w=new EffectInstanceEditionWindowController(null,e,MODE.CREATION);
    EffectInstance result=w.editModal();
    System.out.println("Result: "+result);
  }
}
