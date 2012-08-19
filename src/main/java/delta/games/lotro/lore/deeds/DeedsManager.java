package delta.games.lotro.lore.deeds;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.Config;
import delta.games.lotro.lore.deeds.index.DeedsIndex;
import delta.games.lotro.lore.deeds.index.io.xml.DeedsIndexXMLParser;
import delta.games.lotro.lore.deeds.io.web.DeedPageParser;
import delta.games.lotro.lore.deeds.io.xml.DeedXMLParser;
import delta.games.lotro.lore.deeds.io.xml.DeedXMLWriter;
import delta.games.lotro.utils.LotroLoggers;
import delta.games.lotro.utils.resources.ResourcesMapping;
import delta.games.lotro.utils.resources.io.xml.ResourcesMappingXMLParser;
import delta.games.lotro.utils.resources.io.xml.ResourcesMappingXMLWriter;

/**
 * Facade for deeds access.
 * @author DAM
 */
public class DeedsManager
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static DeedsManager _instance=new DeedsManager();
  
  private DeedsIndex _index;
  private ResourcesMapping _mapping;
  private HashMap<String,DeedDescription> _cache;
  
  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static DeedsManager getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private DeedsManager()
  {
    //_cache=new HashMap<String,DeedDescription>();
    loadIndex();
    loadResourcesMapping();
  }

  /**
   * Get the deeds index.
   * @return the deeds index.
   */
  public DeedsIndex getIndex()
  {
    return _index;
  }

  /**
   * Get the deed resources mapping.
   * @return the deed resources mapping.
   */
  public ResourcesMapping getDeedResourcesMapping()
  {
    return _mapping;
  }

  /**
   * Get a deed using its identifier.
   * @param id Deed identifier.
   * @return A deed description or <code>null</code> if not found.
   */
  public DeedDescription getDeed(String id)
  {
    DeedDescription ret=null;
    if ((id!=null) && (id.length()>0))
    {
      ret=(_cache!=null)?_cache.get(id):null;
      if (ret==null)
      {
        ret=loadDeed(id);
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
   * Update the deed resources mapping file.
   */
  public void updateDeedResourcesMapping()
  {
    File ressourcesMappingFile=getDeedResourcesMappingFile();
    ResourcesMappingXMLWriter writer=new ResourcesMappingXMLWriter();
    writer.write(ressourcesMappingFile,_mapping,EncodingNames.ISO8859_1);
  }

  private DeedDescription loadDeed(String id)
  {
    DeedDescription ret=null;
    File deedFile=getDeedFile(id);
    if (!deedFile.exists())
    {
      DeedPageParser parser=new DeedPageParser();
      String url=urlFromIdentifier(id);
      ret=parser.parseDeedPage(url);
      if (ret!=null)
      {
        DeedXMLWriter writer=new DeedXMLWriter();
        if (!deedFile.getParentFile().exists())
        {
          deedFile.getParentFile().mkdirs();
        }
        boolean ok=writer.write(deedFile,ret,EncodingNames.UTF_8);
        if (!ok)
        {
          _logger.error("Write failed for deed ["+ret.getName()+"]!");
        }
      }
      else
      {
        _logger.error("Cannot parse deed ["+id+"] at URL ["+url+"]!");
        try
        {
          deedFile.createNewFile();
        }
        catch(IOException ioe)
        {
          _logger.error("Cannot create new file ["+deedFile+"]",ioe);
        }
      }
    }
    else
    {
      if (deedFile.length()>0)
      {
        DeedXMLParser parser=new DeedXMLParser();
        ret=parser.parseXML(deedFile);
        if (ret==null)
        {
          _logger.error("Cannot load deed file ["+deedFile+"]!");
        }
      }
    }
    return ret;
  }

  private File getDeedFile(String id)
  {
    File deedsDir=Config.getInstance().getDeedsDir();
    String fileName=fileNameFromIdentifier(id);
    File ret=new File(deedsDir,fileName);
    return ret;
  }

  private String urlFromIdentifier(String id)
  {
    id=id.replace("?","%3F");
    String url="http://lorebook.lotro.com/wiki/Deed:"+id;
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
    File deedsDir=Config.getInstance().getDeedsDir();
    File deedIndexFile=new File(deedsDir,"deedsIndex.xml");
    DeedsIndexXMLParser parser=new DeedsIndexXMLParser();
    _index=parser.parseXML(deedIndexFile);
  }

  private void loadResourcesMapping()
  {
    File ressourcesMappingFile=getDeedResourcesMappingFile();
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

  private File getDeedResourcesMappingFile()
  {
    File questsDir=Config.getInstance().getDeedsDir();
    File ressourcesMappingFile=new File(questsDir,"deedResourcesMapping.xml");
    return ressourcesMappingFile;
  }
}
