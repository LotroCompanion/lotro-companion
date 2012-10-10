package delta.games.lotro.tools.lore.quests;

import java.io.File;

import delta.common.utils.files.archives.ArchiveBuilder;

/**
 * Builds quests archive file.
 * @author DAM
 */
public class QuestsArchiveBuilder
{
  private File _questsDir;
  private File _archiveFile;

  /**
   * Constructor.
   * @param questsDir Quests directory. 
   * @param archiveFile Archive file.
   */
  public QuestsArchiveBuilder(File questsDir, File archiveFile)
  {
    _questsDir=questsDir;
    _archiveFile=archiveFile;
  }

  /**
   * Do build quests index.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt()
  {
    System.out.println("Building archive: "+_archiveFile);
    ArchiveBuilder builder=new ArchiveBuilder(_archiveFile);
    boolean ret=builder.start();
    if (ret)
    {
      ret=builder.addDirectory(_questsDir,null);
      builder.terminate();
    }
    return ret;
  }
}
