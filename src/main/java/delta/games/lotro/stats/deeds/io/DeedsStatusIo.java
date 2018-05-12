package delta.games.lotro.stats.deeds.io;

import java.io.File;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.reputation.ReputationStatus;
import delta.games.lotro.stats.deeds.DeedsStatusManager;
import delta.games.lotro.stats.deeds.SyncDeedsStatusAndReputationStatus;
import delta.games.lotro.stats.deeds.io.xml.DeedsStatusXMLParser;
import delta.games.lotro.stats.deeds.io.xml.DeedsStatusXMLWriter;

/**
 * I/O methods for deeds status.
 * @author DAM
 */
public class DeedsStatusIo
{
  /**
   * Load the deeds status for a character.
   * @param character Targeted character.
   * @return A deeds status.
   */
  public static DeedsStatusManager load(CharacterFile character)
  {
    File fromFile=getStatusFile(character);
    DeedsStatusManager status=null;
    if (fromFile.exists())
    {
      DeedsStatusXMLParser parser=new DeedsStatusXMLParser();
      status=parser.parseXML(fromFile);
    }
    if (status==null)
    {
      // Initialize from reputation status
      status=new DeedsStatusManager();
      ReputationStatus repStatus=character.getReputation();
      SyncDeedsStatusAndReputationStatus.syncDeedsStatus(repStatus,status);
      save(character,status);
    }
    status.setCharacter(character.getName(),character.getServerName());
    return status;
  }

  /**
   * Save the deeds status for a character.
   * @param character Targeted character.
   * @param status Status to save.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public static boolean save(CharacterFile character, DeedsStatusManager status)
  {
    File toFile=getStatusFile(character);
    DeedsStatusXMLWriter writer=new DeedsStatusXMLWriter();
    boolean ok=writer.write(toFile,status,EncodingNames.UTF_8);
    return ok;
  }

  private static File getStatusFile(CharacterFile character)
  {
    File rootDir=character.getRootDir();
    File statusFile=new File(rootDir,"deedsStatus.xml");
    return statusFile;
  }
}
