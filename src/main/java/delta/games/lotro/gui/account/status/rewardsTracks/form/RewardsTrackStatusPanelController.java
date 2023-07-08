package delta.games.lotro.gui.account.status.rewardsTracks.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.rewardsTrack.RewardsTrack;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller for a panel to show a rewards track status.
 * @author DAM
 */
public class RewardsTrackStatusPanelController
{
  // Data
  private RewardsTrackStatus _status;
  private RewardsTrack _rewardsTrack;
  // UI
  private JPanel _panel;
  // Controllers
  private RewardsTrackStatusSummaryPanelController _statusSummary;
  private RewardsTrackStatusRewardsPanelController _rewards;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to show.
   */
  public RewardsTrackStatusPanelController(WindowController parent, RewardsTrackStatus status)
  {
    _status=status;
    _rewardsTrack=_status.getRewardsTrack();
    _statusSummary=new RewardsTrackStatusSummaryPanelController(status);
    _rewards=new RewardsTrackStatusRewardsPanelController(parent,status);
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
    // Summary panel
    JPanel summaryPanel=buildAllegianceSummaryPanel();
    // Rewards panel
    JPanel rewardsPanel=_rewards.getPanel();
    String rewardsTitle=Labels.getLabel("rewards.track.details.rewards.title");
    rewardsPanel.setBorder(GuiFactory.buildTitledBorder(rewardsTitle));
    // Status summary panel
    JPanel statusSummaryPanel=_statusSummary.getPanel();

    // Assembly
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    int y=0;
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,5,0,5),0,0);
    ret.add(summaryPanel,c);
    y++;
    if (statusSummaryPanel!=null)
    {
      String statusSummaryTitle=Labels.getLabel("rewards.track.details.statusSummary.title");
      statusSummaryPanel.setBorder(GuiFactory.buildTitledBorder(statusSummaryTitle));
      c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,5,0,5),0,0);
      ret.add(statusSummaryPanel,c);
      y++;
    }
    c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,5,0,5),0,0);
    ret.add(rewardsPanel,c);

    return ret;
  }

  private JPanel buildAllegianceSummaryPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Name
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0);
    ret.add(buildNameGadget(),c);
    // Description
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,5),0,0);
    ret.add(buildDescriptionGadget(),c);
    return ret;
  }

  private JLabel buildNameGadget()
  {
    String name=_rewardsTrack.getName();
    return GuiFactory.buildLabel(name,28.0f);
  }
 
  private JComponent buildDescriptionGadget()
  {
    JEditorPane ret=GuiFactory.buildHtmlPanel();
    String html="<html><body>"+HtmlUtils.toHtml(_rewardsTrack.getDescription())+"</body></html>";
    ret.setText(html);
    ret.setCaretPosition(0);
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _rewardsTrack=null;
    _status=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_statusSummary!=null)
    {
      _statusSummary.dispose();
      _statusSummary=null;
    }
    if (_rewards!=null)
    {
      _rewards.dispose();
      _rewards=null;
    }
  }
}
