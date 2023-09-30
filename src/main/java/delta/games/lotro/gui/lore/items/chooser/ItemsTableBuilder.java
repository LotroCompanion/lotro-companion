package delta.games.lotro.gui.lore.items.chooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.ColumnsUtils;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.classes.AbstractClassDescription;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.money.comparator.MoneyComparator;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatType;
import delta.games.lotro.common.stats.StatsRegistry;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.lore.items.ItemColumnIds;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.utils.MoneyCellRenderer;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;
import delta.games.lotro.gui.utils.l10n.StatRenderer;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemBinding;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.comparators.WeaponSlayerInfoComparator;
import delta.games.lotro.lore.items.details.ItemDetailsManager;
import delta.games.lotro.lore.items.details.WeaponSlayerInfo;
import delta.games.lotro.lore.items.weapons.WeaponSpeedEntry;

/**
 * Builder for a table that shows items.
 * @author DAM
 */
public class ItemsTableBuilder
{
  private static final Logger LOGGER=Logger.getLogger(ItemsTableBuilder.class);

  /**
   * Build a table to show items.
   * @param items Items to show.
   * @return A new table controller.
   */
  public static GenericTableController<Item> buildTable(List<? extends Item> items)
  {
    DataProvider<Item> provider=new ListDataProvider<Item>(items);
    GenericTableController<Item> table=new GenericTableController<Item>(provider);
    List<DefaultTableColumnController<Item,?>> columns=initColumns();
    TableColumnsManager<Item> columnsManager=table.getColumnsManager();
    for(DefaultTableColumnController<Item,?> column : columns)
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
    // Category column
    columns.add(buildCategoryColumn());
    // Required min level column
    columns.add(buildMinLevelColumn());
    // Required max level column
    columns.add(buildMaxLevelColumn());
    // Class requirement
    {
      CellDataProvider<Item,AbstractClassDescription> requiredClassCell=new CellDataProvider<Item,AbstractClassDescription>()
      {
        @Override
        public AbstractClassDescription getData(Item item)
        {
          return item.getRequiredClass();
        }
      };
      String columnName=Labels.getLabel("items.table.class");
      DefaultTableColumnController<Item,AbstractClassDescription> requiredClassColumn=new DefaultTableColumnController<Item,AbstractClassDescription>(ItemColumnIds.CLASS.name(),columnName,AbstractClassDescription.class,requiredClassCell);
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
      String columnName=Labels.getLabel("items.table.slotsCount");
      DefaultTableColumnController<Item,Integer> slotsColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.SLOT_COUNT.name(),columnName,Integer.class,slotsCell);
      slotsColumn.setWidthSpecs(55,55,50);
      columns.add(slotsColumn);
    }
    // Tier (essences, traceries)
    {
      CellDataProvider<Item,Integer> tierCell=new CellDataProvider<Item,Integer>()
      {
        @Override
        public Integer getData(Item item)
        {
          return item.getTier();
        }
      };
      String columnName=Labels.getLabel("items.table.tier");
      DefaultTableColumnController<Item,Integer> tierColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.TIER.name(),columnName,Integer.class,tierCell);
      tierColumn.setWidthSpecs(55,55,50);
      columns.add(tierColumn);
    }
    // Quality
    columns.add(buildQualityColumn());
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
      String columnName=Labels.getLabel("items.table.armourType");
      DefaultTableColumnController<Item,ArmourType> armourTypeColumn=new DefaultTableColumnController<Item,ArmourType>(ItemColumnIds.ARMOUR_TYPE.name(),columnName,ArmourType.class,armourTypeCell);
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
      String columnName=Labels.getLabel("items.table.weaponType");
      DefaultTableColumnController<Item,WeaponType> weaponTypeColumn=new DefaultTableColumnController<Item,WeaponType>(ItemColumnIds.WEAPON_TYPE.name(),columnName,WeaponType.class,weaponTypeCell);
      weaponTypeColumn.setWidthSpecs(150,150,150);
      columns.add(weaponTypeColumn);
    }
    // DPS
    columns.add(buildDPSColumn());
    // Speed
    boolean isLive=LotroCoreConfig.isLive();
    if (!isLive)
    {
      columns.add(buildSpeedColumn());
    }
    // Damage type
    columns.add(buildDamageTypeColumn());
    // Min Damage
    columns.add(buildMinDamageColumn());
    // Max Damage
    columns.add(buildMaxDamageColumn());
    // Weapon slayer
    columns.add(buildWeaponSlayerColumn());
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
    String columnName=Labels.getLabel("items.table.icon");
    DefaultTableColumnController<Item,Icon> iconColumn=new DefaultTableColumnController<Item,Icon>(ItemColumnIds.ICON.name(),columnName,Icon.class,iconCell);
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
    String columnName=Labels.getLabel("items.table.id");
    DefaultTableColumnController<Item,Integer> idColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.ID.name(),columnName,Integer.class,idCell);
    idColumn.setWidthSpecs(90,90,50);
    return idColumn;
  }

  /**
   * Build a column for the name of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,String> buildNameColumn()
  {
    String columnName=Labels.getLabel("items.table.name");
    return buildNameColumn(ItemColumnIds.NAME.name(),columnName);
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
    String columnName=Labels.getLabel("items.table.itemLevel");
    DefaultTableColumnController<Item,Integer> levelColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.ITEM_LEVEL.name(),columnName,Integer.class,levelCell);
    levelColumn.setWidthSpecs(55,55,50);
    return levelColumn;
  }

  /**
   * Build a column for the min required level of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,Integer> buildMinLevelColumn()
  {
    CellDataProvider<Item,Integer> minLevelCell=new CellDataProvider<Item,Integer>()
    {
      @Override
      public Integer getData(Item item)
      {
        return item.getMinLevel();
      }
    };
    String columnName=Labels.getLabel("items.table.level");
    DefaultTableColumnController<Item,Integer> minLevelColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.REQUIRED_LEVEL.name(),columnName,Integer.class,minLevelCell);
    minLevelColumn.setWidthSpecs(55,55,50);
    return minLevelColumn;
  }

  /**
   * Build a column for the max required level of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,Integer> buildMaxLevelColumn()
  {
    CellDataProvider<Item,Integer> maxLevelCell=new CellDataProvider<Item,Integer>()
    {
      @Override
      public Integer getData(Item item)
      {
        return item.getMaxLevel();
      }
    };
    String columnName=Labels.getLabel("items.table.maxLevel");
    DefaultTableColumnController<Item,Integer> maxLevelColumn=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.REQUIRED_MAX_LEVEL.name(),columnName,Integer.class,maxLevelCell);
    maxLevelColumn.setWidthSpecs(55,55,50);
    return maxLevelColumn;
  }

  /**
   * Build a column for the quality of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,ItemQuality> buildQualityColumn()
  {
    CellDataProvider<Item,ItemQuality> qualityCell=new CellDataProvider<Item,ItemQuality>()
    {
      @Override
      public ItemQuality getData(Item item)
      {
        return item.getQuality();
      }
    };
    String columnName=Labels.getLabel("items.table.quality");
    DefaultTableColumnController<Item,ItemQuality> qualityColumn=new DefaultTableColumnController<Item,ItemQuality>(ItemColumnIds.QUALITY.name(),columnName,ItemQuality.class,qualityCell);
    qualityColumn.setWidthSpecs(100,100,100);
    return qualityColumn;
  }

  /**
   * Build a column for the category of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<Item,String> buildCategoryColumn()
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
    String columnName=Labels.getLabel("items.table.category");
    DefaultTableColumnController<Item,String> categoryColumn=new DefaultTableColumnController<Item,String>(ItemColumnIds.CATEGORY.name(),columnName,String.class,categoryCell);
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
    String columnName=Labels.getLabel("items.table.value");
    DefaultTableColumnController<Item,Money> valueColumn=new DefaultTableColumnController<Item,Money>(ItemColumnIds.VALUE.name(),columnName,Money.class,valueCell);
    valueColumn.setWidthSpecs(120,120,120);
    valueColumn.setCellRenderer(new MoneyCellRenderer());
    valueColumn.setComparator(new MoneyComparator());
    return valueColumn;
  }

  private static DefaultTableColumnController<Item,Float> buildDPSColumn()
  {
    CellDataProvider<Item,Float> cell=new CellDataProvider<Item,Float>()
    {
      @Override
      public Float getData(Item item)
      {
        if (item instanceof Weapon)
        {
          Weapon weapon=(Weapon)item;
          return Float.valueOf(weapon.getDPS());
        }
        return null;
      }
    };
    String columnName=Labels.getLabel("items.table.dps");
    DefaultTableColumnController<Item,Float> column=new DefaultTableColumnController<Item,Float>(ItemColumnIds.DPS.name(),columnName,Float.class,cell);
    ColumnsUtils.configureFloatColumn(column,0,1,50);
    return column;
  }

  private static DefaultTableColumnController<Item,Float> buildSpeedColumn()
  {
    CellDataProvider<Item,Float> cell=new CellDataProvider<Item,Float>()
    {
      @Override
      public Float getData(Item item)
      {
        if (item instanceof Weapon)
        {
          Weapon weapon=(Weapon)item;
          WeaponSpeedEntry speedEntry=weapon.getSpeed();
          if (speedEntry!=null)
          {
            return Float.valueOf(speedEntry.getBaseActionDuration());
          }
        }
        return null;
      }
    };
    String columnName=Labels.getLabel("items.table.speed");
    DefaultTableColumnController<Item,Float> column=new DefaultTableColumnController<Item,Float>(ItemColumnIds.SPEED.name(),columnName,Float.class,cell);
    ColumnsUtils.configureFloatColumn(column,0,1,50);
    return column;
  }

  private static DefaultTableColumnController<Item,DamageType> buildDamageTypeColumn()
  {
    CellDataProvider<Item,DamageType> cell=new CellDataProvider<Item,DamageType>()
    {
      @Override
      public DamageType getData(Item item)
      {
        if (item instanceof Weapon)
        {
          Weapon weapon=(Weapon)item;
          return weapon.getDamageType();
        }
        return null;
      }
    };
    String columnName=Labels.getLabel("items.table.damageType");
    DefaultTableColumnController<Item,DamageType> column=new DefaultTableColumnController<Item,DamageType>(ItemColumnIds.DAMAGE_TYPE.name(),columnName,DamageType.class,cell);
    column.setWidthSpecs(100,130,130);
    return column;
  }

  private static DefaultTableColumnController<Item,Integer> buildMaxDamageColumn()
  {
    CellDataProvider<Item,Integer> cell=new CellDataProvider<Item,Integer>()
    {
      @Override
      public Integer getData(Item item)
      {
        if (item instanceof Weapon)
        {
          Weapon weapon=(Weapon)item;
          return Integer.valueOf(weapon.getMaxDamage());
        }
        return null;
      }
    };
    String columnName=Labels.getLabel("items.table.maxDamage");
    DefaultTableColumnController<Item,Integer> column=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.MAX_DAMAGE.name(),columnName,Integer.class,cell);
    ColumnsUtils.configureIntegerColumn(column,50);
    return column;
  }

  private static DefaultTableColumnController<Item,Integer> buildMinDamageColumn()
  {
    CellDataProvider<Item,Integer> cell=new CellDataProvider<Item,Integer>()
    {
      @Override
      public Integer getData(Item item)
      {
        if (item instanceof Weapon)
        {
          Weapon weapon=(Weapon)item;
          return Integer.valueOf(weapon.getMinDamage());
        }
        return null;
      }
    };
    String columnName=Labels.getLabel("items.table.minDamage");
    DefaultTableColumnController<Item,Integer> column=new DefaultTableColumnController<Item,Integer>(ItemColumnIds.MIN_DAMAGE.name(),columnName,Integer.class,cell);
    ColumnsUtils.configureIntegerColumn(column,50);
    return column;
  }

  private static DefaultTableColumnController<Item,WeaponSlayerInfo> buildWeaponSlayerColumn()
  {
    CellDataProvider<Item,WeaponSlayerInfo> cell=new CellDataProvider<Item,WeaponSlayerInfo>()
    {
      @Override
      public WeaponSlayerInfo getData(Item item)
      {
        ItemDetailsManager detailsMgr=item.getDetails();
        return (detailsMgr!=null)?detailsMgr.getFirstItemDetail(WeaponSlayerInfo.class):null;
      }
    };
    String columnName=Labels.getLabel("items.table.slayer");
    DefaultTableColumnController<Item,WeaponSlayerInfo> column=new DefaultTableColumnController<Item,WeaponSlayerInfo>(ItemColumnIds.WEAPON_SLAYER.name(),columnName,WeaponSlayerInfo.class,cell);
    column.setWidthSpecs(100,200,200);
    column.setComparator(new WeaponSlayerInfoComparator());
    return column;
  }

  private static DefaultTableColumnController<Item,Number> buildStatColumn(final StatDescription stat)
  {
    CellDataProvider<Item,Number> statCell=new CellDataProvider<Item,Number>()
    {
      @Override
      public Number getData(Item item)
      {
        BasicStatsSet stats=item.getStats();
        Number value=stats.getStat(stat);
        return value;
      }
    };
    String id=stat.getPersistenceKey();
    String name=stat.getName();
    StatType type=stat.getType();
    Class<? extends Number> valueClass;
    if (type==StatType.INTEGER)
    {
      valueClass=Integer.class;
    }
    else if (type==StatType.FLOAT)
    {
      valueClass=Float.class;
    }
    else
    {
      valueClass=Integer.class;
      LOGGER.warn("Unmanaged stat type: "+type);
    }
    DefaultTableColumnController<Item,Number> statColumn=new DefaultTableColumnController<Item,Number>(id,name,valueClass,statCell);
    StatRenderer renderer=new StatRenderer(stat);
    StatColumnsUtils.configureStatValueColumn(statColumn,renderer,55);
    return statColumn;
  }
}
