package delta.games.lotro.gui.common.rewards.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.rewards.EmoteReward;
import delta.games.lotro.common.rewards.ItemReward;
import delta.games.lotro.common.rewards.RelicReward;
import delta.games.lotro.common.rewards.ReputationReward;
import delta.games.lotro.common.rewards.RewardElement;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.TitleReward;
import delta.games.lotro.common.rewards.VirtueReward;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.emotes.EmotesManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;
import delta.games.lotro.utils.Proxy;

/**
 * Controller for a panel to display rewards.
 * @author DAM
 */
public class RewardsPanelController
{
  private static final int NB_COLUMNS=2;
  // Data
  private Rewards _rewards;
  private JPanel _panel;
  private ClassPointRewardGadgetsController _classPoint;
  private LotroPointsRewardGadgetsController _lotroPoints;
  private DestinyPointsRewardGadgetsController _destinyPoints;
  private List<ItemRewardGadgetsController> _itemRewards;
  private List<TitleRewardGadgetsController> _titleRewards;
  private List<VirtueRewardGadgetsController> _virtueRewards;
  private List<ReputationRewardGadgetsController> _reputationRewards;
  private List<RelicRewardGadgetsController> _relicRewards;
  private List<EmoteRewardGadgetsController> _emoteRewards;
  private MoneyDisplayController _moneyController;

  // TODO
  // XP, Item XP, Mount XP, Glory
  // Selectable, Skill, Trait

  /**
   * Constructor.
   * @param rewards Rewards to display.
   */
  public RewardsPanelController(Rewards rewards)
  {
    _rewards=rewards;
    // Class Point
    int classPoints=rewards.getClassPoints();
    if (classPoints>0)
    {
      _classPoint=new ClassPointRewardGadgetsController(classPoints);
    }
    // LOTRO Points
    int lotroPoints=rewards.getLotroPoints();
    if (lotroPoints>0)
    {
      _lotroPoints=new LotroPointsRewardGadgetsController(lotroPoints);
    }
    // Destiny Points
    int destinyPoints=rewards.getDestinyPoints();
    if (destinyPoints>0)
    {
      _destinyPoints=new DestinyPointsRewardGadgetsController(destinyPoints);
    }
    // Reward elements
    _itemRewards=new ArrayList<ItemRewardGadgetsController>();
    _titleRewards=new ArrayList<TitleRewardGadgetsController>();
    _virtueRewards=new ArrayList<VirtueRewardGadgetsController>();
    _reputationRewards=new ArrayList<ReputationRewardGadgetsController>();
    _relicRewards=new ArrayList<RelicRewardGadgetsController>();
    _emoteRewards=new ArrayList<EmoteRewardGadgetsController>();

    for(RewardElement rewardElement : rewards.getRewardElements())
    {
      // Item reward
      if (rewardElement instanceof ItemReward)
      {
        ItemReward itemReward=(ItemReward)rewardElement;
        Proxy<Item> itemProxy=itemReward.getItemProxy();
        int id=itemProxy.getId();
        int count=itemReward.getQuantity();
        Item item=ItemsManager.getInstance().getItem(id);
        ItemRewardGadgetsController itemRewardUi=new ItemRewardGadgetsController(item,count);
        _itemRewards.add(itemRewardUi);
      }
      // Title reward
      else if (rewardElement instanceof TitleReward)
      {
        TitleReward titleReward=(TitleReward)rewardElement;
        TitleRewardGadgetsController titleRewardUi=new TitleRewardGadgetsController(titleReward);
        _titleRewards.add(titleRewardUi);
      }
      // Virtue reward
      else if (rewardElement instanceof VirtueReward)
      {
        VirtueReward virtueReward=(VirtueReward)rewardElement;
        VirtueRewardGadgetsController virtueRewardUi=new VirtueRewardGadgetsController(virtueReward);
        _virtueRewards.add(virtueRewardUi);
      }
      // Reputation
      else if (rewardElement instanceof ReputationReward)
      {
        ReputationReward reputationReward=(ReputationReward)rewardElement;
        ReputationRewardGadgetsController reputationRewardUi=new ReputationRewardGadgetsController(reputationReward);
        _reputationRewards.add(reputationRewardUi);
      }
      // Relic reward
      else if (rewardElement instanceof RelicReward)
      {
        RelicReward relicReward=(RelicReward)rewardElement;
        Proxy<Relic> relicProxy=relicReward.getRelicProxy();
        int id=relicProxy.getId();
        int count=relicReward.getQuantity();
        Relic relic=RelicsManager.getInstance().getById(id);
        RelicRewardGadgetsController relicRewardUi=new RelicRewardGadgetsController(relic,count);
        _relicRewards.add(relicRewardUi);
      }
      // Emote reward
      else if (rewardElement instanceof EmoteReward)
      {
        EmoteReward emoteReward=(EmoteReward)rewardElement;
        Proxy<EmoteDescription> emoteProxy=emoteReward.getEmoteProxy();
        int id=emoteProxy.getId();
        EmoteDescription emote=EmotesManager.getInstance().getEmote(id);
        EmoteRewardGadgetsController emoteRewardUi=new EmoteRewardGadgetsController(emote);
        _emoteRewards.add(emoteRewardUi);
      }
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
    int nbColumns=2;
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Class Points
    if (_classPoint!=null)
    {
      addRewardGadgets(ret,_classPoint.getLabelIcon(),_classPoint.getLabel(),c);
    }
    // LOTRO Points
    if (_lotroPoints!=null)
    {
      addRewardGadgets(ret,_lotroPoints.getLabelIcon(),_lotroPoints.getLabel(),c);
    }
    // Destiny Points
    if (_destinyPoints!=null)
    {
      addRewardGadgets(ret,_destinyPoints.getLabelIcon(),_destinyPoints.getLabel(),c);
    }
    // Items
    for(ItemRewardGadgetsController reward : _itemRewards)
    {
      addRewardGadgets(ret,reward.getLabelIcon(),reward.getLabel(),c);
    }
    // Titles
    for(TitleRewardGadgetsController titleReward : _titleRewards)
    {
      addRewardGadgets(ret,titleReward.getLabelIcon(),titleReward.getLabel(),c);
    }
    // Virtues
    for(VirtueRewardGadgetsController virtueReward : _virtueRewards)
    {
      addRewardGadgets(ret,virtueReward.getLabelIcon(),virtueReward.getLabel(),c);
    }
    // Reputation
    for(ReputationRewardGadgetsController reputationReward : _reputationRewards)
    {
      addRewardGadgets(ret,reputationReward.getLabelIcon(),reputationReward.getLabel(),c);
    }
    // Relics
    for(RelicRewardGadgetsController reward : _relicRewards)
    {
      addRewardGadgets(ret,reward.getLabelIcon(),reward.getLabel(),c);
    }
    // Emotes
    for(EmoteRewardGadgetsController emoteReward : _emoteRewards)
    {
      addRewardGadgets(ret,emoteReward.getLabelIcon(),emoteReward.getLabel(),c);
    }
    // Money
    Money money=_rewards.getMoney();
    if (!money.isEmpty())
    {
      _moneyController=new MoneyDisplayController();
      _moneyController.setMoney(money);
      c.gridx=0;c.gridy++;
      c.gridwidth=2;
      ret.add(_moneyController.getPanel(),c);
    }
    // Add space on right
    c=new GridBagConstraints(2*nbColumns,0,1,c.gridy,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    JPanel paddingPanel=GuiFactory.buildPanel(null);
    ret.add(paddingPanel,c);
    return ret;
  }

  private void addRewardGadgets(JPanel target, JLabel icon, JLabel label, GridBagConstraints c)
  {
    target.add(icon,c);
    c.gridx++;
    target.add(label,c);
    c.gridx++;
    if (c.gridx/2==NB_COLUMNS)
    {
      c.gridx=0;
      c.gridy++;
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _rewards=null;
    // Controllers
    _classPoint=null;
    _lotroPoints=null;
    _destinyPoints=null;
    _itemRewards=null;
    _titleRewards=null;
    _virtueRewards=null;
    _reputationRewards=null;
    _relicRewards=null;
    _emoteRewards=null;
    if (_moneyController!=null)
    {
      _moneyController.dispose();
      _moneyController=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
