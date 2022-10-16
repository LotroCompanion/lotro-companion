package delta.games.lotro.gui.character.traitTree.setup.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.classes.traitTree.setup.TraitTreeSetup;
import delta.games.lotro.character.classes.traitTree.setup.TraitTreeSetupsManager;

/**
 * Builder for a table that shows trait tree setups.
 * @author DAM
 */
public class TraitTreeSetupTableBuilder
{
  /**
   * Build a table to show trait tree setups.
   * @param traitTreeKey Trait tree key to use.
   * @return A new table controller.
   */
  public static GenericTableController<TraitTreeSetup> buildTable(final String traitTreeKey)
  {
    // Provider
    TraitTreeSetupsManager setupsMgr=TraitTreeSetupsManager.getInstance();
    List<TraitTreeSetup> setups=setupsMgr.getAll();
    DataProvider<TraitTreeSetup> provider=new ListDataProvider<TraitTreeSetup>(setups);
    GenericTableController<TraitTreeSetup> table=new GenericTableController<TraitTreeSetup>(provider);
    // Filter
    Filter<TraitTreeSetup> filter=new Filter<TraitTreeSetup>()
    {
      @Override
      public boolean accept(TraitTreeSetup setup)
      {
        String setupKey=setup.getKey();
        return (setupKey.equals(traitTreeKey));
      }
    };
    table.setFilter(filter);
    // Columns
    List<DefaultTableColumnController<TraitTreeSetup,?>> columns=initColumns();
    TableColumnsManager<TraitTreeSetup> columnsManager=table.getColumnsManager();
    for(DefaultTableColumnController<TraitTreeSetup,?> column : columns)
    {
      columnsManager.addColumnController(column,false);
    }
    List<String> columnsIds=getDefaultColumnIds();
    columnsManager.setColumns(columnsIds);
    DefaultTableColumnController<TraitTreeSetup,String> deleteColumn=buildDeleteColumn(table);
    columnsManager.addColumnController(deleteColumn,true);
    // Ensure that the Swing table is built (to avoid nasty NPEs)
    table.getTable();
    return table;
  }

  private static List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(TraitTreeSetupColumnIds.NAME.name());
    columnsIds.add(TraitTreeSetupColumnIds.CLASS.name());
    columnsIds.add(TraitTreeSetupColumnIds.MAIN_BRANCH.name());
    columnsIds.add(TraitTreeSetupColumnIds.COST.name());
    return columnsIds;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  private static List<DefaultTableColumnController<TraitTreeSetup,?>> initColumns()
  {
    List<DefaultTableColumnController<TraitTreeSetup,?>> columns=new ArrayList<DefaultTableColumnController<TraitTreeSetup,?>>();
    // Name column
    columns.add(buildNameColumn());
    // Class column
    columns.add(buildClassColumn());
    // Branch column
    columns.add(buildBranchColumn());
    // Cost column
    columns.add(buildCostColumn());
    return columns;
  }

  /**
   * Build a column for the name of a trait tree setup.
   * @return a column.
   */
  private static DefaultTableColumnController<TraitTreeSetup,String> buildNameColumn()
  {
    CellDataProvider<TraitTreeSetup,String> nameCell=new CellDataProvider<TraitTreeSetup,String>()
    {
      @Override
      public String getData(TraitTreeSetup setup)
      {
        return setup.getName();
      }
    };
    DefaultTableColumnController<TraitTreeSetup,String> nameColumn=new DefaultTableColumnController<TraitTreeSetup,String>(TraitTreeSetupColumnIds.NAME.name(),"Name",String.class,nameCell);
    nameColumn.setWidthSpecs(150,-1,150);
    return nameColumn;
  }

  /**
   * Build a column for the cost of a trait tree setup.
   * @return a column.
   */
  private static DefaultTableColumnController<TraitTreeSetup,Integer> buildCostColumn()
  {
    CellDataProvider<TraitTreeSetup,Integer> costCell=new CellDataProvider<TraitTreeSetup,Integer>()
    {
      @Override
      public Integer getData(TraitTreeSetup setup)
      {
        return Integer.valueOf(setup.getStatus().getCost());
      }
    };
    DefaultTableColumnController<TraitTreeSetup,Integer> costColumn=new DefaultTableColumnController<TraitTreeSetup,Integer>(TraitTreeSetupColumnIds.COST.name(),"Cost",Integer.class,costCell);
    costColumn.setWidthSpecs(55,55,50);
    return costColumn;
  }

  /**
   * Build a column for the class of a trait tree setup.
   * @return a column.
   */
  private static DefaultTableColumnController<TraitTreeSetup,String> buildClassColumn()
  {
    CellDataProvider<TraitTreeSetup,String> keyCell=new CellDataProvider<TraitTreeSetup,String>()
    {
      @Override
      public String getData(TraitTreeSetup setup)
      {
        return setup.getKey();
      }
    };
    DefaultTableColumnController<TraitTreeSetup,String> keyColumn=new DefaultTableColumnController<TraitTreeSetup,String>(TraitTreeSetupColumnIds.CLASS.name(),"Class",String.class,keyCell);
    keyColumn.setWidthSpecs(80,100,80);
    return keyColumn;
  }

  /**
   * Build a column for the branch of a trait tree setup.
   * @return a column.
   */
  private static DefaultTableColumnController<TraitTreeSetup,String> buildBranchColumn()
  {
    CellDataProvider<TraitTreeSetup,String> classCell=new CellDataProvider<TraitTreeSetup,String>()
    {
      @Override
      public String getData(TraitTreeSetup setup)
      {
        return setup.getSelectedBranch().getName();
      }
    };
    DefaultTableColumnController<TraitTreeSetup,String> classColumn=new DefaultTableColumnController<TraitTreeSetup,String>(TraitTreeSetupColumnIds.MAIN_BRANCH.name(),"Branch",String.class,classCell);
    classColumn.setWidthSpecs(150,150,150);
    return classColumn;
  }


  /**
   * Build a column with a 'Delete' button.
   * @param table Table to use.
   * @return A column controller.
   */
  private static DefaultTableColumnController<TraitTreeSetup,String> buildDeleteColumn(final GenericTableController<TraitTreeSetup> table)
  {
    DefaultTableColumnController<TraitTreeSetup,String> column=table.buildButtonColumn(TraitTreeSetupColumnIds.DELETE.name(),"Delete",80);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        final TraitTreeSetup source=(TraitTreeSetup)e.getSource();
        // Invoke later to avoid error on click event if table becomes empty
        Runnable r=new Runnable()
        {
          @Override
          public void run()
          {
            TraitTreeSetupsManager setupsMgr=TraitTreeSetupsManager.getInstance();
            setupsMgr.remove(source);
            table.refresh();
          }
        };
        SwingUtilities.invokeLater(r);
      }
    };
    column.setActionListener(al);
    return column;
  }
}
