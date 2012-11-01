package delta.games.lotro.lore.quests;

import java.io.File;
import java.io.InputStream;

import org.apache.log4j.Logger;

import delta.common.utils.cache.WeakReferencesCache;
import delta.common.utils.files.archives.ArchiveManager;
import delta.games.lotro.Config;
import delta.games.lotro.lore.quests.index.QuestsIndex;
import delta.games.lotro.lore.quests.index.io.xml.QuestsIndexXMLParser;
import delta.games.lotro.lore.quests.io.xml.QuestXMLParser;
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
  private ArchiveManager _archive;
  private WeakReferencesCache<Integer,QuestDescription> _cache;
  
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
    _cache=new WeakReferencesCache<Integer,QuestDescription>(100);
    File loreDir=Config.getInstance().getLoreDir();
    File questsArchive=new File(loreDir,"quests.zip");
    _archive=new ArchiveManager(questsArchive);
    _archive.open();
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
  public QuestDescription getQuest(int id)
  {
    QuestDescription ret=null;
    if (id>0)
    {
      Integer idKey=Integer.valueOf(id);
      ret=(_cache!=null)?_cache.getObject(idKey):null;
      if (ret==null)
      {
        ret=loadQuest(id);
        if (ret!=null)
        {
          if (_cache!=null)
          {
            _cache.registerObject(idKey,ret);
          }
        }
      }
    }
    return ret;
  }

  private QuestDescription loadQuest(int id)
  {
    QuestDescription ret=null;
    String fileName=String.valueOf(id)+".xml";
    InputStream is=_archive.getEntry(fileName);
    if (is!=null)
    {
      QuestXMLParser parser=new QuestXMLParser();
      ret=parser.parseXML(is);
      if (ret==null)
      {
        _logger.error("Cannot load quest ["+fileName+"]!");
      }
    }
    return ret;
  }

  private void loadIndex()
  {
    File dir=Config.getInstance().getIndexesDir();
    File questIndexFile=new File(dir,"questsIndex.xml");
    if (questIndexFile.exists())
    {
      QuestsIndexXMLParser parser=new QuestsIndexXMLParser();
      _index=parser.parseXML(questIndexFile);
    }
    else
    {
      _index=new QuestsIndex();
    }
  }
}
