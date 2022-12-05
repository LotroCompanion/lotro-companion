package delta.games.lotro.gui.character.virtues;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.character.status.virtues.VirtuesStatus;
import delta.games.lotro.character.status.virtues.VirtuesStatusUtils;

/**
 * Controller for a panel to display the statistics of virtues.
 * @author DAM
 */
public class VirtuesStatisticsPanelController
{
  // Data
  private VirtuesStatus _current;
  // UI
  private JPanel _panel;
  private JLabel _neededXP;

  /**
   * Constructor.
   * @param current Current virtues status.
   */
  public VirtuesStatisticsPanelController(VirtuesStatus current)
  {
    _current=current;
    _panel=build();
  }

  /**
   * Update the display using the given virtues.
   * @param virtues Virtues to use.
   */
  public void update(VirtuesSet virtues)
  {
    int neededXP=VirtuesStatusUtils.getNeededVirtueXP(virtues,_current);
    _neededXP.setText(String.valueOf(neededXP));
    _panel.revalidate();
    _panel.repaint();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    panel.setBorder(GuiFactory.buildTitledBorder("Statistics"));
    // Needed XP
    _neededXP=GuiFactory.buildLabel("");
    JLabel neededXP=GuiFactory.buildLabel("vXP need:");
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    panel.add(neededXP,c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,5),0,0);
    panel.add(_neededXP,c);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _current=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _neededXP=null;
  }
}
