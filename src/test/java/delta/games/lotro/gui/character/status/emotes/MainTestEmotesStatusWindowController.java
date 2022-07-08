package delta.games.lotro.gui.character.status.emotes;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Test class for the emotes status window.
 * @author DAM
 */
public class MainTestEmotesStatusWindowController
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Meva");
    EmotesStatusWindowController window=new EmotesStatusWindowController(null,toon);
    window.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestEmotesStatusWindowController().doIt();
  }
}
