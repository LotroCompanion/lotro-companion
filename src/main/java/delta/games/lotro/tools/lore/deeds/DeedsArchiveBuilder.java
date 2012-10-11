package delta.games.lotro.tools.lore.deeds;

import java.io.File;

import delta.common.utils.files.archives.ArchiveBuilder;

/**
 * Builds deeds archive file.
 * @author DAM
 */
public class DeedsArchiveBuilder
{
  private File _deedsDir;
  private File _archiveFile;

  /**
   * Constructor.
   * @param deedsDir Deeds directory. 
   * @param archiveFile Archive file.
   */
  public DeedsArchiveBuilder(File deedsDir, File archiveFile)
  {
    _deedsDir=deedsDir;
    _archiveFile=archiveFile;
  }

  /**
   * Do build deeds index.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt()
  {
    System.out.println("Building archive: "+_archiveFile);
    ArchiveBuilder builder=new ArchiveBuilder(_archiveFile);
    boolean ret=builder.start();
    if (ret)
    {
      ret=builder.addDirectory(_deedsDir,null);
      builder.terminate();
    }
    return ret;
  }
}
