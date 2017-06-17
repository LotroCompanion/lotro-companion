package delta.games.lotro.utils.gui.tables;

import javax.swing.table.AbstractTableModel;

/**
 * A table model that works with the generic table model.
 * @param <POJO> Type of managed data.
 * @author DAM
 */
public class GenericTableModel<POJO> extends AbstractTableModel
{
  private GenericTableController<POJO> _table;
  private DataProvider<POJO> _provider;

  /**
   * Constructor.
   * @param table Associated table.
   * @param provider Data provider.
   */
  public GenericTableModel(GenericTableController<POJO> table, DataProvider<POJO> provider)
  {
    _table=table;
    _provider=provider;
  }

  /**
   * Get the name of the targeted column.
   * @param columnIndex Index of the targeted column, starting at 0.
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName(int columnIndex)
  {
    TableColumnController<POJO,?> controller=_table.getColumnController(columnIndex);
    String header=controller.getHeader();
    return header;
  }

  /**
   * Get the number of columns.
   * @see javax.swing.table.AbstractTableModel#getColumnCount()
   */
  public int getColumnCount()
  {
    return _table.getColumnCount();
  }

  /**
   * Get the number of rows.
   * @see javax.swing.table.TableModel#getRowCount()
   */
  public int getRowCount()
  {
    return (_provider!=null)?_provider.getCount():0;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex)
  {
    TableColumnController<POJO,?> controller=_table.getColumnController(columnIndex);
    Class<?> dataType=controller.getDataType();
    return dataType;
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
    POJO dataItem=_provider.getAt(rowIndex);
    if (dataItem!=null)
    {
      TableColumnController<POJO,?> controller=_table.getColumnController(columnIndex);
      CellDataProvider<POJO,?> provider=controller.getValueProvider();
      ret=provider.getData(dataItem);
    }
    return ret;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    TableColumnController<POJO,?> controller=_table.getColumnController(columnIndex);
    return controller.isEditable();
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
    POJO dataItem=_provider.getAt(rowIndex);
    if (dataItem!=null)
    {
      TableColumnController<POJO,?> controller=_table.getColumnController(columnIndex);
      CellDataUpdater<POJO> updater=controller.getValueUpdater();
      updater.setData(dataItem,aValue);
    }
  }
}
