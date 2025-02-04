package delta.games.lotro.gui.character.status.housing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.ColumnsUtils;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.Sort;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.math.geometry.Vector3D;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.housing.HousingItem;
import delta.games.lotro.common.enums.HousingHookID;
import delta.games.lotro.common.geo.Position;
import delta.games.lotro.common.id.InternalGameId;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.table.ItemColumnIds;
import delta.games.lotro.gui.lore.items.table.ItemsTableBuilder;
import delta.games.lotro.gui.utils.tables.renderers.InternalGameIdRenderer;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Controller for a table that shows house items.
 * @author DAM
 */
public class HouseItemsTableController
{
  /**
   * Identifier of the "Hook" column.
   */
  public static final String HOOK_COLUMN="HOOK";
  /**
   * Identifier of the "Position" column.
   */
  public static final String POSITION_COLUMN="POSITION";
  /**
   * Identifier of the "Rotation" column.
   */
  public static final String ROTATION_COLUMN="ROTATION";
  /**
   * Identifier of the "Hook" column.
   */
  public static final String HOOK_ROTATION_COLUMN="HOOK_ROTATION";
  /**
   * Identifier of the "Position offset" column.
   */
  public static final String POSITION_OFFSET_COLUMN="POSITION_OFFSET";
  /**
   * Identifier of the "Bound to" column.
   */
  public static final String BOUND_TO_COLUMN="BOUND_TO";

  // Parent controller
  private WindowController _parent;
  // Preferences
  private TypedProperties _prefs;
  // Data
  protected List<HousingItem> _items;
  // GUI
  private GenericTableController<HousingItem> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Managed filter.
   */
  public HouseItemsTableController(WindowController parent, TypedProperties prefs, List<HousingItem> items, Filter<HousingItem> filter)
  {
    _parent=parent;
    _prefs=prefs;
    _items=items;
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  /**
   * Get the managed generic table controller.
   * @return the managed generic table controller.
   */
  public GenericTableController<HousingItem> getTableController()
  {
    return _tableController;
  }

  private GenericTableController<HousingItem> buildTable()
  {
    // Build table
    DataProvider<HousingItem> provider=new ListDataProvider<HousingItem>(_items);
    GenericTableController<HousingItem> table=new GenericTableController<HousingItem>(provider);
    List<TableColumnController<HousingItem,?>> columns=initColumns();
    TableColumnsManager<HousingItem> columnsManager=table.getColumnsManager();
    for(TableColumnController<HousingItem,?> column : columns)
    {
      columnsManager.addColumnController(column,false);
    }
    List<String> columnsIds=getColumnsId();
    columnsManager.setColumns(columnsIds);

    // Sort
    String sort=Sort.SORT_ASCENDING+ItemColumnIds.NAME+Sort.SORT_ITEM_SEPARATOR+Sort.SORT_ASCENDING+HOOK_COLUMN;
    table.setSort(Sort.buildFromString(sort));
    return table;
  }

  /**
   * Build a list of all managed columns.
   * @return A list of column controllers.
   */
  private List<TableColumnController<HousingItem,?>> initColumns()
  {
    List<TableColumnController<HousingItem,?>> ret=new ArrayList<TableColumnController<HousingItem,?>>();

    List<TableColumnController<Item,?>> columns=ItemsTableBuilder.initColumns();
    for(TableColumnController<Item,?> column : columns)
    {
      CellDataProvider<HousingItem,Item> dataProvider=new CellDataProvider<HousingItem,Item>()
      {
        @Override
        public Item getData(HousingItem hi)
        {
          int itemID=hi.getItemID();
          // TODO : put the Item inside the HousingItem
          Item item=ItemsManager.getInstance().getItem(itemID);
          return item;
        }
      };
      @SuppressWarnings("unchecked")
      TableColumnController<Item,Object> c=(TableColumnController<Item,Object>)column;
      TableColumnController<HousingItem,Object> proxiedColumn=new ProxiedTableColumnController<HousingItem,Item,Object>(c,dataProvider);
      ret.add(proxiedColumn);
    }
    // Hook column
    ret.add(buildHookColumn());
    // Position column
    ret.add(buildPositionColumn());
    // Rotation column
    ret.add(buildRotationColumn());
    // Hook rotation column
    ret.add(buildHookRotationColumn());
    // Position offset column
    ret.add(buildPositionOffsetColumn());
    // Bound to column
    ret.add(buildBoundToColumn());
    return ret;
  }

  private TableColumnController<HousingItem,?> buildHookColumn()
  {
    CellDataProvider<HousingItem,HousingHookID> cell=new CellDataProvider<HousingItem,HousingHookID>()
    {
      @Override
      public HousingHookID getData(HousingItem item)
      {
        return item.getHookID();
      }
    };
    DefaultTableColumnController<HousingItem,HousingHookID> column=new DefaultTableColumnController<HousingItem,HousingHookID>(HOOK_COLUMN,"Hook",HousingHookID.class,cell); // I18n
    column.setWidthSpecs(100,100,100);
    return column;
  }

  private TableColumnController<HousingItem,?> buildPositionColumn()
  {
    CellDataProvider<HousingItem,Position> cell=new CellDataProvider<HousingItem,Position>()
    {
      @Override
      public Position getData(HousingItem item)
      {
        return item.getPosition();
      }
    };
    DefaultTableColumnController<HousingItem,Position> column=new DefaultTableColumnController<HousingItem,Position>(POSITION_COLUMN,"Position",Position.class,cell); // I18n
    column.setWidthSpecs(100,150,150);
    column.setCellRenderer(new PositionRenderer());
    return column;
  }

  private TableColumnController<HousingItem,?> buildRotationColumn()
  {
    CellDataProvider<HousingItem,Float> cell=new CellDataProvider<HousingItem,Float>()
    {
      @Override
      public Float getData(HousingItem item)
      {
        return Float.valueOf(item.getRotationOffset());
      }
    };
    DefaultTableColumnController<HousingItem,Float> column=new DefaultTableColumnController<HousingItem,Float>(ROTATION_COLUMN,"Rotation",Float.class,cell); // I18n
    ColumnsUtils.configureFloatColumn(column,0,1,50);
    return column;
  }

  private TableColumnController<HousingItem,?> buildHookRotationColumn()
  {
    CellDataProvider<HousingItem,Float> cell=new CellDataProvider<HousingItem,Float>()
    {
      @Override
      public Float getData(HousingItem item)
      {
        return Float.valueOf(item.getHookRotation());
      }
    };
    DefaultTableColumnController<HousingItem,Float> column=new DefaultTableColumnController<HousingItem,Float>(HOOK_ROTATION_COLUMN,"Hook rotation",Float.class,cell); // I18n
    ColumnsUtils.configureFloatColumn(column,0,1,50);
    return column;
  }

  private TableColumnController<HousingItem,?> buildPositionOffsetColumn()
  {
    CellDataProvider<HousingItem,Vector3D> cell=new CellDataProvider<HousingItem,Vector3D>()
    {
      @Override
      public Vector3D getData(HousingItem item)
      {
        return item.getPositionOffset();
      }
    };
    DefaultTableColumnController<HousingItem,Vector3D> column=new DefaultTableColumnController<HousingItem,Vector3D>(POSITION_OFFSET_COLUMN,"Offset",Vector3D.class,cell); // I18n
    column.setWidthSpecs(100,200,150);
    column.setCellRenderer(new PositionOffsetRenderer());
    return column;
  }

  private TableColumnController<HousingItem,?> buildBoundToColumn()
  {
    CellDataProvider<HousingItem,InternalGameId> cell=new CellDataProvider<HousingItem,InternalGameId>()
    {
      @Override
      public InternalGameId getData(HousingItem item)
      {
        return item.getBoundTo();
      }
    };
    DefaultTableColumnController<HousingItem,InternalGameId> column=new DefaultTableColumnController<HousingItem,InternalGameId>(BOUND_TO_COLUMN,"Bound to",InternalGameId.class,cell); // I18n
    column.setWidthSpecs(150,150,150);
    column.setCellRenderer(new InternalGameIdRenderer());
    return column;
  }

  protected List<String> getColumnsId()
  {
    List<String> columnsIds=null;
    if (_prefs!=null)
    {
      columnsIds=_prefs.getStringList(ItemChooser.COLUMNS_PROPERTY);
    }
    if (columnsIds==null)
    {
      columnsIds=getDefaultColumnIds();
    }
    return columnsIds;
  }

  protected List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(ItemColumnIds.ICON.name());
    columnsIds.add(ItemColumnIds.NAME.name());
    columnsIds.add(HOOK_COLUMN);
    columnsIds.add(POSITION_COLUMN);
    columnsIds.add(POSITION_OFFSET_COLUMN);
    columnsIds.add(ROTATION_COLUMN);
    columnsIds.add(HOOK_ROTATION_COLUMN);
    columnsIds.add(BOUND_TO_COLUMN);
    return columnsIds;
  }

  private void configureTable()
  {
    JTable table=getTable();
    // Adjust table row height for icons (32 pixels)
    table.setRowHeight(32);
    // Action listener
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          Object sourceItem=event.getSource();
          showItem(sourceItem);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showItem(Object sourceItem)
  {
    HousingItem housingItem=(HousingItem)sourceItem;
    int itemID=housingItem.getItemID();
    Item item=ItemsManager.getInstance().getItem(itemID);
    if (item!=null)
    {
      ItemUiTools.showItemForm(_parent,item);
    }
  }

  /**
   * Update display.
   */
  public void update()
  {
    _tableController.refresh();
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
   * Release all managed resources.
   */
  public void dispose()
  {
    // Parent controller
    _parent=null;
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnIds=_tableController.getColumnsManager().getSelectedColumnsIds();
      _prefs.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
    }
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
