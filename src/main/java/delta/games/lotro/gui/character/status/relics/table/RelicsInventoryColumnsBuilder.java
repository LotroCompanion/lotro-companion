package delta.games.lotro.gui.character.status.relics.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.status.relics.RelicsInventoryEntry;
import delta.games.lotro.gui.lore.items.legendary.relics.RelicsTableBuilder;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Builds column definitions for RelicInventoryEntry data.
 * @author DAM
 */
public class RelicsInventoryColumnsBuilder
{
  /**
   * Identifier of the "Count" column.
   */
  public static final String COUNT_COLUMN="COUNT";

  /**
   * Build the columns to show the attributes of a relic inventory entry.
   * @return a list of columns.
   */
  public static List<TableColumnController<RelicsInventoryEntry,?>> buildRelicInventoryEntryColumns()
  {
    List<TableColumnController<RelicsInventoryEntry,?>> ret=new ArrayList<TableColumnController<RelicsInventoryEntry,?>>();
    // Relic
    {
      CellDataProvider<RelicsInventoryEntry,Relic> provider=new CellDataProvider<RelicsInventoryEntry,Relic>()
      {
        @Override
        public Relic getData(RelicsInventoryEntry entry)
        {
          return entry.getRelic();
        }
      };
      for(TableColumnController<Relic,?> relicColumns : RelicsTableBuilder.buildColumns())
      {
        @SuppressWarnings("unchecked")
        TableColumnController<Relic,Object> c=(TableColumnController<Relic,Object>)relicColumns;
        ProxiedTableColumnController<RelicsInventoryEntry,Relic,Object> column=new ProxiedTableColumnController<RelicsInventoryEntry,Relic,Object>(c,provider);
        ret.add(column);
      }
    }
    // Count
    {
      CellDataProvider<RelicsInventoryEntry,Integer> countCell=new CellDataProvider<RelicsInventoryEntry,Integer>()
      {
        @Override
        public Integer getData(RelicsInventoryEntry item)
        {
          return Integer.valueOf(item.getCount());
        }
      };
      DefaultTableColumnController<RelicsInventoryEntry,Integer> countColumn=new DefaultTableColumnController<RelicsInventoryEntry,Integer>(COUNT_COLUMN,"Count",Integer.class,countCell); // I18n
      countColumn.setWidthSpecs(55,55,50);
      ret.add(countColumn);
    }
    return ret;
  }
}
