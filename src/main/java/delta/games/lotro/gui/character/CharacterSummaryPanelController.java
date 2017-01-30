package delta.games.lotro.gui.character;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconUtils;

/**
 * Controller for character summary panel.
 * @author DAM
 */
public class CharacterSummaryPanelController
{
  private JPanel _panel;
  private CharacterSummary _summary;
  private JLabel _nameLabel;
  private JLabel _levelLabel;
  
  /**
   * Constructor.
   * @param summary Summary of toon to display.
   */
  public CharacterSummaryPanelController(CharacterSummary summary)
  {
    _summary=summary;
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
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);

    CharacterClass cClass=null;
    Race race=null;
    if (_summary!=null)
    {
      cClass=_summary.getCharacterClass();
      race=_summary.getRace();
    }
    ImageIcon classIcon=IconUtils.getClassIcon(cClass,IconUtils.MEDIUM_SIZE);
    panel.add(GuiFactory.buildIconLabel(classIcon),c);
    ImageIcon raceIcon=IconUtils.getRaceIcon(race);
    c.gridx=1;
    panel.add(GuiFactory.buildIconLabel(raceIcon),c);
    _nameLabel=GuiFactory.buildLabel("",28.0f);
    _levelLabel=GuiFactory.buildLabel("",32.0f);
    update();
    c.gridx=3;c.anchor=GridBagConstraints.EAST;
    panel.add(_levelLabel,c);
    c.gridx=2;c.anchor=GridBagConstraints.CENTER;
    c.weightx=1.0;c.fill=GridBagConstraints.BOTH;
    panel.add(_nameLabel,c);
    return panel;
  }

  /**
   * Update contents.
   */
  public void update()
  {
    if (_summary!=null)
    {
      String name=_summary.getName();
      //String region=_summary.getRegion();
      String text=name;//+" of "+region;
      _nameLabel.setText(text);
      /*
      int level=_summary.getLevel();
      _levelLabel.setText(String.valueOf(level));
      */
    }
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
    _summary=null;
  }
}
