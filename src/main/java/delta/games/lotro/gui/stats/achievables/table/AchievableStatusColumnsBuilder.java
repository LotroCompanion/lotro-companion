package delta.games.lotro.gui.stats.achievables.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.CellDataUpdater;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.utils.Formats;

/**
 * Builds column definitions for AchievableStatus data.
 * @author DAM
 */
public class AchievableStatusColumnsBuilder
{
  /**
   * Build the columns to show the attributes of a quest AchievableStatus.
   * @return a list of columns.
   */
  public static List<TableColumnController<AchievableStatus,?>> buildQuestStateColumns()
  {
    List<TableColumnController<AchievableStatus,?>> ret=new ArrayList<TableColumnController<AchievableStatus,?>>();
    // State
    ret.add(buildAchievableStateColumn(false));
    {
      CellDataProvider<AchievableStatus,Integer> countCell=new CellDataProvider<AchievableStatus,Integer>()
      {
        @Override
        public Integer getData(AchievableStatus status)
        {
          return status.getCompletionCount();
        }
      };
      DefaultTableColumnController<AchievableStatus,Integer> countColumn=new DefaultTableColumnController<AchievableStatus,Integer>(AchievableStatusColumnIds.COMPLETION_COUNT.name(),"Count",Integer.class,countCell);
      countColumn.setWidthSpecs(50,50,50);
      ret.add(countColumn);
    }
    return ret;
  }

  /**
   * Build the columns to show the attributes of a deed AchievableStatus.
   * @return a list of columns.
   */
  public static List<TableColumnController<AchievableStatus,?>> buildDeedStateColumns()
  {
    List<TableColumnController<AchievableStatus,?>> ret=new ArrayList<TableColumnController<AchievableStatus,?>>();
    // State
    ret.add(buildAchievableStateColumn(true));
    // Completion date column
    {
      CellDataProvider<AchievableStatus,Date> completionDateCell=new CellDataProvider<AchievableStatus,Date>()
      {
        @Override
        public Date getData(AchievableStatus status)
        {
          Long timestamp=status.getCompletionDate();
          return (timestamp!=null)?new Date(timestamp.longValue()):null;
        }
      };
      DefaultTableColumnController<AchievableStatus,Date> completionDateColumn=new DefaultTableColumnController<AchievableStatus,Date>(AchievableStatusColumnIds.COMPLETION_DATE.name(),"Completion Date",Date.class,completionDateCell);
      completionDateColumn.setWidthSpecs(120,120,120);
      completionDateColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      ret.add(completionDateColumn);
    }
    return ret;
  }

  /**
   * Build a column to show an achievable state.
   * @param editable Indicates if the state column is editable or not.
   * @return a column.
   */
  private static TableColumnController<AchievableStatus,?> buildAchievableStateColumn(boolean editable)
  {
    CellDataProvider<AchievableStatus,AchievableElementState> completedCell=new CellDataProvider<AchievableStatus,AchievableElementState>()
    {
      @Override
      public AchievableElementState getData(AchievableStatus status)
      {
        return status.getState();
      }
    };
    DefaultTableColumnController<AchievableStatus,AchievableElementState> completedColumn=new DefaultTableColumnController<AchievableStatus,AchievableElementState>(AchievableStatusColumnIds.COMPLETED.name(),"Completed",AchievableElementState.class,completedCell);
    completedColumn.setWidthSpecs(30,30,30);
    completedColumn.setEditable(editable);
    // Renderer
    completedColumn.setCellRenderer(new AchievableElementStateTableCellRenderer());
    if (editable)
    {
      // Editor
      completedColumn.setCellEditor(new AchievableElementStateTableCellEditor());
      // Updater
      CellDataUpdater<AchievableStatus> updater=new CellDataUpdater<AchievableStatus>()
      {
        @Override
        public void setData(AchievableStatus status, Object value)
        {
          status.setState((AchievableElementState)value);
        }
      };
      completedColumn.setValueUpdater(updater);
    }
    return completedColumn;
  }
}
