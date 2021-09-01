package delta.games.lotro.gui.lore.items.chooser;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.ui.swing.text.range.RangeEditorController;
import delta.common.ui.swing.text.range.RangeListener;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.filters.ArmourTypeFilter;
import delta.games.lotro.lore.items.filters.CharacterProficienciesFilter;
import delta.games.lotro.lore.items.filters.ItemCharacterLevelFilter;
import delta.games.lotro.lore.items.filters.ItemLevelFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredClassFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredRaceFilter;
import delta.games.lotro.lore.items.filters.ItemStatFilter;
import delta.games.lotro.lore.items.filters.WeaponTypeFilter;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;

/**
 * Controller for a item filter edition panel.
 * @author DAM
 */
public class ItemFilterController extends ObjectFilterPanelController
{
  // Data
  private ItemFilterConfiguration _cfg;
  private ItemChooserFilter _filter;
  private TypedProperties _props;
  // GUI
  private JPanel _panel;
  private JTextField _contains;
  // Controllers
  private DynamicTextEditionController _textController;
  private ComboBoxController<Integer> _tier;
  private ComboBoxController<ItemQuality> _quality;
  private ComboBoxController<Boolean> _legendary;
  private ComboBoxController<WeaponType> _weaponType;
  private ComboBoxController<ArmourType> _armourType;
  private ComboBoxController<ArmourType> _shieldType;
  private List<ComboBoxController<StatDescription>> _stats;
  private CheckboxController _classRequirement;
  private CheckboxController _characterLevelRequirement;
  private CheckboxController _proficienciesRequirement;
  private RangeEditorController _itemLevelRange;
  private ComboBoxController<CharacterClass> _characterClass;
  private ComboBoxController<Race> _race;

  /**
   * Constructor.
   * @param cfg Configuration.
   * @param attrs Attributes of toon to use (may be <code>null</code>).
   * @param props Filter state.
   */
  public ItemFilterController(ItemFilterConfiguration cfg, BasicCharacterAttributes attrs, TypedProperties props)
  {
    _cfg=cfg;
    _filter=new ItemChooserFilter(cfg,attrs);
    _props=props;
    ItemChooserFilterIo.loadFrom(_filter,props);
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<Item> getFilter()
  {
    return _filter;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  @Override
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

  private void setFilter()
  {
    // Essence Tier
    if (_tier!=null)
    {
      Integer tier=_filter.getEssenceTierFilter().getTier();
      _tier.selectItem(tier);
    }
    // Name
    if (_contains!=null)
    {
      String contains=_filter.getNameFilter().getPattern();
      if (contains!=null)
      {
        _contains.setText(contains);
      }
    }
    // Quality
    if (_quality!=null)
    {
      ItemQuality quality=_filter.getQualityFilter().getQuality();
      _quality.selectItem(quality);
    }
    // Legendary
    if (_legendary!=null)
    {
      Boolean legendary=_filter.getLegendaryFilter().getLegendary();
      _legendary.selectItem(legendary);
    }
    // Weapon type
    if (_weaponType!=null)
    {
      WeaponTypeFilter weaponTypeFilter=_filter.getWeaponTypeFilter();
      WeaponType weaponType=weaponTypeFilter.getWeaponType();
      _weaponType.selectItem(weaponType);
    }
    // Armour type
    if (_armourType!=null)
    {
      ArmourTypeFilter armourTypeFilter=_filter.getArmourTypeFilter();
      ArmourType armourType=armourTypeFilter.getArmourType();
      _armourType.selectItem(armourType);
    }
    // Shield type
    if (_shieldType!=null)
    {
      ArmourTypeFilter shieldTypeFilter=_filter.getShieldTypeFilter();
      ArmourType shieldType=shieldTypeFilter.getArmourType();
      _shieldType.selectItem(shieldType);
    }
    // Stats
    if (_stats!=null)
    {
      ItemStatFilter statFilter=_filter.getStatFilter();
      int nbStats=statFilter.getNbItems();
      int nbUiStats=_stats.size();
      for(int i=0;i<Math.min(nbStats,nbUiStats);i++)
      {
        _stats.get(i).selectItem(statFilter.getStat(i));
      }
    }
    // Character requirements
    if (_classRequirement!=null)
    {
      ItemRequiredClassFilter classFilter=_filter.getClassFilter();
      _classRequirement.setSelected(classFilter.isEnabled());
    }
    if (_proficienciesRequirement!=null)
    {
      CharacterProficienciesFilter proficienciesFilter=_filter.getProficienciesFilter();
      _proficienciesRequirement.setSelected(proficienciesFilter.isEnabled());
    }
    if (_characterLevelRequirement!=null)
    {
      ItemCharacterLevelFilter levelFilter=_filter.getLevelFilter();
      _characterLevelRequirement.setSelected(levelFilter.isEnabled());
    }
    // Item level range
    if (_itemLevelRange!=null)
    {
      ItemLevelFilter itemLevelFilter=_filter.getItemLevelFilter();
      _itemLevelRange.setCurrentRange(itemLevelFilter.getMinItemLevel(),itemLevelFilter.getMaxItemLevel());
    }
    if (_characterClass!=null)
    {
      ItemRequiredClassFilter classFilter=_filter.getClassFilter();
      _characterClass.setSelectedItem(classFilter.getCharacterClass());
    }
    if (_race!=null)
    {
      ItemRequiredRaceFilter raceFilter=_filter.getRaceFilter();
      _race.setSelectedItem(raceFilter.getRace());
    }
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Line 1: quality, name
    JPanel line1Panel=buildLine1();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line1Panel,c);
    // Line 2: stats
    boolean useStats=_cfg.hasComponent(ItemChooserFilterComponent.STAT);
    if (useStats)
    {
      JPanel line2Panel=buildLine2();
      c=new GridBagConstraints(0,1,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(line2Panel,c);
    }
    // Line 3: weapon type, armour type, shield type
    JPanel line3Panel=buildLine3();
    c=new GridBagConstraints(0,2,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line3Panel,c);
    // Line 4: character-related requirements ; item level range
    JPanel line4Panel=buildLine4Panel();
    c=new GridBagConstraints(0,3,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line4Panel,c);
    return panel;
  }

  private JPanel buildLine1()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Tier
    boolean useTier=_cfg.hasComponent(ItemChooserFilterComponent.TIER);
    if (useTier)
    {
      JPanel tierPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      tierPanel.add(GuiFactory.buildLabel("Tier:"));
      _tier=buildTierCombo();
      ItemSelectionListener<Integer> tierListener=new ItemSelectionListener<Integer>()
      {
        @Override
        public void itemSelected(Integer tier)
        {
          _filter.getEssenceTierFilter().setTier(tier);
          filterUpdated();
        }
      };
      _tier.addListener(tierListener);
      tierPanel.add(_tier.getComboBox());
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(tierPanel,c);
    }
    // Quality
    boolean useQuality=_cfg.hasComponent(ItemChooserFilterComponent.QUALITY);
    if (useQuality)
    {
      JPanel qualityPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      qualityPanel.add(GuiFactory.buildLabel("Quality:"));
      _quality=ItemUiTools.buildQualityCombo();
      ItemSelectionListener<ItemQuality> qualityListener=new ItemSelectionListener<ItemQuality>()
      {
        @Override
        public void itemSelected(ItemQuality quality)
        {
          _filter.getQualityFilter().setQuality(quality);
          filterUpdated();
        }
      };
      _quality.addListener(qualityListener);
      qualityPanel.add(_quality.getComboBox());
      panel.add(qualityPanel);
    }
    // Name filter
    boolean useName=_cfg.hasComponent(ItemChooserFilterComponent.NAME);
    if (useName)
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
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
          _filter.getNameFilter().setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      panel.add(containsPanel);
    }
    // Legendary
    boolean useLegendary=_cfg.hasComponent(ItemChooserFilterComponent.LEGENDARY);
    if (useLegendary)
    {
      JPanel legendaryPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      legendaryPanel.add(GuiFactory.buildLabel("Legendary:"));
      _legendary=buildLegendaryCombo();
      ItemSelectionListener<Boolean> legendaryListener=new ItemSelectionListener<Boolean>()
      {
        @Override
        public void itemSelected(Boolean legendary)
        {
          _filter.getLegendaryFilter().setLegendary(legendary);
          filterUpdated();
        }
      };
      _legendary.addListener(legendaryListener);
      legendaryPanel.add(_legendary.getComboBox());
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(legendaryPanel,c);
    }
    return panel;
  }

  private JPanel buildLine2()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    {
      _stats=new ArrayList<ComboBoxController<StatDescription>>();
      JPanel statPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      for(int i=0;i<ItemChooserFilter.NB_STATS;i++)
      {
        statPanel.add(GuiFactory.buildLabel("Stat:"));
        ComboBoxController<StatDescription> statChooser=ItemUiTools.buildStatChooser();
        final int statIndex=i;
        ItemSelectionListener<StatDescription> statListener=new ItemSelectionListener<StatDescription>()
        {
          @Override
          public void itemSelected(StatDescription stat)
          {
            _filter.getStatFilter().setStat(statIndex,stat);
            filterUpdated();
          }
        };
        statChooser.addListener(statListener);
        _stats.add(statChooser);
        statPanel.add(statChooser.getComboBox());
        panel.add(statPanel);
      }
    }
    return panel;
  }

  private JPanel buildLine3()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    // Weapon type
    final WeaponTypeFilter weaponTypeFilter=_filter.getWeaponTypeFilter();
    boolean useWeaponType=_cfg.hasComponent(ItemChooserFilterComponent.WEAPON_TYPE);
    if ((weaponTypeFilter!=null) && (useWeaponType))
    {
      List<WeaponType> weaponTypes=_filter.getConfiguration().getWeaponTypes();
      _weaponType=ItemUiTools.buildWeaponTypeCombo(weaponTypes);
      JPanel weaponTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      weaponTypePanel.add(GuiFactory.buildLabel("Weapon type:"));
      ItemSelectionListener<WeaponType> weaponTypeListener=new ItemSelectionListener<WeaponType>()
      {
        @Override
        public void itemSelected(WeaponType type)
        {
          weaponTypeFilter.setWeaponType(type);
          // If a weapon type is selected,
          if (type!=null)
          {
            // Reset the shield type combo
            if (_armourType!=null)
            {
              _armourType.selectItem(null);
            }
            // Reset the shield type combo
            if (_shieldType!=null)
            {
              _shieldType.selectItem(null);
            }
          }
          filterUpdated();
        }
      };
      _weaponType.addListener(weaponTypeListener);
      weaponTypePanel.add(_weaponType.getComboBox());
      panel.add(weaponTypePanel);
    }
    // Armour type
    final ArmourTypeFilter armourTypeFilter=_filter.getArmourTypeFilter();
    boolean useArmourType=_cfg.hasComponent(ItemChooserFilterComponent.ARMOUR_TYPE);
    if ((armourTypeFilter!=null) && (useArmourType))
    {
      List<ArmourType> armourTypes=_filter.getConfiguration().getArmourTypes();
      _armourType=ItemUiTools.buildArmourTypeCombo(armourTypes);
      JPanel armourTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      armourTypePanel.add(GuiFactory.buildLabel("Armour type:"));
      ItemSelectionListener<ArmourType> armourTypeListener=new ItemSelectionListener<ArmourType>()
      {
        @Override
        public void itemSelected(ArmourType type)
        {
          armourTypeFilter.setArmourType(type);
          // If an armour type is selected,
          if (type!=null)
          {
            // Reset the weapon type combo
            if (_weaponType!=null)
            {
              _weaponType.selectItem(null);
            }
            // Reset the shield type combo
            if (_shieldType!=null)
            {
              _shieldType.selectItem(null);
            }
          }
          filterUpdated();
        }
      };
      _armourType.addListener(armourTypeListener);
      armourTypePanel.add(_armourType.getComboBox());
      panel.add(armourTypePanel);
    }
    // Shield type
    final ArmourTypeFilter shieldTypeFilter=_filter.getShieldTypeFilter();
    boolean useShieldType=_cfg.hasComponent(ItemChooserFilterComponent.SHIELD_TYPE);
    if ((shieldTypeFilter!=null) && (useShieldType))
    {
      List<ArmourType> shieldTypes=_filter.getConfiguration().getShieldTypes();
      _shieldType=ItemUiTools.buildArmourTypeCombo(shieldTypes);
      JPanel shieldTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      shieldTypePanel.add(GuiFactory.buildLabel("Shield type:"));
      ItemSelectionListener<ArmourType> shieldTypeListener=new ItemSelectionListener<ArmourType>()
      {
        @Override
        public void itemSelected(ArmourType type)
        {
          shieldTypeFilter.setArmourType(type);
          // If a shield type is selected,
          if (type!=null)
          {
            // Reset the weapon type combo
            if (_weaponType!=null)
            {
              _weaponType.selectItem(null);
            }
            // Reset the armour type combo
            if (_armourType!=null)
            {
              _armourType.selectItem(null);
            }
          }
          filterUpdated();
        }
      };
      _shieldType.addListener(shieldTypeListener);
      shieldTypePanel.add(_shieldType.getComboBox());
      panel.add(shieldTypePanel);
    }
    return panel;
  }

  private JPanel buildLine4Panel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    boolean useCurrentCharacterClass=_cfg.hasComponent(ItemChooserFilterComponent.CHAR_CLASS);
    boolean useProficiences=_cfg.hasComponent(ItemChooserFilterComponent.CHAR_PROFICIENCIES);
    boolean useLevel=_cfg.hasComponent(ItemChooserFilterComponent.CHAR_LEVEL);
    if (useCurrentCharacterClass || useProficiences || useLevel)
    {
      JPanel requirementsPanel=buildCurrentCharacterRequirementsPanel(useCurrentCharacterClass,useProficiences,useLevel);
      panel.add(requirementsPanel);
    }
    boolean useCharacterClass=_cfg.hasComponent(ItemChooserFilterComponent.CHARACTER_CLASS);
    boolean useRace=_cfg.hasComponent(ItemChooserFilterComponent.CHARACTER_RACE);
    if (useCharacterClass || useRace)
    {
      JPanel requirementsPanel=buildCharacterRequirementsPanel(useCharacterClass,useRace);
      panel.add(requirementsPanel);
    }
    boolean useItemLevel=_cfg.hasComponent(ItemChooserFilterComponent.ITEM_LEVEL);
    if (useItemLevel)
    {
      JPanel itemLevelPanel=buildItemLevelRangePanel();
      panel.add(itemLevelPanel);
    }
    return panel;
  }

  private JPanel buildCurrentCharacterRequirementsPanel(boolean useClass,boolean useProficiences,boolean useLevel)
  {
    JPanel requirementsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    TitledBorder border=GuiFactory.buildTitledBorder("Character requirements");
    requirementsPanel.setBorder(border);
    // Class requirement
    if (useClass)
    {
      _classRequirement=new CheckboxController("Class");
      final JCheckBox classCheckbox=_classRequirement.getCheckbox();
      _classRequirement.setSelected(true);
      requirementsPanel.add(classCheckbox);
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean selected=classCheckbox.isSelected();
          _filter.getClassFilter().setEnabled(selected);
          filterUpdated();
        }
      };
      classCheckbox.addActionListener(l);
    }
    // Proficiencies
    if (useProficiences)
    {
      _proficienciesRequirement=new CheckboxController("Proficiencies");
      final JCheckBox proficienciesCheckbox=_proficienciesRequirement.getCheckbox();
      _proficienciesRequirement.setSelected(true);
      requirementsPanel.add(proficienciesCheckbox);
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean selected=proficienciesCheckbox.isSelected();
          _filter.getProficienciesFilter().setEnabled(selected);
          filterUpdated();
        }
      };
      proficienciesCheckbox.addActionListener(l);
    }
    // Level
    if (useLevel)
    {
      _characterLevelRequirement=new CheckboxController("Level");
      final JCheckBox levelCheckbox=_characterLevelRequirement.getCheckbox();
      _characterLevelRequirement.setSelected(true);
      requirementsPanel.add(levelCheckbox);
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean selected=levelCheckbox.isSelected();
          _filter.getLevelFilter().setEnabled(selected);
          filterUpdated();
        }
      };
      levelCheckbox.addActionListener(l);
    }
    return requirementsPanel;
  }

  private JPanel buildCharacterRequirementsPanel(boolean useCharacterClass, boolean useRace)
  {
    JPanel requirementsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    TitledBorder border=GuiFactory.buildTitledBorder("Character requirements");
    requirementsPanel.setBorder(border);
    // Class requirement
    if (useCharacterClass)
    {
      _characterClass=CharacterUiUtils.buildClassCombo(true);
      requirementsPanel.add(GuiFactory.buildLabel("Class:"));
      requirementsPanel.add(_characterClass.getComboBox());
      ItemSelectionListener<CharacterClass> l=new ItemSelectionListener<CharacterClass>()
      {
        @Override
        public void itemSelected(CharacterClass selected)
        {
          _filter.getClassFilter().setCharacterClass(selected);
          filterUpdated();
        }
      };
      _characterClass.addListener(l);
    }
    // Race requirement
    if (useRace)
    {
      _race=CharacterUiUtils.buildRaceCombo(true);
      requirementsPanel.add(GuiFactory.buildLabel("Race:"));
      requirementsPanel.add(_race.getComboBox());
      ItemSelectionListener<Race> l=new ItemSelectionListener<Race>()
      {
        @Override
        public void itemSelected(Race selected)
        {
          _filter.getRaceFilter().setRace(selected);
          filterUpdated();
        }
      };
      _race.addListener(l);
    }
    return requirementsPanel;
  }

  /**
   * Build a controller for a combo box to choose an essence tier.
   * @return A new controller.
   */
  private ComboBoxController<Integer> buildTierCombo()
  {
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>();
    ctrl.addEmptyItem("");
    for(int tier=1;tier<=12;tier++)
    {
      ctrl.addItem(Integer.valueOf(tier),"Tier "+tier);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for a combo box to choose the legendary quality of item.
   * @return A new controller.
   */
  private ComboBoxController<Boolean> buildLegendaryCombo()
  {
    ComboBoxController<Boolean> ctrl=new ComboBoxController<Boolean>();
    ctrl.addEmptyItem("");
    ctrl.addItem(Boolean.TRUE,"Yes");
    ctrl.addItem(Boolean.FALSE,"No");
    ctrl.selectItem(null);
    return ctrl;
  }

  private JPanel buildItemLevelRangePanel()
  {
    List<Integer> itemLevels=buildItemLevels();
    _itemLevelRange=new RangeEditorController();
    JPanel rangePanel=_itemLevelRange.getPanel();
    _itemLevelRange.setRangeValues(itemLevels);
    TitledBorder title=GuiFactory.buildTitledBorder("Item Level");
    rangePanel.setBorder(title);
    RangeListener listener=new RangeListener()
    {
      @Override
      public void rangeUpdated(RangeEditorController source, Integer minValue, Integer maxValue)
      {
        _filter.getItemLevelFilter().setRange(minValue,maxValue);
        filterUpdated();
      }
    };
    _itemLevelRange.getListeners().addListener(listener);
    return rangePanel;
  }

  private List<Integer> buildItemLevels()
  {
    Set<Integer> possibleItemLevels=_filter.getConfiguration().getItemLevels();
    Set<Integer> allItemLevels=new HashSet<Integer>(possibleItemLevels);
    ItemLevelFilter itemLevelFilter=_filter.getItemLevelFilter();
    if (itemLevelFilter!=null)
    {
      Integer minLevel=itemLevelFilter.getMinItemLevel();
      if (minLevel!=null)
      {
        allItemLevels.add(minLevel);
      }
      Integer maxLevel=itemLevelFilter.getMaxItemLevel();
      if (maxLevel!=null)
      {
        allItemLevels.add(maxLevel);
      }
    }
    List<Integer> ret=new ArrayList<Integer>(allItemLevels);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    if (_props!=null)
    {
      ItemChooserFilterIo.saveTo(_filter,_props);
      _props=null;
    }
    _filter=null;
    // Controllers
    if (_tier!=null)
    {
      _tier.dispose();
      _tier=null;
    }
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    if (_quality!=null)
    {
      _quality.dispose();
      _quality=null;
    }
    if (_weaponType!=null)
    {
      _weaponType.dispose();
      _weaponType=null;
    }
    if (_armourType!=null)
    {
      _armourType.dispose();
      _armourType=null;
    }
    // Stats
    if (_stats!=null)
    {
      for(ComboBoxController<StatDescription> statCombo : _stats)
      {
        statCombo.dispose();
      }
      _stats.clear();
      _stats=null;
    }
    // Requirements
    if (_classRequirement!=null)
    {
      _classRequirement.dispose();
      _classRequirement=null;
    }
    if (_characterLevelRequirement!=null)
    {
      _characterLevelRequirement.dispose();
      _characterLevelRequirement=null;
    }
    if (_proficienciesRequirement!=null)
    {
      _proficienciesRequirement.dispose();
      _proficienciesRequirement=null;
    }
    if (_itemLevelRange!=null)
    {
      _itemLevelRange.dispose();
      _itemLevelRange=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contains=null;
  }
}
