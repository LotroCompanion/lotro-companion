package delta.games.lotro.tools.lore.recipes;

import java.io.File;

import delta.common.utils.files.archives.ArchiveBuilder;

/**
 * Builds recipes archive file.
 * @author DAM
 */
public class RecipesArchiveBuilder
{
  private File _recipesDir;
  private File _archiveFile;

  /**
   * Constructor.
   * @param recipesDir Recipes directory. 
   * @param archiveFile Archive file.
   */
  public RecipesArchiveBuilder(File recipesDir, File archiveFile)
  {
    _recipesDir=recipesDir;
    _archiveFile=archiveFile;
  }

  /**
   * Do build recipes archive.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt()
  {
    System.out.println("Building archive: "+_archiveFile);
    ArchiveBuilder builder=new ArchiveBuilder(_archiveFile);
    boolean ret=builder.start();
    if (ret)
    {
      ret=builder.addDirectory(_recipesDir,null);
      builder.terminate();
    }
    return ret;
  }
}
