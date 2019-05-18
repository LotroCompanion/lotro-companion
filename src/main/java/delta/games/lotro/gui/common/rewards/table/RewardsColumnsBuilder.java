package delta.games.lotro.gui.common.rewards.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.games.lotro.common.rewards.EmoteReward;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.TitleReward;
import delta.games.lotro.common.rewards.TraitReward;
import delta.games.lotro.common.rewards.VirtueReward;

/**
 * Builder for columns that show rewards data.
 * @author DAM
 */
public class RewardsColumnsBuilder
{
  /**
   * Build columns to display rewards.
   * @return A list of columns controllers for rewards data.
   */
  public static List<DefaultTableColumnController<Rewards,?>> buildRewardColumns()
  {
    List<DefaultTableColumnController<Rewards,?>> ret=new ArrayList<DefaultTableColumnController<Rewards,?>>();
    // LOTRO points column
    {
      CellDataProvider<Rewards,Integer> lpCell=new CellDataProvider<Rewards,Integer>()
      {
        @Override
        public Integer getData(Rewards rewards)
        {
          int lotroPoints=rewards.getLotroPoints();
          return (lotroPoints>0)?Integer.valueOf(lotroPoints):null;
        }
      };
      DefaultTableColumnController<Rewards,Integer> lpColumn=new DefaultTableColumnController<Rewards,Integer>(RewardsColumnIds.LOTRO_POINTS.name(),"LOTRO Points",Integer.class,lpCell);
      lpColumn.setWidthSpecs(40,40,40);
      ret.add(lpColumn);
    }
    // Class point column
    {
      CellDataProvider<Rewards,Boolean> cpCell=new CellDataProvider<Rewards,Boolean>()
      {
        @Override
        public Boolean getData(Rewards rewards)
        {
          int classPoints=rewards.getClassPoints();
          return (classPoints>0)?Boolean.TRUE:Boolean.FALSE;
        }
      };
      DefaultTableColumnController<Rewards,Boolean> cpColumn=new DefaultTableColumnController<Rewards,Boolean>(RewardsColumnIds.CLASS_POINT.name(),"Class Point",Boolean.class,cpCell);
      cpColumn.setWidthSpecs(40,40,40);
      ret.add(cpColumn);
    }
    // Title column
    {
      CellDataProvider<Rewards,String> titleCell=new CellDataProvider<Rewards,String>()
      {
        @Override
        public String getData(Rewards rewards)
        {
          List<TitleReward> titleRewards=rewards.getRewardElementsOfClass(TitleReward.class);
          return ((titleRewards.size()>0))?titleRewards.get(0).getName():null;
        }
      };
      DefaultTableColumnController<Rewards,String> titleColumn=new DefaultTableColumnController<Rewards,String>(RewardsColumnIds.TITLE.name(),"Title",String.class,titleCell);
      titleColumn.setWidthSpecs(100,300,200);
      ret.add(titleColumn);
    }
    // Virtue column
    {
      CellDataProvider<Rewards,String> virtueCell=new CellDataProvider<Rewards,String>()
      {
        @Override
        public String getData(Rewards rewards)
        {
          List<VirtueReward> virtueRewards=rewards.getRewardElementsOfClass(VirtueReward.class);
          return (virtueRewards.size()>0)?virtueRewards.get(0).getIdentifier().getLabel():null;
        }
      };
      DefaultTableColumnController<Rewards,String> virtueColumn=new DefaultTableColumnController<Rewards,String>(RewardsColumnIds.VIRTUE.name(),"Virtue",String.class,virtueCell);
      virtueColumn.setWidthSpecs(100,300,200);
      ret.add(virtueColumn);
    }
    // Emote column
    {
      CellDataProvider<Rewards,String> emoteCell=new CellDataProvider<Rewards,String>()
      {
        @Override
        public String getData(Rewards rewards)
        {
          List<EmoteReward> emoteRewards=rewards.getRewardElementsOfClass(EmoteReward.class);
          return ((emoteRewards.size()>0))?emoteRewards.get(0).getName():null;
        }
      };
      DefaultTableColumnController<Rewards,String> emoteColumn=new DefaultTableColumnController<Rewards,String>(RewardsColumnIds.EMOTE.name(),"Emote",String.class,emoteCell);
      emoteColumn.setWidthSpecs(100,300,200);
      ret.add(emoteColumn);
    }
    // Trait column
    {
      CellDataProvider<Rewards,String> traitCell=new CellDataProvider<Rewards,String>()
      {
        @Override
        public String getData(Rewards rewards)
        {
          List<TraitReward> traitRewards=rewards.getRewardElementsOfClass(TraitReward.class);
          return ((traitRewards.size()>0))?traitRewards.get(0).getName():null;
        }
      };
      DefaultTableColumnController<Rewards,String> traitColumn=new DefaultTableColumnController<Rewards,String>(RewardsColumnIds.TRAIT.name(),"Trait",String.class,traitCell);
      traitColumn.setWidthSpecs(100,300,200);
      ret.add(traitColumn);
    }
    // XP column
    {
      CellDataProvider<Rewards,Integer> xpCell=new CellDataProvider<Rewards,Integer>()
      {
        @Override
        public Integer getData(Rewards rewards)
        {
          int xp=rewards.getXp();
          return (xp>0)?Integer.valueOf(xp):null;
        }
      };
      DefaultTableColumnController<Rewards,Integer> xpColumn=new DefaultTableColumnController<Rewards,Integer>(RewardsColumnIds.XP.name(),"XP",Integer.class,xpCell);
      xpColumn.setWidthSpecs(60,60,60);
      ret.add(xpColumn);
    }
    // Item XP column
    {
      CellDataProvider<Rewards,Integer> itemXpCell=new CellDataProvider<Rewards,Integer>()
      {
        @Override
        public Integer getData(Rewards rewards)
        {
          int itemXp=rewards.getItemXp();
          return (itemXp>0)?Integer.valueOf(itemXp):null;
        }
      };
      DefaultTableColumnController<Rewards,Integer> itemXpColumn=new DefaultTableColumnController<Rewards,Integer>(RewardsColumnIds.ITEM_XP.name(),"Item XP",Integer.class,itemXpCell);
      itemXpColumn.setWidthSpecs(60,60,60);
      ret.add(itemXpColumn);
    }
    // Mount XP column
    {
      CellDataProvider<Rewards,Integer> mountXpCell=new CellDataProvider<Rewards,Integer>()
      {
        @Override
        public Integer getData(Rewards rewards)
        {
          int mountXp=rewards.getMountXp();
          return (mountXp>0)?Integer.valueOf(mountXp):null;
        }
      };
      DefaultTableColumnController<Rewards,Integer> mountXpColumn=new DefaultTableColumnController<Rewards,Integer>(RewardsColumnIds.MOUNT_XP.name(),"Mount XP",Integer.class,mountXpCell);
      mountXpColumn.setWidthSpecs(60,60,60);
      ret.add(mountXpColumn);
    }
    return ret;
  }
}
