package delta.games.lotro.gui.character.status.reputation.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.reputation.FactionStatus;

/**
 * Controller for a faction display panel.
 * It includes:
 * <ul>
 * <li>an edition panel,
 * <li>a chart to display the history of a faction.
 * </ul>
 * @author DAM
 */
public class FactionStatusPanelController
{
  // Controllers
  private FactionStatusEditionPanelController _statusController;
  private FactionHistoryEditionPanelController _historyController;
  private FactionHistoryChartPanelController _chartController;

  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param status Faction status to display.
   */
  public FactionStatusPanelController(FactionStatus status)
  {
    _statusController=new FactionStatusEditionPanelController(status);
    _chartController=new FactionHistoryChartPanelController(status);
    _historyController=new FactionHistoryEditionPanelController(status,_chartController);
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
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel statusPanel=_statusController.getPanel();
    statusPanel.setBorder(GuiFactory.buildTitledBorder("Current reputation"));
    panel.add(statusPanel,c);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel historyPanel=_historyController.getPanel();
    historyPanel.setBorder(GuiFactory.buildTitledBorder("History"));
    panel.add(historyPanel,c);
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    JPanel chartPanel=_chartController.getPanel();
    chartPanel.setBorder(GuiFactory.buildTitledBorder("History chart"));
    panel.add(chartPanel,c);
    return panel;
  }

  /**
   * Update data from the UI contents.
   */
  public void updateData()
  {
    _statusController.updateData();
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
    if (_statusController!=null)
    {
      _statusController.dispose();
      _statusController=null;
    }
    if (_historyController!=null)
    {
      _historyController.dispose();
      _historyController=null;
    }
    if (_chartController!=null)
    {
      _chartController.dispose();
      _chartController=null;
    }
  }
}
