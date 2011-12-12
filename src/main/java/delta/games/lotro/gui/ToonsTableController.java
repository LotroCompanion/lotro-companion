package delta.games.lotro.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * @author DAM
 */
public class ToonsTableController
{
  private JTable _table;
  private HashMap<String,Character> _cache;
  private List<CharacterFile> _toons;

  private static final String[] COLUMN_NAMES=
  {
    "Name",
    "Race",
    "Class",
    "Level",
    "Server"
  };

  /**
   * Constructor.
   */
  public ToonsTableController()
  {
    _cache=new HashMap<String,Character>();
    _toons=new ArrayList<CharacterFile>();
    init();
  }

  private void reset()
  {
    _toons.clear();
    _cache.clear();
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
      }
      _toons.add(toon);
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
    ToonsTableModel model=new ToonsTableModel();
    JTable table=new JTable(model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    return table;
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
      return _toons.size();
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
    if (_table!=null)
    {
      _table.setModel(null);
      _table=null;
    }
    _cache=null;
    _toons=null;
  }
}
