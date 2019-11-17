package delta.games.lotro.gui.items.chooser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.NumericTools;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsRegistry;
import delta.games.lotro.gui.items.ItemColumnIds;
import delta.games.lotro.gui.items.ItemUiTools;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemBinding;
import delta.games.lotro.lore.items.ItemPropertyNames;
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
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  public static List<DefaultTableColumnController<Item,?>> initColumns()
  {
    List<DefaultTableColumnController<Item,?>> columns=new ArrayList<DefaultTableColumnController<Item,?>>();
    // Icon column
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
      columns.add(iconColumn);
    }
    // ID column
    {
      CellDataProvider<Item,Long> idCell=new CellDataProvider<Item,Long>()
      {
        @Override
        public Long getData(Item item)
        {
          return Long.valueOf(item.getIdentifier());
        }
      };
      DefaultTableColumnController<Item,Long> idColumn=new DefaultTableColumnController<Item,Long>(ItemColumnIds.ID.name(),"ID",Long.class,idCell);
      idColumn.setWidthSpecs(90,90,50);
      columns.add(idColumn);
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
      DefaultTableColumnController<Item,String> nameColumn=new DefaultTableColumnController<Item,String>(ItemColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(150,-1,150);
      columns.add(nameColumn);
    }
    // Factory comment column
    {
      CellDataProvider<Item,String> factoryCommentCell=new CellDataProvider<Item,String>()
      {
        @Override
        public String getData(Item item)
        {
          return item.getProperty(ItemPropertyNames.FACTORY_COMMENT);
        }
      };
      DefaultTableColumnController<Item,String> factoryCommentColumn=new DefaultTableColumnController<Item,String>(ItemColumnIds.FACTORY_COMMENTS.name(),"Comment",String.class,factoryCommentCell);
      factoryCommentColumn.setWidthSpecs(150,-1,150);
      columns.add(factoryCommentColumn);
    }    // Level column
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
      columns.add(levelColumn);
    }
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
