package delta.games.lotro.items;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.Config;
import delta.games.lotro.items.io.ItemXMLParser;
import delta.games.lotro.items.io.web.ItemPageParser;
import delta.games.lotro.items.io.xml.ItemXMLWriter;
import delta.games.lotro.quests.io.web.MyLotroURL2Identifier;
import delta.games.lotro.utils.Escapes;
import delta.games.lotro.utils.LotroLoggers;
import delta.games.lotro.utils.resources.ResourcesMapping;
import delta.games.lotro.utils.resources.io.xml.ResourcesMappingXMLParser;
import delta.games.lotro.utils.resources.io.xml.ResourcesMappingXMLWriter;

/**
 * Facade for items access.
 * @author DAM
 */
public class ItemsManager
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();
  private static final String URL_SEED="http://lorebook.lotro.com/wiki/Special:LotroResource?id=";
  private static final String REAL_URL_SEED="http://lorebook.lotro.com/wiki/";

  private static ItemsManager _instance=new ItemsManager();
  
  private ResourcesMapping _mapping;
  private HashMap<String,Item> _cache;
  
  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static ItemsManager getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private ItemsManager()
  {
    _cache=new HashMap<String,Item>();
    loadResourcesMapping();
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
   * Get an item using its identifier.
   * @param id Item identifier.
   * @return An item description or <code>null</code> if not found.
   */
  public Item getItem(String id)
  {
    Item ret=null;
    if ((id!=null) && (id.length()>0))
    {
      ret=(_cache!=null)?_cache.get(id):null;
      if (ret==null)
      {
        ret=loadItem(id);
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
   * Extract item identifier from LOTRO resource URL.
   * @param url URL to use.
   * @return An item identifier or <code>null</code> if URL does not fit.
   */
  public String idFromURL(String url)
  {
    String ret=null;
    if ((url!=null) && (url.startsWith(URL_SEED)))
    {
      ret=url.substring(URL_SEED.length());
    }
    return ret;
  }

  private Item loadItem(String id)
  {
    Item ret=null;
    File itemFile=getItemFile(id);
    if (!itemFile.exists())
    {
      ItemPageParser parser=new ItemPageParser();
      String url=urlFromIdentifier(id);
      if (url!=null)
      {
        ret=parser.parseItemPage(url);
        if (ret!=null)
        {
          ItemXMLWriter writer=new ItemXMLWriter();
          if (!itemFile.getParentFile().exists())
          {
            itemFile.getParentFile().mkdirs();
          }
          boolean ok=writer.write(itemFile,ret,EncodingNames.UTF_8);
          if (!ok)
          {
            String name=ret.getName();
            _logger.error("Write failed for item ["+name+"]!");
          }
        }
        else
        {
          _logger.error("Cannot parse item ["+id+"] at URL ["+url+"]!");
        }
      }
      else
      {
        _logger.error("Cannot parse item ["+id+"]. URL is null!");
      }
    }
    else
    {
      ItemXMLParser parser=new ItemXMLParser();
      ret=parser.parseXML(itemFile);
      if (ret==null)
      {
        _logger.error("Cannot load item file ["+itemFile+"]!");
      }
    }
    return ret;
  }

  private File getItemFile(String id)
  {
    File itemsDir=Config.getInstance().getItemsDir();
    String fileName=id+".xml";
    File ret=new File(itemsDir,fileName);
    return ret;
  }

  private String urlFromIdentifier(String id)
  {
    String ret=null;
    String baseURL=URL_SEED+id;
    MyLotroURL2Identifier finder=new MyLotroURL2Identifier();
    id=finder.findIdentifier(baseURL,true);
    if (id!=null)
    {
      id=Escapes.escapeIdentifier(id);
      ret=REAL_URL_SEED+id;
    }
    return ret;
  }

  private File getResourcesMappingFile()
  {
    File itemsDir=Config.getInstance().getItemsDir();
    File ressourcesMappingFile=new File(itemsDir,"itemsResourcesMapping.xml");
    return ressourcesMappingFile;
  }

  /**
   * Update resources mapping file.
   */
  public void updateResourcesMapping()
  {
    File ressourcesMappingFile=getResourcesMappingFile();
    ResourcesMappingXMLWriter writer=new ResourcesMappingXMLWriter();
    writer.write(ressourcesMappingFile,_mapping,EncodingNames.ISO8859_1);
  }

  private void loadResourcesMapping()
  {
    File ressourcesMappingFile=getResourcesMappingFile();
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
}
