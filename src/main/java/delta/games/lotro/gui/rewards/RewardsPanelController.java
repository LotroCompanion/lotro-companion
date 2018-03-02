package delta.games.lotro.gui.rewards;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.common.Reputation;
import delta.games.lotro.common.ReputationItem;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.Virtue;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Controller for a panel to display rewards.
 * @author DAM
 */
public class RewardsPanelController
{
  private JPanel _panel;
  private LotroPointsRewardGadgetsController _lotroPoints;
  private List<ItemRewardGadgetsController> _itemRewards;
  private List<VirtueRewardGadgetsController> _virtueRewards;
  private List<ReputationRewardGadgetsController> _reputationRewards;

  /**
   * Constructor.
   * @param rewards Rewards to display.
   */
  public RewardsPanelController(Rewards rewards)
  {
    // LOTRO Points
    int lotroPoints=rewards.getLotroPoints();
    if (lotroPoints>0)
    {
      _lotroPoints=new LotroPointsRewardGadgetsController(lotroPoints);
    }
    // Item rewards
    ObjectsSet objects=rewards.getObjects();
    _itemRewards=new ArrayList<ItemRewardGadgetsController>();
    int nbItems=objects.getNbObjectItems();
    for(int i=0;i<nbItems;i++)
    {
      ObjectItem object=objects.getItem(i);
      int id=object.getItemId();
      int count=objects.getQuantity(i);
      Item item=ItemsManager.getInstance().getItem(id);
      ItemRewardGadgetsController itemReward=new ItemRewardGadgetsController(item,count);
      _itemRewards.add(itemReward);
    }
    // TODO Title
    // Virtue(s) reward(s)
    _virtueRewards=new ArrayList<VirtueRewardGadgetsController>();
    Virtue[] virtues=rewards.getVirtues();
    if (virtues!=null)
    {
      for(Virtue virtue : virtues)
      {
        VirtueRewardGadgetsController virtueReward=new VirtueRewardGadgetsController(virtue);
        _virtueRewards.add(virtueReward);
      }
    }
    // Reputation
    _reputationRewards=new ArrayList<ReputationRewardGadgetsController>();
    Reputation reputation=rewards.getReputation();
    ReputationItem[] reputationItems=reputation.getItems();
    for(ReputationItem reputationItem : reputationItems)
    {
      ReputationRewardGadgetsController reputationReward=new ReputationRewardGadgetsController(reputationItem);
      _reputationRewards.add(reputationReward);
    }
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // LOTRO Points
    if (_lotroPoints!=null)
    {
      c.gridx=0;
      ret.add(_lotroPoints.getLabelIcon(),c);
      c.gridx++;
      ret.add(_lotroPoints.getLabel(),c);
      c.gridy++;
    }
    // Items
    for(ItemRewardGadgetsController reward : _itemRewards)
    {
      c.gridx=0;
      ret.add(reward.getLabelIcon(),c);
      c.gridx++;
      ret.add(reward.getLabel(),c);
      c.gridy++;
    }
    // Virtues
    for(VirtueRewardGadgetsController virtueReward : _virtueRewards)
    {
      c.gridx=0;
      ret.add(virtueReward.getLabelIcon(),c);
      c.gridx++;
      ret.add(virtueReward.getLabel(),c);
      c.gridy++;
    }
    // Reputation
    for(ReputationRewardGadgetsController reputationReward : _reputationRewards)
    {
      c.gridx=0;
      ret.add(reputationReward.getLabelIcon(),c);
      c.gridx++;
      ret.add(reputationReward.getLabel(),c);
      c.gridy++;
    }
    return ret;
  }


  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    _lotroPoints=null;
    _itemRewards=null;
    _virtueRewards=null;
    _reputationRewards=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
