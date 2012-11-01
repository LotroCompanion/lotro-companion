package delta.games.lotro.gui.toon;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.gui.utils.GuiFactory;

/**
 * Controller for a table that shows all available toons.
 * @author DAM
 */
public class ToonsTableController
{
  /**
   * Double-click action command.
   */
  public static final String DOUBLE_CLICK="double click";
  // Data
  private HashMap<String,Character> _cache;
  private List<CharacterFile> _toons;
  // GUI
  private JTable _table;
  private ToonsTableModel _model;
  private TableRowSorter<ToonsTableModel> _sorter;
  // Control
  private List<ActionListener> _actionListeners;

  private static final String[] COLUMN_NAMES=
  {
    "Name",
    "Race",
    "Class",
    "Level",
    "Server"
  };
  private static final Class<?>[] COLUMN_CLASSES=
  {
    String.class, Race.class, CharacterClass.class, Integer.class, String.class
  };
  private static final int[] MIN_WIDTH = { 100, 100, 100, 100, 100 };
  private static final int[] MAX_WIDTH = { 100, 100, 100, 100, 100 };
  private static final int[] PREFERRED_WIDTH = { 100, 100, 100, 100, 100 };

  /**
   * Constructor.
   */
  public ToonsTableController()
  {
    _cache=new HashMap<String,Character>();
    _toons=new ArrayList<CharacterFile>();
    _actionListeners=new ArrayList<ActionListener>();
    init();
  }

  private void reset()
  {
    _toons.clear();
    _cache.clear();
  }

  /**
   * Refresh toons table.
   */
  public void refresh()
  {
    init();
    if (_table!=null)
    {
      _model.fireTableDataChanged();
    }
  }

  private void init()
  {
    reset();
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    for(CharacterFile toon : toons)
    {
      Character c=toon.getLastCharacterInfo();
      if (c!=null)
      {
        String id=toon.getIdentifier();
        _cache.put(id,c);
        _toons.add(toon);
      }
      toon.getLogsManager().pruneLogFiles();
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

  /*
   * For future use: class and race (enums) renderers
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
  */

  private JTable build()
  {
    _model=new ToonsTableModel();
    final JTable table=GuiFactory.buildTable();
    table.setModel(_model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    _sorter=new TableRowSorter<ToonsTableModel>(_model) {

      @Override
      protected boolean useToString(int column)
      {
        if (column==3) return false; // Level
        
        return super.useToString(column);
      }
      
    };
    for(int i=0;i<COLUMN_NAMES.length;i++)
    {
      TableColumn column=table.getColumnModel().getColumn(i);
      int preferredWidth=PREFERRED_WIDTH[i];
      if (preferredWidth>=0)
      {
        column.setPreferredWidth(preferredWidth);
      }
      int minWidth=MIN_WIDTH[i];
      if (minWidth>=0)
      {
        column.setMinWidth(minWidth);
      }
      int setMaxWidth=MAX_WIDTH[i];
      if (setMaxWidth>=0)
      {
        column.setMaxWidth(setMaxWidth);
      }
    }
    //typeColumn.setCellRenderer(new LogItemTypeRenderer());
    for(int i=0;i<COLUMN_NAMES.length;i++)
    {
      _sorter.setSortable(i,true);
    }
    _sorter.setMaxSortKeys(3);
    table.setRowSorter(_sorter);
    
    MouseAdapter doucleClickAdapter=new MouseAdapter()
    {
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
    CharacterFile toon=_toons.get(row);
    ActionEvent e=new ActionEvent(toon,0,DOUBLE_CLICK);
    ActionListener[] als=_actionListeners.toArray(new ActionListener[_actionListeners.size()]);
    for(ActionListener al : als)
    {
      al.actionPerformed(e);
    }
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

  private class ToonsTableModel extends AbstractTableModel
  {
    /**
     * Constructor.
     */
    public ToonsTableModel()
    {
      // TODO: initialize data table cache?
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
      return (_toons!=null)?_toons.size():0;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
      return COLUMN_CLASSES[columnIndex];
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
      CharacterFile toon=_toons.get(rowIndex);
      if (toon!=null)
      {
        String id=toon.getIdentifier();
        Character c=_cache.get(id);
        if (c!=null)
        {
          if (columnIndex==0)
          {
            ret=c.getName();
          }
          else if (columnIndex==1)
          {
            ret=c.getRace();
          }
          else if (columnIndex==2)
          {
            ret=c.getCharacterClass();
          }
          else if (columnIndex==3)
          {
            ret=Integer.valueOf(c.getLevel());
          }
          else if (columnIndex==4)
          {
            ret=c.getServer();
          }
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
      _table=null;
    }
    _model=null;
    _sorter=null;
    // Data
    _cache=null;
    _toons=null;
  }
}
