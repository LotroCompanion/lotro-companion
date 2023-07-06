package delta.games.lotro.gui.character.status.titles.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.status.titles.TitleState;
import delta.games.lotro.character.status.titles.TitleStatus;

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
    /*
    {
      CellDataProvider<TitleStatus,Double> timestampCell=new CellDataProvider<TitleStatus,Double>()
      {
        @Override
        public Double getData(TitleStatus status)
        {
          Double timestamp=status.getAcquisitionTimeStamp();
          return timestamp;
        }
      };
      DefaultTableColumnController<TitleStatus,Double> timestampColumn=new DefaultTableColumnController<TitleStatus,Double>(TitleStatusColumnIds.ACQUISITION_TIMESTAMP.name(),"Acquisition Timestamp",Double.class,timestampCell);
      timestampColumn.setWidthSpecs(120,120,120);
      ret.add(timestampColumn);
    }
    // Acquisition date column
    {
      CellDataProvider<TitleStatus,Date> completionDateCell=new CellDataProvider<TitleStatus,Date>()
      {
        @Override
        public Date getData(TitleStatus status)
        {
          Long acquisitionTime=status.getAcquisitionDate();
          return (acquisitionTime!=null)?new Date(acquisitionTime.longValue()):null;
        }
      };
      DefaultTableColumnController<TitleStatus,Date> completionDateColumn=new DefaultTableColumnController<TitleStatus,Date>(TitleStatusColumnIds.ACQUISITION_DATE.name(),"Date",Date.class,completionDateCell);
      completionDateColumn.setWidthSpecs(120,120,120);
      completionDateColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      ret.add(completionDateColumn);
    }
    */
    // Order column
    CellDataProvider<TitleStatus,Integer> orderCell=new CellDataProvider<TitleStatus,Integer>()
    {
      @Override
      public Integer getData(TitleStatus status)
      {
        return status.getAcquisitionOrder();
      }
    };
    DefaultTableColumnController<TitleStatus,Integer> orderColumn=new DefaultTableColumnController<TitleStatus,Integer>(TitleStatusColumnIds.ORDER.name(),"#",Integer.class,orderCell);
    orderColumn.setWidthSpecs(40,40,40);
    ret.add(orderColumn);
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
        TitleState state=status.getState();
        if ((state==TitleState.ACQUIRED) || (state==TitleState.SUPERSEDED))
        {
          return Boolean.TRUE;
        }
        return Boolean.FALSE;
      }
    };
    DefaultTableColumnController<TitleStatus,Boolean> stateColumn=new DefaultTableColumnController<TitleStatus,Boolean>(TitleStatusColumnIds.ACQUIRED.name(),"Acquired",Boolean.class,stateCell); // I18n
    stateColumn.setWidthSpecs(30,30,30);
    return stateColumn;
  }
}
