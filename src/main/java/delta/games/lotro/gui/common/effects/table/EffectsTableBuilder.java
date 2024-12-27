package delta.games.lotro.gui.common.effects.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.effects.EffectUiTools;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Builder for a table that shows effects.
 * @author DAM
 */
public class EffectsTableBuilder
{
  /**
   * Build a table to show effects.
   * @param effects Items to show.
   * @return A new table controller.
   */
  public static GenericTableController<Effect> buildTable(List<? extends Effect> effects)
  {
    DataProvider<Effect> provider=new ListDataProvider<Effect>(effects);
    GenericTableController<Effect> table=new GenericTableController<Effect>(provider);
    List<TableColumnController<Effect,?>> columns=initColumns();
    TableColumnsManager<Effect> columnsManager=table.getColumnsManager();
    for(TableColumnController<Effect,?> column : columns)
    {
      columnsManager.addColumnController(column,false);
    }
    // Adjust table row height for icons (32 pixels)
    JTable swingTable=table.getTable();
    swingTable.setRowHeight(32);
    return table;
  }

  /**
   * Add a details column on the given table.
   * @param parent Parent window.
   * @param table Table to use.
   * @return A column controller.
   */
  public static DefaultTableColumnController<Effect,String> addDetailsColumn(final WindowController parent, GenericTableController<Effect> table)
  {
    DefaultTableColumnController<Effect,String> column=table.buildButtonColumn(EffectColumnIds.DETAILS.name(),"Details...",90);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Effect source=(Effect)e.getSource();
        EffectUiTools.showEffectForm(parent,source);
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
  public static List<TableColumnController<Effect,?>> initColumns()
  {
    List<TableColumnController<Effect,?>> columns=new ArrayList<TableColumnController<Effect,?>>();
    // Icon column
    columns.add(buildIconColumn());
    // ID column
    if (UiConfiguration.showTechnicalColumns())
    {
      columns.add(buildIdColumn());
    }
    // Name column
    columns.add(buildNameColumn());
    return columns;
  }

  /**
   * Build a column for the icon of an effect.
   * @return a column.
   */
  public static DefaultTableColumnController<Effect,Icon> buildIconColumn()
  {
    CellDataProvider<Effect,Icon> iconCell=new CellDataProvider<Effect,Icon>()
    {
      @Override
      public Icon getData(Effect effect)
      {
        Icon icon=LotroIconsManager.getEffectIcon(effect.getIdentifier());
        return icon;
      }
    };
    String columnName=Labels.getLabel("effects.table.icon");
    DefaultTableColumnController<Effect,Icon> iconColumn=new DefaultTableColumnController<Effect,Icon>(EffectColumnIds.ICON.name(),columnName,Icon.class,iconCell);
    iconColumn.setWidthSpecs(50,50,50);
    iconColumn.setSortable(false);
    return iconColumn;
  }

  /**
   * Build a column for the identifier of an effect.
   * @return a column.
   */
  public static DefaultTableColumnController<Effect,Integer> buildIdColumn()
  {
    CellDataProvider<Effect,Integer> idCell=new CellDataProvider<Effect,Integer>()
    {
      @Override
      public Integer getData(Effect effect)
      {
        return Integer.valueOf(effect.getIdentifier());
      }
    };
    String columnName=Labels.getLabel("effects.table.id");
    DefaultTableColumnController<Effect,Integer> idColumn=new DefaultTableColumnController<Effect,Integer>(EffectColumnIds.ID.name(),columnName,Integer.class,idCell);
    idColumn.setWidthSpecs(90,90,50);
    return idColumn;
  }

  /**
   * Build a column for the name of an effect.
   * @return a column.
   */
  public static DefaultTableColumnController<Effect,String> buildNameColumn()
  {
    String columnName=Labels.getLabel("effects.table.name");
    return buildNameColumn(EffectColumnIds.NAME.name(),columnName);
  }

  /**
   * Build a column for the name of an effect.
   * @param id Column identifier.
   * @param label Column label.
   * @return a column.
   */
  public static DefaultTableColumnController<Effect,String> buildNameColumn(String id, String label)
  {
    CellDataProvider<Effect,String> nameCell=new CellDataProvider<Effect,String>()
    {
      @Override
      public String getData(Effect item)
      {
        return item.getName();
      }
    };
    DefaultTableColumnController<Effect,String> nameColumn=new DefaultTableColumnController<Effect,String>(id,label,String.class,nameCell);
    nameColumn.setWidthSpecs(150,-1,150);
    return nameColumn;
  }
}
