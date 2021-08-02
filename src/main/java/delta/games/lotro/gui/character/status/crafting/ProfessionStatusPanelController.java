package delta.games.lotro.gui.character.status.crafting;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.crafting.ProfessionStatus;

/**
 * Controller for a panel to display a profession status.
 * It includes:
 * <ul>
 * <li>an edition panel,
 * <li>a chart that displays the profession history.
 * </ul>
 * @author DAM
 */
public class ProfessionStatusPanelController
{
  // Controllers
  private ProfessionStatusEditionPanelController _editionController;
  private ProfessionHistoryChartPanelController _chartController;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param status Profession status to display.
   */
  public ProfessionStatusPanelController(ProfessionStatus status)
  {
    _chartController=new ProfessionHistoryChartPanelController(status);
    _editionController=new ProfessionStatusEditionPanelController(status,_chartController);
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
   * Update data from UI contents.
   */
  public void updateDataFromUi()
  {
    _editionController.updateDataFromUi();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
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
