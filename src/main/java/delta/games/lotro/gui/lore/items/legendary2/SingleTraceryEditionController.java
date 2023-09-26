package delta.games.lotro.gui.lore.items.legendary2;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.labels.LabelLineStyle;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.legendary2.traceries.chooser.TraceryChooser;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.gui.utils.items.ItemIconController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary2.SocketEntry;
import delta.games.lotro.lore.items.legendary2.SocketEntryInstance;
import delta.games.lotro.lore.items.legendary2.Tracery;

/**
 * Controller for the UI items of a single tracery.
 * @author DAM
 */
public class SingleTraceryEditionController
{
  // Data
  private SocketEntryInstance _data;
  private int _characterLevel;
  private int _liItemLevel;
  // Controllers
  private WindowController _parent;
  // GUI
  private ItemIconController _icon;
  private MultilineLabel2 _value;
  private JButton _chooseButton;
  private JButton _deleteButton;
  // Current level
  private ComboBoxController<Integer> _currentLevel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param characterLevel Character level.
   * @param liItemLevel Item level of the parent LI.
   * @param data Entry to edit.
   */
  public SingleTraceryEditionController(WindowController parent, SocketEntryInstance data, int characterLevel, int liItemLevel)
  {
    _parent=parent;
    _data=data;
    _characterLevel=characterLevel;
    _liItemLevel=liItemLevel;
    // UI
    // - icon
    _icon=IconControllerFactory.buildItemIcon(_parent,null,1);
    // - value display
    _value=new MultilineLabel2();
    LabelLineStyle defaultStyle=_value.getDefaultStyle();
    LabelLineStyle newDefaultStyle=defaultStyle.setFontSize(10);
    _value.setDefaultStyle(newDefaultStyle);
    _value.setLineStyle(0,defaultStyle);
    Dimension dimension=new Dimension(200,32);
    _value.setMinimumSize(dimension);
    _value.setSize(dimension);
    // - chooser button
    _chooseButton=GuiFactory.buildButton("...");
    // - delete button
    _deleteButton=GuiFactory.buildIconButton("/resources/gui/icons/cross.png");
    // - current level
    _currentLevel=new ComboBoxController<Integer>();
    // - init combo contents
    initLevelCombo();
    setUiFromTracery();
  }

  /**
   * Check input.
   * @return <code>true</code> if OK, <code>false</code> otherwise.
   */
  public boolean checkInput()
  {
    if (!hasTracery())
    {
      return true;
    }
    if (!isEnabled(_liItemLevel))
    {
      return true;
    }
    // Check tracery level
    Tracery tracery=_data.getTracery();
    boolean ok=tracery.isApplicableForItemLevel(_liItemLevel);
    if (!ok)
    {
      return false;
    }
    Integer selectedLevel=_currentLevel.getSelectedItem();
    if (selectedLevel==null)
    {
      //System.out.println("Bad selected level");
      return false;
    }
    // Check character level
    ok=tracery.isApplicableForCharacterLevel(_characterLevel);
    return ok;
  }

  /**
   * Extract data from UI to the given storage.
   * @param storage Storage to use.
   */
  public void getData(SocketEntryInstance storage)
  {
    storage.setTracery(_data.getTracery());
    storage.setItemLevel(_data.getItemLevel());
  }

  /**
   * Handle 'choose'.
   * @param availableTraceries Traceries to choose from.
   * @return <code>true</code> if a tracery was chosen, <code>false</code> otherwise.
   */
  public boolean handleChooseTracery(List<Tracery> availableTraceries)
  {
    SocketEntry template=_data.getTemplate();
    SocketType type=template.getType();
    Tracery tracery=TraceryChooser.selectTracery(_parent,_data.getTracery(),type,availableTraceries);
    if (tracery!=null)
    {
      setTracery(tracery);
      setUiFromTracery();
    }
    return (tracery!=null);
  }

  /**
   * Handle 'delete'.
   */
  public void handleDeleteTracery()
  {
    setTracery(null);
    _data.setItemLevel(1);
    setUiFromTracery();
  }

  /**
   * Handle level change.
   * @param currentLevel New level.
   */
  public void handleCurrentLevelUpdate(int currentLevel)
  {
    _data.setItemLevel(currentLevel);
    updateStats();
  }

  private boolean hasTracery()
  {
    return ((_data!=null) && (_data.getTracery()!=null));
  }

  private void setTracery(Tracery tracery)
  {
    _data.setTracery(tracery);
    initLevelCombo();
  }

  /**
   * Set the item level of the parent LI.
   * @param liItemLevel Item level to set.
   */
  public void setLIItemLevel(int liItemLevel)
  {
    _liItemLevel=liItemLevel;
    initLevelCombo();
  }

  private void initLevelCombo()
  {
    Tracery tracery=_data.getTracery();
    if (tracery!=null)
    {
      int minItemLevel=tracery.getMinItemLevel();
      int maxItemLevel=tracery.getMaxItemLevel();
      int maxLevel=Math.min(maxItemLevel,_liItemLevel);
      initLevelCombo(_currentLevel,minItemLevel,maxLevel);
    }
    else
    {
      _currentLevel.removeAllItems();
    }
  }

  private void initLevelCombo(ComboBoxController<Integer> levelCombo, int min, int max)
  {
    Integer previousValue=levelCombo.getSelectedItem();
    // Push values from min to max
    levelCombo.removeAllItems();
    for(int level=min;level<=max;level++)
    {
      levelCombo.addItem(Integer.valueOf(level),String.valueOf(level));
    }
    if (previousValue!=null)
    {
      int selectedValue=previousValue.intValue();
      if (selectedValue<min)
      {
        selectedValue=min;
      }
      if (selectedValue>max)
      {
        selectedValue=max;
      }
      levelCombo.selectItem(Integer.valueOf(selectedValue));
    }
  }

  /**
   * Get the managed data.
   * @return the managed data.
   */
  public SocketEntryInstance getData()
  {
    return _data;
  }

  /**
   * Minimize the item level.
   */
  public void minimizeLevel()
  {
    List<Integer> levels=_currentLevel.getItems();
    int nbLevels=levels.size();
    if (nbLevels>0)
    {
      Integer minLevel=levels.get(0);
      _currentLevel.selectItem(minLevel);
    }
  }

  /**
   * Maximize the item level.
   */
  public void maximizeLevel()
  {
    List<Integer> levels=_currentLevel.getItems();
    int nbLevels=levels.size();
    if (nbLevels>0)
    {
      Integer maxLevel=levels.get(nbLevels-1);
      _currentLevel.selectItem(maxLevel);
    }
  }

  /**
   * Update UI to show the internal tracery data.
   */
  public void setUiFromTracery()
  {
    // Update UI to reflect the internal legacy data
    if (hasTracery())
    {
      // - Update current level
      _currentLevel.getComboBox().setEnabled(true);
      int currentLevel=_data.getItemLevel();
      _currentLevel.selectItem(Integer.valueOf(currentLevel));
      // - Update icon
      updateIcon();
      // - Update stats
      updateStats();
    }
    else
    {
      // - Update current level
      _currentLevel.getComboBox().setEnabled(false);
      _currentLevel.selectItem(null);
      // - Update icon
      updateIcon();
      // - Update stats
      updateStats();
    }
  }

  private void updateStats()
  {
    if (hasTracery())
    {
      // Color
      Tracery tracery=_data.getTracery();
      boolean ok=tracery.isApplicable(_characterLevel,_liItemLevel);
      Color foreground=ok?Color.BLACK:Color.RED;
      _value.setForegroundColor(foreground);
      // Name
      String name=tracery.getName();
      LabelLineStyle nameStyle=LabelLineStyle.DEFAULT_LINE_STYLE.setHalo(true);
      Color nameColor=ItemUiTools.getColorForItem(tracery.getItem(),Color.WHITE);
      nameStyle=nameStyle.setForegroundColor(nameColor);
      _value.setLineStyle(0,nameStyle);
      // Stats
      BasicStatsSet stats=_data.getStats();
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      String[] toShow=new String[lines.length+1];
      System.arraycopy(lines,0,toShow,1,lines.length);
      toShow[0]=name;
      _value.setText(toShow);
    }
    else
    {
      _value.setLineStyle(0,LabelLineStyle.DEFAULT_LINE_STYLE);
      // Nothing slotted. Give a hint on the expected socket type
      SocketType type=_data.getTemplate().getType();
      String hint="( "+type.getLabel()+" )";
      _value.setText(new String[]{hint});
    }
  }

  private void updateIcon()
  {
    Tracery tracery=_data.getTracery();
    if (tracery!=null)
    {
      Item item=tracery.getItem();
      _icon.setItem(item,1);
    }
    else
    {
      int socketTypeCode=_data.getTemplate().getType().getCode();
      _icon.clear(LotroIconsManager.getEmptySocketIcon(socketTypeCode));
    }
  }

  /**
   * Get the icon gadget.
   * @return the icon gadget.
   */
  public JButton getIcon()
  {
    return _icon.getIcon();
  }

  /**
   * Get the label to display the legacy stats.
   * @return a multiline label.
   */
  public MultilineLabel2 getValueLabel()
  {
    return _value;
  }

  /**
   * Get the managed 'choose' button.
   * @return the managed 'choose' button.
   */
  public JButton getChooseButton()
  {
    return _chooseButton;
  }

  /**
   * Get the managed 'delete' button.
   * @return the managed 'delete' button.
   */
  public JButton getDeleteButton()
  {
    return _deleteButton;
  }

  /**
   * Get the combo-box controller for the current level.
   * @return a combo-box controller.
   */
  public ComboBoxController<Integer> getCurrentLevelCombo()
  {
    return _currentLevel;
  }

  /**
   * Indicates if this socket is enabled or not.
   * @param itemLevel Item level to use.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isEnabled(int itemLevel)
  {
    return _data.getTemplate().isEnabled(itemLevel);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _data=null;
    // Controllers
    _parent=null;
    if (_icon!=null)
    {
      _icon.dispose();
      _icon=null;
    }
    // UI
    _icon=null;
    _value=null;
    _chooseButton=null;
    _deleteButton=null;
    if (_currentLevel!=null)
    {
      _currentLevel.dispose();
      _currentLevel=null;
    }
  }
}
