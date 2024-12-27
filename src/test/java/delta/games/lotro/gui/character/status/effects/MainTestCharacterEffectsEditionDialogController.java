package delta.games.lotro.gui.character.status.effects;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.effects.CharacterEffectsManager;

/**
 * Test class for the character effects edition window.
 * @author DAM
 */
class MainTestCharacterEffectsEditionDialogController
{
  /**
   * Main method for this test.
   * @param args
   */
  public static void main(String[] args)
  {
    CharacterFile toon=new LotroTestUtils().getMainToon();
    CharacterData current=toon.getInfosManager().getCurrentData();
    CharacterEffectsManager effectsMgr=current.getEffects();
    System.out.println("Input: "+effectsMgr);
    CharacterEffectsManager result=CharacterEffectsEditionDialogController.editEffects(null,effectsMgr);
    System.out.println("Result: "+result);
  }
}
