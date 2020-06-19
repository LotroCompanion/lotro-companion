package delta.games.lotro.gui.stats.traitPoints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.CellDataUpdater;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;

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
  private WindowsManager _achievableWindows;
  private GenericTableController<TraitPoint> _tableController;
  private TraitPointsStatusListener _listener;
  // GUI
  private JTable _table;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param pointsStatus Status to edit.
   * @param points Points to edit.
   */
  public TraitPointsTableController(WindowController parent, TraitPointsStatus pointsStatus, List<TraitPoint> points)
  {
    _pointsStatus=pointsStatus;
    _points=points;
    _achievableWindows=new WindowsManager();
    _tableController=buildTable(parent);
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

  private GenericTableController<TraitPoint> buildTable(final WindowController parent)
  {
    DataProvider<TraitPoint> provider=new ListDataProvider<TraitPoint>(_points);
    GenericTableController<TraitPoint> table=new GenericTableController<TraitPoint>(provider);

    // Acquired
    {
      CellDataProvider<TraitPoint,Boolean> acquiredCell=new CellDataProvider<TraitPoint,Boolean>()
      {
        @Override
        public Boolean getData(TraitPoint item)
        {
          return Boolean.valueOf(_pointsStatus.isAcquired(item.getId()));
        }
      };
      DefaultTableColumnController<TraitPoint,Boolean> acquiredColumn=new DefaultTableColumnController<TraitPoint,Boolean>("Acquired",Boolean.class,acquiredCell);
      acquiredColumn.setWidthSpecs(80,80,80);
      acquiredColumn.setEditable(true);
      acquiredColumn.setCellRenderer(new TraitPointCellRenderer());
      CellDataUpdater<TraitPoint> acquiredCellUpdater=new CellDataUpdater<TraitPoint>()
      {
        @Override
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
        @Override
        public String getData(TraitPoint item)
        {
          return item.getLabel();
        }
      };
      DefaultTableColumnController<TraitPoint,String> labelColumn=new DefaultTableColumnController<TraitPoint,String>("Label",String.class,descriptionCell);
      labelColumn.setWidthSpecs(100,-1,100);
      table.addColumnController(labelColumn);
    }
    // Show
    DefaultTableColumnController<TraitPoint,String> column=table.buildButtonColumn("SHOW","Show...",90);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        TraitPoint data=(TraitPoint)e.getSource();
        Achievable achievable=getAchievable(data.getAchievableId());
        showAchievable(parent,achievable);
      }
    };
    column.setActionListener(al);
    table.addColumnController(column);
    return table;
  }

  private void showAchievable(WindowController parent, Achievable achievable)
  {
    if (achievable==null)
    {
      return;
    }
    int id=_achievableWindows.getAll().size();
    NavigatorWindowController window=NavigatorFactory.buildNavigator(parent,id);
    PageIdentifier ref=ReferenceConstants.getAchievableReference(achievable);
    window.navigateTo(ref);
    window.show(false);
    _achievableWindows.registerWindow(window);
  }

  private Achievable getAchievable(int id)
  {
    DeedsManager deedsMgr=DeedsManager.getInstance();
    Achievable ret=deedsMgr.getDeed(id);
    if (ret==null)
    {
      QuestsManager questsMgr=QuestsManager.getInstance();
      ret=questsMgr.getQuest(id);
    }
    return ret;
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
    _listener=null;
    if (_achievableWindows!=null)
    {
      _achievableWindows.dispose();
      _achievableWindows=null;
    }
    // GUI
    _table=null;
    // Data
    _pointsStatus=null;
    _points=null;
  }
}
