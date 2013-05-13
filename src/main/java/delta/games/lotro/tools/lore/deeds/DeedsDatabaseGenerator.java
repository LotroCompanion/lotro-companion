package delta.games.lotro.tools.lore.deeds;

import java.io.File;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Deeds database generator.
 * @author DAM
 */
public class DeedsDatabaseGenerator
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private File _workDir;
  private File _deedsDir;

  /**
   * Constructor.
   */
  public DeedsDatabaseGenerator()
  {
    _workDir=FileSystem.getTmpDir();
    _deedsDir=new File(_workDir,"deeds");
    //_deedsDir=Config.getInstance().getDeedsDir();
  }

  /**
   * Perform deeds database generation. 
   */
  public void doIt()
  {
    // 1 - get deeds index
    File tmpDeedsIndexFile=new File(_workDir,"tmpDeedsIndex.xml");
    DeedsIndexLoader indexLoader=new DeedsIndexLoader();
    boolean indexLoadingOK=indexLoader.doIt(tmpDeedsIndexFile);
    if (!indexLoadingOK)
    {
      _logger.error("Cannot get deeds index! Stopping.");
      return;
    }
    // 2 - load deeds
    DeedsLoader deedsLoader=new DeedsLoader(_deedsDir,tmpDeedsIndexFile);
    boolean deedsLoadingOK=deedsLoader.doIt();
    if (!deedsLoadingOK)
    {
      _logger.error("Cannot load deeds! Stopping.");
      return;
    }
    // 3 - build deed index
    File deedsIndexFile=new File(_workDir,"deedsIndex.xml");
    DeedsIndexBuilder deedsIndexBuilder=new DeedsIndexBuilder(_deedsDir,deedsIndexFile);
    boolean indexOK=deedsIndexBuilder.doIt();
    if (!indexOK)
    {
      _logger.error("Cannot build deeds index! Stopping.");
      return;
    }
    // 4 - archive deeds
    File archiveFile=new File(_workDir,"deeds.zip");
    DeedsArchiveBuilder archiveBuilder=new DeedsArchiveBuilder(_deedsDir,archiveFile);
    boolean archiveOK=archiveBuilder.doIt();
    if (!archiveOK)
    {
      _logger.error("Cannot build deeds archive! Stopping.");
      return;
    }
  }

  /**
   * Main method for deeds database generator.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    DeedsDatabaseGenerator generator=new DeedsDatabaseGenerator();
    generator.doIt();
  }
}
