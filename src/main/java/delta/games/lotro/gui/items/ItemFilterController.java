package delta.games.lotro.gui.items;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.filters.ArmourTypeFilter;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
import delta.games.lotro.lore.items.filters.ItemQualityFilter;
import delta.games.lotro.lore.items.filters.WeaponTypeFilter;

/**
 * Controller for a item filter edition panel.
 * @author DAM
 */
public class ItemFilterController extends AbstractItemFilterPanelController
{
  // Data
  private Filter<Item> _filter;
  private ItemNameFilter _nameFilter;
  private ItemQualityFilter _qualityFilter;
  private WeaponTypeFilter _weaponTypeFilter;
  private ArmourTypeFilter _armourTypeFilter;
  private ArmourTypeFilter _shieldTypeFilter;
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

  /**
   * Constructor.
   */
  public ItemFilterController()
  {
    this(new ItemFilterConfiguration());
  }

  /**
   * Constructor.
   * @param cfg Configuration.
   */
  public ItemFilterController(ItemFilterConfiguration cfg)
  {
    _cfg=cfg;
    List<Filter<Item>> filters=new ArrayList<Filter<Item>>();
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
    _filter=new CompoundFilter<Item>(Operator.AND,filters);
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
    // Line 2: weapon type, armour type, shield type
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
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line1Panel,c);
    c=new GridBagConstraints(0,1,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line2Panel,c);

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
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contains=null;
  }
}
