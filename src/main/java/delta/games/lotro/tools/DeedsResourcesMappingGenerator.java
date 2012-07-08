package delta.games.lotro.tools;

import java.io.File;
import java.util.List;

import delta.common.utils.environment.FileSystem;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.utils.resources.ResourcesMapping;
import delta.games.lotro.utils.resources.io.xml.ResourcesMappingXMLWriter;

/**
 * Generator for the mapping of deed resources.
 * @author DAM
 */
public class DeedsResourcesMappingGenerator
{

  /**
   * Main method of tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    DeedsManager deedsManager=DeedsManager.getInstance();
    ResourcesMapping mapping=deedsManager.getDeedResourcesMapping();
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    for(CharacterFile toon : toons)
    {
      CharacterLog log=toon.getLastCharacterLog();
      if (log!=null)
      {
        int nbItems=log.getNbItems();
        for(int i=0;i<nbItems;i++)
        {
          CharacterLogItem item=log.getLogItem(i);
          handleItem(mapping,item);
        }
      }
    }
    ResourcesMappingXMLWriter writer=new ResourcesMappingXMLWriter();
    File to=new File(FileSystem.getTmpDir(),"mapping.xml");
    writer.write(to,mapping,EncodingNames.ISO8859_1);
  }

  private static void handleItem(ResourcesMapping mapping, CharacterLogItem item)
  {
    if (item.getLogItemType()==LogItemType.DEED)
    {
      String url=item.getAssociatedUrl();
      String identifier=item.getIdentifier();
      if ((identifier!=null) && (url!=null))
      {
        mapping.registerMapping(url,identifier);
      }
    }
  }
}
