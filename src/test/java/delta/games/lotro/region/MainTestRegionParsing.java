package delta.games.lotro.region;

import java.io.File;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.io.web.QuestPageParser;
import delta.games.lotro.lore.quests.io.xml.QuestXMLWriter;
import delta.games.lotro.region.io.web.AreaPageParser;
import delta.games.lotro.region.io.web.RegionPageParser;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Test for region description parsing.
 * @author DAM
 */
public class MainTestRegionParsing
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    QuestPageParser questParser=new QuestPageParser();
    RegionPageParser regionParser=new RegionPageParser();
    Region shire=regionParser.parseRegionPage("The_Shire");
    File toDir=new File("/home/dm/lotro/quests/The_Shire");
    toDir.mkdirs();
    QuestXMLWriter writer=new QuestXMLWriter();
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
        for(String questIdentifier : questIdentifiers)
        {
          String url="http://lorebook.lotro.com/wiki/Quest:"+questIdentifier;
          QuestDescription q=questParser.parseQuestPage(url);
          if (q!=null)
          {
            String title=q.getTitle();
            System.out.println(title);
            File to=new File(toDir,questIdentifier+".xml");
            if (!to.exists())
            {
              boolean ok=writer.write(to,q,EncodingNames.UTF_8);
              if (!ok)
              {
                _logger.error("Write failed for ["+q.getTitle()+"]");
              }
            }
          }
          else
          {
            _logger.error("Cannot get quest ["+questIdentifier+"]!");
          }
        }
      }
    }
  }
}
