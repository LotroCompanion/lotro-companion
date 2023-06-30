package delta.games.lotro.gui.character.status.reputation.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.character.status.reputation.FactionStatus;

/**
 * Controller for a panel that displays the history of a single faction.
 * @author DAM
 */
public class FactionHistoryChartPanelController extends AbstractPanelController
{
  // GUI
  private FactionHistoryChartController _history;
  // Data
  private FactionStatus _stats;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param stats Faction stats to display.
   */
  public FactionHistoryChartPanelController(AreaController parent, FactionStatus stats)
  {
    super(parent);
    _stats=stats;
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    JPanel panel=super.getPanel();
    if (panel==null)
    {
      panel=buildPanel();
      setPanel(panel);
    }
    return panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // History chart
    _history=new FactionHistoryChartController(this,_stats);
    JPanel historyPanel=_history.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,10,5,10),0,0);
    panel.add(historyPanel,c);

    return panel;
  }

  /**
   * Update graph data.
   */
  public void updateData()
  {
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
    super.dispose();
    if (_history!=null)
    {
      _history.dispose();
      _history=null;
    }
    _stats=null;
  }
}
