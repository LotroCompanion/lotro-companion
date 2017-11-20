package delta.games.lotro.gui.character.stats.contribs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.character.stats.contribs.ContribsByStat;
import delta.games.lotro.character.stats.contribs.StatContribution;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for a table that shows the contributions to a stat.
 * @author DAM
 */
public class StatContribsTableController
{
  // Data
  private List<StatContribution> _statContribs;
  // GUI
  private GenericTableController<StatContribution> _tableController;

  /**
   * Constructor.
   */
  public StatContribsTableController()
  {
    _statContribs=new ArrayList<StatContribution>();
    _tableController=buildTable();
   configureTable();
  }

  /**
   * Set the contributions to display.
   * @param contribs Contributions to display.
   */
  public void setContributions(ContribsByStat contribs)
  {
    _statContribs.clear();
    _statContribs.addAll(contribs.getContribs());
    // High values first
    Collections.reverse(_statContribs);
    _tableController.refresh();
  }

  /**
   * Get the managed generic table controller.
   * @return the managed generic table controller.
   */
  public GenericTableController<StatContribution> getTableController()
  {
    return _tableController;
  }

  private GenericTableController<StatContribution> buildTable()
  {
    DataProvider<StatContribution> provider=new ListDataProvider<StatContribution>(_statContribs);
    GenericTableController<StatContribution> table=new GenericTableController<StatContribution>(provider);
    initColumns(table.getColumnsManager());
    return table;
  }

  private void initColumns(TableColumnsManager<StatContribution> columnsManager)
  {
    // Source column
    {
      CellDataProvider<StatContribution,String> sourceCell=new CellDataProvider<StatContribution,String>()
      {
        public String getData(StatContribution item)
        {
          return item.getSource().getLabel();
        }
      };
      TableColumnController<StatContribution,String> sourceColumn=new TableColumnController<StatContribution,String>("Source",String.class,sourceCell);
      sourceColumn.setWidthSpecs(150,-1,150);
      columnsManager.addColumnController(sourceColumn,true);
    }
    // Value column
    {
      CellDataProvider<StatContribution,FixedDecimalsInteger> statCell=new CellDataProvider<StatContribution,FixedDecimalsInteger>()
      {
        public FixedDecimalsInteger getData(StatContribution item)
        {
          return item.getValue();
        }
      };
      TableColumnController<StatContribution,FixedDecimalsInteger> statColumn=new TableColumnController<StatContribution,FixedDecimalsInteger>("Value",FixedDecimalsInteger.class,statCell);
      statColumn.setWidthSpecs(55,55,50);
      columnsManager.addColumnController(statColumn,true);
    }
  }

  private void configureTable()
  {
    //JTable table=getTable();
    // Adjust table row height for icons (32 pixels)
    //table.setRowHeight(32);
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    return _tableController.getTable();
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
    // Data
    _statContribs=null;
  }
}
