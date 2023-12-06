package delta.games.lotro.gui.lore.items;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.form.LabeledComponent;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.common.colors.ColorDescription;
import delta.games.lotro.common.id.InternalGameId;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.lore.items.ItemSturdiness;
import delta.games.lotro.lore.items.WeaponInstance;
import delta.games.lotro.utils.Formats;

/**
 * Controller for an item instance edition panel.
 * @author DAM
 */
public class ItemInstanceMainAttrsDisplayPanelController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  // GUI
  private JPanel _panel;
  // - Instance ID
  private LabeledComponent<JLabel> _instanceId;
  // - Validity date
  private LabeledComponent<JLabel> _date;
  // - Birth name
  private LabeledComponent<JLabel> _birthName;
  // - Crafter name
  private LabeledComponent<JLabel> _crafterName;
  // - Durability
  private JLabel _durability;
  // - Sturdiness
  private JLabel _sturdiness;
  // - Item level
  private JLabel _itemLevel;
  // - Min level
  private JLabel _minLevel;
  // - Value
  private MoneyDisplayController _value;
  // - Color
  private LabeledComponent<JLabel> _color;
  // - Bound to
  // TODO
  // Weapon specifics
  private JLabel _dps;
  private JLabel _minMaxDamage;
  private JLabel _damageType;
  // - User comments
  private LabeledComponent<JLabel> _userComments;

  /**
   * Constructor.
   * @param itemInstance Item instance.
   */
  public ItemInstanceMainAttrsDisplayPanelController(ItemInstance<? extends Item> itemInstance)
  {
    _itemInstance=itemInstance;
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

  private void initGadgets()
  {
    // - Instance ID
    _instanceId=new LabeledComponent<JLabel>("ID:",GuiFactory.buildLabel(""));
    // Validity date
    _date=new LabeledComponent<JLabel>("Date:",GuiFactory.buildLabel(""));
    // Birth name
    _birthName=new LabeledComponent<JLabel>("Name:",GuiFactory.buildLabel(""));
    // Crafter name
    _crafterName=new LabeledComponent<JLabel>("Crafter:",GuiFactory.buildLabel(""));
    // - Durability
    _durability=GuiFactory.buildLabel("");
    // - Sturdiness
    _sturdiness=GuiFactory.buildLabel("");
    // - Item level
    _itemLevel=GuiFactory.buildLabel("");
    // - Min level
    _minLevel=GuiFactory.buildLabel("");
    // - Value
    _value=new MoneyDisplayController();
    // - Color
    _color=new LabeledComponent<JLabel>("Color:",GuiFactory.buildLabel(""));
    // - Bound to
    // TODO
    // Weapons
    if (_itemInstance instanceof WeaponInstance)
    {
      _dps=GuiFactory.buildLabel("");
      _minMaxDamage=GuiFactory.buildLabel("");
      _damageType=GuiFactory.buildLabel("");
    }
    // - User comments
    _userComments=new LabeledComponent<JLabel>("Comments:",GuiFactory.buildLabel(""));
  }

  private JPanel build()
  {
    initGadgets();
    _panel=buildPanel();
    update();
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Identification line: ID and validity date
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // ID
      panelLine.add(_instanceId.getLabel());
      panelLine.add(_instanceId.getComponent());
      // Date
      panelLine.add(_date.getLabel());
      panelLine.add(_date.getComponent());
    }
    // Birth line: birth name and crafter name
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Birth name
      panelLine.add(_birthName.getLabel());
      panelLine.add(_birthName.getComponent());
      // Crafter name
      panelLine.add(_crafterName.getLabel());
      panelLine.add(_crafterName.getComponent());
    }
    // Level line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Item level
      panelLine.add(GuiFactory.buildLabel("Item level:"));
      panelLine.add(_itemLevel);
      // Minimum level
      panelLine.add(GuiFactory.buildLabel("Required level:"));
      panelLine.add(_minLevel);
    }
    // Durability line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Durability
      panelLine.add(GuiFactory.buildLabel("Durability:"));
      panelLine.add(_durability);
      // Sturdiness
      panelLine.add(_sturdiness);
    }
    // Color and value line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Color
      panelLine.add(_color.getLabel());
      panelLine.add(_color.getComponent());
      // Value
      panelLine.add(GuiFactory.buildLabel("Value:"));
      panelLine.add(_value.getPanel());
    }
    // User comments
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(_userComments.getLabel());
      panelLine.add(_userComments.getComponent());
    }
    // Binding
    // TODO
    // Weapon specifics
    if (_itemInstance instanceof WeaponInstance)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // DPS
      panelLine.add(GuiFactory.buildLabel("DPS:"));
      panelLine.add(_dps);
      // Damage
      panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Damage:"));
      panelLine.add(_minMaxDamage);
      panelLine.add(_damageType);
    }
    return panel;
  }

  /**
   * Update display.
   */
  public void update()
  {
    // Instance ID
    InternalGameId instanceId=_itemInstance.getInstanceId();
    _instanceId.setVisible(instanceId!=null);
    if (instanceId!=null)
    {
      _instanceId.getComponent().setText(instanceId.asDisplayableString());
    }
    // Validity date
    Long time=_itemInstance.getTime();
    _date.setVisible(time!=null);
    if (time!=null)
    {
      String validityDateStr=Formats.getDateTimeString(new Date(time.longValue()));
      _date.getComponent().setText(validityDateStr);
    }
    // Adjust visibility of the parent panel
    _date.getComponent().getParent().setVisible((instanceId!=null)||(time!=null));
    // Birth name
    String birthName=_itemInstance.getBirthName();
    boolean hasName=((birthName!=null) && (birthName.length()>0));
    _birthName.setVisible(hasName);
    if (hasName)
    {
      _birthName.getComponent().setText(birthName);
    }
    // Crafter name
    String crafterName=_itemInstance.getCrafterName();
    boolean hasCrafter=((crafterName!=null) && (crafterName.length()>0));
    _crafterName.setVisible(hasCrafter);
    if (hasCrafter)
    {
      _crafterName.getComponent().setText(crafterName);
    }
    // Adjust visibility of the parent panel
    _crafterName.getComponent().getParent().setVisible(hasName||hasCrafter);
    // - Durability
    String durabilityStr=getDurabilityLabel();
    _durability.setText(durabilityStr);
    // - Sturdiness
    String sturdiness=getSturdinessLabel();
    _sturdiness.setText(sturdiness);
    // - Item level
    Integer itemLevel=_itemInstance.getEffectiveItemLevel();
    String itemLevelStr=(itemLevel!=null)?itemLevel.toString():"-";
    _itemLevel.setText(itemLevelStr);
    // - Min level
    Integer minLevel=_itemInstance.getEffectiveMinLevel();
    _minLevel.setText(minLevel!=null?minLevel.toString():"-");
    // - Value
    Money value=_itemInstance.getEffectiveValue();
    if (value!=null)
    {
      _value.setMoney(value);
    }
    // - Color
    ColorDescription color=_itemInstance.getColor();
    _color.setVisible(color!=null);
    if (color!=null)
    {
      _color.getComponent().setText(color.getName());
    }
    // - Bound to
    // TODO
    // Weapon specifics
    if (_itemInstance instanceof WeaponInstance)
    {
      WeaponInstance<?> wi=(WeaponInstance<?>)_itemInstance;
      // DPS
      float dps=wi.getEffectiveDPS();
      String dpsStr=L10n.getString(dps,1);
      _dps.setText(dpsStr);
      // Damage
      int minDamage=wi.getEffectiveMinDamage();
      int maxDamage=wi.getEffectiveMaxDamage();
      String damageStr=minDamage+" - "+maxDamage;
      _minMaxDamage.setText(damageStr);
      DamageType damageType=wi.getEffectiveDamageType();
      String damageTypeStr=(damageType!=null)?damageType.getLabel():"";
      _damageType.setText(damageTypeStr);
    }
    // - User comments
    String userComments=_itemInstance.getProperty(ItemPropertyNames.USER_COMMENT);
    boolean hasUserComments=(userComments!=null);
    _userComments.setVisible(hasUserComments);
    // Adjust visibility of the parent panel
    _userComments.getComponent().getParent().setVisible(hasUserComments);
    if (hasUserComments)
    {
      _userComments.getComponent().setText(userComments);
    }
  }

  private String getDurabilityLabel()
  {
    Integer instanceDurability=_itemInstance.getEffectiveDurability();
    Item reference=_itemInstance.getReference();
    Integer durability=(reference!=null)?reference.getDurability():null;
    String durabilityStr="-";
    if (durability!=null)
    {
      durabilityStr=durability.toString();
      if ((instanceDurability!=null) && (!instanceDurability.equals(durability)))
      {
        durabilityStr=instanceDurability.toString()+"/"+durabilityStr;
      }
    }
    return durabilityStr;
  }

  private String getSturdinessLabel()
  {
    String ret="";
    Item reference=_itemInstance.getReference();
    ItemSturdiness sturdiness=reference.getSturdiness();
    String label=(sturdiness!=null)?sturdiness.getLabel():null;
    if (label!=null)
    {
      ret="("+label+")";
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _itemInstance=null;
    // UI/controllers
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // - Instance ID
    if (_instanceId!=null)
    {
      _instanceId.dispose();
      _instanceId=null;
    }
    // - Validity date
    if (_date!=null)
    {
      _date.dispose();
      _date=null;
    }
    // - Birth name
    if (_birthName!=null)
    {
      _birthName.dispose();
      _birthName=null;
    }
    // - Crafter name
    if (_crafterName!=null)
    {
      _crafterName.dispose();
      _crafterName=null;
    }
    // - Durability
    _durability=null;
    // - Sturdiness
    _sturdiness=null;
    // - Item level
    _itemLevel=null;
    // - Min level
    _minLevel=null;
    // - Value
    if (_value!=null)
    {
      _value.dispose();
      _value=null;
    }
    // - Color
    if (_color!=null)
    {
      _color.dispose();
      _color=null;
    }
    // - Bound to
    // TODO
    // Weapon specifics
    _dps=null;
    _minMaxDamage=null;
    _damageType=null;
    // - User comments
    if (_userComments!=null)
    {
      _userComments.dispose();
      _userComments=null;
    }
  }
}
