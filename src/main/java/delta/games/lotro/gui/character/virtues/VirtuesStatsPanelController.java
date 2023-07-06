package delta.games.lotro.gui.character.virtues;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.virtues.VirtuesContributionsMgr;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.gui.common.stats.StatsPanel;

/**
 * Controller for a panel to display the stats of virtues.
 * @author DAM
 */
public class VirtuesStatsPanelController
{
  private JPanel _panel;
  private JPanel _activesPanel;
  private JPanel _passivesPanel;

  /**
   * Constructor.
   */
  public VirtuesStatsPanelController()
  {
    _panel=build();
  }

  /**
   * Update the display using the given virtues.
   * @param virtues Virtues to use.
   */
  public void update(VirtuesSet virtues)
  {
    VirtuesContributionsMgr contribsMgr=VirtuesContributionsMgr.get();
    BasicStatsSet activeStats=contribsMgr.getStatsContributions(virtues,true,false);
    StatsPanel.fillStatsPanel(_activesPanel,activeStats);
    BasicStatsSet passiveStats=contribsMgr.getStatsContributions(virtues,false,true);
    StatsPanel.fillStatsPanel(_passivesPanel,passiveStats);
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
    // - actives
    _activesPanel=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder activesBorder=GuiFactory.buildTitledBorder("Active virtues"); // I18n
    _activesPanel.setBorder(activesBorder);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_activesPanel,c);
    // - passives
    _passivesPanel=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder passivesBorder=GuiFactory.buildTitledBorder("Passive virtues"); // I18n
    _passivesPanel.setBorder(passivesBorder);
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_passivesPanel,c);
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
    _activesPanel=null;
    _passivesPanel=null;
  }
}
