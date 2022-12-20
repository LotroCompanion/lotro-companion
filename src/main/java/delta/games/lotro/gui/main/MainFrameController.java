package delta.games.lotro.gui.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.Preferences;
import delta.games.lotro.Config;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.about.AboutDialogController;
import delta.games.lotro.gui.about.CreditsDialogController;
import delta.games.lotro.gui.account.AccountsManagementController;
import delta.games.lotro.gui.character.status.crafting.synopsis.CraftingSynopsisWindowController;
import delta.games.lotro.gui.character.status.currencies.MultipleCharactersCurrencyHistoryWindowController;
import delta.games.lotro.gui.character.status.emotes.synopsis.EmotesSynopsisWindowController;
import delta.games.lotro.gui.character.status.levelling.CharacterLevelWindowController;
import delta.games.lotro.gui.character.status.reputation.synopsis.ReputationSynopsisWindowController;
import delta.games.lotro.gui.character.status.warbands.WarbandsWindowController;
import delta.games.lotro.gui.clientImport.ClientImportDialogController;
import delta.games.lotro.gui.configuration.ConfigurationDialogController;
import delta.games.lotro.gui.kinship.KinshipsManagementController;
import delta.games.lotro.gui.lore.instances.explorer.InstancesExplorerWindowController;
import delta.games.lotro.gui.maps.global.MapWindowController;
import delta.games.lotro.gui.maps.resources.ResourcesMapsExplorerWindowController;
import delta.games.lotro.gui.misc.paypal.PaypalButtonController;
import delta.games.lotro.gui.toon.ToonsManagementController;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.utils.cfg.ApplicationConfiguration;
import delta.games.lotro.utils.dat.DatInterface;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for the main frame.
 * @author DAM
 */
public class MainFrameController extends DefaultWindowController implements ActionListener
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="MAIN_WINDOW";

  private static final String LEVELLING_COMMAND="levellingCommand";
  private static final String WARBANDS_COMMAND="warbandsCommand";
  private static final String INSTANCES_COMMAND="instancesCommand";
  private static final String RESOURCES_MAPS_COMMAND="resourcesMapsCommand";
  private static final String REPUTATION_SYNOPSIS_COMMAND="reputationSynopsisCommand";
  private static final String CRAFTING_SYNOPSIS_COMMAND="craftingSynopsisCommand";
  private static final String CURRENCIES_SYNOPSIS_COMMAND="currenciesCommand";
  private static final String EMOTES_SYNOPSIS_COMMAND="emotesSynopsisCommand";
  private static final String MAP_COMMAND="mapCommand";
  private static final String CLIENT_SYNCHRO_COMMAND="clientSynchroCommand";
  private static final String SETTINGS_COMMAND="settingsCommand";
  private static final String ABOUT_COMMAND="aboutCommand";

  private ToolbarController _toolbarTracking;
  private LoreActionsController _loreCtrl;
  private ToolbarController _toolbarMaps;
  private ToolbarController _toolbarMisc;
  private PaypalButtonController _paypalButton;
  private ToonsManagementController _toonsManager;
  private AccountsManagementController _accountsManager;
  private KinshipsManagementController _kinshipsManager;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   */
  public MainFrameController()
  {
    _toonsManager=new ToonsManagementController(this);
    _accountsManager=new AccountsManagementController(this);
    _kinshipsManager=new KinshipsManagementController(this);
    _windowsManager=new WindowsManager();
    _loreCtrl=new LoreActionsController(this,_windowsManager);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    boolean isLive=LotroCoreConfig.isLive();
    String appName=isLive?"LotRO Companion":"LotRO Lore Database";
    frame.setTitle(appName);
    if (isLive)
    {
      frame.setSize(920,400);
    }
    else
    {
      frame.setSize(600,130);
    }
    frame.setLocation(100,100);
    frame.getContentPane().setBackground(GuiFactory.getBackgroundColor());
    return frame;
  }

  @Override
  protected JMenuBar buildMenuBar()
  {
    JMenuBar menuBar=GuiFactory.buildMenuBar();
    JMenu fileMenu=GuiFactory.buildMenu("File");
    JMenuItem quit=GuiFactory.buildMenuItem("Quit");
    ActionListener alQuit=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doQuit();
      }
    };
    quit.addActionListener(alQuit);
    fileMenu.add(quit);
    menuBar.add(fileMenu);

    boolean isLive=LotroCoreConfig.isLive();
    if (isLive)
    {
      // Statistics
      JMenu statsMenu=buildStatisticsMenu();
      menuBar.add(statsMenu);
    }
    // Compendium menu
    JMenu compendiumMenu=_loreCtrl.buildCompendiumMenu(isLive);
    // Maps
    JMenu mapsMenu=GuiFactory.buildMenu("Maps");
    // - map
    JMenuItem map=GuiFactory.buildMenuItem("Middle-earth Map");
    map.setActionCommand(MAP_COMMAND);
    map.addActionListener(this);
    mapsMenu.add(map);
    if (isLive)
    {
      // - instances
      JMenuItem instancesExplorer=GuiFactory.buildMenuItem("Instances");
      instancesExplorer.setActionCommand(INSTANCES_COMMAND);
      instancesExplorer.addActionListener(this);
      mapsMenu.add(instancesExplorer);
    }
    // - resources maps
    JMenuItem resourcesMapsExplorer=GuiFactory.buildMenuItem("Resources Maps");
    resourcesMapsExplorer.setActionCommand(RESOURCES_MAPS_COMMAND);
    resourcesMapsExplorer.addActionListener(this);
    mapsMenu.add(resourcesMapsExplorer);

    // Help
    JMenu helpMenu=GuiFactory.buildMenu("Help");
    // - about
    JMenuItem aboutMenuItem=GuiFactory.buildMenuItem("About...");
    aboutMenuItem.setActionCommand(ABOUT_COMMAND);
    aboutMenuItem.addActionListener(this);
    helpMenu.add(aboutMenuItem);
    // - credits
    JMenuItem creditsMenuItem=GuiFactory.buildMenuItem("Credits...");
    ActionListener alCredits=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doCredits();
      }
    };
    creditsMenuItem.addActionListener(alCredits);
    helpMenu.add(creditsMenuItem);

    menuBar.add(compendiumMenu);
    menuBar.add(mapsMenu);
    menuBar.add(Box.createHorizontalGlue());
    menuBar.add(helpMenu);
    return menuBar;
  }

  private JMenu buildStatisticsMenu()
  {
    // Statistics
    JMenu statsMenu=GuiFactory.buildMenu("Statistics");
    // - levelling
    JMenuItem levellingStats=GuiFactory.buildMenuItem("Levelling");
    levellingStats.setActionCommand(LEVELLING_COMMAND);
    levellingStats.addActionListener(this);
    statsMenu.add(levellingStats);
    // - warbands
    JMenuItem warbandsStats=GuiFactory.buildMenuItem("Warbands");
    warbandsStats.setActionCommand(WARBANDS_COMMAND);
    warbandsStats.addActionListener(this);
    statsMenu.add(warbandsStats);
    // - reputation
    JMenuItem reputationSynopsis=GuiFactory.buildMenuItem("Reputation synopsis");
    reputationSynopsis.setActionCommand(REPUTATION_SYNOPSIS_COMMAND);
    reputationSynopsis.addActionListener(this);
    statsMenu.add(reputationSynopsis);
    // - crafting
    JMenuItem craftingSynopsis=GuiFactory.buildMenuItem("Crafting synopsis");
    craftingSynopsis.setActionCommand(CRAFTING_SYNOPSIS_COMMAND);
    craftingSynopsis.addActionListener(this);
    statsMenu.add(craftingSynopsis);
    // - currencies
    JMenuItem currenciesSynopsis=GuiFactory.buildMenuItem("Currencies synopsis");
    currenciesSynopsis.setActionCommand(CURRENCIES_SYNOPSIS_COMMAND);
    currenciesSynopsis.addActionListener(this);
    statsMenu.add(currenciesSynopsis);
    // - emotes
    JMenuItem emotesSynopsis=GuiFactory.buildMenuItem("Emotes synopsis");
    emotesSynopsis.setActionCommand(EMOTES_SYNOPSIS_COMMAND);
    emotesSynopsis.addActionListener(this);
    statsMenu.add(emotesSynopsis);
    return statsMenu;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    // Toolbars
    JPanel toolbarsPanel=buildToolbarsPanel();
    ret.add(toolbarsPanel,BorderLayout.NORTH);
    boolean isLive=LotroCoreConfig.isLive();
    if (isLive)
    {
      JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
      // Characters
      JPanel toonsPanel=_toonsManager.getPanel();
      tabbedPane.add("Characters",toonsPanel);
      // Accounts
      JPanel accountsPanel=_accountsManager.getPanel();
      tabbedPane.add("Accounts",accountsPanel);
      // Kinships
      JPanel kinshipsPanel=_kinshipsManager.getPanel();
      tabbedPane.add("Kinships",kinshipsPanel);
      ret.add(tabbedPane,BorderLayout.CENTER);
    }
    return ret;
  }

  private JPanel buildToolbarsPanel()
  {
    boolean isLive=LotroCoreConfig.isLive();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
    if (isLive)
    {
      _toolbarTracking=buildToolBarTracking();
      panel.add(_toolbarTracking.getToolBar(),c);
      c.gridx++;
    }
    ToolbarController loreToolbar=_loreCtrl.buildToolbarLore(isLive);
    panel.add(loreToolbar.getToolBar(),c);
    c.gridx++;
    _toolbarMaps=buildToolBarMaps(isLive);
    panel.add(_toolbarMaps.getToolBar(),c);
    c.gridx++;
    _toolbarMisc=buildToolBarMisc(isLive);
    panel.add(_toolbarMisc.getToolBar(),c);
    c.gridx++;
    JPanel padding=GuiFactory.buildPanel(new FlowLayout());
    c=new GridBagConstraints(c.gridx,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,0),0,0);
    c.gridx++;
    panel.add(padding,c);
    c=new GridBagConstraints(c.gridx,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
    _paypalButton=new PaypalButtonController();
    panel.add(_paypalButton.getButton(),c);
    c.gridx++;
    return panel;
  }

  private ToolbarController buildToolBarTracking()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Leveling icon
    String levellingIconPath=SharedUiUtils.getToolbarIconPath("levelling");
    ToolbarIconItem levellingIconItem=new ToolbarIconItem(LEVELLING_COMMAND,levellingIconPath,LEVELLING_COMMAND,"Levelling...","Levelling");
    model.addToolbarIconItem(levellingIconItem);
    // Warbands icon
    String warbandsIconPath=SharedUiUtils.getToolbarIconPath("warbands");
    ToolbarIconItem warbandsIconItem=new ToolbarIconItem(WARBANDS_COMMAND,warbandsIconPath,WARBANDS_COMMAND,"Warbands...","Warbands");
    model.addToolbarIconItem(warbandsIconItem);
    // Reputation synopsis icon
    String reputationSynopsisIconPath=SharedUiUtils.getToolbarIconPath("reputation");
    ToolbarIconItem reputationSynopsisIconItem=new ToolbarIconItem(REPUTATION_SYNOPSIS_COMMAND,reputationSynopsisIconPath,REPUTATION_SYNOPSIS_COMMAND,"Reputation synopsis...","Reputation synopsis");
    model.addToolbarIconItem(reputationSynopsisIconItem);
    // Crafting synopsis icon
    String craftingSynopsisIconPath=SharedUiUtils.getToolbarIconPath("crafting");
    ToolbarIconItem craftingSynopsisIconItem=new ToolbarIconItem(CRAFTING_SYNOPSIS_COMMAND,craftingSynopsisIconPath,CRAFTING_SYNOPSIS_COMMAND,"Crafting synopsis...","Crafting synopsis");
    model.addToolbarIconItem(craftingSynopsisIconItem);
    // Currencies synopsis icon
    String currenciesSynopsisIconPath=SharedUiUtils.getToolbarIconPath("currencies");
    ToolbarIconItem currenciesSynopsisIconItem=new ToolbarIconItem(CURRENCIES_SYNOPSIS_COMMAND,currenciesSynopsisIconPath,CURRENCIES_SYNOPSIS_COMMAND,"Currencies synopsis...","Currencies synopsis");
    model.addToolbarIconItem(currenciesSynopsisIconItem);
    // Emotes synopsis icon
    String emotesSynopsisIconPath=SharedUiUtils.getToolbarIconPath("emotes");
    ToolbarIconItem emotesSynopsisIconItem=new ToolbarIconItem(EMOTES_SYNOPSIS_COMMAND,emotesSynopsisIconPath,EMOTES_SYNOPSIS_COMMAND,"Emotes synopsis...","Emotes synopsis");
    model.addToolbarIconItem(emotesSynopsisIconItem);
    // Border
    controller.getToolBar().setBorder(GuiFactory.buildTitledBorder("Tracking Synopsis"));
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  private ToolbarController buildToolBarMaps(boolean isLive)
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Map icon
    String mapIconPath=SharedUiUtils.getToolbarIconPath("globe");
    ToolbarIconItem mapIconItem=new ToolbarIconItem(MAP_COMMAND,mapIconPath,MAP_COMMAND,"Maps...","Maps");
    model.addToolbarIconItem(mapIconItem);
    if (isLive)
    {
      // Instances icon
      String instancessIconPath=SharedUiUtils.getToolbarIconPath("instances");
      ToolbarIconItem instancesIconItem=new ToolbarIconItem(INSTANCES_COMMAND,instancessIconPath,INSTANCES_COMMAND,"Instances...","Instances");
      model.addToolbarIconItem(instancesIconItem);
    }
    // Resources maps icon
    String resourcesMapsIconPath=SharedUiUtils.getToolbarIconPath("resourcesMap");
    ToolbarIconItem resourcesMapsIconItem=new ToolbarIconItem(RESOURCES_MAPS_COMMAND,resourcesMapsIconPath,RESOURCES_MAPS_COMMAND,"Resources maps...","Resources Maps");
    model.addToolbarIconItem(resourcesMapsIconItem);
    // Border
    controller.getToolBar().setBorder(GuiFactory.buildTitledBorder("Maps"));
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  private ToolbarController buildToolBarMisc(boolean isLive)
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Settings
    String settingsIconPath=SharedUiUtils.getToolbarIconPath("settings");
    ToolbarIconItem settingsIconItem=new ToolbarIconItem(SETTINGS_COMMAND,settingsIconPath,SETTINGS_COMMAND,"Settings...","Settings...");
    model.addToolbarIconItem(settingsIconItem);
    if (isLive)
    {
      // Import from LOTRO (client)
      String importIconPath=SharedUiUtils.getToolbarIconPath("lotro-import");
      ToolbarIconItem importIconItem=new ToolbarIconItem(CLIENT_SYNCHRO_COMMAND,importIconPath,CLIENT_SYNCHRO_COMMAND,"Import from LotRO...","Import...");
      model.addToolbarIconItem(importIconItem);
    }
    // About
    String aboutIconPath=SharedUiUtils.getToolbarIconPath("about");
    ToolbarIconItem aboutIconItem=new ToolbarIconItem(ABOUT_COMMAND,aboutIconPath,ABOUT_COMMAND,"About Lotro Companion...","About...");
    model.addToolbarIconItem(aboutIconItem);
    // Border
    controller.getToolBar().setBorder(GuiFactory.buildTitledBorder("Misc"));
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  @Override
  protected void doWindowClosing()
  {
    doQuit();
  }

  private void doWarbands()
  {
    String id=WarbandsWindowController.getIdentifier();
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new WarbandsWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doReputationSynopsis()
  {
    String id=ReputationSynopsisWindowController.getIdentifier();
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new ReputationSynopsisWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doCraftingSynopsis()
  {
    String id=CraftingSynopsisWindowController.IDENTIFIER;
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CraftingSynopsisWindowController();
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doCurrenciesSynopsis()
  {
    String id=MultipleCharactersCurrencyHistoryWindowController.getIdentifier();
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new MultipleCharactersCurrencyHistoryWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doEmotesSynopsis()
  {
    String id=EmotesSynopsisWindowController.getIdentifier();
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new EmotesSynopsisWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doMap()
  {
    WindowController controller=_windowsManager.getWindow(MapWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new MapWindowController(Maps.getMaps().getMapsManager());
      _windowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(getFrame());
    }
    controller.bringToFront();
  }

  private void doInstances()
  {
    WindowController controller=_windowsManager.getWindow(InstancesExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new InstancesExplorerWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doResourcesMaps()
  {
    WindowController controller=_windowsManager.getWindow(ResourcesMapsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      DataFacade facade=DatInterface.getInstance().getFacade();
      controller=new ResourcesMapsExplorerWindowController(this,facade);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doClientSynchronizer()
  {
    WindowController controller=_windowsManager.getWindow(ClientImportDialogController.IDENTIFIER);
    if (controller==null)
    {
      controller=new ClientImportDialogController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doSettings()
  {
    ApplicationConfiguration configuration=ApplicationConfiguration.getInstance();
    ConfigurationDialogController dialog=new ConfigurationDialogController(this,configuration);
    dialog.getDialog().setLocationRelativeTo(this.getWindow());
    dialog.show(true);
  }

  @Override
  public void actionPerformed(ActionEvent event)
  {
    String cmd=event.getActionCommand();
    if (LEVELLING_COMMAND.equals(cmd))
    {
      doLevelling();
    }
    else if (WARBANDS_COMMAND.equals(cmd))
    {
      doWarbands();
    }
    else if (REPUTATION_SYNOPSIS_COMMAND.equals(cmd))
    {
      doReputationSynopsis();
    }
    else if (CRAFTING_SYNOPSIS_COMMAND.equals(cmd))
    {
      doCraftingSynopsis();
    }
    else if (CURRENCIES_SYNOPSIS_COMMAND.equals(cmd))
    {
      doCurrenciesSynopsis();
    }
    else if (EMOTES_SYNOPSIS_COMMAND.equals(cmd))
    {
      doEmotesSynopsis();
    }
    else if (MAP_COMMAND.equals(cmd))
    {
      doMap();
    }
    else if (INSTANCES_COMMAND.equals(cmd))
    {
      doInstances();
    }
    else if (RESOURCES_MAPS_COMMAND.equals(cmd))
    {
      doResourcesMaps();
    }
    else if (CLIENT_SYNCHRO_COMMAND.equals(cmd))
    {
      doClientSynchronizer();
    }
    else if (SETTINGS_COMMAND.equals(cmd))
    {
      doSettings();
    }
    else if (ABOUT_COMMAND.equals(cmd))
    {
      doAbout();
    }
  }

  private void doLevelling()
  {
    String id=CharacterLevelWindowController.getIdentifier();
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CharacterLevelWindowController();
      _windowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(getFrame());
    }
    controller.bringToFront();
  }

  private void doAbout()
  {
    String id=AboutDialogController.IDENTIFIER;
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      JFrame thisFrame=getFrame();
      controller=new AboutDialogController(this);
      _windowsManager.registerWindow(controller);
      Window w=controller.getWindow();
      w.setLocationRelativeTo(thisFrame);
      Point p=w.getLocation();
      w.setLocation(p.x+100,p.y+100);
    }
    controller.bringToFront();
  }

  private void doCredits()
  {
    String id=CreditsDialogController.IDENTIFIER;
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      JFrame thisFrame=getFrame();
      controller=new CreditsDialogController(this);
      _windowsManager.registerWindow(controller);
      Window w=controller.getWindow();
      w.setLocationRelativeTo(thisFrame);
    }
    controller.bringToFront();
  }

  private void doQuit()
  {
    int result=GuiFactory.showQuestionDialog(getFrame(),"Do you really want to quit?","Quit?",JOptionPane.YES_NO_OPTION);
    if (result==JOptionPane.OK_OPTION)
    {
      dispose();
    }
    Preferences preferences=Config.getInstance().getPreferences();
    preferences.saveAllPreferences();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    super.dispose();
    if (_toolbarTracking!=null)
    {
      _toolbarTracking.dispose();
      _toolbarTracking=null;
    }
    if (_loreCtrl!=null)
    {
      _loreCtrl.dispose();
      _loreCtrl=null;
    }
    if (_toolbarMaps!=null)
    {
      _toolbarMaps.dispose();
      _toolbarMaps=null;
    }
    if (_toolbarMisc!=null)
    {
      _toolbarMisc.dispose();
      _toolbarMisc=null;
    }
    if (_paypalButton!=null)
    {
      _paypalButton.dispose();
      _paypalButton=null;
    }
    if (_toonsManager!=null)
    {
      _toonsManager.dispose();
      _toonsManager=null;
    }
    if (_accountsManager!=null)
    {
      _accountsManager.dispose();
      _accountsManager=null;
    }
    if (_kinshipsManager!=null)
    {
      _kinshipsManager.dispose();
      _kinshipsManager=null;
    }
  }
}
