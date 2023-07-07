package delta.games.lotro.gui.lore.collections.pets;

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
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.collections.pets.CosmeticPetDescription;
import delta.games.lotro.lore.collections.pets.CosmeticPetsManager;

/**
 * Controller for a table that shows pets.
 * @author DAM
 */
public class PetsTableController
{
  // Data
  private TypedProperties _prefs;
  private List<CosmeticPetDescription> _pets;
  // GUI
  private JTable _table;
  private GenericTableController<CosmeticPetDescription> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public PetsTableController(TypedProperties prefs, Filter<CosmeticPetDescription> filter)
  {
    _prefs=prefs;
    _pets=new ArrayList<CosmeticPetDescription>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<CosmeticPetDescription> buildTable()
  {
    ListDataProvider<CosmeticPetDescription> provider=new ListDataProvider<CosmeticPetDescription>(_pets);
    GenericTableController<CosmeticPetDescription> table=new GenericTableController<CosmeticPetDescription>(provider);
    List<DefaultTableColumnController<CosmeticPetDescription,?>> columns=buildColumns();
    for(DefaultTableColumnController<CosmeticPetDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<CosmeticPetDescription> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a recipes table.
   * @return A list of columns for a recipes table.
   */
  public static List<DefaultTableColumnController<CosmeticPetDescription,?>> buildColumns()
  {
    List<DefaultTableColumnController<CosmeticPetDescription,?>> ret=new ArrayList<DefaultTableColumnController<CosmeticPetDescription,?>>();
    // Icon column
    {
      CellDataProvider<CosmeticPetDescription,Icon> iconCell=new CellDataProvider<CosmeticPetDescription,Icon>()
      {
        @Override
        public Icon getData(CosmeticPetDescription pet)
        {
          int id=pet.getIconId();
          Icon icon=LotroIconsManager.getPetIcon(id);
          return icon;
        }
      };
      DefaultTableColumnController<CosmeticPetDescription,Icon> iconColumn=new DefaultTableColumnController<CosmeticPetDescription,Icon>(PetColumnIds.ICON.name(),"Icon",Icon.class,iconCell); // 18n
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      ret.add(iconColumn);
    }
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<CosmeticPetDescription,Integer> idCell=new CellDataProvider<CosmeticPetDescription,Integer>()
      {
        @Override
        public Integer getData(CosmeticPetDescription pet)
        {
          return Integer.valueOf(pet.getIdentifier());
        }
      };
      DefaultTableColumnController<CosmeticPetDescription,Integer> idColumn=new DefaultTableColumnController<CosmeticPetDescription,Integer>(PetColumnIds.ID.name(),"ID",Integer.class,idCell); // 18n
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Name column
    {
      CellDataProvider<CosmeticPetDescription,String> nameCell=new CellDataProvider<CosmeticPetDescription,String>()
      {
        @Override
        public String getData(CosmeticPetDescription pet)
        {
          return pet.getName();
        }
      };
      DefaultTableColumnController<CosmeticPetDescription,String> nameColumn=new DefaultTableColumnController<CosmeticPetDescription,String>(PetColumnIds.NAME.name(),"Name",String.class,nameCell); // 18n
      nameColumn.setWidthSpecs(150,200,180);
      ret.add(nameColumn);
    }
    // Initial name column
    {
      CellDataProvider<CosmeticPetDescription,String> initialNameCell=new CellDataProvider<CosmeticPetDescription,String>()
      {
        @Override
        public String getData(CosmeticPetDescription pet)
        {
          return pet.getInitialName();
        }
      };
      DefaultTableColumnController<CosmeticPetDescription,String> initialNameColumn=new DefaultTableColumnController<CosmeticPetDescription,String>(PetColumnIds.INITIAL_NAME.name(),"Initial Name",String.class,initialNameCell); // 18n
      initialNameColumn.setWidthSpecs(120,120,120);
      ret.add(initialNameColumn);
    }
    // Genus column
    {
      CellDataProvider<CosmeticPetDescription,String> genusCell=new CellDataProvider<CosmeticPetDescription,String>()
      {
        @Override
        public String getData(CosmeticPetDescription pet)
        {
          return pet.getClassification().getGenusLabel();
        }
      };
      DefaultTableColumnController<CosmeticPetDescription,String> genusColumn=new DefaultTableColumnController<CosmeticPetDescription,String>(PetColumnIds.GENUS.name(),"Genus",String.class,genusCell); // 18n
      genusColumn.setWidthSpecs(140,140,140);
      ret.add(genusColumn);
    }
    // Species column
    {
      CellDataProvider<CosmeticPetDescription,String> speciesCell=new CellDataProvider<CosmeticPetDescription,String>()
      {
        @Override
        public String getData(CosmeticPetDescription pet)
        {
          return pet.getClassification().getSpeciesLabel();
        }
      };
      DefaultTableColumnController<CosmeticPetDescription,String> speciesColumn=new DefaultTableColumnController<CosmeticPetDescription,String>(PetColumnIds.SPECIES.name(),"Species",String.class,speciesCell); // 18n
      speciesColumn.setWidthSpecs(100,100,100);
      ret.add(speciesColumn);
    }
    // Sub-species column
    {
      CellDataProvider<CosmeticPetDescription,String> subSpeciesCell=new CellDataProvider<CosmeticPetDescription,String>()
      {
        @Override
        public String getData(CosmeticPetDescription pet)
        {
          return pet.getClassification().getSubSpeciesLabel();
        }
      };
      DefaultTableColumnController<CosmeticPetDescription,String> subSpeciesColumn=new DefaultTableColumnController<CosmeticPetDescription,String>(PetColumnIds.SUBSPECIES.name(),"Sub-species",String.class,subSpeciesCell); // 18n
      subSpeciesColumn.setWidthSpecs(100,100,100);
      ret.add(subSpeciesColumn);
    }
    // Description column
    {
      CellDataProvider<CosmeticPetDescription,String> descriptionCell=new CellDataProvider<CosmeticPetDescription,String>()
      {
        @Override
        public String getData(CosmeticPetDescription pet)
        {
          return pet.getDescription();
        }
      };
      DefaultTableColumnController<CosmeticPetDescription,String> descriptionColumn=new DefaultTableColumnController<CosmeticPetDescription,String>(PetColumnIds.DESCRIPTION.name(),"Description",String.class,descriptionCell); // 18n
      descriptionColumn.setWidthSpecs(100,-1,200);
      ret.add(descriptionColumn);
    }
    // Source column
    {
      CellDataProvider<CosmeticPetDescription,String> sourceCell=new CellDataProvider<CosmeticPetDescription,String>()
      {
        @Override
        public String getData(CosmeticPetDescription pet)
        {
          return pet.getSourceDescription();
        }
      };
      DefaultTableColumnController<CosmeticPetDescription,String> sourceColumn=new DefaultTableColumnController<CosmeticPetDescription,String>(PetColumnIds.SOURCE.name(),"Source",String.class,sourceCell); // 18n
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
      columnIds.add(PetColumnIds.ICON.name());
      if (UiConfiguration.showTechnicalColumns())
      {
        columnIds.add(PetColumnIds.ID.name());
      }
      columnIds.add(PetColumnIds.NAME.name());
      columnIds.add(PetColumnIds.INITIAL_NAME.name());
      columnIds.add(PetColumnIds.GENUS.name());
      columnIds.add(PetColumnIds.SPECIES.name());
      columnIds.add(PetColumnIds.SUBSPECIES.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<CosmeticPetDescription> getTableController()
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
    return _pets.size();
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
    _pets.clear();
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
    List<CosmeticPetDescription> pets=CosmeticPetsManager.getInstance().getAll();
    for(CosmeticPetDescription pet : pets)
    {
      _pets.add(pet);
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
    _pets=null;
  }
}
