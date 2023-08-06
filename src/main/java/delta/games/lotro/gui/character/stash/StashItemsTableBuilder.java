package delta.games.lotro.gui.character.stash;

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
public class StashItemsTableBuilder
{
  private static DataProvider<ItemInstance<? extends Item>> buildDataProvider(final CharacterFile toon)
  {
    DataProvider<ItemInstance<? extends Item>> ret=new DataProvider<ItemInstance<? extends Item>>()
    {
      @Override
      public ItemInstance<? extends Item> getAt(int index)
      {
        ItemsStash stash=toon.getStash();
        List<ItemInstance<? extends Item>> items=stash.getItemsList();
        ItemInstance<? extends Item> instance=items.get(index);
        return instance;
      }

      @Override
      public int getCount()
      {
        ItemsStash stash=toon.getStash();
        List<ItemInstance<? extends Item>> items=stash.getItemsList();
        return items.size();
      }
    };
    return ret;
  }

  /**
   * Build a table to show the item instances of a toon stash.
   * @param toon Targeted toon.
   * @return A table to show the item instances in the stash of the given toon.
   */
  public static GenericTableController<ItemInstance<? extends Item>> buildTable(CharacterFile toon)
  {
    DataProvider<ItemInstance<? extends Item>> provider=buildDataProvider(toon);
    return buildTable(provider);
  }

  private static GenericTableController<ItemInstance<? extends Item>> buildTable(DataProvider<ItemInstance<? extends Item>> provider)
  {
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
    // Comments
    table.addColumnController(buildCommentsColumn());

    // Configure table
    JTable swingTable=table.getTable();
    // Adjust table row height for icons (32 pixels)
    swingTable.setRowHeight(32);
    return table;
  }

  private static List<TableColumnController<Item,?>> getItemColumns()
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
      DefaultTableColumnController<Item,ImageIcon> iconColumn=new DefaultTableColumnController<Item,ImageIcon>("Icon",ImageIcon.class,iconCell); // I18n
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
      DefaultTableColumnController<Item,String> nameColumn=new DefaultTableColumnController<Item,String>("Name",String.class,nameCell); // I18n
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
      DefaultTableColumnController<Item,EquipmentLocation> locationColumn=new DefaultTableColumnController<Item,EquipmentLocation>("Location",EquipmentLocation.class,locationCell); // I18n
      locationColumn.setWidthSpecs(70,70,70);
      ret.add(locationColumn);
    }
    return ret;
  }

  private static TableColumnController<ItemInstance<? extends Item>,String> buildCommentsColumn()
  {
    CellDataProvider<ItemInstance<? extends Item>,String> commentCell=new CellDataProvider<ItemInstance<? extends Item>,String>()
    {
      @Override
      public String getData(ItemInstance<? extends Item> item)
      {
        String property=item.getProperty(ItemPropertyNames.USER_COMMENT);
        return property;
      }
    };
    DefaultTableColumnController<ItemInstance<? extends Item>,String> commentColumn=new DefaultTableColumnController<ItemInstance<? extends Item>,String>("Comment",String.class,commentCell); // I18n
    commentColumn.setWidthSpecs(200,-1,400);
    return commentColumn;
  }
}
