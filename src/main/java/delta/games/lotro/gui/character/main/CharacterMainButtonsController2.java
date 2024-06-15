package delta.games.lotro.gui.character.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;

/**
 * Controller for a panel with command buttons for the main character window.
 * @author DAM
 */
public class CharacterMainButtonsController2 implements ActionListener
{
  // Data
  private CharacterFile _toon;
  // Commands manager
  private MainCharacterWindowCommandsManager _commandsManager;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Managed toon.
   */
  public CharacterMainButtonsController2(WindowController parent, CharacterFile toon)
  {
    _toon=toon;
    _commandsManager=new MainCharacterWindowCommandsManager(parent,toon);
  }

  public JPanel buildSummaryCommandsPanel()
  {
    List<JButton> buttons=buildSummaryButtons();
    return buildVerticalPanel(buttons,9);
  }

  public JPanel buildGearCommandsPanel()
  {
    List<JButton> buttons=buildGearButtons();
    return buildVerticalPanel(buttons,7);
  }

  private JPanel buildVerticalPanel(List<JButton> buttons, int nbPerColumn)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,2,5,2),0,0);
    for(JButton button : buttons)
    {
      panel.add(button,c);
      c.gridy++;
      if (c.gridy==nbPerColumn)
      {
        c.gridy=0;
        c.gridx++;
        c.insets.top=5;
      }
      else if (c.gridy>0)
      {
        c.insets.top=0;
      }
    }
    return panel;
  }

  private List<JButton> buildSummaryButtons()
  {
    List<JButton> ret=new ArrayList<JButton>();
    // I18n
    // Levels
    ret.add(buildCommandButton("Levels",MainCharacterWindowCommands.LEVEL_COMMAND));
    // Quests status
    ret.add(buildCommandButton("Quests",MainCharacterWindowCommands.QUESTS_STATUS_COMMAND));
    // Deeds status
    ret.add(buildCommandButton("Deeds",MainCharacterWindowCommands.DEEDS_STATUS_COMMAND));
    // Tasks status
    ret.add(buildCommandButton("Tasks",MainCharacterWindowCommands.TASKS_STATUS_COMMAND));
    // Titles status
    ret.add(buildCommandButton("Titles",MainCharacterWindowCommands.TITLES_STATUS_COMMAND));
    // Reputation
    ret.add(buildCommandButton("Reputation",MainCharacterWindowCommands.REPUTATION_COMMAND));
    // Allegiances status
    ret.add(buildCommandButton("Allegiances",MainCharacterWindowCommands.ALLEGIANCES_COMMAND));
    // Crafting
    ret.add(buildCommandButton("Crafting",MainCharacterWindowCommands.CRAFTING_COMMAND));
    // Recipes status
    ret.add(buildCommandButton("Recipes",MainCharacterWindowCommands.RECIPES_STATUS_COMMAND));
    // Storage
    ret.add(buildCommandButton("Storage",MainCharacterWindowCommands.STORAGE_COMMAND));
    // Currencies
    ret.add(buildCommandButton("Currencies",MainCharacterWindowCommands.CURRENCIES_COMMAND));
    // Emotes status
    ret.add(buildCommandButton("Emotes",MainCharacterWindowCommands.EMOTES_COMMAND));
    // Mounts status
    ret.add(buildCommandButton("Mounts",MainCharacterWindowCommands.MOUNTS_COMMAND));
    // Mounted appearances
    ret.add(buildCommandButton("Mounted Cosmetics",MainCharacterWindowCommands.MOUNTED_APPEARANCES_COMMAND));
    // Travels status
    ret.add(buildCommandButton("Travels",MainCharacterWindowCommands.TRAVELS_COMMAND));
    // Pets status
    ret.add(buildCommandButton("Pets",MainCharacterWindowCommands.PETS_COMMAND));
    // Notes
    ret.add(buildCommandButton("Notes",MainCharacterWindowCommands.NOTES_COMMAND));
    // Log
    JButton logButton=buildCommandButton("Log",MainCharacterWindowCommands.LOG_COMMAND);
    // Disable buttons if no log
    boolean hasLog=_toon.hasLog();
    logButton.setEnabled(hasLog);
    ret.add(logButton);

    return ret;
  }

  private List<JButton> buildGearButtons()
  {
    List<JButton> ret=new ArrayList<JButton>();
    // I18n
    // Stash
    ret.add(buildCommandButton("Stash",MainCharacterWindowCommands.STASH_COMMAND));
    // Relics inventory statistics
    ret.add(buildCommandButton("Relics",MainCharacterWindowCommands.RELICS_INVENTORY_COMMAND));
    // Skirmish statistics
    ret.add(buildCommandButton("Skirmish Stats",MainCharacterWindowCommands.SKIRMISH_STATS_COMMAND));
    // Skirmish traits status
    ret.add(buildCommandButton("Skirmish Traits",MainCharacterWindowCommands.SKIRMISH_TRAITS_COMMAND));
    // Outfits
    ret.add(buildCommandButton("Outfits",MainCharacterWindowCommands.OUTFITS_COMMAND));
    // PVP
    ret.add(buildCommandButton("PVP",MainCharacterWindowCommands.PVP_COMMAND));
    // Configurations
    ret.add(buildCommandButton("Configs",MainCharacterWindowCommands.CONFIGS_COMMAND));

    return ret;
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
  public void dispose()
  {
    if (_commandsManager!=null)
    {
      _commandsManager.dispose();
      _commandsManager=null;
    }
  }
}
