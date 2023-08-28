package delta.games.lotro.gui.character;

import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.Sort;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterInfosManager;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a table that shows all available data for a single toon.
 * @author DAM
 */
public class CharacterDataTableController implements GenericEventsListener<CharacterEvent>
{
  // Column IDs
  private static final String DATE="DATE";
  private static final String LEVEL="LEVEL";
  private static final String DESCRIPTION="DESCRIPTION";

  // Data
  private CharacterFile _toon;
  // GUI
  private JTable _table;
  private GenericTableController<CharacterData> _tableController;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterDataTableController(CharacterFile toon)
  {
    _toon=toon;
    _toon.getInfosManager().sync();
    _tableController=buildTable();
    EventsManager.addListener(CharacterEvent.class,this);
  }

  private DataProvider<CharacterData> buildDataProvider()
  {
    DataProvider<CharacterData> ret=new DataProvider<CharacterData>()
    {
      @Override
      public CharacterData getAt(int index)
      {
        CharacterInfosManager dataMgr=_toon.getInfosManager();
        return dataMgr.getData(index);
      }

      @Override
      public int getCount()
      {
        if (_toon!=null)
        {
          CharacterInfosManager dataMgr=_toon.getInfosManager();
          return dataMgr.getDataCount();
        }
        return 0;
      }
    };
    return ret;
  }

  private GenericTableController<CharacterData> buildTable()
  {
    DataProvider<CharacterData> provider=buildDataProvider();
    GenericTableController<CharacterData> table=new GenericTableController<CharacterData>(provider);

    // Date
    {
      CellDataProvider<CharacterData,Date> lastUpdateCell=new CellDataProvider<CharacterData,Date>()
      {
        @Override
        public Date getData(CharacterData item)
        {
          Long timestamp=item.getDate();
          return (timestamp!=null)?new Date(timestamp.longValue()):null;
        }
      };
      DefaultTableColumnController<CharacterData,Date> lastUpdateColumn=new DefaultTableColumnController<CharacterData,Date>(DATE,"Date",Date.class,lastUpdateCell); // I18n
      StatColumnsUtils.configureDateTimeColumn(lastUpdateColumn);
      table.addColumnController(lastUpdateColumn);
    }
    // Level column
    {
      CellDataProvider<CharacterData,Integer> levelCell=new CellDataProvider<CharacterData,Integer>()
      {
        @Override
        public Integer getData(CharacterData item)
        {
          return Integer.valueOf(item.getLevel());
        }
      };
      DefaultTableColumnController<CharacterData,Integer> serverColumn=new DefaultTableColumnController<CharacterData,Integer>(LEVEL,"Level",Integer.class,levelCell); // I18n
      serverColumn.setWidthSpecs(80,80,80);
      table.addColumnController(serverColumn);
    }
    // Description column
    {
      CellDataProvider<CharacterData,String> descriptionCell=new CellDataProvider<CharacterData,String>()
      {
        @Override
        public String getData(CharacterData item)
        {
          return item.getShortDescription();
        }
      };
      DefaultTableColumnController<CharacterData,String> descriptionColumn=new DefaultTableColumnController<CharacterData,String>(DESCRIPTION,"Description",String.class,descriptionCell); // I18n
      descriptionColumn.setWidthSpecs(100,-1,100);
      table.addColumnController(descriptionColumn);
    }
    String sort=Sort.SORT_DESCENDING+DATE;
    table.setSort(Sort.buildFromString(sort));
    return table;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<CharacterData> getTableController()
  {
    return _tableController;
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
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.CHARACTER_DATA_UPDATED)
    {
      CharacterData data=event.getToonData();
      _tableController.refresh(data);
    }
    if ((type==CharacterEventType.CHARACTER_DATA_ADDED) || (type==CharacterEventType.CHARACTER_DATA_REMOVED))
    {
      _tableController.refresh();
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
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    EventsManager.removeListener(CharacterEvent.class,this);
    _toon=null;
  }
}
