package delta.games.lotro.gui.stats.titles.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.titles.TitleStatus;

/**
 * Builds column definitions for TitleStatus data.
 * @author DAM
 */
public class TitleStatusColumnsBuilder
{
  /**
   * Build the columns to show the attributes of a TitleStatus.
   * @return a list of columns.
   */
  public static List<TableColumnController<TitleStatus,?>> buildTitleStatusColumns()
  {
    List<TableColumnController<TitleStatus,?>> ret=new ArrayList<TableColumnController<TitleStatus,?>>();
    // State
    ret.add(buildTitleStateColumn());
    // Acquisition time column
    {
      CellDataProvider<TitleStatus,Double> completionDateCell=new CellDataProvider<TitleStatus,Double>()
      {
        @Override
        public Double getData(TitleStatus status)
        {
          Double timestamp=status.getAcquisitionTimeStamp();
          return timestamp;
        }
      };
      DefaultTableColumnController<TitleStatus,Double> completionDateColumn=new DefaultTableColumnController<TitleStatus,Double>(TitleStatusColumnIds.ACQUISITION_TIMESTAMP.name(),"Acquisition Timestamp",Double.class,completionDateCell);
      completionDateColumn.setWidthSpecs(120,120,120);
      //completionDateColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      ret.add(completionDateColumn);
    }
    return ret;
  }

  /**
   * Build a column to show a title state.
   * @return a column.
   */
  private static TableColumnController<TitleStatus,?> buildTitleStateColumn()
  {
    CellDataProvider<TitleStatus,Boolean> stateCell=new CellDataProvider<TitleStatus,Boolean>()
    {
      @Override
      public Boolean getData(TitleStatus status)
      {
        return Boolean.valueOf(status.isAcquired());
      }
    };
    DefaultTableColumnController<TitleStatus,Boolean> stateColumn=new DefaultTableColumnController<TitleStatus,Boolean>(TitleStatusColumnIds.ACQUIRED.name(),"Acquired",Boolean.class,stateCell);
    stateColumn.setWidthSpecs(30,30,30);
    // Renderer
    //completedColumn.setCellRenderer(new AchievableElementStateTableCellRenderer());
    return stateColumn;
  }
}
