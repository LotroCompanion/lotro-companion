package delta.games.lotro.character.log.io.web;

import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;

/**
 * @author DAM
 */
public class CharacterLogUpdater
{
  public void updateCharacterLog(CharacterLog log, String url)
  {
    int nbItems=log.getNbItems();
    Long stopDate=null;
    if (nbItems>0)
    {
      CharacterLogItem item=log.getLogItem(0);
      long date=item.getDate();
      stopDate=Long.valueOf(date);
      int nbRemoved=log.removeItemsOfDay(date);
      System.out.println("Removed "+nbRemoved+" item(s)!");
    }
    CharacterLogPageParser parser=new CharacterLogPageParser();
    CharacterLog newLog=parser.parseLogPages(url,stopDate);
    if (newLog!=null)
    {
      // Merge items
      int nbNewItems=newLog.getNbItems();
      for(int i=nbNewItems-1;i>=0;i--)
      {
        CharacterLogItem newItem=newLog.getLogItem(i);
        log.addLogItem(newItem,0);
      }
      System.out.println("Added "+nbNewItems+" item(s)!");
    }
  }
}
