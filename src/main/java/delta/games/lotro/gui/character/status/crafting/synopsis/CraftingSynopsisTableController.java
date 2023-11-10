package delta.games.lotro.gui.character.status.crafting.synopsis;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.context.TableContextManager;
import delta.common.ui.swing.tables.renderers.CustomLabelCellRenderer;
import delta.common.ui.swing.tables.renderers.LabelTableCellRenderer;
import delta.common.utils.context.Context;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.status.crafting.CraftingStatus;
import delta.games.lotro.character.status.crafting.GuildStatus;
import delta.games.lotro.character.status.crafting.ProfessionStatus;
import delta.games.lotro.character.status.reputation.FactionStatus;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.character.status.reputation.synopsis.ReputationSynopsisTableController;
import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.CraftingLevelTier;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.ProfessionComparator;
import delta.games.lotro.lore.crafting.ProfessionFilter;
import delta.games.lotro.lore.crafting.Professions;
import delta.games.lotro.lore.crafting.Vocation;
import delta.games.lotro.lore.reputation.FactionLevelComparator;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.utils.ContextPropertyNames;
import delta.games.lotro.utils.gui.Gradients;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a table that shows crafting status for several toons.
 * @author DAM
 */
public class CraftingSynopsisTableController
{
  // Data
  private List<CharacterFile> _toons;
  private List<CraftingSynopsisItem> _rowItems;
  private CraftingSynopsisItemFilter _filter;
  // GUI
  private GenericTableController<CraftingSynopsisItem> _table;

  /**
   * Constructor.
   * @param filter Profession filter.
   */
  public CraftingSynopsisTableController(ProfessionFilter filter)
  {
    _filter=new CraftingSynopsisItemFilter(filter);
    _toons=new ArrayList<CharacterFile>();
    _rowItems=new ArrayList<CraftingSynopsisItem>();
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

  private void updateRowItems()
  {
    _rowItems.clear();
    TableContextManager contextMgr=_table.getContextManager();
    contextMgr.clear();
    int row=0;
    CraftingData data=CraftingSystem.getInstance().getData();
    Professions professionsRegistry=data.getProfessionsRegistry();
    for(CharacterFile toon : _toons)
    {
      CraftingStatus craftingStatus=toon.getCraftingMgr().getCraftingStatus();
      Vocation vocation=craftingStatus.getVocation();
      List<Profession> professions=professionsRegistry.getAll();
      for(Profession profession : professions)
      {
        ProfessionStatus professionStatus=craftingStatus.getProfessionStatus(profession,false);
        if (professionStatus==null)
        {
          continue;
        }
        GuildStatus displayedStatus=craftingStatus.getGuildStatus(profession,false);
        CraftingSynopsisItem item=new CraftingSynopsisItem(toon,vocation,professionStatus,displayedStatus);
        _rowItems.add(item);
        // Context
        contextMgr.setRowContext(row,ContextPropertyNames.BASE_CHARACTER_SUMMARY,toon.getSummary());
        row++;
      }
    }
  }

  private DataProvider<CraftingSynopsisItem> buildDataProvider()
  {
    DataProvider<CraftingSynopsisItem> ret=new ListDataProvider<CraftingSynopsisItem>(_rowItems);
    return ret;
  }

  private GenericTableController<CraftingSynopsisItem> buildTable()
  {
    DataProvider<CraftingSynopsisItem> provider=buildDataProvider();
    GenericTableController<CraftingSynopsisItem> table=new GenericTableController<CraftingSynopsisItem>(provider);
    table.setFilter(_filter);
    // Profession icon
    DefaultTableColumnController<CraftingSynopsisItem,Profession> professionColumn=buildProfessionColumn(table);
    table.addColumnController(professionColumn);
    // Character name
    DefaultTableColumnController<CraftingSynopsisItem,String> characterNameColumn=buildCharacterNameColumn();
    table.addColumnController(characterNameColumn);
    // Profession name
    DefaultTableColumnController<CraftingSynopsisItem,String> professionNameColumn=buildProfessionNameColumn();
    table.addColumnController(professionNameColumn);
    // Vocation name
    DefaultTableColumnController<CraftingSynopsisItem,String> vocationNameColumn=buildVocationColumn();
    table.addColumnController(vocationNameColumn);
    // Proficiency
    DefaultTableColumnController<CraftingSynopsisItem,CraftingLevel> proficiencyColumn=buildCraftingTierColumn(table,false);
    table.addColumnController(proficiencyColumn);
    // Mastery
    DefaultTableColumnController<CraftingSynopsisItem,CraftingLevel> masteryColumn=buildCraftingTierColumn(table,true);
    table.addColumnController(masteryColumn);
    // Guild
    DefaultTableColumnController<CraftingSynopsisItem,FactionStatus> guildColumn=buildGuildColumn(table);
    table.addColumnController(guildColumn);
    return table;
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

  private DefaultTableColumnController<CraftingSynopsisItem,String> buildCharacterNameColumn()
  {
    CellDataProvider<CraftingSynopsisItem,String> cell=new CellDataProvider<CraftingSynopsisItem,String>()
    {
      @Override
      public String getData(CraftingSynopsisItem item)
      {
        CharacterFile character=item.getCharacter();
        String name=character.getName();
        return name;
      }
    };
    DefaultTableColumnController<CraftingSynopsisItem,String> column=new DefaultTableColumnController<CraftingSynopsisItem,String>(CraftingSynopsisColumnIds.CHARACTER_NAME.name(),"Name",String.class,cell); // I18n

    // Init widths
    column.setMinWidth(100);
    column.setPreferredWidth(150);
    column.setMaxWidth(300);

    return column;
  }

  private TableCellRenderer buildProfessionCellRenderer(GenericTableController<CraftingSynopsisItem> parentTable)
  {
    CustomLabelCellRenderer<CraftingSynopsisItem,Profession> customRenderer=new CustomLabelCellRenderer<CraftingSynopsisItem,Profession>()
    {
      public void configure(GenericTableController<CraftingSynopsisItem> table, Profession value, boolean isSelected, boolean hasFocus, int row, int column, JLabel label)
      {
        Icon icon=LotroIconsManager.getProfessionIcon(value);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setIcon(icon);
        String rawProfession=value.getName();
        Context context=table.getContextManager().getContext(row,column);
        String profession=ContextRendering.render(context,rawProfession);
        label.setToolTipText(profession);
        label.setText("");
      }
    };
    LabelTableCellRenderer<CraftingSynopsisItem,Profession> r=new LabelTableCellRenderer<CraftingSynopsisItem,Profession>(parentTable,customRenderer);
    return r;
  }

  private DefaultTableColumnController<CraftingSynopsisItem,Profession> buildProfessionColumn(GenericTableController<CraftingSynopsisItem> table)
  {
    CellDataProvider<CraftingSynopsisItem,Profession> professionCell=new CellDataProvider<CraftingSynopsisItem,Profession>()
    {
      @Override
      public Profession getData(CraftingSynopsisItem item)
      {
        return item.getProfession();
      }
    };
    DefaultTableColumnController<CraftingSynopsisItem,Profession> professionColumn=new DefaultTableColumnController<CraftingSynopsisItem,Profession>(CraftingSynopsisColumnIds.PROFESSION_ICON.name(),"",Profession.class,professionCell);
    professionColumn.setWidthSpecs(50,50,50);
    // Header renderer
    JPanel emptyHeaderPanel=GuiFactory.buildPanel(new GridBagLayout());
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(emptyHeaderPanel);
    professionColumn.setHeaderCellRenderer(headerRenderer);
    // Cell renderer
    TableCellRenderer cellRenderer=buildProfessionCellRenderer(table);
    professionColumn.setCellRenderer(cellRenderer);
    // Comparator
    ProfessionComparator comparator=new ProfessionComparator();
    professionColumn.setComparator(comparator);
    return professionColumn;
  }

  private DefaultTableColumnController<CraftingSynopsisItem,String> buildProfessionNameColumn()
  {
    CellDataProvider<CraftingSynopsisItem,String> professionCell=new CellDataProvider<CraftingSynopsisItem,String>()
    {
      @Override
      public String getData(CraftingSynopsisItem item)
      {
        return item.getProfession().getName();
      }
    };
    DefaultTableColumnController<CraftingSynopsisItem,String> professionColumn=new DefaultTableColumnController<CraftingSynopsisItem,String>(CraftingSynopsisColumnIds.PROFESSION_NAME.name(),"Profession",String.class,professionCell); // I18n
    professionColumn.setWidthSpecs(100,100,100);
    return professionColumn;
  }

  private DefaultTableColumnController<CraftingSynopsisItem,String> buildVocationColumn()
  {
    CellDataProvider<CraftingSynopsisItem,String> vocationCell=new CellDataProvider<CraftingSynopsisItem,String>()
    {
      @Override
      public String getData(CraftingSynopsisItem item)
      {
        CharacterSummary summary=item.getCharacter().getSummary();
        Vocation vocation=item.getVocation();
        if (vocation!=null)
        {
          String vocationName=vocation.getName();
          return ContextRendering.render(summary,vocationName);
        }
        return null;
      }
    };
    DefaultTableColumnController<CraftingSynopsisItem,String> vocationColumn=new DefaultTableColumnController<CraftingSynopsisItem,String>(CraftingSynopsisColumnIds.VOCATION.name(),"Vocation",String.class,vocationCell); // I18n
    vocationColumn.setWidthSpecs(100,100,100);
    return vocationColumn;
  }

  private JPanel buildCraftingTierPanel(boolean mastery)
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    Icon tierIcon=LotroIconsManager.getCraftingTierIcon(mastery);
    panel.add(GuiFactory.buildIconLabel(tierIcon));
    String label=mastery?"Mastery":"Proficiency"; // I18n
    panel.add(GuiFactory.buildLabel(label));
    return panel;
  }

  private TableCellRenderer buildCraftingLevelCellRenderer(GenericTableController<CraftingSynopsisItem> parentTable, boolean mastery)
  {
    CustomLabelCellRenderer<CraftingSynopsisItem,CraftingLevel> customRenderer=new CustomLabelCellRenderer<CraftingSynopsisItem,CraftingLevel>()
    {
      public void configure(GenericTableController<CraftingSynopsisItem> table, CraftingLevel value, boolean isSelected, boolean hasFocus, int row, int column, JLabel label)
      {
        Context context=table.getContextManager().getContext(row,column);
        configureCraftingLevelLabel(mastery,label,value,context);
      }
    };
    LabelTableCellRenderer<CraftingSynopsisItem,CraftingLevel> r=new LabelTableCellRenderer<CraftingSynopsisItem,CraftingLevel>(parentTable,customRenderer);
    return r;
  }

  private DefaultTableColumnController<CraftingSynopsisItem,CraftingLevel> buildCraftingTierColumn(GenericTableController<CraftingSynopsisItem> table, final boolean mastery)
  {
    CellDataProvider<CraftingSynopsisItem,CraftingLevel> cell=new CellDataProvider<CraftingSynopsisItem,CraftingLevel>()
    {
      @Override
      public CraftingLevel getData(CraftingSynopsisItem item)
      {
        return item.getLevel(mastery);
      }
    };
    String columnName=mastery?"Mastery":"Proficiency"; // I18n
    String id=mastery?CraftingSynopsisColumnIds.MASTERY.name():CraftingSynopsisColumnIds.PROFICIENCY.name();
    DefaultTableColumnController<CraftingSynopsisItem,CraftingLevel> column=new DefaultTableColumnController<CraftingSynopsisItem,CraftingLevel>(id,columnName,CraftingLevel.class,cell);
    // Header cell renderer
    JPanel panel=buildCraftingTierPanel(mastery);
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(panel);
    column.setHeaderCellRenderer(headerRenderer);
    // Cell renderer
    TableCellRenderer renderer=buildCraftingLevelCellRenderer(table,mastery);
    column.setCellRenderer(renderer);

    // Init widths
    column.setMinWidth(150);
    column.setPreferredWidth(200);
    column.setMaxWidth(300);

    // Comparator
    Comparator<CraftingLevel> statsComparator=new Comparator<CraftingLevel>()
    {
      @Override
      public int compare(CraftingLevel data1, CraftingLevel data2)
      {
        return Integer.compare(data1.getTier(),data2.getTier());
      }
    };
    column.setComparator(statsComparator);
    return column;
  }

  private JPanel buildGuildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    panel.add(GuiFactory.buildLabel("Guild")); // I18n
    return panel;
  }

  private DefaultTableColumnController<CraftingSynopsisItem,FactionStatus> buildGuildColumn(GenericTableController<CraftingSynopsisItem> table)
  {
    CellDataProvider<CraftingSynopsisItem,FactionStatus> cell=new CellDataProvider<CraftingSynopsisItem,FactionStatus>()
    {
      @Override
      public FactionStatus getData(CraftingSynopsisItem item)
      {
        return item.getGuildFaction();
      }
    };
    DefaultTableColumnController<CraftingSynopsisItem,FactionStatus> column=new DefaultTableColumnController<CraftingSynopsisItem,FactionStatus>(CraftingSynopsisColumnIds.GUILD.name(),"Guild",FactionStatus.class,cell); // I18n
    // Header cell renderer
    JPanel panel=buildGuildPanel();
    TableCellRenderer headerRenderer=buildSimpleCellRenderer(panel);
    column.setHeaderCellRenderer(headerRenderer);
    // Cell renderer
    TableCellRenderer renderer=ReputationSynopsisTableController.buildFactionStatusCellRenderer(table);
    column.setCellRenderer(renderer);

    // Init widths
    column.setMinWidth(150);
    column.setPreferredWidth(200);
    column.setMaxWidth(300);

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
    _toons.clear();
    _toons.addAll(toons);
    updateRowItems();
    _table.refresh();
  }

  /**
   * Update data for a toon.
   * @param toon Targeted toon.
   */
  public void updateToon(CharacterFile toon)
  {
    updateRowItems();
    _table.refresh();
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    return _table.getTable();
  }

  private void configureTable(final JTable table)
  {
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    //table.setShowGrid(false);
    table.getTableHeader().setReorderingAllowed(false);
    // Adjust table row height for icons (30 pixels)
    table.setRowHeight(30);
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _table.filterUpdated();
  }

  private Color getColorForCraftingLevel(CraftingLevel level)
  {
    int index=level.getTier();
    if (index==0) return Color.GRAY;
    // Gradient from orange to green
    Profession profession=level.getProfession();
    int nbSteps=profession.getMaximumLevel().getTier();
    Color[] gradient=Gradients.getOrangeToGreen(nbSteps);
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

  private void configureCraftingLevelLabel(boolean mastery, JLabel label, CraftingLevel level, Context context)
  {
    Color backgroundColor=null;
    String text="";
    if (level!=null)
    {
      backgroundColor=getColorForCraftingLevel(level);
      CraftingLevelTier tier=mastery?level.getMastery():level.getProficiency();
      TitleDescription title=tier.getTitle();
      if (title!=null)
      {
        String rawTitleName=title.getRawName();
        text=ContextRendering.render(context,rawTitleName);
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
