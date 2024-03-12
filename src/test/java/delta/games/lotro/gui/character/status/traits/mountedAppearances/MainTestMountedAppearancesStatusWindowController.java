package delta.games.lotro.gui.character.status.traits.mountedAppearances;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Test class for the mounted appearances status window.
 * @author DAM
 */
public class MainTestMountedAppearancesStatusWindowController
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Glumlug");
    MountedAppearancesStatusWindowController window=new MountedAppearancesStatusWindowController(null,toon);
    window.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestMountedAppearancesStatusWindowController().doIt();
  }
}
