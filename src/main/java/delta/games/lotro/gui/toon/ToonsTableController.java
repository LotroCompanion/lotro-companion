package delta.games.lotro.gui.toon;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.Sort;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.Race;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a table that shows all available toons.
 * @author DAM
 */
public class ToonsTableController implements GenericEventsListener<CharacterEvent>
{
  private static final String NAME="NAME";
  private static final String SERVER="SERVER";

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
    _tableController=buildTable();
    EventsManager.addListener(CharacterEvent.class,this);
  }

  private GenericTableController<CharacterFile> buildTable()
  {
    ListDataProvider<CharacterFile> provider=new ListDataProvider<CharacterFile>(_toons);
    GenericTableController<CharacterFile> table=new GenericTableController<CharacterFile>(provider);

    // Name column
    {
      CellDataProvider<CharacterFile,String> nameCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getName();
        }
      };
      DefaultTableColumnController<CharacterFile,String> nameColumn=new DefaultTableColumnController<CharacterFile,String>(NAME,"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,100,100);
      table.addColumnController(nameColumn);
    }
    // Race column
    {
      CellDataProvider<CharacterFile,Race> raceCell=new CellDataProvider<CharacterFile,Race>()
      {
        @Override
        public Race getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getRace();
        }
      };
      DefaultTableColumnController<CharacterFile,Race> raceColumn=new DefaultTableColumnController<CharacterFile,Race>("Race",Race.class,raceCell);
      raceColumn.setWidthSpecs(100,100,100);
      table.addColumnController(raceColumn);
    }
    // Class column
    {
      CellDataProvider<CharacterFile,CharacterClass> classCell=new CellDataProvider<CharacterFile,CharacterClass>()
      {
        @Override
        public CharacterClass getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getCharacterClass();
        }
      };
      DefaultTableColumnController<CharacterFile,CharacterClass> classColumn=new DefaultTableColumnController<CharacterFile,CharacterClass>("Class",CharacterClass.class,classCell);
      classColumn.setWidthSpecs(100,100,100);
      table.addColumnController(classColumn);
    }
    // Sex column
    {
      CellDataProvider<CharacterFile,CharacterSex> sexCell=new CellDataProvider<CharacterFile,CharacterSex>()
      {
        @Override
        public CharacterSex getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getCharacterSex();
        }
      };
      DefaultTableColumnController<CharacterFile,CharacterSex> sexColumn=new DefaultTableColumnController<CharacterFile,CharacterSex>("Sex",CharacterSex.class,sexCell);
      sexColumn.setWidthSpecs(80,80,80);
      table.addColumnController(sexColumn);
    }
    // Region column
    {
      CellDataProvider<CharacterFile,String> regionCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getRegion();
        }
      };
      DefaultTableColumnController<CharacterFile,String> regionColumn=new DefaultTableColumnController<CharacterFile,String>("Region",String.class,regionCell);
      regionColumn.setWidthSpecs(100,100,100);
      table.addColumnController(regionColumn);
    }
    // Level column
    {
      CellDataProvider<CharacterFile,Integer> levelCell=new CellDataProvider<CharacterFile,Integer>()
      {
        @Override
        public Integer getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return Integer.valueOf(data.getLevel());
        }
      };
      DefaultTableColumnController<CharacterFile,Integer> serverColumn=new DefaultTableColumnController<CharacterFile,Integer>("Level",Integer.class,levelCell);
      serverColumn.setWidthSpecs(80,80,80);
      table.addColumnController(serverColumn);
    }
    // Server column
    {
      CellDataProvider<CharacterFile,String> serverCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile item)
        {
          CharacterSummary data=getDataForToon(item);
          return data.getServer();
        }
      };
      DefaultTableColumnController<CharacterFile,String> serverColumn=new DefaultTableColumnController<CharacterFile,String>(SERVER,"Server",String.class,serverCell);
      serverColumn.setWidthSpecs(100,100,100);
      table.addColumnController(serverColumn);
    }
    // Account column
    {
      CellDataProvider<CharacterFile,String> accountCell=new CellDataProvider<CharacterFile,String>()
      {
        @Override
        public String getData(CharacterFile item)
        {
          return item.getAccountName();
        }
      };
      DefaultTableColumnController<CharacterFile,String> accountColumn=new DefaultTableColumnController<CharacterFile,String>("Account",String.class,accountCell);
      accountColumn.setWidthSpecs(100,100,100);
      table.addColumnController(accountColumn);
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
      lastUpdateColumn.setCellRenderer(new DateRenderer(Formats.DATE_PATTERN));
      table.addColumnController(lastUpdateColumn);
    }
    */
    String sort=Sort.SORT_ASCENDING+NAME+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+SERVER;
    table.setSort(Sort.buildFromString(sort));
    return table;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<CharacterFile> getTableController()
  {
    return _tableController;
  }

  private CharacterSummary getDataForToon(CharacterFile toon)
  {
    CharacterSummary summary=toon.getSummary();
    if (summary==null)
    {
      summary=new CharacterSummary();
    }
    return summary;
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.CHARACTER_SUMMARY_UPDATED)
    {
      CharacterFile toon=event.getToonFile();
      _tableController.refresh(toon);
    }
  }

  /**
   * Set the characters to show.
   * @param toons List of characters to show.
   */
  public void setToons(List<CharacterFile> toons)
  {
    _toons.clear();
    for(CharacterFile toon : toons)
    {
      CharacterSummary summary=toon.getSummary();
      if (summary!=null)
      {
        _toons.add(toon);
      }
    }
    _tableController.refresh();
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
    // Listeners
    EventsManager.removeListener(CharacterEvent.class,this);
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _toons=null;
  }
}
