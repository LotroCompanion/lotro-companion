package delta.games.lotro.region;

import java.util.Arrays;

import delta.games.lotro.region.io.web.AreaPageParser;
import delta.games.lotro.region.io.web.RegionPageParser;

/**
 * Test for region description parsing.
 * @author DAM
 */
public class MainTestRegionParsing
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    RegionPageParser regionParser=new RegionPageParser();
    Region shire=regionParser.parseRegionPage("The_Shire");
    if (shire!=null)
    {
      AreaPageParser areaParser=new AreaPageParser();
      System.out.println(shire.dump());
      Area[] areas=shire.getAreas();
      for(Area area : areas)
      {
        String identifier=area.getIdentifier();
        Area a=areaParser.parseAreaPage(identifier);
        System.out.println(a.dump());
        String[] questIdentifiers=a.getQuestIdentifiers();
        System.out.println(Arrays.deepToString(questIdentifiers));
      }
    }
  }
}
