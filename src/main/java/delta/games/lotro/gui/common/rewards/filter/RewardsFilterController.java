package delta.games.lotro.gui.common.rewards.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.common.enums.BillingGroup;
import delta.games.lotro.common.rewards.RewardsExplorer;
import delta.games.lotro.common.rewards.filters.BillingGroupRewardFilter;
import delta.games.lotro.common.rewards.filters.ClassPointRewardFilter;
import delta.games.lotro.common.rewards.filters.EmoteRewardFilter;
import delta.games.lotro.common.rewards.filters.GloryRewardFilter;
import delta.games.lotro.common.rewards.filters.ItemRewardFilter;
import delta.games.lotro.common.rewards.filters.ItemXpRewardFilter;
import delta.games.lotro.common.rewards.filters.LotroPointsRewardFilter;
import delta.games.lotro.common.rewards.filters.MountXpRewardFilter;
import delta.games.lotro.common.rewards.filters.RelicRewardFilter;
import delta.games.lotro.common.rewards.filters.ReputationRewardFilter;
import delta.games.lotro.common.rewards.filters.RewardsFilter;
import delta.games.lotro.common.rewards.filters.TitleRewardFilter;
import delta.games.lotro.common.rewards.filters.TraitRewardFilter;
import delta.games.lotro.common.rewards.filters.VirtueRewardFilter;
import delta.games.lotro.common.rewards.filters.VirtueXpRewardFilter;
import delta.games.lotro.common.rewards.filters.XpRewardFilter;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.common.rewards.RewardsUiUtils;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.titles.TitleDescription;

/**
 * Controller for a reward filter edition panel.
 * @author DAM
 */
public class RewardsFilterController extends AbstractPanelController
{
  // Data
  private RewardsFilter _filter;
  private FilterUpdateListener _filterUpdateListener;
  private RewardsUiUtils _uiUtils;

  // Quest or deed?
  private boolean _quest;
  // Controllers
  private ComboBoxController<Faction> _reputation;
  private ComboBoxController<Boolean> _lotroPoints;
  private ComboBoxController<Boolean> _classPoints;
  private ComboBoxController<Boolean> _xp;
  private ComboBoxController<Boolean> _glory;
  private ComboBoxController<Boolean> _itemXp;
  private ComboBoxController<Boolean> _mountXp;
  private ComboBoxController<Boolean> _virtueXp;
  private ComboBoxController<String> _trait;
  private ComboBoxController<TitleDescription> _title;
  private ComboBoxController<VirtueDescription> _virtue;
  private ComboBoxController<String> _emote;
  private ComboBoxController<Integer> _item;
  private ComboBoxController<Integer> _relic;
  private ComboBoxController<BillingGroup> _billingGroup;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   * @param explorer Rewards explorer.
   * @param quest Indicates if this is for the quests filter or for the deeds filter.
   */
  public RewardsFilterController(AreaController parent, RewardsFilter filter, FilterUpdateListener filterUpdateListener, RewardsExplorer explorer, boolean quest)
  {
    super(parent);
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
    _uiUtils=new RewardsUiUtils(explorer);
    _quest=quest;
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel buildPanel()
  {
    JPanel panel=buildRewardsPanel();
    setFilter();
    filterUpdated();
    return panel;
  }

  /**
   * Invoked when the managed filter has been updated.
   */
  private void filterUpdated()
  {
    _filterUpdateListener.filterUpdated();
  }

  /**
   * Reset all gadgets.
   */
  public void reset()
  {
    _reputation.selectItem(null);
    if (_lotroPoints!=null)
    {
      _lotroPoints.selectItem(null);
    }
    if (_classPoints!=null)
    {
      _classPoints.selectItem(null);
    }
    _xp.selectItem(null);
    if (_itemXp!=null)
    {
      _itemXp.selectItem(null);
    }
    if (_mountXp!=null)
    {
      _mountXp.selectItem(null);
    }
    if (_virtueXp!=null)
    {
      _virtueXp.selectItem(null);
    }
    if (_glory!=null)
    {
      _glory.selectItem(null);
    }
    _trait.selectItem(null);
    _title.selectItem(null);
    if (_virtue!=null)
    {
      _virtue.selectItem(null);
    }
    _emote.selectItem(null);
    _item.selectItem(null);
    if (_relic!=null)
    {
      _relic.selectItem(null);
    }
    if (_billingGroup!=null)
    {
      _billingGroup.selectItem(null);
    }
  }

  /**
   * Apply current filter into the managed gadgets.
   */
  public void setFilter()
  {
    // Reputation
    ReputationRewardFilter factionFilter=_filter.getReputationFilter();
    Faction faction=factionFilter.getFaction();
    _reputation.selectItem(faction);
    // LOTRO points
    if (_lotroPoints!=null)
    {
      LotroPointsRewardFilter lotroPointsFilter=_filter.getLotroPointsFilter();
      Boolean lotroPoints=lotroPointsFilter.getHasLotroPointsFlag();
      _lotroPoints.selectItem(lotroPoints);
    }
    // Class point
    if (_classPoints!=null)
    {
      ClassPointRewardFilter classPointFilter=_filter.getClassPointsFilter();
      Boolean classPoint=classPointFilter.getHasClassPointFlag();
      _classPoints.selectItem(classPoint);
    }
    // XP
    XpRewardFilter xpFilter=_filter.getXpFilter();
    Boolean xp=xpFilter.getHasXpFlag();
    _xp.selectItem(xp);
    // Item XP
    if (_itemXp!=null)
    {
      ItemXpRewardFilter itemXpFilter=_filter.getItemXpFilter();
      Boolean itemXp=itemXpFilter.getHasItemXpFlag();
      _itemXp.selectItem(itemXp);
    }
    // Mount XP
    if (_mountXp!=null)
    {
      MountXpRewardFilter mountXpFilter=_filter.getMountXpFilter();
      Boolean mountXp=mountXpFilter.getHasMountXpFlag();
      _mountXp.selectItem(mountXp);
    }
    // Virtue XP
    if (_virtueXp!=null)
    {
      VirtueXpRewardFilter virtueXpFilter=_filter.getVirtueXpFilter();
      Boolean virtueXp=virtueXpFilter.getHasVirtueXpFlag();
      _virtueXp.selectItem(virtueXp);
    }
    // Glory
    if (_glory!=null)
    {
      GloryRewardFilter gloryFilter=_filter.getGloryFilter();
      Boolean glory=gloryFilter.getHasGloryFlag();
      _glory.selectItem(glory);
    }
    // Trait
    TraitRewardFilter traitFilter=_filter.getTraitFilter();
    String trait=traitFilter.getTrait();
    _trait.selectItem(trait);
    // Title
    TitleRewardFilter titleFilter=_filter.getTitleFilter();
    TitleDescription title=titleFilter.getTitle();
    _title.selectItem(title);
    // Virtue
    if (_virtue!=null)
    {
      VirtueRewardFilter virtueFilter=_filter.getVirtueFilter();
      VirtueDescription virtue=virtueFilter.getVirtue();
      _virtue.selectItem(virtue);
    }
    // Emote
    EmoteRewardFilter emoteFilter=_filter.getEmoteFilter();
    String emote=emoteFilter.getEmote();
    _emote.selectItem(emote);
    // Item
    ItemRewardFilter itemFilter=_filter.getItemFilter();
    Integer itemId=itemFilter.getItemId();
    _item.selectItem(itemId);
    // Relic
    if (_relic!=null)
    {
      RelicRewardFilter relicFilter=_filter.getRelicFilter();
      Integer relicId=relicFilter.getRelicId();
      _relic.selectItem(relicId);
    }
    // Billing group
    if (_billingGroup!=null)
    {
      BillingGroupRewardFilter billingGroupFilter=_filter.getBillingGroupFilter();
      BillingGroup billingGroup=billingGroupFilter.getBillingGroup();
      _billingGroup.selectItem(billingGroup);
    }
  }

  private JPanel buildRewardsPanel()
  {
    boolean isLive=LotroCoreConfig.isLive();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;

    GridBagConstraints c;
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Reputation
      linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.reputation")));
      _reputation=buildReputationCombobox();
      linePanel.add(_reputation.getComboBox());
      // Title
      linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.title")));
      _title=buildTitlesCombobox();
      linePanel.add(_title.getComboBox());
      // Virtue
      if ((!isLive) && (!_quest))
      {
        linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.virtue")));
        _virtue=buildVirtuesCombobox();
        linePanel.add(_virtue.getComboBox());
      }
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    _billingGroup=buildBillingGroupsCombobox();
    if (_billingGroup!=null)
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Billing Group
      linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.accountWideToken")));
      linePanel.add(_billingGroup.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Traits
      linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.trait")));
      _trait=buildTraitsCombobox();
      linePanel.add(_trait.getComboBox());
      // Emotes
      linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.emote")));
      _emote=buildEmotesCombobox();
      linePanel.add(_emote.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0);
      // Items
      linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.item")));
      _item=buildItemsCombobox();
      linePanel.add(_item.getComboBox());
      // Relics
      _relic=buildRelicsCombobox();
      if (_relic!=null)
      {
        linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.relic")));
        linePanel.add(_relic.getComboBox());
      }
      panel.add(linePanel,c);
      y++;
    }

    {
      JPanel line=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // LOTRO points
      if (isLive)
      {
        line.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.lotroPoints")));
        _lotroPoints=buildLotroPointsCombobox();
        line.add(_lotroPoints.getComboBox());
        // Class point
        line.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.classPoints")));
        _classPoints=buildClassPointsCombobox();
        line.add(_classPoints.getComboBox());
      }
      // XP
      line.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.xp")));
      _xp=buildXpCombobox();
      line.add(_xp.getComboBox());
      // Item XP
      if ((_quest) && (isLive))
      {
        line.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.itemXP")));
        _itemXp=buildItemXpCombobox();
        line.add(_itemXp.getComboBox());
      }
      // Mount XP
      if (isLive)
      {
        line.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.mountXP")));
        _mountXp=buildMountXpCombobox();
        line.add(_mountXp.getComboBox());
      }
      // Virtue XP
      if (isLive)
      {
        line.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.virtueXP")));
        _virtueXp=buildVirtueXpCombobox();
        line.add(_virtueXp.getComboBox());
      }
      // Glory
      if ((_quest) && (isLive))
      {
        line.add(GuiFactory.buildLabel(Labels.getFieldLabel("rewards.filter.renownOrInfamy")));
        _glory=buildGloryCombobox();
        line.add(_glory.getComboBox());
      }

      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0);
      panel.add(line,c);
      y++;
    }
    return panel;
  }

  private ComboBoxController<Boolean> buildLotroPointsCombobox()
  {
    ComboBoxController<Boolean> combo=build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        LotroPointsRewardFilter filter=_filter.getLotroPointsFilter();
        filter.setHasLotroPointsFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildClassPointsCombobox()
  {
    ComboBoxController<Boolean> combo=build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        ClassPointRewardFilter filter=_filter.getClassPointsFilter();
        filter.setHasClassPointFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildXpCombobox()
  {
    ComboBoxController<Boolean> combo=build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        XpRewardFilter filter=_filter.getXpFilter();
        filter.setHasXpFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildItemXpCombobox()
  {
    ComboBoxController<Boolean> combo=build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        ItemXpRewardFilter filter=_filter.getItemXpFilter();
        filter.setHasItemXpFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildMountXpCombobox()
  {
    ComboBoxController<Boolean> combo=build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        MountXpRewardFilter filter=_filter.getMountXpFilter();
        filter.setHasMountXpFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildVirtueXpCombobox()
  {
    ComboBoxController<Boolean> combo=build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        VirtueXpRewardFilter filter=_filter.getVirtueXpFilter();
        filter.setHasVirtueXpFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildGloryCombobox()
  {
    ComboBoxController<Boolean> combo=build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        GloryRewardFilter filter=_filter.getGloryFilter();
        filter.setHasGloryFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  /**
   * Build a combo-box controller to choose from null, true or false.
   * @return A new combo-box controller.
   */
  private ComboBoxController<Boolean> build3StatesBooleanCombobox()
  {
    String with=Labels.getLabel("shared.with");
    String without=Labels.getLabel("shared.without");
    return SharedUiUtils.build3StatesBooleanCombobox("",with,without);
  }

  private ComboBoxController<Faction> buildReputationCombobox()
  {
    ComboBoxController<Faction> combo=SharedUiUtils.buildFactionCombo(this);
    ItemSelectionListener<Faction> listener=new ItemSelectionListener<Faction>()
    {
      @Override
      public void itemSelected(Faction faction)
      {
        ReputationRewardFilter filter=_filter.getReputationFilter();
        filter.setFaction(faction);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<String> buildTraitsCombobox()
  {
    ComboBoxController<String> combo=_uiUtils.buildTraitsCombo();
    ItemSelectionListener<String> listener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String trait)
      {
        TraitRewardFilter filter=_filter.getTraitFilter();
        filter.setTrait(trait);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<TitleDescription> buildTitlesCombobox()
  {
    ComboBoxController<TitleDescription> combo=_uiUtils.buildTitlesCombo(this);
    ItemSelectionListener<TitleDescription> listener=new ItemSelectionListener<TitleDescription>()
    {
      @Override
      public void itemSelected(TitleDescription title)
      {
        TitleRewardFilter filter=_filter.getTitleFilter();
        filter.setTitle(title);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<VirtueDescription> buildVirtuesCombobox()
  {
    ComboBoxController<VirtueDescription> combo=SharedUiUtils.buildVirtueCombo();
    ItemSelectionListener<VirtueDescription> listener=new ItemSelectionListener<VirtueDescription>()
    {
      @Override
      public void itemSelected(VirtueDescription virtue)
      {
        VirtueRewardFilter filter=_filter.getVirtueFilter();
        filter.setVirtue(virtue);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<String> buildEmotesCombobox()
  {
    ComboBoxController<String> combo=_uiUtils.buildEmotesCombo();
    ItemSelectionListener<String> listener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String emote)
      {
        EmoteRewardFilter filter=_filter.getEmoteFilter();
        filter.setEmote(emote);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Integer> buildItemsCombobox()
  {
    ComboBoxController<Integer> combo=_uiUtils.buildItemsCombo();
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer itemId)
      {
        ItemRewardFilter filter=_filter.getItemFilter();
        filter.setItemId(itemId);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Integer> buildRelicsCombobox()
  {
    ComboBoxController<Integer> combo=_uiUtils.buildRelicsCombo();
    if (combo==null)
    {
      return null;
    }
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer itemId)
      {
        RelicRewardFilter filter=_filter.getRelicFilter();
        filter.setRelicId(itemId);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<BillingGroup> buildBillingGroupsCombobox()
  {
    ComboBoxController<BillingGroup> combo=_uiUtils.buildBillingGroupsCombo();
    if (combo==null)
    {
      return null;
    }
    ItemSelectionListener<BillingGroup> listener=new ItemSelectionListener<BillingGroup>()
    {
      @Override
      public void itemSelected(BillingGroup billingGroup)
      {
        BillingGroupRewardFilter filter=_filter.getBillingGroupFilter();
        filter.setBillingGroup(billingGroup);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Data
    _filter=null;
    // Controllers
    if (_reputation!=null)
    {
      _reputation.dispose();
      _reputation=null;
    }
    if (_lotroPoints!=null)
    {
      _lotroPoints.dispose();
      _lotroPoints=null;
    }
    if (_classPoints!=null)
    {
      _classPoints.dispose();
      _classPoints=null;
    }
    if (_xp!=null)
    {
      _xp.dispose();
      _xp=null;
    }
    if (_itemXp!=null)
    {
      _itemXp.dispose();
      _itemXp=null;
    }
    if (_mountXp!=null)
    {
      _mountXp.dispose();
      _mountXp=null;
    }
    if (_virtueXp!=null)
    {
      _virtueXp.dispose();
      _virtueXp=null;
    }
    if (_glory!=null)
    {
      _glory.dispose();
      _glory=null;
    }
    if (_trait!=null)
    {
      _trait.dispose();
      _trait=null;
    }
    if (_title!=null)
    {
      _title.dispose();
      _title=null;
    }
    if (_virtue!=null)
    {
      _virtue.dispose();
      _virtue=null;
    }
    if (_emote!=null)
    {
      _emote.dispose();
      _emote=null;
    }
    if (_item!=null)
    {
      _item.dispose();
      _item=null;
    }
    if (_relic!=null)
    {
      _relic.dispose();
      _relic=null;
    }
  }
}
