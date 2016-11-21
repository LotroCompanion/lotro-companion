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
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import delta.common.utils.collections.filters.Filter;
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
  private Filter<POJO> _filter;
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
    _filter=null;
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
    _sorter=new TableRowSorter<GenericTableModel<POJO>>(_model)
    {
      @Override
      protected boolean useToString(int column)
      {
        TableColumnController<POJO,?> columnController=_columns.get(column);
        Boolean useToString=columnController.isUseToString();
        if (useToString!=null)
        {
          return useToString.booleanValue();
        }
        return super.useToString(column);
      }
    };
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
        column.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      }
      TableCellRenderer renderer=controller.getCellRenderer();
      if (renderer!=null)
      {
        column.setCellRenderer(renderer);
      }
      boolean sortable=controller.isSortable();
      _sorter.setSortable(i,sortable);
      i++;
    }
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    _sorter.setMaxSortKeys(3);
    table.setRowSorter(_sorter);

    // Filter
    RowFilter<GenericTableModel<POJO>,Integer> guiFilter=new RowFilter<GenericTableModel<POJO>,Integer>()
    {
      @Override
      public boolean include(RowFilter.Entry<? extends GenericTableModel<POJO>,? extends Integer> entry)
      {
        boolean ret=true;
        if (_filter!=null)
        {
          Integer id=entry.getIdentifier();
          POJO item=_dataProvider.getAt(id.intValue());
          ret=_filter.accept(item);
        }
        return ret;
      }
    };
    _sorter.setRowFilter(guiFilter);

    // Double click management
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
   * Set data filter.
   * @param filter Filter to set.
   */
  public void setFilter(Filter<POJO> filter)
  {
    _filter=filter;
  }

  /**
   * To be called when the filter was updated and the GUI needs a refresh.
   */
  public void filterUpdated()
  {
    _sorter.setRowFilter(_sorter.getRowFilter());
  }

  /**
   * Get the number of filtered items in the managed log.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_table.getRowSorter().getViewRowCount();
    return ret;
  }

  /**
   * Get the currently selected item.
   * @return An item or <code>null</code> if not found.
   */
  public POJO getSelectedItem()
  {
    POJO ret=null;
    int selectedItemIndex=_table.getSelectedRow();
    if (selectedItemIndex!=-1)
    {
      int modelIndex=_table.convertRowIndexToModel(selectedItemIndex);
      ret=_dataProvider.getAt(modelIndex);
    }
    return ret;
  }

  /**
   * Select an item.
   * @param item Item to select.
   */
  public void selectItem(POJO item)
  {
    int index=getItemIndex(item);
    ListSelectionModel selectionModel=_table.getSelectionModel();
    if (index!=-1)
    {
      selectionModel.setSelectionInterval(index,index);
    }
    else
    {
      selectionModel.clearSelection();
    }
  }

  private int getItemIndex(POJO item)
  {
    int index=-1;
    if (item!=null)
    {
      int nb=_dataProvider.getCount();
      for(int i=0;i<nb;i++)
      {
        POJO currentItem=_dataProvider.getAt(i);
        if (currentItem==item)
        {
          index=i;
          break;
        }
      }
    }
    return index;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_table!=null)
    {
      _table.setModel(new DefaultTableModel());
      _table=null;
    }
    _model=null;
    _sorter=null;
    // Data
    _dataProvider=null;
    _filter=null;
  }

  /**
   * Date renderer.
   * @author DAM
   */
  public static class DateRenderer extends DefaultTableCellRenderer
  {
    private SimpleDateFormat _formatter;
    /**
     * Constructor.
     * @param format Date format.
     */
    public DateRenderer(String format)
    {
      _formatter=new SimpleDateFormat(format);
    }

    @Override
    public void setValue(Object value)
    {
      setText((value == null) ? "" : _formatter.format(value));
    }
  }
}
