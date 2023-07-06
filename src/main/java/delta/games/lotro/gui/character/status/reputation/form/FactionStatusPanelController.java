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
 * Controller for a faction display panel.
 * It includes:
 * <ul>
 * <li>an edition panel,
 * <li>a chart to display the history of a faction.
 * </ul>
 * @author DAM
 */
public class FactionStatusPanelController extends AbstractPanelController
{
  // Controllers
  private FactionStatusEditionPanelController _statusController;
  private FactionHistoryEditionPanelController _historyController;
  private FactionHistoryChartPanelController _chartController;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Faction status to display.
   */
  public FactionStatusPanelController(AreaController parent, FactionStatus status)
  {
    super(parent);
    _statusController=new FactionStatusEditionPanelController(this,status);
    _chartController=new FactionHistoryChartPanelController(this,status);
    _historyController=new FactionHistoryEditionPanelController(this,status,_chartController);
  }

  @Override
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel statusPanel=_statusController.getPanel();
    statusPanel.setBorder(GuiFactory.buildTitledBorder("Current reputation")); // I18n
    panel.add(statusPanel,c);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel historyPanel=_historyController.getPanel();
    historyPanel.setBorder(GuiFactory.buildTitledBorder("History")); // I18n
    panel.add(historyPanel,c);
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    JPanel chartPanel=_chartController.getPanel();
    chartPanel.setBorder(GuiFactory.buildTitledBorder("History chart")); // I18n
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
  @Override
  public void dispose()
  {
    super.dispose();
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
