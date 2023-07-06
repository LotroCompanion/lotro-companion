package delta.games.lotro.gui.character.storage.statistics;

import java.util.List;

import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.games.lotro.character.storage.statistics.reputation.StorageFactionStats;
import delta.games.lotro.character.storage.statistics.reputation.StorageReputationStats;
import delta.games.lotro.gui.common.statistics.reputation.ReputationTableController;

/**
 * Controller for a table that shows reputations from stored items.
 * @author DAM
 */
public class StorageReputationTableController extends ReputationTableController<StorageFactionStats>
{
  private static final String COUNT="ITEMS_COUNT";

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param stats Stats to show.
   */
  public StorageReputationTableController(AreaController parent, StorageReputationStats stats)
  {
    super(parent,stats);
  }

  protected void defineColumns(GenericTableController<StorageFactionStats> table)
  {
    super.defineColumns(table);
    // Count column
    {
      CellDataProvider<StorageFactionStats,Integer> countCell=new CellDataProvider<StorageFactionStats,Integer>()
      {
        @Override
        public Integer getData(StorageFactionStats item)
        {
          Integer count=Integer.valueOf(item.getItemsCount());
          return count;
        }
      };
      DefaultTableColumnController<StorageFactionStats,Integer> countColumn=new DefaultTableColumnController<StorageFactionStats,Integer>(COUNT,"Items",Integer.class,countCell); // I18n
      countColumn.setWidthSpecs(60,60,60);
      table.addColumnController(countColumn);
    }
  }

  protected List<String> getColumnIds()
  {
    List<String> columnIds=super.getColumnIds();
    columnIds.add(COUNT);
    return columnIds;
  }
}
