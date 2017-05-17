package delta.games.lotro.gui.items;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.gui.character.stats.StatsEditionPanelController;
import delta.games.lotro.gui.items.essences.EssencesEditionPanelController;
import delta.games.lotro.gui.items.relics.RelicsEditionPanelController;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemBinding;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemSturdiness;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.essences.EssencesSet;
import delta.games.lotro.lore.items.legendary.Legendary;
import delta.games.lotro.lore.items.legendary.LegendaryAttrs;
import delta.games.lotro.utils.gui.WindowController;
import delta.games.lotro.utils.gui.combobox.ComboBoxController;
import delta.games.lotro.utils.gui.text.FloatEditionController;
import delta.games.lotro.utils.gui.text.IntegerEditionController;

/**
 * Controller for an item edition panel.
 * @author DAM
 */
public class ItemEditionPanelController
{
  // Data
  private Item _item;
  // GUI
  private JPanel _panel;
  private WindowController _parent;

  private JLabel _icon;
  private ComboBoxController<EquipmentLocation> _slot;
  private JTextField _name;
  private StatsEditionPanelController _stats;
  private EssencesEditionPanelController _essencesEditor;
  private IntegerEditionController _itemLevel;
  private IntegerEditionController _minLevel;
  // character class requirement
  private JTextArea _description;
  private JTextField _subCategory;
  private JTextField _birthName;
  private JTextField _crafterName;
  private ComboBoxController<ItemBinding> _binding;
  private JCheckBox _unique; 
  private IntegerEditionController _durability;
  private ComboBoxController<ItemSturdiness> _sturdiness;
  private IntegerEditionController _stackMax;
  // money
  private ComboBoxController<ItemQuality> _quality;

  // Armour attributes
  private JPanel _armourPanel;
  private IntegerEditionController _armourValue;
  private ComboBoxController<ArmourType> _armourType;

  // Weapon attributes
  private JPanel _weaponPanel;
  private IntegerEditionController _minDamage;
  private IntegerEditionController _maxDamage;
  private ComboBoxController<DamageType> _damageType;
  private FloatEditionController _dps;
  private ComboBoxController<WeaponType> _weaponType;

  private JTabbedPane _tabbedPane;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public ItemEditionPanelController(WindowController parent)
  {
    _parent=parent;
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
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));

    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine);
      // Icon
      _icon=GuiFactory.buildIconLabel(null);
      panelLine.add(_icon);
      // Name
      _name=GuiFactory.buildTextField("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      _name.setColumns(25);
      panelLine.add(_name);
      // Slot
      _slot=buildSlotCombo();
      panelLine.add(_slot.getComboBox());
      // Unicity
      _unique=GuiFactory.buildCheckbox("Unique");
      panelLine.add(_unique);
    }

    // Armour specifics line
    {
      _armourPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(_armourPanel);
      _armourPanel.setVisible(false);
      // Armour
      JTextField armourValue=GuiFactory.buildTextField("");
      _armourValue=new IntegerEditionController(armourValue);
      _armourValue.setValueRange(Integer.valueOf(0),Integer.valueOf(5000));
      _armourPanel.add(GuiFactory.buildLabel("Armour:"));
      _armourPanel.add(_armourValue.getTextField());
      // Armour Type
      _armourType=buildArmourTypeCombo();
      _armourPanel.add(GuiFactory.buildLabel("Armour Type:"));
      _armourPanel.add(_armourType.getComboBox());
    }

    // Weapon specifics line
    {
      _weaponPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(_weaponPanel);
      _weaponPanel.setVisible(false);

      // Weapon type
      _weaponType=buildWeaponTypeCombo();
      _weaponPanel.add(GuiFactory.buildLabel("Type:"));
      _weaponPanel.add(_weaponType.getComboBox());

      // Damage
      JTextField minDamage=GuiFactory.buildTextField("");
      _minDamage=new IntegerEditionController(minDamage,3);
      _minDamage.setValueRange(Integer.valueOf(0),Integer.valueOf(1000));
      _weaponPanel.add(GuiFactory.buildLabel("Damage:"));
      _weaponPanel.add(_minDamage.getTextField());
      _weaponPanel.add(GuiFactory.buildLabel("-"));
      JTextField maxDamage=GuiFactory.buildTextField("");
      _maxDamage=new IntegerEditionController(maxDamage,3);
      _maxDamage.setValueRange(Integer.valueOf(0),Integer.valueOf(1000));
      _weaponPanel.add(_maxDamage.getTextField());

      // Damage type
      _damageType=buildDamageTypeCombo();
      _weaponPanel.add(_damageType.getComboBox());

      // DPS
      JTextField dps=GuiFactory.buildTextField("");
      _dps=new FloatEditionController(dps);
      _dps.setValueRange(Float.valueOf(0),Float.valueOf(1000));
      _weaponPanel.add(GuiFactory.buildLabel("DPS:"));
      _weaponPanel.add(_dps.getTextField());
    }

    // Level and binding line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine);
      // Item level
      JTextField itemLevel=GuiFactory.buildTextField("");
      _itemLevel=new IntegerEditionController(itemLevel);
      _itemLevel.setValueRange(Integer.valueOf(1),Integer.valueOf(500));
      panelLine.add(GuiFactory.buildLabel("Item level:"));
      panelLine.add(_itemLevel.getTextField());
      // Minimum level
      JTextField minLevel=GuiFactory.buildTextField("");
      _minLevel=new IntegerEditionController(minLevel);
      _minLevel.setValueRange(Integer.valueOf(1),Integer.valueOf(150));
      panelLine.add(GuiFactory.buildLabel("Required level:"));
      panelLine.add(_minLevel.getTextField());
      // Binding
      _binding=buildBindingCombo();
      panelLine.add(GuiFactory.buildLabel("Binding:"));
      panelLine.add(_binding.getComboBox());
    }

    // Durability/sturdiness/quality line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine);
      // Durability
      JTextField durability=GuiFactory.buildTextField("");
      _durability=new IntegerEditionController(durability,3);
      _durability.setValueRange(Integer.valueOf(1),Integer.valueOf(150));
      panelLine.add(GuiFactory.buildLabel("Durability:"));
      panelLine.add(_durability.getTextField());
      // Sturdiness
      _sturdiness=buildSturdinessCombo();
      panelLine.add(GuiFactory.buildLabel("Sturdiness:"));
      panelLine.add(_sturdiness.getComboBox());
      // Quality
      _quality=ItemUiTools.buildQualityCombo();
      panelLine.add(GuiFactory.buildLabel("Quality:"));
      panelLine.add(_quality.getComboBox());
      // Stack max
      JTextField stackMax=GuiFactory.buildTextField("");
      _stackMax=new IntegerEditionController(stackMax);
      _stackMax.setValueRange(Integer.valueOf(1),Integer.valueOf(1000));
      panelLine.add(GuiFactory.buildLabel("Stack:"));
      panelLine.add(_stackMax.getTextField());
    }

    // Contextual data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine);
      // Birth name
      _birthName=GuiFactory.buildTextField("");
      _birthName.setColumns(20);
      panelLine.add(GuiFactory.buildLabel("Name:"));
      panelLine.add(_birthName);
      // Crafter name
      _crafterName=GuiFactory.buildTextField("");
      _crafterName.setColumns(20);
      panelLine.add(GuiFactory.buildLabel("Crafter:"));
      panelLine.add(_crafterName);
    }

    // Unused
    {
      // Sub-category
      _subCategory=GuiFactory.buildTextField("");
      _subCategory.setColumns(20);
      //panel.add(GuiFactory.buildLabel("Category:"));
      //panel.add(_subCategory);
    }

    // Tabbed pane at the bottom
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    // Description
    JPanel descriptionPanel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _description=GuiFactory.buildTextArea("",false);
    _description.setColumns(40);
    _description.setLineWrap(true);
    descriptionPanel.add(_description,BorderLayout.CENTER);
    // Stats
    _stats=new StatsEditionPanelController();
    // Essences
    _essencesEditor=new EssencesEditionPanelController(_parent);

    // Tabbed pane assembly
    tabbedPane.add("Stats",_stats.getPanel());
    tabbedPane.add("Description",descriptionPanel);
    tabbedPane.add("Essences",_essencesEditor.getPanel());
    panel.add(tabbedPane);
    _tabbedPane=tabbedPane;

    return panel;
  }

  /**
   * Set the item to display.
   * @param item Item to display.
   */
  public void setItem(Item item)
  {
    // Ensure panel is built!
    getPanel();
    String name=item.getName();
    _parent.setTitle(name);
    // Icon
    String iconId=item.getProperty(ItemPropertyNames.ICON_ID);
    String backgroundIconId=item.getProperty(ItemPropertyNames.BACKGROUND_ICON_ID);
    ImageIcon icon=IconsManager.getItemIcon(iconId,backgroundIconId);
    _icon.setIcon(icon);
    // Name
    _name.setText(name);
    // Slot
    _slot.selectItem(item.getEquipmentLocation());
    // Stats
    _stats.initFromStats(item.getStats());
    // Item level
    Integer itemLevel=item.getItemLevel();
    _itemLevel.setValue(itemLevel);
    // Minimum level
    Integer minLevel=item.getMinLevel();
    _minLevel.setValue(minLevel);
    // Description
    _description.setText(item.getDescription());
    // Sub category
    _subCategory.setText(item.getSubCategory());
    // Birth name
    _birthName.setText(item.getBirthName());
    // Crafter name
    _crafterName.setText(item.getCrafterName());
    // Binding
    _binding.selectItem(item.getBinding());
    // Unicity
    _unique.setSelected(item.isUnique());
    // Durability
    _durability.setValue(item.getDurability());
    // Sturdiness
    _sturdiness.selectItem(item.getSturdiness());
    // Stack max
    _stackMax.setValue(item.getStackMax());
    // Quality
    _quality.selectItem(item.getQuality());

    // Armour specifics
    if (item instanceof Armour)
    {
      Armour armour=(Armour)item;
      _armourValue.setValue(Integer.valueOf(armour.getArmourValue()));
      _armourType.selectItem(armour.getArmourType());
      _armourPanel.setVisible(true);
    }

    // Weapon specifics
    if (item instanceof Weapon)
    {
      Weapon weapon=(Weapon)item;
      _minDamage.setValue(Integer.valueOf(weapon.getMinDamage()));
      _maxDamage.setValue(Integer.valueOf(weapon.getMaxDamage()));
      _dps.setValue(Float.valueOf(weapon.getDPS()));
      _weaponType.selectItem(weapon.getWeaponType());
      _damageType.selectItem(weapon.getDamageType());
      _weaponPanel.setVisible(true);
    }

    // Essences
    _essencesEditor.initFromItem(item);

    // Legendary specifics
    // Relics
    JPanel relicsPanel=null;
    if (item instanceof Legendary)
    {
      Legendary legItem=(Legendary)item;
      LegendaryAttrs attrs=legItem.getLegendaryAttrs();
      RelicsEditionPanelController relicEditor=new RelicsEditionPanelController(_parent,attrs);
      relicsPanel=relicEditor.getPanel();
      _tabbedPane.add("Relics",relicsPanel);
    }
    _item=item;
  }

  /**
   * Get the current value of the edited item.
   * @return An item.
   */
  public Item getItem()
  {
    // Name
    _item.setName(_name.getText());
    // Slot
    _item.setEquipmentLocation(_slot.getSelectedItem());
    // Stats
    BasicStatsSet stats=_item.getStats();
    stats.clear();
    stats.setStats(_stats.getStats());
    // Item level
    _item.setItemLevel(_itemLevel.getValue());
    // Minimum level
    _item.setMinLevel(_minLevel.getValue());
    // Description
    _item.setDescription(_description.getText());
    // Sub category
    _item.setSubCategory(_subCategory.getText());
    // Birth name
    _item.setBirthName(_birthName.getText());
    // Crafter name
    _item.setCrafterName(_crafterName.getText());
    // Binding
    _item.setBinding(_binding.getSelectedItem());
    // Unicity
    _item.setUnique(_unique.isSelected());
    // Durability
    _item.setDurability(_durability.getValue());
    // Sturdiness
    _item.setSturdiness(_sturdiness.getSelectedItem());
    // Stack max
    _item.setStackMax(_stackMax.getValue());
    // Quality
    _item.setQuality(_quality.getSelectedItem());
    // Essences
    List<Item> selectedEssences=_essencesEditor.getEssences();
    EssencesSet essences=null;
    if (selectedEssences.size()>0)
    {
      essences=new EssencesSet(selectedEssences.size());
      for(int i=0;i<selectedEssences.size();i++)
      {
        essences.setEssence(i,selectedEssences.get(i));
      }
    }
    _item.setEssences(essences);
    // Armour specifics
    if (_item instanceof Armour)
    {
      Armour armour=(Armour)_item;
      Integer armourValue=_armourValue.getValue();
      if (armourValue!=null)
      {
        armour.setArmourValue(armourValue.intValue());
      }
      armour.setArmourType(_armourType.getSelectedItem());
    }

    // Armour specifics
    if (_item instanceof Weapon)
    {
      Weapon weapon=(Weapon)_item;
      Integer minDamage=_minDamage.getValue();
      if (minDamage!=null)
      {
        weapon.setMinDamage(minDamage.intValue());
      }
      Integer maxDamage=_maxDamage.getValue();
      if (maxDamage!=null)
      {
        weapon.setMaxDamage(maxDamage.intValue());
      }
      Float dps=_dps.getValue();
      if (dps!=null)
      {
        weapon.setDPS(dps.floatValue());
      }
      weapon.setDamageType(_damageType.getSelectedItem());
      weapon.setWeaponType(_weaponType.getSelectedItem());
    }
    return _item;
  }

  private ComboBoxController<EquipmentLocation> buildSlotCombo()
  {
    ComboBoxController<EquipmentLocation> ctrl=new ComboBoxController<EquipmentLocation>();
    ctrl.addEmptyItem("");
    for(EquipmentLocation slot : EquipmentLocation.getAll())
    {
      ctrl.addItem(slot,slot.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private ComboBoxController<ItemBinding> buildBindingCombo()
  {
    ComboBoxController<ItemBinding> ctrl=new ComboBoxController<ItemBinding>();
    ctrl.addEmptyItem("");
    for(ItemBinding binding : ItemBinding.values())
    {
      ctrl.addItem(binding,binding.name());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private ComboBoxController<ItemSturdiness> buildSturdinessCombo()
  {
    ComboBoxController<ItemSturdiness> ctrl=new ComboBoxController<ItemSturdiness>();
    ctrl.addEmptyItem("");
    for(ItemSturdiness sturdiness : ItemSturdiness.values())
    {
      ctrl.addItem(sturdiness,sturdiness.name());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private ComboBoxController<ArmourType> buildArmourTypeCombo()
  {
    ComboBoxController<ArmourType> ctrl=new ComboBoxController<ArmourType>();
    ctrl.addEmptyItem("");
    for(ArmourType quality : ArmourType.getAll())
    {
      ctrl.addItem(quality,quality.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private ComboBoxController<DamageType> buildDamageTypeCombo()
  {
    ComboBoxController<DamageType> ctrl=new ComboBoxController<DamageType>();
    ctrl.addEmptyItem("");
    for(DamageType damageType : DamageType.getAll())
    {
      ctrl.addItem(damageType,damageType.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private ComboBoxController<WeaponType> buildWeaponTypeCombo()
  {
    ComboBoxController<WeaponType> ctrl=new ComboBoxController<WeaponType>();
    ctrl.addEmptyItem("");
    for(WeaponType weaponType : WeaponType.getAll())
    {
      ctrl.addItem(weaponType,weaponType.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
