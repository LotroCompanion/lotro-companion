package delta.games.lotro.gui.common.effects.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.effects.EffectInstance;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.gui.common.effects.EffectUiTools;

/**
 * Builder for a table that shows effect instances.
 * @author DAM
 */
public class EffectInstancesTableBuilder
{
  /**
   * Build a table to show effect instances.
   * @param effectInstances Effect instances to show.
   * @return A new table controller.
   */
  public static GenericTableController<EffectInstance> buildTable(List<EffectInstance> effectInstances)
  {
    DataProvider<EffectInstance> provider=new ListDataProvider<EffectInstance>(effectInstances);
    GenericTableController<EffectInstance> table=new GenericTableController<EffectInstance>(provider);
    List<TableColumnController<EffectInstance,?>> columns=initColumns();

    TableColumnsManager<EffectInstance> columnsManager=table.getColumnsManager();
    for(TableColumnController<EffectInstance,?> column : columns)
    {
      columnsManager.addColumnController(column,false);
    }
    List<String> columnsIds=getDefaultColumnIds();
    columnsManager.setColumns(columnsIds);
    // Adjust table row height for icons (32 pixels)
    JTable swingTable=table.getTable();
    swingTable.setRowHeight(32);
    return table;
  }

  /**
   * Get the default columns IDs for a table of effect instances.
   * @return a list of column IDs.
   */
  public static List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(EffectColumnIds.ICON.name());
    columnsIds.add(EffectColumnIds.NAME.name());
    columnsIds.add(EffectInstanceColumnIds.SPELLCRAFT.name());
    return columnsIds;
  }

  /**
   * Add a details column on the given table.
   * @param parent Parent window.
   * @param table Table to use.
   * @return A column controller.
   */
  public static DefaultTableColumnController<EffectInstance,String> addDetailsColumn(final WindowController parent, GenericTableController<EffectInstance> table)
  {
    DefaultTableColumnController<EffectInstance,String> column=table.buildButtonColumn(EffectColumnIds.DETAILS.name(),"Details...",90);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        EffectInstance source=(EffectInstance)e.getSource();
        EffectUiTools.showEffectInstanceWindow(parent,source);
      }
    };
    column.setActionListener(al);
    table.addColumnController(column);
    table.updateColumns();
    return column;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  public static List<TableColumnController<EffectInstance,?>> initColumns()
  {
    List<TableColumnController<EffectInstance,?>> columns=new ArrayList<TableColumnController<EffectInstance,?>>();
    columns.addAll(initInstanceColumns());

    CellDataProvider<EffectInstance,Effect> dataProvider=new CellDataProvider<EffectInstance,Effect>()
    {
      @Override
      public Effect getData(EffectInstance effectInstance)
      {
        return effectInstance.getEffect();
      }
    };

    List<TableColumnController<Effect,?>> effectColumns=EffectsTableBuilder.initColumns();
    for(TableColumnController<Effect,?> effectColumn : effectColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<Effect,Object> c=(TableColumnController<Effect,Object>)effectColumn;
      ProxiedTableColumnController<EffectInstance,Effect,Object> column=new ProxiedTableColumnController<EffectInstance,Effect,Object>(c,dataProvider);
      columns.add(column);
    }
    return columns;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  public static List<DefaultTableColumnController<EffectInstance,?>> initInstanceColumns()
  {
    List<DefaultTableColumnController<EffectInstance,?>> columns=new ArrayList<DefaultTableColumnController<EffectInstance,?>>();

    // Spellcraft column
    {
      CellDataProvider<EffectInstance,Float> cell=new CellDataProvider<EffectInstance,Float>()
      {
        @Override
        public Float getData(EffectInstance effectInstance)
        {
          return effectInstance.getSpellcraft();
        }
      };
      DefaultTableColumnController<EffectInstance,Float> column=new DefaultTableColumnController<EffectInstance,Float>(EffectInstanceColumnIds.SPELLCRAFT.name(),"Level",Float.class,cell);
      column.setWidthSpecs(55,55,50);
      columns.add(column);
    }
    return columns;
  }
}
