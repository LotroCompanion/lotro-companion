package delta.games.lotro.gui.items;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemPropertyNames;

/**
 * Controller for a table that shows a choice of items.
 * @author DAM
 */
public class ItemChoiceTableController
{
  /**
   * 
   */
  private static final int ICON_COLUMN_INDEX=0;
  /**
   * 
   */
  private static final int ID_COLUMN_INDEX=1;
  /**
   * 
   */
  private static final int NAME_COLUMN_INDEX=2;
  /**
   * 
   */
  private static final int LEVEL_COLUMN_INDEX=3;
  // Data
  private List<Item> _items;
  private Filter<Item> _filter;
  // GUI
  private JTable _table;
  private ItemTableModel _model;
  private TableRowSorter<ItemTableModel> _sorter;
  private RowFilter<ItemTableModel,Integer> _guiFilter;

  private static final String[] COLUMN_NAMES=
  {
    "Icon",
    "ID",
    "Name",
    "Level"
  };

  /**
   * Constructor.
   * @param items Items to choose from.
   * @param filter Log filter.
   */
  public ItemChoiceTableController(List<Item> items, Filter<Item> filter)
  {
    _items=items;
    _filter=filter;
  }

  /**
   * Set a new set of items.
   * @param items Items to set.
   */
  public void setItems(List<Item> items)
  {
    _items=items;
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

  private JTable build()
  {
    _model=new ItemTableModel();
    JTable table=GuiFactory.buildTable();
    table.setModel(_model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    table.setRowHeight(32);
    _sorter=new TableRowSorter<ItemTableModel>(_model) {

      @Override
      protected boolean useToString(int column)
      {
        if (column==ICON_COLUMN_INDEX) return false;
        if (column==ID_COLUMN_INDEX) return false;
        if (column==NAME_COLUMN_INDEX) return true;
        if (column==LEVEL_COLUMN_INDEX) return false;
        return super.useToString(column);
      }
    };
    TableColumn iconColumn=table.getColumnModel().getColumn(ICON_COLUMN_INDEX);
    iconColumn.setPreferredWidth(50);
    iconColumn.setResizable(false);
    iconColumn.setMinWidth(100);
    iconColumn.setMaxWidth(100);
    TableColumn idColumn=table.getColumnModel().getColumn(ID_COLUMN_INDEX);
    idColumn.setPreferredWidth(50);
    idColumn.setResizable(false);
    idColumn.setMinWidth(100);
    idColumn.setMaxWidth(100);
    TableColumn nameColumn=table.getColumnModel().getColumn(NAME_COLUMN_INDEX);
    nameColumn.setPreferredWidth(150);
    TableColumn levelColumn=table.getColumnModel().getColumn(LEVEL_COLUMN_INDEX);
    levelColumn.setPreferredWidth(50);
    levelColumn.setResizable(false);
    levelColumn.setMinWidth(100);
    levelColumn.setMaxWidth(100);

    _guiFilter=new RowFilter<ItemTableModel,Integer>()
    {
      @Override
      public boolean include(RowFilter.Entry<? extends ItemTableModel,? extends Integer> entry)
      {
        Integer id=entry.getIdentifier();
        Item item=_items.get(id.intValue());
        boolean ret=_filter.accept(item);
        return ret;
      }
    };
    _sorter.setRowFilter(_guiFilter);
    _sorter.setSortable(ICON_COLUMN_INDEX,false);
    _sorter.setSortable(ID_COLUMN_INDEX,true);
    _sorter.setSortable(NAME_COLUMN_INDEX,true);
    _sorter.setSortable(LEVEL_COLUMN_INDEX,true);
    _sorter.setMaxSortKeys(3);
    table.setRowSorter(_sorter);
    return table;
  }

  /**
   * Get the currently selected item.
   * @return An item or <code>null</code> if not found.
   */
  public Item getSelectedItem()
  {
    Item ret=null;
    int selectedItemIndex=_table.getSelectedRow();
    if (selectedItemIndex!=-1)
    {
      int modelIndex=_table.convertRowIndexToModel(selectedItemIndex);
      ret=_items.get(modelIndex);
    }
    return ret;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _sorter.setRowFilter(_guiFilter);
  }

  private static Class<?>[] TYPES=new Class [] { ImageIcon.class, Long.class, String.class, Integer.class};

  private class ItemTableModel extends AbstractTableModel
  {
    /**
     * Constructor.
     */
    public ItemTableModel()
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
      return (_items!=null)?_items.size():0;
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
      Item item=_items.get(rowIndex);
      if (item!=null)
      {
        if (columnIndex==ICON_COLUMN_INDEX)
        {
          String iconId=item.getProperty(ItemPropertyNames.ICON_ID);
          String backgroundIconId=item.getProperty(ItemPropertyNames.BACKGROUND_ICON_ID);
          ret=IconsManager.getItemIcon(iconId, backgroundIconId);
        }
        else if (columnIndex==ID_COLUMN_INDEX)
        {
          ret=Long.valueOf(item.getIdentifier());
        }
        else if (columnIndex==NAME_COLUMN_INDEX)
        {
          ret=item.getName();
        }
        else if (columnIndex==LEVEL_COLUMN_INDEX)
        {
          ret=item.getItemLevel();
        }
      }
      return ret;
    }
  }

  /**
   * Get the total number of items in the managed log.
   * @return A number of items.
   */
  public int getNbItems()
  {
    return (_items!=null)?_items.size():0;
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
    _guiFilter=null;
    // Data
    _items=null;
    _filter=null;
  }
}
