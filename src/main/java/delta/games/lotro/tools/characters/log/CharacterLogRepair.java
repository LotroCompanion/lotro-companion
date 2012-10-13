package delta.games.lotro.tools.characters.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogsManager;
import delta.games.lotro.character.log.io.web.CharacterLogPageParser;

/**
 * Repairs character log to remove crappy log items.
 * @author DAM
 */
public class CharacterLogRepair
{
  private List<Long> getDatesToUpdate(CharacterLog log)
  {
    List<Long> ret=new ArrayList<Long>();
    List<Long> dates=log.getDates();
    for(Long date : dates)
    {
      boolean useIt=false;
      List<CharacterLogItem> items=log.getItemsOfDay(date.longValue());
      for(CharacterLogItem item : items)
      {
        String label=item.getLabel();
        if (("Completed a deed".equals(label)) ||
            ("Completed a quest".equals(label)))
        {
          useIt=true;
          break;
        }
      }
      if (useIt)
      {
        ret.add(date);
      }
    }
    return ret;
  }

  private void handleLogItemsReplacementForDate(Long date, CharacterLog log, CharacterLog newLog)
  {
    List<CharacterLogItem> oldItems=log.getItemsOfDay(date.longValue());
    List<CharacterLogItem> newItems=newLog.getItemsOfDay(date.longValue());
    if (oldItems.size()==newItems.size())
    {
      log.replaceItemsOfDate(date.longValue(),newItems);
    }
  }

  /**
   * Do the job.
   * @param toon Targeted toon.
   * @return <code>true</code> if it succeeded, <code>false</code> otherwise.
   */
  public boolean doIt(CharacterFile toon)
  {
    boolean ok=true;
    CharacterLog log=toon.getLastCharacterLog();
    List<Long> dates=getDatesToUpdate(log);
    if (dates.size()>0)
    {
      Collections.sort(dates);
      Long oldest=dates.get(0);
      
      CharacterLogPageParser parser=new CharacterLogPageParser();
      String url=toon.getBaseMyLotroURL();
      CharacterLog newLog=parser.parseLogPages(url,oldest);
      if (newLog!=null)
      {
        for(Long date : dates)
        {
          handleLogItemsReplacementForDate(date,log,newLog);
        }
        CharacterLogsManager logsManager=toon.getLogsManager();
        ok=logsManager.writeNewLog(log);
      }
      else
      {
        ok=false;
      }
    }
    return ok;
  }
}
