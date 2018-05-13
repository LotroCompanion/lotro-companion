package delta.games.lotro.gui.stats.deeds.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.stats.deeds.DeedsStatistics;
import delta.games.lotro.stats.deeds.statistics.SkillEvent;
import delta.games.lotro.utils.Formats;

/**
 * Controller for a table that shows the skill events for a single character.
 * @author DAM
 */
public class SkillEventsTableController
{
  private static final String SKILL="SKILL";
  private static final String DATE="COMPLETION_DATE";
  private static final String DEED_NAME="DEED";

  // Data
  private DeedsStatistics _stats;
  // GUI
  private JTable _table;
  private GenericTableController<SkillEvent> _tableController;

  /**
   * Constructor.
   * @param stats Stats to show.
   */
  public SkillEventsTableController(DeedsStatistics stats)
  {
    _stats=stats;
    _tableController=buildTable();
  }

  private GenericTableController<SkillEvent> buildTable()
  {
    ListDataProvider<SkillEvent> provider=new ListDataProvider<SkillEvent>(_stats.getSkills());
    GenericTableController<SkillEvent> table=new GenericTableController<SkillEvent>(provider);

    // Skill
    {
      CellDataProvider<SkillEvent,String> skillCell=new CellDataProvider<SkillEvent,String>()
      {
        @Override
        public String getData(SkillEvent item)
        {
          return item.getSkill();
        }
      };
      DefaultTableColumnController<SkillEvent,String> skillColumn=new DefaultTableColumnController<SkillEvent,String>(SKILL,"Skill",String.class,skillCell);
      skillColumn.setWidthSpecs(100,300,200);
      table.addColumnController(skillColumn);
    }
    // Date column
    {
      CellDataProvider<SkillEvent,Date> completionDateCell=new CellDataProvider<SkillEvent,Date>()
      {
        @Override
        public Date getData(SkillEvent item)
        {
          Long timestamp=item.getDate();
          return (timestamp!=null)?new Date(timestamp.longValue()):null;
        }
      };
      DefaultTableColumnController<SkillEvent,Date> completionDateColumn=new DefaultTableColumnController<SkillEvent,Date>(DATE,"Date",Date.class,completionDateCell);
      completionDateColumn.setWidthSpecs(120,120,120);
      completionDateColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      table.addColumnController(completionDateColumn);
    }
    // Deed name column
    {
      CellDataProvider<SkillEvent,String> deedCell=new CellDataProvider<SkillEvent,String>()
      {
        @Override
        public String getData(SkillEvent item)
        {
          return item.getDeed().getName();
        }
      };
      DefaultTableColumnController<SkillEvent,String> deedColumn=new DefaultTableColumnController<SkillEvent,String>(DEED_NAME,"Deed",String.class,deedCell);
      deedColumn.setWidthSpecs(100,300,200);
      table.addColumnController(deedColumn);
    }

    TableColumnsManager<SkillEvent> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(SKILL);
    columnIds.add(DATE);
    columnIds.add(DEED_NAME);
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<SkillEvent> getTableController()
  {
    return _tableController;
  }

  /**
   * Update display.
   */
  public void update()
  {
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
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _stats=null;
  }
}
