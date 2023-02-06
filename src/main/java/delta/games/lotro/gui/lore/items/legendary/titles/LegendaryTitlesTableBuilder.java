package delta.games.lotro.gui.lore.items.legendary.titles;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.Sort;
import delta.games.lotro.common.enums.Genus;
import delta.games.lotro.common.enums.LegendaryTitleCategory;
import delta.games.lotro.common.enums.LegendaryTitleTier;
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
      CellDataProvider<LegendaryTitle,LegendaryTitleCategory> categoryCell=new CellDataProvider<LegendaryTitle,LegendaryTitleCategory>()
      {
        @Override
        public LegendaryTitleCategory getData(LegendaryTitle item)
        {
          return item.getCategory();
        }
      };
      DefaultTableColumnController<LegendaryTitle,LegendaryTitleCategory> categoryColumn=new DefaultTableColumnController<LegendaryTitle,LegendaryTitleCategory>(LegendaryTitleColumnIds.CATEGORY.name(),"Category",LegendaryTitleCategory.class,categoryCell);
      categoryColumn.setWidthSpecs(250,300,250);
      table.addColumnController(categoryColumn);
    }
    // Tier column
    {
      CellDataProvider<LegendaryTitle,LegendaryTitleTier> tierCell=new CellDataProvider<LegendaryTitle,LegendaryTitleTier>()
      {
        @Override
        public LegendaryTitleTier getData(LegendaryTitle item)
        {
          return item.getTier();
        }
      };
      DefaultTableColumnController<LegendaryTitle,LegendaryTitleTier> tierColumn=new DefaultTableColumnController<LegendaryTitle,LegendaryTitleTier>(LegendaryTitleColumnIds.TIER.name(),"Tier",LegendaryTitleTier.class,tierCell);
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
      CellDataProvider<LegendaryTitle,Genus> slayerCell=new CellDataProvider<LegendaryTitle,Genus>()
      {
        @Override
        public Genus getData(LegendaryTitle item)
        {
          return item.getSlayerGenusType();
        }
      };
      DefaultTableColumnController<LegendaryTitle,Genus> slayerColumn=new DefaultTableColumnController<LegendaryTitle,Genus>(LegendaryTitleColumnIds.SLAYER.name(),"Slayer",Genus.class,slayerCell);
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
