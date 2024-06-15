package delta.games.lotro.gui.character.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.details.CharacterDetails;
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
import delta.games.lotro.utils.ContextPropertyNames;

/**
 * Controller for the main window of a character.
 * @author DAM
 */
public class MainCharacterWindowController extends DefaultWindowController
{
  // Data
  private CharacterFile _toon;
  // Controllers
  private CharacterSummaryPanelController _summaryController;
  private AchievementsSummaryPanelController _achievements;
  private CraftingStatusSummaryPanelController _crafting;
  private EquipmentDisplayPanelController _gear;
  private VirtuesDisplayPanelController _virtues;
  private RacialTraitsDisplayPanelController _racialTraits;
  private CharacterStatsSummaryPanelController _stats;
  private XpDisplayPanelController _xp;
  private CharacterStorageSummaryPanelController _storage;
  private MoneyDisplayController _money;
  private HobbiesStatusPanelController _hobbies;
  private CharacterMainButtonsController2 _mainButtons;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public MainCharacterWindowController(CharacterFile toon)
  {
    _toon=toon;
    setContextProperty(ContextPropertyNames.BASE_CHARACTER_SUMMARY,toon.getSummary());
    _summaryController=new CharacterSummaryPanelController(this);
    setContextProperty(ContextPropertyNames.BASE_CHARACTER_SUMMARY,toon.getSummary());
    _achievements=new AchievementsSummaryPanelController(this);
    _crafting=new CraftingStatusSummaryPanelController();
    CharacterData current=_toon.getInfosManager().getCurrentData();
    _gear=new EquipmentDisplayPanelController(this,current.getEquipment());
    _gear.initButtonListeners();
    _virtues=new VirtuesDisplayPanelController();
    int level=toon.getSummary().getLevel();
    _racialTraits=new RacialTraitsDisplayPanelController(this,level);
    _stats=new CharacterStatsSummaryPanelController(this,current);
    _xp=new XpDisplayPanelController();
    _storage=new CharacterStorageSummaryPanelController();
    _money=new MoneyDisplayController();
    HobbiesStatusManager status=HobbiesStatusIo.load(toon);
    _hobbies=new HobbiesStatusPanelController(this,status);
    _mainButtons=new CharacterMainButtonsController2(this,toon);
    fill();
  }

  private void fill()
  {
    CharacterSummary summary=_toon.getSummary();
    CharacterDetails details=_toon.getDetails();
    // Summary
    _summaryController.setSummary(summary,details);
    // Achievements
    AchievementsSummary achievements=AchievementsSummaryIO.loadAchievementsSummary(_toon);
    _achievements.setSummary(achievements);
    // Crafting
    CraftingStatus status=_toon.getCraftingMgr().getCraftingStatus();
    CraftingStatusSummaryBuilder b=new CraftingStatusSummaryBuilder();
    CraftingStatusSummary craftingSummary=b.buildSummary(status);
    _crafting.setStatus(craftingSummary);
    // Virtues
    CharacterData current=_toon.getInfosManager().getCurrentData();
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
    // Header: TODO
    // Tabs
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    JTabbedPane tabs=GuiFactory.buildTabbedPane();
    tabs.add("Summary",buildTab1());
    tabs.add("Gear",buildTab2());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
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
    // Crafting & co.
    JPanel column2=buildTab1Column2();
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(column2,c);
    // Command buttons
    JPanel commandsPanel=_mainButtons.buildSummaryCommandsPanel();
    c=new GridBagConstraints(2,0,1,1,0.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(commandsPanel,c);
    // Glue
    c=new GridBagConstraints(3,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    return panel;
  }

  private JPanel buildTab1Column1()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Summary panel
    JPanel summaryPanel=_summaryController.getPanel();
    summaryPanel.setBorder(GuiFactory.buildTitledBorder("Summary"));
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(3,5,3,5),0,0);
    panel.add(summaryPanel,c);
    // XP
    JPanel xpPanel=_xp.getPanel();
    xpPanel.setBorder(GuiFactory.buildTitledBorder("XP"));
    c=new GridBagConstraints(0,1,2,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(xpPanel,c);
    // Storage
    JPanel storagePanel=_storage.getPanel();
    storagePanel.setBorder(GuiFactory.buildTitledBorder("Storage"));
    c=new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(storagePanel,c);
    // Achievements
    JPanel achievementsPanel=_achievements.getPanel();
    achievementsPanel.setBorder(GuiFactory.buildTitledBorder("Achievements"));
    c=new GridBagConstraints(1,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(achievementsPanel,c);
    return panel;
  }

  private JPanel buildTab1Column2()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Crafting
    JPanel craftingStatusPanel=_crafting.getPanel();
    craftingStatusPanel.setBorder(GuiFactory.buildTitledBorder("Crafting"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(craftingStatusPanel,c);
    // Hobbies
    JPanel hobbiesStatusPanel=_hobbies.getPanel();
    hobbiesStatusPanel.setBorder(GuiFactory.buildTitledBorder("Hobbies"));
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(hobbiesStatusPanel,c);
    // Money
    JPanel moneyPanel=_money.getPanel();
    moneyPanel.setBorder(GuiFactory.buildTitledBorder("Money"));
    c=new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(moneyPanel,c);
    return panel;
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
    JPanel commandsPanel=_mainButtons.buildGearCommandsPanel();
    c=new GridBagConstraints(2,0,1,1,0.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(commandsPanel,c);
    // Glue
    c=new GridBagConstraints(3,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    return panel;
  }

  private JPanel buildTab2Column1()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Equipment
    JPanel gearPanel=_gear.getPanel();
    gearPanel.setBorder(GuiFactory.buildTitledBorder("Gear"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(gearPanel,c);
    // Virtues
    JPanel virtuesPanel=_virtues.getPanel();
    virtuesPanel.setBorder(GuiFactory.buildTitledBorder("Virtues"));
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(virtuesPanel,c);
    // Racial traits
    JPanel racialTraitPanel=_racialTraits.getPanel();
    racialTraitPanel.setBorder(GuiFactory.buildTitledBorder("Racial Traits"));
    c=new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(racialTraitPanel,c);
    return panel;
  }

  private JPanel buildTab2Column2()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Stats
    JPanel statsPanel=_stats.getPanel();
    statsPanel.setBorder(GuiFactory.buildTitledBorder("Stats"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statsPanel,c);
    return panel;
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
    super.dispose();
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
    if (_crafting!=null)
    {
      _crafting.dispose();
      _crafting=null;
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
    if (_mainButtons!=null)
    {
      _mainButtons.dispose();
      _mainButtons=null;
    }
    if (_toon!=null)
    {
      _toon.getPreferences().saveAllPreferences();
      _toon.gc();
      _toon=null;
    }
  }
}
