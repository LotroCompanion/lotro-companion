package delta.games.lotro.gui.stats.traitPoints;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.stats.traitPoints.TraitPoints;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;

/**
 * Controller for trait points summary panel.
 * @author DAM
 */
public class TraitPointsSummaryPanelController
{
  private JPanel _panel;
  private CharacterSummary _summary;
  private TraitPointsStatus _status;
  private JLabel _nameLabel;

  /**
   * Constructor.
   * @param summary Summary of toon to display.
   * @param status Trait points status.
   */
  public TraitPointsSummaryPanelController(CharacterSummary summary, TraitPointsStatus status)
  {
    _summary=summary;
    _status=status;
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
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    _nameLabel=GuiFactory.buildLabel("",28.0f);
    update();
    panel.add(_nameLabel);
    return panel;
  }

  /**
   * Update contents.
   */
  public void update()
  {
    String points=getPoints();
    String toDisplay="Trait points: "+points;
    _nameLabel.setText(toDisplay);
  }

  private String getPoints()
  {
    CharacterClass characterClass=_summary.getCharacterClass();
    int level=_summary.getLevel();
    int maxPoints=TraitPoints.get().getMaxTraitPoints(characterClass,level);
    int points=_status.getPointsCount(level);
    String ret=points+" / "+maxPoints;
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _nameLabel=null;
    // Data
    _status=null;
    _summary=null;
  }
}
