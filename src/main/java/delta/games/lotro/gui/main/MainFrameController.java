package delta.games.lotro.gui.main;

import java.awt.BorderLayout;
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
import javax.swing.JToolBar;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.Preferences;
import delta.games.lotro.Config;
import delta.games.lotro.gui.about.AboutDialogController;
import delta.games.lotro.gui.about.CreditsDialogController;
import delta.games.lotro.gui.account.AccountsManagementController;
import delta.games.lotro.gui.deed.explorer.DeedsExplorerWindowController;
import delta.games.lotro.gui.emotes.explorer.EmotesExplorerWindowController;
import delta.games.lotro.gui.lore.trade.barter.explorer.BarterersExplorerWindowController;
import delta.games.lotro.gui.lore.trade.vendor.explorer.VendorsExplorerWindowController;
import delta.games.lotro.gui.mounts.explorer.MountsExplorerWindowController;
import delta.games.lotro.gui.pets.explorer.PetsExplorerWindowController;
import delta.games.lotro.gui.quests.explorer.QuestsExplorerWindowController;
import delta.games.lotro.gui.recipes.explorer.RecipesExplorerWindowController;
import delta.games.lotro.gui.stats.crafting.synopsis.CraftingSynopsisWindowController;
import delta.games.lotro.gui.stats.levelling.CharacterLevelWindowController;
import delta.games.lotro.gui.stats.reputation.synopsis.ReputationSynopsisWindowController;
import delta.games.lotro.gui.stats.warbands.WarbandsWindowController;
import delta.games.lotro.gui.titles.explorer.TitlesExplorerWindowController;
import delta.games.lotro.gui.toon.ToonsManagementController;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.ui.MapWindowController;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for the main frame.
 * @author DAM
 */
public class MainFrameController extends DefaultWindowController implements ActionListener
{
  private static final String LEVELLING_ID="levelling";
  private static final String WARBANDS_ID="warbands";
  private static final String DEEDS_ID="deeds";
  private static final String QUESTS_ID="quests";
  private static final String RECIPES_ID="recipes";
  private static final String TITLES_ID="titles";
  private static final String EMOTES_ID="emotes";
  private static final String MOUNTS_ID="mounts";
  private static final String PETS_ID="pets";
  private static final String VENDORS_ID="vendors";
  private static final String BARTERERS_ID="barterers";
  private static final String REPUTATION_SYNOPSIS_ID="reputationSynopsis";
  private static final String CRAFTING_SYNOPSIS_ID="craftingSynopsis";
  private static final String MAP_ID="map";

  private ToolbarController _toolbar;
  private ToonsManagementController _toonsManager;
  private AccountsManagementController _accountsManager;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   */
  public MainFrameController()
  {
    _toonsManager=new ToonsManagementController(this);
    _accountsManager=new AccountsManagementController(this);
    _windowsManager=new WindowsManager();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("LOTRO Companion");
    frame.setSize(810,400);
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

    // Compendium
    JMenu compendiumMenu=GuiFactory.buildMenu("Compendium");
    // - map
    JMenuItem map=GuiFactory.buildMenuItem("Middle-earth Map");
    map.setActionCommand(MAP_ID);
    map.addActionListener(this);
    compendiumMenu.add(map);
    // - deeds
    JMenuItem deedsExplorer=GuiFactory.buildMenuItem("Deeds");
    deedsExplorer.setActionCommand(DEEDS_ID);
    deedsExplorer.addActionListener(this);
    compendiumMenu.add(deedsExplorer);
    // - quests
    JMenuItem questsExplorer=GuiFactory.buildMenuItem("Quests");
    questsExplorer.setActionCommand(QUESTS_ID);
    questsExplorer.addActionListener(this);
    compendiumMenu.add(questsExplorer);
    // - recipes
    JMenuItem recipesExplorer=GuiFactory.buildMenuItem("Recipes");
    recipesExplorer.setActionCommand(RECIPES_ID);
    recipesExplorer.addActionListener(this);
    compendiumMenu.add(recipesExplorer);
    // - titles
    JMenuItem titlesExplorer=GuiFactory.buildMenuItem("Titles");
    titlesExplorer.setActionCommand(TITLES_ID);
    titlesExplorer.addActionListener(this);
    compendiumMenu.add(titlesExplorer);
    // - emotes
    JMenuItem emotesExplorer=GuiFactory.buildMenuItem("Emotes");
    emotesExplorer.setActionCommand(EMOTES_ID);
    emotesExplorer.addActionListener(this);
    compendiumMenu.add(emotesExplorer);
    // - mounts
    JMenuItem mountsExplorer=GuiFactory.buildMenuItem("Mounts");
    mountsExplorer.setActionCommand(MOUNTS_ID);
    mountsExplorer.addActionListener(this);
    compendiumMenu.add(mountsExplorer);
    // - pets
    JMenuItem petsExplorer=GuiFactory.buildMenuItem("Pets");
    petsExplorer.setActionCommand(PETS_ID);
    petsExplorer.addActionListener(this);
    compendiumMenu.add(petsExplorer);
    // - vendors
    JMenuItem vendorsExplorer=GuiFactory.buildMenuItem("Vendors");
    vendorsExplorer.setActionCommand(VENDORS_ID);
    vendorsExplorer.addActionListener(this);
    compendiumMenu.add(vendorsExplorer);
    // - barterers
    JMenuItem barterersExplorer=GuiFactory.buildMenuItem("Barterers");
    barterersExplorer.setActionCommand(BARTERERS_ID);
    barterersExplorer.addActionListener(this);
    compendiumMenu.add(barterersExplorer);

    // Help
    JMenu helpMenu=GuiFactory.buildMenu("Help");
    // - about
    JMenuItem aboutMenuItem=GuiFactory.buildMenuItem("About...");
    ActionListener alAbout=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doAbout();
      }
    };
    aboutMenuItem.addActionListener(alAbout);
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
    menuBar.add(Box.createHorizontalGlue());
    menuBar.add(helpMenu);
    return menuBar;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    // Characters
    JPanel toonsPanel=_toonsManager.getPanel();
    tabbedPane.add("Characters",toonsPanel);
    // Accounts
    JPanel accountsPanel=_accountsManager.getPanel();
    tabbedPane.add("Accounts",accountsPanel);
    ret.add(tabbedPane,BorderLayout.CENTER);
    return ret;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Levelling icon
    String levellingIconPath=getToolbarIconPath("levelling");
    ToolbarIconItem levellingIconItem=new ToolbarIconItem(LEVELLING_ID,levellingIconPath,LEVELLING_ID,"Levelling...","Levelling");
    model.addToolbarIconItem(levellingIconItem);
    // Warbands icon
    String warbandsIconPath=getToolbarIconPath("warbands");
    ToolbarIconItem warbandsIconItem=new ToolbarIconItem(WARBANDS_ID,warbandsIconPath,WARBANDS_ID,"Warbands...","Warbands");
    model.addToolbarIconItem(warbandsIconItem);
    // Reputation synopsis icon
    String reputationSynopsisIconPath=getToolbarIconPath("reputation");
    ToolbarIconItem reputationSynopsisIconItem=new ToolbarIconItem(REPUTATION_SYNOPSIS_ID,reputationSynopsisIconPath,REPUTATION_SYNOPSIS_ID,"Reputation synopsis...","Reputation synopsis");
    model.addToolbarIconItem(reputationSynopsisIconItem);
    // Crafting synopsis icon
    String craftingSynopsisIconPath=getToolbarIconPath("crafting");
    ToolbarIconItem craftingSynopsisIconItem=new ToolbarIconItem(CRAFTING_SYNOPSIS_ID,craftingSynopsisIconPath,CRAFTING_SYNOPSIS_ID,"Crafting synopsis...","Crafting synopsis");
    model.addToolbarIconItem(craftingSynopsisIconItem);
    // Map icon
    String mapIconPath=getToolbarIconPath("globe");
    ToolbarIconItem mapIconItem=new ToolbarIconItem(MAP_ID,mapIconPath,MAP_ID,"Map...","Map");
    model.addToolbarIconItem(mapIconItem);
    // Deeds icon
    String deedsIconPath=getToolbarIconPath("deeds");
    ToolbarIconItem deedsIconItem=new ToolbarIconItem(DEEDS_ID,deedsIconPath,DEEDS_ID,"Deeds...","Deeds");
    model.addToolbarIconItem(deedsIconItem);
    // Quests icon
    String questsIconPath=getToolbarIconPath("quests");
    ToolbarIconItem questsIconItem=new ToolbarIconItem(QUESTS_ID,questsIconPath,QUESTS_ID,"Quests...","Quests");
    model.addToolbarIconItem(questsIconItem);
    // Recipes icon
    String recipesIconPath=getToolbarIconPath("recipes");
    ToolbarIconItem recipesIconItem=new ToolbarIconItem(RECIPES_ID,recipesIconPath,RECIPES_ID,"Recipes...","Recipes");
    model.addToolbarIconItem(recipesIconItem);
    // Titles icon
    String titlesIconPath=getToolbarIconPath("titles");
    ToolbarIconItem titlesIconItem=new ToolbarIconItem(TITLES_ID,titlesIconPath,TITLES_ID,"Titles...","Titles");
    model.addToolbarIconItem(titlesIconItem);
    // Emotes icon
    String emotesIconPath=getToolbarIconPath("emotes");
    ToolbarIconItem emotesIconItem=new ToolbarIconItem(EMOTES_ID,emotesIconPath,EMOTES_ID,"Emotes...","Emotes");
    model.addToolbarIconItem(emotesIconItem);
    // Mounts icon
    String mountsIconPath=getToolbarIconPath("mounts");
    ToolbarIconItem mountsIconItem=new ToolbarIconItem(MOUNTS_ID,mountsIconPath,MOUNTS_ID,"Mounts...","Mounts");
    model.addToolbarIconItem(mountsIconItem);
    // Pets icon
    String petsIconPath=getToolbarIconPath("pets");
    ToolbarIconItem petsIconItem=new ToolbarIconItem(PETS_ID,petsIconPath,PETS_ID,"Pets...","Pets");
    model.addToolbarIconItem(petsIconItem);
    // Vendors icon
    String vendorsIconPath=getToolbarIconPath("vendors");
    ToolbarIconItem vendorsIconItem=new ToolbarIconItem(VENDORS_ID,vendorsIconPath,VENDORS_ID,"Vendors...","Vendors");
    model.addToolbarIconItem(vendorsIconItem);
    // Barterers icon
    String barterersIconPath=getToolbarIconPath("barterers");
    ToolbarIconItem barterersIconItem=new ToolbarIconItem(BARTERERS_ID,barterersIconPath,BARTERERS_ID,"Barterers...","Barterers");
    model.addToolbarIconItem(barterersIconItem);
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
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
      MapsManager mapsManager=Maps.getMaps().getMapsManager();
      controller=new MapWindowController(mapsManager);
      _windowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(getFrame());
    }
    controller.bringToFront();
  }

  private void doDeeds()
  {
    WindowController controller=_windowsManager.getWindow(DeedsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new DeedsExplorerWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doQuests()
  {
    WindowController controller=_windowsManager.getWindow(QuestsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new QuestsExplorerWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doRecipes()
  {
    WindowController controller=_windowsManager.getWindow(RecipesExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new RecipesExplorerWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doTitles()
  {
    WindowController controller=_windowsManager.getWindow(TitlesExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new TitlesExplorerWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doEmotes()
  {
    WindowController controller=_windowsManager.getWindow(EmotesExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new EmotesExplorerWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doMounts()
  {
    WindowController controller=_windowsManager.getWindow(MountsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new MountsExplorerWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doPets()
  {
    WindowController controller=_windowsManager.getWindow(PetsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new PetsExplorerWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doVendors()
  {
    WindowController controller=_windowsManager.getWindow(VendorsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new VendorsExplorerWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doBarterers()
  {
    WindowController controller=_windowsManager.getWindow(BarterersExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new BarterersExplorerWindowController(this);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
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
    else if (DEEDS_ID.equals(cmd))
    {
      doDeeds();
    }
    else if (QUESTS_ID.equals(cmd))
    {
      doQuests();
    }
    else if (RECIPES_ID.equals(cmd))
    {
      doRecipes();
    }
    else if (TITLES_ID.equals(cmd))
    {
      doTitles();
    }
    else if (EMOTES_ID.equals(cmd))
    {
      doEmotes();
    }
    else if (MOUNTS_ID.equals(cmd))
    {
      doMounts();
    }
    else if (PETS_ID.equals(cmd))
    {
      doPets();
    }
    else if (VENDORS_ID.equals(cmd))
    {
      doVendors();
    }
    else if (BARTERERS_ID.equals(cmd))
    {
      doBarterers();
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
    if (_toolbar!=null)
    {
      _toolbar.dispose();
      _toolbar=null;
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
  }
}
