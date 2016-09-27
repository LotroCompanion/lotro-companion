package delta.games.lotro.utils.gui.tables;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.utils.Formats;

/**
 * Generic controller for a table.
 * @param <POJO> Type of tabled data.
 * @author DAM
 */
public class GenericTableController<POJO>
{
  /**
   * Double-click action command.
   */
  public static final String DOUBLE_CLICK="double click";
  // Data
  private DataProvider<POJO> _dataProvider;
  // Columns
  private List<TableColumnController<POJO,?>> _columns;
  // GUI
  private JTable _table;
  private GenericTableModel<POJO> _model;
  private TableRowSorter<GenericTableModel<POJO>> _sorter;
  // Control
  private List<ActionListener> _actionListeners;

  /**
   * Constructor.
   * @param dataProvider Data provider.
   */
  public GenericTableController(DataProvider<POJO> dataProvider)
  {
    _dataProvider=dataProvider;
    _columns=new ArrayList<TableColumnController<POJO,?>>();
    _actionListeners=new ArrayList<ActionListener>();
  }

  /**
   * Refresh table.
   */
  public void refresh()
  {
    if (_table!=null)
    {
      _model.fireTableDataChanged();
    }
  }

  /**
   * Refresh an item in this table.
   * @param dataItem Data item to refresh.
   */
  public void refresh(POJO dataItem)
  {
    if (_table!=null)
    {
      int row=getRowForItem(dataItem);
      if (row!=-1)
      {
        _model.fireTableRowsUpdated(row,row);
      }
    }
  }

  /**
   * Refresh an item in this table.
   * @param row Row to refresh.
   */
  public void refreshRow(int row)
  {
    if (_table!=null)
    {
      if (row!=-1)
      {
        _model.fireTableRowsUpdated(row,row);
      }
    }
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

  private JTable build()
  {
    _model=new GenericTableModel<POJO>(this,_dataProvider);
    final JTable table=GuiFactory.buildTable();
    table.setModel(_model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _sorter=new TableRowSorter<GenericTableModel<POJO>>(_model);
    /*
    {
      @Override
      protected boolean useToString(int column)
      {
        TableColumnController columnController=_columns.get(column);
        Class<?> dataType=columnController.getDataType();
        if (Number.class.isAssignableFrom(dataType))
        {
          return false;
        }
        if (column==3) return false; // Level
        return super.useToString(column);
      }
    };
    */
    _sorter.setSortsOnUpdates(true);
    int i=0;
    for(TableColumnController<POJO,?> controller : _columns)
    {
      TableColumn column=table.getColumnModel().getColumn(i);
      int preferredWidth=controller.getPreferredWidth();
      if (preferredWidth>=0)
      {
        column.setPreferredWidth(preferredWidth);
      }
      int minWidth=controller.getMinWidth();
      if (minWidth>=0)
      {
        column.setMinWidth(minWidth);
      }
      int maxWidth=controller.getMaxWidth();
      if (maxWidth>=0)
      {
        column.setMaxWidth(maxWidth);
      }
      column.setResizable(true);
      Class<?> dataType=controller.getDataType();
      if (Date.class.isAssignableFrom(dataType))
      {
        column.setCellRenderer(new DateRenderer());
      }
      _sorter.setSortable(i,true);
      i++;
    }
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    _sorter.setMaxSortKeys(3);
    table.setRowSorter(_sorter);

    MouseAdapter doucleClickAdapter=new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount() == 2)
        {
          Point p = e.getPoint();
          int row = table.convertRowIndexToModel(table.rowAtPoint(p));
          int column = table.convertColumnIndexToModel(table.columnAtPoint(p));
          if (row >= 0 && column >= 0)
          {
            invokeDoubleClickAction(row);
          }
        }
      }
    };
    table.addMouseListener(doucleClickAdapter);
    return table;
  }

  private void invokeDoubleClickAction(int row)
  {
    POJO dataItem=_dataProvider.getAt(row);
    ActionEvent e=new ActionEvent(dataItem,0,DOUBLE_CLICK);
    ActionListener[] als=_actionListeners.toArray(new ActionListener[_actionListeners.size()]);
    for(ActionListener al : als)
    {
      al.actionPerformed(e);
    }
  }

  /**
   * Add a column controller.
   * @param controller Controller to add.
   */
  public void addColumnController(TableColumnController<POJO,?> controller)
  {
    _columns.add(controller);
  }

  /**
   * Get a column controller.
   * @param index Index of the targeted column, starting at 0.
   * @return A column controller.
   */
  public TableColumnController<POJO,?> getColumnController(int index)
  {
    return _columns.get(index);
  }

  /**
   * Get the number of columns.
   * @return the number of columns.
   */
  public int getColumnCount()
  {
    return _columns.size();
  }

  /**
   * Add an action listener.
   * @param al Action listener to add.
   */
  public void addActionListener(ActionListener al)
  {
    _actionListeners.add(al);
  }

  /**
   * Remove an action listener.
   * @param al Action listener to remove.
   */
  public void removeActionListener(ActionListener al)
  {
    _actionListeners.remove(al);
  }

  /**
   * Get the row for the given item.
   * @param item Item to search.
   * @return A row index or <code>-1</code> if not found.
   */
  public int getRowForItem(POJO item)
  {
    int ret=-1;
    int nb=_dataProvider.getCount();
    for(int i=0;i<nb;i++)
    {
      if (_dataProvider.getAt(i)==item)
      {
        ret=i;
        break;
      }
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_table!=null)
    {
      _table=null;
    }
    _model=null;
    _sorter=null;
    // Data
    _dataProvider=null;
  }

  static class DateRenderer extends DefaultTableCellRenderer
  {
    private SimpleDateFormat _formatter;  
    public DateRenderer()
    {
      _formatter=new SimpleDateFormat(Formats.DATE_TIME_PATTERN);
    }

    @Override
    public void setValue(Object value)
    {
        setText((value == null) ? "" : _formatter.format(value));  
    }
  }
}
