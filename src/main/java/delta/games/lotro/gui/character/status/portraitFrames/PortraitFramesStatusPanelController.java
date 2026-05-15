package delta.games.lotro.gui.character.status.portraitFrames;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.portraitFrames.PortraitFramesStatus;
import delta.games.lotro.character.status.portraitFrames.filters.PortraitFramesStatusFilter;
import delta.games.lotro.gui.character.status.portraitFrames.filter.PortraitFramesStatusFilterController;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for a window that show the status of portrait frames.
 * @author DAM
 */
public class PortraitFramesStatusPanelController extends AbstractPanelController
{
  // Controllers
  private PortraitFramesStatusFilterController _filterController;
  private PortraitFramesStatusDisplayPanelController _panelController;
  // Data
  private PortraitFramesStatus _status;
  private PortraitFramesStatusFilter _filter;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public PortraitFramesStatusPanelController(WindowController parent, PortraitFramesStatus status)
  {
    super(parent);
    _filter=new PortraitFramesStatusFilter();
    _status=status;
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Display
    WindowController parent=getParentWindowController();
    _panelController=new PortraitFramesStatusDisplayPanelController(parent,_status,_filter);
    JPanel displayPanel=_panelController.getPanel();
    JScrollPane scroll=GuiFactory.buildScrollPane(displayPanel);
    scroll.setBorder(GuiFactory.buildTitledBorder("Portrait frames")); // I18n
    // Filter
    _filterController=new PortraitFramesStatusFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder(Labels.getLabel("shared.title.filter"));
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(scroll,c);
    return panel;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Controllers
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    // Data
    _status=null;
    _filter=null;
  }
}
