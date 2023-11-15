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

import javax.swing.JButton;
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
import delta.games.lotro.character.classes.AbstractClassDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.enums.Genus;
import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemUtils;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.filters.ArmourTypeFilter;
import delta.games.lotro.lore.items.filters.CharacterProficienciesFilter;
import delta.games.lotro.lore.items.filters.DamageTypeFilter;
import delta.games.lotro.lore.items.filters.ItemCharacterLevelFilter;
import delta.games.lotro.lore.items.filters.ItemEquipmentLocationFilter;
import delta.games.lotro.lore.items.filters.ItemLevelFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredClassFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredRaceFilter;
import delta.games.lotro.lore.items.filters.ItemStatFilter;
import delta.games.lotro.lore.items.filters.ScalableItemFilter;
import delta.games.lotro.lore.items.filters.WeaponSlayerFilter;
import delta.games.lotro.lore.items.filters.WeaponTypeFilter;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;

/**
 * Controller for a item filter edition panel.
 * @author DAM
 */
public class ItemFilterController extends ObjectFilterPanelController implements ActionListener
{
  // Data
  private ItemFilterConfiguration _cfg;
  private ItemChooserFilter _filter;
  private TypedProperties _props;
  // GUI
  private JPanel _panel;
  private JTextField _contains;
  private JButton _reset;
  // Controllers
  private DynamicTextEditionController _textController;
  private ComboBoxController<Integer> _tier;
  private ComboBoxController<ItemQuality> _quality;
  private ComboBoxController<ItemClass> _itemClass;
  private ComboBoxController<Boolean> _legendary;
  private ComboBoxController<Set<EquipmentLocation>> _location;
  private ComboBoxController<WeaponType> _weaponType;
  private ComboBoxController<DamageType> _damageType;
  private ComboBoxController<Genus> _slayerGenus;
  private ComboBoxController<ArmourType> _armourType;
  private ComboBoxController<ArmourType> _shieldType;
  private List<ComboBoxController<StatDescription>> _stats;
  private CheckboxController _classRequirement;
  private CheckboxController _characterLevelRequirement;
  private CheckboxController _proficienciesRequirement;
  private RangeEditorController _itemLevelRange;
  private ComboBoxController<Boolean> _scalable;
  private ComboBoxController<AbstractClassDescription> _class;
  private ComboBoxController<RaceDescription> _race;

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

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      if (_tier!=null)
      {
        _tier.selectItem(null);
      }
      // Name
      if (_contains!=null)
      {
        _contains.setText("");
      }
      // Quality
      if (_quality!=null)
      {
        _quality.selectItem(null);
      }
      // Item class
      if (_itemClass!=null)
      {
        _itemClass.selectItem(null);
      }
      // Legendary
      if (_legendary!=null)
      {
        _legendary.selectItem(null);
      }
      // Location
      if (_location!=null)
      {
        _location.selectItem(new ItemEquipmentLocationFilter().getSelectedLocations());
      }
      // Weapon type
      if (_weaponType!=null)
      {
        _weaponType.selectItem(null);
      }
      // Damage type
      if (_damageType!=null)
      {
        _damageType.selectItem(null);
      }
      // Slayer genus
      if (_slayerGenus!=null)
      {
        _slayerGenus.selectItem(null);
      }
      // Armour type
      if (_armourType!=null)
      {
        _armourType.selectItem(null);
      }
      // Shield type
      if (_shieldType!=null)
      {
        _shieldType.selectItem(null);
      }
      // Stats
      if (_stats!=null)
      {
        int nbUiStats=_stats.size();
        for(int i=0;i<nbUiStats;i++)
        {
          _stats.get(i).selectItem(null);
        }
      }
      // Character requirements
      if (_classRequirement!=null)
      {
        _classRequirement.setSelected(true);
      }
      if (_proficienciesRequirement!=null)
      {
        _proficienciesRequirement.setSelected(true);
      }
      if (_characterLevelRequirement!=null)
      {
        _characterLevelRequirement.setSelected(true);
      }
      // Item level range
      if (_itemLevelRange!=null)
      {
        _itemLevelRange.setCurrentRange(null,null);
      }
      // Scalable
      if (_scalable!=null)
      {
        _scalable.selectItem(null);
      }
      if (_class!=null)
      {
        _class.selectItem(null);
      }
      if (_race!=null)
      {
        _race.selectItem(null);
      }
    }
  }

  private void setFilter()
  {
    // Tier
    if (_tier!=null)
    {
      Integer tier=_filter.getTierFilter().getTier();
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
    // Item class
    if (_itemClass!=null)
    {
      ItemClass category=_filter.getCategoryFilter().getItemClass();
      _itemClass.selectItem(category);
    }
    // Legendary
    if (_legendary!=null)
    {
      Boolean legendary=_filter.getLegendaryFilter().getLegendary();
      _legendary.selectItem(legendary);
    }
    // Location
    if (_location!=null)
    {
      ItemEquipmentLocationFilter locationFilter=_filter.getLocationFilter();
      Set<EquipmentLocation> selectedLocations=locationFilter.getSelectedLocations();
      _location.selectItem(selectedLocations);
    }
    // Weapon type
    if (_weaponType!=null)
    {
      WeaponTypeFilter weaponTypeFilter=_filter.getWeaponTypeFilter();
      WeaponType weaponType=weaponTypeFilter.getWeaponType();
      _weaponType.selectItem(weaponType);
    }
    // Damage type
    if (_damageType!=null)
    {
      DamageTypeFilter damageTypeFilter=_filter.getDamageTypeFilter();
      DamageType damageType=damageTypeFilter.getDamageType();
      _damageType.selectItem(damageType);
    }
    // Slayer genus
    if (_slayerGenus!=null)
    {
      WeaponSlayerFilter slayerGenusFilter=_filter.getSlayerGenusFilter();
      Genus genus=slayerGenusFilter.getGenus();
      _slayerGenus.selectItem(genus);
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
      ItemRequiredClassFilter classFilter=_filter.getCurrentCharacterClassFilter();
      _classRequirement.setSelected(classFilter.isEnabled());
    }
    if (_proficienciesRequirement!=null)
    {
      CharacterProficienciesFilter proficienciesFilter=_filter.getCurrentCharacterProficienciesFilter();
      _proficienciesRequirement.setSelected(proficienciesFilter.isEnabled());
    }
    if (_characterLevelRequirement!=null)
    {
      ItemCharacterLevelFilter levelFilter=_filter.getCurrentCharacterLevelFilter();
      _characterLevelRequirement.setSelected(levelFilter.isEnabled());
    }
    // Item level range
    if (_itemLevelRange!=null)
    {
      ItemLevelFilter itemLevelFilter=_filter.getItemLevelFilter();
      _itemLevelRange.setCurrentRange(itemLevelFilter.getMinItemLevel(),itemLevelFilter.getMaxItemLevel());
    }
    // Scalable
    if (_scalable!=null)
    {
      ScalableItemFilter filter=_filter.getScalableFilter();
      _scalable.selectItem(filter.getScalable());
    }
    // Class
    if (_class!=null)
    {
      ItemRequiredClassFilter classFilter=_filter.getGenericClassFilter();
      _class.setSelectedItem(classFilter.getCharacterClass());
    }
    // Race
    if (_race!=null)
    {
      ItemRequiredRaceFilter raceFilter=_filter.getGenericRaceFilter();
      _race.setSelectedItem(raceFilter.getRace());
    }
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Line 1: quality, name
    JPanel line1Panel=buildLine1();
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line1Panel,c);
    // Line 2: stats
    boolean useStats=_cfg.hasComponent(ItemChooserFilterComponent.STAT);
    if (useStats)
    {
      JPanel line2Panel=buildLine2();
      c=new GridBagConstraints(0,1,2,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(line2Panel,c);
    }
    // Line 3: weapon type, armour type, shield type
    JPanel line3Panel=buildLine3();
    c=new GridBagConstraints(0,2,2,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line3Panel,c);
    // Line 4: character-related requirements ; item level range
    JPanel line4Panel=buildLine4();
    c=new GridBagConstraints(0,3,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line4Panel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,3,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);

    return panel;
  }

  private JPanel buildLine1()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Location
    initLocation(panel);
    // Quality
    initQualityPanel(panel);
    // Name filter
    initNamePanel(panel);
    // Category
    initCategoryPanel(panel);
    return panel;
  }

  private void initTierPanel(JPanel panel)
  {
    boolean useTier=_cfg.hasComponent(ItemChooserFilterComponent.TIER);
    if (useTier)
    {
      JPanel tierPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      tierPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.tier")));
      _tier=buildTierCombo();
      ItemSelectionListener<Integer> tierListener=new ItemSelectionListener<Integer>()
      {
        @Override
        public void itemSelected(Integer tier)
        {
          _filter.getTierFilter().setTier(tier);
          filterUpdated();
        }
      };
      _tier.addListener(tierListener);
      tierPanel.add(_tier.getComboBox());
      panel.add(tierPanel);
    }
  }

  private void initQualityPanel(JPanel panel)
  {
    boolean useQuality=_cfg.hasComponent(ItemChooserFilterComponent.QUALITY);
    if (useQuality)
    {
      JPanel qualityPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      qualityPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.quality")));
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
  }

  private void initNamePanel(JPanel panel)
  {
    boolean useName=_cfg.hasComponent(ItemChooserFilterComponent.NAME);
    if (useName)
    {
      JPanel namePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      namePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.name")));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      namePanel.add(_contains);
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
      panel.add(namePanel);
    }
  }

  private void initCategoryPanel(JPanel panel)
  {
    boolean useCategory=_cfg.hasComponent(ItemChooserFilterComponent.CATEGORY);
    if (useCategory)
    {
      JPanel categoryPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      categoryPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.category")));
      _itemClass=ItemUiTools.buildCategoryCombo();
      ItemSelectionListener<ItemClass> itemClassListener=new ItemSelectionListener<ItemClass>()
      {
        @Override
        public void itemSelected(ItemClass category)
        {
          _filter.getCategoryFilter().setItemClass(category);
          filterUpdated();
        }
      };
      _itemClass.addListener(itemClassListener);
      categoryPanel.add(_itemClass.getComboBox());
      panel.add(categoryPanel);
    }
  }

  private void initLegendaryPanel(JPanel panel)
  {
    boolean useLegendary=_cfg.hasComponent(ItemChooserFilterComponent.LEGENDARY);
    if (useLegendary)
    {
      JPanel legendaryPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      legendaryPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.legendary")));
      _legendary=SharedUiUtils.build3StatesBooleanCombobox();
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
      panel.add(legendaryPanel);
    }
  }

  private JPanel buildLine2()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    {
      _stats=new ArrayList<ComboBoxController<StatDescription>>();
      JPanel statPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      for(int i=0;i<ItemChooserFilter.NB_STATS;i++)
      {
        statPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.stat")));
        ComboBoxController<StatDescription> statChooser=SharedUiUtils.buildStatChooser();
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
    initWeaponType(panel);
    // Damage type
    initDamageType(panel);
    // Slayer genus
    initSlayerGenus(panel);
    // Armour type
    initArmourType(panel);
    // Shield type
    initShieldType(panel);
    return panel;
  }

  private void initLocation(JPanel panel)
  {
    final ItemEquipmentLocationFilter locationFilter=_filter.getLocationFilter();
    boolean useLocation=_cfg.hasComponent(ItemChooserFilterComponent.LOCATION);
    if ((locationFilter!=null) && (useLocation))
    {
      _location=ItemUiTools.buildLocationsCombo();
      JPanel locationPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      locationPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.location")));
      ItemSelectionListener<Set<EquipmentLocation>> locationListener=new ItemSelectionListener<Set<EquipmentLocation>>()
      {
        @Override
        public void itemSelected(Set<EquipmentLocation> locations)
        {
          if (locations!=null)
          {
            Set<EquipmentLocation> selectedLocations=new HashSet<EquipmentLocation>();
            selectedLocations.addAll(locations);
            locationFilter.setLocations(selectedLocations);
          }
          else
          {
            locationFilter.selectAll();
          }
          filterUpdated();
        }
      };
      _location.addListener(locationListener);
      locationPanel.add(_location.getComboBox());
      panel.add(locationPanel);
    }
  }

  private void initWeaponType(JPanel panel)
  {
    final WeaponTypeFilter weaponTypeFilter=_filter.getWeaponTypeFilter();
    boolean useWeaponType=_cfg.hasComponent(ItemChooserFilterComponent.WEAPON_TYPE);
    if ((weaponTypeFilter!=null) && (useWeaponType))
    {
      List<WeaponType> weaponTypes=_filter.getConfiguration().getWeaponTypes();
      _weaponType=ItemUiTools.buildWeaponTypeCombo(weaponTypes);
      JPanel weaponTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      weaponTypePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.weaponType")));
      ItemSelectionListener<WeaponType> weaponTypeListener=new ItemSelectionListener<WeaponType>()
      {
        @Override
        public void itemSelected(WeaponType type)
        {
          weaponTypeFilter.setWeaponType(type);
          // If a weapon type is selected,
          if (type!=null)
          {
            // Reset the armour type combo
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
  }

  private void initDamageType(JPanel panel)
  {
    final DamageTypeFilter damageTypeFilter=_filter.getDamageTypeFilter();
    boolean useDamageType=_cfg.hasComponent(ItemChooserFilterComponent.DAMAGE_TYPE);
    if ((damageTypeFilter!=null) && (useDamageType))
    {
      List<DamageType> damageTypes=_filter.getConfiguration().getDamageTypes();
      _damageType=ItemUiTools.buildDamageTypeCombo(damageTypes);
      JPanel damageTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      damageTypePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.damageType")));
      ItemSelectionListener<DamageType> damageTypeListener=new ItemSelectionListener<DamageType>()
      {
        @Override
        public void itemSelected(DamageType type)
        {
          damageTypeFilter.setDamageType(type);
          // If a weapon type is selected,
          if (type!=null)
          {
            // Reset the armour type combo
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
      _damageType.addListener(damageTypeListener);
      damageTypePanel.add(_damageType.getComboBox());
      panel.add(damageTypePanel);
    }
  }

  private void initSlayerGenus(JPanel panel)
  {
    final WeaponSlayerFilter slayerGenusFilter=_filter.getSlayerGenusFilter();
    boolean useSlayerGenus=_cfg.hasComponent(ItemChooserFilterComponent.SLAYER_GENUS);
    if ((slayerGenusFilter!=null) && (useSlayerGenus))
    {
      List<Genus> genuses=ItemUtils.getAvailableSlayerGenus();
      _slayerGenus=ItemUiTools.buildGenus(genuses);
      JPanel genusPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      genusPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.slayGenus")));
      ItemSelectionListener<Genus> genusListener=new ItemSelectionListener<Genus>()
      {
        @Override
        public void itemSelected(Genus genus)
        {
          slayerGenusFilter.setGenus(genus);
          // If a weapon type is selected,
          if (genus!=null)
          {
            // Reset the armour type combo
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
      _slayerGenus.addListener(genusListener);
      genusPanel.add(_slayerGenus.getComboBox());
      panel.add(genusPanel);
    }
  }

  private void initArmourType(JPanel panel)
  {
    final ArmourTypeFilter armourTypeFilter=_filter.getArmourTypeFilter();
    boolean useArmourType=_cfg.hasComponent(ItemChooserFilterComponent.ARMOUR_TYPE);
    if ((armourTypeFilter!=null) && (useArmourType))
    {
      List<ArmourType> armourTypes=_filter.getConfiguration().getArmourTypes();
      _armourType=ItemUiTools.buildArmourTypeCombo(armourTypes);
      JPanel armourTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      armourTypePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.armourType")));
      ItemSelectionListener<ArmourType> armourTypeListener=new ItemSelectionListener<ArmourType>()
      {
        @Override
        public void itemSelected(ArmourType type)
        {
          armourTypeFilter.setArmourType(type);
          // If an armour type is selected,
          if (type!=null)
          {
            resetWeaponGadgets();
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
  }

  private void resetWeaponGadgets()
  {
    // Reset the weapon type combo
    if (_weaponType!=null)
    {
      _weaponType.selectItem(null);
    }
    // Reset the damage type combo
    if (_damageType!=null)
    {
      _damageType.selectItem(null);
    }
    // Reset slayer genus
    if (_slayerGenus!=null)
    {
      _slayerGenus.selectItem(null);
    }
  }

  private void initShieldType(JPanel panel)
  {
    final ArmourTypeFilter shieldTypeFilter=_filter.getShieldTypeFilter();
    boolean useShieldType=_cfg.hasComponent(ItemChooserFilterComponent.SHIELD_TYPE);
    if ((shieldTypeFilter!=null) && (useShieldType))
    {
      List<ArmourType> shieldTypes=_filter.getConfiguration().getShieldTypes();
      _shieldType=ItemUiTools.buildArmourTypeCombo(shieldTypes);
      JPanel shieldTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      shieldTypePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.shieldType")));
      ItemSelectionListener<ArmourType> shieldTypeListener=new ItemSelectionListener<ArmourType>()
      {
        @Override
        public void itemSelected(ArmourType type)
        {
          shieldTypeFilter.setArmourType(type);
          // If a shield type is selected,
          if (type!=null)
          {
            resetWeaponGadgets();
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
  }

  private JPanel buildLine4()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    // Class/proficiencies/level requirements
    boolean useCurrentCharacterClass=_cfg.hasComponent(ItemChooserFilterComponent.CURRENT_CHAR_CLASS);
    boolean useCurrentCharacterProficiences=_cfg.hasComponent(ItemChooserFilterComponent.CURRENT_CHAR_PROFICIENCIES);
    boolean useCurrentCharacterLevel=_cfg.hasComponent(ItemChooserFilterComponent.CURRENT_CHAR_LEVEL);
    if (useCurrentCharacterClass || useCurrentCharacterProficiences || useCurrentCharacterLevel)
    {
      JPanel requirementsPanel=buildCurrentCharacterRequirementsPanel(useCurrentCharacterClass,useCurrentCharacterProficiences,useCurrentCharacterLevel);
      panel.add(requirementsPanel);
    }
    // Race/class requirements
    boolean useGenericClass=_cfg.hasComponent(ItemChooserFilterComponent.GENERIC_CHARACTER_CLASS);
    boolean useGenericRace=_cfg.hasComponent(ItemChooserFilterComponent.GENERIC_CHARACTER_RACE);
    if (useGenericClass || useGenericRace)
    {
      JPanel requirementsPanel=buildCharacterRequirementsPanel(useGenericClass,useGenericRace,true);
      panel.add(requirementsPanel);
    }
    // Item level
    boolean useItemLevel=_cfg.hasComponent(ItemChooserFilterComponent.ITEM_LEVEL);
    if (useItemLevel)
    {
      JPanel itemLevelPanel=buildItemLevelRangePanel();
      panel.add(itemLevelPanel);
    }
    // Scalable
    boolean useScalable=_cfg.hasComponent(ItemChooserFilterComponent.SCALABLE);
    if (useScalable)
    {
      JPanel scalablePanel=buildScalablePanel();
      panel.add(scalablePanel);
    }
    // Legendary
    initLegendaryPanel(panel);
    // Tier
    initTierPanel(panel);
    return panel;
  }

  private JPanel buildCurrentCharacterRequirementsPanel(boolean useClass,boolean useProficiences,boolean useLevel)
  {
    JPanel requirementsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    TitledBorder border=GuiFactory.buildTitledBorder(Labels.getLabel("items.filter.characterRequirements.border"));
    requirementsPanel.setBorder(border);
    // Class requirement
    if (useClass)
    {
      _classRequirement=new CheckboxController(Labels.getLabel("items.filter.class.checkbox"));
      final JCheckBox classCheckbox=_classRequirement.getCheckbox();
      _classRequirement.setSelected(true);
      requirementsPanel.add(classCheckbox);
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean selected=classCheckbox.isSelected();
          _filter.getCurrentCharacterClassFilter().setEnabled(selected);
          filterUpdated();
        }
      };
      classCheckbox.addActionListener(l);
    }
    // Proficiencies
    if (useProficiences)
    {
      _proficienciesRequirement=new CheckboxController(Labels.getLabel("items.filter.proficiencies.checkbox"));
      final JCheckBox proficienciesCheckbox=_proficienciesRequirement.getCheckbox();
      _proficienciesRequirement.setSelected(true);
      requirementsPanel.add(proficienciesCheckbox);
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean selected=proficienciesCheckbox.isSelected();
          _filter.getCurrentCharacterProficienciesFilter().setEnabled(selected);
          filterUpdated();
        }
      };
      proficienciesCheckbox.addActionListener(l);
    }
    // Level
    if (useLevel)
    {
      _characterLevelRequirement=new CheckboxController(Labels.getLabel("items.filter.level.checkbox"));
      final JCheckBox levelCheckbox=_characterLevelRequirement.getCheckbox();
      _characterLevelRequirement.setSelected(true);
      requirementsPanel.add(levelCheckbox);
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean selected=levelCheckbox.isSelected();
          _filter.getCurrentCharacterLevelFilter().setEnabled(selected);
          filterUpdated();
        }
      };
      levelCheckbox.addActionListener(l);
    }
    return requirementsPanel;
  }

  private JPanel buildCharacterRequirementsPanel(boolean useCharacterClass, boolean useRace, boolean useMonsterClasses)
  {
    JPanel requirementsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    TitledBorder border=GuiFactory.buildTitledBorder(Labels.getLabel("items.filter.characterRequirements.border"));
    requirementsPanel.setBorder(border);
    // Class requirement
    if (useCharacterClass)
    {
      _class=CharacterUiUtils.buildClassCombo(true,useMonsterClasses);
      requirementsPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.class")));
      requirementsPanel.add(_class.getComboBox());
      ItemSelectionListener<AbstractClassDescription> l=new ItemSelectionListener<AbstractClassDescription>()
      {
        @Override
        public void itemSelected(AbstractClassDescription selected)
        {
          _filter.getGenericClassFilter().setClass(selected);
          filterUpdated();
        }
      };
      _class.addListener(l);
    }
    // Race requirement
    if (useRace)
    {
      _race=CharacterUiUtils.buildRaceCombo(true);
      requirementsPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.race")));
      requirementsPanel.add(_race.getComboBox());
      ItemSelectionListener<RaceDescription> l=new ItemSelectionListener<RaceDescription>()
      {
        @Override
        public void itemSelected(RaceDescription selected)
        {
          _filter.getGenericRaceFilter().setRace(selected);
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
    for(int tier=1;tier<=15;tier++)
    {
      String tierLabel=Labels.getLabel("shared.tier",new Object[] { Integer.valueOf(tier) });
      ctrl.addItem(Integer.valueOf(tier),tierLabel);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private JPanel buildItemLevelRangePanel()
  {
    List<Integer> itemLevels=buildItemLevels();
    _itemLevelRange=new RangeEditorController();
    JPanel rangePanel=_itemLevelRange.getPanel();
    _itemLevelRange.setRangeValues(itemLevels);
    TitledBorder title=GuiFactory.buildTitledBorder(Labels.getLabel("items.filter.itemLevel.border"));
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

  private JPanel buildScalablePanel()
  {
    JPanel scalablePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    scalablePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("items.filter.scalable")));
    _scalable=SharedUiUtils.build3StatesBooleanCombobox(Labels.getLabel("shared.both"));
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean scalable)
      {
        _filter.getScalableFilter().setScalable(scalable);
        filterUpdated();
      }
    };
    _scalable.addListener(listener);
    scalablePanel.add(_scalable.getComboBox());
    return scalablePanel;
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
    if (_itemClass!=null)
    {
      _itemClass.dispose();
      _itemClass=null;
    }
    if (_location!=null)
    {
      _location.dispose();
      _location=null;
    }
    if (_weaponType!=null)
    {
      _weaponType.dispose();
      _weaponType=null;
    }
    if (_damageType!=null)
    {
      _damageType.dispose();
      _damageType=null;
    }
    if (_slayerGenus!=null)
    {
      _slayerGenus.dispose();
      _slayerGenus=null;
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
    if (_scalable!=null)
    {
      _scalable.dispose();
      _scalable=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contains=null;
    _reset=null;
  }
}
