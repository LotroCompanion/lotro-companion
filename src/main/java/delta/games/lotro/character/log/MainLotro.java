package delta.games.lotro.character.log;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.common.utils.text.EncodingNames;
import delta.downloads.Downloader;
import delta.games.lotro.character.log.io.web.CharacterLogPageParser;
import delta.games.lotro.character.log.io.xml.CharacterLogXMLWriter;
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
    String rootURL="http://my.lotro.com/home/character/2427907/146366987891794854";
    String name="Glumlug";
    //String rootURL="http://my.lotro.com/home/character/2427907/146366987891895618";
    //String name="Alphael";
    //String rootURL="http://my.lotro.com/home/character/2427907/146366987891842857";
    //String name="Minirdil";
    rootURL+="/activitylog?"+URLEncoder.encode("cl[pp]");
    File tmpDir=FileSystem.getTmpDir();
    File toFile=new File(tmpDir,"lotroLogPage.html");
    Downloader d=new Downloader();
    CharacterLog log=new CharacterLog(name);
    CharacterLogPageParser parser=new CharacterLogPageParser();
    for(int i=1;i<=116;i++) {
      String url=rootURL+"="+String.valueOf(i);
      try {
        boolean ok=d.downloadPage(url,toFile);
        System.out.println("Page #"+i);
        if (ok)
        {
          List<CharacterLogItem> items=parser.parseLogPage(toFile);
          if ((items!=null) && (items.size()>0))
          {
            for(CharacterLogItem item : items)
            {
              log.addLogItem(item);
            }
          }
        }
        else
        {
          _logger.error("Cannot download log page #"+i+" for character ["+name+"]");
        }
      }
      catch(Exception e) {
        _logger.error("Error when downloading character log page!",e);
      }
    }
    File rootDir=new File("/home/dm/lotro");
    File characterDir=new File(rootDir,name);
    characterDir.mkdirs();
    File logFile=new File(characterDir,"log.xml");
    CharacterLogXMLWriter writer=new CharacterLogXMLWriter();
    writer.write(logFile,log,EncodingNames.UTF_8);
    /*
    String url="http://lorebook.lotro.com/wiki/Special:LotroResource?id=1879157116";
    File to=new File(rootDir2,"infos.html");
    d.downloadPage(url,to);
    */
  }
}
