package delta.games.lotro.gui.character.storage.cosmetics;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.ColumnsUtils;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.cosmetics.CosmeticItemsGroup;
import delta.games.lotro.gui.character.storage.StoredItemsTableController;
import delta.games.lotro.gui.lore.items.table.ItemColumnIds;
import delta.games.lotro.gui.lore.items.table.ItemsTableBuilder;
import delta.games.lotro.lore.items.Item;

/**
 * Builder for a table that items with the same cosmetics.
 * @author DAM
 */
public class SameCosmeticsTableBuilder
{
  /**
   * Build a table to show 'same cosmetics' items.
   * @param parent Parent window controller.
   * @param rows Rows to show.
   * @return A new table controller.
   */
  public static GenericTableController<SameCosmeticsTableRow> buildTable(WindowController parent, List<SameCosmeticsTableRow> rows)
  {
    DataProvider<SameCosmeticsTableRow> provider=new ListDataProvider<SameCosmeticsTableRow>(rows);
    GenericTableController<SameCosmeticsTableRow> table=new GenericTableController<SameCosmeticsTableRow>(provider);
    List<TableColumnController<SameCosmeticsTableRow,?>> columns=initColumns(parent);

    TableColumnsManager<SameCosmeticsTableRow> columnsManager=table.getColumnsManager();
    for(TableColumnController<SameCosmeticsTableRow,?> column : columns)
    {
      columnsManager.addColumnController(column,false);
    }
    List<String> columnsIds=getDefaultColumnIds();
    columnsManager.setColumns(columnsIds);
    StoredItemsTableController.configureTable(parent,table);
    return table;
  }

  /**
   * Get the default columns IDs for a table of item instances.
   * @return a list of column IDs.
   */
  public static List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(ItemColumnIds.ICON.name());
    columnsIds.add(SameCosmeticsColumnIds.GROUP_ID.name());
    columnsIds.add(SameCosmeticsColumnIds.COSMETIC_ID.name());
    columnsIds.add(ItemColumnIds.NAME.name());
    columnsIds.add(StoredItemsTableController.OWNER_COLUMN);
    columnsIds.add(StoredItemsTableController.LOCATION_COLUMN);
    return columnsIds;
  }

  /**
   * Build a list of all managed columns.
   * @param parent Parent window.
   * @return A list of column controllers.
   */
  public static List<TableColumnController<SameCosmeticsTableRow,?>> initColumns(WindowController parent)
  {
    List<TableColumnController<SameCosmeticsTableRow,?>> columns=new ArrayList<TableColumnController<SameCosmeticsTableRow,?>>();
    columns.addAll(buildGroupColumns());
    columns.addAll(buildItemColumns());
    columns.addAll(buildStoredItemColumns());
    return columns;
  }

  private static List<TableColumnController<SameCosmeticsTableRow,?>> buildItemColumns()
  {
    CellDataProvider<SameCosmeticsTableRow,Item> dataProvider=new CellDataProvider<SameCosmeticsTableRow,Item>()
    {
      @Override
      public Item getData(SameCosmeticsTableRow p)
      {
        return p.getStoredItem().getItem().getItem();
      }
    };

    List<TableColumnController<Item,?>> itemColumns=new ArrayList<TableColumnController<Item,?>>();
    itemColumns.add(ItemsTableBuilder.buildIconColumn());
    itemColumns.add(ItemsTableBuilder.buildNameColumn());
    itemColumns.add(ItemsTableBuilder.buildCategoryColumn());

    List<TableColumnController<SameCosmeticsTableRow,?>> columns=new ArrayList<TableColumnController<SameCosmeticsTableRow,?>>();
    for(TableColumnController<Item,?> itemColumn : itemColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)itemColumn;
      ProxiedTableColumnController<SameCosmeticsTableRow,Item,Object> column=new ProxiedTableColumnController<SameCosmeticsTableRow,Item,Object>(c,dataProvider);
      columns.add(column);
    }
    return columns;
  }

  private static List<TableColumnController<SameCosmeticsTableRow,?>> buildStoredItemColumns()
  {
    CellDataProvider<SameCosmeticsTableRow,StoredItem> dataProvider=new CellDataProvider<SameCosmeticsTableRow,StoredItem>()
    {
      @Override
      public StoredItem getData(SameCosmeticsTableRow p)
      {
        return p.getStoredItem();
      }
    };

    List<TableColumnController<StoredItem,?>> storedItemColumns=new ArrayList<TableColumnController<StoredItem,?>>();
    storedItemColumns.add(StoredItemsTableController.buildOwnerColumn());
    storedItemColumns.add(StoredItemsTableController.buildLocationColumn());

    List<TableColumnController<SameCosmeticsTableRow,?>> columns=new ArrayList<TableColumnController<SameCosmeticsTableRow,?>>();
    for(TableColumnController<StoredItem,?> storedItemColumn : storedItemColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<StoredItem,Object> c=(TableColumnController<StoredItem,Object>)storedItemColumn;
      ProxiedTableColumnController<SameCosmeticsTableRow,StoredItem,Object> column=new ProxiedTableColumnController<SameCosmeticsTableRow,StoredItem,Object>(c,dataProvider);
      columns.add(column);
    }
    return columns;
  }

  private static List<TableColumnController<SameCosmeticsTableRow,?>> buildGroupColumns()
  {
    List<TableColumnController<SameCosmeticsTableRow,?>> columns=new ArrayList<TableColumnController<SameCosmeticsTableRow,?>>();

    CellDataProvider<SameCosmeticsTableRow,CosmeticItemsGroup> dataProvider=new CellDataProvider<SameCosmeticsTableRow,CosmeticItemsGroup>()
    {
      @Override
      public CosmeticItemsGroup getData(SameCosmeticsTableRow p)
      {
        return p.getGroup();
      }
    };

    // Group ID
    {
      CellDataProvider<CosmeticItemsGroup,Integer> cell=new CellDataProvider<CosmeticItemsGroup,Integer>()
      {
        @Override
        public Integer getData(CosmeticItemsGroup item)
        {
          return Integer.valueOf(item.getGroupID());
        }
      };
      DefaultTableColumnController<CosmeticItemsGroup,Integer> column=new DefaultTableColumnController<CosmeticItemsGroup,Integer>(SameCosmeticsColumnIds.GROUP_ID.name(),"Group ID",Integer.class,cell);
      ColumnsUtils.configureIntegerColumn(column);
      column.setSortable(true);
      columns.add(new ProxiedTableColumnController<SameCosmeticsTableRow,CosmeticItemsGroup,Integer>(column,dataProvider));
    }

    // Cosmetic ID
    {
      CellDataProvider<CosmeticItemsGroup,Integer> cell=new CellDataProvider<CosmeticItemsGroup,Integer>()
      {
        @Override
        public Integer getData(CosmeticItemsGroup item)
        {
          return Integer.valueOf(item.getCosmeticID());
        }
      };
      DefaultTableColumnController<CosmeticItemsGroup,Integer> column=new DefaultTableColumnController<CosmeticItemsGroup,Integer>(SameCosmeticsColumnIds.COSMETIC_ID.name(),"Cosmetic ID",Integer.class,cell);
      ColumnsUtils.configureIntegerColumn(column);
      column.setSortable(true);
      columns.add(new ProxiedTableColumnController<SameCosmeticsTableRow,CosmeticItemsGroup,Integer>(column,dataProvider));
    }
    return columns;
  }
}
