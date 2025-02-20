package delta.games.lotro.gui.character.status.housing;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;

/**
 * Test class for the character housing status window.
 * @author DAM
 */
class MainTestCharacterHousingStatusWindowController
{
  private void doIt()
  {
    CharacterFile character=new LotroTestUtils().getMainToon();
    CharacterHousingStatusWindowController ctrl=new CharacterHousingStatusWindowController(null,character);
    ctrl.show();
  }

  public static void main(String[] args)
  {
    new MainTestCharacterHousingStatusWindowController().doIt();
  }
}
