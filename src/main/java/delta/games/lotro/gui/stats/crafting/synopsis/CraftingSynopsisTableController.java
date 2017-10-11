package delta.games.lotro.gui.stats.crafting.synopsis;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.crafting.CraftingStatus;
import delta.games.lotro.character.crafting.ProfessionStatus;
import delta.games.lotro.crafting.CraftingLevel;
import delta.games.lotro.crafting.Profession;
import delta.games.lotro.crafting.ProfessionFilter;

/**
 * Controller for a table that shows crafting status for several toons.
 * @author DAM
 */
public class CraftingSynopsisTableController
{
  // Data
  private List<CharacterFile> _toons;
  private List<CraftingSynopsisItem> _rowItems;
  private CraftingSynopsisItemFilter _filter;
  // GUI
  private GenericTableController<CraftingSynopsisItem> _table;

  /**
   * Constructor.
   * @param filter Profession filter.
   */
  public CraftingSynopsisTableController(ProfessionFilter filter)
  {
    _filter=new CraftingSynopsisItemFilter(filter);
    _toons=new ArrayList<CharacterFile>();
    _rowItems=new ArrayList<CraftingSynopsisItem>();
    _table=buildTable();
    configureTable(_table.getTable());
  }

  /**
   * Get the managed toons.
   * @return the managed toons.
   */
  public List<CharacterFile> getToons()
  {
    return _toons;
  }

  private void updateRowItems()
  {
    _rowItems.clear();
    for(CharacterFile toon : _toons)
    {
      CraftingStatus craftingStatus=toon.getCraftingStatus();
      Profession[] professions=craftingStatus.getProfessions();
      for(Profession profession : professions)
      {
        ProfessionStatus professionStatus=craftingStatus.getProfessionStatus(profession,true);
        CraftingSynopsisItem item=new CraftingSynopsisItem(toon,professionStatus);
        _rowItems.add(item);
      }
    }
  }

  private DataProvider<CraftingSynopsisItem> buildDataProvider()
  {
    DataProvider<CraftingSynopsisItem> ret=new ListDataProvider<CraftingSynopsisItem>(_rowItems);
    return ret;
  }

  private GenericTableController<CraftingSynopsisItem> buildTable()
  {
    DataProvider<CraftingSynopsisItem> provider=buildDataProvider();
    GenericTableController<CraftingSynopsisItem> table=new GenericTableController<CraftingSynopsisItem>(provider);
    table.setFilter(_filter);
    // Row header column
    TableColumnController<CraftingSynopsisItem,String> rowHeaderColumn=buildRowHeaderColumn();
    table.addColumnController(rowHeaderColumn);
    // Proficiency
    TableColumnController<CraftingSynopsisItem,CraftingLevel> proficiencyColumn=buildCraftingTierColumn(false);
    table.addColumnController(proficiencyColumn);
    // Proficiency
    TableColumnController<CraftingSynopsisItem,CraftingLevel> masteryColumn=buildCraftingTierColumn(true);
    table.addColumnController(masteryColumn);
    return table;
  }

  private TableCellRenderer buildSimpleCellRenderer(final JPanel panel)
  {
    TableCellRenderer renderer=new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        return panel;
      }
    };
    return renderer;
  }

  private TableColumnController<CraftingSynopsisItem,String> buildRowHeaderColumn()
  {
    CellDataProvider<CraftingSynopsisItem,String> cell=new CellDataProvider<CraftingSynopsisItem,String>()
    {
      public String getData(CraftingSynopsisItem item)
      {
        Profession profession=item.getProfession();
        CharacterFile character=item.getCharacter();
        String name=character.getName();
        return name+" ("+profession+")";
      }
    };
    TableColumnController<CraftingSynopsisItem,String> column=new TableColumnController<CraftingSynopsisItem,String>("Items",String.class,cell);

    // Init panels
    column.setMinWidth(200);
    column.setPreferredWidth(200);

    // Header renderer
    JPanel emptyHeaderPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(emptyHeaderPanel);
    column.setHeaderCellRenderer(headerRenderer);

    return column;
  }

  private TableColumnController<CraftingSynopsisItem,CraftingLevel> buildCraftingTierColumn(final boolean mastery)
  {
    CellDataProvider<CraftingSynopsisItem,CraftingLevel> cell=new CellDataProvider<CraftingSynopsisItem,CraftingLevel>()
    {
      public CraftingLevel getData(CraftingSynopsisItem item)
      {
        return item.getLevel(mastery);
      }
    };
    String columnName=mastery?"Mastery":"Proficiency";
    String id=columnName;
    TableColumnController<CraftingSynopsisItem,CraftingLevel> column=new TableColumnController<CraftingSynopsisItem,CraftingLevel>(id,columnName,CraftingLevel.class,cell);

    // Width
    column.setMinWidth(100);
    column.setPreferredWidth(100);

    // Comparator
    Comparator<CraftingLevel> statsComparator=new Comparator<CraftingLevel>()
    {
      public int compare(CraftingLevel data1, CraftingLevel data2)
      {
        return Integer.compare(data1.getTier(),data2.getTier());
      }
    };
    column.setComparator(statsComparator);
    return column;
  }

  /**
   * Set the displayed toons.
   * @param toons Toons to display.
   */
  public void setToons(List<CharacterFile> toons)
  {
    _toons.clear();
    _toons.addAll(toons);
    updateRowItems();
    _table.refresh();
  }

  /**
   * Update data for a toon.
   * @param toon Targeted toon.
   */
  public void updateToon(CharacterFile toon)
  {
    updateRowItems();
    _table.refresh();
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    return _table.getTable();
  }

  private void configureTable(final JTable statsTable)
  {
    statsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    statsTable.setShowGrid(false);
    statsTable.getTableHeader().setReorderingAllowed(false);
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _table.filterUpdated();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_table!=null)
    {
      _table.dispose();
      _table=null;
    }
    // Data
    _toons=null;
    _filter=null;
  }
}
