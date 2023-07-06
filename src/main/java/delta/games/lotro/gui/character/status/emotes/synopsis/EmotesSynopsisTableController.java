package delta.games.lotro.gui.character.status.emotes.synopsis;

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
import delta.games.lotro.character.status.emotes.EmoteStatus;
import delta.games.lotro.character.status.emotes.EmotesStatusManager;
import delta.games.lotro.character.status.emotes.io.EmotesStatusIo;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.emotes.EmoteFilter;
import delta.games.lotro.gui.utils.SharedPanels;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.emotes.EmotesManager;

/**
 * Controller for a table that shows emotes for several toons.
 * @author DAM
 */
public class EmotesSynopsisTableController
{
  // Data
  private List<CharacterFile> _toons;
  private EmoteFilter _filter;
  private Map<String,EmotesStatusManager> _cache;

  // GUI
  private GenericTableController<EmoteDescription> _table;

  /**
   * Constructor.
   * @param filter Emote filter.
   */
  public EmotesSynopsisTableController(EmoteFilter filter)
  {
    _filter=filter;
    _toons=new ArrayList<CharacterFile>();
    _cache=new HashMap<String,EmotesStatusManager>();
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

  private DataProvider<EmoteDescription> buildDataProvider()
  {
    EmotesManager emotesMgr=EmotesManager.getInstance();
    List<EmoteDescription> emotes=emotesMgr.getAll();
    Collections.sort(emotes,new NamedComparator());
    DataProvider<EmoteDescription> ret=new ListDataProvider<EmoteDescription>(emotes);
    return ret;
  }

  private GenericTableController<EmoteDescription> buildTable()
  {
    DataProvider<EmoteDescription> provider=buildDataProvider();
    GenericTableController<EmoteDescription> table=new GenericTableController<EmoteDescription>(provider);
    table.setFilter(_filter);
    // Emote icon column
    DefaultTableColumnController<EmoteDescription,Icon> iconColumn=buildIconColumn();
    table.addColumnController(iconColumn);
    // Emote name column
    DefaultTableColumnController<EmoteDescription,String> emotesColumn=buildEmoteColumn();
    table.addColumnController(emotesColumn);
    return table;
  }

  private DefaultTableColumnController<EmoteDescription,String> buildEmoteColumn()
  {
    CellDataProvider<EmoteDescription,String> cell=new CellDataProvider<EmoteDescription,String>()
    {
      @Override
      public String getData(EmoteDescription emote)
      {
        return emote.getName();
      }
    };
    DefaultTableColumnController<EmoteDescription,String> column=new DefaultTableColumnController<EmoteDescription,String>("Emotes",String.class,cell); // I18n

    // Init panels
    column.setMinWidth(100);
    column.setPreferredWidth(100);

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
   * Build a column for the icon of an emote.
   * @return a column.
   */
  private DefaultTableColumnController<EmoteDescription,Icon> buildIconColumn()
  {
    CellDataProvider<EmoteDescription,Icon> iconCell=new CellDataProvider<EmoteDescription,Icon>()
    {
      @Override
      public Icon getData(EmoteDescription item)
      {
        return LotroIconsManager.getEmoteIcon(item.getIconId());
      }
    };
    DefaultTableColumnController<EmoteDescription,Icon> iconColumn=new DefaultTableColumnController<EmoteDescription,Icon>("Icon",Icon.class,iconCell);
    iconColumn.setWidthSpecs(50,50,50);
    iconColumn.setSortable(false);
    // Header renderer
    JPanel emptyHeaderPanel=GuiFactory.buildPanel(new GridBagLayout());
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(emptyHeaderPanel);
    iconColumn.setHeaderCellRenderer(headerRenderer);
    return iconColumn;
  }

  private DefaultTableColumnController<EmoteDescription,EmoteStatus> buildCharacterColumn(final CharacterFile character)
  {
    CellDataProvider<EmoteDescription,EmoteStatus> cell=new CellDataProvider<EmoteDescription,EmoteStatus>()
    {
      @Override
      public EmoteStatus getData(EmoteDescription item)
      {
        EmotesStatusManager statusMgr=getEmotesStatusForToon(character);
        return statusMgr.get(item,true);
      }
    };
    String id=character.getIdentifier();
    DefaultTableColumnController<EmoteDescription,EmoteStatus> column=new DefaultTableColumnController<EmoteDescription,EmoteStatus>(id,"Emote",EmoteStatus.class,cell);

    // Cell renderer
    TableCellRenderer renderer=new EmoteStatusCellRenderer();
    column.setCellRenderer(renderer);
    // Header renderer
    JPanel headerPanel=SharedPanels.buildToonHeaderPanel(character);
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(headerPanel);
    column.setHeaderCellRenderer(headerRenderer);
    int minWidth=headerPanel.getPreferredSize().width;
    column.setMinWidth(minWidth);
    column.setPreferredWidth(minWidth);

    // Comparator
    Comparator<EmoteStatus> stateComparator=new Comparator<EmoteStatus>()
    {
      @Override
      public int compare(EmoteStatus data1, EmoteStatus data2)
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
    DefaultTableColumnController<EmoteDescription,EmoteStatus> column=buildCharacterColumn(toon);
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
    TableColumnsManager<EmoteDescription> mgr=_table.getColumnsManager();
    TableColumnController<EmoteDescription,?> column=mgr.getById(id);
    mgr.removeColumn(column);
  }

  private EmotesStatusManager getEmotesStatusForToon(CharacterFile character)
  {
    String key=character.getIdentifier();
    EmotesStatusManager ret=_cache.get(key);
    if (ret==null)
    {
      ret=EmotesStatusIo.load(character);
      _cache.put(key,ret);
    }
    return ret;
  }

  /**
   * Get the managed generic table.
   * @return the managed generic table.
   */
  public GenericTableController<EmoteDescription> getGenericTable()
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
