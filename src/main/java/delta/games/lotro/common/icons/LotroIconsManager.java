package delta.games.lotro.common.icons;

import java.io.File;

import org.apache.log4j.Logger;

import delta.common.utils.files.FileCopy;
import delta.games.lotro.Config;
import delta.games.lotro.utils.LotroLoggers;
import delta.games.lotro.utils.cache.StringToFileCache;

/**
 * Facade for icons access.
 * @author DAM
 */
public class LotroIconsManager
{
  private static final String SEED="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/";
  private static final String SEED2="https://my.lotro.com/sites/lorebook.lotro.com/images/icons";

  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static LotroIconsManager _instance=new LotroIconsManager();
  
  private File _rootDir;
  private StringToFileCache _cache;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static LotroIconsManager getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private LotroIconsManager()
  {
    _rootDir=Config.getInstance().getIconsDir();
    String[] seeds={SEED,SEED2};
    _cache=new StringToFileCache(_rootDir,seeds);
  }

  /**
   * Get the icon at the given URL.
   * @param url URL of icon.
   * @return A file or <code>null</code> if not found.
   */
  public File getIconFile(String url)
  {
    File ret=null;
    File file=_cache.getFileForString(url);
    if (file.canRead())
    {
      ret=file;
    }
    else
    {
      boolean ok=fetchIcon(url,file);
      if (ok)
      {
        ret=file;
      }
    }
    return ret;
  }

  private boolean fetchIcon(String url, File to)
  {
    boolean ret=true;
    File parentFile=to.getParentFile();
    if (!parentFile.exists())
    {
      ret=parentFile.mkdirs();
      if (!ret)
      {
        _logger.error("Cannot create directory ["+parentFile+"]!");
      }
    }
    if (ret)
    {
      ret=FileCopy.copyFromURL(url,to);
    }
    return ret;
  }
}
