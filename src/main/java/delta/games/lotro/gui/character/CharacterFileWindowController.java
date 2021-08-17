package delta.games.lotro.gui.character;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFactory;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterInfosManager;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.io.xml.CharacterDataIO;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.io.DeedsStatusIo;
import delta.games.lotro.character.status.achievables.io.QuestsStatusIo;
import delta.games.lotro.character.status.skirmishes.SkirmishStatsManager;
import delta.games.lotro.character.status.skirmishes.io.SkirmishStatsIo;
import delta.games.lotro.character.status.tasks.TasksStatusManager;
import delta.games.lotro.character.status.titles.TitlesStatusManager;
import delta.games.lotro.character.status.titles.io.TitlesStatusIo;
import delta.games.lotro.character.status.traitPoints.TraitPoints;
import delta.games.lotro.character.status.traitPoints.TraitPointsStatus;
import delta.games.lotro.gui.character.log.CharacterLogWindowController;
import delta.games.lotro.gui.character.stash.StashWindowController;
import delta.games.lotro.gui.character.status.crafting.CraftingWindowController;
import delta.games.lotro.gui.character.status.deeds.DeedsStatusWindowController;
import delta.games.lotro.gui.character.status.levelling.LevelHistoryEditionDialogController;
import delta.games.lotro.gui.character.status.quests.QuestsStatusWindowController;
import delta.games.lotro.gui.character.status.reputation.CharacterReputationDialogController;
import delta.games.lotro.gui.character.status.skirmishes.SkirmishStatisticsWindowController;
import delta.games.lotro.gui.character.status.tasks.TasksStatusWindowController;
import delta.games.lotro.gui.character.status.titles.TitlesStatusWindowController;
import delta.games.lotro.gui.character.status.traitPoints.TraitPointsEditionWindowController;
import delta.games.lotro.gui.character.storage.own.CharacterStorageDisplayWindowController;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.gui.filechooser.FileChooserController;

/**
 * Controller for a "character" window.
 * @author DAM
 */
public class CharacterFileWindowController extends DefaultWindowController implements ActionListener
{
  private static final String NEW_TOON_DATA_ID="newToonData";
  private static final String CLONE_TOON_DATA_ID="cloneToonData";
  private static final String EXPORT_TOON_DATA_ID="exportToonData";
  private static final String REMOVE_TOON_DATA_ID="removeToonData";
  private static final String LOG_COMMAND="log";
  private static final String LEVEL_COMMAND="level";
  private static final String REPUTATION_COMMAND="reputation";
  private static final String CRAFTING_COMMAND="crafting";
  private static final String STASH_COMMAND="stash";
  private static final String TRAIT_POINTS_COMMAND="traitPoints";
  private static final String DEEDS_STATUS_COMMAND="deedsStatus";
  private static final String QUESTS_STATUS_COMMAND="questsStatus";
  private static final String TASKS_STATUS_COMMAND="tasksStatus";
  private static final String SKIRMISH_STATS_COMMAND="skirmishStats";
  private static final String TITLES_STATUS_COMMAND="titlesStatus";
  private static final String STORAGE_COMMAND="storage";

  private CharacterSummaryPanelController _summaryController;
  private CharacterDataTableController _toonsTable;
  private ToolbarController _toolbar;
  private CharacterFile _toon;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterFileWindowController(CharacterFile toon)
  {
    _toon=toon;
    _windowsManager=new WindowsManager();
    _summaryController=new CharacterSummaryPanelController(this,_toon);
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
    // Summary panel
    JPanel summaryPanel=_summaryController.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(3,5,3,5),0,0);
    panel.add(summaryPanel,c);
    // Character data table
    JPanel tablePanel=buildTablePanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    tablePanel.setBorder(GuiFactory.buildTitledBorder("Configurations"));
    // Command buttons
    JPanel commandsPanel=buildCommandsPanel();
    c=new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
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
    String title="Character: "+name+" @ "+serverName;
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

  private JPanel buildCommandsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Log
    JButton logButton=buildCommandButton("Log",LOG_COMMAND);
    panel.add(logButton,c);
    c.gridx++;
    // Reputation
    JButton reputationButton=buildCommandButton("Reputation",REPUTATION_COMMAND);
    panel.add(reputationButton,c);
    c.gridx++;
    // Levels
    JButton levelsButton=buildCommandButton("Levels",LEVEL_COMMAND);
    panel.add(levelsButton,c);
    c.gridx++;
    // Crafting
    JButton craftingButton=buildCommandButton("Crafting",CRAFTING_COMMAND);
    panel.add(craftingButton,c);
    c.gridx++;
    // Stash
    JButton stashButton=buildCommandButton("Stash",STASH_COMMAND);
    panel.add(stashButton,c);
    c.gridx++;
    // Trait points
    JButton traitPointsButton=buildCommandButton("Trait points",TRAIT_POINTS_COMMAND);
    panel.add(traitPointsButton,c);
    c.gridx++;
    // Deeds status
    JButton deedsButton=buildCommandButton("Deeds",DEEDS_STATUS_COMMAND);
    panel.add(deedsButton,c);
    c.gridx++;
    // Quests status
    JButton questsButton=buildCommandButton("Quests",QUESTS_STATUS_COMMAND);
    panel.add(questsButton,c);
    c.gridx++;
    // Tasks status
    JButton tasksButton=buildCommandButton("Tasks",TASKS_STATUS_COMMAND);
    panel.add(tasksButton,c);
    c.gridx++;
    // Skirmish statistics
    JButton skirmishsButton=buildCommandButton("Skirmishs",SKIRMISH_STATS_COMMAND);
    panel.add(skirmishsButton,c);
    c.gridx++;
    // Titles status
    JButton titlesButton=buildCommandButton("Titles",TITLES_STATUS_COMMAND);
    panel.add(titlesButton,c);
    c.gridx++;
    // Storage
    JButton storageButton=buildCommandButton("Storage",STORAGE_COMMAND);
    panel.add(storageButton,c);
    c.gridx++;

    // Disable buttons if no log
    boolean hasLog=_toon.hasLog();
    logButton.setEnabled(hasLog);

    return panel;
  }

  private JButton buildCommandButton(String label, String command)
  {
    JButton b=GuiFactory.buildButton(label);
    b.setActionCommand(command);
    b.addActionListener(this);
    return b;
  }

  /**
   * Handle button actions.
   * @param e Source event.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    String command=e.getActionCommand();
    if (LOG_COMMAND.equals(command))
    {
      // Show log
      String serverName=_toon.getServerName();
      String toonName=_toon.getName();
      String id=CharacterLogWindowController.getIdentifier(serverName,toonName);
      WindowController controller=_windowsManager.getWindow(id);
      if (controller==null)
      {
        controller=new CharacterLogWindowController(_toon);
        _windowsManager.registerWindow(controller);
        controller.getWindow().setLocationRelativeTo(getFrame());
      }
      controller.bringToFront();
    }
    else if (REPUTATION_COMMAND.equals(command))
    {
      // Reputation
      CharacterReputationDialogController controller=new CharacterReputationDialogController(this,_toon);
      controller.editModal();
    }
    else if (LEVEL_COMMAND.equals(command))
    {
      // Level history
      LevelHistoryEditionDialogController controller=new LevelHistoryEditionDialogController(this,_toon);
      controller.editModal();
    }
    else if (CRAFTING_COMMAND.equals(command))
    {
      // Crafting
      CraftingWindowController controller=new CraftingWindowController(this,_toon);
      controller.editModal();
    }
    else if (STASH_COMMAND.equals(command))
    {
      showStash();
    }
    else if (TRAIT_POINTS_COMMAND.equals(command))
    {
      editTraitPoints();
    }
    else if (DEEDS_STATUS_COMMAND.equals(command))
    {
      editDeedsStatus();
    }
    else if (QUESTS_STATUS_COMMAND.equals(command))
    {
      showQuestsStatus();
    }
    else if (TASKS_STATUS_COMMAND.equals(command))
    {
      showTasksStatus();
    }
    else if (SKIRMISH_STATS_COMMAND.equals(command))
    {
      showSkirmishStatistics();
    }
    else if (TITLES_STATUS_COMMAND.equals(command))
    {
      showTitlesStatus();
    }
    else if (STORAGE_COMMAND.equals(command))
    {
      showStorage();
    }
    else if (NEW_TOON_DATA_ID.equals(command))
    {
      startNewCharacterData();
    }
    else if (CLONE_TOON_DATA_ID.equals(command))
    {
      cloneCharacterData();
    }
    else if (EXPORT_TOON_DATA_ID.equals(command))
    {
      exportCharacterData();
    }
    else if (REMOVE_TOON_DATA_ID.equals(command))
    {
      removeCharacterData();
    }
    else if (GenericTableController.DOUBLE_CLICK.equals(command))
    {
      CharacterData data=(CharacterData)e.getSource();
      showCharacterData(data);
    }
  }

  private JPanel buildTablePanel()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _toonsTable=buildToonsTable();
    JTable table=_toonsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // New icon
    String newIconPath=getToolbarIconPath("new");
    ToolbarIconItem newIconItem=new ToolbarIconItem(NEW_TOON_DATA_ID,newIconPath,NEW_TOON_DATA_ID,"Create a new character configuration...","New");
    model.addToolbarIconItem(newIconItem);
    // Clone icon
    String cloneIconPath=getToolbarIconPath("copy");
    ToolbarIconItem cloneIconItem=new ToolbarIconItem(CLONE_TOON_DATA_ID,cloneIconPath,CLONE_TOON_DATA_ID,"Clone the selected character configuration...","Clone");
    model.addToolbarIconItem(cloneIconItem);
    // Remove icon
    String deleteIconPath=getToolbarIconPath("delete");
    ToolbarIconItem deleteIconItem=new ToolbarIconItem(REMOVE_TOON_DATA_ID,deleteIconPath,REMOVE_TOON_DATA_ID,"Remove the selected character...","Remove");
    model.addToolbarIconItem(deleteIconItem);
    // Export icon
    String exportIconPath=getToolbarIconPath("export");
    ToolbarIconItem exportIconItem=new ToolbarIconItem(EXPORT_TOON_DATA_ID,exportIconPath,EXPORT_TOON_DATA_ID,"Export the selected character configuration...","Export");
    model.addToolbarIconItem(exportIconItem);
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private CharacterDataTableController buildToonsTable()
  {
    CharacterDataTableController tableController=new CharacterDataTableController(_toon);
    tableController.addActionListener(this);
    return tableController;
  }

  private void startNewCharacterData()
  {
    CharacterData newInfos=CharacterFactory.buildNewData(_toon.getSummary());
    // Compute stats
    CharacterStatsComputer computer=new CharacterStatsComputer();
    newInfos.getStats().setStats(computer.getStats(newInfos));
    boolean ok=_toon.getInfosManager().writeNewCharacterData(newInfos);
    if (ok)
    {
      showCharacterData(newInfos);
    }
  }

  private void showCharacterData(CharacterData data)
  {
    String id=CharacterDataWindowController.getIdentifier(data);
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CharacterDataWindowController(this,_toon,data);
      _windowsManager.registerWindow(controller);
      Window thisWindow=SwingUtilities.getWindowAncestor(_toonsTable.getTable());
      controller.getWindow().setLocationRelativeTo(thisWindow);
    }
    controller.bringToFront();
  }

  private void cloneCharacterData()
  {
    GenericTableController<CharacterData> controller=_toonsTable.getTableController();
    CharacterData data=controller.getSelectedItem();
    if (data!=null)
    {
      // Build new configuration
      CharacterData newInfos=new CharacterData(data);
      newInfos.setDate(Long.valueOf(System.currentTimeMillis()));
      // Register new configuration
      CharacterInfosManager infos=_toon.getInfosManager();
      infos.writeNewCharacterData(newInfos);
    }
  }

  private void exportCharacterData()
  {
    GenericTableController<CharacterData> controller=_toonsTable.getTableController();
    CharacterData data=controller.getSelectedItem();
    if (data!=null)
    {
      FileChooserController ctrl=new FileChooserController("export", "Export character...");
      File toFile=ctrl.chooseFile(getWindow(),"Export");
      if (toFile!=null)
      {
        boolean doIt=true;
        if (!toFile.getName().toLowerCase().endsWith(".xml"))
        {
          toFile=new File(toFile.getParentFile(),toFile.getName()+".xml");
        }
        if (toFile.exists())
        {
          doIt=false;
          int result=GuiFactory.showQuestionDialog(getFrame(),"Do you really want to overwrite the selected file?","Overwrite?",JOptionPane.YES_NO_OPTION);
          if (result==JOptionPane.OK_OPTION)
          {
            doIt=true;
          }
        }
        if (doIt)
        {
          boolean ok=CharacterDataIO.saveInfo(toFile,data);
          Window window=getWindow();
          if (ok)
          {
            GuiFactory.showInformationDialog(window,"Export OK!","OK!");
          }
          else
          {
            GuiFactory.showErrorDialog(window,"Export failed!","Error!");
          }
        }
      }
    }
  }

  private void removeCharacterData()
  {
    GenericTableController<CharacterData> controller=_toonsTable.getTableController();
    CharacterData data=controller.getSelectedItem();
    if (data!=null)
    {
      // Check deletion
      String serverName=data.getServer();
      String toonName=data.getName();
      int result=GuiFactory.showQuestionDialog(getFrame(),"Do you really want to delete this configuration of " + toonName+"@"+ serverName + "?","Delete?",JOptionPane.YES_NO_OPTION);
      if (result==JOptionPane.OK_OPTION)
      {
        String id=CharacterDataWindowController.getIdentifier(data);
        WindowController windowController=_windowsManager.getWindow(id);
        if (windowController!=null)
        {
          windowController.dispose();
        }
        boolean ok=_toon.getInfosManager().remove(data);
        if (ok)
        {
          CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_REMOVED,_toon,data);
          EventsManager.invokeEvent(event);
        }
      }
    }
  }

  private void showStash()
  {
    // Stash
    String serverName=_toon.getServerName();
    String toonName=_toon.getName();
    String id=StashWindowController.getIdentifier(serverName,toonName);
    WindowController controller=_windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new StashWindowController(_toon);
      _windowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(getFrame());
    }
    controller.bringToFront();
  }

  private void editTraitPoints()
  {
    CharacterSummary summary=_toon.getSummary();
    TraitPointsStatus pointsStatus=TraitPoints.get().load(_toon);
    TraitPointsEditionWindowController controller=new TraitPointsEditionWindowController(this,summary,pointsStatus);
    TraitPointsStatus newStatus=controller.editModal();
    if (newStatus!=null)
    {
      TraitPoints.get().save(_toon,newStatus);
    }
  }

  private void editDeedsStatus()
  {
    AchievablesStatusManager status=DeedsStatusIo.load(_toon);
    DeedsStatusWindowController controller=new DeedsStatusWindowController(this,status,_toon);
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
    AchievablesStatusManager status=QuestsStatusIo.load(_toon);
    QuestsStatusWindowController controller=new QuestsStatusWindowController(this,status,_toon);
    controller.show();
  }

  private void showTasksStatus()
  {
    AchievablesStatusManager status=QuestsStatusIo.load(_toon);
    TasksStatusManager tasksStatus=new TasksStatusManager();
    tasksStatus.init(status);
    TasksStatusWindowController controller=new TasksStatusWindowController(this,tasksStatus,status,_toon);
    controller.show();
  }

  private void showSkirmishStatistics()
  {
    SkirmishStatsManager status=SkirmishStatsIo.load(_toon);
    SkirmishStatisticsWindowController controller=new SkirmishStatisticsWindowController(this,status);
    controller.show();
  }

  private void showTitlesStatus()
  {
    TitlesStatusManager status=TitlesStatusIo.load(_toon);
    TitlesStatusWindowController controller=new TitlesStatusWindowController(this,status);
    controller.show();
  }

  private void showStorage()
  {
    CharacterStorageDisplayWindowController summaryController=(CharacterStorageDisplayWindowController)_windowsManager.getWindow(CharacterStorageDisplayWindowController.IDENTIFIER);
    if (summaryController==null)
    {
      summaryController=new CharacterStorageDisplayWindowController(this,_toon);
      _windowsManager.registerWindow(summaryController);
      summaryController.getWindow().setLocationRelativeTo(getWindow());
    }
    summaryController.bringToFront();
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
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    if (_toonsTable!=null)
    {
      _toonsTable.dispose();
      _toonsTable=null;
    }
    if (_toolbar!=null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
    if (_toon!=null)
    {
      _toon.gc();
      _toon=null;
    }
  }
}
