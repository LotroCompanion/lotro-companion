package delta.games.lotro.gui.lore.quests.filter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.LockType;
import delta.games.lotro.common.Repeatability;
import delta.games.lotro.common.Size;
import delta.games.lotro.common.enums.QuestCategory;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.common.rewards.RewardsExplorer;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.common.requirements.RequirementsFilterController;
import delta.games.lotro.gui.common.rewards.filter.RewardsFilterController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.lore.quests.QuestsUiUtils;
import delta.games.lotro.gui.lore.webStoreItems.WebStoreItemsFilterController;
import delta.games.lotro.gui.lore.worldEvents.WorldEventsFilterController;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.lore.quests.filter.AchievableMonsterPlayFilter;
import delta.games.lotro.lore.quests.filter.AutoBestowedQuestFilter;
import delta.games.lotro.lore.quests.filter.HiddenAchievableFilter;
import delta.games.lotro.lore.quests.filter.InstancedQuestFilter;
import delta.games.lotro.lore.quests.filter.LockTypeFilter;
import delta.games.lotro.lore.quests.filter.QuestArcFilter;
import delta.games.lotro.lore.quests.filter.QuestCategoryFilter;
import delta.games.lotro.lore.quests.filter.QuestFilter;
import delta.games.lotro.lore.quests.filter.QuestSizeFilter;
import delta.games.lotro.lore.quests.filter.RepeatabilityFilter;
import delta.games.lotro.lore.quests.filter.SessionPlayQuestFilter;
import delta.games.lotro.lore.quests.filter.ShareableQuestFilter;

/**
 * Controller for a quest filter edition panel.
 * @author DAM
 */
public class QuestFilterController extends AbstractPanelController implements ActionListener
{
  // Data
  private QuestFilter _filter;
  // GUI
  private JButton _reset;
  // -- Quest attributes UI --
  private JTextField _contains;
  private ComboBoxController<QuestCategory> _category;
  private ComboBoxController<String> _questArc; // Live only
  private ComboBoxController<Boolean> _instanced;
  private ComboBoxController<Boolean> _shareable; // Live only
  private ComboBoxController<Boolean> _sessionPlay;
  private ComboBoxController<Boolean> _autoBestowed; // Live only
  private ComboBoxController<Boolean> _hidden;
  private ComboBoxController<Repeatability> _repeatability;
  private ComboBoxController<LockType> _lockType; // Live only!
  private ComboBoxController<Size> _size;
  private ComboBoxController<Boolean> _monsterPlay;
  // -- Requirements UI --
  private RequirementsFilterController _requirements;
  // -- Rewards UI --
  private RewardsFilterController _rewards;
  // -- World Events UI --
  private WorldEventsFilterController _worldEvents;
  // -- Web Store Items UI --
  private WebStoreItemsFilterController<QuestDescription> _webStoreItems; // Live only
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   * @param useRequirements Use requirements or not.
   */
  public QuestFilterController(AreaController parent, QuestFilter filter, FilterUpdateListener filterUpdateListener, boolean useRequirements)
  {
    super(parent);
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
    // Requirements
    if (useRequirements)
    {
      _requirements=new RequirementsFilterController(filter.getRequirementsFilter(),filterUpdateListener);
    }
    // Rewards
    RewardsExplorer explorer=QuestsManager.getInstance().buildRewardsExplorer();
    _rewards=new RewardsFilterController(this,filter.getRewardsFilter(),filterUpdateListener,explorer,true);
    // World events
    List<QuestDescription> quests=QuestsManager.getInstance().getAll();
    _worldEvents=new WorldEventsFilterController(quests,filter.getWorldEventsFilter(),filterUpdateListener);
    // Web Store items
    boolean isLive=LotroCoreConfig.isLive();
    if (isLive)
    {
      _webStoreItems=new WebStoreItemsFilterController<QuestDescription>(quests,filter.getWebStoreItemsFilter(),filterUpdateListener);
    }
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<QuestDescription> getFilter()
  {
    return _filter;
  }

  private JPanel buildPanel()
  {
    JPanel panel=build();
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

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _category.selectItem(null);
      if (_questArc!=null)
      {
        _questArc.selectItem(null);
      }
      _size.selectItem(null);
      _monsterPlay.selectItem(null);
      _instanced.selectItem(null);
      if (_shareable!=null)
      {
        _shareable.selectItem(null);
      }
      _sessionPlay.selectItem(null);
      if (_autoBestowed!=null)
      {
        _autoBestowed.selectItem(null);
      }
      _hidden.selectItem(null);
      _repeatability.selectItem(null);
      if (_lockType!=null)
      {
        _lockType.selectItem(null);
      }
      if (_requirements!=null)
      {
        _requirements.reset();
      }
      _rewards.reset();
      _worldEvents.reset();
      if (_webStoreItems!=null)
      {
        _webStoreItems.reset();
      }
      _contains.setText("");
    }
  }

  private void setFilter()
  {
    // Name
    NamedFilter<QuestDescription> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Category
    QuestCategoryFilter categoryFilter=_filter.getCategoryFilter();
    QuestCategory category=categoryFilter.getQuestCategory();
    _category.selectItem(category);
    // Quest arc
    if (_questArc!=null)
    {
      QuestArcFilter questArcFilter=_filter.getQuestArcFilter();
      String questArc=questArcFilter.getQuestArc();
      _questArc.selectItem(questArc);
    }
    // Size
    QuestSizeFilter sizeFilter=_filter.getQuestSizeFilter();
    Size size=sizeFilter.getQuestSize();
    _size.selectItem(size);
    // Monster play
    AchievableMonsterPlayFilter<QuestDescription> monsterPlayFilter=_filter.getMonsterPlayFilter();
    Boolean monsterPlayFlag=monsterPlayFilter.getIsMonsterPlayFlag();
    _monsterPlay.selectItem(monsterPlayFlag);
    // Instanced
    InstancedQuestFilter instancedFilter=_filter.getInstancedQuestFilter();
    Boolean instancedFlag=instancedFilter.getIsInstancedFlag();
    _instanced.selectItem(instancedFlag);
    // Shareable
    if (_shareable!=null)
    {
      ShareableQuestFilter shareableFilter=_filter.getShareableQuestFilter();
      Boolean shareableFlag=shareableFilter.getIsShareableFlag();
      _shareable.selectItem(shareableFlag);
    }
    // Session-play
    SessionPlayQuestFilter sessionPlayFilter=_filter.getSessionPlayQuestFilter();
    Boolean sessionPlayFlag=sessionPlayFilter.getIsSessionPlayFlag();
    _sessionPlay.selectItem(sessionPlayFlag);
    // Auto-bestowed
    if (_autoBestowed!=null)
    {
      AutoBestowedQuestFilter autoBestowedFilter=_filter.getAutoBestowedQuestFilter();
      Boolean autoBestowedFlag=autoBestowedFilter.getIsAutoBestowedFlag();
      _autoBestowed.selectItem(autoBestowedFlag);
    }
    // Hidden
    HiddenAchievableFilter<QuestDescription> hiddenFilter=_filter.getHiddenFilter();
    Boolean hiddenFlag=hiddenFilter.getIsHiddenFlag();
    _hidden.selectItem(hiddenFlag);
    // Repeatability
    RepeatabilityFilter repeatabilityFilter=_filter.getRepeatabilityFilter();
    Repeatability repeatability=repeatabilityFilter.getRepeatability();
    _repeatability.selectItem(repeatability);
    // Lock type
    if (_lockType!=null)
    {
      LockTypeFilter lockTypeFilter=_filter.getLockTypeFilter();
      LockType lockType=lockTypeFilter.getLockType();
      _lockType.selectItem(lockType);
    }
    // Requirements
    if (_requirements!=null)
    {
      _requirements.setFilter();
    }
    // Rewards
    _rewards.setFilter();
    // World Events
    _worldEvents.setFilter();
    // Web store items
    if (_webStoreItems!=null)
    {
      _webStoreItems.setFilter();
    }
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Line 1
    JPanel line1Panel=GuiFactory.buildPanel(new GridBagLayout());
    // Quest attributes
    JPanel questPanel=buildQuestPanel();
    Border questBorder=GuiFactory.buildTitledBorder(Labels.getLabel("quests.filter.quest.border"));
    questPanel.setBorder(questBorder);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    line1Panel.add(questPanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,0,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,0),0,0);
    line1Panel.add(_reset,c);
    c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(line1Panel,c);
    y++;

    // Line 2
    JPanel line2Panel=GuiFactory.buildPanel(new GridBagLayout());
    // Requirements
    if (_requirements!=null)
    {
      JPanel requirementsPanel=_requirements.getPanel();
      Border requirementsBorder=GuiFactory.buildTitledBorder(Labels.getLabel("quests.filter.requirements.border"));
      requirementsPanel.setBorder(requirementsBorder);
      c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      line2Panel.add(requirementsPanel,c);
    }
    // World Events
    {
      JPanel contextsPanel=_worldEvents.getPanel();
      Border border=GuiFactory.buildTitledBorder(Labels.getLabel("quests.filter.context.border"));
      contextsPanel.setBorder(border);
      c=new GridBagConstraints(1,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      line2Panel.add(contextsPanel,c);
    }
    // Web Store Items
    if (_webStoreItems!=null)
    {
      JPanel webStoreItemsPanel=_webStoreItems.getPanel();
      Border border=GuiFactory.buildTitledBorder(Labels.getLabel("quests.filter.contentsPack.border"));
      webStoreItemsPanel.setBorder(border);
      c=new GridBagConstraints(2,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      line2Panel.add(webStoreItemsPanel,c);
    }
    c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(line2Panel,c);
    y++;

    // Rewards
    JPanel rewardsPanel=_rewards.getPanel();
    Border rewardsBorder=GuiFactory.buildTitledBorder(Labels.getLabel("quests.filter.rewards.border"));
    rewardsPanel.setBorder(rewardsBorder);
    c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(rewardsPanel,c);
    // Push everything on left
    c=new GridBagConstraints(1,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(GuiFactory.buildPanel(new BorderLayout()),c);

    return panel;
  }

  private JPanel buildQuestPanel()
  {
    boolean isLive=LotroCoreConfig.isLive();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,0,0));
    // Label filter
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      containsPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.name")));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          NamedFilter<QuestDescription> nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      line1Panel.add(containsPanel);
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Category
    {
      JLabel label=GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.category"));
      line2Panel.add(label);
      _category=QuestsUiUtils.buildCategoryCombo();
      ItemSelectionListener<QuestCategory> categoryListener=new ItemSelectionListener<QuestCategory>()
      {
        @Override
        public void itemSelected(QuestCategory category)
        {
          QuestCategoryFilter categoryFilter=_filter.getCategoryFilter();
          categoryFilter.setQuestCategory(category);
          filterUpdated();
        }
      };
      _category.addListener(categoryListener);
      line2Panel.add(_category.getComboBox());
    }
    // Quest arc
    if (isLive)
    {
      JLabel label=GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.questArc"));
      line2Panel.add(label);
      _questArc=buildQuestArcCombobox();
      line2Panel.add(_questArc.getComboBox());
    }
    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line2Panel,c);
    y++;

    // Flags line
    {
      JPanel line=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Instanced
      line.add(GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.instanced")));
      _instanced=buildInstancedCombobox();
      line.add(_instanced.getComboBox());
      // Shareable
      if (isLive)
      {
        line.add(GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.shareable")));
        _shareable=buildShareableCombobox();
        line.add(_shareable.getComboBox());
      }
      // Session-play
      line.add(GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.sessionPlay")));
      _sessionPlay=buildSessionPlayCombobox();
      line.add(_sessionPlay.getComboBox());
      // Auto-bestowed
      if (isLive)
      {
        line.add(GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.autoBestowed")));
        _autoBestowed=buildAutoBestowedCombobox();
        line.add(_autoBestowed.getComboBox());
      }
      // Hidden
      line.add(GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.hidden")));
      _hidden=buildHiddenCombobox();
      line.add(_hidden.getComboBox());

      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0);
      panel.add(line,c);
      y++;
    }
    // 4th line
    {
      JPanel line=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Repeatability
      line.add(GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.repeatability")));
      _repeatability=buildRepeatabilityCombobox();
      line.add(_repeatability.getComboBox());
      // Lock type
      if (isLive)
      {
        line.add(GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.lockType")));
        _lockType=buildLockTypeCombobox();
        line.add(_lockType.getComboBox());
      }
      // Size
      line.add(GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.size")));
      _size=buildSizeCombobox();
      line.add(_size.getComboBox());
      // Faction
      line.add(GuiFactory.buildLabel(Labels.getFieldLabel("quests.filter.faction")));
      _monsterPlay=buildMonsterPlayCombobox();
      line.add(_monsterPlay.getComboBox());

      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0);
      panel.add(line,c);
      y++;
    }
    return panel;
  }

  private ComboBoxController<String> buildQuestArcCombobox()
  {
    ComboBoxController<String> combo=QuestsUiUtils.buildQuestArcCombo();
    ItemSelectionListener<String> questArcListener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String questArc)
      {
        QuestArcFilter questArcFilter=_filter.getQuestArcFilter();
        questArcFilter.setQuestArc(questArc);
        filterUpdated();
      }
    };
    combo.addListener(questArcListener);
    return combo;
  }

  private ComboBoxController<Size> buildSizeCombobox()
  {
    ComboBoxController<Size> combo=QuestsUiUtils.buildQuestSizeCombo();
    ItemSelectionListener<Size> questSizeListener=new ItemSelectionListener<Size>()
    {
      @Override
      public void itemSelected(Size size)
      {
        QuestSizeFilter questSizeFilter=_filter.getQuestSizeFilter();
        questSizeFilter.setQuestSize(size);
        filterUpdated();
      }
    };
    combo.addListener(questSizeListener);
    return combo;
  }

  private ComboBoxController<Boolean> buildMonsterPlayCombobox()
  {
    String monsterPlayLabel=Labels.getLabel("quests.filter.monsterPlay");
    String freePeoplesLabel=Labels.getLabel("quests.filter.freePeoples");
    ComboBoxController<Boolean> combo=SharedUiUtils.build3StatesBooleanCombobox("",monsterPlayLabel,freePeoplesLabel);
    ItemSelectionListener<Boolean> questSizeListener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean monsterPlayFlag)
      {
        AchievableMonsterPlayFilter<QuestDescription> monsterPlayFilter=_filter.getMonsterPlayFilter();
        monsterPlayFilter.setIsMonsterPlayFlag(monsterPlayFlag);
        filterUpdated();
      }
    };
    combo.addListener(questSizeListener);
    return combo;
  }

  private ComboBoxController<Repeatability> buildRepeatabilityCombobox()
  {
    ComboBoxController<Repeatability> combo=QuestsUiUtils.buildRepeatabilityCombo();
    ItemSelectionListener<Repeatability> listener=new ItemSelectionListener<Repeatability>()
    {
      @Override
      public void itemSelected(Repeatability repeatability)
      {
        RepeatabilityFilter repeatabilityFilter=_filter.getRepeatabilityFilter();
        repeatabilityFilter.setRepeatability(repeatability);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<LockType> buildLockTypeCombobox()
  {
    ComboBoxController<LockType> combo=QuestsUiUtils.buildLockTypeCombo();
    ItemSelectionListener<LockType> listener=new ItemSelectionListener<LockType>()
    {
      @Override
      public void itemSelected(LockType lockType)
      {
        LockTypeFilter lockTypeFilter=_filter.getLockTypeFilter();
        lockTypeFilter.setLockType(lockType);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildInstancedCombobox()
  {
    ComboBoxController<Boolean> combo=QuestsUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        InstancedQuestFilter filter=_filter.getInstancedQuestFilter();
        filter.setIsInstancedFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildShareableCombobox()
  {
    ComboBoxController<Boolean> combo=QuestsUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        ShareableQuestFilter filter=_filter.getShareableQuestFilter();
        filter.setIsShareableFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildSessionPlayCombobox()
  {
    ComboBoxController<Boolean> combo=QuestsUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        SessionPlayQuestFilter filter=_filter.getSessionPlayQuestFilter();
        filter.setIsSessionPlayFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildAutoBestowedCombobox()
  {
    ComboBoxController<Boolean> combo=QuestsUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        AutoBestowedQuestFilter filter=_filter.getAutoBestowedQuestFilter();
        filter.setIsAutoBestowedFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildHiddenCombobox()
  {
    ComboBoxController<Boolean> combo=QuestsUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        HiddenAchievableFilter<QuestDescription> filter=_filter.getHiddenFilter();
        filter.setIsHiddenFlag(value);
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
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    if (_questArc!=null)
    {
      _questArc.dispose();
      _questArc=null;
    }
    if (_size!=null)
    {
      _size.dispose();
      _size=null;
    }
    if (_monsterPlay!=null)
    {
      _monsterPlay.dispose();
      _monsterPlay=null;
    }
    if (_instanced!=null)
    {
      _instanced.dispose();
      _instanced=null;
    }
    if (_shareable!=null)
    {
      _shareable.dispose();
      _shareable=null;
    }
    if (_sessionPlay!=null)
    {
      _sessionPlay.dispose();
      _sessionPlay=null;
    }
    if (_autoBestowed!=null)
    {
      _autoBestowed.dispose();
      _autoBestowed=null;
    }
    if (_hidden!=null)
    {
      _hidden.dispose();
      _hidden=null;
    }
    if (_repeatability!=null)
    {
      _repeatability.dispose();
      _repeatability=null;
    }
    if (_lockType!=null)
    {
      _lockType.dispose();
      _lockType=null;
    }
    if (_requirements!=null)
    {
      _requirements.dispose();
      _requirements=null;
    }
    if (_worldEvents!=null)
    {
      _worldEvents.dispose();
      _worldEvents=null;
    }
    if (_webStoreItems!=null)
    {
      _webStoreItems.dispose();
      _webStoreItems=null;
    }
    if (_rewards!=null)
    {
      _rewards.dispose();
      _rewards=null;
    }
    _contains=null;
    _reset=null;
  }
}
