package delta.games.lotro.gui.items;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.filters.ArmourTypeFilter;
import delta.games.lotro.lore.items.filters.CharacterProficienciesFilter;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
import delta.games.lotro.lore.items.filters.ItemQualityFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredClassFilter;
import delta.games.lotro.lore.items.filters.ItemRequiredLevelFilter;
import delta.games.lotro.lore.items.filters.ItemStatFilter;
import delta.games.lotro.lore.items.filters.WeaponTypeFilter;

/**
 * Controller for a item filter edition panel.
 * @author DAM
 */
public class ItemFilterController extends AbstractItemFilterPanelController
{
  private static final int NB_STATS=3;

  // Data
  private Filter<Item> _filter;
  private ItemRequiredClassFilter _classFilter;
  private CharacterProficienciesFilter _proficienciesFilter;
  private ItemRequiredLevelFilter _levelFilter;
  private ItemNameFilter _nameFilter;
  private ItemQualityFilter _qualityFilter;
  private WeaponTypeFilter _weaponTypeFilter;
  private ArmourTypeFilter _armourTypeFilter;
  private ArmourTypeFilter _shieldTypeFilter;
  private ItemStatFilter _statFilter;
  private ItemFilterConfiguration _cfg;
  // GUI
  private JPanel _panel;
  private JTextField _contains;
  // Controllers
  private DynamicTextEditionController _textController;
  private ComboBoxController<ItemQuality> _quality;
  private ComboBoxController<WeaponType> _weaponType;
  private ComboBoxController<ArmourType> _armourType;
  private ComboBoxController<ArmourType> _shieldType;
  private List<ComboBoxController<STAT>> _stats;
  private CheckboxController _classRequirement;
  private CheckboxController _characterLevelRequirement;
  private CheckboxController _proficienciesRequirement;

  /**
   * Constructor.
   */
  public ItemFilterController()
  {
    this(new ItemFilterConfiguration(),null);
  }

  /**
   * Constructor.
   * @param cfg Configuration.
   * @param character Targeted character (may be <code>null</code>).
   */
  public ItemFilterController(ItemFilterConfiguration cfg, CharacterData character)
  {
    _cfg=cfg;
    List<Filter<Item>> filters=new ArrayList<Filter<Item>>();
    // Character proficiencies
    if (character!=null)
    {
      CharacterClass characterClass=character.getCharacterClass();
      int level=character.getLevel();
      // Class
      _classFilter=new ItemRequiredClassFilter(characterClass,false);
      filters.add(_classFilter);
      // Proficiencies
      _proficienciesFilter=new CharacterProficienciesFilter(characterClass,level);
      filters.add(_proficienciesFilter);
      // Level
      _levelFilter=new ItemRequiredLevelFilter(level);
      filters.add(_levelFilter);
    }
    // Name
    _nameFilter=new ItemNameFilter();
    filters.add(_nameFilter);
    // Quality
    _qualityFilter=new ItemQualityFilter(null);
    filters.add(_qualityFilter);
    // Weapon type
    List<WeaponType> weaponTypes=_cfg.getWeaponTypes();
    if (weaponTypes.size()>0)
    {
      _weaponTypeFilter=new WeaponTypeFilter(null);
      _weaponType=ItemUiTools.buildWeaponTypeCombo(weaponTypes);
      filters.add(_weaponTypeFilter);
    }
    // Armour type
    List<ArmourType> armourTypes=_cfg.getArmourTypes();
    if (armourTypes.size()>0)
    {
      _armourTypeFilter=new ArmourTypeFilter(null);
      _armourType=ItemUiTools.buildArmourTypeCombo(armourTypes);
      filters.add(_armourTypeFilter);
    }
    // Shield type
    List<ArmourType> shieldTypes=_cfg.getShieldTypes();
    if (shieldTypes.size()>0)
    {
      _shieldTypeFilter=new ArmourTypeFilter(null);
      _shieldType=ItemUiTools.buildArmourTypeCombo(shieldTypes);
      filters.add(_shieldTypeFilter);
    }
    // Stat contribution
    _statFilter=new ItemStatFilter(NB_STATS);
    filters.add(_statFilter);
    _filter=new CompoundFilter<Item>(Operator.AND,filters);
    // Character requirements
    _classRequirement=new CheckboxController("Class");
    _proficienciesRequirement=new CheckboxController("Proficiencies");
    _characterLevelRequirement=new CheckboxController("Level");
    
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
    // Name
    String contains=_nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Quality
    ItemQuality quality=_qualityFilter.getQuality();
    _quality.selectItem(quality);
    // Weapon type
    if (_weaponType!=null)
    {
      WeaponType weaponType=_weaponTypeFilter.getWeaponType();
      _weaponType.selectItem(weaponType);
    }
    // Armour type
    if (_armourType!=null)
    {
      ArmourType armourType=_armourTypeFilter.getArmourType();
      _armourType.selectItem(armourType);
    }
    // Shield type
    if (_shieldType!=null)
    {
      ArmourType shieldType=_shieldTypeFilter.getArmourType();
      _shieldType.selectItem(shieldType);
    }
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Line 1: quality, name
    JPanel line1Panel=buildLine1();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line1Panel,c);
    // Line 1bis: stats
    JPanel line1BisPanel=buildLine1Bis();
    c=new GridBagConstraints(0,1,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line1BisPanel,c);
    // Line 2: weapon type, armour type, shield type
    JPanel line2Panel=buildLine2();
    c=new GridBagConstraints(0,2,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line2Panel,c);
    // Line 3: character-related requirements
    JPanel requirementsPanel=buildRequirementsPanel();
    c=new GridBagConstraints(0,3,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(requirementsPanel,c);
    return panel;
  }

  private JPanel buildLine1()
  {
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Quality
    {
      JPanel qualityPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      qualityPanel.add(GuiFactory.buildLabel("Quality:"));
      _quality=ItemUiTools.buildQualityCombo();
      ItemSelectionListener<ItemQuality> qualityListener=new ItemSelectionListener<ItemQuality>()
      {
        @Override
        public void itemSelected(ItemQuality quality)
        {
          _qualityFilter.setQuality(quality);
          filterUpdated();
        }
      };
      _quality.addListener(qualityListener);
      qualityPanel.add(_quality.getComboBox());
      line1Panel.add(qualityPanel);
    }
    // Label filter
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      containsPanel.add(GuiFactory.buildLabel("Label filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          _nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      line1Panel.add(containsPanel);
    }
    return line1Panel;
  }

  private JPanel buildLine1Bis()
  {
    JPanel line1BisPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    {
      _stats=new ArrayList<ComboBoxController<STAT>>();
      JPanel statPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      for(int i=0;i<NB_STATS;i++)
      {
        statPanel.add(GuiFactory.buildLabel("Stat:"));
        ComboBoxController<STAT> statChooser=ItemUiTools.buildStatChooser();
        final int statIndex=i;
        ItemSelectionListener<STAT> statListener=new ItemSelectionListener<STAT>()
        {
          @Override
          public void itemSelected(STAT stat)
          {
            _statFilter.setStat(statIndex,stat);
            filterUpdated();
          }
        };
        statChooser.addListener(statListener);
        _stats.add(statChooser);
        statPanel.add(statChooser.getComboBox());
        line1BisPanel.add(statPanel);
      }
    }
    return line1BisPanel;
  }

  private JPanel buildLine2()
  {
    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    // Weapon type
    if (_weaponType!=null)
    {
      JPanel weaponTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      weaponTypePanel.add(GuiFactory.buildLabel("Weapon type:"));
      ItemSelectionListener<WeaponType> weaponTypeListener=new ItemSelectionListener<WeaponType>()
      {
        @Override
        public void itemSelected(WeaponType type)
        {
          _weaponTypeFilter.setWeaponType(type);
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
      line2Panel.add(weaponTypePanel);
    }
    // Armour type
    if (_armourType!=null)
    {
      JPanel armourTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      armourTypePanel.add(GuiFactory.buildLabel("Armour type:"));
      ItemSelectionListener<ArmourType> armourTypeListener=new ItemSelectionListener<ArmourType>()
      {
        @Override
        public void itemSelected(ArmourType type)
        {
          _armourTypeFilter.setArmourType(type);
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
      line2Panel.add(armourTypePanel);
    }
    // Shield type
    if (_shieldType!=null)
    {
      JPanel shieldTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      shieldTypePanel.add(GuiFactory.buildLabel("Shield type:"));
      ItemSelectionListener<ArmourType> shieldTypeListener=new ItemSelectionListener<ArmourType>()
      {
        @Override
        public void itemSelected(ArmourType type)
        {
          _shieldTypeFilter.setArmourType(type);
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
      line2Panel.add(shieldTypePanel);
    }
    return line2Panel;
  }

  private JPanel buildRequirementsPanel()
  {
    JPanel requirementsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    // Class requirement
    {
      final JCheckBox classCheckbox=_classRequirement.getCheckbox();
      _classRequirement.setSelected(true);
      requirementsPanel.add(classCheckbox);
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean selected=classCheckbox.isSelected();
          _classFilter.setEnabled(selected);
          filterUpdated();
        }
      };
      classCheckbox.addActionListener(l);
    }
    // Proficiencies
    {
      final JCheckBox proficienciesCheckbox=_proficienciesRequirement.getCheckbox();
      _proficienciesRequirement.setSelected(true);
      requirementsPanel.add(proficienciesCheckbox);
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean selected=proficienciesCheckbox.isSelected();
          _proficienciesFilter.setEnabled(selected);
          filterUpdated();
        }
      };
      proficienciesCheckbox.addActionListener(l);
    }
    // Level
    {
      final JCheckBox levelCheckbox=_characterLevelRequirement.getCheckbox();
      _characterLevelRequirement.setSelected(true);
      requirementsPanel.add(levelCheckbox);
      ActionListener l=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          boolean selected=levelCheckbox.isSelected();
          _levelFilter.setEnabled(selected);
          filterUpdated();
        }
      };
      levelCheckbox.addActionListener(l);
    }
    return requirementsPanel;
  }

  /**
   * Release all managed resources.
   */
  @Override
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
      for(ComboBoxController<STAT> statCombo : _stats)
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
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contains=null;
  }
}
