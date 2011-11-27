package delta.games.lotro.character.log;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import delta.games.lotro.character.CharacterFile;

/**
 * @author DAM
 */
public class CharacterLogsManager
{
  private CharacterFile _file;

  public CharacterLogsManager(CharacterFile file)
  {
    _file=file;
    
  }

  public File getLastLogFile()
  {
    File lastLog=null;
    File characterDir=_file.getRootDir();
    if (characterDir.exists())
    {
      FileFilter filter=new FileFilter()
      {
        public boolean accept(File pathname)
        {
          String name=pathname.getName();
          if ((name.startsWith("log ")) && (name.endsWith(".xml")))
          {
            return true;
          }
          return false;
        }
      };
      File[] files=characterDir.listFiles(filter);
      if ((files!=null) && (files.length>0))
      {
        Arrays.sort(files);
        lastLog=files[files.length-1];
      }
    }
    return lastLog;
  }

  public File getNewLogFile()
  {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HHmm");
    String filename="log "+sdf.format(new Date())+".xml";
    File characterDir=_file.getRootDir();
    File logFile=new File(characterDir,filename);
    return logFile;
  }
}
