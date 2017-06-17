package delta.games.lotro.gui.stats.traitPoints;

import java.util.List;

import javax.swing.JTable;

import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;
import delta.games.lotro.utils.gui.tables.CellDataProvider;
import delta.games.lotro.utils.gui.tables.CellDataUpdater;
import delta.games.lotro.utils.gui.tables.DataProvider;
import delta.games.lotro.utils.gui.tables.GenericTableController;
import delta.games.lotro.utils.gui.tables.ListDataProvider;
import delta.games.lotro.utils.gui.tables.TableColumnController;

/**
 * Controller for a table that shows the trait points status for a character.
 * @author DAM
 */
public class TraitPointsTableController
{
  // Data
  private TraitPointsStatus _pointsStatus;
  private List<TraitPoint> _points;
  // Controllers
  private GenericTableController<TraitPoint> _tableController;
  private TraitPointsStatusListener _listener;
  // GUI
  private JTable _table;

  /**
   * Constructor.
   * @param pointsStatus Status to edit.
   * @param points Points to edit.
   */
  public TraitPointsTableController(TraitPointsStatus pointsStatus, List<TraitPoint> points)
  {
    _pointsStatus=pointsStatus;
    _points=points;
    _tableController=buildTable();
    initTable();
  }

  /**
   * Set the listener for the managed trait points status.
   * @param listener Listener to set.
   */
  public void setListener(TraitPointsStatusListener listener)
  {
    _listener=listener;
  }

  private GenericTableController<TraitPoint> buildTable()
  {
    DataProvider<TraitPoint> provider=new ListDataProvider<TraitPoint>(_points);
    GenericTableController<TraitPoint> table=new GenericTableController<TraitPoint>(provider);

    // Acquired
    {
      CellDataProvider<TraitPoint,Boolean> acquiredCell=new CellDataProvider<TraitPoint,Boolean>()
      {
        public Boolean getData(TraitPoint item)
        {
          return Boolean.valueOf(_pointsStatus.isAcquired(item.getId()));
        }
      };
      TableColumnController<TraitPoint,Boolean> acquiredColumn=new TableColumnController<TraitPoint,Boolean>("Acquired",Boolean.class,acquiredCell);
      acquiredColumn.setWidthSpecs(80,80,80);
      acquiredColumn.setEditable(true);
      acquiredColumn.setCellRenderer(new TraitPointCellRenderer());
      CellDataUpdater<TraitPoint> acquiredCellUpdater=new CellDataUpdater<TraitPoint>()
      {
        public void setData(TraitPoint item, Object value)
        {
          boolean acquired=((Boolean)value).booleanValue();
          _pointsStatus.setStatus(item.getId(),acquired);
          if (_listener!=null)
          {
            _listener.statusUpdated();
          }
        }
      };
      acquiredColumn.setValueUpdater(acquiredCellUpdater);
      table.addColumnController(acquiredColumn);
    }
    // Label column
    {
      CellDataProvider<TraitPoint,String> descriptionCell=new CellDataProvider<TraitPoint,String>()
      {
        public String getData(TraitPoint item)
        {
          return item.getLabel();
        }
      };
      TableColumnController<TraitPoint,String> labelColumn=new TableColumnController<TraitPoint,String>("Label",String.class,descriptionCell);
      labelColumn.setWidthSpecs(100,-1,100);
      table.addColumnController(labelColumn);
    }
    return table;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<TraitPoint> getTableController()
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

  private void initTable()
  {
    JTable table=getTable();
    table.setShowGrid(false);
    table.setRowSelectionAllowed(false);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    _tableController=null;
    _listener=null;
    // GUI
    _table=null;
    // Data
    _pointsStatus=null;
    _points=null;
  }
}
