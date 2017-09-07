package delta.games.lotro.gui.character.summary;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.OKCancelPanelController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.Race;

/**
 * Controller for the "character summary toon" dialog.
 * @author DAM
 */
public class CharacterSummaryDialogController extends DefaultDialogController implements ActionListener
{
  private static final int TOON_NAME_SIZE=32;
  // Data
  private CharacterSummary _summary;
  // UI
  private OKCancelPanelController _okCancelController;
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
    super(parentController);
    _summary=summary;
  }

  /**
   * Get the managed summary.
   * @return a summary or <code>null</code> if canceled.
   */
  public CharacterSummary getSummary()
  {
    return _summary;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Edit character summary...");
    dialog.setResizable(false);
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    return dialog;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel dataPanel=buildPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Character summary");
    dataPanel.setBorder(pathsBorder);
    panel.add(dataPanel,BorderLayout.CENTER);
    _okCancelController=new OKCancelPanelController();
    JPanel okCancelPanel=_okCancelController.getPanel();
    panel.add(okCancelPanel,BorderLayout.SOUTH);
    _okCancelController.getOKButton().addActionListener(this);
    _okCancelController.getCancelButton().addActionListener(this);
    initData();
    return panel;
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

  public void actionPerformed(ActionEvent event)
  {
    String action=event.getActionCommand();
    if (OKCancelPanelController.OK_COMMAND.equals(action))
    {
      ok();
    }
    else if (OKCancelPanelController.CANCEL_COMMAND.equals(action))
    {
      cancel();
    }
  }

  private void initData()
  {
    // Toon name
    String name=_summary.getName();
    _toonName.setText(name);
    // Server
    String server=_summary.getServer();
    _server.selectItem(server);
    // Class
    CharacterClass characterClass=_summary.getCharacterClass();
    _class.selectItem(characterClass);
    // Race
    Race race=_summary.getRace();
    _race.selectItem(race);
    _region.setRace(race);
    // Sex
    CharacterSex sex=_summary.getCharacterSex();
    _sex.selectItem(sex);
    // Region
    String region=_summary.getRegion();
    _region.getComboBoxController().selectItem(region);
    // Level
    int level=_summary.getLevel();
    _level.selectItem(Integer.valueOf(level));
  }

  private void ok()
  {
    String toonName=_toonName.getText();
    _summary.setName(toonName);
    String server=_server.getSelectedItem();
    _summary.setServer(server);
    CharacterClass cClass=_class.getSelectedItem();
    _summary.setCharacterClass(cClass);
    Race race=_race.getSelectedItem();
    _summary.setRace(race);
    CharacterSex sex=_sex.getSelectedItem();
    _summary.setCharacterSex(sex);
    String region=_region.getComboBoxController().getSelectedItem();
    _summary.setRegion(region);
    int level=_level.getSelectedItem().intValue();
    _summary.setLevel(level);
    dispose();
  }

  private void cancel()
  {
    _summary=null;
    dispose();
  }

  @Override
  protected void doWindowClosing()
  {
    cancel();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    if (_okCancelController!=null)
    {
      _okCancelController.dispose();
      _okCancelController=null;
    }
    _toonName=null;
    // DO NOT set _summary to null since it is used afterwards
    //_summary=null;
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
