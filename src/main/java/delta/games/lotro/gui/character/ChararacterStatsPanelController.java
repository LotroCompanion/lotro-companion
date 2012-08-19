package delta.games.lotro.gui.character;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import delta.games.lotro.character.CharacterFile;

/**
 * Controller for character stats panel.
 * @author DAM
 */
public class ChararacterStatsPanelController
{
  private JPanel _panel;
  private CharacterFile _toon;
  
  /**
   * Constructor.
   * @param toon Toon to display.
   */
  public ChararacterStatsPanelController(CharacterFile toon)
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
    panel.setPreferredSize(new Dimension(100,500));
    panel.setBackground(Color.GREEN);
    
    // Show stats
    // Morale, Power, Armor
    // Might, Agility, Vitality, Will, Fate
    // Offence: crit hit, finesse,
    // Physical mastery X:
    // Tactical Mastery X:
    // Defence: Resistance, crit hit avoidance, incoming healing X
    // - Avoidance: block, parry, evade
    // - mitigations:
    // -- source: melee X , ranged X, tactical X
    // -- type: physical, tactical
    
    
    return panel;
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
