package delta.games.lotro.gui.lore.items.legendary.passives;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.Sort;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.lore.items.legendary.passives.Passive;

/**
 * Builder for a table that shows passives.
 * @author DAM
 */
public class PassivesTableBuilder
{
  /**
   * Build a table to show passives.
   * @param passives Passives to show.
   * @param itemLevel Item level to use for stats computations.
   * @return A new table controller.
   */
  public static GenericTableController<Passive> buildTable(List<Passive> passives, final int itemLevel)
  {
    DataProvider<Passive> provider=new ListDataProvider<Passive>(passives);
    GenericTableController<Passive> table=new GenericTableController<Passive>(provider);

    // Label column
    {
      CellDataProvider<Passive,String> labelCell=new CellDataProvider<Passive,String>()
      {
        @Override
        public String getData(Passive item)
        {
          String label=item.getStatsProvider().getLabel();
          return label;
        }
      };
      DefaultTableColumnController<Passive,String> labelColumn=new DefaultTableColumnController<Passive,String>(PassiveColumnIds.LABEL.name(),"Name",String.class,labelCell);
      labelColumn.setWidthSpecs(250,-1,250);
      table.addColumnController(labelColumn);
    }
    // Value column
    {
      CellDataProvider<Passive,Number> valueCell=new CellDataProvider<Passive,Number>()
      {
        @Override
        public Number getData(Passive item)
        {
          StatsProvider statsProvider=item.getStatsProvider();
          BasicStatsSet stats=statsProvider.getStats(1,itemLevel);
          StatDescription stat=statsProvider.getFirstStat();
          return stats.getStat(stat);
        }
      };
      DefaultTableColumnController<Passive,Number> valueColumn=new DefaultTableColumnController<Passive,Number>(PassiveColumnIds.VALUE.name(),"Value",Number.class,valueCell);
      valueColumn.setWidthSpecs(100,100,100);
      table.addColumnController(valueColumn);
    }
    // Sort
    String sort=Sort.SORT_ASCENDING+PassiveColumnIds.LABEL;
    table.setSort(Sort.buildFromString(sort));
    // Displayed columns
    List<String> columnsIds=getDefaultColumnIds();
    table.getColumnsManager().setColumns(columnsIds);
    return table;
  }

  private static List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(PassiveColumnIds.LABEL.name());
    columnsIds.add(PassiveColumnIds.VALUE.name());
    return columnsIds;
  }
}
