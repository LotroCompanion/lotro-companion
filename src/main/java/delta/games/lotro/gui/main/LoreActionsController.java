package delta.games.lotro.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.lore.agents.mobs.explorer.MobsExplorerWindowController;
import delta.games.lotro.gui.lore.collections.mounts.explorer.MountsExplorerWindowController;
import delta.games.lotro.gui.lore.collections.pets.explorer.PetsExplorerWindowController;
import delta.games.lotro.gui.lore.crafting.recipes.explorer.RecipesExplorerWindowController;
import delta.games.lotro.gui.lore.deeds.explorer.DeedsExplorerWindowController;
import delta.games.lotro.gui.lore.emotes.explorer.EmotesExplorerWindowController;
import delta.games.lotro.gui.lore.items.explorer.ItemsExplorerWindowController;
import delta.games.lotro.gui.lore.quests.explorer.QuestsExplorerWindowController;
import delta.games.lotro.gui.lore.titles.explorer.TitlesExplorerWindowController;
import delta.games.lotro.gui.lore.trade.barter.explorer.BarterersExplorerWindowController;
import delta.games.lotro.gui.lore.trade.vendor.explorer.VendorsExplorerWindowController;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for the 'lore' actions (toolbar and menu).
 * @author DAM
 */
public class LoreActionsController implements ActionListener,Disposable
{
  private static final String ITEMS_ID="itemsCommand";
  private static final String MOBS_ID="mobsCommand";
  private static final String DEEDS_ID="deedsCommand";
  private static final String QUESTS_ID="questsCommand";
  private static final String RECIPES_ID="recipesCommand";
  private static final String TITLES_ID="titlesCommand";
  private static final String EMOTES_ID="emotesCommand";
  private static final String MOUNTS_ID="mountsCommand";
  private static final String PETS_ID="petsCommand";
  private static final String VENDORS_ID="vendorsCommand";
  private static final String BARTERERS_ID="barterersCommand";

  private WindowController _parent;
  private WindowsManager _windowsManager;
  private ToolbarController _loreToolbar;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param windowsManager Windows manager.
   */
  public LoreActionsController(WindowController parent, WindowsManager windowsManager)
  {
    _parent=parent;
    _windowsManager=windowsManager;
  }

  /**
   * Build the 'compendium' menu.
   * @param isLive Live mode or not.
   * @return a new menu.
   */
  public JMenu buildCompendiumMenu(boolean isLive)
  {
    // Compendium
    JMenu compendiumMenu=GuiFactory.buildMenu(Labels.getLabel("main.window.menu.compendium"));
    // - items
    JMenuItem itemsExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.items"));
    itemsExplorer.setActionCommand(ITEMS_ID);
    itemsExplorer.addActionListener(this);
    compendiumMenu.add(itemsExplorer);
    // - mobs
    JMenuItem mobsExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.mobs"));
    mobsExplorer.setActionCommand(MOBS_ID);
    mobsExplorer.addActionListener(this);
    compendiumMenu.add(mobsExplorer);
    // - deeds
    JMenuItem deedsExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.deeds"));
    deedsExplorer.setActionCommand(DEEDS_ID);
    deedsExplorer.addActionListener(this);
    compendiumMenu.add(deedsExplorer);
    // - quests
    JMenuItem questsExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.quests"));
    questsExplorer.setActionCommand(QUESTS_ID);
    questsExplorer.addActionListener(this);
    compendiumMenu.add(questsExplorer);
    // - recipes
    JMenuItem recipesExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.recipes"));
    recipesExplorer.setActionCommand(RECIPES_ID);
    recipesExplorer.addActionListener(this);
    compendiumMenu.add(recipesExplorer);
    // - titles
    JMenuItem titlesExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.titles"));
    titlesExplorer.setActionCommand(TITLES_ID);
    titlesExplorer.addActionListener(this);
    compendiumMenu.add(titlesExplorer);
    // - emotes
    JMenuItem emotesExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.emotes"));
    emotesExplorer.setActionCommand(EMOTES_ID);
    emotesExplorer.addActionListener(this);
    compendiumMenu.add(emotesExplorer);
    if (isLive)
    {
      // - mounts
      JMenuItem mountsExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.mounts"));
      mountsExplorer.setActionCommand(MOUNTS_ID);
      mountsExplorer.addActionListener(this);
      compendiumMenu.add(mountsExplorer);
      // - pets
      JMenuItem petsExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.pets"));
      petsExplorer.setActionCommand(PETS_ID);
      petsExplorer.addActionListener(this);
      compendiumMenu.add(petsExplorer);
    }
    // - vendors
    JMenuItem vendorsExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.vendors"));
    vendorsExplorer.setActionCommand(VENDORS_ID);
    vendorsExplorer.addActionListener(this);
    compendiumMenu.add(vendorsExplorer);
    // - barterers
    JMenuItem barterersExplorer=GuiFactory.buildMenuItem(Labels.getLabel("main.window.menuitem.barterers"));
    barterersExplorer.setActionCommand(BARTERERS_ID);
    barterersExplorer.addActionListener(this);
    compendiumMenu.add(barterersExplorer);

    return compendiumMenu;
  }

  /**
   * Build the 'lore' toolbar.
   * @param isLive Live mode or not.
   * @return a new toobar.
   */
  public ToolbarController buildToolbarLore(boolean isLive)
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // Items icon
    String itemsIconPath=SharedUiUtils.getToolbarIconPath("items");
    String itemsTooltip=Labels.getLabel("main.window.toolbar.items.tooltip");
    String itemsAltText=Labels.getLabel("main.window.toolbar.items.altText");
    ToolbarIconItem itemsIconItem=new ToolbarIconItem(ITEMS_ID,itemsIconPath,ITEMS_ID,itemsTooltip,itemsAltText);
    model.addToolbarIconItem(itemsIconItem);
    // Mobs icon
    String mobsIconPath=SharedUiUtils.getToolbarIconPath("mobs");
    String mobsTooltip=Labels.getLabel("main.window.toolbar.mobs.tooltip");
    String mobsAltText=Labels.getLabel("main.window.toolbar.mobs.altText");
    ToolbarIconItem mobsIconItem=new ToolbarIconItem(MOBS_ID,mobsIconPath,MOBS_ID,mobsTooltip,mobsAltText);
    model.addToolbarIconItem(mobsIconItem);
    // Deeds icon
    String deedsIconPath=SharedUiUtils.getToolbarIconPath("deeds");
    String deedsTooltip=Labels.getLabel("main.window.toolbar.deeds.tooltip");
    String deedsAltText=Labels.getLabel("main.window.toolbar.deeds.altText");
    ToolbarIconItem deedsIconItem=new ToolbarIconItem(DEEDS_ID,deedsIconPath,DEEDS_ID,deedsTooltip,deedsAltText);
    model.addToolbarIconItem(deedsIconItem);
    // Quests icon
    String questsIconPath=SharedUiUtils.getToolbarIconPath("quests");
    String questsTooltip=Labels.getLabel("main.window.toolbar.quests.tooltip");
    String questsAltText=Labels.getLabel("main.window.toolbar.quests.altText");
    ToolbarIconItem questsIconItem=new ToolbarIconItem(QUESTS_ID,questsIconPath,QUESTS_ID,questsTooltip,questsAltText);
    model.addToolbarIconItem(questsIconItem);
    // Recipes icon
    String recipesIconPath=SharedUiUtils.getToolbarIconPath("recipes");
    String recipesTooltip=Labels.getLabel("main.window.toolbar.recipes.tooltip");
    String recipesAltText=Labels.getLabel("main.window.toolbar.recipes.altText");
    ToolbarIconItem recipesIconItem=new ToolbarIconItem(RECIPES_ID,recipesIconPath,RECIPES_ID,recipesTooltip,recipesAltText);
    model.addToolbarIconItem(recipesIconItem);
    // Titles icon
    String titlesIconPath=SharedUiUtils.getToolbarIconPath("titles");
    String titlesTooltip=Labels.getLabel("main.window.toolbar.titles.tooltip");
    String titlesAltText=Labels.getLabel("main.window.toolbar.titles.altText");
    ToolbarIconItem titlesIconItem=new ToolbarIconItem(TITLES_ID,titlesIconPath,TITLES_ID,titlesTooltip,titlesAltText);
    model.addToolbarIconItem(titlesIconItem);
    // Emotes icon
    String emotesIconPath=SharedUiUtils.getToolbarIconPath("emotes");
    String emotesTooltip=Labels.getLabel("main.window.toolbar.emotes.tooltip");
    String emotesAltText=Labels.getLabel("main.window.toolbar.emotes.altText");
    ToolbarIconItem emotesIconItem=new ToolbarIconItem(EMOTES_ID,emotesIconPath,EMOTES_ID,emotesTooltip,emotesAltText);
    model.addToolbarIconItem(emotesIconItem);
    if (isLive)
    {
      // Mounts icon
      String mountsIconPath=SharedUiUtils.getToolbarIconPath("mounts");
      String mountsTooltip=Labels.getLabel("main.window.toolbar.mounts.tooltip");
      String mountsAltText=Labels.getLabel("main.window.toolbar.mounts.altText");
      ToolbarIconItem mountsIconItem=new ToolbarIconItem(MOUNTS_ID,mountsIconPath,MOUNTS_ID,mountsTooltip,mountsAltText);
      model.addToolbarIconItem(mountsIconItem);
      // Pets icon
      String petsIconPath=SharedUiUtils.getToolbarIconPath("pets");
      String petsTooltip=Labels.getLabel("main.window.toolbar.pets.tooltip");
      String petsAltText=Labels.getLabel("main.window.toolbar.pets.altText");
      ToolbarIconItem petsIconItem=new ToolbarIconItem(PETS_ID,petsIconPath,PETS_ID,petsTooltip,petsAltText);
      model.addToolbarIconItem(petsIconItem);
    }
    // Vendors icon
    String vendorsIconPath=SharedUiUtils.getToolbarIconPath("vendors");
    String vendorsTooltip=Labels.getLabel("main.window.toolbar.vendors.tooltip");
    String vendorsAltText=Labels.getLabel("main.window.toolbar.vendors.altText");
    ToolbarIconItem vendorsIconItem=new ToolbarIconItem(VENDORS_ID,vendorsIconPath,VENDORS_ID,vendorsTooltip,vendorsAltText);
    model.addToolbarIconItem(vendorsIconItem);
    // Barterers icon
    String barterersIconPath=SharedUiUtils.getToolbarIconPath("barterers");
    String barterersTooltip=Labels.getLabel("main.window.toolbar.barterers.tooltip");
    String barterersAltText=Labels.getLabel("main.window.toolbar.barterers.altText");
    ToolbarIconItem barterersIconItem=new ToolbarIconItem(BARTERERS_ID,barterersIconPath,BARTERERS_ID,barterersTooltip,barterersAltText);
    model.addToolbarIconItem(barterersIconItem);
    // Border
    String border=Labels.getLabel("main.window.toolbar.border.compendium");
    controller.getToolBar().setBorder(GuiFactory.buildTitledBorder(border));
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  @Override
  public void actionPerformed(ActionEvent event)
  {
    String cmd=event.getActionCommand();
    if (ITEMS_ID.equals(cmd))
    {
      doItems();
    }
    else if (MOBS_ID.equals(cmd))
    {
      doMobs();
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

  private void doItems()
  {
    WindowController controller=_windowsManager.getWindow(ItemsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new ItemsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doMobs()
  {
    WindowController controller=_windowsManager.getWindow(MobsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new MobsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doDeeds()
  {
    WindowController controller=_windowsManager.getWindow(DeedsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new DeedsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doQuests()
  {
    WindowController controller=_windowsManager.getWindow(QuestsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new QuestsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doRecipes()
  {
    WindowController controller=_windowsManager.getWindow(RecipesExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new RecipesExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doTitles()
  {
    WindowController controller=_windowsManager.getWindow(TitlesExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new TitlesExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doEmotes()
  {
    WindowController controller=_windowsManager.getWindow(EmotesExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new EmotesExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doMounts()
  {
    WindowController controller=_windowsManager.getWindow(MountsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new MountsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doPets()
  {
    WindowController controller=_windowsManager.getWindow(PetsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new PetsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doVendors()
  {
    WindowController controller=_windowsManager.getWindow(VendorsExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new VendorsExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  private void doBarterers()
  {
    WindowController controller=_windowsManager.getWindow(BarterersExplorerWindowController.IDENTIFIER);
    if (controller==null)
    {
      controller=new BarterersExplorerWindowController(_parent);
      _windowsManager.registerWindow(controller);
    }
    controller.bringToFront();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_loreToolbar!=null)
    {
      _loreToolbar.dispose();
      _loreToolbar=null;
    }
  }
}
