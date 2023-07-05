package delta.games.lotro.gui.lore.trade.barter.form;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.area.AbstractAreaController;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.icons.IconWithText;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.ItemsSummaryPanelController;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.trade.barter.BarterEntry;
import delta.games.lotro.lore.trade.barter.BarterEntryElement;
import delta.games.lotro.lore.trade.barter.ItemBarterEntryElement;
import delta.games.lotro.lore.trade.barter.ReputationBarterEntryElement;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a table that shows barter entries.
 * @author DAM
 */
public class BarterEntriesTableController extends AbstractAreaController
{
  // Data
  private TypedProperties _prefs;
  private List<BarterEntry> _barterEntries;
  // GUI
  private JTable _table;
  private GenericTableController<BarterEntry> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param entries Entries to show.
   */
  public BarterEntriesTableController(AreaController parent, TypedProperties prefs, Filter<BarterEntry> filter, List<BarterEntry> entries)
  {
    super(parent);
    _prefs=prefs;
    _barterEntries=new ArrayList<BarterEntry>(entries);
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<BarterEntry> buildTable()
  {
    ListDataProvider<BarterEntry> provider=new ListDataProvider<BarterEntry>(_barterEntries);
    GenericTableController<BarterEntry> table=new GenericTableController<BarterEntry>(provider);
    List<DefaultTableColumnController<BarterEntry,?>> columns=buildColumns();
    for(DefaultTableColumnController<BarterEntry,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<BarterEntry> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a barter entries table.
   * @return A list of columns for a barter entries table.
   */
  @SuppressWarnings("rawtypes")
  private List<DefaultTableColumnController<BarterEntry,?>> buildColumns()
  {
    List<DefaultTableColumnController<BarterEntry,?>> ret=new ArrayList<DefaultTableColumnController<BarterEntry,?>>();
    // To receive (icon) column
    {
      CellDataProvider<BarterEntry,Icon> iconCell=new CellDataProvider<BarterEntry,Icon>()
      {
        @Override
        public Icon getData(BarterEntry entry)
        {
          BarterEntryElement toReceive=entry.getElementToReceive();
          if (toReceive instanceof ItemBarterEntryElement)
          {
            ItemBarterEntryElement itemEntry=(ItemBarterEntryElement)toReceive;
            Item item=itemEntry.getItem();
            int quantity=itemEntry.getQuantity();
            Icon icon=ItemUiTools.buildItemIcon(item,quantity);
            return icon;
          }
          else if (toReceive instanceof ReputationBarterEntryElement)
          {
            ReputationBarterEntryElement reputationEntry=(ReputationBarterEntryElement)toReceive;
            int amount=reputationEntry.getAmount();
            String iconName=(amount>0)?"reputation":"reputation-decrease";
            Icon icon=IconsManager.getIcon("/resources/gui/icons/"+iconName+".png");
            IconWithText iconWithText=new IconWithText(icon,String.valueOf(amount),Color.WHITE);
            return iconWithText;
          }
          return null;
        }
      };
      DefaultTableColumnController<BarterEntry,Icon> iconColumn=new DefaultTableColumnController<BarterEntry,Icon>(BarterEntriesColumnIds.TO_RECEIVE_ICON.name(),"",Icon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      ret.add(iconColumn);
    }
    // To receive (name) column
    {
      CellDataProvider<BarterEntry,String> nameCell=new CellDataProvider<BarterEntry,String>()
      {
        @Override
        public String getData(BarterEntry entry)
        {
          String name=null;
          BarterEntryElement toReceive=entry.getElementToReceive();
          if (toReceive instanceof ItemBarterEntryElement)
          {
            ItemBarterEntryElement itemEntry=(ItemBarterEntryElement)toReceive;
            name=itemEntry.getItem().getName();
          }
          else if (toReceive instanceof ReputationBarterEntryElement)
          {
            ReputationBarterEntryElement reputationEntry=(ReputationBarterEntryElement)toReceive;
            Faction faction=reputationEntry.getFaction();
            String rawFactionName=faction.getName();
            name=ContextRendering.render(BarterEntriesTableController.this,rawFactionName);
          }
          return name;
        }
      };
      DefaultTableColumnController<BarterEntry,String> nameColumn=new DefaultTableColumnController<BarterEntry,String>(BarterEntriesColumnIds.TO_RECEIVE_NAME.name(),"To receive",String.class,nameCell);
      nameColumn.setWidthSpecs(100,300,200);
      ret.add(nameColumn);
    }
    // To give column
    {
      CellDataProvider<BarterEntry,List> toGiveCell=new CellDataProvider<BarterEntry,List>()
      {
        @Override
        public List getData(BarterEntry entry)
        {
          return BarterUiUtils.getToGiveItems(entry);
        }
      };
      DefaultTableColumnController<BarterEntry,List> toGiveColumn=new DefaultTableColumnController<BarterEntry,List>(BarterEntriesColumnIds.TO_GIVE.name(),"To give",List.class,toGiveCell);
      int size=getBestSizeForToGiveColumn();
      toGiveColumn.setWidthSpecs(size,size,size);
      ItemsSummaryPanelController panelController=new ItemsSummaryPanelController();
      TableCellRenderer renderer=panelController.buildRenderer();
      toGiveColumn.setCellRenderer(renderer);
      toGiveColumn.setSortable(false);
      ret.add(toGiveColumn);
    }
    return ret;
  }

  private int getBestSizeForToGiveColumn()
  {
    int max=0;
    for(BarterEntry entry : _barterEntries)
    {
      int nbToGive=entry.getNumberOfItemsToGive();
      if (nbToGive>max)
      {
        max=nbToGive;
      }
    }
    return 5+(max*(32+5));
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=null;
    if (_prefs!=null)
    {
      columnIds=_prefs.getStringList(ItemChooser.COLUMNS_PROPERTY);
    }
    if (columnIds==null)
    {
      columnIds=new ArrayList<String>();
      columnIds.add(BarterEntriesColumnIds.TO_RECEIVE_ICON.name());
      columnIds.add(BarterEntriesColumnIds.TO_RECEIVE_NAME.name());
      columnIds.add(BarterEntriesColumnIds.TO_GIVE.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<BarterEntry> getTableController()
  {
    return _tableController;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of entries.
   * @return A number of barter entries.
   */
  public int getNbItems()
  {
    return _barterEntries.size();
  }

  /**
   * Get the number of filtered items in the managed table.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_tableController.getNbFilteredItems();
    return ret;
  }

  private void configureTable()
  {
    JTable table=getTable();
    // Adjust table row height for icons (32 pixels)
    table.setRowHeight(32);
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    if (_table==null)
    {
      _table=_tableController.getTable();
    }
    return _table;
  }

  /**
   * Add an action listener.
   * @param al Action listener to add.
   */
  public void addActionListener(ActionListener al)
  {
    _tableController.addActionListener(al);
  }

  /**
   * Remove an action listener.
   * @param al Action listener to remove.
   */
  public void removeActionListener(ActionListener al)
  {
    _tableController.removeActionListener(al);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnIds=_tableController.getColumnsManager().getSelectedColumnsIds();
      _prefs.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
      _prefs=null;
    }
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _barterEntries=null;
  }
}
