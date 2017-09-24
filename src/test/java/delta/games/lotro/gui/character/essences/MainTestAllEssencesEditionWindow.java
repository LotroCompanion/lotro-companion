package delta.games.lotro.gui.character.essences;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.CharacterGenerationTools;
import delta.games.lotro.character.stats.CharacterGeneratorMeva;

/**
 * Test for all essences edition panel.
 * @author DAM
 */
public class MainTestAllEssencesEditionWindow
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CharacterGenerationTools tools=new CharacterGenerationTools();
    CharacterGeneratorMeva mevaGenerator=new CharacterGeneratorMeva(tools);
    CharacterData meva=mevaGenerator.buildCharacter();
    meva.setDate(Long.valueOf(System.currentTimeMillis()));
    AllEssencesEditionWindowController ctrl=new AllEssencesEditionWindowController(null,meva);
    ctrl.show();
  }
}
