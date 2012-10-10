package delta.games.lotro.tools.lore.quests;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.files.FilesDeleter;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.index.QuestCategory;
import delta.games.lotro.lore.quests.index.QuestSummary;
import delta.games.lotro.lore.quests.index.QuestsIndex;
import delta.games.lotro.lore.quests.index.io.xml.QuestsIndexXMLParser;
import delta.games.lotro.lore.quests.io.web.QuestPageParser;
import delta.games.lotro.lore.quests.io.xml.QuestXMLWriter;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Quests loader.
 * @author DAM
 */
public class QuestsLoader
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();
  
  private File _questsDir;
  private File _indexFile;
  private QuestsIndex _index;
  private int _totalNbQuestsToLoad;
  private int _totalNbQuestsLoaded;

  /**
   * Constructor.
   * @param questsDir Directory to write quests to.
   * @param indexFile Index file.
   */
  public QuestsLoader(File questsDir, File indexFile)
  {
    _questsDir=questsDir;
    _indexFile=indexFile;
  }

  /**
   * Do load quests.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt()
  {
    boolean ok=init();
    if (!ok)
    {
      return false;
    }
    loadQuests();
    System.out.println("Quests loaded: "+_totalNbQuestsLoaded+"/"+_totalNbQuestsToLoad+".");
    return true;
  }

  private boolean init()
  {
    // Build work directory
    if (_questsDir.exists())
    {
      FilesDeleter deleter=new FilesDeleter(_questsDir,null,true);
      deleter.doIt();
    }
    boolean ret=_questsDir.mkdirs();
    if (!ret)
    {
      _logger.error("Cannot empty work directory ["+_questsDir+"]!");
    }
    // Load quests index
    QuestsIndexXMLParser parser=new QuestsIndexXMLParser();
    _index=parser.parseXML(_indexFile);
    if (_index==null)
    {
      _logger.error("Cannot read quests index!");
    }
    ret=(_index!=null);
    return ret;
  }

  private void loadQuests()
  {
    String[] categories=_index.getCategories();
    for(String category : categories)
    {
      loadQuestsForCategory(category);
    }
  }

  private void loadQuestsForCategory(String categoryName)
  {
    System.out.println("Category ["+categoryName+"]");
    QuestCategory category=_index.getCategory(categoryName);
    QuestSummary[] summaries=category.getQuests();
    int nbQuestsToLoad=summaries.length;
    int nbQuestsLoaded=0;
    for(String key : category.getKeys())
    {
      int nbQuests=loadQuestsDefinition(key);
      nbQuestsLoaded+=nbQuests;
    }
    _totalNbQuestsToLoad+=nbQuestsToLoad;
    _totalNbQuestsLoaded+=nbQuestsLoaded;
    System.out.println("Category ["+categoryName+"]: "+nbQuestsLoaded+"/"+nbQuestsToLoad+".");
  }

  /**
   * Load quest definitions.
   * @param key Quest key.
   * @return Number of loaded quests.
   */
  private int loadQuestsDefinition(String key)
  {
    int nbQuests=0;
    QuestPageParser parser=new QuestPageParser();
    String url=urlFromIdentifier(key);
    List<QuestDescription> quests=parser.parseQuestPage(url);
    if ((quests!=null) && (quests.size()>0))
    {
      QuestXMLWriter writer=new QuestXMLWriter();
      for(QuestDescription quest : quests)
      {
        int id=quest.getIdentifier();
        String fileName=String.valueOf(id)+".xml";
        File questFile=new File(_questsDir,fileName);
        if (!questFile.getParentFile().exists())
        {
          questFile.getParentFile().mkdirs();
        }
        boolean ok=writer.write(questFile,quest,EncodingNames.UTF_8);
        if (ok)
        {
          nbQuests++;
        }
        else
        {
          String title=quest.getTitle();
          _logger.error("Write failed for quest ["+title+"]!");
        }
      }
    }
    else
    {
      _logger.error("Cannot parse quests ["+key+"] at URL ["+url+"]!");
    }
    return nbQuests;
  }

  private String urlFromIdentifier(String id)
  {
    id=id.replace("?","%3F");
    String url="http://lorebook.lotro.com/wiki/Quest:"+id;
    return url;
  }
}
