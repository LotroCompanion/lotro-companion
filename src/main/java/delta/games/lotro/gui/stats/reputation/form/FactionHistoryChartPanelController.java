package delta.games.lotro.gui.stats.reputation.form;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.reputation.FactionStatus;
import delta.games.lotro.lore.reputation.FactionLevel;

/**
 * Controller for a panel that displays the history of a single faction.
 * @author DAM
 */
public class FactionHistoryChartPanelController
{
  // GUI
  private JPanel _panel;
  private JLabel _label;
  private FactionHistoryChartController _history;
  // Data
  private FactionStatus _stats;

  /**
   * Constructor.
   * @param stats Faction stats to display.
   */
  public FactionHistoryChartPanelController(FactionStatus stats)
  {
    _stats=stats;
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());

    // Level label
    _label=GuiFactory.buildLabel("");
    updateLevelLabel();
    // History chart
    _history=new FactionHistoryChartController(_stats,false);
    JPanel historyPanel=_history.getPanel();

    // Assembly
    JPanel labelsPanel=GuiFactory.buildPanel(null);
    labelsPanel.setLayout(new BoxLayout(labelsPanel,BoxLayout.PAGE_AXIS));
    _label.setAlignmentX(Component.LEFT_ALIGNMENT);
    labelsPanel.add(_label);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,10,0,10),0,0);
    panel.add(labelsPanel,c);
    GridBagConstraints c2=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,10,5,10),0,0);
    panel.add(historyPanel,c2);

    return panel;
  }

  private void updateLevelLabel()
  {
    FactionLevel level=_stats.getFactionLevel();
    String name=(level!=null)?level.getName():"";
    _label.setText("Level: "+name);
  }

  /**
   * Update graph data.
   */
  public void updateData()
  {
    updateLevelLabel();
    if (_history!=null)
    {
      _history.updateData();
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
    _label=null;
    if (_history!=null)
    {
      _history.dispose();
      _history=null;
    }
    _stats=null;
  }
}
