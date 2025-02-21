package delta.games.lotro.gui.character.main;

import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.io.DeedsStatusIo;
import delta.games.lotro.character.status.achievables.io.QuestsStatusIo;
import delta.games.lotro.character.status.allegiances.AllegiancesStatusManager;
import delta.games.lotro.character.status.allegiances.io.AllegiancesStatusIo;
import delta.games.lotro.character.status.crafting.CraftingStatus;
import delta.games.lotro.character.status.notes.CharacterNotes;
import delta.games.lotro.character.status.notes.io.CharacterNotesIo;
import delta.games.lotro.character.status.recipes.RecipesStatusManager;
import delta.games.lotro.character.status.relics.RelicsInventory;
import delta.games.lotro.character.status.relics.RelicsInventoryManager;
import delta.games.lotro.character.status.relics.io.RelicsInventoryIo;
import delta.games.lotro.character.status.skirmishes.SkirmishEntriesManager;
import delta.games.lotro.character.status.skirmishes.SkirmishStatsManager;
import delta.games.lotro.character.status.skirmishes.io.SkirmishStatsIo;
import delta.games.lotro.character.status.tasks.TasksStatusManager;
import delta.games.lotro.character.status.titles.TitlesStatusManager;
import delta.games.lotro.character.status.titles.io.TitlesStatusIo;
import delta.games.lotro.character.status.traitTree.TraitTreeStatus;
import delta.games.lotro.gui.character.cosmetics.OutfitsDisplayWindowController;
import delta.games.lotro.gui.character.log.CharacterLogWindowController;
import delta.games.lotro.gui.character.stash.StashWindowController;
import delta.games.lotro.gui.character.status.allegiances.summary.AllegiancesStatusSummaryWindowController;
import delta.games.lotro.gui.character.status.collections.CollectablesStatusWindowController;
import delta.games.lotro.gui.character.status.collections.CollectablesStatusWindowController.STATUS_TYPE;
import delta.games.lotro.gui.character.status.crafting.CraftingWindowController;
import delta.games.lotro.gui.character.status.currencies.SingleCharacterCurrencyHistoryWindowController;
import delta.games.lotro.gui.character.status.deeds.DeedsStatusWindowController;
import delta.games.lotro.gui.character.status.emotes.EmotesStatusWindowController;
import delta.games.lotro.gui.character.status.hobbies.HobbiesStatusWindowController;
import delta.games.lotro.gui.character.status.housing.CharacterHousingStatusWindowController;
import delta.games.lotro.gui.character.status.levelling.LevelHistoryEditionDialogController;
import delta.games.lotro.gui.character.status.notes.CharacterNotesEditionDialogController;
import delta.games.lotro.gui.character.status.pvp.PvpStatusWindowController;
import delta.games.lotro.gui.character.status.quests.QuestsStatusWindowController;
import delta.games.lotro.gui.character.status.recipes.RecipesStatusWindowController;
import delta.games.lotro.gui.character.status.relics.RelicsInventoryWindowController;
import delta.games.lotro.gui.character.status.reputation.CharacterReputationDialogController;
import delta.games.lotro.gui.character.status.skirmishes.SkirmishStatisticsWindowController;
import delta.games.lotro.gui.character.status.tasks.TasksStatusWindowController;
import delta.games.lotro.gui.character.status.titles.TitlesStatusWindowController;
import delta.games.lotro.gui.character.status.traits.mountedAppearances.MountedAppearancesStatusWindowController;
import delta.games.lotro.gui.character.status.traits.skirmish.SkirmishTraitsStatusWindowController;
import delta.games.lotro.gui.character.status.travels.TravelsStatusWindowController;
import delta.games.lotro.gui.character.storage.own.CharacterStorageDisplayWindowController;
import delta.games.lotro.gui.character.traitTree.TraitTreeWindowController;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Handle commands from the main character window.
 * @author DAM
 */
public class MainCharacterWindowCommandsManager
{
  private WindowController _parent;
  private CharacterFile _toon;

  /**
   * Constructor.
   * @param parent Parent window (main character window)/
   * @param toon Character to use.
   */
  public MainCharacterWindowCommandsManager(WindowController parent, CharacterFile toon)
  {
    _parent=parent;
    _toon=toon;
  }

  /**
   * Handle a character command.
   * @param command Command ID.
   */
  public void handleCommand(String command)
  {
    if (MainCharacterWindowCommands.LOG_COMMAND.equals(command))
    {
      // Show log
      String serverName=_toon.getServerName();
      String toonName=_toon.getName();
      String id=CharacterLogWindowController.getIdentifier(serverName,toonName);
      WindowsManager windowsManager=getWindowsManager();
      WindowController controller=windowsManager.getWindow(id);
      if (controller==null)
      {
        controller=new CharacterLogWindowController(_toon);
        windowsManager.registerWindow(controller);
        controller.getWindow().setLocationRelativeTo(_parent.getWindow());
      }
      controller.bringToFront();
    }
    else if (MainCharacterWindowCommands.LEVEL_COMMAND.equals(command))
    {
      // Level history
      LevelHistoryEditionDialogController controller=new LevelHistoryEditionDialogController(_parent,_toon);
      controller.editModal();
    }
    else if (MainCharacterWindowCommands.HOUSING_COMMAND.equals(command))
    {
      // Housing
      String id=CharacterHousingStatusWindowController.IDENTIFIER;
      WindowsManager windowsManager=getWindowsManager();
      WindowController controller=windowsManager.getWindow(id);
      if (controller==null)
      {
        controller=new CharacterHousingStatusWindowController(_parent,_toon);
        windowsManager.registerWindow(controller);
        controller.getWindow().setLocationRelativeTo(_parent.getWindow());
      }
      controller.bringToFront();
    }
    else if (MainCharacterWindowCommands.REPUTATION_COMMAND.equals(command))
    {
      // Reputation
      CharacterReputationDialogController controller=new CharacterReputationDialogController(_parent,_toon);
      controller.editModal();
    }
    else if (MainCharacterWindowCommands.CRAFTING_COMMAND.equals(command))
    {
      // Crafting
      CraftingWindowController controller=new CraftingWindowController(_parent,_toon);
      controller.editModal();
    }
    else if (MainCharacterWindowCommands.STASH_COMMAND.equals(command))
    {
      showStash();
    }
    else if (MainCharacterWindowCommands.DEEDS_STATUS_COMMAND.equals(command))
    {
      editDeedsStatus();
    }
    else if (MainCharacterWindowCommands.QUESTS_STATUS_COMMAND.equals(command))
    {
      showQuestsStatus();
    }
    else if (MainCharacterWindowCommands.TASKS_STATUS_COMMAND.equals(command))
    {
      showTasksStatus();
    }
    else if (MainCharacterWindowCommands.SKIRMISH_STATS_COMMAND.equals(command))
    {
      showSkirmishStatistics();
    }
    else if (MainCharacterWindowCommands.RELICS_INVENTORY_COMMAND.equals(command))
    {
      showRelicsInventory();
    }
    else if (MainCharacterWindowCommands.RECIPES_STATUS_COMMAND.equals(command))
    {
      showRecipesStatus();
    }
    else if (MainCharacterWindowCommands.TITLES_STATUS_COMMAND.equals(command))
    {
      showTitlesStatus();
    }
    else if (MainCharacterWindowCommands.STORAGE_COMMAND.equals(command))
    {
      showStorage();
    }
    else if (MainCharacterWindowCommands.ALLEGIANCES_COMMAND.equals(command))
    {
      showAllegiancesStatus();
    }
    else if (MainCharacterWindowCommands.TRAVELS_COMMAND.equals(command))
    {
      showTravelsStatus();
    }
    else if (MainCharacterWindowCommands.CURRENCIES_COMMAND.equals(command))
    {
      showCurrencies();
    }
    else if (MainCharacterWindowCommands.OUTFITS_COMMAND.equals(command))
    {
      showOutfits();
    }
    else if (MainCharacterWindowCommands.EMOTES_COMMAND.equals(command))
    {
      showEmotesStatus();
    }
    else if (MainCharacterWindowCommands.MOUNTS_COMMAND.equals(command))
    {
      showCollectablesStatus(STATUS_TYPE.MOUNTS);
    }
    else if (MainCharacterWindowCommands.PETS_COMMAND.equals(command))
    {
      showCollectablesStatus(STATUS_TYPE.PETS);
    }
    else if (MainCharacterWindowCommands.HOBBIES_COMMAND.equals(command))
    {
      showHobbiesStatus();
    }
    else if (MainCharacterWindowCommands.SKIRMISH_TRAITS_COMMAND.equals(command))
    {
      showSkirmishTraitsStatus();
    }
    else if (MainCharacterWindowCommands.NOTES_COMMAND.equals(command))
    {
      editNotes();
    }
    else if (MainCharacterWindowCommands.MOUNTED_APPEARANCES_COMMAND.equals(command))
    {
      showMountedAppearances();
    }
    else if (MainCharacterWindowCommands.PVP_COMMAND.equals(command))
    {
      showPVPStatus();
    }
    else if (MainCharacterWindowCommands.TRAIT_TREE_COMMAND.equals(command))
    {
      showTraitTree();
    }
    else if (MainCharacterWindowCommands.CONFIGS_COMMAND.equals(command))
    {
      showConfigs();
    }
  }

  private void showStash()
  {
    // Stash
    String serverName=_toon.getServerName();
    String toonName=_toon.getName();
    String id=StashWindowController.getIdentifier(serverName,toonName);
    WindowsManager windowsManager=getWindowsManager();
    WindowController controller=windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new StashWindowController(_toon);
      windowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    controller.bringToFront();
  }

  private void editDeedsStatus()
  {
    AchievablesStatusManager status=DeedsStatusIo.load(_toon);
    DeedsStatusWindowController controller=new DeedsStatusWindowController(_parent,status,_toon);
    AchievablesStatusManager newStatus=controller.editModal();
    if (newStatus!=null)
    {
      boolean ok=DeedsStatusIo.save(_toon,newStatus);
      if (ok)
      {
        // Broadcast deeds status update event...
        CharacterEvent event=new CharacterEvent(CharacterEventType.DEEDS_STATUS_UPDATED,_toon,null);
        EventsManager.invokeEvent(event);
      }
    }
  }

  private void showQuestsStatus()
  {
    WindowsManager windowsManager=getWindowsManager();
    QuestsStatusWindowController windowCtrl=(QuestsStatusWindowController)windowsManager.getWindow(QuestsStatusWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      AchievablesStatusManager status=QuestsStatusIo.load(_toon);
      windowCtrl=new QuestsStatusWindowController(_parent,status,_toon);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.bringToFront();
  }

  private void showTasksStatus()
  {
    WindowsManager windowsManager=getWindowsManager();
    TasksStatusWindowController windowCtrl=(TasksStatusWindowController)windowsManager.getWindow(TasksStatusWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      AchievablesStatusManager status=QuestsStatusIo.load(_toon);
      TasksStatusManager tasksStatus=new TasksStatusManager();
      tasksStatus.init(status);
      windowCtrl=new TasksStatusWindowController(_parent,tasksStatus,status,_toon);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.bringToFront();
  }

  private void showSkirmishStatistics()
  {
    WindowsManager windowsManager=getWindowsManager();
    SkirmishStatisticsWindowController windowCtrl=(SkirmishStatisticsWindowController)windowsManager.getWindow(SkirmishStatisticsWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      SkirmishStatsManager status=SkirmishStatsIo.load(_toon);
      SkirmishEntriesManager entriesMgr=new SkirmishEntriesManager(status);
      windowCtrl=new SkirmishStatisticsWindowController(_parent,entriesMgr);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.bringToFront();
  }

  private void showRelicsInventory()
  {
    WindowsManager windowsManager=getWindowsManager();
    RelicsInventoryWindowController windowCtrl=(RelicsInventoryWindowController)windowsManager.getWindow(RelicsInventoryWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      RelicsInventory status=RelicsInventoryIo.load(_toon);
      RelicsInventoryManager relicsStatusMgr=new RelicsInventoryManager();
      relicsStatusMgr.init(status);
      windowCtrl=new RelicsInventoryWindowController(_parent,relicsStatusMgr);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.bringToFront();
  }

  private void showRecipesStatus()
  {
    WindowsManager windowsManager=getWindowsManager();
    RecipesStatusWindowController windowCtrl=(RecipesStatusWindowController)windowsManager.getWindow(RecipesStatusWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      CraftingStatus status=_toon.getCraftingMgr().getCraftingStatus();
      RecipesStatusManager recipesStatusMgr=new RecipesStatusManager();
      recipesStatusMgr.init(status);
      windowCtrl=new RecipesStatusWindowController(_parent,_toon,recipesStatusMgr);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.bringToFront();
  }

  private void showTitlesStatus()
  {
    WindowsManager windowsManager=getWindowsManager();
    TitlesStatusWindowController windowCtrl=(TitlesStatusWindowController)windowsManager.getWindow(TitlesStatusWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      TitlesStatusManager status=TitlesStatusIo.load(_toon);
      windowCtrl=new TitlesStatusWindowController(_parent,status);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.bringToFront();
  }

  private void showStorage()
  {
    WindowsManager windowsManager=getWindowsManager();
    CharacterStorageDisplayWindowController windowCtrl=(CharacterStorageDisplayWindowController)windowsManager.getWindow(CharacterStorageDisplayWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      windowCtrl=new CharacterStorageDisplayWindowController(_parent,_toon);
      windowsManager.registerWindow(windowCtrl);
      windowCtrl.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    windowCtrl.bringToFront();
  }

  private void showAllegiancesStatus()
  {
    WindowsManager windowsManager=getWindowsManager();
    AllegiancesStatusSummaryWindowController windowCtrl=(AllegiancesStatusSummaryWindowController)windowsManager.getWindow(AllegiancesStatusSummaryWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      AllegiancesStatusManager status=AllegiancesStatusIo.load(_toon);
      windowCtrl=new AllegiancesStatusSummaryWindowController(_parent,status);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.bringToFront();
  }

  private void showTravelsStatus()
  {
    WindowsManager windowsManager=getWindowsManager();
    TravelsStatusWindowController windowCtrl=(TravelsStatusWindowController)windowsManager.getWindow(TravelsStatusWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      windowCtrl=new TravelsStatusWindowController(_parent,_toon);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.show();
  }

  private void showCurrencies()
  {
    WindowsManager windowsManager=getWindowsManager();
    SingleCharacterCurrencyHistoryWindowController windowCtrl=(SingleCharacterCurrencyHistoryWindowController)windowsManager.getWindow(SingleCharacterCurrencyHistoryWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      windowCtrl=new SingleCharacterCurrencyHistoryWindowController(_parent,_toon);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.show();
  }

  private void showOutfits()
  {
    WindowsManager windowsManager=getWindowsManager();
    OutfitsDisplayWindowController windowCtrl=(OutfitsDisplayWindowController)windowsManager.getWindow(OutfitsDisplayWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      windowCtrl=new OutfitsDisplayWindowController(_parent,_toon);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.show();
  }

  private void showEmotesStatus()
  {
    WindowsManager windowsManager=getWindowsManager();
    EmotesStatusWindowController windowCtrl=(EmotesStatusWindowController)windowsManager.getWindow(EmotesStatusWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      windowCtrl=new EmotesStatusWindowController(_parent,_toon);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.show();
  }

  private void showCollectablesStatus(STATUS_TYPE type)
  {
    WindowsManager windowsManager=getWindowsManager();
    String id=CollectablesStatusWindowController.getWindowIdentifier(type);
    CollectablesStatusWindowController windowCtrl=(CollectablesStatusWindowController)windowsManager.getWindow(id);
    if (windowCtrl==null)
    {
      windowCtrl=new CollectablesStatusWindowController(_parent,_toon,type);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.show();
  }

  private void showHobbiesStatus()
  {
    WindowsManager windowsManager=getWindowsManager();
    HobbiesStatusWindowController windowCtrl=(HobbiesStatusWindowController)windowsManager.getWindow(HobbiesStatusWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      windowCtrl=new HobbiesStatusWindowController(_parent,_toon);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.show();
  }

  private void showSkirmishTraitsStatus()
  {
    WindowsManager windowsManager=getWindowsManager();
    SkirmishTraitsStatusWindowController windowCtrl=(SkirmishTraitsStatusWindowController)windowsManager.getWindow(SkirmishTraitsStatusWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      windowCtrl=new SkirmishTraitsStatusWindowController(_parent,_toon);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.show();
  }

  private void editNotes()
  {
    CharacterNotes notes=CharacterNotesIo.load(_toon);
    CharacterNotesEditionDialogController editDialog=new CharacterNotesEditionDialogController(_parent,notes);
    notes=editDialog.editModal();
    if (notes!=null)
    {
      CharacterNotesIo.save(_toon,notes);
    }
  }

  private void showMountedAppearances()
  {
    WindowsManager windowsManager=getWindowsManager();
    MountedAppearancesStatusWindowController windowCtrl=(MountedAppearancesStatusWindowController)windowsManager.getWindow(MountedAppearancesStatusWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      windowCtrl=new MountedAppearancesStatusWindowController(_parent,_toon);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.show();
  }

  private void showPVPStatus()
  {
    WindowsManager windowsManager=getWindowsManager();
    PvpStatusWindowController windowCtrl=(PvpStatusWindowController)windowsManager.getWindow(PvpStatusWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      windowCtrl=new PvpStatusWindowController(_parent,_toon);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.show();
  }

  private void showTraitTree()
  {
    WindowsManager windowsManager=getWindowsManager();
    TraitTreeWindowController windowCtrl=(TraitTreeWindowController)windowsManager.getWindow(TraitTreeWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      CharacterData data=_toon.getInfosManager().getCurrentData();
      TraitTreeStatus traitTreeStatus=data.getTraits().getTraitTreeStatus();
      int level=data.getLevel();
      windowCtrl=new TraitTreeWindowController(_parent,level,traitTreeStatus);
      windowsManager.registerWindow(windowCtrl);
    }
    windowCtrl.show();
  }

  private void showConfigs()
  {
    WindowsManager windowsManager=getWindowsManager();
    CharacterConfigsWindowController windowCtrl=(CharacterConfigsWindowController)windowsManager.getWindow(CharacterConfigsWindowController.IDENTIFIER);
    if (windowCtrl==null)
    {
      windowCtrl=new CharacterConfigsWindowController(_toon);
      windowsManager.registerWindow(windowCtrl);
      windowCtrl.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    windowCtrl.show();
  }

  
  private WindowsManager getWindowsManager()
  {
    return _parent.getWindowsManager();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    _toon=null;
  }
}
