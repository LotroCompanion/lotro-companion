package delta.games.lotro.gui.common.rewards.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.games.lotro.common.rewards.EmoteReward;
import delta.games.lotro.common.rewards.ReputationReward;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.TitleReward;
import delta.games.lotro.common.rewards.TraitReward;
import delta.games.lotro.common.rewards.VirtueReward;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Builder for columns that show rewards data.
 * @author DAM
 */
public class RewardsColumnsBuilder
{
  /**
   * Build columns to display rewards.
   * @param parent Parent controller.
   * @return A list of columns controllers for rewards data.
   */
  public static List<DefaultTableColumnController<Rewards,?>> buildRewardColumns(AreaController parent)
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
      DefaultTableColumnController<Rewards,Integer> lpColumn=new DefaultTableColumnController<Rewards,Integer>(RewardsColumnIds.LOTRO_POINTS.name(),"LOTRO Points",Integer.class,lpCell); // I18n
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
      DefaultTableColumnController<Rewards,Boolean> cpColumn=new DefaultTableColumnController<Rewards,Boolean>(RewardsColumnIds.CLASS_POINT.name(),"Class Point",Boolean.class,cpCell); // I18n
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
          if (!titleRewards.isEmpty())
          {
            TitleDescription title=titleRewards.get(0).getTitle();
            String rawTitleName=title.getRawName();
            String titleName=ContextRendering.render(parent,rawTitleName);
            return titleName;
          }
          return null;
        }
      };
      DefaultTableColumnController<Rewards,String> titleColumn=new DefaultTableColumnController<Rewards,String>(RewardsColumnIds.TITLE.name(),"Title",String.class,titleCell); // I18n
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
          return (virtueRewards.size()>0)?virtueRewards.get(0).getVirtue().getName():null;
        }
      };
      DefaultTableColumnController<Rewards,String> virtueColumn=new DefaultTableColumnController<Rewards,String>(RewardsColumnIds.VIRTUE.name(),"Virtue",String.class,virtueCell); // I18n
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
          return (emoteRewards.size()>0)?emoteRewards.get(0).getName():null;
        }
      };
      DefaultTableColumnController<Rewards,String> emoteColumn=new DefaultTableColumnController<Rewards,String>(RewardsColumnIds.EMOTE.name(),"Emote",String.class,emoteCell); // I18n
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
          return (traitRewards.size()>0)?traitRewards.get(0).getName():null;
        }
      };
      DefaultTableColumnController<Rewards,String> traitColumn=new DefaultTableColumnController<Rewards,String>(RewardsColumnIds.TRAIT.name(),"Trait",String.class,traitCell); // I18n
      traitColumn.setWidthSpecs(100,300,200);
      ret.add(traitColumn);
    }
    // Faction column
    ret.add(buildFactionNameColumn(parent));
    // Faction amount column
    ret.add(buildFactionAmountColumn());
    // XP column
    ret.add(buildXPColumn());
    // Item XP column
    ret.add(buildItemXPColumn());
    // Mount XP column
    ret.add(buildMountXPColumn());
    // Virtue XP column
    {
      CellDataProvider<Rewards,Integer> virtueXpCell=new CellDataProvider<Rewards,Integer>()
      {
        @Override
        public Integer getData(Rewards rewards)
        {
          int virtueXp=rewards.getVirtueXp();
          return (virtueXp>0)?Integer.valueOf(virtueXp):null;
        }
      };
      DefaultTableColumnController<Rewards,Integer> virtueXpColumn=new DefaultTableColumnController<Rewards,Integer>(RewardsColumnIds.VIRTUE_XP.name(),"Virtue XP",Integer.class,virtueXpCell); // I18n
      StatColumnsUtils.configureIntegerColumn(virtueXpColumn);
      ret.add(virtueXpColumn);
    }
    return ret;
  }

  /**
   * Build a 'XP' column.
   * @return a column.
   */
  public static DefaultTableColumnController<Rewards,Integer>  buildXPColumn()
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
    DefaultTableColumnController<Rewards,Integer> xpColumn=new DefaultTableColumnController<Rewards,Integer>(RewardsColumnIds.XP.name(),"XP",Integer.class,xpCell); // I18n
    StatColumnsUtils.configureIntegerColumn(xpColumn);
    return xpColumn;
  }

  /**
   * Build an 'item XP' column.
   * @return a column.
   */
  public static DefaultTableColumnController<Rewards,Integer> buildItemXPColumn()
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
    DefaultTableColumnController<Rewards,Integer> itemXpColumn=new DefaultTableColumnController<Rewards,Integer>(RewardsColumnIds.ITEM_XP.name(),"Item XP",Integer.class,itemXpCell); // I18n
    StatColumnsUtils.configureIntegerColumn(itemXpColumn);
    return itemXpColumn;
  }

  /**
   * Build a 'mount XP' column.
   * @return a column.
   */
  public static DefaultTableColumnController<Rewards,Integer> buildMountXPColumn()
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
    DefaultTableColumnController<Rewards,Integer> mountXpColumn=new DefaultTableColumnController<Rewards,Integer>(RewardsColumnIds.MOUNT_XP.name(),"Mount XP",Integer.class,mountXpCell); // I18n
    StatColumnsUtils.configureIntegerColumn(mountXpColumn);
    return mountXpColumn;
  }

  /**
   * Build a 'faction name' column.
   * @param parent Parent controller.
   * @return a column.
   */
  public static DefaultTableColumnController<Rewards,String> buildFactionNameColumn(AreaController parent)
  {
    CellDataProvider<Rewards,String> factionCell=new CellDataProvider<Rewards,String>()
    {
      @Override
      public String getData(Rewards rewards)
      {
        List<ReputationReward> reputationRewards=rewards.getRewardElementsOfClass(ReputationReward.class);
        if (!reputationRewards.isEmpty())
        {
          Faction faction=reputationRewards.get(0).getFaction();
          String rawFactionName=faction.getName();
          String factionName=ContextRendering.render(parent,rawFactionName);
          return factionName;
        }
        return null;
      }
    };
    DefaultTableColumnController<Rewards,String> factionColumn=new DefaultTableColumnController<Rewards,String>(RewardsColumnIds.FACTION.name(),"Faction",String.class,factionCell); // I18n
    factionColumn.setWidthSpecs(100,300,200);
    return factionColumn;
  }

  /**
   * Build a 'faction amount' column.
   * @return a column.
   */
  public static DefaultTableColumnController<Rewards,Integer> buildFactionAmountColumn()
  {
    CellDataProvider<Rewards,Integer> reputationAmountCell=new CellDataProvider<Rewards,Integer>()
    {
      @Override
      public Integer getData(Rewards rewards)
      {
        List<ReputationReward> reputationRewards=rewards.getRewardElementsOfClass(ReputationReward.class);
        return (reputationRewards.size()>0)?Integer.valueOf(reputationRewards.get(0).getAmount()):null;
      }
    };
    DefaultTableColumnController<Rewards,Integer> reputationAmountColumn=new DefaultTableColumnController<Rewards,Integer>(RewardsColumnIds.REPUTATION_AMOUNT.name(),"Rep Amount",Integer.class,reputationAmountCell); // I18n
    reputationAmountColumn.setWidthSpecs(60,60,60);
    return reputationAmountColumn;
  }
}
