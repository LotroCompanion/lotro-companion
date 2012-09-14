package delta.games.lotro.lore.quests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.Config;
import delta.games.lotro.lore.quests.index.QuestsIndex;
import delta.games.lotro.lore.quests.index.io.xml.QuestsIndexXMLParser;
import delta.games.lotro.lore.quests.io.web.QuestPageParser;
import delta.games.lotro.lore.quests.io.xml.QuestXMLParser;
import delta.games.lotro.lore.quests.io.xml.QuestXMLWriter;
import delta.games.lotro.utils.LotroLoggers;
import delta.games.lotro.utils.resources.ResourcesMapping;
import delta.games.lotro.utils.resources.io.xml.ResourcesMappingXMLParser;
import delta.games.lotro.utils.resources.io.xml.ResourcesMappingXMLWriter;

/**
 * Facade for quests access.
 * @author DAM
 */
public class QuestsManager
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static QuestsManager _instance=new QuestsManager();
  
  private QuestsIndex _index;
  private ResourcesMapping _mapping;
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
    loadResourcesMapping();
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
   * Get the quest resources mapping.
   * @return the quest resources mapping.
   */
  public ResourcesMapping getQuestResourcesMapping()
  {
    return _mapping;
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

  /**
   * Update the quest resources mapping file.
   */
  public void updateQuestResourcesMapping()
  {
    File ressourcesMappingFile=getQuestResourcesMappingFile();
    ResourcesMappingXMLWriter writer=new ResourcesMappingXMLWriter();
    writer.write(ressourcesMappingFile,_mapping,EncodingNames.ISO8859_1);
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
        try
        {
          questFile.createNewFile();
        }
        catch(IOException ioe)
        {
          _logger.error("Cannot create new file ["+questFile+"]",ioe);
        }
      }
    }
    else
    {
      if (questFile.length()>0)
      {
        QuestXMLParser parser=new QuestXMLParser();
        ret=parser.parseXML(questFile);
        if (ret==null)
        {
          _logger.error("Cannot load quest file ["+questFile+"]!");
        }
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
    filename=filename.replace("�","%C3%A2");
    filename=filename.replace("�","%C3%A4");
    filename=filename.replace("�","%C3%82");
    filename=filename.replace("�","%C3%81");
    filename=filename.replace("�","%C3%AB");
    filename=filename.replace("�","%C3%A9");
    filename=filename.replace("�","%C3%AD");
    filename=filename.replace("�","%C3%AE");
    filename=filename.replace("�","%C3%B3");
    filename=filename.replace("�","%C3%BB");
    filename=filename.replace("�","%C3%BA");
    filename=filename.replace("?","%3F");
    
    return filename;
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

  private void loadResourcesMapping()
  {
    File ressourcesMappingFile=getQuestResourcesMappingFile();
    if (ressourcesMappingFile.exists())
    {
      ResourcesMappingXMLParser parser=new ResourcesMappingXMLParser();
      _mapping=parser.parseXML(ressourcesMappingFile);
    }
    else
    {
      _mapping=new ResourcesMapping();
    }
  }

  private File getQuestResourcesMappingFile()
  {
    File dir=Config.getInstance().getIndexesDir();
    File ressourcesMappingFile=new File(dir,"questResourcesMapping.xml");
    return ressourcesMappingFile;
  }
}
