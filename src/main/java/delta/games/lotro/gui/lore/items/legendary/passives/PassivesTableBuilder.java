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
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsProvider;

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
  public static GenericTableController<Effect> buildTable(List<Effect> passives, final int itemLevel)
  {
    DataProvider<Effect> provider=new ListDataProvider<Effect>(passives);
    GenericTableController<Effect> table=new GenericTableController<Effect>(provider);

    // Label column
    {
      CellDataProvider<Effect,String> labelCell=new CellDataProvider<Effect,String>()
      {
        @Override
        public String getData(Effect item)
        {
          String label=item.getStatsProvider().getLabel();
          return label;
        }
      };
      DefaultTableColumnController<Effect,String> labelColumn=new DefaultTableColumnController<Effect,String>(PassiveColumnIds.LABEL.name(),"Name",String.class,labelCell);
      labelColumn.setWidthSpecs(250,-1,250);
      table.addColumnController(labelColumn);
    }
    // Value column
    {
      CellDataProvider<Effect,Number> valueCell=new CellDataProvider<Effect,Number>()
      {
        @Override
        public Number getData(Effect item)
        {
          StatsProvider statsProvider=item.getStatsProvider();
          BasicStatsSet stats=statsProvider.getStats(1,itemLevel);
          StatDescription stat=statsProvider.getFirstStat();
          return stats.getStat(stat);
        }
      };
      DefaultTableColumnController<Effect,Number> valueColumn=new DefaultTableColumnController<Effect,Number>(PassiveColumnIds.VALUE.name(),"Value",Number.class,valueCell);
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
