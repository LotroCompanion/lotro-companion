package delta.games.lotro.character.log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import delta.games.lotro.character.log.io.web.CharacterLogPageParser;

/**
 * Test for character activity log parsing.
 * @author DAM
 */
public class MainTestCharacterActivityLogParsing
{
  private static List<File> sortFiles(File[] files)
  {
    List<File> ret=new ArrayList<File>();
    if ((files!=null) && (files.length>0))
    {
      for(File file : files)
      {
        if (file.getName().endsWith(".html"))
        {
          ret.add(file);
        }
      }

      Comparator<File> c=new Comparator<File>()
      {
        public int compare(File f1, File f2)
        {
          String name1=f1.getName();
          int n1=Integer.parseInt(name1.substring(0,name1.length()-5));
          String name2=f2.getName();
          int n2=Integer.parseInt(name2.substring(0,name2.length()-5));
          if (n1>n2) return 1;
          if (n1<n2) return -1;
          return 0;
        }
      };
      Collections.sort(ret,c);
    }
    return ret;
  }

  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    File rootDir=new File("/home/dm/lotroPages/glumlug");
    File[] files=rootDir.listFiles();
    if (files!=null)
    {
      List<File> filesToParse=sortFiles(files);
      List<CharacterLogItem> completeLog=new ArrayList<CharacterLogItem>();
      CharacterLogPageParser parser=new CharacterLogPageParser();
      for(File file : filesToParse)
      {
        List<CharacterLogItem> items=parser.parseLogPage(file);
        if ((items!=null) && (items.size()>0))
        {
          completeLog.addAll(items);
          /*
          if (items.size()!=20)
          {
            System.out.println(items.size()+" "+file);
          }
          */
        }
      }
      for(CharacterLogItem logItem : completeLog)
      {
        System.out.println(logItem);
      }
      System.out.println(completeLog.size());
    }
  }
}
