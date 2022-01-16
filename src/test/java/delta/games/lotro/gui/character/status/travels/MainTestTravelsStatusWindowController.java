package delta.games.lotro.gui.character.status.travels;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Test class for the travels status window.
 * @author DAM
 */
public class MainTestTravelsStatusWindowController
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Glumlug");
    TravelsStatusWindowController window=new TravelsStatusWindowController(null,toon);
    window.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestTravelsStatusWindowController().doIt();
  }
}
