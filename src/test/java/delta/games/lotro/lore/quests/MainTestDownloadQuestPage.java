package delta.games.lotro.lore.quests;

import java.io.File;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.downloads.DownloadException;
import delta.downloads.Downloader;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Test of page download.
 * @author DAM
 */
public class MainTestDownloadQuestPage
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();

  /**
   * Main method.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    String url="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879210718";
    Downloader d=new Downloader();
    File tmpDir=FileSystem.getTmpDir();
    File tmp=new File(tmpDir,"tmp.html");
    try
    {
      d.downloadToFile(url,tmp);
    }
    catch(DownloadException de)
    {
      _logger.error("Cannot download quest page!",de);
    }
  }
}
