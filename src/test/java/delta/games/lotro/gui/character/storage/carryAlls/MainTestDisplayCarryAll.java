package delta.games.lotro.gui.character.storage.carryAlls;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.StoragesIO;
import delta.games.lotro.character.storage.carryAlls.CarryAllInstance;

/**
 * Simple test class for the carry-all display panel controller.
 * @author DAM
 */
public class MainTestDisplayCarryAll
{
  private void doIt()
  {
    CharactersManager mgr=CharactersManager.getInstance();
    CharacterFile toon=mgr.getToonById("Landroval","Kargarth");
    CharacterStorage characterStorage=StoragesIO.loadCharacterStorage(toon);
    for(CarryAllInstance carryAll : characterStorage.getCarryAlls(true))
    {
      CarryAllWindowController ctrl=new CarryAllWindowController(null,carryAll);
      ctrl.show();
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestDisplayCarryAll().doIt();
  }
}
