package delta.games.lotro.gui.stats.warbands;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconUtils;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.lore.warbands.WarbandDefinition;
import delta.games.lotro.lore.warbands.WarbandFilter;
import delta.games.lotro.lore.warbands.WarbandsRegistry;
import delta.games.lotro.stats.warbands.MultipleToonsWarbandsStats;
import delta.games.lotro.stats.warbands.WarbandStats;
import delta.games.lotro.stats.warbands.WarbandsStats;
import delta.games.lotro.utils.Formats;

/**
 * Controller for a table that shows all warbands.
 * @author DAM
 */
public class WarbandsTableController
{
  // Data
  private MultipleToonsWarbandsStats _stats;
  private List<CharacterFile> _toons;
  private WarbandDefinition[] _warbands;
  private WarbandFilter _filter;
  // GUI
  private JTable _table;
  private WarbandsTableModel _model;
  private TableRowSorter<WarbandsTableModel> _sorter;
  private RowFilter<WarbandsTableModel,Integer> _guiFilter;
  // - map toon IDs to a map warband ID -> stats panel
  private HashMap<String,HashMap<String,JPanel>> _cellPanels;
  // - map toon IDs to toon header panels
  private HashMap<String,JPanel> _headerPanels;
  // - map warband ID -> warband panel
  private HashMap<String,JPanel> _warbandPanels;

  /**
   * Constructor.
   * @param stats Underlying warbands statistics.
   * @param filter Warband filter.
   */
  public WarbandsTableController(MultipleToonsWarbandsStats stats, WarbandFilter filter)
  {
    _stats=stats;
    _filter=filter;
    WarbandsRegistry registry=WarbandsRegistry.getWarbandsRegistry();
    _warbands=registry.getAllWarbands();
    init();
    refresh();
  }

  private void init()
  {
    _cellPanels=new HashMap<String,HashMap<String,JPanel>>();
    _headerPanels=new HashMap<String,JPanel>();
    JPanel emptyHeaderPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    _headerPanels.put("",emptyHeaderPanel);
    _warbandPanels=new HashMap<String,JPanel>();
    
    for(WarbandDefinition warband : _warbands)
    {
      JPanel warbandPanel=buildWarbandPanel(warband);
      String warbandID=warband.getName();
      _warbandPanels.put(warbandID,warbandPanel);
    }
  }

  /**
   * Refresh toons table.
   */
  public void refresh()
  {
    if (_toons!=null)
    {
      for(CharacterFile toon : _toons)
      {
        removeToon(toon);
      }
    }
    _toons=_stats.getToonsList();
    for(CharacterFile toon : _toons)
    {
      addToon(toon);
    }
    if (_table!=null)
    {
      _model.fireTableStructureChanged();
      configureTable(_table);
    }
  }

  private void addToon(CharacterFile toon)
  {
    String toonID=toon.getIdentifier();
    JPanel toonHeaderPanel=buildToonHeaderPanel(toon);
    _headerPanels.put(toonID,toonHeaderPanel);
    WarbandsStats stats=_stats.getStatsForToon(toonID);
    HashMap<String,JPanel> toonStatsPanels=new HashMap<String,JPanel>();
    for(WarbandDefinition warband : _warbands)
    {
      WarbandStats warbandStats=stats.getWarbandStats(warband,true);
      JPanel toonPanel=buildToonPanel(toon,warbandStats);
      String warbandID=warband.getName();
      toonStatsPanels.put(warbandID,toonPanel);
    }
    _cellPanels.put(toonID,toonStatsPanels);
  }

  private void removeToon(CharacterFile toon)
  {
    String toonID=toon.getIdentifier();
    JPanel panel=_headerPanels.remove(toonID);
    if (panel!=null)
    {
      panel.removeAll();
    }
    HashMap<String,JPanel> panels=_cellPanels.remove(toonID);
    if (panels!=null)
    {
      for(JPanel cellPanel : panels.values())
      {
        cellPanel.removeAll();
      }
    }
  }

  private JPanel buildToonHeaderPanel(CharacterFile toon)
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Class icon
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ImageIcon classIcon=null;
    Character character=toon.getLastCharacterInfo();
    if (character!=null)
    {
      CharacterClass cClass=character.getCharacterClass();
      classIcon=IconUtils.getClassIcon(cClass,IconUtils.MEDIUM_SIZE);
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
    JLabel nameLabel=GuiFactory.buildLabel(name,24.0f);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(nameLabel,c);
    return panel;
  }

  private JPanel buildToonPanel(CharacterFile toon, WarbandStats stats)
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
      lastDateStr="never";
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
      nbTimesStr="1 time";
    }
    else
    {
      nbTimesStr=nbTimes+" times";
    }
    JPanel textPanel=GuiFactory.buildPanel(new GridBagLayout());
    // Last date
    JLabel lastDateLabel=GuiFactory.buildLabel(lastDateStr,24.0f);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    textPanel.add(lastDateLabel,c);
    // Number of times
    if (nbTimesStr!=null)
    {
      JLabel nbTimesLabel=GuiFactory.buildLabel(nbTimesStr,18.0f);
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
    JLabel regionLabel=GuiFactory.buildLabel(region,18.0f);
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    textPanel.add(regionLabel,c);
    // Level
    Integer level=warband.getLevel();
    JLabel levelLabel=GuiFactory.buildLabel(String.valueOf(level),18.0f);
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
    if (_table==null)
    {
      _table=build();
    }
    return _table;
  }

  private int getNumberOfColumns()
  {
    int nbToons=_stats.getNumberOfToons();
    return 1+nbToons;
  }

  private JTable build()
  {
    JTable statsTable=GuiFactory.buildTable();

    _model=new WarbandsTableModel();
    statsTable.setModel(_model);

    configureTable(statsTable);
    return statsTable;
  }

  private void configureTable(final JTable statsTable)
  {
    int nbColumns=getNumberOfColumns();
    statsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    statsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    statsTable.setShowGrid(false);
    statsTable.getTableHeader().setReorderingAllowed(false);
    TableCellRenderer statCellRenderer=new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        JPanel panel;
        column=table.getColumnModel().getColumn(column).getModelIndex();
        if (column==0)
        {
          WarbandDefinition warband=(WarbandDefinition)value;
          panel=_warbandPanels.get(warband.getName());
          int height=panel.getPreferredSize().height;
          table.setRowHeight(row,height);
        }
        else
        {
          CharacterFile toon=_toons.get(column-1);
          String toonID=toon.getIdentifier();
          WarbandStats stats=(WarbandStats)value;
          WarbandDefinition warband=stats.getDefinition();
          HashMap<String,JPanel> warbandsPanels=_cellPanels.get(toonID);
          panel=warbandsPanels.get(warband.getName());
        }
        return panel;
      }
    };
    TableCellRenderer headerCellRenderer=new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        String id="";
        column=table.getColumnModel().getColumn(column).getModelIndex();
        if (column>0)
        {
          CharacterFile toon=_toons.get(column-1);
          id=toon.getIdentifier();
        }
        JPanel headerPanel=_headerPanels.get(id);
        return headerPanel;
      }
    };
    for(int i=0;i<nbColumns;i++)
    {
      TableColumn column=statsTable.getColumnModel().getColumn(i);
      column.setCellRenderer(statCellRenderer);
      column.setHeaderRenderer(headerCellRenderer);
      if (i>0)
      {
        CharacterFile toon=_toons.get(i-1);
        String toonID=toon.getIdentifier();
        JPanel headerPanel=_headerPanels.get(toonID);
        int minWidth=headerPanel.getPreferredSize().width;
        column.setMinWidth(minWidth);
        column.setPreferredWidth(minWidth);
      }
     }

    // Column size
    int warbandColumnWidth=0;
    for(JPanel warbandPanel : _warbandPanels.values())
    {
      int width=warbandPanel.getPreferredSize().width;
      warbandColumnWidth=Math.max(warbandColumnWidth,width);
    }
    TableColumn warbandColumn=statsTable.getColumnModel().getColumn(0);
    warbandColumn.setMinWidth(warbandColumnWidth+10);
    warbandColumn.setPreferredWidth(warbandColumnWidth+10);
    
    // Sorting
    _guiFilter=new RowFilter<WarbandsTableModel,Integer>()
    {
      @Override
      public boolean include(RowFilter.Entry<? extends WarbandsTableModel,? extends Integer> entry)
      {
        Integer id=entry.getIdentifier();
        WarbandDefinition warband=_warbands[id.intValue()];
        boolean ret=_filter.filterItem(warband);
        return ret;
      }
    };
    _sorter=new TableRowSorter<WarbandsTableModel>(_model);
    _sorter.setRowFilter(_guiFilter);
    
    // Comparators setup
    Comparator<WarbandStats> statsComparator=new Comparator<WarbandStats>() {
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
    Comparator<WarbandDefinition> warbandComparator=new Comparator<WarbandDefinition>() {
      public int compare(WarbandDefinition w1, WarbandDefinition w2)
      {
        String n1=w1.getSafeShortName();
        String n2=w2.getSafeShortName();
        return n1.compareTo(n2);
      }
    };
    for(int i=0;i<nbColumns;i++)
    {
      _sorter.setSortable(i,true);
      if (i>0)
      {
        _sorter.setComparator(i,statsComparator);
      }
      else
      {
        _sorter.setComparator(0,warbandComparator);
      }
    }
    _sorter.setMaxSortKeys(1);
    statsTable.setRowSorter(_sorter);
    //statsTable.setPreferredScrollableViewportSize(new Dimension(800,600));
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _sorter.setRowFilter(_guiFilter);
  }

  private class WarbandsTableModel extends AbstractTableModel
  {
    /**
     * Constructor.
     */
    public WarbandsTableModel()
    {
    }

    /**
     * Get the number of columns.
     * @see javax.swing.table.AbstractTableModel#getColumnCount()
     */
    public int getColumnCount()
    {
      return getNumberOfColumns();
    }

    /**
     * Get the number of rows.
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
      return _warbands.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
      if (columnIndex==0) return WarbandDefinition.class;
      return WarbandStats.class;
    }

    /**
     * Get the value of a cell.
     * @param rowIndex Index of targeted row.
     * @param columnIndex Index of targeted column.
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
      WarbandDefinition warband=_warbands[rowIndex];
      if (columnIndex==0)
      {
        return warband;
      }
      CharacterFile toon=_toons.get(columnIndex-1);
      String toonID=toon.getIdentifier();
      WarbandsStats warbandsStats=_stats.getStatsForToon(toonID);
      WarbandStats warbandStats=warbandsStats.getWarbandStats(warband);
      return warbandStats;
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    _table=null;
    _model=null;
    _sorter=null;
    _guiFilter=null;
    _cellPanels=null;
    _headerPanels=null;
    _warbandPanels=null;
    // Data
    _stats=null;
    _toons=null;
    _warbands=null;
    _filter=null;
  }
}
