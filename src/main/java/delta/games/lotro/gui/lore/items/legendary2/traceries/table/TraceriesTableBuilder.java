package delta.games.lotro.gui.lore.items.legendary2.traceries.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.lore.items.ItemColumnIds;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.chooser.ItemsTableBuilder;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary2.Tracery;

/**
 * Builder for traceries tables.
 * @author DAM
 */
public class TraceriesTableBuilder
{
  /**
   * Build a traceries table controller using the given traceries.
   * @param traceries Traceries to use.
   * @return a traceries table controller.
   */
  public static GenericTableController<Tracery> buildTable(List<Tracery> traceries)
  {
    ListDataProvider<Tracery> provider=new ListDataProvider<Tracery>(traceries);
    GenericTableController<Tracery> table=new GenericTableController<Tracery>(provider);
    List<TableColumnController<Tracery,?>> columns=buildColumns();
    for(TableColumnController<Tracery,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<Tracery> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getDefaultColumnIds();
    columnsManager.setColumns(columnsIds);
    // Adjust table row height for icons (32 pixels)
    JTable swingTable=table.getTable();
    swingTable.setRowHeight(32);
    return table;
  }

  /**
   * Build the columns for a traceries table.
   * @return A list of columns for a traceries table.
   */
  public static List<TableColumnController<Tracery,?>> buildColumns()
  {
    List<TableColumnController<Tracery,?>> ret=new ArrayList<TableColumnController<Tracery,?>>();
    // Identifier column
    //if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<Tracery,Integer> idCell=new CellDataProvider<Tracery,Integer>()
      {
        @Override
        public Integer getData(Tracery tracery)
        {
          return Integer.valueOf(tracery.getIdentifier());
        }
      };
      DefaultTableColumnController<Tracery,Integer> idColumn=new DefaultTableColumnController<Tracery,Integer>(TraceryColumnIds.ID.name(),"ID",Integer.class,idCell);
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Name column
    {
      CellDataProvider<Tracery,String> nameCell=new CellDataProvider<Tracery,String>()
      {
        @Override
        public String getData(Tracery tracery)
        {
          return tracery.getName();
        }
      };
      DefaultTableColumnController<Tracery,String> nameColumn=new DefaultTableColumnController<Tracery,String>(TraceryColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,-1,200);
      ret.add(nameColumn);
    }
    // Tier column
    {
      CellDataProvider<Tracery,Integer> tierCell=new CellDataProvider<Tracery,Integer>()
      {
        @Override
        public Integer getData(Tracery tracery)
        {
          return tracery.getTier();
        }
      };
      DefaultTableColumnController<Tracery,Integer> tierColumn=new DefaultTableColumnController<Tracery,Integer>(TraceryColumnIds.TIER.name(),"Tier",Integer.class,tierCell);
      tierColumn.setWidthSpecs(60,60,60);
      ret.add(tierColumn);
    }
    // Min item level column
    {
      CellDataProvider<Tracery,Integer> minItemLevelCell=new CellDataProvider<Tracery,Integer>()
      {
        @Override
        public Integer getData(Tracery tracery)
        {
          return Integer.valueOf(tracery.getMinItemLevel());
        }
      };
      DefaultTableColumnController<Tracery,Integer> minItemLevelColumn=new DefaultTableColumnController<Tracery,Integer>(TraceryColumnIds.MIN_ITEM_LEVEL.name(),"Min Item Level",Integer.class,minItemLevelCell);
      minItemLevelColumn.setWidthSpecs(60,60,60);
      ret.add(minItemLevelColumn);
    }
    // Max item level column
    {
      CellDataProvider<Tracery,Integer> maxItemLevelCell=new CellDataProvider<Tracery,Integer>()
      {
        @Override
        public Integer getData(Tracery tracery)
        {
          return Integer.valueOf(tracery.getMaxItemLevel());
        }
      };
      DefaultTableColumnController<Tracery,Integer> maxItemLevelColumn=new DefaultTableColumnController<Tracery,Integer>(TraceryColumnIds.MAX_ITEM_LEVEL.name(),"Max Item Level",Integer.class,maxItemLevelCell);
      maxItemLevelColumn.setWidthSpecs(60,60,60);
      ret.add(maxItemLevelColumn);
    }
    // Stats column
    {
      CellDataProvider<Tracery,String> statsCell=new CellDataProvider<Tracery,String>()
      {
        @Override
        public String getData(Tracery tracery)
        {
          return tracery.getItem().getStats().toString();
        }
      };
      DefaultTableColumnController<Tracery,String> statsColumn=new DefaultTableColumnController<Tracery,String>(TraceryColumnIds.STATS.name(),"Stats",String.class,statsCell);
      statsColumn.setWidthSpecs(100,-1,100);
      ret.add(statsColumn);
    }
    // Item columns
    CellDataProvider<Tracery,Item> provider=new CellDataProvider<Tracery,Item>()
    {
      @Override
      public Item getData(Tracery tracery)
      {
        return tracery.getItem();
      }
    };
    for(TableColumnController<Item,?> itemColumn : buildItemColumns())
    {
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)itemColumn;
      ProxiedTableColumnController<Tracery,Item,Object> column=new ProxiedTableColumnController<Tracery,Item,Object>(c,provider);
      ret.add(column);
    }
    return ret;
  }

  private static List<TableColumnController<Item,?>> buildItemColumns()
  {
    List<TableColumnController<Item,?>> ret=new ArrayList<TableColumnController<Item,?>>();
    ret.add(ItemsTableBuilder.buildIconColumn());
    ret.add(ItemsTableBuilder.buildMinLevelColumn());
    ret.add(ItemsTableBuilder.buildMaxLevelColumn());
    ret.add(ItemsTableBuilder.buildQualityColumn());
    return ret;
  }

  private static List<String> getDefaultColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    if (UiConfiguration.showTechnicalColumns())
    {
      columnIds.add(TraceryColumnIds.ID.name());
    }
    columnIds.add(ItemColumnIds.ICON.name());
    columnIds.add(TraceryColumnIds.NAME.name());
    columnIds.add(TraceryColumnIds.STATS.name());
    columnIds.add(TraceryColumnIds.TIER.name());
    columnIds.add(ItemColumnIds.QUALITY.name());
    columnIds.add(ItemColumnIds.REQUIRED_LEVEL.name());
    columnIds.add(ItemColumnIds.REQUIRED_MAX_LEVEL.name());
    columnIds.add(TraceryColumnIds.MIN_ITEM_LEVEL.name());
    columnIds.add(TraceryColumnIds.MAX_ITEM_LEVEL.name());
    return columnIds;
  }

  /**
   * Add a details column on the given table.
   * @param parent Parent window.
   * @param table Table to use.
   * @return A column controller.
   */
  public static DefaultTableColumnController<Tracery,String> addDetailsColumn(final WindowController parent, GenericTableController<Tracery> table)
  {
    DefaultTableColumnController<Tracery,String> column=table.buildButtonColumn(ItemColumnIds.DETAILS.name(),"Details...",90);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Tracery source=(Tracery)e.getSource();
        ItemUiTools.showItemForm(parent,source.getItem());
      }
    };
    column.setActionListener(al);
    table.addColumnController(column);
    table.updateColumns();
    return column;
  }
}
