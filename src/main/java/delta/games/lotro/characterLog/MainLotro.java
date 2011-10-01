package delta.games.lotro.characterLog;

import java.io.File;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import delta.downloads.Downloader;
import delta.games.lotro.utils.LotroLoggers;

/**
 * @author DAM
 */
public class MainLotro
{
  private static final Logger _logger=LotroLoggers.getCharacterLogLogger();

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    new MainLotro().doIt();
  }

  private void doIt()
  {
    String rootURL="http://my.lotro.com/home/character/2427907/146366987891794854/activitylog?"+URLEncoder.encode("cl[pp]");
    File rootDir=new File("/home/dm/lotroPages/glumlug");
    Downloader d=new Downloader();
    for(int i=1;i<=78;i++) {
      String url=rootURL+"="+String.valueOf(i);
      File toFIle=new File(rootDir,String.valueOf(i)+".html");
      toFIle.getParentFile().mkdirs();
      try {
        //FileCopy.copyFromURL(new URL(url),toFIle);
        d.downloadPage(url,toFIle);
      }
      catch(Exception e) {
        _logger.error("Error when downloading character log page!",e);
      }
    }
  }
}
