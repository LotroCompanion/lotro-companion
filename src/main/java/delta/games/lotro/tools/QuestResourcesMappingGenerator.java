package delta.games.lotro.tools;

import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.quests.QuestsManager;
import delta.games.lotro.utils.resources.ResourcesMapping;

/**
 * Generator for the mapping of quest resources.
 * @author DAM
 */
public class QuestResourcesMappingGenerator
{

  /**
   * Main method of tool.
   * @param args Not used.
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
    qm.updateQuestResourcesMapping();
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
