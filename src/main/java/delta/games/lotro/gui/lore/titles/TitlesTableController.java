package delta.games.lotro.gui.lore.titles;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;

import delta.common.ui.swing.area.AbstractAreaController;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a table that shows titles.
 * @author DAM
 */
public class TitlesTableController extends AbstractAreaController
{
  // Data
  private TypedProperties _prefs;
  private List<TitleDescription> _titles;
  // GUI
  private JTable _table;
  private GenericTableController<TitleDescription> _tableController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public TitlesTableController(AreaController parent, TypedProperties prefs, Filter<TitleDescription> filter)
  {
    super(parent);
    _prefs=prefs;
    _titles=new ArrayList<TitleDescription>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<TitleDescription> buildTable()
  {
    ListDataProvider<TitleDescription> provider=new ListDataProvider<TitleDescription>(_titles);
    GenericTableController<TitleDescription> table=new GenericTableController<TitleDescription>(provider);
    List<DefaultTableColumnController<TitleDescription,?>> columns=buildColumns(this);
    for(DefaultTableColumnController<TitleDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<TitleDescription> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a recipes table.
   * @param parent Parent controller.
   * @return A list of columns for a recipes table.
   */
  public static List<DefaultTableColumnController<TitleDescription,?>> buildColumns(AreaController parent)
  {
    List<DefaultTableColumnController<TitleDescription,?>> ret=new ArrayList<DefaultTableColumnController<TitleDescription,?>>();
    // Icon column
    {
      CellDataProvider<TitleDescription,Icon> iconCell=new CellDataProvider<TitleDescription,Icon>()
      {
        @Override
        public Icon getData(TitleDescription title)
        {
          int id=title.getIconId();
          Icon icon=LotroIconsManager.getTitleIcon(id);
          return icon;
        }
      };
      DefaultTableColumnController<TitleDescription,Icon> iconColumn=new DefaultTableColumnController<TitleDescription,Icon>(TitleColumnIds.ICON.name(),"Icon",Icon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      ret.add(iconColumn);
    }
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<TitleDescription,Integer> idCell=new CellDataProvider<TitleDescription,Integer>()
      {
        @Override
        public Integer getData(TitleDescription title)
        {
          return Integer.valueOf(title.getIdentifier());
        }
      };
      DefaultTableColumnController<TitleDescription,Integer> idColumn=new DefaultTableColumnController<TitleDescription,Integer>(TitleColumnIds.ID.name(),"ID",Integer.class,idCell);
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Category column
    {
      CellDataProvider<TitleDescription,String> categoryCell=new CellDataProvider<TitleDescription,String>()
      {
        @Override
        public String getData(TitleDescription title)
        {
          return title.getCategory();
        }
      };
      DefaultTableColumnController<TitleDescription,String> categoryColumn=new DefaultTableColumnController<TitleDescription,String>(TitleColumnIds.CATEGORY.name(),"Category",String.class,categoryCell);
      categoryColumn.setWidthSpecs(100,200,180);
      ret.add(categoryColumn);
    }
    // Name column
    {
      CellDataProvider<TitleDescription,String> nameCell=new CellDataProvider<TitleDescription,String>()
      {
        @Override
        public String getData(TitleDescription title)
        {
          String rawName=title.getRawName();
          return ContextRendering.render(parent,rawName);
        }
      };
      DefaultTableColumnController<TitleDescription,String> nameColumn=new DefaultTableColumnController<TitleDescription,String>(TitleColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,-1,200);
      ret.add(nameColumn);
    }
    // Description column
    {
      CellDataProvider<TitleDescription,String> descriptionCell=new CellDataProvider<TitleDescription,String>()
      {
        @Override
        public String getData(TitleDescription title)
        {
          return title.getDescription();
        }
      };
      DefaultTableColumnController<TitleDescription,String> descriptionColumn=new DefaultTableColumnController<TitleDescription,String>(TitleColumnIds.DESCRIPTION.name(),"Description",String.class,descriptionCell);
      descriptionColumn.setWidthSpecs(100,-1,200);
      ret.add(descriptionColumn);
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
      columnIds.add(TitleColumnIds.ICON.name());
      if (UiConfiguration.showTechnicalColumns())
      {
        columnIds.add(TitleColumnIds.ID.name());
      }
      columnIds.add(TitleColumnIds.CATEGORY.name());
      columnIds.add(TitleColumnIds.NAME.name());
      columnIds.add(TitleColumnIds.DESCRIPTION.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<TitleDescription> getTableController()
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
   * Get the total number of recipes.
   * @return A number of recipes.
   */
  public int getNbItems()
  {
    return _titles.size();
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

  private void reset()
  {
    _titles.clear();
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
    reset();
    List<TitleDescription> titles=TitlesManager.getInstance().getAll();
    _titles.addAll(titles);
    Collections.sort(_titles,new NamedComparator());
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
    super.dispose();
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
    _titles=null;
  }
}
