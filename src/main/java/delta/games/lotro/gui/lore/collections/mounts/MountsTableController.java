package delta.games.lotro.gui.lore.collections.mounts;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.enums.MountType;
import delta.games.lotro.common.enums.SkillCharacteristicSubCategory;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.collections.mounts.MountDescription;
import delta.games.lotro.lore.collections.mounts.MountsManager;

/**
 * Controller for a table that shows mounts.
 * @author DAM
 */
public class MountsTableController
{
  // Data
  private TypedProperties _prefs;
  private List<MountDescription> _mounts;
  // GUI
  private JTable _table;
  private GenericTableController<MountDescription> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public MountsTableController(TypedProperties prefs, Filter<MountDescription> filter)
  {
    _prefs=prefs;
    _mounts=new ArrayList<MountDescription>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<MountDescription> buildTable()
  {
    ListDataProvider<MountDescription> provider=new ListDataProvider<MountDescription>(_mounts);
    GenericTableController<MountDescription> table=new GenericTableController<MountDescription>(provider);
    List<DefaultTableColumnController<MountDescription,?>> columns=buildColumns();
    for(DefaultTableColumnController<MountDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<MountDescription> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a mounts table.
   * @return A list of columns for a mounts table.
   */
  public static List<DefaultTableColumnController<MountDescription,?>> buildColumns()
  {
    List<DefaultTableColumnController<MountDescription,?>> ret=new ArrayList<DefaultTableColumnController<MountDescription,?>>();
    // Icon column
    {
      CellDataProvider<MountDescription,Icon> iconCell=new CellDataProvider<MountDescription,Icon>()
      {
        @Override
        public Icon getData(MountDescription mount)
        {
          int id=mount.getIconId();
          Icon icon=LotroIconsManager.getMountIcon(id);
          return icon;
        }
      };
      DefaultTableColumnController<MountDescription,Icon> iconColumn=new DefaultTableColumnController<MountDescription,Icon>(MountColumnIds.ICON.name(),"Icon",Icon.class,iconCell); // 18n
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      ret.add(iconColumn);
    }
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<MountDescription,Integer> idCell=new CellDataProvider<MountDescription,Integer>()
      {
        @Override
        public Integer getData(MountDescription mount)
        {
          return Integer.valueOf(mount.getIdentifier());
        }
      };
      DefaultTableColumnController<MountDescription,Integer> idColumn=new DefaultTableColumnController<MountDescription,Integer>(MountColumnIds.ID.name(),"ID",Integer.class,idCell); // 18n
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Category column
    {
      CellDataProvider<MountDescription,SkillCharacteristicSubCategory> categoryCell=new CellDataProvider<MountDescription,SkillCharacteristicSubCategory>()
      {
        @Override
        public SkillCharacteristicSubCategory getData(MountDescription mount)
        {
          return mount.getMountCategory();
        }
      };
      DefaultTableColumnController<MountDescription,SkillCharacteristicSubCategory> categoryColumn=new DefaultTableColumnController<MountDescription,SkillCharacteristicSubCategory>(MountColumnIds.CATEGORY.name(),"Category",SkillCharacteristicSubCategory.class,categoryCell); // 18n
      categoryColumn.setWidthSpecs(100,140,140);
      ret.add(categoryColumn);
    }
    // Name column
    {
      CellDataProvider<MountDescription,String> nameCell=new CellDataProvider<MountDescription,String>()
      {
        @Override
        public String getData(MountDescription mount)
        {
          return mount.getName();
        }
      };
      DefaultTableColumnController<MountDescription,String> nameColumn=new DefaultTableColumnController<MountDescription,String>(MountColumnIds.NAME.name(),"Name",String.class,nameCell); // 18n
      nameColumn.setWidthSpecs(100,200,180);
      ret.add(nameColumn);
    }
    // Initial name column
    {
      CellDataProvider<MountDescription,String> initialNameCell=new CellDataProvider<MountDescription,String>()
      {
        @Override
        public String getData(MountDescription mount)
        {
          return mount.getInitialName();
        }
      };
      DefaultTableColumnController<MountDescription,String> initialNameColumn=new DefaultTableColumnController<MountDescription,String>(MountColumnIds.INITIAL_NAME.name(),"Initial Name",String.class,initialNameCell); // 18n
      initialNameColumn.setWidthSpecs(120,120,120);
      ret.add(initialNameColumn);
    }
    // Mount type column
    {
      CellDataProvider<MountDescription,MountType> mountTypeCell=new CellDataProvider<MountDescription,MountType>()
      {
        @Override
        public MountType getData(MountDescription mount)
        {
          return mount.getMountType();
        }
      };
      DefaultTableColumnController<MountDescription,MountType> mountTypeColumn=new DefaultTableColumnController<MountDescription,MountType>(MountColumnIds.MOUNT_TYPE.name(),"Mount Type",MountType.class,mountTypeCell); // 18n
      mountTypeColumn.setWidthSpecs(100,100,100);
      ret.add(mountTypeColumn);
    }
    // Max morale
    {
      CellDataProvider<MountDescription,Integer> moraleCell=new CellDataProvider<MountDescription,Integer>()
      {
        @Override
        public Integer getData(MountDescription mount)
        {
          return Integer.valueOf(mount.getMorale());
        }
      };
      DefaultTableColumnController<MountDescription,Integer> moraleColumn=new DefaultTableColumnController<MountDescription,Integer>(MountColumnIds.MORALE.name(),"Morale",Integer.class,moraleCell); // 18n
      moraleColumn.setWidthSpecs(50,50,50);
      ret.add(moraleColumn);
    }
    // Speed
    {
      CellDataProvider<MountDescription,Integer> speedCell=new CellDataProvider<MountDescription,Integer>()
      {
        @Override
        public Integer getData(MountDescription mount)
        {
          return Integer.valueOf((int)(mount.getSpeed()*100));
        }
      };
      DefaultTableColumnController<MountDescription,Integer> speedColumn=new DefaultTableColumnController<MountDescription,Integer>(MountColumnIds.SPEED.name(),"Speed",Integer.class,speedCell); // 18n
      speedColumn.setWidthSpecs(50,50,50);
      ret.add(speedColumn);
    }
    // Size
    {
      CellDataProvider<MountDescription,String> sizeCell=new CellDataProvider<MountDescription,String>()
      {
        @Override
        public String getData(MountDescription mount)
        {
          return mount.isTall()?"Tall":"Short";
        }
      };
      DefaultTableColumnController<MountDescription,String> sizeColumn=new DefaultTableColumnController<MountDescription,String>(MountColumnIds.SIZE.name(),"Size",String.class,sizeCell); // 18n
      sizeColumn.setWidthSpecs(50,50,50);
      ret.add(sizeColumn);
    }
    // Description column
    {
      CellDataProvider<MountDescription,String> descriptionCell=new CellDataProvider<MountDescription,String>()
      {
        @Override
        public String getData(MountDescription mount)
        {
          return mount.getDescription();
        }
      };
      DefaultTableColumnController<MountDescription,String> descriptionColumn=new DefaultTableColumnController<MountDescription,String>(MountColumnIds.DESCRIPTION.name(),"Description",String.class,descriptionCell); // 18n
      descriptionColumn.setWidthSpecs(100,-1,200);
      ret.add(descriptionColumn);
    }
    // Source column
    {
      CellDataProvider<MountDescription,String> sourceCell=new CellDataProvider<MountDescription,String>()
      {
        @Override
        public String getData(MountDescription mount)
        {
          return mount.getSourceDescription();
        }
      };
      DefaultTableColumnController<MountDescription,String> sourceColumn=new DefaultTableColumnController<MountDescription,String>(MountColumnIds.SOURCE.name(),"Source",String.class,sourceCell); // 18n
      sourceColumn.setWidthSpecs(100,-1,200);
      ret.add(sourceColumn);
    }
    return ret;
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
      columnIds.add(MountColumnIds.ICON.name());
      if (UiConfiguration.showTechnicalColumns())
      {
        columnIds.add(MountColumnIds.ID.name());
      }
      columnIds.add(MountColumnIds.CATEGORY.name());
      columnIds.add(MountColumnIds.MOUNT_TYPE.name());
      columnIds.add(MountColumnIds.NAME.name());
      columnIds.add(MountColumnIds.INITIAL_NAME.name());
      columnIds.add(MountColumnIds.MORALE.name());
      columnIds.add(MountColumnIds.SPEED.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<MountDescription> getTableController()
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
   * Get the total number of mounts.
   * @return A number of mounts.
   */
  public int getNbItems()
  {
    return _mounts.size();
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

  /**
   * Refresh table.
   */
  public void refresh()
  {
    init();
    if (_table!=null)
    {
      _tableController.refresh();
    }
  }

  private void init()
  {
    _mounts.clear();
    List<MountDescription> mounts=MountsManager.getInstance().getAll();
    for(MountDescription mount : mounts)
    {
      _mounts.add(mount);
    }
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
    _mounts=null;
  }
}
