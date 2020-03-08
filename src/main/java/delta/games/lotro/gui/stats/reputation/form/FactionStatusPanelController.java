package delta.games.lotro.gui.stats.reputation.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.reputation.FactionStatus;

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
  private FactionStatusEditionPanelController _editionController;
  private FactionHistoryChartPanelController _chartController;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param status Faction status to display.
   */
  public FactionStatusPanelController(FactionStatus status)
  {
    _chartController=new FactionHistoryChartPanelController(status);
    _editionController=new FactionStatusEditionPanelController(status,_chartController);
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
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(_editionController.getPanel(),c);
    c=new GridBagConstraints(1,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(_chartController.getPanel(),c);
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
    if (_editionController!=null)
    {
      _editionController.dispose();
      _editionController=null;
    }
    if (_chartController!=null)
    {
      _chartController.dispose();
      _chartController=null;
    }
  }
}
