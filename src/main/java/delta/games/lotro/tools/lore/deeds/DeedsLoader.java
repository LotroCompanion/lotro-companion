package delta.games.lotro.tools.lore.deeds;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.files.FilesDeleter;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.index.DeedCategory;
import delta.games.lotro.lore.deeds.index.DeedSummary;
import delta.games.lotro.lore.deeds.index.DeedsIndex;
import delta.games.lotro.lore.deeds.index.io.xml.DeedsIndexXMLParser;
import delta.games.lotro.lore.deeds.io.web.DeedPageParser;
import delta.games.lotro.lore.deeds.io.xml.DeedXMLWriter;
import delta.games.lotro.utils.Escapes;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Deeds loader.
 * @author DAM
 */
public class DeedsLoader
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();
  
  private File _deedsDir;
  private File _indexFile;
  private DeedsIndex _index;
  private int _totalNbDeedsToLoad;
  private int _totalNbDeedsLoaded;

  /**
   * Constructor.
   * @param deedsDir Directory to write deeds to.
   * @param indexFile Index file.
   */
  public DeedsLoader(File deedsDir, File indexFile)
  {
    _deedsDir=deedsDir;
    _indexFile=indexFile;
  }

  /**
   * Do load deeds.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt()
  {
    boolean ok=init();
    if (!ok)
    {
      return false;
    }
    loadDeeds();
    System.out.println("Deeds loaded: "+_totalNbDeedsLoaded+"/"+_totalNbDeedsToLoad+".");
    return true;
  }

  private boolean init()
  {
    // Build work directory
    if (_deedsDir.exists())
    {
      FilesDeleter deleter=new FilesDeleter(_deedsDir,null,true);
      deleter.doIt();
    }
    boolean ret=_deedsDir.mkdirs();
    if (!ret)
    {
      _logger.error("Cannot empty work directory ["+_deedsDir+"]!");
    }
    // Load deeds index
    DeedsIndexXMLParser parser=new DeedsIndexXMLParser();
    _index=parser.parseXML(_indexFile);
    if (_index==null)
    {
      _logger.error("Cannot read deeds index!");
    }
    ret=(_index!=null);
    return ret;
  }

  private void loadDeeds()
  {
    String[] categories=_index.getCategories();
    for(String category : categories)
    {
      loadDeedsForCategory(category);
    }
  }

  private void loadDeedsForCategory(String categoryName)
  {
    System.out.println("Loading deeds for category ["+categoryName+"]...");
    DeedCategory category=_index.getCategory(categoryName);
    DeedSummary[] summaries=category.getDeeds();
    int nbDeedsToLoad=summaries.length;
    int nbDeedsLoaded=0;
    for(String key : category.getKeys())
    {
      int nbDeeds=loadDeedsDefinition(key);
      nbDeedsLoaded+=nbDeeds;
    }
    _totalNbDeedsToLoad+=nbDeedsToLoad;
    _totalNbDeedsLoaded+=nbDeedsLoaded;
    System.out.println("Category ["+categoryName+"]: "+nbDeedsLoaded+"/"+nbDeedsToLoad+".");
  }

  /**
   * Load deed definitions.
   * @param key Deed key.
   * @return Number of loaded deeds.
   */
  private int loadDeedsDefinition(String key)
  {
    int nbDeeds=0;
    DeedPageParser parser=new DeedPageParser();
    String url=urlFromIdentifier(key);
    List<DeedDescription> deeds=parser.parseDeedPage(url);
    if ((deeds!=null) && (deeds.size()>0))
    {
      DeedXMLWriter writer=new DeedXMLWriter();
      for(DeedDescription deed : deeds)
      {
        int id=deed.getIdentifier();
        String fileName=String.valueOf(id)+".xml";
        File deedFile=new File(_deedsDir,fileName);
        if (!deedFile.getParentFile().exists())
        {
          deedFile.getParentFile().mkdirs();
        }
        boolean ok=writer.write(deedFile,deed,EncodingNames.UTF_8);
        if (ok)
        {
          nbDeeds++;
        }
        else
        {
          String name=deed.getName();
          _logger.error("Write failed for deed ["+name+"]!");
        }
      }
    }
    else
    {
      _logger.error("Cannot parse deed ["+key+"] at URL ["+url+"]!");
    }
    return nbDeeds;
  }

  private String urlFromIdentifier(String id)
  {
    id=Escapes.escapeIdentifier(id);
    String url="http://lorebook.lotro.com/wiki/Deed:"+id;
    return url;
  }
}
