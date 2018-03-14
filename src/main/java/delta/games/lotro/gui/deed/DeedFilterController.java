package delta.games.lotro.gui.deed;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.common.rewards.filters.ClassPointRewardFilter;
import delta.games.lotro.common.rewards.filters.EmoteRewardFilter;
import delta.games.lotro.common.rewards.filters.ItemRewardFilter;
import delta.games.lotro.common.rewards.filters.LotroPointsRewardFilter;
import delta.games.lotro.common.rewards.filters.ReputationRewardFilter;
import delta.games.lotro.common.rewards.filters.SkillRewardFilter;
import delta.games.lotro.common.rewards.filters.TitleRewardFilter;
import delta.games.lotro.common.rewards.filters.TraitRewardFilter;
import delta.games.lotro.common.rewards.filters.VirtueRewardFilter;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.filters.DeedCategoryFilter;
import delta.games.lotro.lore.deeds.filters.DeedNameFilter;
import delta.games.lotro.lore.deeds.filters.DeedTypeFilter;
import delta.games.lotro.lore.reputation.Faction;

/**
 * Controller for a deed filter edition panel.
 * @author DAM
 */
public class DeedFilterController
{
  // Data
  private DeedFilter _filter;
  // GUI
  private JPanel _panel;
  private JTextField _contains;
  private ComboBoxController<DeedType> _type;
  private ComboBoxController<String> _category;
  // -- Rewards UI --
  private ComboBoxController<Faction> _reputation;
  private ComboBoxController<Boolean> _lotroPoints;
  private ComboBoxController<Boolean> _classPoints;
  private ComboBoxController<String> _trait;
  private ComboBoxController<String> _skill;
  private ComboBoxController<String> _title;
  private ComboBoxController<VirtueId> _virtue;
  private ComboBoxController<String> _emote;
  private ComboBoxController<String> _item;
  // Controllers
  private DynamicTextEditionController _textController;
  private DeedExplorerPanelController _panelController;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param panelController Deed explorer panel.
   */
  public DeedFilterController(DeedFilter filter, DeedExplorerPanelController panelController)
  {
    _filter=filter;
    _panelController=panelController;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<DeedDescription> getFilter()
  {
    return _filter;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
      setFilter();
      filterUpdated();
    }
    return _panel;
  }

  /**
   * Invoked when the managed filter has been updated.
   */
  protected void filterUpdated()
  {
    _panelController.filterUpdated();
  }

  private void setFilter()
  {
    // Name
    DeedNameFilter nameFilter=_filter.getNameFilter();
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
    String category=categoryFilter.getDeedCategory();
    _category.selectItem(category);

    // Reputation
    ReputationRewardFilter factionFilter=_filter.getReputationFilter();
    Faction faction=factionFilter.getFaction();
    _reputation.selectItem(faction);
    // LOTRO points
    LotroPointsRewardFilter lotroPointsFilter=_filter.getLotroPointsFilter();
    Boolean lotroPoints=lotroPointsFilter.getHasLotroPointsFlag();
    _lotroPoints.selectItem(lotroPoints);
    // Class point
    ClassPointRewardFilter classPointFilter=_filter.getClassPointsFilter();
    Boolean classPoint=classPointFilter.getHasClassPointFlag();
    _classPoints.selectItem(classPoint);
    // Trait
    TraitRewardFilter traitFilter=_filter.getTraitFilter();
    String trait=traitFilter.getTrait();
    _trait.selectItem(trait);
    // Skill
    SkillRewardFilter skillFilter=_filter.getSkillFilter();
    String skill=skillFilter.getSkill();
    _skill.selectItem(skill);
    // Title
    TitleRewardFilter titleFilter=_filter.getTitleFilter();
    String title=titleFilter.getTitle();
    _title.selectItem(title);
    // Virtue
    VirtueRewardFilter virtueFilter=_filter.getVirtueFilter();
    VirtueId virtueId=virtueFilter.getVirtueId();
    _virtue.selectItem(virtueId);
    // Emote
    EmoteRewardFilter emoteFilter=_filter.getEmoteFilter();
    String emote=emoteFilter.getEmote();
    _emote.selectItem(emote);
    // Item
    ItemRewardFilter itemFilter=_filter.getItemFilter();
    String itemName=itemFilter.getItemName();
    _item.selectItem(itemName);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      containsPanel.add(GuiFactory.buildLabel("Label filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      TextListener listener=new TextListener()
      {
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          DeedNameFilter nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      line1Panel.add(containsPanel);
    }
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line1Panel,c);

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Type
    {
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
      line2Panel.add(_type.getComboBox());
    }
    // Category
    {
      _category=DeedUiUtils.buildCategoryCombo();
      ItemSelectionListener<String> categoryListener=new ItemSelectionListener<String>()
      {
        @Override
        public void itemSelected(String category)
        {
          DeedCategoryFilter categoryFilter=_filter.getCategoryFilter();
          categoryFilter.setDeedCategory(category);
          filterUpdated();
        }
      };
      _category.addListener(categoryListener);
      line2Panel.add(_category.getComboBox());
    }
    c=new GridBagConstraints(0,1,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line2Panel,c);

    JPanel line3Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Reputation
    _reputation=buildReputationCombobox();
    line3Panel.add(_reputation.getComboBox());
    // LOTRO points
    _lotroPoints=buildLotroPointsCombobox();
    line3Panel.add(_lotroPoints.getComboBox());
    // Class point
    _classPoints=buildClassPointsCombobox();
    line3Panel.add(_classPoints.getComboBox());
    // Title
    _title=buildTitlesCombobox();
    line3Panel.add(_title.getComboBox());
    // Virtue
    _virtue=buildVirtuesCombobox();
    line3Panel.add(_virtue.getComboBox());
    c=new GridBagConstraints(0,2,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line3Panel,c);

    JPanel line4Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Traits
    _trait=buildTraitsCombobox();
    line4Panel.add(_trait.getComboBox());
    // Skills
    _skill=buildSkillsCombobox();
    line4Panel.add(_skill.getComboBox());
    // Emotes
    _emote=buildEmotesCombobox();
    line4Panel.add(_emote.getComboBox());
    // Items
    _item=buildItemsCombobox();
    line4Panel.add(_item.getComboBox());
    c=new GridBagConstraints(0,3,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line4Panel,c);

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

  /**
   * Build a combo-box controller to choose from null, true or false.
   * @return A new combo-box controller.
   */
  private ComboBoxController<Boolean> build3StatesBooleanCombobox()
  {
    ComboBoxController<Boolean> ctrl=new ComboBoxController<Boolean>();
    ctrl.addEmptyItem("");
    ctrl.addItem(Boolean.TRUE,"With");
    ctrl.addItem(Boolean.FALSE,"Without");
    ctrl.selectItem(null);
    return ctrl;
  }

  private ComboBoxController<Faction> buildReputationCombobox()
  {
    ComboBoxController<Faction> combo=DeedUiUtils.buildFactionCombo();
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
    ComboBoxController<String> combo=DeedUiUtils.buildTraitsCombo();
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

  private ComboBoxController<String> buildSkillsCombobox()
  {
    ComboBoxController<String> combo=DeedUiUtils.buildSkillsCombo();
    ItemSelectionListener<String> listener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String skill)
      {
        SkillRewardFilter filter=_filter.getSkillFilter();
        filter.setSkill(skill);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<String> buildTitlesCombobox()
  {
    ComboBoxController<String> combo=DeedUiUtils.buildTitlesCombo();
    ItemSelectionListener<String> listener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String title)
      {
        TitleRewardFilter filter=_filter.getTitleFilter();
        filter.setTitle(title);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<VirtueId> buildVirtuesCombobox()
  {
    ComboBoxController<VirtueId> combo=DeedUiUtils.buildVirtueCombo();
    ItemSelectionListener<VirtueId> listener=new ItemSelectionListener<VirtueId>()
    {
      @Override
      public void itemSelected(VirtueId virtueId)
      {
        VirtueRewardFilter filter=_filter.getVirtueFilter();
        filter.setVirtueId(virtueId);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<String> buildEmotesCombobox()
  {
    ComboBoxController<String> combo=DeedUiUtils.buildEmotesCombo();
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

  private ComboBoxController<String> buildItemsCombobox()
  {
    ComboBoxController<String> combo=DeedUiUtils.buildItemsCombo();
    ItemSelectionListener<String> listener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String itemName)
      {
        ItemRewardFilter filter=_filter.getItemFilter();
        filter.setItemName(itemName);
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
    // Data
    _filter=null;
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
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
    if (_trait!=null)
    {
      _trait.dispose();
      _trait=null;
    }
    if (_skill!=null)
    {
      _skill.dispose();
      _skill=null;
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
    _contains=null;
  }
}
