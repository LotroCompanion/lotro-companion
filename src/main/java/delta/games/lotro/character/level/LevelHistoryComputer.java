package delta.games.lotro.character.level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import delta.common.utils.NumericTools;
import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterInfosManager;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;

/**
 * Computes a level history using log and character data history.
 * @author DAM
 */
public class LevelHistoryComputer
{
  /**
   * Build a level history for a toon.
   * @param toon Toon to use.
   * @return A level history.
   */
  public LevelHistory buildLevelHistory(CharacterFile toon)
  {
    String name=toon.getName();
    LevelHistory history=new LevelHistory(name);
    CharacterLog log=toon.getLastCharacterLog();
    if (log!=null)
    {
      loadLog(history,log);
    }
    loadInfos(history,toon);
    return history;
  }

  private void loadInfos(LevelHistory history, CharacterFile toon)
  {
    CharacterInfosManager infosMgr=toon.getInfosManager();
    File[] infoFiles=infosMgr.getInfoFiles();
    if (infoFiles!=null)
    {
      for(File infoFile : infoFiles)
      {
        Character c=infosMgr.getCharacterDescription(infoFile);
        if (c!=null)
        {
          Long date=c.getDate();
          if (date!=null)
          {
            int level=c.getLevel();
            history.setLevel(level,date.longValue());
          }
        }
      }
    }
  }

  /**
   * Load data from a character log.
   * @param history History to fill.
   * @param log Log items to use.
   */
  private void loadLog(LevelHistory history, CharacterLog log)
  {
    List<CharacterLogItem> items=getLevelItems(log);
    parseLevelUpItems(history,items);
  }

  private List<CharacterLogItem> getLevelItems(CharacterLog log)
  {
    List<CharacterLogItem> ret=new ArrayList<CharacterLogItem>();
    int nb=log.getNbItems();
    for(int i=0;i<nb;i++)
    {
      CharacterLogItem item=log.getLogItem(i);
      if ((item!=null) && (item.getLogItemType()==LogItemType.LEVELUP))
      {
        ret.add(item);
      }
    }
    return ret;
  }

  private void parseLevelUpItems(LevelHistory history, List<CharacterLogItem> items)
  {
    for(CharacterLogItem item : items)
    {
      if (item!=null)
      {
        String label=item.getLabel();
        int level=NumericTools.parseInt(label,0);
        long date=item.getDate();
        if ((level!=0) && (date!=0))
        {
          history.setLevel(level,date);
        }
      }
    }
  }
}
