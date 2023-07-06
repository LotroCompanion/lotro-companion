package delta.games.lotro.gui.character.status.allegiances.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.multicheckbox.MultiCheckboxController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.allegiances.AllegianceStatus;

/**
 * Controller for a panel to display the rewards of an allegiance status.
 * @author DAM
 */
public class AllegianceStatusRewardsPanelController
{
  // Data
  private AllegianceRewardsFilter _filter;
  // UI
  private JPanel _panel;
  // Controllers
  private MultiCheckboxController<AllegianceRewardState> _stateFilter;
  private AllegianceRewardsSummaryPanelController _summary;
  private AllegianceRewardsDetailsPanelController _details;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to show.
   */
  public AllegianceStatusRewardsPanelController(WindowController parent, AllegianceStatus status)
  {
    _filter=new AllegianceRewardsFilter();
    _stateFilter=buildFilterUI();
    _summary=new AllegianceRewardsSummaryPanelController(parent,status,_filter);
    _details=new AllegianceRewardsDetailsPanelController(parent,status,_filter);
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
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(_stateFilter.getPanel(),c);
    // Summary
    JScrollPane summaryPane=GuiFactory.buildScrollPane(_summary.getPanel());
    summaryPane.setBorder(GuiFactory.buildTitledBorder("Total")); // I18n
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    ret.add(summaryPane,c);
    // Details
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details.getPanel());
    detailsPane.setBorder(GuiFactory.buildTitledBorder("Details")); // I18n
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    ret.add(detailsPane,c);
    return ret;
  }

  private MultiCheckboxController<AllegianceRewardState> buildFilterUI()
  {
    final MultiCheckboxController<AllegianceRewardState> ret=new MultiCheckboxController<AllegianceRewardState>();
    for(AllegianceRewardState state : AllegianceRewardState.values())
    {
      ret.addItem(state,state.getLabel());
      ret.setItemState(state,true);
    }
    // Listener
    ItemSelectionListener<AllegianceRewardState> listener=new ItemSelectionListener<AllegianceRewardState>()
    {
      @Override
      public void itemSelected(AllegianceRewardState level)
      {
        _filter.setSelectedStates(ret.getSelectedItems());
        filterUpdated();
      }
    };
    ret.addListener(listener);
    return ret;
  }

  private void filterUpdated()
  {
    _summary.updatePanel();
    _details.updatePanel();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_stateFilter!=null)
    {
      _stateFilter.dispose();
      _stateFilter=null;
    }
    if (_summary!=null)
    {
      _summary.dispose();
      _summary=null;
    }
    if (_details!=null)
    {
      _details.dispose();
      _details=null;
    }
  }
}
