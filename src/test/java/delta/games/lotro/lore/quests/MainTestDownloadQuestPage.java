package delta.games.lotro.lore.quests;

import java.io.File;

import delta.common.utils.environment.FileSystem;
import delta.downloads.Downloader;

/**
 * Test of page download.
 * @author DAM
 */
public class MainTestDownloadQuestPage
{
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
    d.downloadPage(url,tmp);
  }
}
