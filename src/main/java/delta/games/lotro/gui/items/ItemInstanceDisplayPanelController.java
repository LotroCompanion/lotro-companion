package delta.games.lotro.gui.items;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.text.dates.DateCodec;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.colors.ColorDescription;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.stats.WellKnownStat;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.id.ItemInstanceIdEditionPanelController;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourInstance;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.utils.DateFormat;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for an item instance edition panel.
 * @author DAM
 */
public class ItemInstanceDisplayPanelController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  // GUI
  private JPanel _panel;
  private WindowController _parent;
  // - Item identification (icon+name)
  private JLabel _icon;
  private JLabel _name;
  // - Instance ID
  private ItemInstanceIdEditionPanelController _instanceId;
  // - Validity date
  private JLabel _date;
  // - Birth name
  private JLabel _birthName;
  // - Crafter name
  private JLabel _crafterName;
  // - Durability
  private JLabel _durability;
  // - Item level
  private JLabel _itemLevel;
  // - Min level
  private JLabel _minLevel;
  // - Value
  private MoneyDisplayController _value;
  // - Color => only for armours?
  private JLabel _color;
  // - Bound to
  // TODO
  // - User comments
  private JLabel _userComments;

  // Armour specifics
  private JPanel _armourPanel;
  private JLabel _armourValue;

  // Item model data:
  // - slot
  // - category?
  // - binding
  // - unique
  // - sturdiness
  // - max level
  // - requirements: class, and later: race, faction, glory rank, trait
  // - description
  // - value
  // - stack max
  // - quality

  /**
   * Constructor.
   * @param parent Parent window.
   * @param itemInstance Item instance.
   */
  public ItemInstanceDisplayPanelController(WindowController parent, ItemInstance<? extends Item> itemInstance)
  {
    _parent=parent;
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
    // Item identification (icon+name)
    // - icon
    _icon=GuiFactory.buildIconLabel(null);
    // - name
    _name=GuiFactory.buildLabel("");
    _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
    // - Instance ID
    _instanceId=new ItemInstanceIdEditionPanelController();
    // Validity date
    _date=GuiFactory.buildLabel("");
    // Birth name
    _birthName=GuiFactory.buildLabel("");
    // Crafter name
    _crafterName=GuiFactory.buildLabel("");
    // - Durability
    _durability=GuiFactory.buildLabel("");
    // - Item level
    _itemLevel=GuiFactory.buildLabel("");
    // - Min level
    _minLevel=GuiFactory.buildLabel("");
    // - Value
    _value=new MoneyDisplayController();
    // - Color
    _color=GuiFactory.buildLabel("");
    // - Bound to
    // TODO
    // - User comments
    _userComments=GuiFactory.buildLabel("");

    // Armour specifics
    if (_itemInstance instanceof ArmourInstance)
    {
      _armourPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Armour value
      _armourValue=GuiFactory.buildLabel("");
    }
  }

  private JPanel build()
  {
    initGadgets();
    return buildPanel();
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Icon
      panelLine.add(_icon);
      // Name
      panelLine.add(_name);
    }
    // Identification line: ID and validity date
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // ID
      panelLine.add(GuiFactory.buildLabel("ID:"));
      panelLine.add(_instanceId.getPanel());
      // Date
      panelLine.add(GuiFactory.buildLabel("Date:"));
      panelLine.add(_date);
    }
    // Birth line: birth name and crafter name
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Birth name
      panelLine.add(GuiFactory.buildLabel("Name:"));
      panelLine.add(_birthName);
      // Crafter name
      panelLine.add(GuiFactory.buildLabel("Crafter:"));
      panelLine.add(_crafterName);
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
    }
    // Color and value line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Color
      panelLine.add(GuiFactory.buildLabel("Color:"));
      panelLine.add(_color);
      // Value
      panelLine.add(GuiFactory.buildLabel("Value:"));
      panelLine.add(_value.getPanel());
    }
    // Armour specifics line
    if (_itemInstance instanceof ArmourInstance)
    {
      panel.add(_armourPanel,c);
      c.gridy++;
      // Armour value
      _armourPanel.add(GuiFactory.buildLabel("Armour:"));
      _armourPanel.add(_armourValue);
    }
    // User comments
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Comments:"));
      panelLine.add(_userComments);
    }

    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(panel,c);

    _panel=ret;
    setItem();
    return ret;
  }

  /**
   * Set the item to display.
   */
  private void setItem()
  {
    Item item=_itemInstance.getReference();
    // Title
    String name=item.getName();
    if (_parent!=null)
    {
      _parent.setTitle(name);
    }
    // Item identification (icon+name)
    // - icon
    String iconPath=item.getIcon();
    ImageIcon icon=LotroIconsManager.getItemIcon(iconPath);
    _icon.setIcon(icon);
    // - name
    _name.setText(name);
    // - Instance ID
    _instanceId.setInstanceId(_itemInstance.getInstanceId());
    // Validity date
    String validityDateStr="-";
    Long time=_itemInstance.getTime();
    if (time!=null)
    {
      DateCodec codec=DateFormat.getDateTimeCodec();
      validityDateStr=codec.formatDate(time);
    }
    _date.setText(validityDateStr);
    // Birth name
    String birthName=_itemInstance.getBirthName();
    if ((birthName==null) || (birthName.length()==0))
    {
      birthName="-";
    }
    _birthName.setText(birthName);
    // Crafter name
    String crafterName=_itemInstance.getCrafterName();
    if ((crafterName==null) || (crafterName.length()==0))
    {
      crafterName="-";
    }
    _crafterName.setText(crafterName);
    // - Own stats
    setStats(_itemInstance.getEffectiveOwnStats());
    // - Durability
    String durabilityStr=getDurabilityLabel();
    _durability.setText(durabilityStr);
    // - Item level
    Integer itemLevel=_itemInstance.getEffectiveItemLevel();
    String itemLevelStr=(itemLevel!=null)?itemLevel.toString():"-";
    _itemLevel.setText(itemLevelStr);
    // - Min level
    Integer minLevel=_itemInstance.getEffectiveMinLevel();
    _minLevel.setText(minLevel!=null?minLevel.toString():"-");
    // - Value
    Money value=_itemInstance.getValue();
    if (value!=null)
    {
      _value.setMoney(value);
    }
    // - Color
    ColorDescription color=_itemInstance.getColor();
    _color.setText(color!=null?color.getName():"-");
    // - Bound to
    // TODO
    // - User comments
    String userComments=_itemInstance.getProperty(ItemPropertyNames.USER_COMMENT);
    if (userComments==null) userComments="";
    _userComments.setText(userComments);
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

  private void setStats(BasicStatsSet stats)
  {
    Item item=_itemInstance.getReference();
    if (item instanceof Armour)
    {
      FixedDecimalsInteger armourValue=stats.getStat(WellKnownStat.ARMOUR);
      if (armourValue!=null)
      {
        _armourValue.setText(armourValue.toString());
      }
    }
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
    _parent=null;
    // - Item identification (icon+name)
    _icon=null;
    _name=null;
    // - Instance ID
    if (_instanceId!=null)
    {
      _instanceId.dispose();
      _instanceId=null;
    }
    // - Validity date
    _date=null;
    // - Birth name
    _birthName=null;
    // - Crafter name
    _crafterName=null;
    // - Durability
    _durability=null;
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
    _color=null;
    // - Bound to
    // TODO
    // - User comments
    _userComments=null;

    // Armour specifics
    _armourPanel=null;
    _armourValue=null;
  }
}
