package delta.games.lotro.gui.lore.agents.mobs;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.enums.AgentClass;
import delta.games.lotro.common.enums.Alignment;
import delta.games.lotro.common.enums.ClassificationFilter;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.agents.mobs.MobDescription;

/**
 * Controller for a table that shows mobs.
 * @author DAM
 */
public class MobsTableController
{
  // Data
  private TypedProperties _prefs;
  private List<MobDescription> _mobs;
  // GUI
  private JTable _table;
  private GenericTableController<MobDescription> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   * @param mobs Mobs to use.
   */
  public MobsTableController(TypedProperties prefs, Filter<MobDescription> filter, List<MobDescription> mobs)
  {
    _prefs=prefs;
    _mobs=new ArrayList<MobDescription>(mobs);
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<MobDescription> buildTable()
  {
    ListDataProvider<MobDescription> provider=new ListDataProvider<MobDescription>(_mobs);
    GenericTableController<MobDescription> table=new GenericTableController<MobDescription>(provider);
    List<DefaultTableColumnController<MobDescription,?>> columns=buildColumns();
    for(DefaultTableColumnController<MobDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<MobDescription> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a recipes table.
   * @return A list of columns for a recipes table.
   */
  public static List<DefaultTableColumnController<MobDescription,?>> buildColumns()
  {
    List<DefaultTableColumnController<MobDescription,?>> ret=new ArrayList<DefaultTableColumnController<MobDescription,?>>();
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<MobDescription,Integer> idCell=new CellDataProvider<MobDescription,Integer>()
      {
        @Override
        public Integer getData(MobDescription mob)
        {
          return Integer.valueOf(mob.getIdentifier());
        }
      };
      DefaultTableColumnController<MobDescription,Integer> idColumn=new DefaultTableColumnController<MobDescription,Integer>(MobColumnIds.ID.name(),"ID",Integer.class,idCell); // 18n
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Name column
    {
      CellDataProvider<MobDescription,String> nameCell=new CellDataProvider<MobDescription,String>()
      {
        @Override
        public String getData(MobDescription mob)
        {
          return mob.getName();
        }
      };
      DefaultTableColumnController<MobDescription,String> nameColumn=new DefaultTableColumnController<MobDescription,String>(MobColumnIds.NAME.name(),"Name",String.class,nameCell); // 18n
      nameColumn.setWidthSpecs(100,-1,200);
      ret.add(nameColumn);
    }
    // Alignment column
    {
      CellDataProvider<MobDescription,String> alignmentCell=new CellDataProvider<MobDescription,String>()
      {
        @Override
        public String getData(MobDescription mob)
        {
          Alignment alignment=mob.getClassification().getAlignment();
          return (alignment!=null)?alignment.getLabel():null;
        }
      };
      DefaultTableColumnController<MobDescription,String> alignementColumn=new DefaultTableColumnController<MobDescription,String>(MobColumnIds.ALIGNMENT.name(),"Alignment",String.class,alignmentCell); // 18n
      alignementColumn.setWidthSpecs(60,60,60);
      ret.add(alignementColumn);
    }
    // Agent class column
    {
      CellDataProvider<MobDescription,String> agentClassCell=new CellDataProvider<MobDescription,String>()
      {
        @Override
        public String getData(MobDescription mob)
        {
          AgentClass agentClass=mob.getClassification().getAgentClass();
          return (agentClass!=null)?agentClass.getLabel():null;
        }
      };
      DefaultTableColumnController<MobDescription,String> agentClassColumn=new DefaultTableColumnController<MobDescription,String>(MobColumnIds.CLASS.name(),"Class",String.class,agentClassCell); // 18n
      agentClassColumn.setWidthSpecs(80,90,90);
      ret.add(agentClassColumn);
    }
    // Class filter column
    {
      CellDataProvider<MobDescription,String> classFilterCell=new CellDataProvider<MobDescription,String>()
      {
        @Override
        public String getData(MobDescription mob)
        {
          ClassificationFilter classification=mob.getClassification().getClassificationFilter();
          return (classification!=null)?classification.getLabel():null;
        }
      };
      DefaultTableColumnController<MobDescription,String> classFilterColumn=new DefaultTableColumnController<MobDescription,String>(MobColumnIds.CLASS_FILTER.name(),"Classification",String.class,classFilterCell); // 18n
      classFilterColumn.setWidthSpecs(60,60,60);
      ret.add(classFilterColumn);
    }
    // Genus column
    {
      CellDataProvider<MobDescription,String> genusCell=new CellDataProvider<MobDescription,String>()
      {
        @Override
        public String getData(MobDescription mob)
        {
          return mob.getClassification().getEntityClassification().getGenusLabel();
        }
      };
      DefaultTableColumnController<MobDescription,String> genusColumn=new DefaultTableColumnController<MobDescription,String>(MobColumnIds.GENUS.name(),"Genus",String.class,genusCell); // 18n
      genusColumn.setWidthSpecs(80,120,120);
      ret.add(genusColumn);
    }
    // Species column
    {
      CellDataProvider<MobDescription,String> speciesCell=new CellDataProvider<MobDescription,String>()
      {
        @Override
        public String getData(MobDescription mob)
        {
          return mob.getClassification().getEntityClassification().getSpeciesLabel();
        }
      };
      DefaultTableColumnController<MobDescription,String> speciesColumn=new DefaultTableColumnController<MobDescription,String>(MobColumnIds.SPECIES.name(),"Species",String.class,speciesCell); // 18n
      speciesColumn.setWidthSpecs(80,120,120);
      ret.add(speciesColumn);
    }
    // Subspecies column
    {
      CellDataProvider<MobDescription,String> subSpeciesCell=new CellDataProvider<MobDescription,String>()
      {
        @Override
        public String getData(MobDescription mob)
        {
          return mob.getClassification().getEntityClassification().getSubSpeciesLabel();
        }
      };
      DefaultTableColumnController<MobDescription,String> subSpeciesColumn=new DefaultTableColumnController<MobDescription,String>(MobColumnIds.SUBSPECIES.name(),"Subspecies",String.class,subSpeciesCell); // 18n
      subSpeciesColumn.setWidthSpecs(80,180,180);
      ret.add(subSpeciesColumn);
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
      if (UiConfiguration.showTechnicalColumns())
      {
        columnIds.add(MobColumnIds.ID.name());
      }
      columnIds.add(MobColumnIds.NAME.name());
      columnIds.add(MobColumnIds.ALIGNMENT.name());
      columnIds.add(MobColumnIds.CLASS.name());
      columnIds.add(MobColumnIds.CLASS_FILTER.name());
      columnIds.add(MobColumnIds.GENUS.name());
      columnIds.add(MobColumnIds.SPECIES.name());
      columnIds.add(MobColumnIds.SUBSPECIES.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<MobDescription> getTableController()
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
    return _mobs.size();
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
    _mobs=null;
  }
}
