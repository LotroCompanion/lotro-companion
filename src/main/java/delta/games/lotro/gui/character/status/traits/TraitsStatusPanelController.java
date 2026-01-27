package delta.games.lotro.gui.character.status.traits;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.traits.filters.TraitStatusFilter;
import delta.games.lotro.character.status.traits.shared.AvailableTraitsStatus;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.character.status.traits.filter.TraitStatusFilterController;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for a panel that shows the availability of traits.
 * @author DAM
 */
public class TraitsStatusPanelController
{
  // Controllers
  private WindowController _parent;
  private TraitStatusFilterController _filterController;
  private TraitsStatusDisplayPanelController _panelController;
  // Data
  private AvailableTraitsStatus _status;
  private TraitStatusFilter _filter;
  private List<TraitDescription> _traits;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param traits Traits to show.
   * @param status Status to show.
   */
  public TraitsStatusPanelController(WindowController parent, List<TraitDescription> traits, AvailableTraitsStatus status)
  {
    _parent=parent;
    _traits=new ArrayList<TraitDescription>(traits);
    _filter=new TraitStatusFilter();
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
    _panelController=new TraitsStatusDisplayPanelController(_parent,_traits,_status,_filter);
    JPanel displayPanel=_panelController.getPanel();
    JScrollPane scroll=GuiFactory.buildScrollPane(displayPanel);
    scroll.setBorder(GuiFactory.buildTitledBorder("Traits")); // I18n
    // Filter
    _filterController=new TraitStatusFilterController(_filter,_panelController);
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
    _traits=null;
  }
}
