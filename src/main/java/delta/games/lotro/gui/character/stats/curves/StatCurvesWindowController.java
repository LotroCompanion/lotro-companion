package delta.games.lotro.gui.character.stats.curves;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterData;

/**
 * Controller for a "stat curves" window.
 * @author DAM
 */
public class StatCurvesWindowController extends DefaultDialogController
{
  private StatCurvesPanelController _panelController;
  private StatCurvesChartConfiguration _configuration;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param configuration Configuration.
   */
  public StatCurvesWindowController(WindowController parent, StatCurvesChartConfiguration configuration)
  {
    super(parent);
    _panelController=new StatCurvesPanelController(configuration);
    _configuration=configuration;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel = _panelController.getPanel();
    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    // Title
    String title=_configuration.getTitle();
    dialog.setTitle(title);
    dialog.pack();
    dialog.setResizable(true);
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return _configuration.getTitle();
  }

  /**
   * Update contents with current toon data.
   * @param toon Character data to use.
   */
  public void update(CharacterData toon)
  {
    _panelController.update(toon);
    getWindow().pack();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    _configuration=null;
    super.dispose();
  }
}
