package delta.games.lotro.gui.items;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTable;

import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.utils.gui.tables.CellDataProvider;
import delta.games.lotro.utils.gui.tables.DataProvider;
import delta.games.lotro.utils.gui.tables.GenericTableController;
import delta.games.lotro.utils.gui.tables.ListDataProvider;
import delta.games.lotro.utils.gui.tables.TableColumnController;

/**
 * Controller for a table that shows a choice of items.
 * @author DAM
 */
public class ItemChoiceTableController
{
  // Data
  private List<Item> _items;
  // GUI
  private GenericTableController<Item> _tableController;

  /**
   * Constructor.
   * @param items Items to choose from.
   * @param filter Items filter.
   */
  public ItemChoiceTableController(List<Item> items, Filter<Item> filter)
  {
    _items=items;
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  /**
   * Set a new set of items.
   * @param items Items to set.
   */
  public void setItems(List<Item> items)
  {
    _items.clear();
    _items.addAll(items);
    _tableController.refresh();
  }

  private GenericTableController<Item> buildTable()
  {
    DataProvider<Item> provider=new ListDataProvider<Item>(_items);
    GenericTableController<Item> table=new GenericTableController<Item>(provider);

    // Icon column
    {
      CellDataProvider<Item,ImageIcon> iconCell=new CellDataProvider<Item,ImageIcon>()
      {
        public ImageIcon getData(Item item)
        {
          String iconId=item.getProperty(ItemPropertyNames.ICON_ID);
          String backgroundIconId=item.getProperty(ItemPropertyNames.BACKGROUND_ICON_ID);
          ImageIcon icon=IconsManager.getItemIcon(iconId, backgroundIconId);
          return icon;
        }
      };
      TableColumnController<Item,ImageIcon> iconColumn=new TableColumnController<Item,ImageIcon>("Icon",ImageIcon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      table.addColumnController(iconColumn);
    }
    // ID column
    {
      CellDataProvider<Item,Long> idCell=new CellDataProvider<Item,Long>()
      {
        public Long getData(Item item)
        {
          return Long.valueOf(item.getIdentifier());
        }
      };
      TableColumnController<Item,Long> idColumn=new TableColumnController<Item,Long>("ID",Long.class,idCell);
      idColumn.setWidthSpecs(100,100,50);
      table.addColumnController(idColumn);
    }
    // Name column
    {
      CellDataProvider<Item,String> nameCell=new CellDataProvider<Item,String>()
      {
        public String getData(Item item)
        {
          return item.getName();
        }
      };
      TableColumnController<Item,String> nameColumn=new TableColumnController<Item,String>("Name",String.class,nameCell);
      nameColumn.setWidthSpecs(150,-1,150);
      table.addColumnController(nameColumn);
    }
    // Level column
    {
      CellDataProvider<Item,Integer> levelCell=new CellDataProvider<Item,Integer>()
      {
        public Integer getData(Item item)
        {
          return item.getItemLevel();
        }
      };
      TableColumnController<Item,Integer> levelColumn=new TableColumnController<Item,Integer>("Level",Integer.class,levelCell);
      levelColumn.setWidthSpecs(70,70,50);
      table.addColumnController(levelColumn);
    }
    return table;
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
    return _tableController.getTable();
  }

  /**
   * Get the currently selected item.
   * @return An item or <code>null</code> if not found.
   */
  public Item getSelectedItem()
  {
    Item ret=_tableController.getSelectedItem();
    return ret;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of items in the managed table.
   * @return A number of items.
   */
  public int getNbItems()
  {
    return (_items!=null)?_items.size():0;
  }

  /**
   * Get the number of filtered items.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_tableController.getNbFilteredItems();
    return ret;
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
    _items=null;
  }
}
