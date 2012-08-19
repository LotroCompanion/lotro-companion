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
  
  private JPanel buildPanel()
  {
    JPanel panel=new JPanel();
    panel.setPreferredSize(new Dimension(300,70));
    ImageIcon classIcon=getClassIcon();
    panel.add(new JLabel(classIcon));
    ImageIcon raceIcon=getRaceIcon();
    panel.add(new JLabel(raceIcon));
    panel.setBackground(Color.WHITE);
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
