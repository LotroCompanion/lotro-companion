package delta.games.lotro.gui.character.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
import delta.games.lotro.gui.character.config.CharacterStatsSummaryPanelController;
import delta.games.lotro.gui.character.gear.EquipmentDisplayPanelController;
import delta.games.lotro.gui.character.main.summary.CharacterSummaryPanelController;
import delta.games.lotro.gui.character.status.crafting.summary.CraftingStatusSummaryPanelController;
import delta.games.lotro.gui.character.virtues.VirtuesDisplayPanelController;
import delta.games.lotro.gui.character.xp.XpDisplayPanelController;
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
  private CraftingStatusSummaryPanelController _crafting;
  private EquipmentDisplayPanelController _gear;
  private VirtuesDisplayPanelController _virtues;
  private CharacterStatsSummaryPanelController _stats;
  private XpDisplayPanelController _xp;
  private CharacterMainButtonsController _mainButtons;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public MainCharacterWindowController(CharacterFile toon)
  {
    _toon=toon;
    _summaryController=new CharacterSummaryPanelController(this);
    setContextProperty(ContextPropertyNames.BASE_CHARACTER_SUMMARY,toon.getSummary());
    _crafting=new CraftingStatusSummaryPanelController();
    CharacterData current=_toon.getInfosManager().getCurrentData();
    _gear=new EquipmentDisplayPanelController(this,current.getEquipment());
    _gear.initButtonListeners();
    _virtues=new VirtuesDisplayPanelController();
    _stats=new CharacterStatsSummaryPanelController(this,current);
    _xp=new XpDisplayPanelController();
    _mainButtons=new CharacterMainButtonsController(this,toon);
    fill();
  }

  private void fill()
  {
    CharacterSummary summary=_toon.getSummary();
    CharacterDetails details=_toon.getDetails();
    // Summary
    _summaryController.setSummary(summary,details);
    // Crafting
    CraftingStatus status=_toon.getCraftingMgr().getCraftingStatus();
    CraftingStatusSummaryBuilder b=new CraftingStatusSummaryBuilder();
    CraftingStatusSummary craftingSummary=b.buildSummary(status);
    _crafting.setStatus(craftingSummary);
    // Virtues
    CharacterData current=_toon.getInfosManager().getCurrentData();
    VirtuesSet virtues=current.getVirtues();
    _virtues.setVirtues(virtues);
    // Stats
    _stats.getPanel();
    _stats.update();
    // XP
    long xp=details.getXp();
    _xp.setXP(xp);
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
    // Whole panel
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Column 1
    JPanel column1Panel=GuiFactory.buildPanel(new GridBagLayout());
    // Summary panel
    JPanel summaryPanel=_summaryController.getPanel();
    summaryPanel.setBorder(GuiFactory.buildTitledBorder("Summary"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(3,5,3,5),0,0);
    column1Panel.add(summaryPanel,c);
    // XP
    JPanel xpPanel=_xp.getPanel();
    xpPanel.setBorder(GuiFactory.buildTitledBorder("XP"));
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    column1Panel.add(xpPanel,c);
    // => add column
    c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(column1Panel,c);

    // Column 2
    JPanel column2Panel=GuiFactory.buildPanel(new GridBagLayout());
    // Equipment
    JPanel gearPanel=_gear.getPanel();
    gearPanel.setBorder(GuiFactory.buildTitledBorder("Gear"));
    c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    column2Panel.add(gearPanel,c);
    // Virtues
    JPanel virtuesPanel=_virtues.getPanel();
    virtuesPanel.setBorder(GuiFactory.buildTitledBorder("Virtues"));
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    column2Panel.add(virtuesPanel,c);
    // => add column
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(column2Panel,c);

    // Stats
    JPanel statsPanel=_stats.getPanel();
    statsPanel.setBorder(GuiFactory.buildTitledBorder("Stats"));
    c=new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statsPanel,c);
    // Crafting
    JPanel craftingStatusPanel=_crafting.getPanel();
    craftingStatusPanel.setBorder(GuiFactory.buildTitledBorder("Crafting"));
    c=new GridBagConstraints(3,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(craftingStatusPanel,c);
    // Command buttons
    JPanel commandsPanel=_mainButtons.getPanel();
    c=new GridBagConstraints(0,1,4,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(commandsPanel,c);
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
