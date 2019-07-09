package delta.games.lotro.gui.items.legendary.shared;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.Sort;
import delta.games.lotro.common.effects.Effect;

/**
 * Builder for a table that shows passives.
 * @author DAM
 */
public class PassivesTableBuilder
{
  /**
   * Build a table to show passives.
   * @param passives Passives to show.
   * @return A new table controller.
   */
  public static GenericTableController<Effect> buildTable(List<Effect> passives)
  {
    DataProvider<Effect> provider=new ListDataProvider<Effect>(passives);
    GenericTableController<Effect> table=new GenericTableController<Effect>(provider);

    // Label column
    {
      CellDataProvider<Effect,String> labelCell=new CellDataProvider<Effect,String>()
      {
        @Override
        public String getData(Effect item)
        {
          String label=item.getStatsProvider().getLabel();
          return label;
        }
      };
      DefaultTableColumnController<Effect,String> labelColumn=new DefaultTableColumnController<Effect,String>(PassiveColumnIds.LABEL.name(),"Name",String.class,labelCell);
      labelColumn.setWidthSpecs(250,-1,250);
      table.addColumnController(labelColumn);
    }
    // Sort
    String sort=Sort.SORT_ASCENDING+PassiveColumnIds.LABEL;
    table.setSort(Sort.buildFromString(sort));
    // Displayed columns
    List<String> columnsIds=getDefaultColumnIds();
    table.getColumnsManager().setColumns(columnsIds);
    return table;
  }

  private static List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(PassiveColumnIds.LABEL.name());
    return columnsIds;
  }
}
