package delta.games.lotro.gui.deed;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
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
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.filters.DeedCategoryFilter;
import delta.games.lotro.lore.deeds.filters.DeedClassRequirementFilter;
import delta.games.lotro.lore.deeds.filters.DeedNameFilter;
import delta.games.lotro.lore.deeds.filters.DeedRaceRequirementFilter;
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
  // -- Requirements UI --
  private ComboBoxController<CharacterClass> _class;
  private ComboBoxController<Race> _race;
  // -- Rewards UI --
  private ComboBoxController<Faction> _reputation;
  private ComboBoxController<Boolean> _lotroPoints;
  private ComboBoxController<Boolean> _classPoints;
  private ComboBoxController<String> _trait;
  private ComboBoxController<String> _skill;
  private ComboBoxController<String> _title;
  private ComboBoxController<VirtueId> _virtue;
  private ComboBoxController<String> _emote;
  private ComboBoxController<Integer> _item;
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

    // Requirements:
    // - class
    DeedClassRequirementFilter classFilter=_filter.getClassFilter();
    CharacterClass requiredClass=classFilter.getCharacterClass();
    _class.selectItem(requiredClass);
    // - race
    DeedRaceRequirementFilter raceFilter=_filter.getRaceFilter();
    Race requiredRace=raceFilter.getRace();
    _race.selectItem(requiredRace);

    // Rewards:
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
    Integer itemId=itemFilter.getItemId();
    _item.selectItem(itemId);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Deed attributes
    JPanel deedPanel=buildDeedPanel();
    Border deedBorder=GuiFactory.buildTitledBorder("Deed");
    deedPanel.setBorder(deedBorder);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(deedPanel,c);
    y++;

    // Requirements
    JPanel requirementsPanel=buildRequirementsPanel();
    Border requirementsBorder=GuiFactory.buildTitledBorder("Requirements");
    requirementsPanel.setBorder(requirementsBorder);
    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(requirementsPanel,c);
    y++;

    // Rewards
    JPanel rewardsPanel=buildRewardsPanel();
    Border rewardsBorder=GuiFactory.buildTitledBorder("Rewards");
    rewardsPanel.setBorder(rewardsBorder);
    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(rewardsPanel,c);
    y++;

    return panel;
  }

  private JPanel buildDeedPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,0,0));
    // Label filter
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      containsPanel.add(GuiFactory.buildLabel("Name filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
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
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Type
    {
      JLabel label=GuiFactory.buildLabel("Type:");
      line2Panel.add(label);
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
      JLabel label=GuiFactory.buildLabel("Category:");
      line2Panel.add(label);
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
    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line2Panel,c);
    y++;

    return panel;
  }

  private JPanel buildRequirementsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;

    GridBagConstraints c;
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Class
      linePanel.add(GuiFactory.buildLabel("Class:"));
      _class=buildCharacterClassCombobox();
      linePanel.add(_class.getComboBox());
      // Race
      linePanel.add(GuiFactory.buildLabel("Race:"));
      _race=buildRaceCombobox();
      linePanel.add(_race.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    return panel;
  }

  private ComboBoxController<CharacterClass> buildCharacterClassCombobox()
  {
    ComboBoxController<CharacterClass> combo=CharacterUiUtils.buildClassCombo(true);
    ItemSelectionListener<CharacterClass> listener=new ItemSelectionListener<CharacterClass>()
    {
      @Override
      public void itemSelected(CharacterClass requiredClass)
      {
        DeedClassRequirementFilter filter=_filter.getClassFilter();
        filter.setCharacterClass(requiredClass);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Race> buildRaceCombobox()
  {
    ComboBoxController<Race> combo=CharacterUiUtils.buildRaceCombo(true);
    ItemSelectionListener<Race> listener=new ItemSelectionListener<Race>()
    {
      @Override
      public void itemSelected(Race requiredRace)
      {
        DeedRaceRequirementFilter filter=_filter.getRaceFilter();
        filter.setRace(requiredRace);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private JPanel buildRewardsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;

    GridBagConstraints c;
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Reputation
      linePanel.add(GuiFactory.buildLabel("Reputation:"));
      _reputation=buildReputationCombobox();
      linePanel.add(_reputation.getComboBox());
      // Title
      linePanel.add(GuiFactory.buildLabel("Title:"));
      _title=buildTitlesCombobox();
      linePanel.add(_title.getComboBox());
      // Virtue
      linePanel.add(GuiFactory.buildLabel("Virtue:"));
      _virtue=buildVirtuesCombobox();
      linePanel.add(_virtue.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Traits
      linePanel.add(GuiFactory.buildLabel("Trait:"));
      _trait=buildTraitsCombobox();
      linePanel.add(_trait.getComboBox());
      // Skills
      linePanel.add(GuiFactory.buildLabel("Skill:"));
      _skill=buildSkillsCombobox();
      linePanel.add(_skill.getComboBox());
      // Emotes
      linePanel.add(GuiFactory.buildLabel("Emote:"));
      _emote=buildEmotesCombobox();
      linePanel.add(_emote.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    {
      JPanel line=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Items
      line.add(GuiFactory.buildLabel("Item:"));
      _item=buildItemsCombobox();
      line.add(_item.getComboBox());
      // LOTRO points
      line.add(GuiFactory.buildLabel("LOTRO points:"));
      _lotroPoints=buildLotroPointsCombobox();
      line.add(_lotroPoints.getComboBox());
      // Class point
      line.add(GuiFactory.buildLabel("Class point:"));
      _classPoints=buildClassPointsCombobox();
      line.add(_classPoints.getComboBox());
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

  private ComboBoxController<Integer> buildItemsCombobox()
  {
    ComboBoxController<Integer> combo=DeedUiUtils.buildItemsCombo();
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
