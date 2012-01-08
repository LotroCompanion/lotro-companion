package delta.games.lotro.quests;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.Config;
import delta.games.lotro.quests.index.QuestsIndex;
import delta.games.lotro.quests.index.io.xml.QuestsIndexXMLParser;
import delta.games.lotro.quests.io.web.QuestPageParser;
import delta.games.lotro.quests.io.xml.QuestXMLParser;
import delta.games.lotro.quests.io.xml.QuestXMLWriter;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Facade for quests access.
 * @author DAM
 */
public class QuestsManager
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static QuestsManager _instance=new QuestsManager();
  
  private QuestsIndex _index;
  private HashMap<String,QuestDescription> _cache;
  
  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static QuestsManager getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private QuestsManager()
  {
    //_cache=new HashMap<String,QuestDescription>();
    loadIndex();
  }

  /**
   * Get the quests index.
   * @return the quests index.
   */
  public QuestsIndex getIndex()
  {
    return _index;
  }

  /**
   * Get a quest using its identifier.
   * @param id Quest identifier.
   * @return A quest description or <code>null</code> if not found.
   */
  public QuestDescription getQuest(String id)
  {
    QuestDescription ret=null;
    if ((id!=null) && (id.length()>0))
    {
      ret=(_cache!=null)?_cache.get(id):null;
      if (ret==null)
      {
        ret=loadQuest(id);
        if (ret!=null)
        {
          if (_cache!=null)
          {
            _cache.put(id,ret);
          }
        }
      }
    }
    return ret;
  }

  private QuestDescription loadQuest(String id)
  {
    QuestDescription ret=null;
    File questFile=getQuestFile(id);
    if (!questFile.exists())
    {
      QuestPageParser parser=new QuestPageParser();
      String url=urlFromIdentifier(id);
      ret=parser.parseQuestPage(url);
      if (ret!=null)
      {
        QuestXMLWriter writer=new QuestXMLWriter();
        if (!questFile.getParentFile().exists())
        {
          questFile.getParentFile().mkdirs();
        }
        boolean ok=writer.write(questFile,ret,EncodingNames.UTF_8);
        if (!ok)
        {
          _logger.error("Write failed for quest ["+ret.getTitle()+"]!");
        }
      }
      else
      {
        _logger.error("Cannot parse quest ["+id+"] at URL ["+url+"]!");
      }
    }
    else
    {
      QuestXMLParser parser=new QuestXMLParser();
      ret=parser.parseXML(questFile);
      if (ret==null)
      {
        _logger.error("Cannot load quest file ["+questFile+"]!");
      }
    }
    return ret;
  }

  private File getQuestFile(String id)
  {
    File questsDir=Config.getInstance().getQuestsDir();
    String fileName=fileNameFromIdentifier(id);
    File ret=new File(questsDir,fileName);
    return ret;
  }

  private String urlFromIdentifier(String id)
  {
    id=id.replace("?","%3F");
    String url="http://lorebook.lotro.com/wiki/Quest:"+id;
    return url;
  }

  private String fileNameFromIdentifier(String id)
  {
    String filename=id+".xml";
    filename=filename.replace(":","%3A");
    filename=filename.replace("'","%27");
    filename=filename.replace("â","%C3%A2");
    filename=filename.replace("ä","%C3%A4");
    filename=filename.replace("Â","%C3%82");
    filename=filename.replace("Á","%C3%81");
    filename=filename.replace("ë","%C3%AB");
    filename=filename.replace("é","%C3%A9");
    filename=filename.replace("í","%C3%AD");
    filename=filename.replace("î","%C3%AE");
    filename=filename.replace("ó","%C3%B3");
    filename=filename.replace("û","%C3%BB");
    filename=filename.replace("ú","%C3%BA");
    filename=filename.replace("?","%3F");
    
    return filename;
  }

  private void loadIndex()
  {
    File questsDir=Config.getInstance().getQuestsDir();
    File questIndexFile=new File(questsDir,"questsIndex.xml");
    QuestsIndexXMLParser parser=new QuestsIndexXMLParser();
    _index=parser.parseXML(questIndexFile);
  }
}
