package delta.games.lotro.gui.character.storage.bags;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.bags.BagsManager;
import delta.games.lotro.character.storage.bags.BagsSetup;
import delta.games.lotro.character.storage.bags.SingleBagSetup;
import delta.games.lotro.character.storage.bags.io.BagsIo;

/**
 * Simple test class for the bag display panel controller.
 * @author DAM
 */
public class MainTestDisplaySingleBag
{
  private void doIt()
  {
    CharactersManager mgr=CharactersManager.getInstance();
    CharacterFile toon=mgr.getToonById("Landroval","Meva");
    BagsManager bagsMgr=BagsIo.load(toon);
    BagsSetup setup=bagsMgr.getBagsSetup();
    for(Integer index : setup.getBagIndexes())
    {
      SingleBagSetup bagSetup=setup.getBagSetup(index.intValue());
      int size=bagSetup.getSize();
      if (size==0)
      {
        continue;
      }
      BagWindowController ctrl=new BagWindowController(null,bagsMgr,index.intValue());
      ctrl.show();
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestDisplaySingleBag().doIt();
  }
}
