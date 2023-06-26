package delta.games.lotro.gui.account.status.rewardsTracks.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.IntegerHolder;
import delta.games.lotro.account.status.rewardsTrack.RewardsTrackStatus;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.rewards.ItemReward;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.gui.common.rewards.form.RewardsPanelController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.rewardsTrack.RewardsTrack;
import delta.games.lotro.lore.rewardsTrack.RewardsTrackStep;

/**
 * Controller for a panel to display rewards summary.
 * @author DAM
 */
public class RewardsTrackRewardsSummaryPanelController
{
  // Data
  private RewardsTrackStatus _status;
  private RewardsTrackStepStateFilter _filter;
  // Controllers
  private WindowController _parent;
  private RewardsPanelController _rewards;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to display.
   * @param filter Rewards track steps filter.
   */
  public RewardsTrackRewardsSummaryPanelController(WindowController parent, RewardsTrackStatus status, RewardsTrackStepStateFilter filter)
  {
    _parent=parent;
    _status=status;
    _filter=filter;
    _rewards=new RewardsPanelController(parent,new Rewards(),100);
    _panel=GuiFactory.buildPanel(new GridBagLayout());
    updatePanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Update the contents of the managed panel according to the filter status.
   */
  public void updatePanel()
  {
    _panel.removeAll();
    List<Item> itemRewards=new ArrayList<Item>();
    RewardsTrack rewardsTrack=_status.getRewardsTrack();
    int nbSteps=rewardsTrack.getSize();
    List<RewardsTrackStep> steps=rewardsTrack.getSteps();
    for(int i=1;i<=nbSteps;i++)
    {
      RewardsTrackStepState state=RewardsTracksUtils.getState(_status,i);
      boolean ok=_filter.accept(state);
      if (ok)
      {
        RewardsTrackStep step=steps.get(i-1);
        Item itemReward=step.getReward();
        if (itemReward!=null)
        {
          itemRewards.add(itemReward);
        }
      }
    }
    Rewards total=buildTotalRewards(itemRewards);
    _rewards.dispose();
    _rewards=new RewardsPanelController(_parent,total,100);
    // Assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    _panel.add(_rewards.getPanel(),c);
    // Push everything on top/left
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    _panel.add(Box.createGlue(),c);
    _panel.revalidate();
    _panel.repaint();
  }

  private Rewards buildTotalRewards(List<Item> itemRewards)
  {
    Collections.sort(itemRewards,new NamedComparator());
    Map<Integer,IntegerHolder> counters=new HashMap<Integer,IntegerHolder>();
    for(Item itemReward : itemRewards)
    {
      Integer key=Integer.valueOf(itemReward.getIdentifier());
      IntegerHolder counter=counters.get(key);
      if (counter==null)
      {
        counter=new IntegerHolder();
        counters.put(key,counter);
      }
      counter.increment();
    }
    int lastItemId=0;
    Rewards ret=new Rewards();
    for(Item item : itemRewards)
    {
      int itemId=item.getIdentifier();
      if (itemId!=lastItemId)
      {
        int quantity=counters.get(Integer.valueOf(itemId)).getInt();
        ItemReward itemReward=new ItemReward(item,quantity);
        ret.addRewardElement(itemReward);
        lastItemId=itemId;
      }
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _status=null;
    _filter=null;
    // Controllers
    _parent=null;
    if (_rewards!=null)
    {
      _rewards.dispose();
      _rewards=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
