package delta.games.lotro.gui.character.stash;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTable;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.ItemsStash;
import delta.games.lotro.gui.items.ItemConstants;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.utils.gui.tables.CellDataProvider;
import delta.games.lotro.utils.gui.tables.DataProvider;
import delta.games.lotro.utils.gui.tables.GenericTableController;
import delta.games.lotro.utils.gui.tables.TableColumnController;

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
  private GenericTableController<Item> _tableController;

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

  private DataProvider<Item> buildDataProvider()
  {
    DataProvider<Item> ret=new DataProvider<Item>()
    {
      public Item getAt(int index)
      {
        ItemsStash stash=_toon.getStash();
        List<Item> items=stash.getItemsList();
        return items.get(index);
      }

      public int getCount()
      {
        ItemsStash stash=_toon.getStash();
        List<Item> items=stash.getItemsList();
        return items.size();
      }
    };
    return ret;
  }

  private GenericTableController<Item> buildTable()
  {
    DataProvider<Item> provider=buildDataProvider();
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
      nameColumn.setWidthSpecs(200,-1,300);
      table.addColumnController(nameColumn);
    }
    // Location column
    {
      CellDataProvider<Item,EquipmentLocation> locationCell=new CellDataProvider<Item,EquipmentLocation>()
      {
        public EquipmentLocation getData(Item item)
        {
          return item.getEquipmentLocation();
        }
      };
      TableColumnController<Item,EquipmentLocation> locationColumn=new TableColumnController<Item,EquipmentLocation>("Location",EquipmentLocation.class,locationCell);
      locationColumn.setWidthSpecs(70,70,70);
      table.addColumnController(locationColumn);
    }
    // Comment column
    {
      CellDataProvider<Item,String> commentCell=new CellDataProvider<Item,String>()
      {
        public String getData(Item item)
        {
          String property=item.getProperty(ItemConstants.USER_COMMENT);
          return property;
        }
      };
      TableColumnController<Item,String> commentColumn=new TableColumnController<Item,String>("Comment",String.class,commentCell);
      commentColumn.setWidthSpecs(200,-1,400);
      table.addColumnController(commentColumn);
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
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<Item> getTableController()
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
