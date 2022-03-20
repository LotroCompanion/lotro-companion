package delta.games.lotro.utils.charts;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.utils.MultipleToonsStats;
import delta.games.lotro.gui.character.chooser.CharacterSelectionStateListener;
import delta.games.lotro.gui.character.chooser.CharacterSelectionStructureChangeEvent;
import delta.games.lotro.gui.character.chooser.CharacterSelectionStructureListener;
import delta.games.lotro.gui.character.chooser.CharactersSelectionPanelController;
import delta.games.lotro.gui.character.status.curves.DatedCurveProvider;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartConfiguration;
import delta.games.lotro.gui.character.status.curves.DatedCurvesChartController;
import delta.games.lotro.gui.character.status.curves.MultipleToonsDatedCurvesProvider;

/**
 * Controller for a panel to show a chart with curves for a series of characters.
 * @param <T> Type of managed stats.
 * @author DAM
 */
public class MultipleToonsDatedCurvesChartPanelController<T> implements CharacterSelectionStateListener,CharacterSelectionStructureListener
{
  // GUI
  private JPanel _panel;
  // Controllers
  private WindowController _parentController;
  private DatedCurvesChartController _chartController;
  private CharactersSelectionPanelController _toonSelectionController;
  // Data
  private MultipleToonsStats<T> _stats;

  /**
   * Constructor.
   * @param parentController Parent window controller.
   * @param stats Stats to display.
   * @param curveProvider Curve provider.
   * @param configuration Chart configuration.
   */
  public MultipleToonsDatedCurvesChartPanelController(WindowController parentController, MultipleToonsStats<T> stats, DatedCurveProvider<T> curveProvider, DatedCurvesChartConfiguration configuration)
  {
    _parentController=parentController;
    _stats=stats;
    MultipleToonsDatedCurvesProvider<T> provider=new MultipleToonsDatedCurvesProvider<T>(stats,curveProvider);
    _chartController=new DatedCurvesChartController(provider,configuration);
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
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());

    // Chart
    JPanel chartPanel=_chartController.getPanel();
    panel.add(chartPanel,BorderLayout.CENTER);

    // Toons selection
    List<CharacterFile> toons=_stats.getToonsList();
    _toonSelectionController=new CharactersSelectionPanelController(_parentController,toons);
    _toonSelectionController.getStateListenersManager().addListener(this);
    _toonSelectionController.getStructureListenersManager().addListener(this);
    JPanel toonsControlPanel=_toonSelectionController.getPanel();
    panel.add(toonsControlPanel,BorderLayout.EAST);
    return panel;
  }

  @Override
  public void selectionStructureChanged(CharacterSelectionStructureChangeEvent event)
  {
    for(CharacterFile addedToon : event.getAddedToons())
    {
      _stats.addToon(addedToon);
    }
    for(CharacterFile removedToon : event.getRemovedToons())
    {
      _stats.removeToon(removedToon);
    }
    _chartController.refresh();
  }

  @Override
  public void selectionStateChanged(String toonId, boolean selected)
  {
    if (_chartController!=null)
    {
      _chartController.setVisible(toonId,selected);
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
    _parentController=null;
    if (_chartController!=null)
    {
      _chartController.dispose();
      _chartController=null;
    }
    if (_toonSelectionController!=null)
    {
      _toonSelectionController.dispose();
      _toonSelectionController=null;
    }
    _stats=null;
  }
}
