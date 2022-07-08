package delta.games.lotro.gui.character.status.emotes;

import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.emotes.EmotesManager;

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
    List<EmoteDescription> emotes=EmotesManager.getInstance().getAll();
    EmotesStatusWindowController window=new EmotesStatusWindowController(null,emotes,toon);
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
