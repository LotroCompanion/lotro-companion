package delta.games.lotro.gui.character.stash;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.stash.ItemsStash;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemPropertyNames;

/**
 * Controller for a table that shows all items in a stash.
 * @author DAM
 */
public class StashItemsTableController
{
  // Data
  private CharacterFile _toon;
  // GUI
  private JTable _table;
  private GenericTableController<ItemInstance<? extends Item>> _tableController;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public StashItemsTableController(CharacterFile toon)
  {
    _toon=toon;
    _tableController=buildTable();
    configureTable();
  }

  private DataProvider<ItemInstance<? extends Item>> buildDataProvider()
  {
    DataProvider<ItemInstance<? extends Item>> ret=new DataProvider<ItemInstance<? extends Item>>()
    {
      @Override
      public ItemInstance<? extends Item> getAt(int index)
      {
        ItemsStash stash=_toon.getStash();
        List<ItemInstance<? extends Item>> items=stash.getItemsList();
        ItemInstance<? extends Item> instance=items.get(index);
        return instance;
      }

      @Override
      public int getCount()
      {
        ItemsStash stash=_toon.getStash();
        List<ItemInstance<? extends Item>> items=stash.getItemsList();
        return items.size();
      }
    };
    return ret;
  }

  private GenericTableController<ItemInstance<? extends Item>> buildTable()
  {
    DataProvider<ItemInstance<? extends Item>> provider=buildDataProvider();
    GenericTableController<ItemInstance<? extends Item>> table=new GenericTableController<ItemInstance<? extends Item>>(provider);

    List<TableColumnController<Item,?>> itemColumns=getItemColumns();

    CellDataProvider<ItemInstance<? extends Item>,Item> dataProvider=new CellDataProvider<ItemInstance<? extends Item>,Item>()
    {
      @Override
      public Item getData(ItemInstance<? extends Item> p)
      {
        return p.getReference();
      }
    };

    for(TableColumnController<Item,?> itemColumn : itemColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)itemColumn;
      ProxiedTableColumnController<ItemInstance<? extends Item>,Item,Object> column=new ProxiedTableColumnController<ItemInstance<? extends Item>,Item,Object>(c,dataProvider);
      table.addColumnController(column);
    }
    return table;
  }

  private List<TableColumnController<Item,?>> getItemColumns()
  {
    List<TableColumnController<Item,?>> ret=new ArrayList<TableColumnController<Item,?>>();
    // Icon column
    {
      CellDataProvider<Item,ImageIcon> iconCell=new CellDataProvider<Item,ImageIcon>()
      {
        @Override
        public ImageIcon getData(Item item)
        {
          String iconPath=item.getIcon();
          ImageIcon icon=LotroIconsManager.getItemIcon(iconPath);
          return icon;
        }
      };
      DefaultTableColumnController<Item,ImageIcon> iconColumn=new DefaultTableColumnController<Item,ImageIcon>("Icon",ImageIcon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      ret.add(iconColumn);
    }
    // Name column
    {
      CellDataProvider<Item,String> nameCell=new CellDataProvider<Item,String>()
      {
        @Override
        public String getData(Item item)
        {
          return item.getName();
        }
      };
      DefaultTableColumnController<Item,String> nameColumn=new DefaultTableColumnController<Item,String>("Name",String.class,nameCell);
      nameColumn.setWidthSpecs(200,-1,300);
      ret.add(nameColumn);
    }
    // Location column
    {
      CellDataProvider<Item,EquipmentLocation> locationCell=new CellDataProvider<Item,EquipmentLocation>()
      {
        @Override
        public EquipmentLocation getData(Item item)
        {
          return item.getEquipmentLocation();
        }
      };
      DefaultTableColumnController<Item,EquipmentLocation> locationColumn=new DefaultTableColumnController<Item,EquipmentLocation>("Location",EquipmentLocation.class,locationCell);
      locationColumn.setWidthSpecs(70,70,70);
      ret.add(locationColumn);
    }
    // Comment column
    {
      CellDataProvider<Item,String> commentCell=new CellDataProvider<Item,String>()
      {
        @Override
        public String getData(Item item)
        {
          String property=item.getProperty(ItemPropertyNames.USER_COMMENT);
          return property;
        }
      };
      DefaultTableColumnController<Item,String> commentColumn=new DefaultTableColumnController<Item,String>("Comment",String.class,commentCell);
      commentColumn.setWidthSpecs(200,-1,400);
      ret.add(commentColumn);
    }
    return ret;
  }

  private void configureTable()
  {
    JTable table=getTable();
    // Adjust table row height for icons (32 pixels)
    table.setRowHeight(32);
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<ItemInstance<? extends Item>> getTableController()
  {
    return _tableController;
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
    // GUI
    if (_table!=null)
    {
      _table=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    _toon=null;
  }
}
