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
import delta.games.lotro.quests.QuestsManager;
import delta.games.lotro.utils.resources.ResourcesMapping;
import delta.games.lotro.utils.resources.io.xml.ResourcesMappingXMLWriter;

/**
 * @author DAM
 */
public class QuestResourcesMappingGenerator
{

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    QuestsManager qm=QuestsManager.getInstance();
    ResourcesMapping mapping=qm.getQuestResourcesMapping();
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
    if (item.getLogItemType()==LogItemType.QUEST)
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
