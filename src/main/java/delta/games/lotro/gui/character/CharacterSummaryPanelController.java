package delta.games.lotro.gui.character;

import java.awt.Color;
import java.awt.Dimension;

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
  private JLabel _regionLabel;
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
    JPanel panel=new JPanel();
    panel.setPreferredSize(new Dimension(300,70));
    ImageIcon classIcon=getClassIcon();
    panel.add(new JLabel(classIcon));
    ImageIcon raceIcon=getRaceIcon();
    panel.add(new JLabel(raceIcon));
    panel.setBackground(Color.BLACK);
    _nameLabel=buildLabel(32.0f);
    _regionLabel=buildLabel(24.0f);
    _levelLabel=buildLabel(32.0f);
    Character info=_toon.getLastCharacterInfo();
    if (info!=null)
    {
      String name=info.getName();
      _nameLabel.setText(name);
      String region=info.getRegion();
      _regionLabel.setText("of "+region);
      int level=info.getLevel();
      _levelLabel.setText(String.valueOf(level));
    }
    panel.add(_nameLabel);
    panel.add(_regionLabel);
    panel.add(_levelLabel);
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
