package delta.games.lotro.gui.toon;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.details.CharacterDetails;
import delta.games.lotro.common.Duration;
import delta.games.lotro.common.geo.Position;
import delta.games.lotro.common.geo.PositionUtils;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.money.comparator.MoneyComparator;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.utils.MoneyCellRenderer;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;
import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Vocation;
import delta.games.lotro.lore.maps.Zone;
import delta.games.lotro.lore.maps.ZoneUtils;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Builds column definitions for CharacterFile data.
 * @author DAM
 */
public class CharacterFileColumnsBuilder
{
  /**
   * Build the columns to show a <code>CharacterFile</code>.
   * @return a list of columns.
   */
  public static List<TableColumnController<CharacterFile,?>> build()
  {
    List<TableColumnController<CharacterFile,?>> columns=new ArrayList<TableColumnController<CharacterFile,?>>();
    // Summary columns
    List<TableColumnController<CharacterSummary,?>> summaryColumns=CharacterSummaryColumnsBuilder.buildCharacterSummaryColumns();
    CellDataProvider<CharacterFile,CharacterSummary> dataProvider=new CellDataProvider<CharacterFile,CharacterSummary>()
    {
      @Override
      public CharacterSummary getData(CharacterFile characterFile)
      {
        CharacterSummary summary=characterFile.getSummary();
        if (summary==null)
        {
          summary=new CharacterSummary();
        }
        return summary;
      }
    };
    for(TableColumnController<CharacterSummary,?> summaryColumn:summaryColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<CharacterSummary,Object> c=(TableColumnController<CharacterSummary,Object>)summaryColumn;
      TableColumnController<CharacterFile,Object> proxiedColumn=new ProxiedTableColumnController<CharacterFile,CharacterSummary,Object>(c,dataProvider);
      columns.add(proxiedColumn);
    }
    // Details columns
    List<TableColumnController<CharacterFile,?>> detailsColumns=getDetailsColumns();
    columns.addAll(detailsColumns);
    return columns;
  }

  private static List<TableColumnController<CharacterFile,?>> getDetailsColumns()
  {
    List<TableColumnController<CharacterFile,?>> ret=new ArrayList<TableColumnController<CharacterFile,?>>();
    // XP column
    {
      CellDataProvider<CharacterFile,Long> xpCell=new CellDataProvider<CharacterFile,Long>()
      {
        @Override
        public Long getData(CharacterFile file)
        {
          CharacterDetails data=file.getDetails();
          return Long.valueOf(data.getXp());
        }
      };
      DefaultTableColumnController<CharacterFile,Long> xpColumn=new DefaultTableColumnController<CharacterFile,Long>(ToonsTableColumnIds.XP.name(),"XP",Long.class,xpCell); // I18n
      StatColumnsUtils.configureLongColumn(xpColumn);
      ret.add(xpColumn);
    }
    // In-game time column
    {
      CellDataProvider<CharacterFile,Integer> cooldownCell=new CellDataProvider<CharacterFile,Integer>()
      {
        @Override
        public Integer getData(CharacterFile file)
        {
          CharacterDetails data=file.getDetails();
          return Integer.valueOf(data.getIngameTime());
        }
      };
      DefaultTableColumnController<CharacterFile,Integer> cooldownColumn=new DefaultTableColumnController<CharacterFile,Integer>(ToonsTableColumnIds.INGAME_TIME.name(),"In-game Time",Integer.class,cooldownCell); // I18n
      cooldownColumn.setWidthSpecs(120,120,120);
      DefaultTableCellRenderer renderer=new DefaultTableCellRenderer()
      {
        @Override
        public void setValue(Object value)
        {
          setHorizontalAlignment(SwingConstants.CENTER);
          setText((value == null) ? "" : Duration.getDurationString(((Integer)value).intValue()));
        }
      };
      cooldownColumn.setCellRenderer(renderer);
      ret.add(cooldownColumn);
    }
    // Money
    {
      CellDataProvider<CharacterFile,Money> moneyCell=new CellDataProvider<CharacterFile,Money>()
      {
        @Override
        public Money getData(CharacterFile file)
        {
          CharacterDetails data=file.getDetails();
          return data.getMoney();
        }
      };
      DefaultTableColumnController<CharacterFile,Money> moneyColumn=new DefaultTableColumnController<CharacterFile,Money>(ToonsTableColumnIds.MONEY.name(),"Money",Money.class,moneyCell); // I18n
      moneyColumn.setWidthSpecs(180,180,180);
      moneyColumn.setCellRenderer(new MoneyCellRenderer());
      moneyColumn.setComparator(new MoneyComparator());
      ret.add(moneyColumn);
    }
    // Last logout time
    {
      CellDataProvider<CharacterFile,Long> lastLogoutCell=new CellDataProvider<CharacterFile,Long>()
      {
        public Long getData(CharacterFile file)
        {
          CharacterDetails data=file.getDetails();
          return data.getLastLogoutDate();
        }
      };
      DefaultTableColumnController<CharacterFile,Long> lastLogoutColumn=new DefaultTableColumnController<CharacterFile,Long>(ToonsTableColumnIds.LAST_LOGOUT_DATE.name(),"Last logout",Long.class,lastLogoutCell); // I18n
      StatColumnsUtils.configureDateTimeColumn(lastLogoutColumn);
      ret.add(lastLogoutColumn);
    }
    // Title column
    {
      CellDataProvider<CharacterFile,String> titleCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile file)
        {
          String titleName=null;
          Integer titleId=file.getDetails().getCurrentTitleId();
          if (titleId!=null)
          {
            TitleDescription title=TitlesManager.getInstance().getTitle(titleId.intValue());
            if (title!=null)
            {
              titleName=ContextRendering.render(file.getSummary(),title.getRawName());
            }
          }
          return titleName;
        }
      };
      DefaultTableColumnController<CharacterFile,String> titleColumn=new DefaultTableColumnController<CharacterFile,String>(ToonsTableColumnIds.TITLE.name(),"Title",String.class,titleCell); // I18n
      titleColumn.setWidthSpecs(100,-1,200);
      ret.add(titleColumn);
    }
    // Area
    {
      CellDataProvider<CharacterFile,String> areaCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile file)
        {
          String zoneName=null;
          Integer areaID=file.getDetails().getAreaID();
          if (areaID!=null)
          {
            Zone zone=ZoneUtils.getZone(areaID.intValue());
            if (zone!=null)
            {
              zoneName=zone.getName();
            }
          }
          return zoneName;
        }
      };
      DefaultTableColumnController<CharacterFile,String> areaColumn=new DefaultTableColumnController<CharacterFile,String>(ToonsTableColumnIds.AREA.name(),"Area",String.class,areaCell); // I18n
      areaColumn.setWidthSpecs(80,250,250);
      ret.add(areaColumn);
    }
    // Dungeon
    {
      CellDataProvider<CharacterFile,String> dungeonCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile file)
        {
          String zoneName=null;
          Integer dungeonID=file.getDetails().getDungeonID();
          if (dungeonID!=null)
          {
            Zone zone=ZoneUtils.getZone(dungeonID.intValue());
            if (zone!=null)
            {
              zoneName=zone.getName();
            }
          }
          return zoneName;
        }
      };
      DefaultTableColumnController<CharacterFile,String> dungeonColumn=new DefaultTableColumnController<CharacterFile,String>(ToonsTableColumnIds.DUNGEON.name(),"Dungeon",String.class,dungeonCell); // I18n
      dungeonColumn.setWidthSpecs(80,250,250);
      ret.add(dungeonColumn);
    }
    // Vocation column
    if (!LotroCoreConfig.isLive())
    {
      CellDataProvider<CharacterFile,String> vocationCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile file)
        {
          String vocationName=null;
          Integer vocationId=file.getDetails().getCurrentVocationId();
          if (vocationId!=null)
          {
            CraftingData craftingData=CraftingSystem.getInstance().getData();
            Vocation vocation=craftingData.getVocationsRegistry().getVocationById(vocationId.intValue());
            if (vocation!=null)
            {
              vocationName=ContextRendering.render(file.getSummary(),vocation.getName());
            }
          }
          return vocationName;
        }
      };
      DefaultTableColumnController<CharacterFile,String> vocationColumn=new DefaultTableColumnController<CharacterFile,String>(ToonsTableColumnIds.VOCATION.name(),"Vocation",String.class,vocationCell); // I18n
      vocationColumn.setWidthSpecs(100,100,100);
      ret.add(vocationColumn);
    }
    // Position
    ret.add(getPositionColumn());
    return ret;
  }

  private static DefaultTableColumnController<CharacterFile,String> getPositionColumn()
  {
    CellDataProvider<CharacterFile,String> positionCell=new CellDataProvider<CharacterFile,String>()
    {
      @Override
      public String getData(CharacterFile file)
      {
        String positionStr=null;
        Position position=file.getDetails().getPosition();
        if (position!=null)
        {
          positionStr=PositionUtils.getLabel(position);
        }
        return positionStr;
      }
    };
    DefaultTableColumnController<CharacterFile,String> positionColumn=new DefaultTableColumnController<CharacterFile,String>(ToonsTableColumnIds.POSITION.name(),"Position",String.class,positionCell); // I18n
    positionColumn.setWidthSpecs(100,200,200);
    return positionColumn;
  }
}
