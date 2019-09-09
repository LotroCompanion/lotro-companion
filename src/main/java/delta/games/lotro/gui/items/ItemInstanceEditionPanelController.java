package delta.games.lotro.gui.items;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.ui.swing.text.dates.DateCodec;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.NumericTools;
import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.colors.ColorDescription;
import delta.games.lotro.common.colors.ColorsManager;
import delta.games.lotro.common.id.ItemInstanceId;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.common.stats.WellKnownStat;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.character.stats.StatsEditionPanelController;
import delta.games.lotro.gui.common.id.ItemInstanceIdEditionPanelController;
import delta.games.lotro.gui.common.money.MoneyEditionPanelController;
import delta.games.lotro.gui.items.essences.EssencesEditionPanelController;
import delta.games.lotro.gui.items.legendary.relics.RelicsEditionPanelController;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourInstance;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.lore.items.essences.EssencesSet;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.scaling.Munging;
import delta.games.lotro.utils.DateFormat;
import delta.games.lotro.utils.FixedDecimalsInteger;
import delta.games.lotro.utils.maths.Progression;

/**
 * Controller for an item instance edition panel.
 * @author DAM
 */
public class ItemInstanceEditionPanelController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  private Munging _munging;
  private CharacterSummary _character;
  // GUI
  private JPanel _panel;
  private WindowController _parent;
  // - Item identification (icon+name)
  private JLabel _icon;
  private JTextField _name;
  // - Instance ID
  private ItemInstanceIdEditionPanelController _instanceId;
  // - Validity date
  private DateEditionController _date;
  // - Birth name
  private JTextField _birthName;
  // - Crafter name
  private JTextField _crafterName;
  // - Own stats
  private StatsEditionPanelController _stats;
  // - Essences
  private EssencesEditionPanelController _essencesEditor;
  // - Durability
  private IntegerEditionController _durability;
  // - Scaling level
  private JLabel _mungingLabel;
  private ComboBoxController<Integer> _mungingLevel;
  // - Item level
  private JTextField _itemLevel;
  private DynamicTextEditionController _itemLevelController;
  // - Min level
  private IntegerEditionController _minLevel;
  // - Value
  private MoneyEditionPanelController _value;
  // - Color
  private ComboBoxController<ColorDescription> _color;
  // - Bound to
  // TODO
  // - User comments
  private JTextField _userComments;

  // Armour specifics
  private JPanel _armourPanel;
  private IntegerEditionController _armourValue;

  // Legendary specifics
  private RelicsEditionPanelController _relicsEditor;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Character data.
   * @param itemInstance Item instance.
   */
  public ItemInstanceEditionPanelController(WindowController parent, CharacterSummary character, ItemInstance<? extends Item> itemInstance)
  {
    _parent=parent;
    _character=character;
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
    _name=GuiFactory.buildTextField("");
    _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
    _name.setColumns(25);
    _name.setEditable(false);
    // - Instance ID
    _instanceId=new ItemInstanceIdEditionPanelController();
    // Validity date
    DateCodec codec=DateFormat.getDateTimeCodec();
    _date=new DateEditionController(codec);
    // Birth name
    _birthName=GuiFactory.buildTextField("");
    _birthName.setColumns(20);
    // Crafter name
    _crafterName=GuiFactory.buildTextField("");
    _crafterName.setColumns(20);
    // - Own stats
    _stats=new StatsEditionPanelController();
    // - Essences
    _essencesEditor=new EssencesEditionPanelController(_parent,_character);
    // - Durability
    JTextField durability=GuiFactory.buildTextField("");
    _durability=new IntegerEditionController(durability,3);
    _durability.setValueRange(Integer.valueOf(1),Integer.valueOf(150));
    // - Scaling level
    _mungingLabel=GuiFactory.buildLabel("Scaling level:");
    _mungingLevel=new ComboBoxController<Integer>(false,Integer.class);
    // - Item level
    _itemLevel=GuiFactory.buildTextField("");
    _itemLevel.setColumns(4);
    // - Min level
    JTextField minLevel=GuiFactory.buildTextField("");
    _minLevel=new IntegerEditionController(minLevel,3);
    int maxLevel=Config.getInstance().getMaxCharacterLevel();
    _minLevel.setValueRange(Integer.valueOf(1),Integer.valueOf(maxLevel));
    // - Value
    _value=new MoneyEditionPanelController();
    // - Color
    _color=buildColorCombo();
    // - Bound to
    // TODO
    // - User comments
    _userComments=GuiFactory.buildTextField("");
    _userComments.setColumns(40);

    // Armour specifics
    if (_itemInstance instanceof ArmourInstance)
    {
      _armourPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Armour value
      JTextField armourValue=GuiFactory.buildTextField("");
      _armourValue=new IntegerEditionController(armourValue);
      _armourValue.setValueRange(Integer.valueOf(0),Integer.valueOf(100000));
    }

    // Legendary specifics
    // - relics
    if (_itemInstance instanceof LegendaryInstance)
    {
      LegendaryInstance legItem=(LegendaryInstance)_itemInstance;
      LegendaryInstanceAttrs attrs=new LegendaryInstanceAttrs(legItem.getLegendaryAttributes());
      _relicsEditor=new RelicsEditionPanelController(_parent,attrs.getRelicsSet());
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

    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(linePanel,c);
      c.gridy++;
      // Color
      linePanel.add(GuiFactory.buildLabel("Color:"));
      linePanel.add(_color.getComboBox());
      // Value
      linePanel.add(GuiFactory.buildLabel("Value:"));
      linePanel.add(_value.getPanel());
      // ID
      linePanel.add(GuiFactory.buildLabel("ID:"));
      linePanel.add(_instanceId.getPanel());
    }

    // Armour specifics line
    if (_itemInstance instanceof ArmourInstance)
    {
      panel.add(_armourPanel,c);
      c.gridy++;
      // Armour value
      _armourPanel.add(GuiFactory.buildLabel("Armour:"));
      _armourPanel.add(_armourValue.getTextField());
    }

    // Level line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Munging level
      panelLine.add(_mungingLabel);
      panelLine.add(_mungingLevel.getComboBox());
      // Item level
      panelLine.add(GuiFactory.buildLabel("Item level:"));
      panelLine.add(_itemLevel);
      // Minimum level
      panelLine.add(GuiFactory.buildLabel("Required level:"));
      panelLine.add(_minLevel.getTextField());
    }

    // Durability line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Durability
      panelLine.add(GuiFactory.buildLabel("Durability:"));
      panelLine.add(_durability.getTextField());
      // Date
      panelLine.add(GuiFactory.buildLabel("Date:"));
      panelLine.add(_date.getTextField());
    }

    // Contextual data line
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
    // User comments
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Comments:"));
      panelLine.add(_userComments);
    }

    // Tabbed pane at the bottom
    // Tabbed pane assembly
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    tabbedPane.add("Stats",_stats.getPanel());
    tabbedPane.add("Essences",_essencesEditor.getPanel());

    // Legendary specifics
    // - relics
    if (_relicsEditor!=null)
    {
      JPanel relicsPanel=_relicsEditor.getPanel();
      tabbedPane.add("Relics",relicsPanel);
    }

    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(panel,c);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(tabbedPane,c);

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
    _parent.setTitle(name);
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
    Long time=_itemInstance.getTime();
    if (time!=null)
    {
      _date.setDate(time);
    }
    // Birth name
    _birthName.setText(_itemInstance.getBirthName());
    // Crafter name
    _crafterName.setText(_itemInstance.getCrafterName());
    // - Own stats
    setStats(_itemInstance.getEffectiveOwnStats());
    // - Essences
    _essencesEditor.initFromItem(_itemInstance);
    // - Durability
    _durability.setValue(_itemInstance.getDurability());
    // - Scaling level
    configureScaling(item);
    // - Item level
    Integer itemLevel=_itemInstance.getItemLevel();
    String itemLevelStr=(itemLevel!=null)?itemLevel.toString():"";
    _itemLevel.setText(itemLevelStr);
    // - Min level
    Integer minLevel=_itemInstance.getMinLevel();
    _minLevel.setValue(minLevel);
    // - Value
    Money value=_itemInstance.getValue();
    if (value!=null)
    {
      _value.setMoney(value);
    }
    // - Color
    _color.selectItem(_itemInstance.getColor());
    // - Bound to
    // TODO
    // - User comments
    String userComments=_itemInstance.getProperty(ItemPropertyNames.USER_COMMENT);
    if (userComments==null) userComments="";
    _userComments.setText(userComments);
  }

  /**
   * Get the current value of the edited item instance.
   */
  public void getItem()
  {
    // - Instance ID
    ItemInstanceId id=_instanceId.getInstanceId();
    _itemInstance.setInstanceId(id);
    // Validity date
    Long date=_date.getDate();
    _itemInstance.setTime(date);
    // Birth name
    _itemInstance.setBirthName(_birthName.getText());
    // Crafter name
    _itemInstance.setCrafterName(_crafterName.getText());
    // - Own stats
    {
      BasicStatsSet stats=_stats.getStats();
      // Armour specifics
      if (_itemInstance instanceof ArmourInstance)
      {
       Integer armourValue=_armourValue.getValue();
        if (armourValue!=null)
        {
          stats.addStat(WellKnownStat.ARMOUR,new FixedDecimalsInteger(armourValue.intValue()));
        }
      }
      _itemInstance.setOwnStats(stats);
    }
    // - Essences
    {
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
      _itemInstance.setEssences(essences);
    }
    // - Durability
    _itemInstance.setDurability(_durability.getValue());
    // - Scaling level
    _mungingLabel=GuiFactory.buildLabel("Scaling level:");
    _mungingLevel=new ComboBoxController<Integer>(false,Integer.class);
    // - Item level
    String itemLevelStr=_itemLevel.getText();
    Integer itemLevel=NumericTools.parseInteger(itemLevelStr);
    _itemInstance.setItemLevel(itemLevel);
    // - Min level
    _itemInstance.setMinLevel(_minLevel.getValue());
    // - Value
    Money value=_value.getMoney();
    _itemInstance.setValue(value);
    // - Color
    _itemInstance.setColor(_color.getSelectedItem());
    // - Bound to
    // TODO
    // - User comments
    {
      String userComments=_userComments.getText();
      if (userComments.length()>0)
      {
        _itemInstance.setProperty(ItemPropertyNames.USER_COMMENT,userComments);
      }
      else
      {
        _itemInstance.removeProperty(ItemPropertyNames.USER_COMMENT);
      }
    }

    // Legendary specifics
    if (_itemInstance instanceof LegendaryInstance)
    {
      LegendaryInstance legendary=(LegendaryInstance)_itemInstance;
      _relicsEditor.getData(legendary.getLegendaryAttributes().getRelicsSet());
    }
  }

  private ComboBoxController<ColorDescription> buildColorCombo()
  {
    ComboBoxController<ColorDescription> ctrl=new ComboBoxController<ColorDescription>();
    ctrl.addEmptyItem("");
    for(ColorDescription color : ColorsManager.getInstance().getAll())
    {
      ctrl.addItem(color,color.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private void configureScaling(Item item)
  {
    TextListener listener=new TextListener()
    {
      @Override
      public void textChanged(String newText)
      {
        Integer itemLevel=NumericTools.parseInteger(newText);
        selectItemLevel(itemLevel);
      }
    };
    _itemLevelController=new DynamicTextEditionController(_itemLevel,listener);
    boolean hide=true;
    String mungingSpec=item.getProperty(ItemPropertyNames.MUNGING);
    if (mungingSpec!=null)
    {
      _munging=Munging.fromString(mungingSpec);
      Progression progression=_munging.getProgression();
      if (progression!=null)
      {
        Integer min=_munging.getMin();
        int minLevel=(min!=null?min.intValue():1);
        Integer max=_munging.getMax();
        int levelCap=Config.getInstance().getMaxCharacterLevel();
        int maxLevel=(max!=null?max.intValue():levelCap);
        _mungingLevel.addEmptyItem("");
        for(int level=minLevel;level<=maxLevel;level++)
        {
          _mungingLevel.addItem(Integer.valueOf(level),String.valueOf(level));
        }
        ItemSelectionListener<Integer> listenerMungingLevel=new ItemSelectionListener<Integer>()
        {
          @Override
          public void itemSelected(Integer mungingLevel)
          {
            selectMungingLevel(mungingLevel);
          }
        };
        _mungingLevel.addListener(listenerMungingLevel);
        hide=false;
      }
    }
    if (hide)
    {
      _mungingLabel.setVisible(false);
      _mungingLevel.getComboBox().setVisible(false);
    }
  }

  private void selectMungingLevel(Integer mungingLevel)
  {
    if (mungingLevel!=null)
    {
      Progression progression=_munging.getProgression();
      Float itemLevel=progression.getValue(mungingLevel.intValue());
      if (itemLevel!=null)
      {
        _itemLevel.setText(String.valueOf(itemLevel.intValue()));
      }
    }
  }

  private void selectItemLevel(Integer itemLevel)
  {
    if (itemLevel!=null)
    {
      Item item=_itemInstance.getReference();
      StatsProvider provider=item.getStatsProvider();
      if (provider!=null)
      {
        BasicStatsSet stats=provider.getStats(1,itemLevel.intValue(),true);
        setStats(stats);
      }
    }
  }

  private void setStats(BasicStatsSet stats)
  {
    FixedDecimalsInteger armourValue=null;
    Item item=_itemInstance.getReference();
    if (item instanceof Armour)
    {
      armourValue=stats.getStat(WellKnownStat.ARMOUR);
      if (armourValue!=null)
      {
        stats.removeStat(WellKnownStat.ARMOUR);
        _armourValue.setValue(Integer.valueOf(armourValue.intValue()));
      }
    }
    _stats.initFromStats(stats);
    if (armourValue!=null)
    {
      stats.setStat(WellKnownStat.ARMOUR,armourValue);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _itemInstance=null;
    _character=null;
    _munging=null;
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
    if (_date!=null)
    {
      _date.dispose();
      _date=null;
    }
    // - Birth name
    _birthName=null;
    // - Crafter name
    _crafterName=null;
    // - Own stats
    if (_stats!=null)
    {
      _stats.dispose();
      _stats=null;
    }
    // - Essences
    if (_essencesEditor!=null)
    {
      _essencesEditor.dispose();
      _essencesEditor=null;
    }
    // - Durability
    if (_durability!=null)
    {
      _durability.dispose();
      _durability=null;
    }
    // - Scaling level
    _mungingLabel=null;
    if (_mungingLevel!=null)
    {
      _mungingLevel.dispose();
      _mungingLevel=null;
    }
    // - Item level
    _itemLevel=null;
    if (_itemLevelController!=null)
    {
      _itemLevelController.dispose();
      _itemLevelController=null;
    }
    // - Min level
    if (_minLevel!=null)
    {
      _minLevel.dispose();
      _minLevel=null;
    }
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
    // - User comments
    _userComments=null;

    // Armour specifics
    _armourPanel=null;
    if (_armourValue!=null)
    {
      _armourValue.dispose();
      _armourValue=null;
    }
    // Legendary specifics
    // - relics
    if (_relicsEditor!=null)
    {
      _relicsEditor.dispose();
      _relicsEditor=null;
    }
  }
}
