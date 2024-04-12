package delta.games.lotro.gui.character.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.utils.ContextPropertyNames;

/**
 * Controller for a "character" window.
 * @author DAM
 */
public class CharacterFileWindowController extends DefaultWindowController implements ActionListener
{
  // Data
  private CharacterFile _toon;
  // Controllers
  private CharacterSummaryPanelController _summaryController;
  private CharacterConfigurationsPanelController _configsController;
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
    _configsController=new CharacterConfigurationsPanelController(this,toon);
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
    // Character configs
    JPanel configsPanel=_configsController.getPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(configsPanel,c);
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
    _commandsManager.handleCommand(command);
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
    if (_configsController!=null)
    {
      _configsController.dispose();
      _configsController=null;
    }
    if (_toon!=null)
    {
      _toon.getPreferences().saveAllPreferences();
      _toon.gc();
      _toon=null;
    }
  }
}
