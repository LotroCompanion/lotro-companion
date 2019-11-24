package delta.games.lotro.gui.items.legendary.titles;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.Sort;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitle;

/**
 * Builder for a table that shows legendary titles.
 * @author DAM
 */
public class LegendaryTitlesTableBuilder
{
  /**
   * Build a table to show legendary titles.
   * @param titles Legendary titles to show.
   * @return A new table controller.
   */
  public static GenericTableController<LegendaryTitle> buildTable(List<LegendaryTitle> titles)
  {
    DataProvider<LegendaryTitle> provider=new ListDataProvider<LegendaryTitle>(titles);
    GenericTableController<LegendaryTitle> table=new GenericTableController<LegendaryTitle>(provider);

    // ID column
    if (UiConfiguration.showTechnicalColumns())
   {
      CellDataProvider<LegendaryTitle,Long> idCell=new CellDataProvider<LegendaryTitle,Long>()
      {
        @Override
        public Long getData(LegendaryTitle item)
        {
          return Long.valueOf(item.getIdentifier());
        }
      };
      DefaultTableColumnController<LegendaryTitle,Long> idColumn=new DefaultTableColumnController<LegendaryTitle,Long>(LegendaryTitleColumnIds.ID.name(),"ID",Long.class,idCell);
      idColumn.setWidthSpecs(90,90,50);
      table.addColumnController(idColumn);
    }
    // Name column
    {
      CellDataProvider<LegendaryTitle,String> nameCell=new CellDataProvider<LegendaryTitle,String>()
      {
        @Override
        public String getData(LegendaryTitle item)
        {
          return item.getName();
        }
      };
      DefaultTableColumnController<LegendaryTitle,String> nameColumn=new DefaultTableColumnController<LegendaryTitle,String>(LegendaryTitleColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(200,230,210);
      table.addColumnController(nameColumn);
    }
    // Category column
    {
      CellDataProvider<LegendaryTitle,String> categoryCell=new CellDataProvider<LegendaryTitle,String>()
      {
        @Override
        public String getData(LegendaryTitle item)
        {
          return item.getCategory();
        }
      };
      DefaultTableColumnController<LegendaryTitle,String> categoryColumn=new DefaultTableColumnController<LegendaryTitle,String>(LegendaryTitleColumnIds.CATEGORY.name(),"Category",String.class,categoryCell);
      categoryColumn.setWidthSpecs(250,300,250);
      table.addColumnController(categoryColumn);
    }
    // Tier column
    {
      CellDataProvider<LegendaryTitle,Integer> tierCell=new CellDataProvider<LegendaryTitle,Integer>()
      {
        @Override
        public Integer getData(LegendaryTitle item)
        {
          return Integer.valueOf(item.getTier());
        }
      };
      DefaultTableColumnController<LegendaryTitle,Integer> tierColumn=new DefaultTableColumnController<LegendaryTitle,Integer>(LegendaryTitleColumnIds.TIER.name(),"Tier",Integer.class,tierCell);
      tierColumn.setWidthSpecs(40,40,40);
      table.addColumnController(tierColumn);
    }
    // Damage type column
    {
      CellDataProvider<LegendaryTitle,DamageType> damageTypeCell=new CellDataProvider<LegendaryTitle,DamageType>()
      {
        @Override
        public DamageType getData(LegendaryTitle item)
        {
          return item.getDamageType();
        }
      };
      DefaultTableColumnController<LegendaryTitle,DamageType> damageTypeColumn=new DefaultTableColumnController<LegendaryTitle,DamageType>(LegendaryTitleColumnIds.DAMAGE_TYPE.name(),"Damage Type",DamageType.class,damageTypeCell);
      damageTypeColumn.setWidthSpecs(120,120,120);
      table.addColumnController(damageTypeColumn);
    }
    // Slayer column
    {
      CellDataProvider<LegendaryTitle,String> slayerCell=new CellDataProvider<LegendaryTitle,String>()
      {
        @Override
        public String getData(LegendaryTitle item)
        {
          return item.getSlayerGenusType();
        }
      };
      DefaultTableColumnController<LegendaryTitle,String> slayerColumn=new DefaultTableColumnController<LegendaryTitle,String>(LegendaryTitleColumnIds.SLAYER.name(),"Slayer",String.class,slayerCell);
      slayerColumn.setWidthSpecs(120,120,120);
      table.addColumnController(slayerColumn);
    }
    // Stats column
    {
      CellDataProvider<LegendaryTitle,String> statsCell=new CellDataProvider<LegendaryTitle,String>()
      {
        @Override
        public String getData(LegendaryTitle item)
        {
          String statsStr=item.getStats().toString();
          return statsStr;
        }
      };
      DefaultTableColumnController<LegendaryTitle,String> statsColumn=new DefaultTableColumnController<LegendaryTitle,String>(LegendaryTitleColumnIds.STATS.name(),"Stats",String.class,statsCell);
      statsColumn.setWidthSpecs(250,-1,250);
      table.addColumnController(statsColumn);
    }
    // Sort
    String sort=Sort.SORT_ASCENDING+LegendaryTitleColumnIds.NAME;
    table.setSort(Sort.buildFromString(sort));
    // Displayed columns
    List<String> columnsIds=getDefaultColumnIds();
    table.getColumnsManager().setColumns(columnsIds);
    return table;
  }

  private static List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(LegendaryTitleColumnIds.NAME.name());
    columnsIds.add(LegendaryTitleColumnIds.CATEGORY.name());
    columnsIds.add(LegendaryTitleColumnIds.TIER.name());
    columnsIds.add(LegendaryTitleColumnIds.DAMAGE_TYPE.name());
    columnsIds.add(LegendaryTitleColumnIds.SLAYER.name());
    columnsIds.add(LegendaryTitleColumnIds.STATS.name());
    return columnsIds;
  }
}
