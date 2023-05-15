package delta.games.lotro.gui.character.status.achievables.aggregator;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.io.DeedsStatusIo;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.deeds.filters.DeedFilter;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Test class for the achievables geo status aggregator.
 * @author DAM
 */
public class MainTestAggregatedGeoItemsMapWindow
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CharactersManager charsMgr=CharactersManager.getInstance();
    CharacterFile toon=charsMgr.getToonById("Landroval","Meva");
    AchievablesStatusManager deedsStatusMgr=DeedsStatusIo.load(toon);
    List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
    DeedFilter filter=new DeedFilter();
    DeedType deedType=LotroEnumsRegistry.getInstance().get(DeedType.class).getByKey("EXPLORER");
    filter.getTypeFilter().setDeedType(deedType);
    List<Achievable> deedsToUse=new ArrayList<Achievable>();
    for(DeedDescription deed : deeds)
    {
      if (filter.accept(deed))
      {
        deedsToUse.add(deed);
      }
    }
    AggregatedGeoItemsMapWindowController ctrl=new AggregatedGeoItemsMapWindowController(null,deedsStatusMgr);
    ctrl.setAchievables(deedsToUse);
    ctrl.show();
  }
}
