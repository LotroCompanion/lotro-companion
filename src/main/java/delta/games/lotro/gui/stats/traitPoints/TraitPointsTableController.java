package delta.games.lotro.gui.stats.traitPoints;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPointStatus;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;
import delta.games.lotro.utils.gui.tables.CellDataProvider;
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
  // GUI
  private GenericTableController<TraitPointStatus> _tableController;
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
  }

  private DataProvider<TraitPointStatus> buildDataProvider()
  {
    List<TraitPointStatus> pointStatuses=new ArrayList<TraitPointStatus>();
    for(TraitPoint point : _points)
    {
      TraitPointStatus status=new TraitPointStatus(point);
      boolean acquired=_pointsStatus.isAcquired(point.getId());
      status.setAcquired(acquired);
      pointStatuses.add(status);
    }
    DataProvider<TraitPointStatus> ret=new ListDataProvider<TraitPointStatus>(pointStatuses);
    return ret;
  }

  private GenericTableController<TraitPointStatus> buildTable()
  {
    DataProvider<TraitPointStatus> provider=buildDataProvider();
    GenericTableController<TraitPointStatus> table=new GenericTableController<TraitPointStatus>(provider);

    // Acquired
    {
      CellDataProvider<TraitPointStatus,Boolean> acquiredCell=new CellDataProvider<TraitPointStatus,Boolean>()
      {
        public Boolean getData(TraitPointStatus item)
        {
          return Boolean.valueOf(item.isAcquired());
        }
      };
      TableColumnController<TraitPointStatus,Boolean> acquiredColumn=new TableColumnController<TraitPointStatus,Boolean>("Acquired",Boolean.class,acquiredCell);
      acquiredColumn.setWidthSpecs(80,80,80);
      table.addColumnController(acquiredColumn);
    }
    // Label column
    {
      CellDataProvider<TraitPointStatus,String> descriptionCell=new CellDataProvider<TraitPointStatus,String>()
      {
        public String getData(TraitPointStatus item)
        {
          return item.getTraitPoint().getLabel();
        }
      };
      TableColumnController<TraitPointStatus,String> labelColumn=new TableColumnController<TraitPointStatus,String>("Label",String.class,descriptionCell);
      labelColumn.setWidthSpecs(100,-1,100);
      table.addColumnController(labelColumn);
    }
    return table;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<TraitPointStatus> getTableController()
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
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    _tableController=null;
    _table=null;
    // Data
    _pointsStatus=null;
    _points=null;
  }
}
