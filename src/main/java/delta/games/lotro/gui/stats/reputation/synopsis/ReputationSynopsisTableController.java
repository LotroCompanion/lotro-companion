package delta.games.lotro.gui.stats.reputation.synopsis;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.reputation.FactionData;
import delta.games.lotro.character.reputation.ReputationData;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionFilter;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.lore.reputation.FactionLevelComparator;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Controller for a table that shows reputation for several toons.
 * @author DAM
 */
public class ReputationSynopsisTableController
{
  // Data
  private List<CharacterFile> _toons;
  private FactionFilter _filter;
  // GUI
  private GenericTableController<Faction> _table;

  /**
   * Constructor.
   * @param filter Faction filter.
   */
  public ReputationSynopsisTableController(FactionFilter filter)
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

  private DataProvider<Faction> buildDataProvider()
  {
    FactionsRegistry registry=FactionsRegistry.getInstance();
    List<Faction> factions=registry.getAll();
    DataProvider<Faction> ret=new ListDataProvider<Faction>(factions);
    return ret;
  }

  private GenericTableController<Faction> buildTable()
  {
    DataProvider<Faction> provider=buildDataProvider();
    GenericTableController<Faction> table=new GenericTableController<Faction>(provider);
    table.setFilter(_filter);
    TableColumnController<Faction,String> factionsColumn=buildFactionsColumn();
    table.addColumnController(factionsColumn);
    return table;
  }

  private TableColumnController<Faction,String> buildFactionsColumn()
  {
    CellDataProvider<Faction,String> cell=new CellDataProvider<Faction,String>()
    {
      public String getData(Faction item)
      {
        return item.getName();
      }
    };
    TableColumnController<Faction,String> column=new TableColumnController<Faction,String>("Factions",String.class,cell);

    // Init panels
    column.setMinWidth(200);
    column.setPreferredWidth(200);

    // Header renderer
    JPanel emptyHeaderPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(emptyHeaderPanel);
    column.setHeaderCellRenderer(headerRenderer);

    return column;
  }

  private TableCellRenderer buildStatCellRenderer()
  {
    final JLabel label=GuiFactory.buildLabel("");
    TableCellRenderer renderer=new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        FactionData data=(FactionData)value;
        configureFactionLabel(label,data);
        return label;
      }
    };
    return renderer;
  }

  private TableCellRenderer buildSimpleCellRenderer(final JPanel panel)
  {
    TableCellRenderer renderer=new TableCellRenderer()
    {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        return panel;
      }
    };
    return renderer;
  }

  private TableColumnController<Faction,FactionData> buildCharacterColumn(CharacterFile character)
  {
    final CharacterFile toon=character;
    CellDataProvider<Faction,FactionData> cell=new CellDataProvider<Faction,FactionData>()
    {
      public FactionData getData(Faction item)
      {
        ReputationData data=toon.getReputation();
        return data.getOrCreateFactionStat(item);
      }
    };
    String id=character.getIdentifier();
    TableColumnController<Faction,FactionData> column=new TableColumnController<Faction,FactionData>(id,"Faction",FactionData.class,cell);

    // Cell renderer
    TableCellRenderer renderer=buildStatCellRenderer();
    column.setCellRenderer(renderer);
    // Header renderer
    JPanel headerPanel=buildToonHeaderPanel(character);
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(headerPanel);
    column.setHeaderCellRenderer(headerRenderer);
    int minWidth=headerPanel.getPreferredSize().width;
    column.setMinWidth(minWidth);
    column.setPreferredWidth(minWidth);

    // Comparator
    final FactionLevelComparator factionLevelComparator=new FactionLevelComparator();
    Comparator<FactionData> statsComparator=new Comparator<FactionData>()
    {
      public int compare(FactionData data1, FactionData data2)
      {
        return factionLevelComparator.compare(data1.getFactionLevel(),data2.getFactionLevel());
      }
    };
    column.setComparator(statsComparator);
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
    TableColumnController<Faction,FactionData> column=buildCharacterColumn(toon);
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
    TableColumnsManager<Faction> mgr=_table.getColumnsManager();
    TableColumnController<Faction,?> column=mgr.getById(id);
    mgr.removeColumn(column);
  }

  private JPanel buildToonHeaderPanel(CharacterFile toon)
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Class icon
    GridBagConstraints c=new GridBagConstraints(0,0,1,2,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ImageIcon classIcon=null;
    CharacterSummary summary=toon.getSummary();
    if (summary!=null)
    {
      CharacterClass cClass=summary.getCharacterClass();
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

  private static final Color[] STEPS_4={
    Color.ORANGE,
    new Color(215,219,0),
    new Color(159,237,0),
    Color.GREEN
  };

  private static final Color[] STEPS_5={
    Color.ORANGE,
    new Color(226,214,0),
    new Color(190,228,0),
    new Color(139,242,0),
    Color.GREEN
  };

  private static final Color[] STEPS_7={
    Color.ORANGE,
    new Color(236,209,0),
    new Color(215,219,0),
    new Color(190,228,0),
    new Color(159,237,0),
    new Color(116,246,0),
    Color.GREEN
  };

  private static final Color[] STEPS_8={
    Color.ORANGE,
    new Color(239,207,0),
    new Color(221,216,0),
    new Color(201,224,0),
    new Color(177,232,0),
    new Color(148,240,0),
    new Color(108,247,0),
    Color.GREEN
  };

  private Color getColorForFactionLevel(Faction faction, FactionLevel level)
  {
    int index=level.getValue();
    if (index==-1) return Color.RED;
    if (index==0) return Color.GRAY;
    FactionLevel[] levels=faction.getLevels();
    int max=levels[levels.length-1].getValue();
    if (max==1) return Color.GREEN;
    // Gradient from orange to green
    if (max==4) return STEPS_4[index-1];
    if (max==5) return STEPS_5[index-1];
    if (max==7) return STEPS_7[index-1];
    if (max==8) return STEPS_8[index-1];
    System.out.println("Unmanaged steps: " + max);
    return Color.WHITE;
  }

  private void configureFactionLabel(JLabel label, FactionData factionData)
  {
    Color backgroundColor=null;
    String text="";
    if (factionData!=null)
    {
      FactionLevel level=factionData.getFactionLevel();
      //backgroundColor=getColor(level);
      backgroundColor=getColorForFactionLevel(factionData.getFaction(),level);
      text=level.getName();
    }
    label.setForeground(Color.BLACK);
    if (backgroundColor!=null)
    {
      label.setOpaque(true);
      backgroundColor=new Color(backgroundColor.getRed(),backgroundColor.getGreen(),backgroundColor.getBlue(),128);
      label.setBackground(backgroundColor);
    }
    else
    {
      label.setOpaque(false);
    }
    label.setText(text);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    Dimension preferredSize=label.getPreferredSize();
    label.setMaximumSize(new Dimension(50,preferredSize.height));
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
