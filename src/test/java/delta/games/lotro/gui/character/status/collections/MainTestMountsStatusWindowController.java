package delta.games.lotro.gui.character.status.collections;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Test class for the collection status window.
 * @author DAM
 */
public class MainTestMountsStatusWindowController
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Meva");
    MountsStatusWindowController window=new MountsStatusWindowController(null,toon);
    window.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestMountsStatusWindowController().doIt();
  }
}
