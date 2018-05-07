package delta.games.lotro.gui.deed;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Emote;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.Skill;
import delta.games.lotro.common.Title;
import delta.games.lotro.common.Trait;
import delta.games.lotro.common.Virtue;
import delta.games.lotro.gui.items.ItemChoiceWindowController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.DeedsManager;

/**
 * Controller for a table that shows deeds.
 * @author DAM
 */
public class DeedsTableController
{
  // Data
  private TypedProperties _prefs;
  private List<DeedDescription> _deeds;
  // GUI
  private JTable _table;
  private GenericTableController<DeedDescription> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public DeedsTableController(TypedProperties prefs, Filter<DeedDescription> filter)
  {
    _prefs=prefs;
    _deeds=new ArrayList<DeedDescription>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<DeedDescription> buildTable()
  {
    ListDataProvider<DeedDescription> provider=new ListDataProvider<DeedDescription>(_deeds);
    GenericTableController<DeedDescription> table=new GenericTableController<DeedDescription>(provider);
    List<DefaultTableColumnController<DeedDescription,?>> columns=buildColumns();
    for(DefaultTableColumnController<DeedDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<DeedDescription> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a deeds table.
   * @return A list of columns for a deeds table.
   */
  public static List<DefaultTableColumnController<DeedDescription,?>> buildColumns()
  {
    List<DefaultTableColumnController<DeedDescription,?>> ret=new ArrayList<DefaultTableColumnController<DeedDescription,?>>();
    // Identifier column
    {
      CellDataProvider<DeedDescription,Integer> idCell=new CellDataProvider<DeedDescription,Integer>()
      {
        @Override
        public Integer getData(DeedDescription deed)
        {
          return Integer.valueOf(deed.getIdentifier());
        }
      };
      DefaultTableColumnController<DeedDescription,Integer> idColumn=new DefaultTableColumnController<DeedDescription,Integer>(DeedColumnIds.ID.name(),"ID",Integer.class,idCell);
      idColumn.setWidthSpecs(100,100,100);
      ret.add(idColumn);
    }
    // Key column
    {
      CellDataProvider<DeedDescription,String> keyCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          return deed.getKey();
        }
      };
      DefaultTableColumnController<DeedDescription,String> keyColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.KEY.name(),"Key",String.class,keyCell);
      keyColumn.setWidthSpecs(100,200,200);
      ret.add(keyColumn);
    }
    // Name column
    {
      CellDataProvider<DeedDescription,String> nameCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          deed.getObjectives();
          deed.getDescription();
          deed.getRewards();
          return deed.getName();
        }
      };
      DefaultTableColumnController<DeedDescription,String> nameColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,300,200);
      ret.add(nameColumn);
    }
    // Type column
    {
      CellDataProvider<DeedDescription,DeedType> typeCell=new CellDataProvider<DeedDescription,DeedType>()
      {
        @Override
        public DeedType getData(DeedDescription deed)
        {
          return deed.getType();
        }
      };
      DefaultTableColumnController<DeedDescription,DeedType> typeColumn=new DefaultTableColumnController<DeedDescription,DeedType>(DeedColumnIds.TYPE.name(),"Type",DeedType.class,typeCell);
      typeColumn.setWidthSpecs(80,100,80);
      ret.add(typeColumn);
    }
    // Category column
    {
      CellDataProvider<DeedDescription,String> categoryCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          return deed.getCategory();
        }
      };
      DefaultTableColumnController<DeedDescription,String> categoryColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.CATEGORY.name(),"Category",String.class,categoryCell);
      categoryColumn.setWidthSpecs(80,350,80);
      ret.add(categoryColumn);
    }
    // Class column
    {
      CellDataProvider<DeedDescription,CharacterClass> classCell=new CellDataProvider<DeedDescription,CharacterClass>()
      {
        @Override
        public CharacterClass getData(DeedDescription deed)
        {
          return deed.getRequiredClass();
        }
      };
      DefaultTableColumnController<DeedDescription,CharacterClass> classColumn=new DefaultTableColumnController<DeedDescription,CharacterClass>(DeedColumnIds.REQUIRED_CLASS.name(),"Class",CharacterClass.class,classCell);
      classColumn.setWidthSpecs(80,100,80);
      ret.add(classColumn);
    }
    // Race column
    {
      CellDataProvider<DeedDescription,Race> raceCell=new CellDataProvider<DeedDescription,Race>()
      {
        @Override
        public Race getData(DeedDescription deed)
        {
          return deed.getRequiredRace();
        }
      };
      DefaultTableColumnController<DeedDescription,Race> raceColumn=new DefaultTableColumnController<DeedDescription,Race>(DeedColumnIds.REQUIRED_RACE.name(),"Race",Race.class,raceCell);
      raceColumn.setWidthSpecs(80,100,80);
      ret.add(raceColumn);
    }
    // Min level column
    {
      CellDataProvider<DeedDescription,Integer> minLevelCell=new CellDataProvider<DeedDescription,Integer>()
      {
        @Override
        public Integer getData(DeedDescription deed)
        {
          return deed.getMinLevel();
        }
      };
      DefaultTableColumnController<DeedDescription,Integer> minLevelColumn=new DefaultTableColumnController<DeedDescription,Integer>(DeedColumnIds.REQUIRED_LEVEL.name(),"Min Level",Integer.class,minLevelCell);
      minLevelColumn.setWidthSpecs(40,40,40);
      ret.add(minLevelColumn);
    }
    // Rewards
    ret.addAll(buildRewardsColumns());
    // Objectives column
    {
      CellDataProvider<DeedDescription,String> objectivesCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          return deed.getObjectives();
        }
      };
      DefaultTableColumnController<DeedDescription,String> objectivesColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.OBJECTIVES.name(),"Objectives",String.class,objectivesCell);
      objectivesColumn.setWidthSpecs(100,-1,100);
      ret.add(objectivesColumn);
    }
    return ret;
  }

  private static List<DefaultTableColumnController<DeedDescription,?>> buildRewardsColumns()
  {
    List<DefaultTableColumnController<DeedDescription,?>> ret=new ArrayList<DefaultTableColumnController<DeedDescription,?>>();
    // LOTRO points column
    {
      CellDataProvider<DeedDescription,Integer> lpCell=new CellDataProvider<DeedDescription,Integer>()
      {
        @Override
        public Integer getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          int lotroPoints=rewards.getLotroPoints();
          return (lotroPoints>0)?Integer.valueOf(lotroPoints):null;
        }
      };
      DefaultTableColumnController<DeedDescription,Integer> lpColumn=new DefaultTableColumnController<DeedDescription,Integer>(DeedColumnIds.LOTRO_POINTS.name(),"LOTRO Points",Integer.class,lpCell);
      lpColumn.setWidthSpecs(40,40,40);
      ret.add(lpColumn);
    }
    // Destiny points column
    {
      CellDataProvider<DeedDescription,Integer> dpCell=new CellDataProvider<DeedDescription,Integer>()
      {
        @Override
        public Integer getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          int destinyPoints=rewards.getDestinyPoints();
          return (destinyPoints>0)?Integer.valueOf(destinyPoints):null;
        }
      };
      DefaultTableColumnController<DeedDescription,Integer> dpColumn=new DefaultTableColumnController<DeedDescription,Integer>(DeedColumnIds.DESTINY_POINTS.name(),"Destiny Points",Integer.class,dpCell);
      dpColumn.setWidthSpecs(40,40,40);
      ret.add(dpColumn);
    }
    // Class point column
    {
      CellDataProvider<DeedDescription,Boolean> cpCell=new CellDataProvider<DeedDescription,Boolean>()
      {
        @Override
        public Boolean getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          int classPoints=rewards.getClassPoints();
          return (classPoints>0)?Boolean.TRUE:Boolean.FALSE;
        }
      };
      DefaultTableColumnController<DeedDescription,Boolean> cpColumn=new DefaultTableColumnController<DeedDescription,Boolean>(DeedColumnIds.CLASS_POINT.name(),"Class Point",Boolean.class,cpCell);
      cpColumn.setWidthSpecs(40,40,40);
      ret.add(cpColumn);
    }
    // Title column
    {
      CellDataProvider<DeedDescription,String> titleCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          Title[] titles=rewards.getTitles();
          return ((titles!=null) && (titles.length>0))?titles[0].getName():null;
        }
      };
      DefaultTableColumnController<DeedDescription,String> titleColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.TITLE.name(),"Title",String.class,titleCell);
      titleColumn.setWidthSpecs(100,300,200);
      ret.add(titleColumn);
    }
    // Virtue column
    {
      CellDataProvider<DeedDescription,String> virtueCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          Virtue[] virtues=rewards.getVirtues();
          return ((virtues!=null) && (virtues.length>0))?virtues[0].getIdentifier().getLabel():null;
        }
      };
      DefaultTableColumnController<DeedDescription,String> virtueColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.VIRTUE.name(),"Virtue",String.class,virtueCell);
      virtueColumn.setWidthSpecs(100,300,200);
      ret.add(virtueColumn);
    }
    // Emote column
    {
      CellDataProvider<DeedDescription,String> emoteCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          Emote[] emotes=rewards.getEmotes();
          return ((emotes!=null) && (emotes.length>0))?emotes[0].getName():null;
        }
      };
      DefaultTableColumnController<DeedDescription,String> emoteColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.EMOTE.name(),"Emote",String.class,emoteCell);
      emoteColumn.setWidthSpecs(100,300,200);
      ret.add(emoteColumn);
    }
    // Trait column
    {
      CellDataProvider<DeedDescription,String> traitCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          Trait[] traits=rewards.getTraits();
          return ((traits!=null) && (traits.length>0))?traits[0].getName():null;
        }
      };
      DefaultTableColumnController<DeedDescription,String> traitColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.TRAIT.name(),"Trait",String.class,traitCell);
      traitColumn.setWidthSpecs(100,300,200);
      ret.add(traitColumn);
    }
    // Skill column
    {
      CellDataProvider<DeedDescription,String> skillCell=new CellDataProvider<DeedDescription,String>()
      {
        @Override
        public String getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          Skill[] skills=rewards.getSkills();
          return ((skills!=null) && (skills.length>0))?skills[0].getName():null;
        }
      };
      DefaultTableColumnController<DeedDescription,String> skillColumn=new DefaultTableColumnController<DeedDescription,String>(DeedColumnIds.SKILL.name(),"Skill",String.class,skillCell);
      skillColumn.setWidthSpecs(100,300,200);
      ret.add(skillColumn);
    }
    return ret;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=null;
    if (_prefs!=null)
    {
      columnIds=_prefs.getStringList(ItemChoiceWindowController.COLUMNS_PROPERTY);
    }
    if (columnIds==null)
    {
      columnIds=new ArrayList<String>();
      columnIds.add(DeedColumnIds.NAME.name());
      columnIds.add(DeedColumnIds.TYPE.name());
      columnIds.add(DeedColumnIds.CATEGORY.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<DeedDescription> getTableController()
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
   * Get the total number of deeds.
   * @return A number of deeds.
   */
  public int getNbItems()
  {
    return _deeds.size();
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
    _deeds.clear();
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

  /**
   * Refresh table.
   * @param deed Deed to refresh.
   */
  public void refresh(DeedDescription deed)
  {
    if (_table!=null)
    {
      _tableController.refresh(deed);
    }
  }

  private void init()
  {
    reset();
    DeedsManager manager=DeedsManager.getInstance();
    List<DeedDescription> deeds=manager.getAll();
    for(DeedDescription deed : deeds)
    {
      _deeds.add(deed);
    }
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
      _prefs.setStringList(ItemChoiceWindowController.COLUMNS_PROPERTY,columnIds);
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
    _deeds=null;
  }
}
