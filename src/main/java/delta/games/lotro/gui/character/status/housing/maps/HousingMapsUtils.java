package delta.games.lotro.gui.character.status.housing.maps;

import delta.games.lotro.lore.maps.Dungeon;
import delta.games.lotro.lore.maps.DungeonsManager;
import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;

/**
 * Utility methods related to housing UI.
 * @author DAM
 */
public class HousingMapsUtils
{
  /**
   * Build a map description for the given zone/area.
   * @param zoneID Zone identifier (dungeon or area).
   * @return A map description or <code>null</code> if not found.
   */
  public static MapDescription buildMapDescription(int zoneID)
  {
    DungeonsManager dungeonsMgr=DungeonsManager.getInstance();
    Dungeon dungeon=dungeonsMgr.getDungeonById(zoneID);
    if (dungeon!=null)
    {
      MapDescription ret=new MapDescription();
      ret.setMapId(Integer.valueOf(dungeon.getIdentifier()));
      return ret;
    }
    // Area => parchment map
    ParchmentMapsManager parchmentMapsMgr=ParchmentMapsManager.getInstance();
    ParchmentMap map=parchmentMapsMgr.getParchmentMapForArea(zoneID);
    if (map!=null)
    {
      MapDescription ret=new MapDescription();
      ret.setMapId(Integer.valueOf(map.getIdentifier()));
      ret.setRegion(map.getRegion());
      return ret;
    }
    return null;
  }
}
