package delta.games.lotro.gui.lore.items.chooser;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.NumericTools;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.money.comparator.MoneyComparator;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsRegistry;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.gui.lore.items.ItemColumnIds;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemBinding;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.filters.EssenceTierFilter;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Builder for a table that shows items.
 * @author DAM
 */
public class ItemsTableBuilder
{
  /**
   * Build a table to show items.
   * @param items Items to show.
   * @return A new table controller.
   */
  public static GenericTableController<Item> buildTable(List<Item> items)
  {
    DataProvider<Item> provider=new ListDataProvider<Item>(items);
    GenericTableController<Item> table=new GenericTableController<Item>(provider);
    List<DefaultTableColumnController<Item,?>> columns=initColumns();
    TableColumnsManager<Item> columnsManager=table.getColumnsManager();
    for(DefaultTableColumnController<Item,?> column : columns)
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

  private static List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(ItemColumnIds.ICON.name());
    columnsIds.add(ItemColumnIds.ID.name());
    columnsIds.add(ItemColumnIds.NAME.name());
    return columnsIds;
  }

  /**
   * Add a details column on the given table.
   * @param parent Parent window.
   * @param table Table to use.
   * @return A column controller.
   */
  public static DefaultTableColumnController<Item,String> addDetailsColumn(final WindowController parent, GenericTableController<Item> table)
  {
    DefaultTableColumnController<Item,String> column=table.buildButtonColumn(ItemColumnIds.DETAILS.name(),"Details...",90);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Item source=(Item)e.getSource();
        ItemUiTools.showItemForm(parent,source);
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
  public static List<DefaultTableColumnController<Item,?>> initColumns()
  {
    List<DefaultTableColumnController<Item,?>> columns=new ArrayList<DefaultTableColumnController<Item,?>>();
    // Icon column
    columns.add(buildIconColumn());
    // ID column
    columns.add(buildIdColumn());
    // Name column
    columns.add(buildNameColumn());
    // Item level column
    columns.add(buildItemLevelColumn());
    // Required level column
    {
      CellDataProvider<Item,Integer> minLevelCell=new CellDataProvider<Item,Integer>()
      {
        @Override
        public Integer getData(Item item)
        {
          return item.getMinLevel();
        }
      };
      DefaultTableColumnController<Item,Integer> minLevelColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.REQUIRED_LEVEL.name(),"Level",Integer.class,minLevelCell);
      minLevelColumn.setWidthSpecs(55,55,50);
      columns.add(minLevelColumn);
    }
    // Class requirement
    {
      CellDataProvider<Item,CharacterClass> requiredClassCell=new CellDataProvider<Item,CharacterClass>()
      {
        @Override
        public CharacterClass getData(Item item)
        {
          return item.getRequiredClass();
        }
      };
      DefaultTableColumnController<Item,CharacterClass> requiredClassColumn=new DefaultTableColumnController<Item,CharacterClass>(ItemColumnIds.CLASS.name(),"Class",CharacterClass.class,requiredClassCell);
      requiredClassColumn.setWidthSpecs(100,100,100);
      columns.add(requiredClassColumn);
    }
    // Value
    columns.add(buildValueColumn());
    // Slot count
    {
      CellDataProvider<Item,Integer> slotsCell=new CellDataProvider<Item,Integer>()
      {
        @Override
        public Integer getData(Item item)
        {
          int nbSlots=item.getEssenceSlots();
          return (nbSlots>0)?Integer.valueOf(nbSlots):null;
        }
      };
      DefaultTableColumnController<Item,Integer> slotsColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.SLOT_COUNT.name(),"Slots",Integer.class,slotsCell);
      slotsColumn.setWidthSpecs(55,55,50);
      columns.add(slotsColumn);
    }
    // Essence tier
    {
      CellDataProvider<Item,Integer> tierCell=new CellDataProvider<Item,Integer>()
      {
        @Override
        public Integer getData(Item item)
        {
          String category=item.getSubCategory();
          if ((category!=null) && (category.startsWith(EssenceTierFilter.ESSENCE_TIER_SEED)))
          {
            return NumericTools.parseInteger(category.substring(EssenceTierFilter.ESSENCE_TIER_SEED.length()));
          }
          return null;
        }
      };
      DefaultTableColumnController<Item,Integer> tierColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.TIER.name(),"Tier",Integer.class,tierCell);
      tierColumn.setWidthSpecs(55,55,50);
      columns.add(tierColumn);
    }
    // Quality
    {
      CellDataProvider<Item,ItemQuality> qualityCell=new CellDataProvider<Item,ItemQuality>()
      {
        @Override
        public ItemQuality getData(Item item)
        {
          return item.getQuality();
        }
      };
      DefaultTableColumnController<Item,ItemQuality> qualityColumn=new DefaultTableColumnController<Item,ItemQuality>(ItemColumnIds.QUALITY.name(),"Quality",ItemQuality.class,qualityCell);
      qualityColumn.setWidthSpecs(100,100,100);
      columns.add(qualityColumn);
    }
    // Armour type
    {
      CellDataProvider<Item,ArmourType> armourTypeCell=new CellDataProvider<Item,ArmourType>()
      {
        @Override
        public ArmourType getData(Item item)
        {
          if (item instanceof Armour)
          {
            Armour armour=(Armour)item;
            return armour.getArmourType();
          }
          return null;
        }
      };
      DefaultTableColumnController<Item,ArmourType> armourTypeColumn=new DefaultTableColumnController<Item,ArmourType>(ItemColumnIds.ARMOUR_TYPE.name(),"Armour type",ArmourType.class,armourTypeCell);
      armourTypeColumn.setWidthSpecs(100,100,100);
      columns.add(armourTypeColumn);
    }
    // Weapon type
    {
      CellDataProvider<Item,WeaponType> weaponTypeCell=new CellDataProvider<Item,WeaponType>()
      {
        @Override
        public WeaponType getData(Item item)
        {
          if (item instanceof Weapon)
          {
            Weapon weapon=(Weapon)item;
            return weapon.getWeaponType();
          }
          return null;
        }
      };
      DefaultTableColumnController<Item,WeaponType> weaponTypeColumn=new DefaultTableColumnController<Item,WeaponType>(ItemColumnIds.WEAPON_TYPE.name(),"Weapon type",WeaponType.class,weaponTypeCell);
      weaponTypeColumn.setWidthSpecs(150,150,150);
      columns.add(weaponTypeColumn);
    }
    // Binding
    {
      CellDataProvider<Item,ItemBinding> bindingCell=new CellDataProvider<Item,ItemBinding>()
      {
        @Override
        public ItemBinding getData(Item item)
        {
          return item.getBinding();
        }
      };
      DefaultTableColumnController<Item,ItemBinding> bindingColumn=new DefaultTableColumnController<Item,ItemBinding>(ItemColumnIds.BINDING.name(),"Binding",ItemBinding.class,bindingCell);
      bindingColumn.setWidthSpecs(150,150,150);
      columns.add(bindingColumn);
    }
    // Stat columns
    StatsRegistry registry=StatsRegistry.getInstance();
    for(StatDescription stat : registry.getAll())
    {
      if (stat.isPremium())
      {
        columns.add(buildStatColumn(stat));
      }
    }
    return columns;
  }

  /**
   * Build a column for the icon of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,Icon> buildIconColumn()
  {
    CellDataProvider<Item,Icon> iconCell=new CellDataProvider<Item,Icon>()
    {
      @Override
      public Icon getData(Item item)
      {
        Icon icon=ItemUiTools.buildItemIcon(item);
        return icon;
      }
    };
    DefaultTableColumnController<Item,Icon> iconColumn=new DefaultTableColumnController<Item,Icon>(ItemColumnIds.ICON.name(),"Icon",Icon.class,iconCell);
    iconColumn.setWidthSpecs(50,50,50);
    iconColumn.setSortable(false);
    return iconColumn;
  }

  /**
   * Build a column for the identifier of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,Integer> buildIdColumn()
  {
    CellDataProvider<Item,Integer> idCell=new CellDataProvider<Item,Integer>()
    {
      @Override
      public Integer getData(Item item)
      {
        return Integer.valueOf(item.getIdentifier());
      }
    };
    DefaultTableColumnController<Item,Integer> idColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.ID.name(),"ID",Integer.class,idCell);
    idColumn.setWidthSpecs(90,90,50);
    return idColumn;
  }

  /**
   * Build a column for the name of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,String> buildNameColumn()
  {
    return buildNameColumn(ItemColumnIds.NAME.name(),"Name");
  }

  /**
   * Build a column for the name of an item.
   * @param id Column identifier.
   * @param label Column label.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,String> buildNameColumn(String id, String label)
  {
    CellDataProvider<Item,String> nameCell=new CellDataProvider<Item,String>()
    {
      @Override
      public String getData(Item item)
      {
        return item.getName();
      }
    };
    DefaultTableColumnController<Item,String> nameColumn=new DefaultTableColumnController<Item,String>(id,label,String.class,nameCell);
    nameColumn.setWidthSpecs(150,-1,150);
    return nameColumn;
  }

  /**
   * Build a column for the item level of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,Integer> buildItemLevelColumn()
  {
    CellDataProvider<Item,Integer> levelCell=new CellDataProvider<Item,Integer>()
    {
      @Override
      public Integer getData(Item item)
      {
        return item.getItemLevel();
      }
    };
    DefaultTableColumnController<Item,Integer> levelColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.ITEM_LEVEL.name(),"Item Lvl",Integer.class,levelCell);
    levelColumn.setWidthSpecs(55,55,50);
    return levelColumn;
  }

  /**
   * Build a column for the category of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,String> buildSubCategoryColumn()
  {
    CellDataProvider<Item,String> categoryCell=new CellDataProvider<Item,String>()
    {
      @Override
      public String getData(Item item)
      {
        String category=item.getSubCategory();
        return category;
      }
    };
    DefaultTableColumnController<Item,String> categoryColumn=new DefaultTableColumnController<Item,String>(ItemColumnIds.CATEGORY.name(),"Category",String.class,categoryCell);
    categoryColumn.setWidthSpecs(150,150,150);
    return categoryColumn;
  }

  /**
   * Build a column for the value of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,Money> buildValueColumn()
  {
    CellDataProvider<Item,Money> valueCell=new CellDataProvider<Item,Money>()
    {
      @Override
      public Money getData(Item item)
      {
        Money money=item.getValueAsMoney();
        return money;
      }
    };
    DefaultTableColumnController<Item,Money> valueColumn=new DefaultTableColumnController<Item,Money>(ItemColumnIds.VALUE.name(),"Value",Money.class,valueCell);
    valueColumn.setWidthSpecs(120,120,120);
    valueColumn.setCellRenderer(buildMoneyCellRenderer());
    valueColumn.setComparator(new MoneyComparator());
    return valueColumn;
  }

  /**
   * Build a cell renderer for a money cell.
   * @return a new cell renderer.
   */
  public static TableCellRenderer buildMoneyCellRenderer()
  {
    final MoneyDisplayController moneyCtrl=new MoneyDisplayController();
    TableCellRenderer renderer=new TableCellRenderer()
    {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        Money money=(Money)value;
        if (money==null) money=new Money();
        moneyCtrl.setMoney(money);
        JPanel panel=moneyCtrl.getPanel();
        return panel;
      }
    };
    return renderer;
  }

  private static DefaultTableColumnController<Item,FixedDecimalsInteger> buildStatColumn(final StatDescription stat)
  {
    CellDataProvider<Item,FixedDecimalsInteger> statCell=new CellDataProvider<Item,FixedDecimalsInteger>()
    {
      @Override
      public FixedDecimalsInteger getData(Item item)
      {
        BasicStatsSet stats=item.getStats();
        FixedDecimalsInteger value=stats.getStat(stat);
        return value;
      }
    };
    String id=stat.getPersistenceKey();
    String name=stat.getName();
    DefaultTableColumnController<Item,FixedDecimalsInteger> statColumn=new DefaultTableColumnController<Item,FixedDecimalsInteger>(id,name,FixedDecimalsInteger.class,statCell);
    statColumn.setWidthSpecs(55,55,50);
    return statColumn;
  }
}
