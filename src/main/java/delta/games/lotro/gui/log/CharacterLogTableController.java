package delta.games.lotro.gui.log;

import java.util.Date;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.utils.Formats;

/**
 * Controller for a table that shows a character log.
 * @author DAM
 */
public class CharacterLogTableController
{
  // Data
  private CharacterLog _log;
  // GUI
  private GenericTableController<CharacterLogItem> _tableController;

  /**
   * Constructor.
   * @param log Character log.
   * @param filter Log filter.
   */
  public CharacterLogTableController(CharacterLog log, Filter<CharacterLogItem> filter)
  {
    _log=log;
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<CharacterLogItem> buildTable()
  {
    DataProvider<CharacterLogItem> provider=new DataProvider<CharacterLogItem>() {
      public CharacterLogItem getAt(int index)
      {
        return _log.getLogItem(index);
      }

      public int getCount()
      {
        return _log.getNbItems();
      }
    };
    GenericTableController<CharacterLogItem> table=new GenericTableController<CharacterLogItem>(provider);

    // Date column
    {
      CellDataProvider<CharacterLogItem,Date> dateCell=new CellDataProvider<CharacterLogItem,Date>()
      {
        public Date getData(CharacterLogItem item)
        {
          long timestamp=item.getDate();
          return new Date(timestamp);
        }
      };
      TableColumnController<CharacterLogItem,Date> dateColumn=new TableColumnController<CharacterLogItem,Date>("Date",Date.class,dateCell);
      dateColumn.setWidthSpecs(100,100,50);
      dateColumn.setCellRenderer(new DateRenderer(Formats.DATE_PATTERN));
      table.addColumnController(dateColumn);
    }
    // Type column
    {
      CellDataProvider<CharacterLogItem,LogItemType> typeCell=new CellDataProvider<CharacterLogItem,LogItemType>()
      {
        public LogItemType getData(CharacterLogItem item)
        {
          return item.getLogItemType();
        }
      };
      TableColumnController<CharacterLogItem,LogItemType> typeColumn=new TableColumnController<CharacterLogItem,LogItemType>("Type",LogItemType.class,typeCell);
      typeColumn.setWidthSpecs(100,100,50);
      typeColumn.setCellRenderer(new LogItemTypeRenderer());
      typeColumn.setUseToString(Boolean.TRUE);
      table.addColumnController(typeColumn);
    }
    // Label column
    {
      CellDataProvider<CharacterLogItem,String> labelCell=new CellDataProvider<CharacterLogItem,String>()
      {
        public String getData(CharacterLogItem item)
        {
          return item.getLabel();
        }
      };
      TableColumnController<CharacterLogItem,String> labelColumn=new TableColumnController<CharacterLogItem,String>("Label",String.class,labelCell);
      labelColumn.setWidthSpecs(150,-1,150);
      table.addColumnController(labelColumn);
    }
    return table;
  }

  private void configureTable()
  {
    JTable table=getTable();
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    return _tableController.getTable();
  }

  static class LogItemTypeRenderer extends DefaultTableCellRenderer
  {
    private HashMap<LogItemType,String> _labels;

    public LogItemTypeRenderer()
    {
      _labels=new HashMap<LogItemType,String>();
      _labels.put(LogItemType.DEED,"Deed");
      _labels.put(LogItemType.LEVELUP,"Level up");
      _labels.put(LogItemType.PROFESSION,"Profession");
      _labels.put(LogItemType.PVMP,"Player vs Monster");
      _labels.put(LogItemType.QUEST,"Quest");
      _labels.put(LogItemType.VOCATION,"Vocation");
      _labels.put(LogItemType.UNKNOWN,"???");
    }

    public void setValue(Object value)
    {
      setText((value == null) ? "" : _labels.get(value));
    }
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of items in the managed log.
   * @return A number of items.
   */
  public int getNbItems()
  {
    return (_log!=null)?_log.getNbItems():0;
  }

  /**
   * Get the number of filtered items in the managed log.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_tableController.getNbFilteredItems();
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _log=null;
  }
}
