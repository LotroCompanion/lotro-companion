package delta.games.lotro.gui.lore.hobbies.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.chooser.ItemsTableBuilder;
import delta.games.lotro.lore.hobbies.rewards.HobbyDropTableEntry;
import delta.games.lotro.lore.items.Item;

/**
 * Builder for a table that shows hobby drop entries.
 * @author DAM
 */
public class HobbyDropTableBuilder
{
  /**
   * Build a table to show a hobby drop table.
   * @param items Items to show.
   * @return A new table controller.
   */
  public static GenericTableController<HobbyDropTableEntry> buildTable(List<HobbyDropTableEntry> items)
  {
    DataProvider<HobbyDropTableEntry> provider=new ListDataProvider<HobbyDropTableEntry>(items);
    GenericTableController<HobbyDropTableEntry> table=new GenericTableController<HobbyDropTableEntry>(provider);

    List<TableColumnController<HobbyDropTableEntry,?>> columns=new ArrayList<TableColumnController<HobbyDropTableEntry,?>>();
    // Item columns
    {
      List<DefaultTableColumnController<Item,?>> itemColumns=buildItemColumns();
      CellDataProvider<HobbyDropTableEntry,Item> dataProvider=new CellDataProvider<HobbyDropTableEntry,Item>()
      {
        @Override
        public Item getData(HobbyDropTableEntry entry)
        {
          return entry.getItem();
        }
      };
      for(DefaultTableColumnController<Item,?> itemColumn : itemColumns)
      {
        @SuppressWarnings("unchecked")
        TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)itemColumn;
        TableColumnController<HobbyDropTableEntry,Object> proxiedColumn=new ProxiedTableColumnController<HobbyDropTableEntry,Item,Object>(c,dataProvider);
        columns.add(proxiedColumn);
      }
    }
    // Drop column
    columns.add(buildDropPercentageColumn());

    TableColumnsManager<HobbyDropTableEntry> columnsManager=table.getColumnsManager();
    for(TableColumnController<HobbyDropTableEntry,?> column : columns)
    {
      columnsManager.addColumnController(column,true);
    }
    // Adjust table row height for icons (32 pixels)
    JTable swingTable=table.getTable();
    swingTable.setRowHeight(32);
    return table;
  }

  /**
   * Add a details column on the given table.
   * @param parent Parent window.
   * @param table Table to use.
   * @return A column controller.
   */
  public static DefaultTableColumnController<HobbyDropTableEntry,String> addDetailsColumn(final WindowController parent, GenericTableController<HobbyDropTableEntry> table)
  {
    DefaultTableColumnController<HobbyDropTableEntry,String> column=table.buildButtonColumn(HobbyDropTableColumnIds.DETAILS.name(),"Details...",90);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        HobbyDropTableEntry source=(HobbyDropTableEntry)e.getSource();
        Item item=source.getItem();
        ItemUiTools.showItemForm(parent,item);
      }
    };
    column.setActionListener(al);
    table.addColumnController(column);
    table.updateColumns();
    return column;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  private static List<DefaultTableColumnController<Item,?>> buildItemColumns()
  {
    List<DefaultTableColumnController<Item,?>> columns=new ArrayList<DefaultTableColumnController<Item,?>>();
    // Icon column
    columns.add(ItemsTableBuilder.buildIconColumn());
    // ID column
    columns.add(ItemsTableBuilder.buildIdColumn());
    // Name column
    columns.add(ItemsTableBuilder.buildNameColumn());
    return columns;
  }

  /**
   * Build a column for the drop percentage of a hobby rewards item.
   * @return a column.
   */
  private static DefaultTableColumnController<HobbyDropTableEntry,Float> buildDropPercentageColumn()
  {
    CellDataProvider<HobbyDropTableEntry,Float> dropCell=new CellDataProvider<HobbyDropTableEntry,Float>()
    {
      @Override
      public Float getData(HobbyDropTableEntry entry)
      {
        return Float.valueOf(entry.getDropPercentage());
      }
    };
    DefaultTableColumnController<HobbyDropTableEntry,Float> dropColumn=new DefaultTableColumnController<HobbyDropTableEntry,Float>(HobbyDropTableColumnIds.DROP_PERCENTAGE.name(),"Drop %",Float.class,dropCell); // I18n
    dropColumn.setCellRenderer(new PercentageRenderer(2));
    dropColumn.setWidthSpecs(55,55,50);
    return dropColumn;
  }
}
