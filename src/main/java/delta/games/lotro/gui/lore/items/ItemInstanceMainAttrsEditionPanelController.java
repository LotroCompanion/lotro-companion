package delta.games.lotro.gui.lore.items;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JPanel;
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
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.colors.ColorDescription;
import delta.games.lotro.common.colors.ColorsManager;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.common.money.MoneyEditionPanelController;
import delta.games.lotro.gui.common.stats.StatsPanel;
import delta.games.lotro.gui.utils.l10n.DateFormat;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.lore.items.scaling.Munging;
import delta.games.lotro.utils.maths.Progression;

/**
 * Controller for a panel to edit the main attributes of an item instance.
 * @author DAM
 */
public class ItemInstanceMainAttrsEditionPanelController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  private Munging _munging;
  // Controllers
  private WindowController _parent;
  // GUI
  private JPanel _panel;
  // - Validity date
  private DateEditionController _date;
  // - Birth name
  private JTextField _birthName;
  // - Crafter name
  private JTextField _crafterName;
  // - Scaling level
  private JLabel _mungingLabel;
  private ComboBoxController<Integer> _mungingLevel;
  // - Item level
  private JTextField _itemLevel;
  private DynamicTextEditionController _itemLevelController;
  // - Min level
  private IntegerEditionController _minLevel;
  // - Durability
  private IntegerEditionController _durability;
  // - Color
  private ComboBoxController<ColorDescription> _color;
  // - Value
  private MoneyEditionPanelController _value;
  // - Bound to
  // TODO
  // - User comments
  private JTextField _userComments;
  // - Own stats display
  private JPanel _stats;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param itemInstance Item instance.
   */
  public ItemInstanceMainAttrsEditionPanelController(WindowController parent, ItemInstance<? extends Item> itemInstance)
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
    // - Validity date
    DateCodec codec=DateFormat.getDateTimeCodec();
    _date=new DateEditionController(codec);
    // - Birth name
    _birthName=GuiFactory.buildTextField("");
    _birthName.setColumns(20);
    // - Crafter name
    _crafterName=GuiFactory.buildTextField("");
    _crafterName.setColumns(20);
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
    // - Durability
    JTextField durability=GuiFactory.buildTextField("");
    _durability=new IntegerEditionController(durability,3);
    _durability.setValueRange(Integer.valueOf(1),Integer.valueOf(150));
    // - Color
    _color=buildColorCombo();
    // - Value
    _value=new MoneyEditionPanelController();
    // - Bound to
    // TODO
    // - User comments
    _userComments=GuiFactory.buildTextField("");
    _userComments.setColumns(40);
    // - Own stats
    _stats=GuiFactory.buildPanel(new GridBagLayout());
    _stats.setBorder(GuiFactory.buildTitledBorder("Stats"));
  }

  private JPanel build()
  {
    initGadgets();
    return buildPanel();
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    panel.setBorder(GuiFactory.buildTitledBorder("Main attributes"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);

    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
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
    }

    // Color and value line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Color
      panelLine.add(GuiFactory.buildLabel("Color:"));
      panelLine.add(_color.getComboBox());
      // Value
      panelLine.add(GuiFactory.buildLabel("Value:"));
      panelLine.add(_value.getPanel());
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
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_stats,c);

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
    // - Validity date
    Long time=_itemInstance.getTime();
    if (time!=null)
    {
      _date.setDate(time);
    }
    // - Birth name
    _birthName.setText(_itemInstance.getBirthName());
    // - Crafter name
    _crafterName.setText(_itemInstance.getCrafterName());
    // - Scaling level
    configureScaling(item);
    // - Item level
    Integer itemLevel=_itemInstance.getEffectiveItemLevel();
    String itemLevelStr=(itemLevel!=null)?itemLevel.toString():"";
    _itemLevel.setText(itemLevelStr);
    // - Min level
    Integer minLevel=_itemInstance.getEffectiveMinLevel();
    _minLevel.setValue(minLevel);
    // - Durability
    _durability.setValue(_itemInstance.getEffectiveDurability());
    // - Color
    _color.selectItem(_itemInstance.getColor());
    // - Value
    Money value=_itemInstance.getEffectiveValue();
    if (value!=null)
    {
      _value.setMoney(value);
    }
    // - Bound to
    // TODO
    // - User comments
    String userComments=_itemInstance.getProperty(ItemPropertyNames.USER_COMMENT);
    if (userComments==null) userComments="";
    _userComments.setText(userComments);
    // - Own stats
    setStats(_itemInstance.getStatsManager().getDefault());
  }

  /**
   * Get the current values of the edited item instance.
   */
  public void getItem()
  {
    Item reference=_itemInstance.getReference();
    // - Validity date
    Long date=_date.getDate();
    _itemInstance.setTime(date);
    // - Birth name
    String birthName=_birthName.getText();
    if (birthName.length()==0) birthName=null;
    _itemInstance.setBirthName(birthName);
    // - Crafter name
    String crafterName=_crafterName.getText();
    if (crafterName.length()==0) crafterName=null;
    _itemInstance.setCrafterName(crafterName);
    // - Scaling level
    _mungingLabel=GuiFactory.buildLabel("Scaling level:");
    _mungingLevel=new ComboBoxController<Integer>(false,Integer.class);
    // - Item level
    String itemLevelStr=_itemLevel.getText();
    Integer itemLevel=NumericTools.parseInteger(itemLevelStr);
    Integer refItemLevel=reference.getItemLevel();
    if (Objects.equals(itemLevel,refItemLevel)) itemLevel=null;
    _itemInstance.setItemLevel(itemLevel);
    // - Min level
    Integer minLevel=_minLevel.getValue();
    Integer refMinLevel=reference.getMinLevel();
    if (Objects.equals(minLevel,refMinLevel)) minLevel=null;
    _itemInstance.setMinLevel(minLevel);
    // - Durability
    Integer durability=_durability.getValue();
    Integer refDurability=reference.getDurability();
    if (Objects.equals(durability,refDurability)) durability=null;
    _itemInstance.setDurability(durability);
    // - Color
    _itemInstance.setColor(_color.getSelectedItem());
    // - Value
    Money value=_value.getMoney();
    _itemInstance.setValue(value);
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
    // - Own stats
    _itemInstance.updateAutoStats();
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
    _munging=item.getMunging();
    if (_munging!=null)
    {
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
      Integer itemLevel=_munging.getItemLevel(mungingLevel.intValue());
      if (itemLevel!=null)
      {
        _itemLevel.setText(itemLevel.toString());
      }
    }
  }

  private void selectItemLevel(Integer itemLevel)
  {
    if (itemLevel!=null)
    {
      BasicStatsSet stats=_itemInstance.getStatsForItemLevel(itemLevel.intValue());
      if (stats!=null)
      {
        setStats(stats);
      }
    }
  }

  private void setStats(BasicStatsSet stats)
  {
    Item item=_itemInstance.getReference();
    StatsProvider provider=item.getStatsProvider();
    StatsPanel.fillStatsPanel(_stats,stats,provider);
    _stats.revalidate();
    _stats.repaint();
    _parent.getWindow().pack();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _itemInstance=null;
    _munging=null;
    // UI/controllers
    _parent=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
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
    // - Durability
    if (_durability!=null)
    {
      _durability.dispose();
      _durability=null;
    }
    // - Color
    if (_color!=null)
    {
      _color.dispose();
      _color=null;
    }
    // - Value
    if (_value!=null)
    {
      _value.dispose();
      _value=null;
    }
    // - Bound to
    // TODO
    // - User comments
    _userComments=null;
    // - Own stats
    if (_stats!=null)
    {
      _stats.removeAll();
      _stats=null;
    }
  }
}
