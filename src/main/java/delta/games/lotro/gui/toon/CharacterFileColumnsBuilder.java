package delta.games.lotro.gui.toon;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.details.CharacterDetails;
import delta.games.lotro.common.Duration;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.money.comparator.MoneyComparator;
import delta.games.lotro.gui.items.chooser.ItemsTableBuilder;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;
import delta.games.lotro.utils.Formats;

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
      DefaultTableColumnController<CharacterFile,Long> xpColumn=new DefaultTableColumnController<CharacterFile,Long>(ToonsTableColumnIds.XP.name(),"XP",Long.class,xpCell);
      xpColumn.setWidthSpecs(80,80,80);
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
      DefaultTableColumnController<CharacterFile,Integer> cooldownColumn=new DefaultTableColumnController<CharacterFile,Integer>(ToonsTableColumnIds.INGAME_TIME.name(),"In-game Time",Integer.class,cooldownCell);
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
      DefaultTableColumnController<CharacterFile,Money> moneyColumn=new DefaultTableColumnController<CharacterFile,Money>(ToonsTableColumnIds.MONEY.name(),"Money",Money.class,moneyCell);
      moneyColumn.setWidthSpecs(180,180,180);
      moneyColumn.setCellRenderer(ItemsTableBuilder.buildMoneyCellRenderer());
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
      DefaultTableColumnController<CharacterFile,Long> lastLogoutColumn=new DefaultTableColumnController<CharacterFile,Long>(ToonsTableColumnIds.LAST_LOGOUT_DATE.name(),"Last logout",Long.class,lastLogoutCell);
      lastLogoutColumn.setWidthSpecs(120,120,120);
      lastLogoutColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
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
              titleName=title.getName();
            }
          }
          return titleName;
        }
      };
      DefaultTableColumnController<CharacterFile,String> titleColumn=new DefaultTableColumnController<CharacterFile,String>(ToonsTableColumnIds.TITLE.name(),"Title",String.class,titleCell);
      titleColumn.setWidthSpecs(100,-1,200);
      ret.add(titleColumn);
    }

    return ret;
  }
}
