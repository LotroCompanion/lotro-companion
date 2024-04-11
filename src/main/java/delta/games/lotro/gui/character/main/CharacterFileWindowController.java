package delta.games.lotro.gui.character.main;

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
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.io.xml.CharacterDataIO;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.gui.character.config.CharacterDataWindowController;
import delta.games.lotro.utils.ContextPropertyNames;
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

  private CharacterSummaryPanelController _summaryController;
  private CharacterDataTableController _toonsTable;
  private ToolbarController _toolbar;
  private CharacterFile _toon;
  private MainCharacterWindowCommandsManager _commandsManager;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterFileWindowController(CharacterFile toon)
  {
    _toon=toon;
    _summaryController=new CharacterSummaryPanelController(this,_toon);
    setContextProperty(ContextPropertyNames.BASE_CHARACTER_SUMMARY,toon.getSummary());
    _commandsManager=new MainCharacterWindowCommandsManager(this,toon);
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
    tablePanel.setBorder(GuiFactory.buildTitledBorder("Configurations")); // I18n
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

  private JPanel buildCommandsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
    // Deeds status
    JButton deedsButton=buildCommandButton("Deeds",MainCharacterWindowCommands.DEEDS_STATUS_COMMAND); // I18n
    panel.add(deedsButton,c);c.gridx++;
    // Titles status
    JButton titlesButton=buildCommandButton("Titles",MainCharacterWindowCommands.TITLES_STATUS_COMMAND); // I18n
    panel.add(titlesButton,c);c.gridx++;
    // Reputation
    JButton reputationButton=buildCommandButton("Reputation",MainCharacterWindowCommands.REPUTATION_COMMAND); // I18n
    panel.add(reputationButton,c);c.gridx++;
    // Crafting
    JButton craftingButton=buildCommandButton("Crafting",MainCharacterWindowCommands.CRAFTING_COMMAND); // I18n
    panel.add(craftingButton,c);c.gridx++;
    // Storage
    JButton storageButton=buildCommandButton("Storage",MainCharacterWindowCommands.STORAGE_COMMAND); // I18n
    panel.add(storageButton,c);c.gridx++;
    // Tasks status
    JButton tasksButton=buildCommandButton("Tasks",MainCharacterWindowCommands.TASKS_STATUS_COMMAND); // I18n
    panel.add(tasksButton,c);c.gridx++;
    // Relics inventory statistics
    JButton relicsButton=buildCommandButton("Relics",MainCharacterWindowCommands.RELICS_INVENTORY_COMMAND); // I18n
    panel.add(relicsButton,c);c.gridx++;
    // Log
    JButton logButton=buildCommandButton("Log",MainCharacterWindowCommands.LOG_COMMAND); // I18n
    c.insets.right=5;
    panel.add(logButton,c);c.gridx++;
    // Currencies
    JButton currenciesButton=buildCommandButton("Currencies",MainCharacterWindowCommands.CURRENCIES_COMMAND); // I18n
    panel.add(currenciesButton,c);c.gridx++;
    // Emotes status
    JButton emotesButton=buildCommandButton("Emotes",MainCharacterWindowCommands.EMOTES_COMMAND); // I18n
    panel.add(emotesButton,c);c.gridx++;
    // Mounts status
    JButton mountsButton=buildCommandButton("Mounts",MainCharacterWindowCommands.MOUNTS_COMMAND); // I18n
    panel.add(mountsButton,c);c.gridx++;
    // Mounted appearances
    JButton mountedAppearancesButton=buildCommandButton("Mounted Cosmetics",MainCharacterWindowCommands.MOUNTED_APPEARANCES_COMMAND); // I18n
    panel.add(mountedAppearancesButton,c);c.gridx++;
    // Notes
    JButton notesButton=buildCommandButton("Notes",MainCharacterWindowCommands.NOTES_COMMAND); // I18n
    panel.add(notesButton,c);c.gridx++;

    c.insets.right=0;
    c.gridx=0;c.gridy++;
    // Quests status
    JButton questsButton=buildCommandButton("Quests",MainCharacterWindowCommands.QUESTS_STATUS_COMMAND); // I18n
    panel.add(questsButton,c);c.gridx++;
    // Trait points
    JButton traitPointsButton=buildCommandButton("Trait points",MainCharacterWindowCommands.TRAIT_POINTS_COMMAND); // I18n
    panel.add(traitPointsButton,c);c.gridx++;
    // Allegiances status
    JButton allegiancesButton=buildCommandButton("Allegiances",MainCharacterWindowCommands.ALLEGIANCES_COMMAND); // I18n
    panel.add(allegiancesButton,c);c.gridx++;
    // Recipes status
    JButton recipesButton=buildCommandButton("Recipes",MainCharacterWindowCommands.RECIPES_STATUS_COMMAND); // I18n
    panel.add(recipesButton,c);c.gridx++;
    // Stash
    JButton stashButton=buildCommandButton("Stash",MainCharacterWindowCommands.STASH_COMMAND); // I18n
    panel.add(stashButton,c);c.gridx++;
    // Skirmish statistics
    JButton skirmishsButton=buildCommandButton("Skirmishs",MainCharacterWindowCommands.SKIRMISH_STATS_COMMAND); // I18n
    panel.add(skirmishsButton,c);c.gridx++;
    // Levels
    JButton levelsButton=buildCommandButton("Levels",MainCharacterWindowCommands.LEVEL_COMMAND); // I18n
    c.insets.right=5;
    panel.add(levelsButton,c);c.gridx++;
    // Travels status
    JButton travelsButton=buildCommandButton("Travels",MainCharacterWindowCommands.TRAVELS_COMMAND); // I18n
    panel.add(travelsButton,c);c.gridx++;
    // Outfits
    JButton outfitsButton=buildCommandButton("Outfits",MainCharacterWindowCommands.OUTFITS_COMMAND); // I18n
    panel.add(outfitsButton,c);c.gridx++;
    // Pets status
    JButton petsButton=buildCommandButton("Pets",MainCharacterWindowCommands.PETS_COMMAND); // I18n
    panel.add(petsButton,c);c.gridx++;
    // Hobbies status
    JButton hobbiesButton=buildCommandButton("Hobbies",MainCharacterWindowCommands.HOBBIES_COMMAND); // I18n
    panel.add(hobbiesButton,c);c.gridx++;
    // Skirmish traits status
    JButton skirmishTraitsButton=buildCommandButton("Skirmish Traits",MainCharacterWindowCommands.SKIRMISH_TRAITS_COMMAND); // I18n
    panel.add(skirmishTraitsButton,c);c.gridx++;

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
    if (NEW_TOON_DATA_ID.equals(command))
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
    else
    {
      _commandsManager.handleCommand(command);
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
    WindowsManager windowsManager=getWindowsManager();
    WindowController controller=windowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new CharacterDataWindowController(this,_toon,data);
      windowsManager.registerWindow(controller);
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
      FileChooserController ctrl=new FileChooserController("export", "Export character..."); // I18n
      File toFile=ctrl.chooseFile(getWindow(),"Export"); // I18n
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
          int result=GuiFactory.showQuestionDialog(getFrame(),"Do you really want to overwrite the selected file?","Overwrite?",JOptionPane.YES_NO_OPTION); // I18n
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
            GuiFactory.showInformationDialog(window,"Export OK!","OK!"); // I18n
          }
          else
          {
            GuiFactory.showErrorDialog(window,"Export failed!","Error!"); // I18n
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
      int result=GuiFactory.showQuestionDialog(getFrame(),"Do you really want to delete this configuration of " + toonName+"@"+ serverName + "?","Delete?",JOptionPane.YES_NO_OPTION); // I18n
      if (result==JOptionPane.OK_OPTION)
      {
        String id=CharacterDataWindowController.getIdentifier(data);
        WindowsManager windowsManager=getWindowsManager();
        WindowController windowController=windowsManager.getWindow(id);
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
      _toon.getPreferences().saveAllPreferences();
      _toon.gc();
      _toon=null;
    }
  }
}
