package delta.games.lotro.gui.character.status.achievables.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.CellDataUpdater;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.status.achievables.AchievableElementState;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.Progress;
import delta.games.lotro.character.status.achievables.comparators.ProgressComparator;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;

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
    ret.add(buildAchievableStateColumn(false,null));
    // Count
    {
      CellDataProvider<AchievableStatus,Integer> countCell=new CellDataProvider<AchievableStatus,Integer>()
      {
        @Override
        public Integer getData(AchievableStatus status)
        {
          return status.getCompletionCountForDisplay();
        }
      };
      DefaultTableColumnController<AchievableStatus,Integer> countColumn=new DefaultTableColumnController<AchievableStatus,Integer>(AchievableStatusColumnIds.COMPLETION_COUNT.name(),"Count",Integer.class,countCell); // I18n
      countColumn.setWidthSpecs(50,50,50);
      ret.add(countColumn);
    }
    return ret;
  }

  /**
   * Build the columns to show the attributes of a deed AchievableStatus.
   * @param listener Listener for updates.
   * @return a list of columns.
   */
  public static List<TableColumnController<AchievableStatus,?>> buildDeedStateColumns(FilterUpdateListener listener)
  {
    List<TableColumnController<AchievableStatus,?>> ret=new ArrayList<TableColumnController<AchievableStatus,?>>();
    // State
    ret.add(buildAchievableStateColumn(true,listener));
    // Progress
    ret.add(buildAchievableProgressColumn());
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
      DefaultTableColumnController<AchievableStatus,Date> completionDateColumn=new DefaultTableColumnController<AchievableStatus,Date>(AchievableStatusColumnIds.COMPLETION_DATE.name(),"Completion Date",Date.class,completionDateCell); // I18n
      StatColumnsUtils.configureDateTimeColumn(completionDateColumn);
      ret.add(completionDateColumn);
    }
    return ret;
  }

  /**
   * Build a column to show an achievable state.
   * @param editable Indicates if the state column is editable or not.
   * @param listener Listener for updates.
   * @return a column.
   */
  private static TableColumnController<AchievableStatus,?> buildAchievableStateColumn(boolean editable, final FilterUpdateListener listener)
  {
    CellDataProvider<AchievableStatus,AchievableElementState> completedCell=new CellDataProvider<AchievableStatus,AchievableElementState>()
    {
      @Override
      public AchievableElementState getData(AchievableStatus status)
      {
        return status.getState();
      }
    };
    DefaultTableColumnController<AchievableStatus,AchievableElementState> completedColumn=new DefaultTableColumnController<AchievableStatus,AchievableElementState>(AchievableStatusColumnIds.COMPLETED.name(),"Completed",AchievableElementState.class,completedCell); // I18n
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
          if (listener!=null)
          {
            listener.filterUpdated();
          }
        }
      };
      completedColumn.setValueUpdater(updater);
    }
    return completedColumn;
  }


  /**
   * Build a column to show an achievable progress.
   * @return a column.
   */
  private static TableColumnController<AchievableStatus,?> buildAchievableProgressColumn()
  {
    CellDataProvider<AchievableStatus,Progress> progressCell=new CellDataProvider<AchievableStatus,Progress>()
    {
      @Override
      public Progress getData(AchievableStatus status)
      {
        return status.getProgress();
      }
    };
    DefaultTableColumnController<AchievableStatus,Progress> progressColumn=new DefaultTableColumnController<AchievableStatus,Progress>(AchievableStatusColumnIds.PROGRESS.name(),"Progress",Progress.class,progressCell); // I18n
    progressColumn.setWidthSpecs(70,70,70);
    progressColumn.setEditable(false);
    // Renderer
    progressColumn.setCellRenderer(new ProgressTableCellRenderer());
    // Comparator
    progressColumn.setComparator(new ProgressComparator());
    return progressColumn;
  }
}
