package delta.games.lotro.gui.toon;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountReference;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFactory;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.initialGear.InitialGearDefinition;
import delta.games.lotro.character.classes.initialGear.InitialGearManager;
import delta.games.lotro.character.gear.CharacterGear;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.character.gear.GearSlotContents;
import delta.games.lotro.character.gear.GearSlotUtils;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for the "new toon" dialog.
 * @author DAM
 */
public class NewToonDialogController extends DefaultFormDialogController<Object>
{
  private static final Logger LOGGER=Logger.getLogger(NewToonDialogController.class);

  private static final int TOON_NAME_SIZE=32;
  private JTextField _toonName;
  private ComboBoxController<String> _server;
  private ComboBoxController<Account> _account;
  private CharacterClassController _class;
  private ComboBoxController<RaceDescription> _race;
  private ComboBoxController<CharacterSex> _sex;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public NewToonDialogController(WindowController parentController)
  {
    super(parentController,null);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("New character..."); // I18n
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildNewToonPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Character"); // I18n
    dataPanel.setBorder(pathsBorder);
    return dataPanel;
  }

  private JPanel buildNewToonPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Toon name
    _toonName=GuiFactory.buildTextField("");
    _toonName.setColumns(TOON_NAME_SIZE);
    // Server
    _server=CharacterUiUtils.buildServerCombo();
    TypedProperties props=Config.getInstance().getParameters();
    String defaultServer=props.getStringProperty("default.server",null);
    if (defaultServer!=null)
    {
      _server.selectItem(defaultServer);
    }
    // Account
    _account=CharacterUiUtils.buildAccountCombo();
    // Class
    _class=new CharacterClassController();
    // Race
    _race=CharacterUiUtils.buildRaceCombo(false);
    ItemSelectionListener<RaceDescription> listener=new ItemSelectionListener<RaceDescription>()
    {
      @Override
      public void itemSelected(RaceDescription race)
      {
        _class.setRace(race);
      }
    };
    _race.addListener(listener);
    // Sex
    _sex=CharacterUiUtils.buildSexCombo(false);

    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    panel.add(GuiFactory.buildLabel("Name:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Server:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Account:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Race:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Class:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Sex:"),gbc); // I18n
    gbc.gridx=1; gbc.gridy=0;
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    panel.add(_toonName,gbc);
    gbc.gridx=1; gbc.gridy++;
    panel.add(_server.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy++;
    panel.add(_account.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy++;
    panel.add(_race.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy++;
    panel.add(_class.getComboBoxController().getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy++;
    panel.add(_sex.getComboBox(),gbc);
    return panel;
  }

  @Override
  protected void okImpl()
  {
    String toonName=_toonName.getText();
    String server=_server.getSelectedItem();
    Account account=_account.getSelectedItem();
    ClassDescription characterClass=_class.getComboBoxController().getSelectedItem();
    RaceDescription race=_race.getSelectedItem();
    CharacterSex sex=_sex.getSelectedItem();
    CharacterSummary summary=new CharacterSummary();
    summary.setName(toonName);
    summary.setServer(server);
    AccountReference accountID=(account!=null)?account.getID():null;
    summary.setAccountID(accountID);
    summary.setCharacterSex(sex);
    summary.setCharacterClass(characterClass);
    summary.setRace(race);
    summary.setNationality(null);
    summary.setLevel(1);

    CharactersManager manager=CharactersManager.getInstance();
    CharacterFile toon=manager.addToon(summary);
    if (toon==null)
    {
      showErrorMessage("Character creation failed!"); // I18n
      return;
    }

    CharacterData data=CharacterFactory.buildNewData(toon.getSummary());
    // Setup gear
    setInitialGear(data);
    // Compute stats
    CharacterStatsComputer computer=new CharacterStatsComputer();
    data.getStats().setStats(computer.getStats(data));
    // Save
    toon.getInfosManager().writeNewCharacterData(data);
  }

  private void setInitialGear(CharacterData info)
  {
    RaceDescription race=info.getRace();
    ClassDescription description=info.getCharacterClass();
    if (description!=null)
    {
      CharacterGear gear=info.getEquipment();
      InitialGearDefinition initialGear=InitialGearManager.getInstance().getByKey(description.getKey());
      List<Item> items=initialGear.getItems(race);
      for(Item item : items)
      {
        EquipmentLocation itemLocation=item.getEquipmentLocation();
        for(GearSlot slot : GearSlot.getAll())
        {
          EquipmentLocation slotLocation=GearSlotUtils.getEquipmentSlot(slot);
          if (slotLocation==itemLocation)
          {
            GearSlotContents contents=gear.getSlotContents(slot,true);
            ItemInstance<? extends Item> old=contents.getItem();
            ItemInstance<? extends Item> itemInstance=ItemFactory.buildInstance(item);
            if (old==null)
            {
              contents.setItem(itemInstance);
            }
            else
            {
              LOGGER.warn("Would have overriden "+old+" by "+itemInstance);
            }
          }
        }
      }
      gear.setWearer(info.getSummary());
    }
  }

  @Override
  protected boolean checkInput()
  {
    String errorMsg=checkData();
    if (errorMsg==null)
    {
      return true;
    }
    showErrorMessage(errorMsg);
    return false;
  }

  private String checkData()
  {
    String errorMsg=null;
    String toonName=_toonName.getText();
    if ((toonName==null) || (toonName.trim().length()==0))
    {
      errorMsg="Invalid toon name!"; // I18n
    }
    String server=_server.getSelectedItem();
    if ((server==null) || (server.trim().length()==0))
    {
      errorMsg="Invalid server name!"; // I18n
    }
    return errorMsg;
  }

  private void showErrorMessage(String errorMsg)
  {
    String title="Character creation"; // I18n
    JDialog dialog=getDialog();
    GuiFactory.showErrorDialog(dialog,errorMsg,title);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_server!=null)
    {
      _server.dispose();
      _server=null;
    }
    if (_account!=null)
    {
      _account.dispose();
      _account=null;
    }
    if (_class!=null)
    {
      _class.dispose();
      _class=null;
    }
    if (_race!=null)
    {
      _race.dispose();
      _race=null;
    }
    if (_sex!=null)
    {
      _sex.dispose();
      _sex=null;
    }
    _toonName=null;
  }
}
