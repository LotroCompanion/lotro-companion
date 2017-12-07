package delta.games.lotro.gui.character;

import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterInfosManager;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventListener;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.utils.Formats;

/**
 * Controller for a table that shows all available data for a single toon.
 * @author DAM
 */
public class CharacterDataTableController implements CharacterEventListener
{
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
    CharacterEventsManager.addListener(this);
  }

  private DataProvider<CharacterData> buildDataProvider()
  {
    DataProvider<CharacterData> ret=new DataProvider<CharacterData>()
    {
      public CharacterData getAt(int index)
      {
        CharacterInfosManager dataMgr=_toon.getInfosManager();
        return dataMgr.getData(index);
      }

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
        public Date getData(CharacterData item)
        {
          Long timestamp=item.getDate();
          return (timestamp!=null)?new Date(timestamp.longValue()):null;
        }
      };
      TableColumnController<CharacterData,Date> lastUpdateColumn=new TableColumnController<CharacterData,Date>("Date",Date.class,lastUpdateCell);
      lastUpdateColumn.setWidthSpecs(120,120,120);
      lastUpdateColumn.setCellRenderer(new DateRenderer(Formats.DATE_PATTERN));
      table.addColumnController(lastUpdateColumn);
    }
    // Level column
    {
      CellDataProvider<CharacterData,Integer> levelCell=new CellDataProvider<CharacterData,Integer>()
      {
        public Integer getData(CharacterData item)
        {
          return Integer.valueOf(item.getLevel());
        }
      };
      TableColumnController<CharacterData,Integer> serverColumn=new TableColumnController<CharacterData,Integer>("Level",Integer.class,levelCell);
      serverColumn.setWidthSpecs(80,80,80);
      table.addColumnController(serverColumn);
    }
    // Description column
    {
      CellDataProvider<CharacterData,String> descriptionCell=new CellDataProvider<CharacterData,String>()
      {
        public String getData(CharacterData item)
        {
          return item.getShortDescription();
        }
      };
      TableColumnController<CharacterData,String> descriptionColumn=new TableColumnController<CharacterData,String>("Description",String.class,descriptionCell);
      descriptionColumn.setWidthSpecs(100,-1,100);
      table.addColumnController(descriptionColumn);
    }
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
   * @param type Event type.
   * @param event Source event.
   */
  public void eventOccurred(CharacterEventType type, CharacterEvent event)
  {
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
    CharacterEventsManager.removeListener(this);
    _toon=null;
  }
}
