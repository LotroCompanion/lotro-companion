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
import delta.common.ui.swing.text.range.RangeEditorController;
import delta.common.ui.swing.text.range.RangeListener;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.filters.ArmourTypeFilter;
import delta.games.lotro.lore.items.filters.WeaponTypeFilter;

/**
 * Controller for a item filter edition panel.
 * @author DAM
 */
public class ItemFilterController extends AbstractItemFilterPanelController
{
  // Data
  private ItemChooserFilter _filter;
  private TypedProperties _props;
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
  private RangeEditorController _itemLevelRange;

  /**
   * Constructor.
   */
  public ItemFilterController()
  {
    this(new ItemFilterConfiguration(),null,null);
  }

  /**
   * Constructor.
   * @param cfg Configuration.
   * @param character Targeted character (may be <code>null</code>).
   * @param props Filter state.
   */
  public ItemFilterController(ItemFilterConfiguration cfg, CharacterData character, TypedProperties props)
  {
    _filter=new ItemChooserFilter(cfg,character);
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
    // Name
    String contains=_filter.getNameFilter().getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Quality
    ItemQuality quality=_filter.getQualityFilter().getQuality();
    _quality.selectItem(quality);
    // Weapon type
    WeaponTypeFilter weaponTypeFilter=_filter.getWeaponTypeFilter();
    if (weaponTypeFilter!=null)
    {
      WeaponType weaponType=weaponTypeFilter.getWeaponType();
      _weaponType.selectItem(weaponType);
    }
    // Armour type
    ArmourTypeFilter armourTypeFilter=_filter.getArmourTypeFilter();
    if (armourTypeFilter!=null)
    {
      ArmourType armourType=armourTypeFilter.getArmourType();
      _armourType.selectItem(armourType);
    }
    // Shield type
    ArmourTypeFilter shieldTypeFilter=_filter.getShieldTypeFilter();
    if (shieldTypeFilter!=null)
    {
      ArmourType shieldType=shieldTypeFilter.getArmourType();
      _shieldType.selectItem(shieldType);
    }
    // Stats
    // TODO
    // Proficiencies
    // TODO
    // Item level range
    // TODO
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
    // Line 4: item level range
    JPanel itemLevelRangePanel=buildItemLevelRangePanel();
    c=new GridBagConstraints(0,4,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(itemLevelRangePanel,c);
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
          _filter.getQualityFilter().setQuality(quality);
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
          _filter.getNameFilter().setPattern(newText);
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
      for(int i=0;i<ItemChooserFilter.NB_STATS;i++)
      {
        statPanel.add(GuiFactory.buildLabel("Stat:"));
        ComboBoxController<STAT> statChooser=ItemUiTools.buildStatChooser();
        final int statIndex=i;
        ItemSelectionListener<STAT> statListener=new ItemSelectionListener<STAT>()
        {
          @Override
          public void itemSelected(STAT stat)
          {
            _filter.getStatFilter().setStat(statIndex,stat);
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
    final WeaponTypeFilter weaponTypeFilter=_filter.getWeaponTypeFilter();
    if (weaponTypeFilter!=null)
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
      line2Panel.add(weaponTypePanel);
    }
    // Armour type
    final ArmourTypeFilter armourTypeFilter=_filter.getArmourTypeFilter();
    if (armourTypeFilter!=null)
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
      line2Panel.add(armourTypePanel);
    }
    // Shield type
    final ArmourTypeFilter shieldTypeFilter=_filter.getShieldTypeFilter();
    if (shieldTypeFilter!=null)
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
      line2Panel.add(shieldTypePanel);
    }
    return line2Panel;
  }

  private JPanel buildRequirementsPanel()
  {
    JPanel requirementsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    // Class requirement
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

  private JPanel buildItemLevelRangePanel()
  {
    List<Integer> itemLevels=_filter.getConfiguration().getItemLevels();
    _itemLevelRange=new RangeEditorController();
    JPanel gadgetsPanel=_itemLevelRange.getPanel();
    _itemLevelRange.setRangeValues(itemLevels);
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    panel.add(GuiFactory.buildLabel("Item Level: "));
    panel.add(gadgetsPanel);
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
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    ItemChooserFilterIo.saveTo(_filter,_props);
    _props=null;
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
