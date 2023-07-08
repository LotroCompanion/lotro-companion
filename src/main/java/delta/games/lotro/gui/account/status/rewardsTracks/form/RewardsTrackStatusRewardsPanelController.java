package delta.games.lotro.gui.account.status.rewardsTracks.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.multicheckbox.MultiCheckboxController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for a panel to display the rewards of a rewards track status.
 * @author DAM
 */
public class RewardsTrackStatusRewardsPanelController
{
  // Data
  private RewardsTrackStepStateFilter _filter;
  // UI
  private JPanel _panel;
  // Controllers
  private MultiCheckboxController<RewardsTrackStepState> _stateFilter;
  private RewardsTrackRewardsSummaryPanelController _summary;
  private RewardsTrackRewardsDetailsPanelController _details;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to show.
   */
  public RewardsTrackStatusRewardsPanelController(WindowController parent, RewardsTrackStatus status)
  {
    _filter=new RewardsTrackStepStateFilter();
    _stateFilter=buildFilterUI();
    _summary=new RewardsTrackRewardsSummaryPanelController(parent,status,_filter);
    _details=new RewardsTrackRewardsDetailsPanelController(parent,status,_filter);
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
    String totalTitle=Labels.getLabel("rewards.track.details.total.title");
    summaryPane.setBorder(GuiFactory.buildTitledBorder(totalTitle));
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    ret.add(summaryPane,c);
    // Details
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details.getPanel());
    String detailsTitle=Labels.getLabel("rewards.track.details.details.title");
    detailsPane.setBorder(GuiFactory.buildTitledBorder(detailsTitle));
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    ret.add(detailsPane,c);
    return ret;
  }

  private MultiCheckboxController<RewardsTrackStepState> buildFilterUI()
  {
    final MultiCheckboxController<RewardsTrackStepState> ret=new MultiCheckboxController<RewardsTrackStepState>();
    for(RewardsTrackStepState state : RewardsTrackStepState.values())
    {
      ret.addItem(state,state.getLabel());
      ret.setItemState(state,true);
    }
    // Listener
    ItemSelectionListener<RewardsTrackStepState> listener=new ItemSelectionListener<RewardsTrackStepState>()
    {
      @Override
      public void itemSelected(RewardsTrackStepState level)
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
