package delta.games.lotro.gui.clientImport;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;
import delta.games.lotro.memory.facade.data.ImportStatusData;

/**
 * Controller for a panel to display the character data.
 * @author DAM
 */
public class CharacterDisplayPanelController implements Disposable
{
  // UI
  private JPanel _panel;
  private JLabel _characterName;
  private JLabel _characterClass;
  private JLabel _race;
  private JLabel _level;

  /**
   * Constructor.
   */
  public CharacterDisplayPanelController()
  {
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    _characterName=GuiFactory.buildLabel("");
    _race=GuiFactory.buildLabel("");
    _characterClass=GuiFactory.buildLabel("");
    _level=GuiFactory.buildLabel("");

    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
    c.gridx++;
    ret.add(Box.createHorizontalStrut(150),c);
    c.gridx=0;c.gridy++;
    // Character name
    ret.add(GuiFactory.buildLabel("Name:"),c); // I18n
    c.gridx++;
    ret.add(_characterName,c);
    c.gridx=0;c.gridy++;
    // Class
    ret.add(GuiFactory.buildLabel("Class:"),c); // I18n
    c.gridx++;
    ret.add(_characterClass,c);
    c.gridx=0;c.gridy++;
    // Race
    ret.add(GuiFactory.buildLabel("Race:"),c); // I18n
    c.gridx++;
    ret.add(_race,c);
    c.gridx=0;c.gridy++;
    // Level
    ret.add(GuiFactory.buildLabel("Level:"),c); // I18n
    c.gridx++;
    ret.add(_level,c);
    c.gridx=0;c.gridy++;
    return ret;
  }

  /**
   * Update UI using the given data.
   * @param data Input data.
   */
  public void updateUi(ImportStatusData data)
  {
    // Character name
    String characterName=data.getCharacterName();
    characterName=(characterName!=null)?characterName:"?";
    _characterName.setText(characterName);
    // Character class
    String characterClass=data.getCharacterClass();
    characterClass=(characterClass!=null)?characterClass:"?";
    _characterClass.setText(characterClass);
    // Race
    String race=data.getCharacterRace();
    race=(race!=null)?race:"?";
    _race.setText(race);
    // Level
    Integer level=data.getCharacterLevel();
    String levelStr=(level!=null)?level.toString():"?";
    _level.setText(levelStr);
  }

  @Override
  public void dispose()
  {
    _characterName=null;
    _race=null;
    _characterClass=null;
    _level=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
