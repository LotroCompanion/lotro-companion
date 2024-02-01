package delta.games.lotro.gui.character.status.achievables.statistics;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.WindowController;

/**
 * Controller for a table panel of a tab of the achievable statistics display panel.
 * @param <T> Type of managed entries.
 * @author DAM
 */
public class AchievableStatisticsTabPanelController<T>
{
  // GUI
  private JPanel _panel;
  // Controllers
  private GenericTablePanelController<T> _panelController;
  private GenericTableController<T> _tableController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param table Table to show.
   */
  public AchievableStatisticsTabPanelController(WindowController parent, GenericTableController<T> table)
  {
    _tableController=table;
    _panelController=new GenericTablePanelController<>(parent,table);
  }

  /**
   * Configure the labels.
   * @param borderTitle Title of the border.
   * @param countLabel Prefix for the count label.
   */
  public void configure(String borderTitle, String countLabel)
  {
    _panelController.getConfiguration().setBorderTitle(borderTitle);
    _panelController.getCountsDisplay().setText(countLabel);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
      _panel.add(_panelController.getPanel(),BorderLayout.CENTER);
    }
    return _panel;
  }

  /**
   * Update display.
   */
  public void update()
  {
    _panelController.getCountsDisplay().update();
    _tableController.refresh();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    _panel=null;
    // Controllers
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
  }
}
