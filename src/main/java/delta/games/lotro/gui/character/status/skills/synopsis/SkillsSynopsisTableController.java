package delta.games.lotro.gui.character.status.skills.synopsis;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.status.skills.SkillStatus;
import delta.games.lotro.character.status.skills.SkillsStatusManager;
import delta.games.lotro.character.status.skills.io.SkillsStatusIo;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.skills.form.SkillFilter;
import delta.games.lotro.gui.utils.SharedPanels;

/**
 * Controller for a table that shows skills status for several toons.
 * @author DAM
 */
public class SkillsSynopsisTableController
{
  // Data
  private List<CharacterFile> _toons;
  private List<SkillDescription> _skills;
  private SkillFilter _filter;
  private Map<String,SkillsStatusManager> _cache;

  // GUI
  private GenericTableController<SkillDescription> _table;

  /**
   * Constructor.
   * @param skills Skills to use.
   * @param filter Skill filter.
   */
  public SkillsSynopsisTableController(List<SkillDescription> skills, SkillFilter filter)
  {
    _skills=skills;
    _filter=filter;
    _toons=new ArrayList<CharacterFile>();
    _cache=new HashMap<String,SkillsStatusManager>();
    _table=buildTable();
    configureTable(_table.getTable());
  }

  /**
   * Get the managed toons.
   * @return the managed toons.
   */
  public List<CharacterFile> getToons()
  {
    return _toons;
  }

  private DataProvider<SkillDescription> buildDataProvider()
  {
    Collections.sort(_skills,new NamedComparator());
    DataProvider<SkillDescription> ret=new ListDataProvider<SkillDescription>(_skills);
    return ret;
  }

  private GenericTableController<SkillDescription> buildTable()
  {
    DataProvider<SkillDescription> provider=buildDataProvider();
    GenericTableController<SkillDescription> table=new GenericTableController<SkillDescription>(provider);
    table.setFilter(_filter);
    // Skill icon column
    DefaultTableColumnController<SkillDescription,Icon> iconColumn=buildIconColumn();
    table.addColumnController(iconColumn);
    // Skill name column
    DefaultTableColumnController<SkillDescription,String> skillNameColumn=buildSkillNameColumn();
    table.addColumnController(skillNameColumn);
    return table;
  }

  private DefaultTableColumnController<SkillDescription,String> buildSkillNameColumn()
  {
    CellDataProvider<SkillDescription,String> cell=new CellDataProvider<SkillDescription,String>()
    {
      @Override
      public String getData(SkillDescription skill)
      {
        return skill.getName();
      }
    };
    DefaultTableColumnController<SkillDescription,String> column=new DefaultTableColumnController<SkillDescription,String>("Skills",String.class,cell);

    // Init panels
    column.setMinWidth(200);
    column.setPreferredWidth(200);

    // Header renderer
    JPanel emptyHeaderPanel=GuiFactory.buildPanel(new GridBagLayout());
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(emptyHeaderPanel);
    column.setHeaderCellRenderer(headerRenderer);

    return column;
  }

  private TableCellRenderer buildSimpleCellRenderer(final JPanel panel)
  {
    TableCellRenderer renderer=new TableCellRenderer()
    {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        return panel;
      }
    };
    return renderer;
  }

  /**
   * Build a column for the icon of a skill.
   * @return a column.
   */
  private DefaultTableColumnController<SkillDescription,Icon> buildIconColumn()
  {
    CellDataProvider<SkillDescription,Icon> iconCell=new CellDataProvider<SkillDescription,Icon>()
    {
      @Override
      public Icon getData(SkillDescription item)
      {
        return LotroIconsManager.getSkillIcon(item.getIconId());
      }
    };
    DefaultTableColumnController<SkillDescription,Icon> iconColumn=new DefaultTableColumnController<SkillDescription,Icon>("Icon",Icon.class,iconCell); // I18n
    iconColumn.setWidthSpecs(50,50,50);
    iconColumn.setSortable(false);
    // Header renderer
    JPanel emptyHeaderPanel=GuiFactory.buildPanel(new GridBagLayout());
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(emptyHeaderPanel);
    iconColumn.setHeaderCellRenderer(headerRenderer);
    return iconColumn;
  }

  private DefaultTableColumnController<SkillDescription,SkillStatus> buildCharacterColumn(final CharacterFile character)
  {
    CellDataProvider<SkillDescription,SkillStatus> cell=new CellDataProvider<SkillDescription,SkillStatus>()
    {
      @Override
      public SkillStatus getData(SkillDescription item)
      {
        SkillsStatusManager statusMgr=getSkillsStatusForToon(character);
        return statusMgr.get(item,true);
      }
    };
    String id=character.getIdentifier();
    DefaultTableColumnController<SkillDescription,SkillStatus> column=new DefaultTableColumnController<SkillDescription,SkillStatus>(id,"Skill",SkillStatus.class,cell); // I18n

    // Cell renderer
    TableCellRenderer renderer=new SkillStatusCellRenderer();
    column.setCellRenderer(renderer);
    // Header renderer
    JPanel headerPanel=SharedPanels.buildToonHeaderPanel(character);
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(headerPanel);
    column.setHeaderCellRenderer(headerRenderer);
    int minWidth=headerPanel.getPreferredSize().width;
    column.setMinWidth(minWidth);
    column.setPreferredWidth(minWidth);

    // Comparator
    Comparator<SkillStatus> stateComparator=new Comparator<SkillStatus>()
    {
      @Override
      public int compare(SkillStatus data1, SkillStatus data2)
      {
        return Boolean.compare(data1.isAvailable(),data2.isAvailable());
      }
    };
    column.setComparator(stateComparator);
    return column;
  }

  /**
   * Set the displayed toons.
   * @param toons Toons to display.
   */
  public void setToons(List<CharacterFile> toons)
  {
    for(CharacterFile toon : _toons)
    {
      removeToon(toon);
    }
    _toons.clear();
    _toons.addAll(toons);
    for(CharacterFile toon : _toons)
    {
      addToon(toon);
    }
    _table.updateColumns();
  }

  private void addToon(CharacterFile toon)
  {
    DefaultTableColumnController<SkillDescription,SkillStatus> column=buildCharacterColumn(toon);
    _table.addColumnController(column);
  }

  /**
   * Update data for a toon.
   * @param toon Targeted toon.
   */
  public void updateToon(CharacterFile toon)
  {
    _table.refresh();
  }

  private void removeToon(CharacterFile toon)
  {
    String id=toon.getIdentifier();
    TableColumnsManager<SkillDescription> mgr=_table.getColumnsManager();
    TableColumnController<SkillDescription,?> column=mgr.getById(id);
    mgr.removeColumn(column);
  }

  private SkillsStatusManager getSkillsStatusForToon(CharacterFile character)
  {
    String key=character.getIdentifier();
    SkillsStatusManager ret=_cache.get(key);
    if (ret==null)
    {
      ret=SkillsStatusIo.load(character);
      _cache.put(key,ret);
    }
    return ret;
  }

  /**
   * Get the managed generic table.
   * @return the managed generic table.
   */
  public GenericTableController<SkillDescription> getGenericTable()
  {
    return _table;
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    return _table.getTable();
  }

  private void configureTable(final JTable statsTable)
  {
    statsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    statsTable.setShowGrid(false);
    statsTable.getTableHeader().setReorderingAllowed(false);
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _table.filterUpdated();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_table!=null)
    {
      _table.dispose();
      _table=null;
    }
    // Data
    _toons=null;
    _filter=null;
    _cache=null;
  }
}
