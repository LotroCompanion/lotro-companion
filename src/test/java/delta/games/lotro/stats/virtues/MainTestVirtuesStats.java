package delta.games.lotro.stats.virtues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.Virtue;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;

/**
 * Test for virtues stats.
 * @author DAM
 */
public class MainTestVirtuesStats
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroTestUtils utils=new LotroTestUtils();
    CharacterFile mainToon=utils.getMainToon();
    //CharacterFile mainToon=utils.getToonByName("Feroce");
    CharacterLog log=mainToon.getLastCharacterLog();
    VirtuesStats stats=null;
    if (log!=null)
    {
      stats=new VirtuesStats(log);
      stats.dump(System.out,true);
    }

    HashMap<String,List<String>> virtuesMap=new HashMap<String,List<String>>();
    {
      DeedsManager dm=DeedsManager.getInstance();
      List<DeedDescription> deeds=dm.getAll();
      for(DeedDescription deed : deeds)
      {
        String name=deed.getName();
        Rewards rewards=deed.getRewards();
        Virtue[] virtues=rewards.getVirtues();
        if (virtues!=null)
        {
          for(Virtue virtue : virtues)
          {
            String virtueId=virtue.getIdentifier();
            List<String> items=virtuesMap.get(virtueId);
            if (items==null)
            {
              items=new ArrayList<String>();
              virtuesMap.put(virtueId,items);
            }
            items.add("Deed:"+name);
          }
        }
      }
    }
    List<String> toShow=new ArrayList<String>();
    toShow.add("Valour");
    toShow.add("Loyalty");
    toShow.add("Justice");
    toShow.add("Honour");
    toShow.add("Innocence");
    toShow.add("Zeal");
    List<String> virtueIds=new ArrayList<String>(virtuesMap.keySet());
    Collections.sort(virtueIds);
    for(String virtueId : virtueIds)
    {
      if (toShow.contains(virtueId))
      {
        List<String> deeds=virtuesMap.get(virtueId);
        System.out.println(virtueId+" ("+deeds.size()+"): "+deeds);
        String[] got=stats.getIDsForAVirtue(virtueId);
        if (got!=null)
        {
          System.out.println("GOT:"+virtueId+" ("+got.length+"): "+Arrays.toString(got));
          for(String id : got) deeds.remove(id);
        }
        System.out.println("MISSING: "+virtueId+" ("+deeds.size()+"): "+deeds);
      }
    }
  }
}
