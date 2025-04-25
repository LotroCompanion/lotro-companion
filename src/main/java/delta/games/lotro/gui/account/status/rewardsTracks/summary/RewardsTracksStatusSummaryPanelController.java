package delta.games.lotro.gui.account.status.rewardsTracks.summary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.account.status.rewardsTrack.RewardsTracksStatusManager;
import delta.games.lotro.gui.account.status.rewardsTracks.form.RewardsTrackStatusWindowController;
import delta.games.lotro.lore.rewardsTrack.RewardsTrack;
import delta.games.lotro.lore.rewardsTrack.RewardsTracksManager;

/**
 * Controller for a panel that display the status of all rewards tracks.
 * @author DAM
 */
public class RewardsTracksStatusSummaryPanelController
{
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<SingleRewardsTrackGadgetsController> _gadgets;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statusMgr Status to show.
   */
  public RewardsTracksStatusSummaryPanelController(WindowController parent, RewardsTracksStatusManager statusMgr)
  {
    _parent=parent;
    _gadgets=new ArrayList<SingleRewardsTrackGadgetsController>();
    _panel=buildPanel(statusMgr);
  }

  private JPanel buildPanel(RewardsTracksStatusManager statusMgr)
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    RewardsTracksManager mgr=RewardsTracksManager.getInstance();
    List<RewardsTrack> rewardsTracks=mgr.getRewardsTracks(false);
    int nbColumns=2;
    int x=0;
    int y=0;
    for(RewardsTrack rewardsTrack : rewardsTracks)
    {
      JPanel panel=buildRewardsTrackPanel(rewardsTrack,statusMgr);
      GridBagConstraints c=new GridBagConstraints(x,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,5),0,0);
      ret.add(panel,c);
      x++;
      if (x==nbColumns)
      {
        x=0;
        y++;
      }
    }
    return ret;
  }

  private JPanel buildRewardsTrackPanel(RewardsTrack rewardsTrack, RewardsTracksStatusManager statusMgr)
  {
    final RewardsTrackStatus status=statusMgr.getStatus(rewardsTrack,true);
    SingleRewardsTrackGadgetsController gadgets=new SingleRewardsTrackGadgetsController(status);
    _gadgets.add(gadgets);

    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    panel.setBorder(GuiFactory.buildTitledBorder(rewardsTrack.getName()));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(gadgets.getStateGadget(),c);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showRewardsTrackStatusDetails(status);
      }
    };
    JButton button=gadgets.getDetailsButton();
    button.addActionListener(al);
    panel.add(button,c);
    return panel;
  }

  private void showRewardsTrackStatusDetails(RewardsTrackStatus status)
  {
    WindowsManager mgr=_parent.getWindowsManager();
    RewardsTrack rewardsTrack=status.getRewardsTrack();
    String identifier=RewardsTrackStatusWindowController.getIdentifier(rewardsTrack);
    WindowController child=mgr.getWindow(identifier);
    if (child==null)
    {
      child=new RewardsTrackStatusWindowController(_parent,status);
      mgr.registerWindow(child);
      child.show();
    }
    else
    {
      child.bringToFront();
    }
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    return _panel;
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
    _parent=null;
    if (_gadgets!=null)
    {
      for(SingleRewardsTrackGadgetsController gadget : _gadgets)
      {
        gadget.dispose();
      }
      _gadgets.clear();
      _gadgets=null;
    }
  }
}
