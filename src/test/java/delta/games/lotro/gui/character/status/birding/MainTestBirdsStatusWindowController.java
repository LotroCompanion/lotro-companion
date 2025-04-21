package delta.games.lotro.gui.character.status.birding;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Test class for the birds status window.
 * @author DAM
 */
public class MainTestBirdsStatusWindowController
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Meva");
    BirdingStatusWindowController window=new BirdingStatusWindowController(null,toon);
    window.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestBirdsStatusWindowController().doIt();
  }
}
