package delta.games.lotro.stats.traitPoints;

import java.io.File;
import java.util.List;

import delta.common.utils.text.EncodingNames;
import delta.games.lotro.LotroCoreConfig;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.stats.traitPoints.io.xml.TraitPointsRegistryXMLParser;
import delta.games.lotro.stats.traitPoints.io.xml.TraitPointsStatusXMLParser;
import delta.games.lotro.stats.traitPoints.io.xml.TraitPointsStatusXMLWriter;

/**
 * Facade for services related to trait points.
 * @author DAM
 */
public class TraitPoints
{
  private static final TraitPoints _instance=new TraitPoints();

  private TraitPointsRegistry _registry;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static TraitPoints get()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private TraitPoints()
  {
    _registry=loadRegistry();
  }

  /**
   * Get the trait points registry.
   * @return the trait points registry.
   */
  public TraitPointsRegistry getRegistry()
  {
    return _registry;
  }

  /**
   * Load the trait points for a character.
   * @param character Targeted character.
   * @return A trait point status.
   */
  public TraitPointsStatus load(CharacterFile character)
  {
    File fromFile=getStatusFile(character);
    TraitPointsStatus status=null;
    if (fromFile.exists())
    {
      TraitPointsStatusXMLParser parser=new TraitPointsStatusXMLParser();
      status=parser.parseXML(fromFile);
    }
    if (status==null)
    {
      status=new TraitPointsStatus();
    }
    return status;
  }

  /**
   * Save the trait points status for a character.
   * @param character Targeted character.
   * @param status Status to save.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean save(CharacterFile character, TraitPointsStatus status)
  {
    File toFile=getStatusFile(character);
    TraitPointsStatusXMLWriter writer=new TraitPointsStatusXMLWriter();
    boolean ok=writer.write(toFile,status,EncodingNames.UTF_8);
    return ok;
  }

  private File getStatusFile(CharacterFile character)
  {
    File rootDir=character.getRootDir();
    File statusFile=new File(rootDir,"traitPoints.xml");
    return statusFile;
  }

  private TraitPointsRegistry loadRegistry()
  {
    LotroCoreConfig cfg=LotroCoreConfig.getInstance();
    File loreDir=cfg.getLoreDir();
    File registryFile=new File(loreDir,"traitPoints.xml");
    TraitPointsRegistryXMLParser parser=new TraitPointsRegistryXMLParser();
    TraitPointsRegistry registry=parser.parseXML(registryFile);
    if (registry==null)
    {
      registry=new TraitPointsRegistry();
    }
    return registry;
  }

  /**
   * Get the maximum number of trait points for a character.
   * @param characterClass Class of the character.
   * @param characterLevel Level of the character.
   * @return A points count.
   */
  public int getMaxTraitPoints(CharacterClass characterClass, int characterLevel)
  {
    int fromLevel=TraitPointsStatus.getTraitPointsFromLevel(characterLevel);
    List<TraitPoint> points=_registry.getPointsForClass(characterClass);
    int acquireable=points.size();
    int total=fromLevel+acquireable;
    return total;
  }
}
