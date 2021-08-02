package delta.games.lotro.gui.lore.items.legendary.non_imbued;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.Sort;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacy;

/**
 * Builder for a table that shows tiered, non-imbued legacies.
 * @author DAM
 */
public class TieredNonImbuedLegaciesTableBuilder
{
  /**
   * Build a table to show tiered non-imbued legacies.
   * @param legacies Legacies to show.
   * @return A new table controller.
   */
  public static GenericTableController<TieredNonImbuedLegacy> buildTable(List<TieredNonImbuedLegacy> legacies)
  {
    DataProvider<TieredNonImbuedLegacy> provider=new ListDataProvider<TieredNonImbuedLegacy>(legacies);
    GenericTableController<TieredNonImbuedLegacy> table=new GenericTableController<TieredNonImbuedLegacy>(provider);

    // Type column
    {
      CellDataProvider<TieredNonImbuedLegacy,String> typeCell=new CellDataProvider<TieredNonImbuedLegacy,String>()
      {
        @Override
        public String getData(TieredNonImbuedLegacy item)
        {
          return item.isMajor()?"Major":"Minor";
        }
      };
      DefaultTableColumnController<TieredNonImbuedLegacy,String> typeColumn=new DefaultTableColumnController<TieredNonImbuedLegacy,String>(TieredNonImbuedLegacyColumnIds.TYPE.name(),"Type",String.class,typeCell);
      typeColumn.setWidthSpecs(60,60,60);
      table.addColumnController(typeColumn);
    }
    // Label column
    {
      CellDataProvider<TieredNonImbuedLegacy,String> labelCell=new CellDataProvider<TieredNonImbuedLegacy,String>()
      {
        @Override
        public String getData(TieredNonImbuedLegacy item)
        {
          String label=item.getStat().getName();
          return label;
        }
      };
      DefaultTableColumnController<TieredNonImbuedLegacy,String> labelColumn=new DefaultTableColumnController<TieredNonImbuedLegacy,String>(TieredNonImbuedLegacyColumnIds.LABEL.name(),"Name",String.class,labelCell);
      labelColumn.setWidthSpecs(250,-1,250);
      table.addColumnController(labelColumn);
    }
    // Sort
    String sort=Sort.SORT_DESCENDING+TieredNonImbuedLegacyColumnIds.TYPE+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+TieredNonImbuedLegacyColumnIds.LABEL;
    table.setSort(Sort.buildFromString(sort));
    // Displayed columns
    List<String> columnsIds=getDefaultColumnIds();
    table.getColumnsManager().setColumns(columnsIds);
    return table;
  }

  private static List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(TieredNonImbuedLegacyColumnIds.TYPE.name());
    columnsIds.add(TieredNonImbuedLegacyColumnIds.LABEL.name());
    return columnsIds;
  }
}
