package delta.games.lotro.quests;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.character.log.CharacterLogsManager;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.quests.io.web.QuestPageParser;
import delta.games.lotro.quests.io.xml.QuestXMLWriter;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Batch quest parsing from MyLotro.
 * @author DAM
 */
public class MainTestBatchQuestsParsing
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    _logger.setLevel(Level.INFO);
    CharacterFile toon=new LotroTestUtils().getMainToon();
    //CharacterFile toon=new LotroTestUtils().getToonByName("Alphael");
    CharacterLogsManager logManager=new CharacterLogsManager(toon);
    CharacterLog log=logManager.getLastLog();
    if (log!=null)
    {
      File rootDir=toon.getRootDir();
      File questsDir=new File(rootDir,"quests");
      questsDir.mkdirs();
      QuestPageParser parser=new QuestPageParser();
      int nb=log.getNbItems();
      for(int i=0;i<nb;i++)
      {
        CharacterLogItem item=log.getLogItem(i);
        if (item.getLogItemType()==LogItemType.QUEST)
        {
          String url=item.getAssociatedUrl();
          String label=item.getLabel();
          if (label.contains(":"))
          {
          _logger.info("Item "+(i+1)+"/"+nb+": "+item.getLabel());
          QuestDescription q=parser.parseQuestPage(url);
          if (q!=null)
          {
            //System.out.println(q.dump());
            QuestXMLWriter writer=new QuestXMLWriter();
            String name=q.getTitle();
            if ((name==null) || (name.length()>0))
            {
              File out=new File(questsDir,name+".xml");
              if (!out.exists())
              {
                boolean ok=writer.write(out,q,EncodingNames.UTF_8);
                if (!ok)
                {
                  _logger.error("Write failed for ["+q.getTitle()+"]");
                }
              }
            }
            else
            {
              _logger.error("Name is empty!");
            }
          }
          else
          {
            _logger.error("Cannot parse quest ["+item.getLabel()+"]");
          }
          }
        }
      }
    }
  }
}
