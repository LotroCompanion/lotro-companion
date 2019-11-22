package delta.games.lotro.stats.virtues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.LotroTestUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.character.virtues.VirtueKeyComparator;
import delta.games.lotro.character.virtues.VirtuesManager;
import delta.games.lotro.common.rewards.RewardElement;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.VirtueReward;
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

    HashMap<VirtueDescription,List<String>> virtuesMap=new HashMap<VirtueDescription,List<String>>();
    {
      DeedsManager dm=DeedsManager.getInstance();
      List<DeedDescription> deeds=dm.getAll();
      for(DeedDescription deed : deeds)
      {
        String name=deed.getName();
        Rewards rewards=deed.getRewards();
        for(RewardElement rewardElement : rewards.getRewardElements())
        {
          if (rewardElement instanceof VirtueReward)
          {
            VirtueReward virtueReward=(VirtueReward)rewardElement;
            VirtueDescription virtue=virtueReward.getVirtue();
            List<String> items=virtuesMap.get(virtue);
            if (items==null)
            {
              items=new ArrayList<String>();
              virtuesMap.put(virtue,items);
            }
            items.add("Deed:"+name);
          }
        }
      }
    }
    List<VirtueDescription> toShow=new ArrayList<VirtueDescription>();
    VirtuesManager virtuesMgr=VirtuesManager.getInstance();
    toShow.add(virtuesMgr.getByKey("VALOUR"));
    toShow.add(virtuesMgr.getByKey("LOYALTY"));
    toShow.add(virtuesMgr.getByKey("JUSTICE"));
    toShow.add(virtuesMgr.getByKey("HONOUR"));
    toShow.add(virtuesMgr.getByKey("INNOCENCE"));
    toShow.add(virtuesMgr.getByKey("ZEAL"));
    List<VirtueDescription> virtues=new ArrayList<VirtueDescription>(virtuesMap.keySet());
    Collections.sort(virtues,new VirtueKeyComparator());
    for(VirtueDescription virtue : virtues)
    {
      if (toShow.contains(virtue))
      {
        List<String> deeds=virtuesMap.get(virtue);
        System.out.println(virtue.getName()+" ("+deeds.size()+"): "+deeds);
        String[] got=stats.getIDsForAVirtue(virtue);
        if (got!=null)
        {
          System.out.println("GOT:"+virtue.getName()+" ("+got.length+"): "+Arrays.toString(got));
          for(String id : got) deeds.remove(id);
        }
        System.out.println("MISSING: "+virtue.getName()+" ("+deeds.size()+"): "+deeds);
      }
    }
  }
}
