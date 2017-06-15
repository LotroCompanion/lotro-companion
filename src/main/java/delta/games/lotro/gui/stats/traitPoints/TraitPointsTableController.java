package delta.games.lotro.gui.stats.traitPoints;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPointFilter;
import delta.games.lotro.stats.traitPoints.TraitPointStatus;
import delta.games.lotro.stats.traitPoints.TraitPoints;
import delta.games.lotro.stats.traitPoints.TraitPointsRegistry;
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
  private CharacterFile _character;
  private TraitPointFilter _filter;
  // GUI
  private GenericTableController<TraitPointStatus> _tableController;
  private JTable _table;

  /**
   * Constructor.
   * @param character Character to use.
   * @param filter Data filter.
   */
  public TraitPointsTableController(CharacterFile character, TraitPointFilter filter)
  {
    _character=character;
    _filter=filter;
    _tableController=buildTable();
  }

  private DataProvider<TraitPointStatus> buildDataProvider()
  {
    CharacterSummary summary=_character.getSummary();
    CharacterClass characterClass=summary.getCharacterClass();
    TraitPointsRegistry registry=TraitPoints.get().getRegistry();
    List<TraitPoint> points=registry.getPointsForClass(characterClass);
    List<TraitPointStatus> selectedPoints=new ArrayList<TraitPointStatus>();
    TraitPointsStatus pointsStatus=TraitPoints.get().load(_character);
    for(TraitPoint point : points)
    {
      if (_filter.accept(point))
      {
        TraitPointStatus status=new TraitPointStatus(point);
        boolean acquired=pointsStatus.isAcquired(point.getId());
        status.setAcquired(acquired);
        selectedPoints.add(status);
      }
    }
    DataProvider<TraitPointStatus> ret=new ListDataProvider<TraitPointStatus>(selectedPoints);
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
    _character=null;
    _filter=null;
  }
}
