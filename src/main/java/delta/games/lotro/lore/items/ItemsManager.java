package delta.games.lotro.lore.items;

import java.io.File;
import java.io.IOException;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.common.utils.cache.WeakReferencesCache;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.Config;
import delta.games.lotro.lore.items.io.xml.ItemXMLParser;
import delta.games.lotro.lore.items.io.xml.ItemXMLWriter;
import delta.games.lotro.lore.items.io.xml.ItemsSetXMLParser;
import delta.games.lotro.lore.items.io.xml.ItemsSetXMLWriter;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Facade for items access.
 * @author DAM
 */
public class ItemsManager
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();
  private static final String URL_SEED="http://lorebook.lotro.com/wiki/Special:LotroResource?id=";
  //private static final String REAL_URL_SEED="http://lorebook.lotro.com/wiki/";

  private static ItemsManager _instance=new ItemsManager();
  
  private WeakReferencesCache<Integer,Item> _cache;
  private WeakHashMap<String,ItemsSet> _setsCache;
  
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
    _cache=new WeakReferencesCache<Integer,Item>(20);
    _setsCache=new WeakHashMap<String,ItemsSet>();
  }

  /**
   * Get an item using its identifier.
   * @param id Item identifier.
   * @return An item description or <code>null</code> if not found.
   */
  public Item getItem(Integer id)
  {
    Item ret=null;
    if (id!=null)
    {
      ret=(_cache!=null)?_cache.getObject(id):null;
      if (ret==null)
      {
        ret=loadItem(id.intValue());
        if (ret!=null)
        {
          if (_cache!=null)
          {
            _cache.registerObject(id,ret);
          }
        }
      }
    }
    return ret;
  }

  /**
   * Get a set of items using its identifier.
   * @param id Set of items identifier.
   * @return A description of this set of items or <code>null</code> if not found.
   */
  public ItemsSet getItemsSet(String id)
  {
    ItemsSet ret=null;
    if ((id!=null) && (id.length()>0))
    {
      ret=(_setsCache!=null)?_setsCache.get(id):null;
      if (ret==null)
      {
        ret=loadItemsSet(id);
        if (ret!=null)
        {
          if (_setsCache!=null)
          {
            _setsCache.put(id,ret);
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
  public Integer idFromURL(String url)
  {
    Integer ret=null;
    if ((url!=null) && (url.startsWith(URL_SEED)))
    {
      String idStr=url.substring(URL_SEED.length());
      ret=NumericTools.parseInteger(idStr,true);
    }
    return ret;
  }

  /*
  private Item loadFromMyLotro(int id)
  {
    Item ret=null;
    String url=urlFromIdentifier(id);
    if (url!=null)
    {
      ItemPageParser parser=new ItemPageParser();
      List<Item> items=parser.parseItemPage(url);
      if ((items!=null) && (items.size()>0))
      {
        for(Item item : items)
        {
          int itemId=item.getIdentifier();
          if (itemId==id)
          {
            ret=item;
            break;
          }
        }
      }
      if (ret==null)
      {
        _logger.error("Cannot parse item ["+id+"] at URL ["+url+"]!");
      }
    }
    else
    {
      _logger.error("Cannot parse item ["+id+"]. URL is null!");
    }
    return ret;
  }
  */

  private Item loadItem(int id)
  {
    Item ret=null;
    File itemFile=getItemFile(id);
    if (!itemFile.exists())
    {
      //ret=loadFromMyLotro(id);
      /*
      if (ret!=null)
      {
        ret=writeItemFile(ret);
      }
      else
      */
      {
        try
        {
          itemFile.createNewFile();
        }
        catch(IOException ioe)
        {
          _logger.error("Cannot create new file ["+itemFile+"]",ioe);
        }
      }
    }
    else
    {
      if (itemFile.length()>0)
      {
        ItemXMLParser parser=new ItemXMLParser();
        ret=parser.parseXML(itemFile);
        if (ret!=null)
        {
          ret.setIdentifier(id);
        }
        else
        {
          _logger.error("Cannot load item file ["+itemFile+"]!");
        }
      }
    }
    return ret;
  }

  /**
   * Write an item file.
   * @param item Item to write.
   * @return An item.
   */
  public Item writeItemFile(Item item)
  {
    ItemXMLWriter writer=new ItemXMLWriter();
    int id=item.getIdentifier();
    File itemFile=getItemFile(id);
    boolean ok=writer.write(itemFile,item,EncodingNames.UTF_8);
    if (!ok)
    {
      String name=item.getName();
      _logger.error("Write failed for item ["+name+"]!");
      item=null;
    }
    else
    {
      ItemsSet set=item.getSet();
      if (set!=null)
      {
        writeSetFile(set);
      }
      // Reload item to cleanup memory retention of web-loaded strings
      item=loadItem(id);
    }
    return item;
  }

  private void writeSetFile(ItemsSet set)
  {
    if (set!=null)
    {
      String setKey=set.getKey();
      File itemsSetFile=getItemsSetFile(setKey);
      if (!itemsSetFile.exists())
      {
        ItemsSetXMLWriter setWriter=new ItemsSetXMLWriter();
        boolean ok=setWriter.write(itemsSetFile,set,EncodingNames.UTF_8);
        if (!ok)
        {
          _logger.error("Write failed for items set ["+setKey+"]!");
        }
      }
    }
  }

  private ItemsSet loadItemsSet(String id)
  {
    ItemsSet ret=null;
    File itemsSetFile=getItemsSetFile(id);
    if (itemsSetFile.exists())
    {
      if (itemsSetFile.length()>0)
      {
        ItemsSetXMLParser parser=new ItemsSetXMLParser();
        ret=parser.parseXML(itemsSetFile);
        if (ret==null)
        {
          _logger.error("Cannot load items set file ["+itemsSetFile+"]!");
        }
      }
    }
    return ret;
  }

  private File getItemFile(int id)
  {
    File itemsDir=Config.getInstance().getItemsDir();
    String fileName=id+".xml";
    File ret=new File(itemsDir,fileName);
    return ret;
  }

  private File getItemsSetFile(String id)
  {
    File itemsDir=Config.getInstance().getItemsDir();
    File setsDir=new File(itemsDir,"sets");
    String fileName=id+".xml";
    File ret=new File(setsDir,fileName);
    return ret;
  }

  /*
  private String urlFromIdentifier(int id)
  {
    String ret=null;
    String baseURL=URL_SEED+id;
    MyLotroURL2Identifier finder=new MyLotroURL2Identifier();
    String idStr=finder.findIdentifier(baseURL,true);
    if (idStr!=null)
    {
      idStr=Escapes.escapeIdentifier(idStr);
      ret=REAL_URL_SEED+idStr;
    }
    return ret;
  }
  */
}
