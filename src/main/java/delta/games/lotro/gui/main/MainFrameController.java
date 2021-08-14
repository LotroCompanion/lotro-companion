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
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.about.AboutDialogController;
import delta.games.lotro.gui.about.CreditsDialogController;
import delta.games.lotro.gui.account.AccountsManagementController;
import delta.games.lotro.gui.character.status.crafting.synopsis.CraftingSynopsisWindowController;
import delta.games.lotro.gui.character.status.levelling.CharacterLevelWindowController;
import delta.games.lotro.gui.character.status.reputation.synopsis.ReputationSynopsisWindowController;
import delta.games.lotro.gui.character.status.warbands.WarbandsWindowController;
import delta.games.lotro.gui.clientImport.ClientImportDialogController;
import delta.games.lotro.gui.configuration.ConfigurationDialogController;
import delta.games.lotro.gui.interceptor.InterceptorInterface;
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
  private static final String LEVELLING_ID="levelling";
  private static final String WARBANDS_ID="warbands";
  private static final String INSTANCES_ID="instances";
  private static final String RESOURCES_MAPS_ID="resourcesMaps";
  private static final String REPUTATION_SYNOPSIS_ID="reputationSynopsis";
  private static final String CRAFTING_SYNOPSIS_ID="craftingSynopsis";
  private static final String MAP_ID="map";
  private static final String NETWORK_SYNCHRO_ID="networkSynchro";
  private static final String CLIENT_SYNCHRO_ID="clientSynchro";
  private static final String SETTINGS_ID="settings";
  private static final String ABOUT_ID="about";

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
    frame.setTitle("LOTRO Companion");
    frame.setSize(850,400);
    frame.setLocation(100,100);
    frame.getContentPane().setBackground(GuiFactory.getBackgroundColor());
    return frame;
  }

  @Override
  protected JMenuBar buildMenuBar()
  {
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

    // Statistics
    JMenu statsMenu=GuiFactory.buildMenu("Statistics");
    // - levelling
    JMenuItem levellingStats=GuiFactory.buildMenuItem("Levelling");
    levellingStats.setActionCommand(LEVELLING_ID);
    levellingStats.addActionListener(this);
    statsMenu.add(levellingStats);
    // - warbands
    JMenuItem warbandsStats=GuiFactory.buildMenuItem("Warbands");
    warbandsStats.setActionCommand(WARBANDS_ID);
    warbandsStats.addActionListener(this);
    statsMenu.add(warbandsStats);
    // - reputation
    JMenuItem reputationSynopsis=GuiFactory.buildMenuItem("Reputation synopsis");
    reputationSynopsis.setActionCommand(REPUTATION_SYNOPSIS_ID);
    reputationSynopsis.addActionListener(this);
    statsMenu.add(reputationSynopsis);
    // - crafting
    JMenuItem craftingSynopsis=GuiFactory.buildMenuItem("Crafting synopsis");
    craftingSynopsis.setActionCommand(CRAFTING_SYNOPSIS_ID);
    craftingSynopsis.addActionListener(this);
    statsMenu.add(craftingSynopsis);

    // Compendium menu
    JMenu compendiumMenu=_loreCtrl.buildCompendiumMenu();

    // Maps
    JMenu mapsMenu=GuiFactory.buildMenu("Maps");
    // - map
    JMenuItem map=GuiFactory.buildMenuItem("Middle-earth Map");
    map.setActionCommand(MAP_ID);
    map.addActionListener(this);
    mapsMenu.add(map);
    // - instances
    JMenuItem instancesExplorer=GuiFactory.buildMenuItem("Instances");
    instancesExplorer.setActionCommand(INSTANCES_ID);
    instancesExplorer.addActionListener(this);
    mapsMenu.add(instancesExplorer);
    // - resources maps
    JMenuItem resourcesMapsExplorer=GuiFactory.buildMenuItem("Resources Maps");
    resourcesMapsExplorer.setActionCommand(RESOURCES_MAPS_ID);
    resourcesMapsExplorer.addActionListener(this);
    mapsMenu.add(resourcesMapsExplorer);

    // Help
    JMenu helpMenu=GuiFactory.buildMenu("Help");
    // - about
    JMenuItem aboutMenuItem=GuiFactory.buildMenuItem("About...");
    aboutMenuItem.setActionCommand(ABOUT_ID);
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

    JMenuBar menuBar=GuiFactory.buildMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(statsMenu);
    menuBar.add(compendiumMenu);
    menuBar.add(mapsMenu);
    menuBar.add(Box.createHorizontalGlue());
    menuBar.add(helpMenu);
    return menuBar;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    // Toolbars
    JPanel toolbarsPanel=buildToolbarsPanel();
    ret.add(toolbarsPanel,BorderLayout.NORTH);
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
    return ret;
  }

  private JPanel buildToolbarsPanel()
  {
    _toolbarTracking=buildToolBarTracking();
    ToolbarController loreToolbar=_loreCtrl.buildToolbarLore();
    _toolbarMaps=buildToolBarMaps();
    _toolbarMisc=buildToolBarMisc();
    _paypalButton=new PaypalButtonController();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
    panel.add(_toolbarTracking.getToolBar(),c);
    c.gridx++;
    panel.add(loreToolbar.getToolBar(),c);
    c.gridx++;
    panel.add(_toolbarMaps.getToolBar(),c);
    c.gridx++;
    if (_toolbarMisc!=null)
    {
      panel.add(_toolbarMisc.getToolBar(),c);
      c.gridx++;
    }
    JPanel padding=GuiFactory.buildPanel(new FlowLayout());
    c=new GridBagConstraints(c.gridx,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,0),0,0);
    c.gridx++;
    panel.add(padding,c);
    c=new GridBagConstraints(c.gridx,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
    panel.add(_paypalButton.getButton(),c);
    c.gridx++;
    return panel;
  }

  private ToolbarController buildToolBarTracking()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Levelling icon
    String levellingIconPath=SharedUiUtils.getToolbarIconPath("levelling");
    ToolbarIconItem levellingIconItem=new ToolbarIconItem(LEVELLING_ID,levellingIconPath,LEVELLING_ID,"Levelling...","Levelling");
    model.addToolbarIconItem(levellingIconItem);
    // Warbands icon
    String warbandsIconPath=SharedUiUtils.getToolbarIconPath("warbands");
    ToolbarIconItem warbandsIconItem=new ToolbarIconItem(WARBANDS_ID,warbandsIconPath,WARBANDS_ID,"Warbands...","Warbands");
    model.addToolbarIconItem(warbandsIconItem);
    // Reputation synopsis icon
    String reputationSynopsisIconPath=SharedUiUtils.getToolbarIconPath("reputation");
    ToolbarIconItem reputationSynopsisIconItem=new ToolbarIconItem(REPUTATION_SYNOPSIS_ID,reputationSynopsisIconPath,REPUTATION_SYNOPSIS_ID,"Reputation synopsis...","Reputation synopsis");
    model.addToolbarIconItem(reputationSynopsisIconItem);
    // Crafting synopsis icon
    String craftingSynopsisIconPath=SharedUiUtils.getToolbarIconPath("crafting");
    ToolbarIconItem craftingSynopsisIconItem=new ToolbarIconItem(CRAFTING_SYNOPSIS_ID,craftingSynopsisIconPath,CRAFTING_SYNOPSIS_ID,"Crafting synopsis...","Crafting synopsis");
    model.addToolbarIconItem(craftingSynopsisIconItem);
    // Border
    controller.getToolBar().setBorder(GuiFactory.buildTitledBorder("Tracking Synopsis"));
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  private ToolbarController buildToolBarMaps()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Map icon
    String mapIconPath=SharedUiUtils.getToolbarIconPath("globe");
    ToolbarIconItem mapIconItem=new ToolbarIconItem(MAP_ID,mapIconPath,MAP_ID,"Maps...","Maps");
    model.addToolbarIconItem(mapIconItem);
    // Instances icon
    String instancessIconPath=SharedUiUtils.getToolbarIconPath("instances");
    ToolbarIconItem instancesIconItem=new ToolbarIconItem(INSTANCES_ID,instancessIconPath,INSTANCES_ID,"Instances...","Instances");
    model.addToolbarIconItem(instancesIconItem);
    // Resources maps icon
    String resourcesMapsIconPath=SharedUiUtils.getToolbarIconPath("resourcesMap");
    ToolbarIconItem resourcesMapsIconItem=new ToolbarIconItem(RESOURCES_MAPS_ID,resourcesMapsIconPath,RESOURCES_MAPS_ID,"Resources maps...","Resources Maps");
    model.addToolbarIconItem(resourcesMapsIconItem);
    // Border
    controller.getToolBar().setBorder(GuiFactory.buildTitledBorder("Maps"));
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  private ToolbarController buildToolBarMisc()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Settings
    String settingsIconPath=SharedUiUtils.getToolbarIconPath("settings");
    ToolbarIconItem settingsIconItem=new ToolbarIconItem(SETTINGS_ID,settingsIconPath,SETTINGS_ID,"Settings...","Settings...");
    model.addToolbarIconItem(settingsIconItem);
    // Import from LOTRO (network)
    boolean hasSynchronizer=InterceptorInterface.checkInterceptorPresence();
    if (hasSynchronizer)
    {
      String importIconPath=SharedUiUtils.getToolbarIconPath("lotro-import");
      ToolbarIconItem importIconItem=new ToolbarIconItem(NETWORK_SYNCHRO_ID,importIconPath,NETWORK_SYNCHRO_ID,"Import from LotRO...","Import...");
      model.addToolbarIconItem(importIconItem);
    }
    // Import from LOTRO (client)
    String importIconPath=SharedUiUtils.getToolbarIconPath("lotro-import");
    ToolbarIconItem importIconItem=new ToolbarIconItem(CLIENT_SYNCHRO_ID,importIconPath,CLIENT_SYNCHRO_ID,"Import from LotRO...","Import...");
    model.addToolbarIconItem(importIconItem);
    // About
    String aboutIconPath=SharedUiUtils.getToolbarIconPath("about");
    ToolbarIconItem aboutIconItem=new ToolbarIconItem(ABOUT_ID,aboutIconPath,ABOUT_ID,"About...","About...");
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
    String id=CraftingSynopsisWindowController.getIdentifier();
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CraftingSynopsisWindowController();
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

  private void doNetworkSynchronizer()
  {
    InterceptorInterface.doSynchronizer(_windowsManager,this);
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
    if (LEVELLING_ID.equals(cmd))
    {
      doLevelling();
    }
    else if (WARBANDS_ID.equals(cmd))
    {
      doWarbands();
    }
    else if (REPUTATION_SYNOPSIS_ID.equals(cmd))
    {
      doReputationSynopsis();
    }
    else if (CRAFTING_SYNOPSIS_ID.equals(cmd))
    {
      doCraftingSynopsis();
    }
    else if (MAP_ID.equals(cmd))
    {
      doMap();
    }
    else if (INSTANCES_ID.equals(cmd))
    {
      doInstances();
    }
    else if (RESOURCES_MAPS_ID.equals(cmd))
    {
      doResourcesMaps();
    }
    else if (NETWORK_SYNCHRO_ID.equals(cmd))
    {
      doNetworkSynchronizer();
    }
    else if (CLIENT_SYNCHRO_ID.equals(cmd))
    {
      doClientSynchronizer();
    }
    else if (SETTINGS_ID.equals(cmd))
    {
      doSettings();
    }
    else if (ABOUT_ID.equals(cmd))
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
    String id=AboutDialogController.getIdentifier();
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
    String id=CreditsDialogController.getIdentifier();
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
