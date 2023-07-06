package delta.games.lotro.gui.character.status.reputation.synopsis;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AbstractAreaController;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.ui.swing.tables.context.TableContextManager;
import delta.common.ui.swing.tables.renderers.CustomLabelCellRenderer;
import delta.common.ui.swing.tables.renderers.LabelTableCellRenderer;
import delta.common.utils.context.Context;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.reputation.FactionStatus;
import delta.games.lotro.character.status.reputation.ReputationStatus;
import delta.games.lotro.gui.utils.SharedPanels;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionFilter;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.lore.reputation.FactionLevelComparator;
import delta.games.lotro.lore.reputation.FactionsRegistry;
import delta.games.lotro.utils.ContextPropertyNames;
import delta.games.lotro.utils.gui.Gradients;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a table that shows reputation for several toons.
 * @author DAM
 */
public class ReputationSynopsisTableController extends AbstractAreaController
{
  // Data
  private List<CharacterFile> _toons;
  private FactionFilter _filter;
  // GUI
  private GenericTableController<Faction> _table;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param filter Faction filter.
   */
  public ReputationSynopsisTableController(AreaController parent, FactionFilter filter)
  {
    super(parent);
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
    List<Faction> selectedFactions=new ArrayList<Faction>();
    for(Faction faction : factions)
    {
      if (faction.isGuildFaction())
      {
        continue;
      }
      if (faction.getCategory().length()==0)
      {
        continue;
      }
      selectedFactions.add(faction);
    }
    DataProvider<Faction> ret=new ListDataProvider<Faction>(selectedFactions);
    return ret;
  }

  private GenericTableController<Faction> buildTable()
  {
    DataProvider<Faction> provider=buildDataProvider();
    GenericTableController<Faction> table=new GenericTableController<Faction>(provider);
    table.setFilter(_filter);
    DefaultTableColumnController<Faction,String> factionsColumn=buildFactionsColumn();
    table.addColumnController(factionsColumn);
    return table;
  }

  private DefaultTableColumnController<Faction,String> buildFactionsColumn()
  {
    CellDataProvider<Faction,String> cell=new CellDataProvider<Faction,String>()
    {
      @Override
      public String getData(Faction item)
      {
        String rawFactionName=item.getName();
        return ContextRendering.render(ReputationSynopsisTableController.this,rawFactionName);
      }
    };
    DefaultTableColumnController<Faction,String> column=new DefaultTableColumnController<Faction,String>("Factions",String.class,cell); // I18n

    // Init panels
    column.setMinWidth(200);
    column.setPreferredWidth(200);

    // Header renderer
    JPanel emptyHeaderPanel=GuiFactory.buildPanel(new GridBagLayout());
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(emptyHeaderPanel);
    column.setHeaderCellRenderer(headerRenderer);

    return column;
  }

  /**
   * Build a cell renderer for a faction status.
   * @param parentTable Table to use.
   * @return A renderer.
   */
  public static <T> TableCellRenderer buildFactionStatusCellRenderer(GenericTableController<T> parentTable)
  {
    CustomLabelCellRenderer<T,FactionStatus> customRenderer=new CustomLabelCellRenderer<T,FactionStatus>()
    {
      public void configure(GenericTableController<T> table, FactionStatus value, boolean isSelected, boolean hasFocus, int row, int column, JLabel label)
      {
        Context context=table.getContextManager().getContext(row,column);
        configureFactionLabel(label,value,context);
      }
    };
    LabelTableCellRenderer<T,FactionStatus> r=new LabelTableCellRenderer<T,FactionStatus>(parentTable,customRenderer);
    return r;
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

  private DefaultTableColumnController<Faction,FactionStatus> buildCharacterColumn(CharacterFile character, GenericTableController<Faction> table)
  {
    final CharacterFile toon=character;
    CellDataProvider<Faction,FactionStatus> cell=new CellDataProvider<Faction,FactionStatus>()
    {
      @Override
      public FactionStatus getData(Faction item)
      {
        ReputationStatus status=toon.getReputation();
        return status.getOrCreateFactionStat(item);
      }
    };
    String id=character.getIdentifier();
    DefaultTableColumnController<Faction,FactionStatus> column=new DefaultTableColumnController<Faction,FactionStatus>(id,"Faction",FactionStatus.class,cell); // I18n

    // Cell renderer
    TableCellRenderer renderer=buildFactionStatusCellRenderer(table);
    column.setCellRenderer(renderer);
    // Header renderer
    JPanel headerPanel=SharedPanels.buildToonHeaderPanel(character);
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(headerPanel);
    column.setHeaderCellRenderer(headerRenderer);
    int minWidth=headerPanel.getPreferredSize().width;
    column.setMinWidth(minWidth);
    column.setPreferredWidth(minWidth);

    // Comparator
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
    return column;
  }

  /**
   * Set the displayed toons.
   * @param toons Toons to display.
   */
  public void setToons(List<CharacterFile> toons)
  {
    // Cleanup
    for(CharacterFile toon : _toons)
    {
      removeToon(toon);
    }
    _toons.clear();
    TableContextManager contextMgr=_table.getContextManager();
    contextMgr.clear();
    // Add columns
    _toons.addAll(toons);
    int column=1;
    for(CharacterFile toon : _toons)
    {
      addToon(toon);
      contextMgr.setColumnContext(column,ContextPropertyNames.BASE_CHARACTER_SUMMARY,toon.getSummary());
      column++;
    }
    _table.updateColumns();
  }

  private void addToon(CharacterFile toon)
  {
    DefaultTableColumnController<Faction,FactionStatus> column=buildCharacterColumn(toon,_table);
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

  private static Color getColorForFactionLevel(Faction faction, FactionLevel level)
  {
    if (level==null) return Color.GRAY;
    int index=level.getTier();
    // TODO Another way to get color
    if (index==-2) return Color.MAGENTA;
    if (index==-1) return Color.RED;
    if (index==0) return Color.GRAY;
    FactionLevel[] levels=faction.getLevels();
    int max=levels[levels.length-1].getTier();
    if (max==1) return Color.GREEN;
    // Gradient from orange to green
    Color[] gradient=Gradients.getOrangeToGreen(max);
    Color ret=null;
    if (gradient!=null)
    {
      ret=gradient[index-1];
    }
    else
    {
      ret=Color.WHITE;
    }
    return ret;
  }

  private static void configureFactionLabel(JLabel label, FactionStatus factionStatus, Context context)
  {
    Color backgroundColor=null;
    String text="";
    if (factionStatus!=null)
    {
      FactionLevel level=factionStatus.getFactionLevel();
      backgroundColor=getColorForFactionLevel(factionStatus.getFaction(),level);
      if (level!=null)
      {
        text=level.getName();
        text=ContextRendering.render(context,text);
      }
      else
      {
        text="-";
      }
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
    super.dispose();
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
