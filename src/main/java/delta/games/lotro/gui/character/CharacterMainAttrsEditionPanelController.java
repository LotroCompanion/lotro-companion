package delta.games.lotro.gui.character;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.utils.Formats;
import delta.games.lotro.utils.TypedProperties;

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
  private JTextField _date;
  private JTextField _shortDescription;
  //private JTextArea _description;

  // Data
  private CharacterData _toon;

  /**
   * Constructor.
   * @param toon Toon to display.
   */
  public CharacterMainAttrsEditionPanelController(CharacterData toon)
  {
    _toon=toon;
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
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
    _level=buildLevelCombo();
    firstLinePanel.add(_level.getComboBox());
    ItemSelectionListener<Integer> levelListener=new ItemSelectionListener<Integer>()
    {
      public void itemSelected(Integer level)
      {
        _toon.setLevel(level.intValue());
        // Broadcast level update event...
        CharacterEvent event=new CharacterEvent(null,_toon);
        CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_UPDATED,event);
      }
    };
    _level.addListener(levelListener);
    // Date
    _date=GuiFactory.buildTextField("");
    _date.setColumns(10);
    firstLinePanel.add(_date);

    // 2nd line
    JPanel secondLinePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // Short description
    secondLinePanel.add(GuiFactory.buildLabel("Description:"));
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
    CharacterClass cClass=_toon.getCharacterClass();
    ImageIcon classIcon=LotroIconsManager.getClassIcon(cClass,LotroIconsManager.MEDIUM_SIZE);
    _classIcon.setIcon(classIcon);
    // Race icon
    Race race=_toon.getRace();
    ImageIcon raceIcon=LotroIconsManager.getRaceIcon(race);
    _raceIcon.setIcon(raceIcon);
    // Name
    _name.setText(_toon.getName());
    // Level
    _level.selectItem(Integer.valueOf(_toon.getLevel()));
    // Date
    Long timestamp=_toon.getDate();
    Date date=(timestamp!=null)?new Date(timestamp.longValue()):null;
    String dateStr=Formats.getDateTimeString(date);
    _date.setText(dateStr);
    // Short description
    _shortDescription.setText(_toon.getShortDescription());
  }

  /**
   * Get data from UI gadgets.
   */
  public void get()
  {
    // Name
    _toon.setName(_name.getText());
    // Level
    Integer level=_level.getSelectedItem();
    if (level!=null)
    {
      _toon.setLevel(level.intValue());
    }
    // Date
    Date date=Formats.parseDate(_date.getText());
    if (date!=null)
    {
      _toon.setDate(Long.valueOf(date.getTime()));
    }
    // Short description
    _toon.setShortDescription(_shortDescription.getText());
  }

  private ComboBoxController<Integer> buildLevelCombo()
  {
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>();
    TypedProperties props=Config.getInstance().getParameters();
    int maxLevel=props.getIntProperty("max.character.level.server",105);
    List<Integer> levels=new ArrayList<Integer>();
    for(int i=1;i<=maxLevel;i++)
    {
      levels.add(Integer.valueOf(i));
    }
    for(Integer level : levels)
    {
      ctrl.addItem(level,level.toString());
    }
    ctrl.selectItem(levels.get(maxLevel-1));
    return ctrl;
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
    _toon=null;
    _classIcon=null;
    _name=null;
    _level=null;
    _date=null;
    _shortDescription=null;
  }
}
