package delta.games.lotro.gui.character.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;

/**
 * Manager for command buttons for the main character window.
 * @author DAM
 */
public class CharacterMainButtonsManager implements ActionListener
{
  // Data
  private CharacterFile _toon;
  // Commands manager
  private MainCharacterWindowCommandsManager _commandsManager;
  // Buttons registry
  private Map<String,JButton> _buttons;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Managed toon.
   */
  public CharacterMainButtonsManager(WindowController parent, CharacterFile toon)
  {
    _toon=toon;
    _commandsManager=new MainCharacterWindowCommandsManager(parent,toon);
    _buttons=new HashMap<String,JButton>();
    buildButtons();
  }

  /**
   * Get a button using its command identifier.
   * @param command Command identifier.
   * @return A button or <code>null</code> if not found.
   */
  public JButton getButton(String command)
  {
    return _buttons.get(command);
  }

  private void buildButtons()
  {
    // I18n
    // Levels
    buildCommandButton("Levels",MainCharacterWindowCommands.LEVEL_COMMAND);
    // Quests status
    buildCommandButton("Quests",MainCharacterWindowCommands.QUESTS_STATUS_COMMAND);
    // Deeds status
    buildCommandButton("Deeds",MainCharacterWindowCommands.DEEDS_STATUS_COMMAND);
    // Tasks status
    buildCommandButton("Tasks",MainCharacterWindowCommands.TASKS_STATUS_COMMAND);
    // Titles status
    buildCommandButton("Titles",MainCharacterWindowCommands.TITLES_STATUS_COMMAND);
    // Reputation
    buildCommandButton("Reputation",MainCharacterWindowCommands.REPUTATION_COMMAND);
    // Allegiances status
    buildCommandButton("Allegiances",MainCharacterWindowCommands.ALLEGIANCES_COMMAND);
    // Crafting
    buildCommandButton("Status",MainCharacterWindowCommands.CRAFTING_COMMAND);
    // Recipes status
    buildCommandButton("Recipes",MainCharacterWindowCommands.RECIPES_STATUS_COMMAND);
    // Storage
    buildCommandButton("Storage",MainCharacterWindowCommands.STORAGE_COMMAND);
    // Currencies
    buildCommandButton("Currencies",MainCharacterWindowCommands.CURRENCIES_COMMAND);
    // Emotes status
    buildCommandButton("Emotes",MainCharacterWindowCommands.EMOTES_COMMAND);
    // Mounts status
    buildCommandButton("Mounts",MainCharacterWindowCommands.MOUNTS_COMMAND);
    // Mounted appearances
    buildCommandButton("Mounted Cosmetics",MainCharacterWindowCommands.MOUNTED_APPEARANCES_COMMAND);
    // Travels status
    buildCommandButton("Travels",MainCharacterWindowCommands.TRAVELS_COMMAND);
    // Pets status
    buildCommandButton("Pets",MainCharacterWindowCommands.PETS_COMMAND);
    // Notes
    buildCommandButton("Notes",MainCharacterWindowCommands.NOTES_COMMAND);
    // Log
    JButton logButton=buildCommandButton("Log",MainCharacterWindowCommands.LOG_COMMAND);
    // Disable buttons if no log
    boolean hasLog=_toon.hasLog();
    logButton.setEnabled(hasLog);
    // Stash
    buildCommandButton("Stash",MainCharacterWindowCommands.STASH_COMMAND);
    // Relics inventory statistics
    buildCommandButton("Relics",MainCharacterWindowCommands.RELICS_INVENTORY_COMMAND);
    // Skirmish statistics
    buildCommandButton("Skirmish Stats",MainCharacterWindowCommands.SKIRMISH_STATS_COMMAND);
    // Skirmish traits status
    buildCommandButton("Skirmish Traits",MainCharacterWindowCommands.SKIRMISH_TRAITS_COMMAND);
    // Outfits
    buildCommandButton("Outfits",MainCharacterWindowCommands.OUTFITS_COMMAND);
    // PVP
    buildCommandButton("PVP",MainCharacterWindowCommands.PVP_COMMAND);
    // Configurations
    buildCommandButton("Configs",MainCharacterWindowCommands.CONFIGS_COMMAND);
  }

  private JButton buildCommandButton(String label, String command)
  {
    JButton b=GuiFactory.buildButton(label);
    b.setActionCommand(command);
    b.addActionListener(this);
    _buttons.put(command,b);
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
  public void dispose()
  {
    // Data
    _toon=null;
    // Commands manager
    if (_commandsManager!=null)
    {
      _commandsManager.dispose();
      _commandsManager=null;
    }
    // Buttons
    _buttons=null;
  }
}
