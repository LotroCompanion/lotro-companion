package delta.games.lotro.gui.character.status.collections;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.gui.character.status.collections.CollectablesStatusWindowController.STATUS_TYPE;

/**
 * Test class for the collection status window.
 * @author DAM
 */
public class MainTestCollectablesStatusWindowController
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Meva");
    CollectablesStatusWindowController mountsWindow=new CollectablesStatusWindowController(null,toon,STATUS_TYPE.MOUNTS);
    mountsWindow.show();
    CollectablesStatusWindowController petsWindow=new CollectablesStatusWindowController(null,toon,STATUS_TYPE.PETS);
    petsWindow.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestCollectablesStatusWindowController().doIt();
  }
}
