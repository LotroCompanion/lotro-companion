package delta.games.lotro.gui.character;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.gui.utils.IconsManager;

/**
 * Controller for character summary panel.
 * @author DAM
 */
public class CharacterSummaryPanelController
{
  private JPanel _panel;
  private CharacterFile _toon;
  private JLabel _nameLabel;
  private JLabel _levelLabel;
  
  /**
   * Constructor.
   * @param toon Toon to display.
   */
  public CharacterSummaryPanelController(CharacterFile toon)
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
  
  private JLabel buildLabel(float size)
  {
    JLabel label=new JLabel();
    label.setForeground(Color.WHITE);
    label.setFont(label.getFont().deriveFont(size));
    return label;
  }

  private JPanel buildPanel()
  {
    JPanel panel=new JPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    ImageIcon classIcon=getClassIcon();
    panel.add(new JLabel(classIcon),c);
    ImageIcon raceIcon=getRaceIcon();
    c.gridx=1;
    panel.add(new JLabel(raceIcon),c);
    panel.setBackground(Color.BLACK);
    _nameLabel=buildLabel(28.0f);
    _levelLabel=buildLabel(32.0f);
    Character info=_toon.getLastCharacterInfo();
    if (info!=null)
    {
      String name=info.getName();
      String region=info.getRegion();
      String text=name+" of "+region;
      _nameLabel.setText(text);
      int level=info.getLevel();
      _levelLabel.setText(String.valueOf(level));
    }
    c.gridx=3;c.anchor=GridBagConstraints.EAST;
    panel.add(_levelLabel,c);
    c.gridx=2;c.anchor=GridBagConstraints.CENTER;
    c.weightx=1.0;c.fill=GridBagConstraints.BOTH;
    panel.add(_nameLabel,c);
    return panel;
  }

  private ImageIcon getClassIcon()
  {
    ImageIcon ret=null;
    Character c=_toon.getLastCharacterInfo();
    if (c!=null)
    {
      CharacterClass cClass=c.getCharacterClass();
      if (cClass!=null)
      {
        String classIconPath=cClass.getIconPath();
        String iconPath="/resources/gui/classes/"+classIconPath+".png";
        ret=IconsManager.getIcon(iconPath);
      }
    }
    return ret;
  }

  private ImageIcon getRaceIcon()
  {
    ImageIcon ret=null;
    Character c=_toon.getLastCharacterInfo();
    if (c!=null)
    {
      Race race=c.getRace();
      if (race!=null)
      {
        String classIconPath=race.getIconPath();
        String iconPath="/resources/gui/races/"+classIconPath+".png";
        ret=IconsManager.getIcon(iconPath);
      }
    }
    return ret;
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
  }
}
