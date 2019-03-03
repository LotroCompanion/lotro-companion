package delta.games.lotro.gui.items.legendary.imbued;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.Sort;
import delta.games.lotro.lore.items.legendary.LegacyType;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacy;

/**
 * Builder for a table that shows imbued legacies.
 * @author DAM
 */
public class ImbuedLegaciesTableBuilder
{
  /**
   * Build a table to show imbued legacies.
   * @param legacies Imbued legacies to show.
   * @return A new table controller.
   */
  public static GenericTableController<ImbuedLegacy> buildTable(List<ImbuedLegacy> legacies)
  {
    DataProvider<ImbuedLegacy> provider=new ListDataProvider<ImbuedLegacy>(legacies);
    GenericTableController<ImbuedLegacy> table=new GenericTableController<ImbuedLegacy>(provider);

    // ID column
    {
      CellDataProvider<ImbuedLegacy,Long> idCell=new CellDataProvider<ImbuedLegacy,Long>()
      {
        @Override
        public Long getData(ImbuedLegacy item)
        {
          return Long.valueOf(item.getIdentifier());
        }
      };
      DefaultTableColumnController<ImbuedLegacy,Long> idColumn=new DefaultTableColumnController<ImbuedLegacy,Long>(ImbuedLegacyColumnIds.ID.name(),"ID",Long.class,idCell);
      idColumn.setWidthSpecs(90,90,50);
      table.addColumnController(idColumn);
    }
    // Type column
    {
      CellDataProvider<ImbuedLegacy,LegacyType> typeCell=new CellDataProvider<ImbuedLegacy,LegacyType>()
      {
        @Override
        public LegacyType getData(ImbuedLegacy item)
        {
          return item.getType();
        }
      };
      DefaultTableColumnController<ImbuedLegacy,LegacyType> typeColumn=new DefaultTableColumnController<ImbuedLegacy,LegacyType>(ImbuedLegacyColumnIds.TYPE.name(),"Type",LegacyType.class,typeCell);
      typeColumn.setWidthSpecs(60,60,60);
      table.addColumnController(typeColumn);
    }
    // Label column
    {
      CellDataProvider<ImbuedLegacy,String> labelCell=new CellDataProvider<ImbuedLegacy,String>()
      {
        @Override
        public String getData(ImbuedLegacy item)
        {
          String label=item.getLabel();
          return label;
        }
      };
      DefaultTableColumnController<ImbuedLegacy,String> labelColumn=new DefaultTableColumnController<ImbuedLegacy,String>(ImbuedLegacyColumnIds.LABEL.name(),"Label",String.class,labelCell);
      labelColumn.setWidthSpecs(250,-1,250);
      table.addColumnController(labelColumn);
    }
    // Sort
    String sort=Sort.SORT_DESCENDING+ImbuedLegacyColumnIds.TYPE+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+ImbuedLegacyColumnIds.LABEL;
    table.setSort(Sort.buildFromString(sort));
    // Displayed columns
    List<String> columnsIds=getDefaultColumnIds();
    table.getColumnsManager().setColumns(columnsIds);
    return table;
  }

  private static List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(ImbuedLegacyColumnIds.TYPE.name());
    columnsIds.add(ImbuedLegacyColumnIds.LABEL.name());
    return columnsIds;
  }
}
