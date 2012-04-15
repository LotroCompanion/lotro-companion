package delta.games.lotro.gui.log;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.character.log.CharacterLogItemsFilter;

/**
 * Controller for a table that shows a character log.
 * @author DAM
 */
public class CharacterLogTableController
{
  // Data
  private CharacterLog _log;
  private CharacterLogItemsFilter _filter;
  // GUI
  private JTable _table;
  private CharacterLogTableModel _model;
  private TableRowSorter<CharacterLogTableModel> _sorter;
  private RowFilter<CharacterLogTableModel,Integer> _guiFilter;

  private static final String[] COLUMN_NAMES=
  {
    "Date",
    "Type",
    "Label"
  };

  /**
   * Constructor.
   * @param log Character log.
   * @param filter Log filter.
   */
  public CharacterLogTableController(CharacterLog log, CharacterLogItemsFilter filter)
  {
    _log=log;
    _filter=filter;
  }

  /**
   * Set a new character log.
   * @param log Character log to set.
   */
  public void setLog(CharacterLog log)
  {
    _log=log;
    _model.fireTableDataChanged();
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    if (_table==null)
    {
      _table=build();
    }
    return _table;
  }

  static class DateRenderer extends DefaultTableCellRenderer
  {
    private SimpleDateFormat _formatter;  
    public DateRenderer()
    {
      _formatter=new SimpleDateFormat("dd/MM/yyyy");
    }  
   
    public void setValue(Object value)
    {  
        setText((value == null) ? "" : _formatter.format(value));  
    }  
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

  private JTable build()
  {
    _model=new CharacterLogTableModel();
    JTable table=new JTable(_model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    _sorter=new TableRowSorter<CharacterLogTableModel>(_model) {

      @Override
      protected boolean useToString(int column)
      {
        if (column==0) return false;
        if (column==1) return true;
        return super.useToString(column);
      }
      
    };
    TableColumn dateColumn=table.getColumnModel().getColumn(0);
    dateColumn.setCellRenderer(new DateRenderer());
    dateColumn.setPreferredWidth(50);
    dateColumn.setMinWidth(100);
    dateColumn.setMaxWidth(100);
    TableColumn typeColumn=table.getColumnModel().getColumn(1);
    typeColumn.setCellRenderer(new LogItemTypeRenderer());
    typeColumn.setPreferredWidth(50);
    typeColumn.setMinWidth(100);
    typeColumn.setMaxWidth(100);
    TableColumn labelColumn=table.getColumnModel().getColumn(2);
    labelColumn.setPreferredWidth(150);
    
    _guiFilter=new RowFilter<CharacterLogTableModel,Integer>()
    {
      @Override
      public boolean include(RowFilter.Entry<? extends CharacterLogTableModel,? extends Integer> entry)
      {
        Integer id=entry.getIdentifier();
        CharacterLogItem item=_log.getLogItem(id.intValue());
        boolean ret=_filter.filterItem(item);
        return ret;
      }
    };
    _sorter.setRowFilter(_guiFilter);
    _sorter.setSortable(0,true);
    _sorter.setSortable(1,true);
    _sorter.setSortable(2,true);
    _sorter.setMaxSortKeys(3);
    table.setRowSorter(_sorter);
    return table;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _sorter.setRowFilter(_guiFilter);
  }

  private static Class<?>[] TYPES=new Class [] { Long.class, LogItemType.class, String.class};  
  
  private class CharacterLogTableModel extends AbstractTableModel
  {
    /**
     * Constructor.
     */
    public CharacterLogTableModel()
    {
    }

    @Override  
    public Class<?> getColumnClass(int columnIndex) {  
        return TYPES[columnIndex];  
    }  

    /**
     * Get the name of the targeted column.
     * @param column Index of the targeted column, starting at 0.
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column)
    {
      return COLUMN_NAMES[column];
    }

    /**
     * Get the number of columns.
     * @see javax.swing.table.AbstractTableModel#getColumnCount()
     */
    public int getColumnCount()
    {
      return COLUMN_NAMES.length;
    }

    /**
     * Get the number of rows.
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
      return _log.getNbItems();
    }

    /**
     * Get the value of a cell.
     * @param rowIndex Index of targeted row.
     * @param columnIndex Index of targeted column.
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
      Object ret=null;
      CharacterLogItem item=_log.getLogItem(rowIndex);
      if (item!=null)
      {
        if (columnIndex==0)
        {
          ret=Long.valueOf(item.getDate());
        }
        else if (columnIndex==1)
        {
          ret=item.getLogItemType();
        }
        else if (columnIndex==2)
        {
          ret=item.getLabel();
        }
      }
      return ret;
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_table!=null)
    {
      _table.setModel(null);
      _table=null;
    }
    _model=null;
    _sorter=null;
    _guiFilter=null;
    // Data
    _log=null;
    _filter=null;
  }
}
