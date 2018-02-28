package delta.games.lotro.gui.deed;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Emote;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.Title;
import delta.games.lotro.common.Virtue;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.DeedsManager;

/**
 * Controller for a table that shows deeds.
 * @author DAM
 */
public class DeedsTableController
{
  /**
   * Double-click action command.
   */
  public static final String DOUBLE_CLICK="double click";
  // Data
  private List<DeedDescription> _deeds;
  // GUI
  private JTable _table;
  private GenericTableController<DeedDescription> _tableController;

  /**
   * Constructor.
   * @param filter Managed filter.
   */
  public DeedsTableController(Filter<DeedDescription> filter)
  {
    _deeds=new ArrayList<DeedDescription>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
  }

  private GenericTableController<DeedDescription> buildTable()
  {
    ListDataProvider<DeedDescription> provider=new ListDataProvider<DeedDescription>(_deeds);
    GenericTableController<DeedDescription> table=new GenericTableController<DeedDescription>(provider);

    // Identifier column
    {
      CellDataProvider<DeedDescription,Integer> idCell=new CellDataProvider<DeedDescription,Integer>()
      {
        public Integer getData(DeedDescription deed)
        {
          return Integer.valueOf(deed.getIdentifier());
        }
      };
      TableColumnController<DeedDescription,Integer> idColumn=new TableColumnController<DeedDescription,Integer>("ID",Integer.class,idCell);
      idColumn.setWidthSpecs(100,100,100);
      table.addColumnController(idColumn);
    }
    // Key column
    {
      CellDataProvider<DeedDescription,String> keyCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          return deed.getKey();
        }
      };
      TableColumnController<DeedDescription,String> keyColumn=new TableColumnController<DeedDescription,String>("Key",String.class,keyCell);
      keyColumn.setWidthSpecs(100,200,200);
      table.addColumnController(keyColumn);
    }
    // Name column
    {
      CellDataProvider<DeedDescription,String> nameCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          deed.getObjectives();
          deed.getDescription();
          deed.getRewards();
          return deed.getName();
        }
      };
      TableColumnController<DeedDescription,String> nameColumn=new TableColumnController<DeedDescription,String>("Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,300,200);
      table.addColumnController(nameColumn);
    }
    // Type column
    {
      CellDataProvider<DeedDescription,DeedType> typeCell=new CellDataProvider<DeedDescription,DeedType>()
      {
        public DeedType getData(DeedDescription deed)
        {
          return deed.getType();
        }
      };
      TableColumnController<DeedDescription,DeedType> typeColumn=new TableColumnController<DeedDescription,DeedType>("Type",DeedType.class,typeCell);
      typeColumn.setWidthSpecs(80,100,80);
      table.addColumnController(typeColumn);
    }
    // Category column
    {
      CellDataProvider<DeedDescription,String> categoryCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          return deed.getCategory();
        }
      };
      TableColumnController<DeedDescription,String> categoryColumn=new TableColumnController<DeedDescription,String>("Category",String.class,categoryCell);
      categoryColumn.setWidthSpecs(80,350,80);
      table.addColumnController(categoryColumn);
    }
    // Class column
    {
      CellDataProvider<DeedDescription,CharacterClass> classCell=new CellDataProvider<DeedDescription,CharacterClass>()
      {
        public CharacterClass getData(DeedDescription deed)
        {
          return deed.getRequiredClass();
        }
      };
      TableColumnController<DeedDescription,CharacterClass> classColumn=new TableColumnController<DeedDescription,CharacterClass>("Class",CharacterClass.class,classCell);
      classColumn.setWidthSpecs(80,100,80);
      table.addColumnController(classColumn);
    }
    // Race column
    {
      CellDataProvider<DeedDescription,Race> raceCell=new CellDataProvider<DeedDescription,Race>()
      {
        public Race getData(DeedDescription deed)
        {
          return deed.getRequiredRace();
        }
      };
      TableColumnController<DeedDescription,Race> raceColumn=new TableColumnController<DeedDescription,Race>("Race",Race.class,raceCell);
      raceColumn.setWidthSpecs(80,100,80);
      table.addColumnController(raceColumn);
    }
    // Min level column
    {
      CellDataProvider<DeedDescription,Integer> minLevelCell=new CellDataProvider<DeedDescription,Integer>()
      {
        public Integer getData(DeedDescription deed)
        {
          return deed.getMinLevel();
        }
      };
      TableColumnController<DeedDescription,Integer> minLevelColumn=new TableColumnController<DeedDescription,Integer>("Min Level",Integer.class,minLevelCell);
      minLevelColumn.setWidthSpecs(40,40,40);
      table.addColumnController(minLevelColumn);
    }
    // Rewards
    initRewardsColumns(table);
    // Objectives column
    {
      CellDataProvider<DeedDescription,String> objectivesCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          return deed.getObjectives();
        }
      };
      TableColumnController<DeedDescription,String> objectivesColumn=new TableColumnController<DeedDescription,String>("Objectives",String.class,objectivesCell);
      objectivesColumn.setWidthSpecs(100,-1,100);
      table.addColumnController(objectivesColumn);
    }
    return table;
  }

  private void initRewardsColumns(GenericTableController<DeedDescription> table)
  {
    // LOTRO points column
    {
      CellDataProvider<DeedDescription,Integer> lpCell=new CellDataProvider<DeedDescription,Integer>()
      {
        public Integer getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          int lotroPoints=rewards.getLotroPoints();
          return (lotroPoints>0)?Integer.valueOf(lotroPoints):null;
        }
      };
      TableColumnController<DeedDescription,Integer> lpColumn=new TableColumnController<DeedDescription,Integer>("LOTRO Points",Integer.class,lpCell);
      lpColumn.setWidthSpecs(40,40,40);
      table.addColumnController(lpColumn);
    }
    // Title column
    {
      CellDataProvider<DeedDescription,String> titleCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          Title[] titles=rewards.getTitles();
          return ((titles!=null) && (titles.length>0))?titles[0].getName():null;
        }
      };
      TableColumnController<DeedDescription,String> titleColumn=new TableColumnController<DeedDescription,String>("Title",String.class,titleCell);
      titleColumn.setWidthSpecs(100,300,200);
      table.addColumnController(titleColumn);
    }
    // Virtue column
    {
      CellDataProvider<DeedDescription,String> virtueCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          Virtue[] virtues=rewards.getVirtues();
          return ((virtues!=null) && (virtues.length>0))?virtues[0].getIdentifier().getLabel():null;
        }
      };
      TableColumnController<DeedDescription,String> virtueColumn=new TableColumnController<DeedDescription,String>("Virtue",String.class,virtueCell);
      virtueColumn.setWidthSpecs(100,300,200);
      table.addColumnController(virtueColumn);
    }
    // Emote column
    {
      CellDataProvider<DeedDescription,String> emoteCell=new CellDataProvider<DeedDescription,String>()
      {
        public String getData(DeedDescription deed)
        {
          Rewards rewards=deed.getRewards();
          Emote[] emotes=rewards.getEmotes();
          return ((emotes!=null) && (emotes.length>0))?emotes[0].getName():null;
        }
      };
      TableColumnController<DeedDescription,String> emoteColumn=new TableColumnController<DeedDescription,String>("Emote",String.class,emoteCell);
      emoteColumn.setWidthSpecs(100,300,200);
      table.addColumnController(emoteColumn);
    }
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
    // GUI
    if (_table!=null)
    {
      _table=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _deeds=null;
  }
}
