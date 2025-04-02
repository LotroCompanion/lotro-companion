package delta.games.lotro.gui.character.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.details.CharacterDetails;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.character.status.crafting.CraftingStatus;
import delta.games.lotro.character.status.crafting.CraftingStatusSummaryBuilder;
import delta.games.lotro.character.status.crafting.summary.CraftingStatusSummary;
import delta.games.lotro.character.status.hobbies.HobbiesStatusManager;
import delta.games.lotro.character.status.hobbies.io.HobbiesStatusIo;
import delta.games.lotro.character.status.summary.AchievementsSummary;
import delta.games.lotro.character.status.summary.io.AchievementsSummaryIO;
import delta.games.lotro.character.status.traits.shared.TraitSlotsStatus;
import delta.games.lotro.character.storage.summary.CharacterStorageSummary;
import delta.games.lotro.character.storage.summary.StorageSummaryIO;
import delta.games.lotro.common.Duration;
import delta.games.lotro.gui.character.config.CharacterStatsSummaryPanelController;
import delta.games.lotro.gui.character.gear.EquipmentDisplayPanelController;
import delta.games.lotro.gui.character.main.summary.CharacterSummaryPanelController;
import delta.games.lotro.gui.character.status.crafting.summary.CraftingStatusSummaryPanelController;
import delta.games.lotro.gui.character.status.hobbies.HobbiesStatusPanelController;
import delta.games.lotro.gui.character.status.summary.AchievementsSummaryPanelController;
import delta.games.lotro.gui.character.status.traits.racial.RacialTraitsDisplayPanelController;
import delta.games.lotro.gui.character.storage.summary.CharacterStorageSummaryPanelController;
import delta.games.lotro.gui.character.virtues.VirtuesDisplayPanelController;
import delta.games.lotro.gui.character.xp.XpDisplayPanelController;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.gui.utils.LayoutUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.utils.ContextPropertyNames;
import delta.games.lotro.utils.Formats;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for the main window of a character.
 * @author DAM
 */
public class MainCharacterWindowController extends DefaultWindowController implements GenericEventsListener<CharacterEvent>
{
  // Data
  private CharacterFile _toon;
  // UI
  private CharacterMainButtonsManager _buttonsMgr;
  // Controllers
  private MainCharacterHeaderPanelController _header;
  private JLabel _importDate;
  private CharacterSummaryPanelController _summaryController;
  private AchievementsSummaryPanelController _achievements;
  private JLabel _inGameTime;
  private CraftingStatusSummaryPanelController _craftingStatus;
  private EquipmentDisplayPanelController _gear;
  private VirtuesDisplayPanelController _virtues;
  private RacialTraitsDisplayPanelController _racialTraits;
  private CharacterStatsSummaryPanelController _stats;
  private XpDisplayPanelController _xp;
  private CharacterStorageSummaryPanelController _storage;
  private MoneyDisplayController _money;
  private HobbiesStatusPanelController _hobbies;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public MainCharacterWindowController(CharacterFile toon)
  {
    _toon=toon;
    setContextProperty(ContextPropertyNames.BASE_CHARACTER_SUMMARY,toon.getSummary());
    _header=new MainCharacterHeaderPanelController(this,_toon);
    _importDate=GuiFactory.buildLabel("");
    _buttonsMgr=new CharacterMainButtonsManager(this,toon);
    _summaryController=new CharacterSummaryPanelController(this);
    _achievements=new AchievementsSummaryPanelController(this);
    _inGameTime=GuiFactory.buildLabel("");
    _craftingStatus=new CraftingStatusSummaryPanelController();
    CharacterData current=_toon.getInfosManager().getCurrentData();
    _gear=new EquipmentDisplayPanelController(this,current.getEquipment());
    _gear.initButtonListeners();
    _virtues=new VirtuesDisplayPanelController(false);
    int level=toon.getSummary().getLevel();
    _racialTraits=new RacialTraitsDisplayPanelController(this,level);
    _stats=new CharacterStatsSummaryPanelController(this,current);
    _xp=new XpDisplayPanelController();
    _storage=new CharacterStorageSummaryPanelController();
    _money=new MoneyDisplayController();
    HobbiesStatusManager status=HobbiesStatusIo.load(toon);
    _hobbies=new HobbiesStatusPanelController(this,status);
    fill();
    EventsManager.addListener(CharacterEvent.class,this);
  }

  private void fill()
  {
    CharacterSummary summary=_toon.getSummary();
    CharacterDetails details=_toon.getDetails();
    // Import date
    Long importDate=summary.getImportDate();
    if (importDate!=null)
    {
      String dateStr=Formats.getDateTimeString(new Date(importDate.longValue()));
      _importDate.setText("Import date: "+dateStr);
      _importDate.setVisible(true);
    }
    else
    {
      _importDate.setVisible(false);
    }
    // Context
    CharacterData current=_toon.getInfosManager().getCurrentData();
    setContext(current);
    // Summary
    _summaryController.setSummary(summary,details);
    // Achievements
    AchievementsSummary achievements=AchievementsSummaryIO.loadAchievementsSummary(_toon);
    _achievements.setSummary(achievements);
    int inGameTime=details.getIngameTime();
    if (inGameTime>0)
    {
      _inGameTime.setText(Duration.getDurationString(inGameTime));
    }
    else
    {
      _inGameTime.setText("?");
    }
    // Crafting
    CraftingStatus status=_toon.getCraftingMgr().getCraftingStatus();
    CraftingStatusSummaryBuilder b=new CraftingStatusSummaryBuilder();
    CraftingStatusSummary craftingSummary=b.buildSummary(status);
    _craftingStatus.setStatus(craftingSummary);
    // Virtues
    VirtuesSet virtues=current.getVirtues();
    _virtues.setVirtues(virtues);
    // Racial traits
    TraitSlotsStatus racialTraitsStatus=current.getTraits().getRacialTraitsStatus();
    _racialTraits.setStatus(racialTraitsStatus);
    // Stats
    _stats.getPanel();
    _stats.update();
    // XP
    int level=summary.getLevel();
    long xp=details.getXp();
    _xp.setXP(xp,level);
    // Storage
    CharacterStorageSummary storageSummary=StorageSummaryIO.loadCharacterStorageSummary(_toon);
    // Money
    _money.setMoney(details.getMoney());
    _storage.update(storageSummary);
  }

  private void setContext(CharacterData current)
  {
    // Character level
    int level=current.getLevel();
    setContextProperty(ContextPropertyNames.CHARACTER_LEVEL,Integer.valueOf(level));
    // Current character
    setContextProperty(ContextPropertyNames.CHARACTER_DATA,current);
  }

  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.CHARACTER_SUMMARY_UPDATED)
    {
      CharacterFile toon=event.getToonFile();
      if (toon==_toon)
      {
        update();
      }
    }
  }

  private void update()
  {
    fill();
  }

  /**
   * Get the window identifier for a given toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A window identifier.
   */
  public static String getIdentifier(String serverName, String toonName)
  {
    String id="FILE#"+serverName+"#"+toonName;
    id=id.toUpperCase();
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Header
    JPanel headerPanel=_header.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(3,5,3,5),0,0);
    panel.add(headerPanel,c);
    // Import date
    c=new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,2,5),0,0);
    panel.add(_importDate,c);
    // Tabs
    JTabbedPane tabs=GuiFactory.buildTabbedPane();
    tabs.add(Labels.getLabel("main.character.window.tab.summary.name"),buildTab1());
    tabs.add(Labels.getLabel("main.character.window.tab.gear.name"),buildTab2());
    c=new GridBagConstraints(0,2,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tabs,c);
    return panel;
  }

  private JPanel buildTab1()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Summary
    JPanel column1=buildTab1Column1();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(column1,c);
    // Crafting, capabilities and hobbies
    JPanel column2=buildTab1Column2();
    c=new GridBagConstraints(1,0,1,1,0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(column2,c);
    // Glue
    c=new GridBagConstraints(2,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    return panel;
  }

  private JPanel buildTab1Column1()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Summary panel
    JPanel summaryPanel=_summaryController.getPanel();
    summaryPanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.summary.name")));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(summaryPanel,c);
    // XP
    JPanel xpPanel=_xp.getPanel();
    xpPanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.xp.name")));
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(xpPanel,c);
    // Wealth
    JPanel wealthPanel=buildWealthPanel();
    c=new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(wealthPanel,c);
    return panel;
  }

  private JPanel buildWealthPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Storage
    JPanel storagePanel=_storage.getPanel();
    storagePanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.storage.name")));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(storagePanel,c);
    // Money
    JPanel moneyPanel=_money.getPanel();
    moneyPanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.money.name")));
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(moneyPanel,c);
    // Buttons
    List<JButton> buttons=new ArrayList<JButton>();
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.STORAGE_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.CURRENCIES_COMMAND));
    JPanel buttonsPanel=LayoutUtils.buildVerticalPanel(buttons,buttons.size());
    c=new GridBagConstraints(1,0,1,2,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(buttonsPanel,c);
    ret.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.wealth.name")));
    return ret;
  }

  private JPanel buildTab1Column2()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Top panel (achievements and reputation)
    JPanel topPanel=buildTopPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,3,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);
    // Capabilities & Misc
    JPanel capabilitiesPanel=buildCapabilitiesPanel();
    JPanel miscButtons=buildMiscButtonsPanel();
    List<JComponent> componentsColumn1=new ArrayList<JComponent>();
    componentsColumn1.add(capabilitiesPanel);
    componentsColumn1.add(miscButtons);
    JPanel column1=LayoutUtils.buildVerticalPanel(componentsColumn1,componentsColumn1.size(),0,0);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(column1,c);
    // Hobbies & Crafting
    JPanel hobbiesStatusPanel=_hobbies.getPanel();
    hobbiesStatusPanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.hobbies.name")));
    JPanel craftingStatusPanel=buildCraftingPanel();
    List<JComponent> componentsColumn2=new ArrayList<JComponent>();
    componentsColumn2.add(hobbiesStatusPanel);
    componentsColumn2.add(craftingStatusPanel);
    JPanel column2=LayoutUtils.buildVerticalPanel(componentsColumn2,componentsColumn2.size(),0,0);
    c=new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(column2,c);
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Achievements
    JPanel achievementsPanel=buildAchievementsPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(achievementsPanel,c);
    // Reputation
    JPanel reputationPanel=buildReputationPanel();
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(reputationPanel,c);
    return panel;
  }

  private JPanel buildCraftingPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Crafting status
    JPanel craftingStatusPanel=_craftingStatus.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(craftingStatusPanel,c);
    // Buttons
    List<JButton> buttons=new ArrayList<JButton>();
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.CRAFTING_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.RECIPES_STATUS_COMMAND));
    JPanel buttonsPanel=LayoutUtils.buildHorizontalPanel(buttons,buttons.size());
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(buttonsPanel,c);
    ret.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.crafting.name")));
    return ret;
  }

  private JPanel buildAchievementsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Summary
    JPanel summaryPanel=_achievements.getPanel();
    summaryPanel.setBorder(GuiFactory.buildTitledBorder("Summary"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,2,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(summaryPanel,c);
    // In-game time
    JPanel inGameTimePanel=buildInGameTimePanel();
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,2,5),0,0);
    ret.add(inGameTimePanel,c);
    // Buttons
    JPanel buttonsPanel=buildAchievementsButtonsPanel();
    c=new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(buttonsPanel,c);
    ret.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.achievements.name")));
    return ret;
  }

  private JPanel buildInGameTimePanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(GuiFactory.buildLabel("In-game time: "),c);
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_inGameTime,c);
    return ret;
  }

  private JPanel buildAchievementsButtonsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    List<JButton> buttonsLine1=new ArrayList<JButton>();
    buttonsLine1.add(_buttonsMgr.getButton(MainCharacterWindowCommands.QUESTS_STATUS_COMMAND));
    buttonsLine1.add(_buttonsMgr.getButton(MainCharacterWindowCommands.DEEDS_STATUS_COMMAND));
    buttonsLine1.add(_buttonsMgr.getButton(MainCharacterWindowCommands.TITLES_STATUS_COMMAND));
    JPanel line1=LayoutUtils.buildHorizontalPanel(buttonsLine1,buttonsLine1.size());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(line1,c);
    List<JButton> buttonsLine2=new ArrayList<JButton>();
    buttonsLine2.add(_buttonsMgr.getButton(MainCharacterWindowCommands.TASKS_STATUS_COMMAND));
    buttonsLine2.add(_buttonsMgr.getButton(MainCharacterWindowCommands.SKIRMISH_STATS_COMMAND));
    JPanel line2=LayoutUtils.buildHorizontalPanel(buttonsLine2,buttonsLine2.size());
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(line2,c);
    return ret;
  }

  private JPanel buildReputationPanel()
  {
    List<JButton> buttons=new ArrayList<JButton>();
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.REPUTATION_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.ALLEGIANCES_COMMAND));
    JPanel ret=LayoutUtils.buildVerticalPanel(buttons,buttons.size());
    ret.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.reputation.name")));
    return ret;
  }

  private JPanel buildCapabilitiesPanel()
  {
    List<JComponent> verticalList=new ArrayList<JComponent>();
    // Line 1
    List<JButton> buttonsLine1=new ArrayList<JButton>();
    buttonsLine1.add(_buttonsMgr.getButton(MainCharacterWindowCommands.MOUNTED_APPEARANCES_COMMAND));
    JPanel line1=LayoutUtils.buildHorizontalPanel(buttonsLine1,buttonsLine1.size());
    verticalList.add(line1);
    // Line 2
    List<JButton> buttonsLine2=new ArrayList<JButton>();
    buttonsLine2.add(_buttonsMgr.getButton(MainCharacterWindowCommands.EMOTES_COMMAND));
    buttonsLine2.add(_buttonsMgr.getButton(MainCharacterWindowCommands.MOUNTS_COMMAND));
    JPanel line2=LayoutUtils.buildHorizontalPanel(buttonsLine2,buttonsLine2.size());
    verticalList.add(line2);
    // Line 3
    List<JButton> buttonsLine3=new ArrayList<JButton>();
    buttonsLine3.add(_buttonsMgr.getButton(MainCharacterWindowCommands.TRAVELS_COMMAND));
    buttonsLine3.add(_buttonsMgr.getButton(MainCharacterWindowCommands.PETS_COMMAND));
    JPanel line3=LayoutUtils.buildHorizontalPanel(buttonsLine3,buttonsLine3.size());
    verticalList.add(line3);
    // Line 4
    List<JButton> buttonsLine4=new ArrayList<JButton>();
    buttonsLine4.add(_buttonsMgr.getButton(MainCharacterWindowCommands.BAUBLES_COMMAND));
    JPanel line4=LayoutUtils.buildHorizontalPanel(buttonsLine4,buttonsLine4.size());
    verticalList.add(line4);
    JPanel ret=LayoutUtils.buildVerticalPanel(verticalList,verticalList.size());
    ret.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.capaCollecs.name")));
    return ret;
  }

  private JPanel buildMiscButtonsPanel()
  {
    List<JButton> buttons=new ArrayList<JButton>();
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.HOUSING_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.LEVEL_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.LOG_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.NOTES_COMMAND));
    JPanel ret=LayoutUtils.buildVerticalPanel(buttons,buttons.size());
    ret.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.misc.name")));
    return ret;
  }

  private JPanel buildTab2()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Gear
    JPanel column1=buildTab2Column1();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(column1,c);
    // Stats
    JPanel column2=buildTab2Column2();
    c=new GridBagConstraints(1,0,1,1,0.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(column2,c);
    // Command buttons
    JPanel buttonsPanel=buildTab2ButtonsPanel();
    c=new GridBagConstraints(2,0,1,1,0.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(buttonsPanel,c);
    JPanel bottomPanel=buildTab2BottomPanel();
    c=new GridBagConstraints(0,1,3,1,0.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(bottomPanel,c);
    // Glue
    c=new GridBagConstraints(3,2,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    return panel;
  }

  private JPanel buildTab2Column1()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Equipment
    JPanel gearPanel=_gear.getPanel();
    gearPanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.gear.name")));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(gearPanel,c);
    // Virtues
    JPanel virtuesPanel=_virtues.getPanel();
    virtuesPanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.virtues.name")));
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(virtuesPanel,c);
    return panel;
  }

  private JPanel buildTab2BottomPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Racial traits
    JPanel racialTraitPanel=_racialTraits.getPanel();
    racialTraitPanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.racialTraits.name")));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(racialTraitPanel,c);
    // Buttons
    JPanel buttonsPanel=buildTab2BottomButtonsPanel();
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(buttonsPanel,c);
    return panel;
  }

  private JPanel buildTab2BottomButtonsPanel()
  {
    List<JButton> buttons=new ArrayList<JButton>();
    // Later: BB Tree, Mounted tree
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.TRAIT_TREE_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.SKIRMISH_TRAITS_COMMAND));
    return LayoutUtils.buildHorizontalPanel(buttons,buttons.size());
  }

  private JPanel buildTab2Column2()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Stats
    JPanel statsPanel=_stats.getPanel();
    statsPanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("main.character.window.panel.stats.name")));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statsPanel,c);
    return panel;
  }

  private JPanel buildTab2ButtonsPanel()
  {
    List<JButton> buttons=new ArrayList<JButton>();
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.CONFIGS_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.STASH_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.RELICS_INVENTORY_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.OUTFITS_COMMAND));
    buttons.add(_buttonsMgr.getButton(MainCharacterWindowCommands.PVP_COMMAND));
    return LayoutUtils.buildVerticalPanel(buttons,buttons.size());
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Character: "+name+" @ "+serverName; // I18n
    frame.setTitle(title);
    frame.pack();
    frame.setResizable(true);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    String serverName=_toon.getServerName();
    String toonName=_toon.getName();
    String id=getIdentifier(serverName,toonName);
    return id;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    EventsManager.removeListener(CharacterEvent.class,this);
    super.dispose();
    if (_buttonsMgr!=null)
    {
      _buttonsMgr.dispose();
      _buttonsMgr=null;
    }
    if (_header!=null)
    {
      _header.dispose();
      _header=null;
    }
    _importDate=null;
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    if (_achievements!=null)
    {
      _achievements.dispose();
      _achievements=null;
    }
    _inGameTime=null;
    if (_craftingStatus!=null)
    {
      _craftingStatus.dispose();
      _craftingStatus=null;
    }
    if (_gear!=null)
    {
      _gear.dispose();
      _gear=null;
    }
    if (_virtues!=null)
    {
      _virtues.dispose();
      _virtues=null;
    }
    if (_racialTraits!=null)
    {
      _racialTraits.dispose();
      _racialTraits=null;
    }
    if (_stats!=null)
    {
      _stats.dispose();
      _stats=null;
    }
    if (_xp!=null)
    {
      _xp.dispose();
      _xp=null;
    }
    if (_storage!=null)
    {
      _storage.dispose();
      _storage=null;
    }
    if (_money!=null)
    {
      _money.dispose();
      _money=null;
    }
    if (_hobbies!=null)
    {
      _hobbies.dispose();
      _hobbies=null;
    }
    if (_toon!=null)
    {
      _toon.getPreferences().saveAllPreferences();
      _toon.gc();
      _toon=null;
    }
  }
}
