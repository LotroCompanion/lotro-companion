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
  // GUI
  private JPanel _panel;
  private JTextField _contains;
  // Controllers
  private DynamicTextEditionController _textController;
  private ComboBoxController<ItemQuality> _quality;
  private ComboBoxController<WeaponType> _weaponType;
  private ComboBoxController<ArmourType> _armourType;

  /**
   * Constructor.
   */
  public ItemFilterController()
  {
    _nameFilter=new ItemNameFilter();
    _qualityFilter=new ItemQualityFilter(null);
    _weaponTypeFilter=new WeaponTypeFilter(null);
    _armourTypeFilter=new ArmourTypeFilter(null);
    List<Filter<Item>> filters=new ArrayList<Filter<Item>>();
    filters.add(_nameFilter);
    filters.add(_qualityFilter);
    filters.add(_weaponTypeFilter);
    filters.add(_armourTypeFilter);
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
    WeaponType weaponType=_weaponTypeFilter.getWeaponType();
    _weaponType.selectItem(weaponType);
    // Armour type
    ArmourType armourType=_armourTypeFilter.getArmourType();
    _armourType.selectItem(armourType);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Quality
    {
      JPanel qualityPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      qualityPanel.add(GuiFactory.buildLabel("Quality:"));
      _quality=ItemUiTools.buildQualityCombo();
      ItemSelectionListener<ItemQuality> qualityListener=new ItemSelectionListener<ItemQuality>()
      {
        public void itemSelected(ItemQuality quality)
        {
          _qualityFilter.setQuality(quality);
          filterUpdated();
        }
      };
      _quality.addListener(qualityListener);
      qualityPanel.add(_quality.getComboBox());
      GridBagConstraints c=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(qualityPanel,c);
    }
    // Weapon type
    {
      JPanel weaponTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      weaponTypePanel.add(GuiFactory.buildLabel("Weapon type:"));
      _weaponType=ItemUiTools.buildWeaponTypeCombo();
      ItemSelectionListener<WeaponType> weaponTypeListener=new ItemSelectionListener<WeaponType>()
      {
        public void itemSelected(WeaponType type)
        {
          _weaponTypeFilter.setWeaponType(type);
          filterUpdated();
        }
      };
      _weaponType.addListener(weaponTypeListener);
      weaponTypePanel.add(_weaponType.getComboBox());
      GridBagConstraints c=new GridBagConstraints(2,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(weaponTypePanel,c);
    }
    // Armour type
    {
      JPanel armourTypePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      armourTypePanel.add(GuiFactory.buildLabel("Armour type:"));
      _armourType=ItemUiTools.buildArmourTypeCombo();
      ItemSelectionListener<ArmourType> ArmourTypeListener=new ItemSelectionListener<ArmourType>()
      {
        public void itemSelected(ArmourType type)
        {
          _armourTypeFilter.setArmourType(type);
          filterUpdated();
        }
      };
      _armourType.addListener(ArmourTypeListener);
      armourTypePanel.add(_armourType.getComboBox());
      GridBagConstraints c=new GridBagConstraints(3,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(armourTypePanel,c);
    }
    // Label filter
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
        _nameFilter.setPattern(newText);
        filterUpdated();
      }
    };
    _textController=new DynamicTextEditionController(_contains,listener);
    GridBagConstraints c=new GridBagConstraints(0,1,3,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(containsPanel,c);
    return panel;
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
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contains=null;
  }
}
