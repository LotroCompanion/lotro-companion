package delta.games.lotro.gui.lore.items.legendary2.traceries.table;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.NumericTools;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary2.Tracery;
import delta.games.lotro.lore.items.legendary2.filters.TraceryTierFilter;

/**
 * Controller for a table that shows traceries.
 * @author DAM
 */
public class TraceryTableController
{
  // Data
  private TypedProperties _prefs;
  private List<Tracery> _traceries;
  // GUI
  private JTable _table;
  private GenericTableController<Tracery> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param traceries Traceries to use.
   */
  public TraceryTableController(TypedProperties prefs, Filter<Tracery> filter, List<Tracery> traceries)
  {
    _prefs=prefs;
    _traceries=new ArrayList<Tracery>(traceries);
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<Tracery> buildTable()
  {
    ListDataProvider<Tracery> provider=new ListDataProvider<Tracery>(_traceries);
    GenericTableController<Tracery> table=new GenericTableController<Tracery>(provider);
    List<DefaultTableColumnController<Tracery,?>> columns=buildColumns();
    for(DefaultTableColumnController<Tracery,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<Tracery> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a recipes table.
   * @return A list of columns for a recipes table.
   */
  public static List<DefaultTableColumnController<Tracery,?>> buildColumns()
  {
    List<DefaultTableColumnController<Tracery,?>> ret=new ArrayList<DefaultTableColumnController<Tracery,?>>();
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
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
          Item item=tracery.getItem();
          if (item==null)
          {
            return null;
          }
          String category=item.getSubCategory();
          if (category!=null)
          {
            int index=category.indexOf(TraceryTierFilter.TIER_PATTERN);
            if (index!=-1)
            {
              String tierStr=category.substring(index+TraceryTierFilter.TIER_PATTERN.length());
              return NumericTools.parseInteger(tierStr);
            }
          }
          return null;
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
    return ret;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=null;
    if (_prefs!=null)
    {
      columnIds=_prefs.getStringList(ItemChooser.COLUMNS_PROPERTY);
    }
    if (columnIds==null)
    {
      columnIds=new ArrayList<String>();
      if (UiConfiguration.showTechnicalColumns())
      {
        columnIds.add(TraceryColumnIds.ID.name());
      }
      columnIds.add(TraceryColumnIds.NAME.name());
      columnIds.add(TraceryColumnIds.TIER.name());
      columnIds.add(TraceryColumnIds.MIN_ITEM_LEVEL.name());
      columnIds.add(TraceryColumnIds.MAX_ITEM_LEVEL.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<Tracery> getTableController()
  {
    return _tableController;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of recipes.
   * @return A number of recipes.
   */
  public int getNbItems()
  {
    return _traceries.size();
  }

  /**
   * Get the number of filtered items in the managed table.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_tableController.getNbFilteredItems();
    return ret;
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    if (_table==null)
    {
      _table=_tableController.getTable();
    }
    return _table;
  }

  /**
   * Add an action listener.
   * @param al Action listener to add.
   */
  public void addActionListener(ActionListener al)
  {
    _tableController.addActionListener(al);
  }

  /**
   * Remove an action listener.
   * @param al Action listener to remove.
   */
  public void removeActionListener(ActionListener al)
  {
    _tableController.removeActionListener(al);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnIds=_tableController.getColumnsManager().getSelectedColumnsIds();
      _prefs.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
      _prefs=null;
    }
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _traceries=null;
  }
}
