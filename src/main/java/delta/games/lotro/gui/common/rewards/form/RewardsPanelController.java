package delta.games.lotro.gui.common.rewards.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.TraitsManager;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.rewards.BillingTokenReward;
import delta.games.lotro.common.rewards.CraftingXpReward;
import delta.games.lotro.common.rewards.EmoteReward;
import delta.games.lotro.common.rewards.ItemReward;
import delta.games.lotro.common.rewards.RelicReward;
import delta.games.lotro.common.rewards.ReputationReward;
import delta.games.lotro.common.rewards.RewardElement;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.SelectableRewardElement;
import delta.games.lotro.common.rewards.TitleReward;
import delta.games.lotro.common.rewards.TraitReward;
import delta.games.lotro.common.rewards.VirtueReward;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.emotes.EmotesManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;
import delta.games.lotro.utils.Proxy;

/**
 * Controller for a panel to display rewards.
 * @author DAM
 */
public class RewardsPanelController
{
  // Data
  private Rewards _rewards;
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private ClassPointRewardGadgetsController _classPoint;
  private LotroPointsRewardGadgetsController _lotroPoints;
  private List<RewardGadgetsController> _rewardControllers;
  private VirtueXpRewardGadgetsController _virtueXpController;
  private MoneyDisplayController _moneyController;
  private XpRewardsDisplayController _xpController;
  // Configuration
  private int _maxRows;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param rewards Rewards to display.
   */
  public RewardsPanelController(WindowController parent, Rewards rewards)
  {
    this(parent,rewards,8);
  }

  /**
   * Constructor.
   * @param parent Parent window.
   * @param rewards Rewards to display.
   * @param maxRows Maximum number of rows.
   */
  public RewardsPanelController(WindowController parent, Rewards rewards, int maxRows)
  {
    _parent=parent;
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
    _rewardControllers=new ArrayList<RewardGadgetsController>();
    _maxRows=maxRows;
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
    JPanel rewardsPanel=buildRewardsPanel();
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    ret.add(rewardsPanel,BorderLayout.WEST);
    return ret;
  }

  private JPanel buildRewardsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Class Points
    if (_classPoint!=null)
    {
      addRewardGadgets(ret,_classPoint.getIcon(),_classPoint.getLabel(),c);
      updateConstraints(c);
    }
    // LOTRO Points
    if (_lotroPoints!=null)
    {
      addRewardGadgets(ret,_lotroPoints.getIcon(),_lotroPoints.getLabel(),c);
      updateConstraints(c);
    }
    // Reward elements
    addRewards(ret,c,_rewards.getRewardElements());
    // Virtue XP
    int virtueXp=_rewards.getVirtueXp();
    if (virtueXp>0)
    {
      _virtueXpController=new VirtueXpRewardGadgetsController(virtueXp);
      addRewardGadgets(ret,_virtueXpController.getIcon(),_virtueXpController.getLabel(),c);
      updateConstraints(c);
    }

    // Money
    Money money=_rewards.getMoney();
    if (!money.isEmpty())
    {
      _moneyController=new MoneyDisplayController();
      _moneyController.setMoney(money);
      c.gridwidth=2;
      ret.add(_moneyController.getPanel(),c);
      c.gridy++;
    }
    // XP
    int xp=_rewards.getXp();
    int itemXp=_rewards.getItemXp();
    int mountXp=_rewards.getMountXp();
    if ((xp>0) || (itemXp>0) || (mountXp>0))
    {
      _xpController=new XpRewardsDisplayController();
      _xpController.setValues(xp,itemXp,mountXp);
      c.gridwidth=2;
      ret.add(_xpController.getPanel(),c);
      c.gridy++;
    }
    return ret;
  }

  private void addRewards(final JPanel target, GridBagConstraints c, List<RewardElement> rewardElements)
  {
    for(RewardElement rewardElement : rewardElements)
    {
      if (rewardElement instanceof SelectableRewardElement)
      {
        SelectableRewardElement selectable=(SelectableRewardElement)rewardElement;
        JPanel selectablesPanel=GuiFactory.buildPanel(new GridBagLayout());
        GridBagConstraints c2=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
        addRewards(selectablesPanel,c2,selectable.getElements());
        Border border=GuiFactory.buildTitledBorder("Select one of:");
        selectablesPanel.setBorder(border);
        int nbElements=selectable.getNbElements();
        GridBagConstraints cSubPanel=new GridBagConstraints(c.gridx,c.gridy,2,nbElements,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
        target.add(selectablesPanel,cSubPanel);
        c.gridy+=(nbElements-1);
      }
      else
      {
        RewardGadgetsController controller=getController(rewardElement);
        addRewardGadgets(target,controller.getIcon(),controller.getLabel(),c);
      }
      updateConstraints(c);
    }
  }

  private void updateConstraints(GridBagConstraints c)
  {
    c.gridy++;
    if (c.gridy>_maxRows)
    {
      c.gridy=0;
      c.gridx+=2;
    }
  }

  private void addRewardGadgets(JPanel target, Component icon, JLabel label, GridBagConstraints c)
  {
    int gridx=c.gridx;
    target.add(icon,c);
    c.gridx++;
    target.add(label,c);
    c.gridx++;
    c.gridx=gridx;
  }

  private RewardGadgetsController getController(RewardElement rewardElement)
  {
    RewardGadgetsController ret=null;
    // Item reward
    if (rewardElement instanceof ItemReward)
    {
      ItemReward itemReward=(ItemReward)rewardElement;
      Proxy<Item> itemProxy=itemReward.getItemProxy();
      int id=itemProxy.getId();
      int count=itemReward.getQuantity();
      Item item=ItemsManager.getInstance().getItem(id);
      ret=new ItemRewardGadgetsController(_parent,item,count);
    }
    // Title reward
    else if (rewardElement instanceof TitleReward)
    {
      TitleReward titleReward=(TitleReward)rewardElement;
      Proxy<TitleDescription> titleProxy=titleReward.getTitleProxy();
      int id=titleProxy.getId();
      TitleDescription title=TitlesManager.getInstance().getTitle(id);
      if (title!=null)
      {
        ret=new TitleRewardGadgetsController(_parent,title);
      }
    }
    // Virtue reward
    else if (rewardElement instanceof VirtueReward)
    {
      VirtueReward virtueReward=(VirtueReward)rewardElement;
      ret=new VirtueRewardGadgetsController(virtueReward);
    }
    // Reputation
    else if (rewardElement instanceof ReputationReward)
    {
      ReputationReward reputationReward=(ReputationReward)rewardElement;
      ret=new ReputationRewardGadgetsController(reputationReward);
    }
    // Relic reward
    else if (rewardElement instanceof RelicReward)
    {
      RelicReward relicReward=(RelicReward)rewardElement;
      Proxy<Relic> relicProxy=relicReward.getRelicProxy();
      int id=relicProxy.getId();
      int count=relicReward.getQuantity();
      Relic relic=RelicsManager.getInstance().getById(id);
      if (relic!=null)
      {
        ret=new RelicRewardGadgetsController(relic,count);
      }
    }
    // Emote reward
    else if (rewardElement instanceof EmoteReward)
    {
      EmoteReward emoteReward=(EmoteReward)rewardElement;
      Proxy<EmoteDescription> emoteProxy=emoteReward.getEmoteProxy();
      int id=emoteProxy.getId();
      EmoteDescription emote=EmotesManager.getInstance().getEmote(id);
      if (emote!=null)
      {
        ret=new EmoteRewardGadgetsController(_parent,emote);
      }
    }
    // Trait reward
    else if (rewardElement instanceof TraitReward)
    {
      TraitReward traitReward=(TraitReward)rewardElement;
      Proxy<TraitDescription> traitProxy=traitReward.getTraitProxy();
      int id=traitProxy.getId();
      TraitDescription trait=TraitsManager.getInstance().getTrait(id);
      if (trait!=null)
      {
        ret=new TraitRewardGadgetsController(_parent,trait);
      }
    }
    // Crafting XP reward
    else if (rewardElement instanceof CraftingXpReward)
    {
      CraftingXpReward craftingXpReward=(CraftingXpReward)rewardElement;
      ret=new CraftingXpRewardGadgetsController(craftingXpReward);
    }
    // Billing token
    else if (rewardElement instanceof BillingTokenReward)
    {
      BillingTokenReward billingTokenReward=(BillingTokenReward)rewardElement;
      ret=new BillingTokenRewardGadgetsController(_parent,billingTokenReward.getBillingGroup());
    }
    if (ret!=null)
    {
      _rewardControllers.add(ret);
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _rewards=null;
    // Controllers
    _parent=null;
    _classPoint=null;
    _lotroPoints=null;
    if (_rewardControllers!=null)
    {
      for(RewardGadgetsController controller : _rewardControllers)
      {
        controller.dispose();
      }
      _rewardControllers=null;
    }
    if (_virtueXpController!=null)
    {
      _virtueXpController.dispose();
      _virtueXpController=null;
    }
    if (_moneyController!=null)
    {
      _moneyController.dispose();
      _moneyController=null;
    }
    if (_xpController!=null)
    {
      _xpController.dispose();
      _xpController=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
