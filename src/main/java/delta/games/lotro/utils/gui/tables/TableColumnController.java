package delta.games.lotro.utils.gui.tables;

import javax.swing.table.TableCellRenderer;

/**
 * Controller for a column of a generic table.
 * @param <POJO> Type of data items.
 * @param <VALUE> Value type of the managed column.
 * @author DAM
 */
public class TableColumnController<POJO,VALUE>
{
  private int _minWidth;
  private int _maxWidth;
  private int _preferredWidth;
  private Class<VALUE> _dataType;
  private String _header;
  private boolean _sortable;
  private CellDataProvider<POJO,VALUE> _valueProvider;
  private TableCellRenderer _renderer;
  private Boolean _useToString;

  /**
   * Constructor.
   * @param header Header label.
   * @param dataType Type of data in the column.
   * @param valueProvider Provider for cell values.
   */
  public TableColumnController(String header, Class<VALUE> dataType, CellDataProvider<POJO,VALUE> valueProvider)
  {
    _header=header;
    _dataType=dataType;
    _sortable=true;
    _valueProvider=valueProvider;
    _renderer=null;
    _useToString=null;
  }

  /**
   * Get the minimum width for this column.
   * @return a value in pixels.
   */
  public int getMinWidth()
  {
    return _minWidth;
  }

  /**
   * Set the minimum width for this column.
   * @param minWidth a width in pixels (-1 for no minimum).
   */
  public void setMinWidth(int minWidth)
  {
    _minWidth=minWidth;
  }

  /**
   * Get the maximum width for this column.
   * @return a value in pixels (-1 means no limit).
   */
  public int getMaxWidth()
  {
    return _maxWidth;
  }

  /**
   * Set the maximum width for this column.
   * @param maxWidth a width in pixels (-1 for no limit).
   */
  public void setMaxWidth(int maxWidth)
  {
    _maxWidth=maxWidth;
  }

  /**
   * Get the preferred width for this column.
   * @return a value in pixels.
   */
  public int getPreferredWidth()
  {
    return _preferredWidth;
  }

  /**
   * Set the preferred width for this column.
   * @param preferredWidth a width in pixels.
   */
  public void setPreferredWidth(int preferredWidth)
  {
    _preferredWidth=preferredWidth;
  }

  /**
   * Set width specifications.
   * @param minWidth Minimum width in pixels.
   * @param maxWidth Maximum width in pixels.
   * @param preferredWidth Preferred width in pixels.
   */
  public void setWidthSpecs(int minWidth, int maxWidth, int preferredWidth)
  {
    _minWidth=minWidth;
    _maxWidth=maxWidth;
    _preferredWidth=preferredWidth;
  }

  /**
   * Indicates if this column is sortable or not.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isSortable()
  {
    return _sortable;
  }

  /**
   * Set the sortable flag.
   * @param sortable Flag value to set.
   */
  public void setSortable(boolean sortable)
  {
    _sortable=sortable;
  }

  /**
   * Get the associated cell renderer, if any.
   * @return A renderer or <code>null</code> to use defaults.
   */
  public TableCellRenderer getCellRenderer()
  {
    return _renderer;
  }

  /**
   * Set a specific cell renderer.
   * @param renderer Renderer to set.
   */
  public void setCellRenderer(TableCellRenderer renderer)
  {
    _renderer=renderer;
  }

  /**
   * Indicates if the 'use to string' has been forced by the user.
   * @return A Boolean value or <code>null</code> to use defaults.
   */
  public Boolean isUseToString()
  {
    return _useToString;
  }

  /**
   * Set a specific value for 'use to string'.
   * @param useToString Value to set.
   */
  public void setUseToString(Boolean useToString)
  {
    _useToString=useToString;
  }

  /**
   * Get the type of data in the managed column.
   * @return a data type.
   */
  public Class<VALUE> getDataType()
  {
    return _dataType;
  }

  /**
   * Get the header label for this column.
   * @return a label.
   */
  public String getHeader()
  {
    return _header;
  }

  /**
   * Get the cell value provider for this column.
   * @return a cell value provider.
   */
  public CellDataProvider<POJO,VALUE> getValueProvider()
  {
    return _valueProvider;
  }
}
