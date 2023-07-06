package delta.games.lotro.gui.character;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterDataSummary;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.gui.utils.l10n.DateFormat;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for character main attributes edition panel.
 * @author DAM
 */
public class CharacterMainAttrsEditionPanelController
{
  // UI
  private JPanel _panel;
  private JLabel _classIcon;
  private JLabel _raceIcon;
  private JTextField _name;
  private ComboBoxController<Integer> _level;
  private DateEditionController _date;
  private JTextField _shortDescription;

  // Data
  private CharacterFile _toonFile;
  private CharacterData _toon;

  /**
   * Constructor.
   * @param toon Parent toon.
   * @param toonData Managed toon.
   */
  public CharacterMainAttrsEditionPanelController(CharacterFile toon, CharacterData toonData)
  {
    _toonFile=toon;
    _toon=toonData;
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // 1st line
    JPanel firstLinePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(firstLinePanel,c);
    // Class icon
    _classIcon=GuiFactory.buildIconLabel(null);
    firstLinePanel.add(_classIcon);
    // Race icon
    _raceIcon=GuiFactory.buildIconLabel(null);
    firstLinePanel.add(_raceIcon);
    // Name
    _name=GuiFactory.buildTextField("");
    _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
    _name.setColumns(25);
    firstLinePanel.add(_name);
    // Level
    _level=CharacterUiUtils.buildLevelCombo();
    firstLinePanel.add(_level.getComboBox());
    ItemSelectionListener<Integer> levelListener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer level)
      {
        _toon.getSummary().setLevel(level.intValue());
        // Broadcast level update event...
        CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_toon);
        EventsManager.invokeEvent(event);
      }
    };
    _level.addListener(levelListener);
    // Date
    _date=new DateEditionController(DateFormat.getDateTimeCodec());
    firstLinePanel.add(_date.getTextField());

    // 2nd line
    JPanel secondLinePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // Short description
    secondLinePanel.add(GuiFactory.buildLabel("Description:")); // I18n
    _shortDescription=GuiFactory.buildTextField("");
    _shortDescription.setColumns(50);
    secondLinePanel.add(_shortDescription);
    c=new GridBagConstraints(0,1,1,1,0.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(secondLinePanel,c);

    return panel;
  }

  /**
   * Write data to UI gadgets.
   */
  public void set()
  {
    getPanel();
    // Class icon
    ClassDescription characterClass=_toon.getCharacterClass();
    ImageIcon classIcon=LotroIconsManager.getClassIcon(characterClass.getIconId());
    _classIcon.setIcon(classIcon);
    // Character icon
    updateSexDisplay();
    // Name
    _name.setText(_toon.getName());
    // Level
    _level.selectItem(Integer.valueOf(_toon.getLevel()));
    // Date
    Long timestamp=_toon.getDate();
    _date.setDate(timestamp);
    // Short description
    _shortDescription.setText(_toon.getShortDescription());
  }

  /**
   * Update sex display.
   */
  public void updateSexDisplay()
  {
    RaceDescription race=_toon.getRace();
    CharacterSex sex=getCharacterSex();
    ImageIcon characterIcon=LotroIconsManager.getCharacterIcon(race,sex);
    _raceIcon.setIcon(characterIcon);
  }

  private CharacterSex getCharacterSex()
  {
    CharacterSex ret=null;
    if (_toonFile!=null)
    {
      CharacterSummary summary=_toonFile.getSummary();
      if (summary!=null)
      {
        ret=summary.getCharacterSex();
      }
    }
    return ret;
  }

  /**
   * Get data from UI gadgets.
   */
  public void get()
  {
    CharacterDataSummary summary=_toon.getSummary();
    // Name
    summary.setName(_name.getText());
    // Level
    Integer level=_level.getSelectedItem();
    if (level!=null)
    {
      summary.setLevel(level.intValue());
    }
    // Date
    Long date=_date.getDate();
    _toon.setDate(date);
    // Short description
    _toon.setShortDescription(_shortDescription.getText());
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_date!=null)
    {
      _date.dispose();
      _date=null;
    }
    _toon=null;
    _classIcon=null;
    _name=null;
    _level=null;
    _shortDescription=null;
  }
}
