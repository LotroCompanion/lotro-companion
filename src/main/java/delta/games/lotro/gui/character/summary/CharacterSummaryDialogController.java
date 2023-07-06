package delta.games.lotro.gui.character.summary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountReference;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.races.NationalityDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.CharacterSex;

/**
 * Controller for the "character summary toon" dialog.
 * @author DAM
 */
public class CharacterSummaryDialogController extends DefaultFormDialogController<CharacterSummary>
{
  private static final int TOON_NAME_SIZE=32;
  // UI
  private JTextField _toonName;
  private ComboBoxController<String> _server;
  private ComboBoxController<Account> _account;
  private ComboBoxController<ClassDescription> _class;
  private ComboBoxController<RaceDescription> _race;
  private ComboBoxController<CharacterSex> _sex;
  private CharacterNationalityController _nationality;
  private ComboBoxController<Integer> _level;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param summary Data to edit.
   */
  public CharacterSummaryDialogController(WindowController parentController, CharacterSummary summary)
  {
    super(parentController,summary);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Edit character summary..."); // I18n
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Character summary"); // I18n
    dataPanel.setBorder(pathsBorder);
    initData();
    return dataPanel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Toon name
    _toonName=GuiFactory.buildTextField("");
    _toonName.setColumns(TOON_NAME_SIZE);
    // Server
    _server=CharacterUiUtils.buildServerCombo();
    // Account
    _account=CharacterUiUtils.buildAccountCombo();
    // Class
    _class=CharacterUiUtils.buildCharacterClassCombo(false);
    _class.getComboBox().setEnabled(false);
    // Race
    _race=CharacterUiUtils.buildRaceCombo(false);
    _race.getComboBox().setEnabled(false);
    // Sex
    _sex=CharacterUiUtils.buildSexCombo(false);
    _sex.getComboBox().setEnabled(true);
    // Nationality
    _nationality=new CharacterNationalityController();
    // Level
    _level=CharacterUiUtils.buildLevelCombo();

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
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Region:"),gbc); // I18n
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Level:"),gbc); // I18n
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
    panel.add(_class.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy++;
    panel.add(_sex.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy++;
    panel.add(_nationality.getComboBoxController().getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy++;
    panel.add(_level.getComboBox(),gbc);
    return panel;
  }

  private void initData()
  {
    // Toon name
    String name=_data.getName();
    _toonName.setText(name);
    // Server
    String server=_data.getServer();
    _server.selectItem(server);
    // Account
    AccountReference accountID=_data.getAccountID();
    Account account=AccountsManager.getInstance().getAccountByID(accountID);
    _account.selectItem(account);
    // Class
    ClassDescription characterClass=_data.getCharacterClass();
    _class.selectItem(characterClass);
    // Race
    RaceDescription race=_data.getRace();
    _race.selectItem(race);
    _nationality.setRace(race);
    // Sex
    CharacterSex sex=_data.getCharacterSex();
    _sex.selectItem(sex);
    // Nationality
    NationalityDescription nationality=_data.getNationality();
    _nationality.getComboBoxController().selectItem(nationality);
    // Level
    int level=_data.getLevel();
    _level.selectItem(Integer.valueOf(level));
  }

  @Override
  protected void okImpl()
  {
    String toonName=_toonName.getText();
    _data.setName(toonName);
    String server=_server.getSelectedItem();
    _data.setServer(server);
    Account account=_account.getSelectedItem();
    AccountReference accountID=(account!=null)?account.getID():null;
    _data.setAccountID(accountID);
    ClassDescription cClass=_class.getSelectedItem();
    _data.setCharacterClass(cClass);
    RaceDescription race=_race.getSelectedItem();
    _data.setRace(race);
    CharacterSex sex=_sex.getSelectedItem();
    _data.setCharacterSex(sex);
    NationalityDescription nationality=_nationality.getComboBoxController().getSelectedItem();
    _data.setNationality(nationality);
    int level=_level.getSelectedItem().intValue();
    _data.setLevel(level);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    _toonName=null;
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
    if (_nationality!=null)
    {
      _nationality.dispose();
      _nationality=null;
    }
    if (_level!=null)
    {
      _level.dispose();
      _level=null;
    }
  }
}
