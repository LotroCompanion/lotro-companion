package delta.games.lotro.tools.lore.quests;

import java.io.File;

import delta.common.utils.files.filter.ExtensionPredicate;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.index.QuestsIndex;
import delta.games.lotro.lore.quests.index.io.xml.QuestsIndexWriter;
import delta.games.lotro.lore.quests.io.xml.QuestXMLParser;

/**
 * Builds a quests index for a series of quest definition files. 
 * @author DAM
 */
public class QuestsIndexBuilder
{
  private File _questsDir;
  private File _indexFile;

  /**
   * Constructor.
   * @param questsDir Quests directory. 
   * @param indexFile Index file.
   */
  public QuestsIndexBuilder(File questsDir, File indexFile)
  {
    _questsDir=questsDir;
    _indexFile=indexFile;
  }

  /**
   * Do build quests index.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt()
  {
    boolean ret=false;
    if (_questsDir.exists())
    {
      QuestsIndex index=new QuestsIndex();
      ExtensionPredicate extFilter=new ExtensionPredicate(".xml");
      File[] questFiles=_questsDir.listFiles(extFilter);
      if (questFiles!=null)
      {
        QuestXMLParser parser=new QuestXMLParser();
        for(File questFile : questFiles)
        {
          QuestDescription q=parser.parseXML(questFile);
          String category=q.getCategory();
          String key=q.getKey();
          String title=q.getTitle();
          int id=q.getIdentifier();
          index.addQuest(category,id,key,title);
        }
        QuestsIndexWriter writer=new QuestsIndexWriter();
        ret=writer.write(_indexFile,index,EncodingNames.UTF_8);
      }
    }
    return ret;
  }
}
