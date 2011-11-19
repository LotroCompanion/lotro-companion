package delta.games.lotro.characterLog;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.downloads.Downloader;
import delta.games.lotro.characterLog.io.web.LotroActivityLogPageParser;
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
    File rootDir2=new File("/home/dm/lotroPages");

    List<LotroLogItem> completeLog=new ArrayList<LotroLogItem>();
    Downloader d=new Downloader();
    for(int i=1;i<=105;i++) {
      String url=rootURL+"="+String.valueOf(i);
      File toFile=new File(rootDir,String.valueOf(i)+".html");
      toFile.getParentFile().mkdirs();
      try {
        d.downloadPage(url,toFile);
        LotroActivityLogPageParser parser=new LotroActivityLogPageParser();
        List<LotroLogItem> items=parser.parseLogPage(toFile);
        if ((items!=null) && (items.size()>0))
        {
          completeLog.addAll(items);
        }
      }
      catch(Exception e) {
        _logger.error("Error when downloading character log page!",e);
      }
    }
    /*
    String url="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879157116";
    File to=new File(rootDir2,"infos.html");
    d.downloadPage(url,to);
    */
  }
}
