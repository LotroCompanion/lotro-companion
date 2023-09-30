package delta.games.lotro.gui.lore.deeds.filter;

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
import delta.games.lotro.common.enums.DeedCategory;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.common.rewards.RewardsExplorer;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.common.requirements.RequirementsFilterController;
import delta.games.lotro.gui.common.rewards.filter.RewardsFilterController;
import delta.games.lotro.gui.lore.deeds.DeedUiUtils;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.lore.quests.QuestsUiUtils;
import delta.games.lotro.gui.lore.webStoreItems.WebStoreItemsFilterController;
import delta.games.lotro.gui.lore.worldEvents.WorldEventsFilterController;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.deeds.filters.DeedCategoryFilter;
import delta.games.lotro.lore.deeds.filters.DeedFilter;
import delta.games.lotro.lore.deeds.filters.DeedTypeFilter;
import delta.games.lotro.lore.quests.filter.AchievableMonsterPlayFilter;
import delta.games.lotro.lore.quests.filter.HiddenAchievableFilter;

/**
 * Controller for a deed filter edition panel.
 * @author DAM
 */
public class DeedFilterController extends AbstractPanelController implements ActionListener
{
  // Data
  private DeedFilter _filter;
  // GUI
  private JButton _reset;
  // -- Deed attributes UI --
  private JTextField _contains;
  private ComboBoxController<DeedType> _type;
  private ComboBoxController<DeedCategory> _category;
  private ComboBoxController<Boolean> _monsterPlay;
  private ComboBoxController<Boolean> _hidden;
  // -- Requirements UI --
  private RequirementsFilterController _requirements;
  // -- Rewards UI --
  private RewardsFilterController _rewards;
  // -- World Events UI --
  private WorldEventsFilterController _worldEvents; // Live only
  // -- Web Store Items UI --
  private WebStoreItemsFilterController<DeedDescription> _webStoreItems; // Live only
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
  public DeedFilterController(AreaController parent, DeedFilter filter, FilterUpdateListener filterUpdateListener, boolean useRequirements)
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
    RewardsExplorer explorer=DeedsManager.getInstance().buildRewardsExplorer();
    _rewards=new RewardsFilterController(parent,filter.getRewardsFilter(),filterUpdateListener,explorer,false);
    boolean isLive=LotroCoreConfig.isLive();
    if (isLive)
    {
      // World events
      List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
      _worldEvents=new WorldEventsFilterController(deeds,filter.getWorldEventsFilter(),filterUpdateListener);
      // Web Store items
      _webStoreItems=new WebStoreItemsFilterController<DeedDescription>(deeds,filter.getWebStoreItemsFilter(),filterUpdateListener);
    }
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<DeedDescription> getFilter()
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
      _type.selectItem(null);
      _category.selectItem(null);
      if (_monsterPlay!=null)
      {
        _monsterPlay.selectItem(null);
      }
      if (_hidden!=null)
      {
        _hidden.selectItem(null);
      }
      if (_requirements!=null)
      {
        _requirements.reset();
      }
      _rewards.reset();
      if (_worldEvents!=null)
      {
        _worldEvents.reset();
      }
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
    NamedFilter<DeedDescription> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Type
    DeedTypeFilter typeFilter=_filter.getTypeFilter();
    DeedType type=typeFilter.getDeedType();
    _type.selectItem(type);
    // Category
    DeedCategoryFilter categoryFilter=_filter.getCategoryFilter();
    DeedCategory category=categoryFilter.getDeedCategory();
    _category.selectItem(category);
    // Monster play
    if (_monsterPlay!=null)
    {
      AchievableMonsterPlayFilter<DeedDescription> monsterPlayFilter=_filter.getMonsterPlayFilter();
      Boolean monsterPlayFlag=monsterPlayFilter.getIsMonsterPlayFlag();
      _monsterPlay.selectItem(monsterPlayFlag);
    }
    // Hidden
    if (_hidden!=null)
    {
      HiddenAchievableFilter<DeedDescription> hiddenFilter=_filter.getHiddenFilter();
      Boolean hiddenFlag=hiddenFilter.getIsHiddenFlag();
      _hidden.selectItem(hiddenFlag);
    }
    // Requirements
    if (_requirements!=null)
    {
      _requirements.setFilter();
    }
    // Rewards
    _rewards.setFilter();
    // World Events
    if (_worldEvents!=null)
    {
      _worldEvents.setFilter();
    }
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
    // Deed attributes
    JPanel deedPanel=buildDeedPanel();
    Border deedBorder=GuiFactory.buildTitledBorder("Deed"); // 18n
    deedPanel.setBorder(deedBorder);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    line1Panel.add(deedPanel,c);

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
      Border requirementsBorder=GuiFactory.buildTitledBorder("Requirements"); // I18n
      requirementsPanel.setBorder(requirementsBorder);
      c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      line2Panel.add(requirementsPanel,c);
    }
    // World Events
    if (_worldEvents!=null)
    {
      JPanel contextsPanel=_worldEvents.getPanel();
      Border border=GuiFactory.buildTitledBorder("Context"); // I18n
      contextsPanel.setBorder(border);
      c=new GridBagConstraints(1,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      line2Panel.add(contextsPanel,c);
    }
    // Web Store Items
    if (_webStoreItems!=null)
    {
      JPanel webStoreItemsPanel=_webStoreItems.getPanel();
      Border border=GuiFactory.buildTitledBorder("Contents Pack"); // I18n
      webStoreItemsPanel.setBorder(border);
      c=new GridBagConstraints(2,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      line2Panel.add(webStoreItemsPanel,c);
    }
    c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(line2Panel,c);
    y++;

    // Rewards
    JPanel rewardsPanel=_rewards.getPanel();
    Border rewardsBorder=GuiFactory.buildTitledBorder("Rewards"); // I18n
    rewardsPanel.setBorder(rewardsBorder);
    c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(rewardsPanel,c);
    // Push everything on left
    c=new GridBagConstraints(1,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(GuiFactory.buildPanel(new BorderLayout()),c);

    return panel;
  }

  private JPanel buildDeedPanel()
  {
    boolean isLive=LotroCoreConfig.isLive();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      linePanel.add(GuiFactory.buildLabel("Name filter:")); // 18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      linePanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          NamedFilter<DeedDescription> nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    // Type
    {
      JLabel label=GuiFactory.buildLabel("Type:"); // 18n
      linePanel.add(label);
      _type=DeedUiUtils.buildDeedTypeCombo();
      ItemSelectionListener<DeedType> typeListener=new ItemSelectionListener<DeedType>()
      {
        @Override
        public void itemSelected(DeedType type)
        {
          DeedTypeFilter typeFilter=_filter.getTypeFilter();
          typeFilter.setDeedType(type);
          filterUpdated();
        }
      };
      _type.addListener(typeListener);
      linePanel.add(_type.getComboBox());
    }
    // Category
    {
      JLabel label=GuiFactory.buildLabel("Category:"); // 18n
      linePanel.add(label);
      _category=DeedUiUtils.buildCategoryCombo();
      ItemSelectionListener<DeedCategory> categoryListener=new ItemSelectionListener<DeedCategory>()
      {
        @Override
        public void itemSelected(DeedCategory category)
        {
          DeedCategoryFilter categoryFilter=_filter.getCategoryFilter();
          categoryFilter.setDeedCategory(category);
          filterUpdated();
        }
      };
      _category.addListener(categoryListener);
      linePanel.add(_category.getComboBox());
    }
    // Faction
    if (isLive)
    {
      linePanel.add(GuiFactory.buildLabel("Faction:")); // 18n
      _monsterPlay=buildMonsterPlayCombobox();
      linePanel.add(_monsterPlay.getComboBox());
    }
    // Hidden
    if (isLive)
    {
      linePanel.add(GuiFactory.buildLabel("Hidden:")); // 18n
      _hidden=buildHiddenCombobox();
      linePanel.add(_hidden.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(linePanel,c);
    y++;
    return panel;
  }

  private ComboBoxController<Boolean> buildMonsterPlayCombobox()
  {
    ComboBoxController<Boolean> combo=SharedUiUtils.build3StatesBooleanCombobox("","Monster Play","Free Peoples"); // 18n
    ItemSelectionListener<Boolean> questSizeListener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean monsterPlayFlag)
      {
        AchievableMonsterPlayFilter<DeedDescription> monsterPlayFilter=_filter.getMonsterPlayFilter();
        monsterPlayFilter.setIsMonsterPlayFlag(monsterPlayFlag);
        filterUpdated();
      }
    };
    combo.addListener(questSizeListener);
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
        HiddenAchievableFilter<DeedDescription> filter=_filter.getHiddenFilter();
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
    if (_type!=null)
    {
      _type.dispose();
      _type=null;
    }
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    if (_monsterPlay!=null)
    {
      _monsterPlay.dispose();
      _monsterPlay=null;
    }
    if (_hidden!=null)
    {
      _hidden.dispose();
      _hidden=null;
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
