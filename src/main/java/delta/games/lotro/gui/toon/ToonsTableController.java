package delta.games.lotro.gui.toon;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.utils.gui.tables.CellDataProvider;
import delta.games.lotro.utils.gui.tables.GenericTableController;
import delta.games.lotro.utils.gui.tables.ListDataProvider;
import delta.games.lotro.utils.gui.tables.TableColumnController;

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
  private List<CharacterFile> _toons;
  // GUI
  private JTable _table;
  private GenericTableController<CharacterFile> _tableController;

  /**
   * Constructor.
   */
  public ToonsTableController()
  {
    _toons=new ArrayList<CharacterFile>();
    init();
    _tableController=buildTable();
  }

  private GenericTableController<CharacterFile> buildTable()
  {
    ListDataProvider<CharacterFile> provider=new ListDataProvider<CharacterFile>(_toons);
    GenericTableController<CharacterFile> table=new GenericTableController<CharacterFile>(provider);

    // Name column
    {
      CellDataProvider<CharacterFile,String> nameCell=new CellDataProvider<CharacterFile,String>()
      {
        public String getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getName();
        }
      };
      TableColumnController<CharacterFile,String> nameColumn=new TableColumnController<CharacterFile,String>("Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,100,100);
      table.addColumnController(nameColumn);
    }
    // Race column
    {
      CellDataProvider<CharacterFile,Race> raceCell=new CellDataProvider<CharacterFile,Race>()
      {
        public Race getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getRace();
        }
      };
      TableColumnController<CharacterFile,Race> raceColumn=new TableColumnController<CharacterFile,Race>("Race",Race.class,raceCell);
      raceColumn.setWidthSpecs(100,100,100);
      table.addColumnController(raceColumn);
    }
    // Class column
    {
      CellDataProvider<CharacterFile,CharacterClass> classCell=new CellDataProvider<CharacterFile,CharacterClass>()
      {
        public CharacterClass getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getCharacterClass();
        }
      };
      TableColumnController<CharacterFile,CharacterClass> classColumn=new TableColumnController<CharacterFile,CharacterClass>("Class",CharacterClass.class,classCell);
      classColumn.setWidthSpecs(100,100,100);
      table.addColumnController(classColumn);
    }
    // Region column
    {
      CellDataProvider<CharacterFile,String> regionCell=new CellDataProvider<CharacterFile,String>()
      {
        public String getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getRegion();
        }
      };
      TableColumnController<CharacterFile,String> regionColumn=new TableColumnController<CharacterFile,String>("Region",String.class,regionCell);
      regionColumn.setWidthSpecs(100,100,100);
      table.addColumnController(regionColumn);
    }
    /*
    // Level column
    {
      CellDataProvider<CharacterFile,Integer> levelCell=new CellDataProvider<CharacterFile,Integer>()
      {
        public Integer getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return Integer.valueOf(data.getLevel());
        }
      };
      TableColumnController<CharacterFile,Integer> serverColumn=new TableColumnController<CharacterFile,Integer>("Level",Integer.class,levelCell);
      serverColumn.setWidthSpecs(80,80,80);
      table.addColumnController(serverColumn);
    }
    */
    // Server column
    {
      CellDataProvider<CharacterFile,String> serverCell=new CellDataProvider<CharacterFile,String>()
      {
        public String getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getServer();
        }
      };
      TableColumnController<CharacterFile,String> serverColumn=new TableColumnController<CharacterFile,String>("Server",String.class,serverCell);
      serverColumn.setWidthSpecs(100,100,100);
      table.addColumnController(serverColumn);
    }
    // Last update time
    /*
    {
      CellDataProvider<CharacterFile,Date> lastUpdateCell=new CellDataProvider<CharacterFile,Date>()
      {
        public Date getData(CharacterFile item)
        {
          return item.getLastInfoUpdate();
        }
      };
      TableColumnController<CharacterFile,Date> lastUpdateColumn=new TableColumnController<CharacterFile,Date>("Last update",Date.class,lastUpdateCell);
      lastUpdateColumn.setWidthSpecs(100,-1,100);
      table.addColumnController(lastUpdateColumn);
    }
    */
    return table;
  }

  private CharacterSummary getDataForToon(CharacterFile toon)
  {
    return toon.getSummary();
  }

  private void reset()
  {
    _toons.clear();
  }

  /**
   * Refresh toons table.
   */
  public void refresh()
  {
    init();
    if (_table!=null)
    {
      _tableController.refresh();
    }
  }

  /**
   * Refresh toons table.
   * @param toon Toon to refresh.
   */
  public void refresh(CharacterFile toon)
  {
    if (_table!=null)
    {
      int row=_toons.indexOf(toon);
      if (row!=-1)
      {
        _tableController.refreshRow(row);
      }
    }
  }

  private void init()
  {
    reset();
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    for(CharacterFile toon : toons)
    {
      loadToon(toon);
    }
  }

  private void loadToon(CharacterFile toon)
  {
    CharacterSummary summary=toon.getSummary();
    if (summary!=null)
    {
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
      _table=_tableController.getTable();
    }
    return _table;
  }

  /**
   * Add an action listener.
   * @param al Action listener to add.
   */
  public void addActionListener(ActionListener al)
  {
    _tableController.addActionListener(al);
  }

  /**
   * Remove an action listener.
   * @param al Action listener to remove.
   */
  public void removeActionListener(ActionListener al)
  {
    _tableController.removeActionListener(al);
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
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _toons=null;
  }
}
