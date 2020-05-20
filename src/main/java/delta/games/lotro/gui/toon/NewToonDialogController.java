package delta.games.lotro.gui.toon;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.CharacterFactory;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.classes.InitialGearDefinition;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.Race;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Controller for the "new toon" dialog.
 * @author DAM
 */
public class NewToonDialogController extends DefaultFormDialogController<Object>
{
  private static final int TOON_NAME_SIZE=32;
  private JTextField _toonName;
  private ComboBoxController<String> _server;
  private ComboBoxController<String> _account;
  private CharacterClassController _class;
  private ComboBoxController<Race> _race;
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
    dialog.setTitle("New character...");
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel dataPanel=buildNewToonPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Character");
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
    ItemSelectionListener<Race> listener=new ItemSelectionListener<Race>()
    {
      @Override
      public void itemSelected(Race race)
      {
        _class.setRace(race);
      }
    };
    _race.addListener(listener);
    // Sex
    _sex=CharacterUiUtils.buildSexCombo();

    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    panel.add(GuiFactory.buildLabel("Name:"),gbc);
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Server:"),gbc);
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Account:"),gbc);
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Race:"),gbc);
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Class:"),gbc);
    gbc.gridx=0; gbc.gridy++;
    panel.add(GuiFactory.buildLabel("Sex:"),gbc);
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
    String account=_account.getSelectedItem();
    CharacterClass cClass=_class.getComboBoxController().getSelectedItem();
    Race race=_race.getSelectedItem();
    CharacterSex sex=_sex.getSelectedItem();
    CharacterSummary summary=new CharacterSummary();
    summary.setName(toonName);
    summary.setServer(server);
    summary.setAccountName(account);
    summary.setCharacterSex(sex);
    summary.setCharacterClass(cClass);
    summary.setRace(race);
    summary.setRegion("");
    summary.setLevel(1);

    CharactersManager manager=CharactersManager.getInstance();
    CharacterFile toon=manager.addToon(summary);
    if (toon==null)
    {
      showErrorMessage("Character creation failed!");
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
    Race race=info.getRace();
    CharacterClass characterClass=info.getCharacterClass();
    ClassDescription description=ClassesManager.getInstance().getClassDescription(characterClass);
    if (description!=null)
    {
      CharacterEquipment gear=info.getEquipment();
      ItemsManager itemsMgr=ItemsManager.getInstance();
      InitialGearDefinition initialGear=description.getInitialGear();
      List<Integer> itemIds=initialGear.getItems(race);
      for(Integer itemId : itemIds)
      {
        Item item=itemsMgr.getItem(itemId.intValue());
        if (item!=null)
        {
          EquipmentLocation location=item.getEquipmentLocation();
          for(EQUIMENT_SLOT slot : EQUIMENT_SLOT.values())
          {
            if (slot.getLocation()==location)
            {
              SlotContents contents=gear.getSlotContents(slot,true);
              ItemInstance<? extends Item> old=contents.getItem();
              ItemInstance<? extends Item> itemInstance=ItemFactory.buildInstance(item);
              if (old==null)
              {
                contents.setItem(itemInstance);
              }
              else
              {
                System.out.println("Would have overriden "+old+" by "+itemInstance);
              }
            }
          }
        }
      }
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
      errorMsg="Invalid toon name!";
    }
    String server=_server.getSelectedItem();
    if ((server==null) || (server.trim().length()==0))
    {
      errorMsg="Invalid server name!";
    }
    return errorMsg;
  }

  private void showErrorMessage(String errorMsg)
  {
    String title="Character creation";
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
