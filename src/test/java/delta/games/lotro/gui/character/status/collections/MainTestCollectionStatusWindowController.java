package delta.games.lotro.gui.character.status.collections;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.status.collections.CollectionStatus;
import delta.games.lotro.character.status.collections.CollectionsStatusBuilder;
import delta.games.lotro.character.status.collections.CollectionsStatusManager;

/**
 * Test class for the collection status window.
 * @author DAM
 */
public class MainTestCollectionStatusWindowController
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Meva");
    CollectionsStatusManager mgr=new CollectionsStatusBuilder().build(toon,null);
    for(CollectionStatus collectionStatus : mgr.getAll())
    {
      //if (collectionStatus.getCompletedCount()>0)
      {
        CollectionStatusWindowController window=new CollectionStatusWindowController(null,collectionStatus);
        window.show();
      }
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestCollectionStatusWindowController().doIt();
  }
}
