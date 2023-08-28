package delta.games.lotro.gui.kinship;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.games.lotro.common.id.InternalGameId;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipSummary;
import delta.games.lotro.kinship.KinshipsManager;
import delta.games.lotro.kinship.events.KinshipEvent;
import delta.games.lotro.kinship.events.KinshipEventType;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a table that shows all available kinships.
 * @author DAM
 */
public class KinshipsTableController implements GenericEventsListener<KinshipEvent>
{
  /**
   * Double-click action command.
   */
  public static final String DOUBLE_CLICK="double click";
  // Data
  private List<Kinship> _kinships;
  // GUI
  private JTable _table;
  private GenericTableController<Kinship> _tableController;

  /**
   * Constructor.
   */
  public KinshipsTableController()
  {
    _kinships=new ArrayList<Kinship>();
    init();
    _tableController=buildTable();
    EventsManager.addListener(KinshipEvent.class,this);
  }

  private GenericTableController<Kinship> buildTable()
  {
    ListDataProvider<Kinship> provider=new ListDataProvider<Kinship>(_kinships);
    GenericTableController<Kinship> table=new GenericTableController<Kinship>(provider);

    // ID column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<Kinship,String> iidCell=new CellDataProvider<Kinship,String>()
      {
        @Override
        public String getData(Kinship kinship)
        {
          Long iid=kinship.getID();
          if (iid!=null)
          {
            return new InternalGameId(iid.longValue()).asPersistedString();
          }
          return null;
        }
      };
      DefaultTableColumnController<Kinship,String> iidColumn=new DefaultTableColumnController<Kinship,String>(KinshipColumnIds.IID.name(),"ID",String.class,iidCell);
      iidColumn.setWidthSpecs(130,130,130);
      table.addColumnController(iidColumn);
    }
    // Name column
    {
      CellDataProvider<Kinship,String> nameCell=new CellDataProvider<Kinship,String>()
      {
        @Override
        public String getData(Kinship kinship)
        {
          KinshipSummary summary=kinship.getSummary();
          return summary.getName();
        }
      };
      DefaultTableColumnController<Kinship,String> nameColumn=new DefaultTableColumnController<Kinship,String>(KinshipColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,100,100);
      table.addColumnController(nameColumn);
    }
    // Creation date
    {
      CellDataProvider<Kinship,Long> creationCell=new CellDataProvider<Kinship,Long>()
      {
        public Long getData(Kinship kinship)
        {
          return kinship.getSummary().getCreationDate();
        }
      };
      DefaultTableColumnController<Kinship,Long> creationColumn=new DefaultTableColumnController<Kinship,Long>(KinshipColumnIds.CREATION_DATE.name(),"Creation",Long.class,creationCell);
      StatColumnsUtils.configureDateTimeColumn(creationColumn);
      table.addColumnController(creationColumn);
    }
    // Message of the day
    {
      CellDataProvider<Kinship,String> motdCell=new CellDataProvider<Kinship,String>()
      {
        @Override
        public String getData(Kinship kinship)
        {
          return kinship.getSummary().getMotd();
        }
      };
      DefaultTableColumnController<Kinship,String> motdColumn=new DefaultTableColumnController<Kinship,String>(KinshipColumnIds.MOTD.name(),"Message of the day",String.class,motdCell);
      motdColumn.setWidthSpecs(100,-1,200);
      table.addColumnController(motdColumn);
    }
    return table;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<Kinship> getTableController()
  {
    return _tableController;
  }

  private void reset()
  {
    _kinships.clear();
  }

  /**
   * Refresh table.
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
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(KinshipEvent event)
  {
    KinshipEventType type=event.getType();
    if (type==KinshipEventType.KINSHIP_SUMMARY_UPDATED)
    {
      Kinship kinship=event.getKinship();
      _tableController.refresh(kinship);
    }
  }

  private void init()
  {
    reset();
    KinshipsManager manager=KinshipsManager.getInstance();
    List<Kinship> kinships=manager.getAllKinships();
    for(Kinship kinship : kinships)
    {
      loadKinship(kinship);
    }
  }

  private void loadKinship(Kinship kinship)
  {
    KinshipSummary summary=kinship.getSummary();
    if (summary!=null)
    {
      _kinships.add(kinship);
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
    EventsManager.removeListener(KinshipEvent.class,this);
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
    _kinships=null;
  }
}
