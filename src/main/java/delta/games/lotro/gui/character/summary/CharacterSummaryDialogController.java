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
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.Race;

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
  private ComboBoxController<CharacterClass> _class;
  private ComboBoxController<Race> _race;
  private ComboBoxController<CharacterSex> _sex;
  private CharacterRegionController _region;
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
    dialog.setTitle("Edit character summary...");
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Character summary");
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
    // Class
    _class=CharacterUiUtils.buildClassCombo();
    _class.getComboBox().setEnabled(false);
    // Race
    _race=CharacterUiUtils.buildRaceCombo();
    _race.getComboBox().setEnabled(false);
    // Sex
    _sex=CharacterUiUtils.buildSexCombo();
    _sex.getComboBox().setEnabled(true);
    // Region
    _region=new CharacterRegionController();
    // Level
    _level=CharacterUiUtils.buildLevelCombo();

    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    panel.add(GuiFactory.buildLabel("Name:"),gbc);
    gbc.gridx=0; gbc.gridy=1;
    panel.add(GuiFactory.buildLabel("Server:"),gbc);
    gbc.gridx=0; gbc.gridy=2;
    panel.add(GuiFactory.buildLabel("Race:"),gbc);
    gbc.gridx=0; gbc.gridy=3;
    panel.add(GuiFactory.buildLabel("Class:"),gbc);
    gbc.gridx=0; gbc.gridy=4;
    panel.add(GuiFactory.buildLabel("Sex:"),gbc);
    gbc.gridx=0; gbc.gridy=5;
    panel.add(GuiFactory.buildLabel("Region:"),gbc);
    gbc.gridx=0; gbc.gridy=6;
    panel.add(GuiFactory.buildLabel("Level:"),gbc);
    gbc.gridx=1; gbc.gridy=0;
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    panel.add(_toonName,gbc);
    gbc.gridx=1; gbc.gridy=1;
    panel.add(_server.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy=2;
    panel.add(_race.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy=3;
    panel.add(_class.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy=4;
    panel.add(_sex.getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy=5;
    panel.add(_region.getComboBoxController().getComboBox(),gbc);
    gbc.gridx=1; gbc.gridy=6;
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
    // Class
    CharacterClass characterClass=_data.getCharacterClass();
    _class.selectItem(characterClass);
    // Race
    Race race=_data.getRace();
    _race.selectItem(race);
    _region.setRace(race);
    // Sex
    CharacterSex sex=_data.getCharacterSex();
    _sex.selectItem(sex);
    // Region
    String region=_data.getRegion();
    _region.getComboBoxController().selectItem(region);
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
    CharacterClass cClass=_class.getSelectedItem();
    _data.setCharacterClass(cClass);
    Race race=_race.getSelectedItem();
    _data.setRace(race);
    CharacterSex sex=_sex.getSelectedItem();
    _data.setCharacterSex(sex);
    String region=_region.getComboBoxController().getSelectedItem();
    _data.setRegion(region);
    int level=_level.getSelectedItem().intValue();
    _data.setLevel(level);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    _toonName=null;
    if (_server!=null)
    {
      _server.dispose();
      _server=null;
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
    if (_region!=null)
    {
      _region.dispose();
      _region=null;
    }
    if (_level!=null)
    {
      _level.dispose();
      _level=null;
    }
  }
}
