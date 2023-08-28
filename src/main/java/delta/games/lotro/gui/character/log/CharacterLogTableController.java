package delta.games.lotro.gui.character.log;

import java.util.Date;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;

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
      @Override
      public CharacterLogItem getAt(int index)
      {
        return _log.getLogItem(index);
      }

      @Override
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
        @Override
        public Date getData(CharacterLogItem item)
        {
          long timestamp=item.getDate();
          return new Date(timestamp);
        }
      };
      DefaultTableColumnController<CharacterLogItem,Date> dateColumn=new DefaultTableColumnController<CharacterLogItem,Date>("Date",Date.class,dateCell); // I18n
      StatColumnsUtils.configureDateColumn(dateColumn);
      table.addColumnController(dateColumn);
    }
    // Type column
    {
      CellDataProvider<CharacterLogItem,LogItemType> typeCell=new CellDataProvider<CharacterLogItem,LogItemType>()
      {
        @Override
        public LogItemType getData(CharacterLogItem item)
        {
          return item.getLogItemType();
        }
      };
      DefaultTableColumnController<CharacterLogItem,LogItemType> typeColumn=new DefaultTableColumnController<CharacterLogItem,LogItemType>("Type",LogItemType.class,typeCell); // I18n
      typeColumn.setWidthSpecs(100,100,50);
      typeColumn.setCellRenderer(new LogItemTypeRenderer());
      typeColumn.setUseToString(Boolean.TRUE);
      table.addColumnController(typeColumn);
    }
    // Label column
    {
      CellDataProvider<CharacterLogItem,String> labelCell=new CellDataProvider<CharacterLogItem,String>()
      {
        @Override
        public String getData(CharacterLogItem item)
        {
          return item.getLabel();
        }
      };
      DefaultTableColumnController<CharacterLogItem,String> labelColumn=new DefaultTableColumnController<CharacterLogItem,String>("Label",String.class,labelCell); // I18n
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

    /**
     * Constructor.
     */
    public LogItemTypeRenderer()
    {
      _labels=new HashMap<LogItemType,String>();
      _labels.put(LogItemType.DEED,"Deed"); // I18n
      _labels.put(LogItemType.LEVELUP,"Level up"); // I18n
      _labels.put(LogItemType.PROFESSION,"Profession"); // I18n
      _labels.put(LogItemType.PVMP,"Player vs Monster"); // I18n
      _labels.put(LogItemType.QUEST,"Quest"); // I18n
      _labels.put(LogItemType.VOCATION,"Vocation"); // I18n
      _labels.put(LogItemType.UNKNOWN,"???"); // I18n
    }

    @Override
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
