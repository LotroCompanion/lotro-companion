package delta.games.lotro.gui.stats.reputation;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;

/**
 * Controller for a reputation panel.
 * @author DAM
 */
public class FactionEditionPanelController
{
  private Faction _faction;
  private JLabel _label;
  private JProgressBar _bar;

  /**
   * Constructor.
   * @param faction Faction to display.
   */
  public FactionEditionPanelController(Faction faction)
  {
    _faction=faction;
    // Label
    String name=_faction.getName();
    _label=GuiFactory.buildLabel(name);
    // Bar
    FactionLevel[] levels=_faction.getLevels();
    int min=0;
    int max=levels[levels.length-1].getValue();
    _bar=new JProgressBar(JProgressBar.HORIZONTAL,min,max);
    _bar.setBackground(GuiFactory.getBackgroundColor());
    _bar.setBorderPainted(true);
    _bar.setStringPainted(true);
  }

  /**
   * Get the managed faction.
   * @return the managed faction.
   */
  public Faction getFaction()
  {
    return _faction;
  }

  /**
   * Get the managed label.
   * @return the managed label.
   */
  public JLabel getLabel()
  {
    return _label;
  }

  /**
   * Get the managed progress bar.
   * @return the managed progress bar.
   */
  public JProgressBar getBar()
  {
    return _bar;
  }

  /**
   * Set the faction level.
   * @param level Level to set.
   */
  public void setFactionLevel(FactionLevel level)
  {
    String levelName=level.getName();
    int value=level.getValue();
    Color background=Color.BLUE;
    if (value<0)
    {
      background=Color.RED;
      value=-value;
    }
    _bar.setForeground(background);
    _bar.setString(levelName);
    _bar.setValue(value);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _faction=null;
    _label=null;
    _bar=null;
  }
}
