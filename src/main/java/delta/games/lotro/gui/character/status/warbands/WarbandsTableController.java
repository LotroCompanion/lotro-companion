package delta.games.lotro.gui.character.status.warbands;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.status.warbands.WarbandStats;
import delta.games.lotro.character.status.warbands.WarbandsStats;
import delta.games.lotro.gui.utils.SharedPanels;
import delta.games.lotro.lore.warbands.WarbandDefinition;
import delta.games.lotro.lore.warbands.WarbandFilter;
import delta.games.lotro.lore.warbands.WarbandsRegistry;
import delta.games.lotro.utils.Formats;

/**
 * Controller for a table that shows all warbands.
 * @author DAM
 */
public class WarbandsTableController
{
  // Data
  private List<CharacterFile> _toons;
  private WarbandFilter _filter;
  // GUI
  private GenericTableController<WarbandDefinition> _table;

  /**
   * Constructor.
   * @param filter Warband filter.
   */
  public WarbandsTableController(WarbandFilter filter)
  {
    _filter=filter;
    _toons=new ArrayList<CharacterFile>();
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

  private DataProvider<WarbandDefinition> buildDataProvider()
  {
    WarbandsRegistry registry=WarbandsRegistry.getWarbandsRegistry();
    WarbandDefinition[] warbands=registry.getAllWarbands();
    List<WarbandDefinition> warbandsList=Arrays.asList(warbands);
    DataProvider<WarbandDefinition> ret=new ListDataProvider<WarbandDefinition>(warbandsList);
    return ret;
  }

  private GenericTableController<WarbandDefinition> buildTable()
  {
    DataProvider<WarbandDefinition> provider=buildDataProvider();
    GenericTableController<WarbandDefinition> table=new GenericTableController<WarbandDefinition>(provider);
    table.setFilter(_filter);
    DefaultTableColumnController<WarbandDefinition,WarbandDefinition> warbandsColumn=buildWarbandColumn();
    table.addColumnController(warbandsColumn);
    return table;
  }

  private TableCellRenderer buildWarbandCellRenderer(final HashMap<String,JPanel> warbandPanels)
  {
    TableCellRenderer renderer=new TableCellRenderer()
    {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        WarbandDefinition warband=(WarbandDefinition)value;
        JPanel panel=warbandPanels.get(warband.getName());
        int height=panel.getPreferredSize().height;
        table.setRowHeight(row,height);
        return panel;
      }
    };

    return renderer;
  }

  private DefaultTableColumnController<WarbandDefinition,WarbandDefinition> buildWarbandColumn()
  {
    CellDataProvider<WarbandDefinition,WarbandDefinition> cell=new CellDataProvider<WarbandDefinition,WarbandDefinition>()
    {
      @Override
      public WarbandDefinition getData(WarbandDefinition item)
      {
        return item;
      }
    };
    DefaultTableColumnController<WarbandDefinition,WarbandDefinition> column=new DefaultTableColumnController<WarbandDefinition,WarbandDefinition>("Warbands",WarbandDefinition.class,cell);

    // Init panels
    int warbandColumnWidth=0;
    final HashMap<String,JPanel> warbandPanels=new HashMap<String,JPanel>();
    WarbandsRegistry registry=WarbandsRegistry.getWarbandsRegistry();
    WarbandDefinition[] warbands=registry.getAllWarbands();
    for(WarbandDefinition warband : warbands)
    {
      JPanel warbandPanel=buildWarbandPanel(warband);
      warbandPanels.put(warband.getName(),warbandPanel);
      // Column size
      int width=warbandPanel.getPreferredSize().width;
      warbandColumnWidth=Math.max(warbandColumnWidth,width);
    }
    column.setMinWidth(warbandColumnWidth+10);
    column.setPreferredWidth(warbandColumnWidth+10);

    // Cell renderer
    TableCellRenderer renderer=buildWarbandCellRenderer(warbandPanels);
    column.setCellRenderer(renderer);
    // Header renderer
    JPanel emptyHeaderPanel=GuiFactory.buildPanel(new GridBagLayout());
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(emptyHeaderPanel);
    column.setHeaderCellRenderer(headerRenderer);

    // Comparator
    Comparator<WarbandDefinition> warbandComparator=new Comparator<WarbandDefinition>() {
      @Override
      public int compare(WarbandDefinition w1, WarbandDefinition w2)
      {
        String n1=w1.getSafeShortName();
        String n2=w2.getSafeShortName();
        return n1.compareTo(n2);
      }
    };
    column.setComparator(warbandComparator);

    return column;
  }

  private TableCellRenderer buildStatCellRenderer(final WarbandsStats warbandsStats)
  {
    // Map warband ID -> stats panel
    final Map<String,JPanel> cellPanels=new HashMap<String,JPanel>();
    TableCellRenderer renderer=new TableCellRenderer()
    {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        WarbandStats stats=(WarbandStats)value;
        JPanel panel=null;
        if (stats!=null)
        {
          WarbandDefinition warband=stats.getDefinition();
          panel=cellPanels.get(warband.getName());
          if (panel==null)
          {
            panel=buildStatsPanel(stats);
            cellPanels.put(warband.getName(),panel);
          }
        }
        else
        {
          panel=GuiFactory.buildPanel(new BorderLayout());
        }
        return panel;
      }
    };
    return renderer;
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

  private WarbandsStats loadToonStats(CharacterFile toon)
  {
    CharacterLog log=toon.getLastCharacterLog();
    WarbandsStats stats=new WarbandsStats(log);
    return stats;
  }

  private DefaultTableColumnController<WarbandDefinition,WarbandStats> buildCharacterColumn(CharacterFile character)
  {
    final WarbandsStats warbandsStats=loadToonStats(character);
    CellDataProvider<WarbandDefinition,WarbandStats> cell=new CellDataProvider<WarbandDefinition,WarbandStats>()
    {
      @Override
      public WarbandStats getData(WarbandDefinition item)
      {
        return warbandsStats.getWarbandStats(item,true);
      }
    };
    String id=character.getIdentifier();
    DefaultTableColumnController<WarbandDefinition,WarbandStats> column=new DefaultTableColumnController<WarbandDefinition,WarbandStats>(id,"Stats",WarbandStats.class,cell);

    // Cell renderer
    TableCellRenderer renderer=buildStatCellRenderer(warbandsStats);
    column.setCellRenderer(renderer);
    // Header renderer
    JPanel headerPanel=buildToonHeaderPanel(character);
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(headerPanel);
    column.setHeaderCellRenderer(headerRenderer);
    int minWidth=headerPanel.getPreferredSize().width;
    column.setMinWidth(minWidth);
    column.setPreferredWidth(minWidth);

    // Comparator
    Comparator<WarbandStats> statsComparator=new Comparator<WarbandStats>() {
      @Override
      public int compare(WarbandStats w1, WarbandStats w2)
      {
        Long d1=w1.getMostRecentDate();
        Long d2=w2.getMostRecentDate();
        if (d1==null)
        {
          if (d2==null) return 0;
          return -1;
        }
        if (d2==null) return 1;
        return d1.compareTo(d2);
      }
    };
    column.setComparator(statsComparator);
    return column;
  }

  /**
   * Refresh toons table.
   * @param toons New toon.
   */
  public void refresh(List<CharacterFile> toons)
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
    DefaultTableColumnController<WarbandDefinition,WarbandStats> column=buildCharacterColumn(toon);
    _table.addColumnController(column);
  }

  private void removeToon(CharacterFile toon)
  {
    String id=toon.getIdentifier();
    TableColumnsManager<WarbandDefinition> mgr=_table.getColumnsManager();
    TableColumnController<WarbandDefinition,?> column=mgr.getById(id);
    mgr.removeColumn(column);
  }

  private JPanel buildToonHeaderPanel(CharacterFile toon)
  {
    JPanel panel=SharedPanels.buildToonHeaderPanel(toon);
    // Log update
    Date logDate=toon.getLastLogUpdate();
    String logDateStr="";
    if (logDate!=null)
    {
      logDateStr=Formats.getDateString(logDate);
    }
    JLabel logDateLabel=GuiFactory.buildLabel(logDateStr);
    GridBagConstraints c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(2,2,5,2),0,0);
    panel.add(logDateLabel,c);
    return panel;
  }

  private JPanel buildStatsPanel(WarbandStats stats)
  {
    Long date=null;
    int nbTimes=0;
    if (stats!=null)
    {
      nbTimes=stats.getNumberOfTimes();
      date=stats.getMostRecentDate();
    }

    String lastDateStr;
    if (date==null)
    {
      lastDateStr="never"; // I18n
    }
    else
    {
      lastDateStr=Formats.getDateString(date);
    }
    String nbTimesStr;
    if (nbTimes==0)
    {
      nbTimesStr=null;
    }
    else if (nbTimes==1)
    {
      nbTimesStr="1 time"; // I18n
    }
    else
    {
      nbTimesStr=nbTimes+" times"; // I18n
    }
    JPanel textPanel=GuiFactory.buildPanel(new GridBagLayout());
    // Last date
    JLabel lastDateLabel=GuiFactory.buildLabel(lastDateStr,18.0f);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    textPanel.add(lastDateLabel,c);
    // Number of times
    if (nbTimesStr!=null)
    {
      JLabel nbTimesLabel=GuiFactory.buildLabel(nbTimesStr,14.0f);
      c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      textPanel.add(nbTimesLabel,c);
    }
    return textPanel;
  }

  private JPanel buildWarbandPanel(WarbandDefinition warband)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Icon
    String iconName=warband.getIconName();
    BufferedImage warbandImage=IconsManager.getImage("/resources/gui/warbands/"+iconName+".png");
    if (warbandImage!=null)
    {
      ImageIcon warbandIcon=new ImageIcon(warbandImage);
      panel.add(new JLabel(warbandIcon),c);
    }

    JPanel textPanel=GuiFactory.buildPanel(new GridBagLayout());
    // Name
    String warbandName=warband.getSafeShortName();
    JLabel nameLabel=GuiFactory.buildLabel(warbandName,24.0f);
    c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    textPanel.add(nameLabel,c);
    // Region
    String region=warband.getRegion();
    JLabel regionLabel=GuiFactory.buildLabel(region,12.0f);
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    textPanel.add(regionLabel,c);
    // Level
    Integer level=warband.getLevel();
    JLabel levelLabel=GuiFactory.buildLabel(String.valueOf(level),12.0f);
    c=new GridBagConstraints(0,2,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    textPanel.add(levelLabel,c);

    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(textPanel,c);

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
  }
}
