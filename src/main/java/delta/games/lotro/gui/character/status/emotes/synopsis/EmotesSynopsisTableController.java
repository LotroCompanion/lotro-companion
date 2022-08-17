package delta.games.lotro.gui.character.status.emotes.synopsis;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
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
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.emotes.EmoteStatus;
import delta.games.lotro.character.status.emotes.EmotesStatusManager;
import delta.games.lotro.character.status.emotes.io.EmotesStatusIo;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.emotes.EmoteFilter;
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
    DataProvider<EmoteDescription> ret=new ListDataProvider<EmoteDescription>(emotes);
    return ret;
  }

  private GenericTableController<EmoteDescription> buildTable()
  {
    DataProvider<EmoteDescription> provider=buildDataProvider();
    GenericTableController<EmoteDescription> table=new GenericTableController<EmoteDescription>(provider);
    table.setFilter(_filter);
    DefaultTableColumnController<EmoteDescription,String> factionsColumn=buildEmoteColumn();
    table.addColumnController(factionsColumn);
    return table;
  }

  private DefaultTableColumnController<EmoteDescription,String> buildEmoteColumn()
  {
    CellDataProvider<EmoteDescription,String> cell=new CellDataProvider<EmoteDescription,String>()
    {
      @Override
      public String getData(EmoteDescription item)
      {
        return item.getName();
      }
    };
    DefaultTableColumnController<EmoteDescription,String> column=new DefaultTableColumnController<EmoteDescription,String>("Emotes",String.class,cell);

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

  private DefaultTableColumnController<EmoteDescription,EmoteStatus> buildCharacterColumn(CharacterFile character)
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
    JPanel headerPanel=buildToonHeaderPanel(character);
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(headerPanel);
    column.setHeaderCellRenderer(headerRenderer);
    int minWidth=headerPanel.getPreferredSize().width;
    column.setMinWidth(minWidth);
    column.setPreferredWidth(minWidth);

    // Comparator
    /*
    final FactionLevelComparator factionLevelComparator=new FactionLevelComparator();
    Comparator<FactionStatus> statsComparator=new Comparator<FactionStatus>()
    {
      @Override
      public int compare(FactionStatus data1, FactionStatus data2)
      {
        return factionLevelComparator.compare(data1.getFactionLevel(),data2.getFactionLevel());
      }
    };
    column.setComparator(statsComparator);
    */
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

  private JPanel buildToonHeaderPanel(CharacterFile toon)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Class icon
    GridBagConstraints c=new GridBagConstraints(0,0,1,2,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ImageIcon classIcon=null;
    BasicCharacterAttributes attrs=toon.getSummary();
    if (attrs!=null)
    {
      CharacterClass cClass=attrs.getCharacterClass();
      classIcon=LotroIconsManager.getClassIcon(cClass,LotroIconsManager.COMPACT_SIZE);
    }
    JLabel classLabel;
    if (classIcon!=null)
    {
      classLabel=new JLabel(classIcon);
    }
    else
    {
      classLabel=new JLabel("(class)");
    }
    panel.add(classLabel,c);
    // Toon name
    String name=toon.getName();
    JLabel nameLabel=GuiFactory.buildLabel(name,16.0f);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,2,2,2),0,0);
    panel.add(nameLabel,c);
    return panel;
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
