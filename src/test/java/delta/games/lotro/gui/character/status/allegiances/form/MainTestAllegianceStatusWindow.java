package delta.games.lotro.gui.character.status.allegiances.form;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.status.allegiances.AllegianceStatus;
import delta.games.lotro.character.status.allegiances.AllegiancesStatusManager;
import delta.games.lotro.character.status.allegiances.io.AllegiancesStatusIo;
import delta.games.lotro.lore.allegiances.AllegianceDescription;
import delta.games.lotro.lore.allegiances.AllegiancesManager;

/**
 * Test class for the allegiance status window.
 * @author DAM
 */
public class MainTestAllegianceStatusWindow
{
  private void doIt()
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Lorewyne");
    AllegiancesStatusManager allegiancesStatusMgr=AllegiancesStatusIo.load(toon);
    for(AllegianceDescription allegiance : AllegiancesManager.getInstance().getAll())
    {
      AllegianceStatus status=allegiancesStatusMgr.get(allegiance,true);
      AllegianceStatusWindowController ctrl=new AllegianceStatusWindowController(null,status);
      ctrl.show(false);
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestAllegianceStatusWindow().doIt();
  }
}
