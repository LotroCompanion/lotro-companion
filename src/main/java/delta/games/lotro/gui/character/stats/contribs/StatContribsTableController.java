package delta.games.lotro.gui.character.stats.contribs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.character.stats.contribs.ContribsByStat;
import delta.games.lotro.character.stats.contribs.StatContribution;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;
import delta.games.lotro.gui.utils.l10n.StatRenderer;

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
  // Renderer
  private StatRenderer _renderer;

  /**
   * Constructor.
   */
  public StatContribsTableController()
  {
    _statContribs=new ArrayList<StatContribution>();
    _renderer=new StatRenderer(null);
    _tableController=buildTable();
  }

  /**
   * Set the contributions to display.
   * @param contribs Contributions to display.
   */
  public void setContributions(ContribsByStat contribs)
  {
    _renderer.setStat(contribs.getStat());
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
        @Override
        public String getData(StatContribution item)
        {
          return item.getSource().getLabel();
        }
      };
      DefaultTableColumnController<StatContribution,String> sourceColumn=new DefaultTableColumnController<StatContribution,String>("Source",String.class,sourceCell); // I18n
      sourceColumn.setWidthSpecs(150,-1,150);
      columnsManager.addColumnController(sourceColumn,true);
    }
    // Value column
    {
      CellDataProvider<StatContribution,Number> statCell=new CellDataProvider<StatContribution,Number>()
      {
        @Override
        public Number getData(StatContribution item)
        {
          return item.getValue();
        }
      };
      DefaultTableColumnController<StatContribution,Number> statColumn=new DefaultTableColumnController<StatContribution,Number>("Value",Number.class,statCell); // I18n
      StatColumnsUtils.configureStatValueColumn(statColumn,_renderer,70);
      columnsManager.addColumnController(statColumn,true);
    }
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
