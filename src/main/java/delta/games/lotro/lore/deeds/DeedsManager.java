package delta.games.lotro.lore.deeds;

import java.io.File;
import java.io.InputStream;

import org.apache.log4j.Logger;

import delta.common.utils.cache.WeakReferencesCache;
import delta.common.utils.files.archives.ArchiveManager;
import delta.games.lotro.Config;
import delta.games.lotro.lore.deeds.index.DeedsIndex;
import delta.games.lotro.lore.deeds.index.io.xml.DeedsIndexXMLParser;
import delta.games.lotro.lore.deeds.io.xml.DeedXMLParser;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Facade for deeds access.
 * @author DAM
 */
public class DeedsManager
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static DeedsManager _instance=new DeedsManager();
  
  private DeedsIndex _index;
  private ArchiveManager _archive;
  private WeakReferencesCache<Integer,DeedDescription> _cache;
  
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
    _cache=new WeakReferencesCache<Integer,DeedDescription>(100);
    File loreDir=Config.getInstance().getLoreDir();
    File deedsArchive=new File(loreDir,"deeds.zip");
    _archive=new ArchiveManager(deedsArchive);
    _archive.open();
    loadIndex();
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
   * Get a deed using its identifier.
   * @param id Deed identifier.
   * @return A deed description or <code>null</code> if not found.
   */
  public DeedDescription getDeed(int id)
  {
    DeedDescription ret=null;
    if (id>0)
    {
      Integer idKey=Integer.valueOf(id);
      ret=(_cache!=null)?_cache.getObject(idKey):null;
      if (ret==null)
      {
        ret=loadDeed(id);
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

  private DeedDescription loadDeed(int id)
  {
    DeedDescription ret=null;
    String fileName=String.valueOf(id)+".xml";
    InputStream is=_archive.getEntry(fileName);
    if (is!=null)
    {
      DeedXMLParser parser=new DeedXMLParser();
      ret=parser.parseXML(is);
      if (ret==null)
      {
        _logger.error("Cannot load deed ["+fileName+"]!");
      }
    }
    return ret;
  }

  private void loadIndex()
  {
    File dir=Config.getInstance().getIndexesDir();
    File deedIndexFile=new File(dir,"deedsIndex.xml");
    if (deedIndexFile.exists())
    {
      DeedsIndexXMLParser parser=new DeedsIndexXMLParser();
      _index=parser.parseXML(deedIndexFile);
    }
    else
    {
      _index=new DeedsIndex();
    }
  }
}
