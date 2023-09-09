package delta.games.lotro.gui.lore.items.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.ColumnsUtils;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.money.comparator.MoneyComparator;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatType;
import delta.games.lotro.gui.lore.items.ItemColumnIds;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.utils.MoneyCellRenderer;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;
import delta.games.lotro.gui.utils.l10n.StatRenderer;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.scaling.ItemScaling;
import delta.games.lotro.lore.items.scaling.ItemScalingEntry;
import delta.games.lotro.lore.items.scaling.WeaponScalingEntry;

/**
 * Builder for a table that scaling data for an item.
 * @author DAM
 */
public class ItemScalingTableBuilder
{
  private static final Logger LOGGER=Logger.getLogger(ItemScalingTableBuilder.class);

  /**
   * Build a table to show item scaling data.
   * @param scaling Scaling data to show.
   * @return A new table controller.
   */
  public static GenericTableController<ItemScalingEntry> buildTable(ItemScaling scaling)
  {
    DataProvider<ItemScalingEntry> provider=new ListDataProvider<ItemScalingEntry>(scaling.getEntries());
    GenericTableController<ItemScalingEntry> table=new GenericTableController<ItemScalingEntry>(provider);
    List<DefaultTableColumnController<ItemScalingEntry,?>> columns=initColumns(scaling);
    TableColumnsManager<ItemScalingEntry> columnsManager=table.getColumnsManager();
    for(DefaultTableColumnController<ItemScalingEntry,?> column : columns)
    {
      columnsManager.addColumnController(column,true);
    }
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
   * @param scaling Scaling data to show.
   * @return A list of column controllers.
   */
  public static List<DefaultTableColumnController<ItemScalingEntry,?>> initColumns(ItemScaling scaling)
  {
    List<DefaultTableColumnController<ItemScalingEntry,?>> columns=new ArrayList<DefaultTableColumnController<ItemScalingEntry,?>>();
    // Level column
    columns.add(buildLevelColumn());
    // Item level column
    columns.add(buildItemLevelColumn());
    // Weapon scaling?
    boolean weaponScaling=false;
    List<ItemScalingEntry> entries=scaling.getEntries();
    if (!entries.isEmpty())
    {
      ItemScalingEntry firstEntry=entries.get(0);
      weaponScaling=(firstEntry instanceof WeaponScalingEntry);
    }
    if (weaponScaling)
    {
      // DPS
      columns.add(buildDPSColumn());
      // Min damage
      columns.add(buildMinDamageColumn());
      // Max damage
      columns.add(buildMaxDamageColumn());
    }
    // Value
    columns.add(buildValueColumn());
    // Stat columns
    for(StatDescription stat : scaling.getStats())
    {
      columns.add(buildStatColumn(stat));
    }
    return columns;
  }

  /**
   * Build a column for the item level of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<ItemScalingEntry,Integer> buildItemLevelColumn()
  {
    CellDataProvider<ItemScalingEntry,Integer> levelCell=new CellDataProvider<ItemScalingEntry,Integer>()
    {
      @Override
      public Integer getData(ItemScalingEntry item)
      {
        return Integer.valueOf(item.getItemLevel());
      }
    };
    DefaultTableColumnController<ItemScalingEntry,Integer> levelColumn=new DefaultTableColumnController<ItemScalingEntry,Integer>(ItemScalingColumnIds.ITEM_LEVEL.name(),"Item Lvl",Integer.class,levelCell);
    levelColumn.setWidthSpecs(55,55,50);
    return levelColumn;
  }

  /**
   * Build a column for the min required level of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<ItemScalingEntry,Integer> buildLevelColumn()
  {
    CellDataProvider<ItemScalingEntry,Integer> minLevelCell=new CellDataProvider<ItemScalingEntry,Integer>()
    {
      @Override
      public Integer getData(ItemScalingEntry item)
      {
        return Integer.valueOf(item.getLevel());
      }
    };
    DefaultTableColumnController<ItemScalingEntry,Integer> minLevelColumn=new DefaultTableColumnController<ItemScalingEntry,Integer>(ItemScalingColumnIds.LEVEL.name(),"Level",Integer.class,minLevelCell);
    minLevelColumn.setWidthSpecs(55,55,50);
    return minLevelColumn;
  }

  /**
   * Build a column for the value of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<ItemScalingEntry,Money> buildValueColumn()
  {
    CellDataProvider<ItemScalingEntry,Money> valueCell=new CellDataProvider<ItemScalingEntry,Money>()
    {
      @Override
      public Money getData(ItemScalingEntry item)
      {
        Money money=item.getMoney();
        return money;
      }
    };
    DefaultTableColumnController<ItemScalingEntry,Money> valueColumn=new DefaultTableColumnController<ItemScalingEntry,Money>(ItemScalingColumnIds.VALUE.name(),"Value",Money.class,valueCell);
    valueColumn.setWidthSpecs(150,150,150);
    valueColumn.setCellRenderer(new MoneyCellRenderer());
    valueColumn.setComparator(new MoneyComparator());
    return valueColumn;
  }

  private static DefaultTableColumnController<ItemScalingEntry,Number> buildStatColumn(final StatDescription stat)
  {
    CellDataProvider<ItemScalingEntry,Number> statCell=new CellDataProvider<ItemScalingEntry,Number>()
    {
      @Override
      public Number getData(ItemScalingEntry item)
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
    DefaultTableColumnController<ItemScalingEntry,Number> statColumn=new DefaultTableColumnController<ItemScalingEntry,Number>(id,name,valueClass,statCell);
    StatRenderer renderer=new StatRenderer(stat);
    StatColumnsUtils.configureStatValueColumn(statColumn,renderer,55);
    return statColumn;
  }

  /**
   * Build a column for the DPS of an item.
   * @return a column.
   */
  public static DefaultTableColumnController<ItemScalingEntry,Float> buildDPSColumn()
  {
    CellDataProvider<ItemScalingEntry,Float> valueCell=new CellDataProvider<ItemScalingEntry,Float>()
    {
      @Override
      public Float getData(ItemScalingEntry item)
      {
        return Float.valueOf(((WeaponScalingEntry)item).getDPS());
      }
    };
    DefaultTableColumnController<ItemScalingEntry,Float> valueColumn=new DefaultTableColumnController<ItemScalingEntry,Float>(ItemScalingColumnIds.DPS.name(),"DPS",Float.class,valueCell);
    ColumnsUtils.configureFloatColumn(valueColumn,0,1,60);
    return valueColumn;
  }

  /**
   * Build a column for the minimum damage of a weapon.
   * @return a column.
   */
  public static DefaultTableColumnController<ItemScalingEntry,Integer> buildMinDamageColumn()
  {
    CellDataProvider<ItemScalingEntry,Integer> valueCell=new CellDataProvider<ItemScalingEntry,Integer>()
    {
      @Override
      public Integer getData(ItemScalingEntry item)
      {
        float minDamage=((WeaponScalingEntry)item).getMinDamage();
        return Integer.valueOf(Math.round(minDamage));
      }
    };
    DefaultTableColumnController<ItemScalingEntry,Integer> valueColumn=new DefaultTableColumnController<ItemScalingEntry,Integer>(ItemScalingColumnIds.MIN_DAMAGE.name(),"Min Damage",Integer.class,valueCell);
    ColumnsUtils.configureIntegerColumn(valueColumn,50);
    return valueColumn;
  }

  /**
   * Build a column for the maximum damage of a weapon.
   * @return a column.
   */
  public static DefaultTableColumnController<ItemScalingEntry,Integer> buildMaxDamageColumn()
  {
    CellDataProvider<ItemScalingEntry,Integer> valueCell=new CellDataProvider<ItemScalingEntry,Integer>()
    {
      @Override
      public Integer getData(ItemScalingEntry item)
      {
        float maxDamage=((WeaponScalingEntry)item).getMaxDamage();
        return Integer.valueOf(Math.round(maxDamage));
      }
    };
    DefaultTableColumnController<ItemScalingEntry,Integer> valueColumn=new DefaultTableColumnController<ItemScalingEntry,Integer>(ItemScalingColumnIds.MAX_DAMAGE.name(),"Max Damage",Integer.class,valueCell);
    ColumnsUtils.configureIntegerColumn(valueColumn,50);
    return valueColumn;
  }
}
