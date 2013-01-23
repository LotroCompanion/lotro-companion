package delta.games.lotro.gui.stats.warbands;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.stats.warbands.MultipleToonsWarbandsStats;
import delta.games.lotro.utils.gui.DefaultWindowController;

/**
 * Controller for a "warbands statistics" window.
 * @author DAM
 */
public class WarbandsWindowController extends DefaultWindowController
{
  private WarbandsPanelController _warbandsStatisticsPanelController;

  /**
   * Constructor.
   * @param stats Underlying warbands statistics.
   */
  public WarbandsWindowController(MultipleToonsWarbandsStats stats)
  {
    _warbandsStatisticsPanelController=new WarbandsPanelController(this,stats);
  }

  /**
   * Get the window identifier for a given toon.
   * @return A window identifier.
   */
  public static String getIdentifier()
  {
    return "WARBANDS";
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=_warbandsStatisticsPanelController.getPanel();
    return panel;
  }
  
  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String title="Warbands statistics";
    frame.setTitle(title);
    frame.pack();
    //frame.setResizable(false);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_warbandsStatisticsPanelController!=null)
    {
      _warbandsStatisticsPanelController.dispose();
      _warbandsStatisticsPanelController=null;
    }
  }
}
