package delta.games.lotro.gui.character.status.birding;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.collections.birds.BirdsStatusManager;
import delta.games.lotro.character.status.collections.birds.filters.BirdStatusFilter;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.collections.birds.BirdDescription;
import delta.games.lotro.lore.collections.birds.BirdsManager;

/**
 * Controller for a panel that show the status of birding.
 * @author DAM
 */
public class BirdingStatusPanelController
{
  // Controllers
  private WindowController _parent;
  private BirdingStatusFilterController _filterController;
  private BirdingStatusDisplayPanelController _panelController;
  // Data
  private BirdsStatusManager _status;
  private BirdStatusFilter _filter;
  private List<BirdDescription> _birds;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public BirdingStatusPanelController(WindowController parent, BirdsStatusManager status)
  {
    _parent=parent;
    _birds=BirdsManager.getInstance().getAll();
    _filter=new BirdStatusFilter();
    _status=status;
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Display
    _panelController=new BirdingStatusDisplayPanelController(_parent,_birds,_status,_filter);
    JPanel displayPanel=_panelController.getPanel();
    JScrollPane scroll=GuiFactory.buildScrollPane(displayPanel);
    scroll.setBorder(GuiFactory.buildTitledBorder("Birds")); // I18n
    // Filter
    _filterController=new BirdingStatusFilterController(_filter,_panelController);
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

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
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
    _birds=null;
  }
}
