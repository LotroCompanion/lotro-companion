package delta.games.lotro.gui.lore.items.legendary.imbued;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacy;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacyInstance;

/**
 * Controller for the UI items of a single imbued legacy.
 * @author DAM
 */
public class SingleImbuedLegacyEditionController
{
  // Data
  private ImbuedLegacyInstance _legacy;
  private ClassAndSlot _constraints;
  // Controllers
  private WindowController _parent;
  // GUI
  private JLabel _icon;
  private MultilineLabel2 _value;
  private JButton _chooseButton;
  private JButton _deleteButton;
  // Current level
  private ComboBoxController<Integer> _currentLevel;
  // Max level
  private ComboBoxController<Integer> _maxLevel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param legacy Legacy to edit.
   * @param constraints Constraints.
   * @param isMain Indicates if we're dealing with the main legacy or not.
   */
  public SingleImbuedLegacyEditionController(WindowController parent, ImbuedLegacyInstance legacy, ClassAndSlot constraints,boolean isMain)
  {
    _parent=parent;
    _legacy=legacy;
    _constraints=constraints;
    // UI
    // - icon
    _icon=GuiFactory.buildTransparentIconlabel(32);
    // - value display
    _value=new MultilineLabel2();
    Dimension dimension=new Dimension(200,32);
    _value.setMinimumSize(dimension);
    _value.setSize(dimension);
    // - chooser button
    if (!isMain)
    {
      _chooseButton=GuiFactory.buildButton("...");
      ActionListener listener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          handleChooseLegacy();
        }
      };
      _chooseButton.addActionListener(listener);
    }
    // - delete button
    if (!isMain)
    {
      _deleteButton=GuiFactory.buildIconButton("/resources/gui/icons/cross.png");
      ActionListener listener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          handleDeleteLegacy();
        }
      };
      _deleteButton.addActionListener(listener);
    }
    // - current level
    _currentLevel=new ComboBoxController<Integer>();
    ItemSelectionListener<Integer> currentLevelListener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer item)
      {
        if (item!=null)
        {
          handleCurrentLevelUpdate(item.intValue());
        }
      }
    };
    // - max level
    _maxLevel=new ComboBoxController<Integer>();
    ItemSelectionListener<Integer> maxLevelListener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer item)
      {
        if (item!=null)
        {
          handleMaxLevelUpdate(item.intValue());
        }
      }
    };
    // - init combos contents
    initLevelCombos();
    // - init listeners
    _currentLevel.addListener(currentLevelListener);
    _maxLevel.addListener(maxLevelListener);
  }

  /**
   * Extract data from UI to the given storage.
   * @param storage Storage to use.
   */
  public void getData(ImbuedLegacyInstance storage)
  {
    storage.setLegacy(_legacy.getLegacy());
    storage.setUnlockedLevels(_legacy.getUnlockedLevels());
    storage.setXp(_legacy.getXp());
  }

  private void handleChooseLegacy()
  {
    ClassDescription characterClass=_constraints.getCharacterClass();
    EquipmentLocation location=_constraints.getSlot();
    ImbuedLegacy legacy=ImbuedLegacyChooser.selectImbuedLegacy(_parent,characterClass,location,_legacy.getLegacy());
    if (legacy!=null)
    {
      setLegacy(legacy);
      setUiFromLegacy();
    }
  }

  private void handleDeleteLegacy()
  {
    setLegacy(null);
    _legacy.setXp(0);
    _legacy.setUnlockedLevels(0);
    setUiFromLegacy();
  }

  private void handleCurrentLevelUpdate(int currentLevel)
  {
    int xp=ImbuedLegacyInstance.getMinXpForLevel(currentLevel);
    _legacy.setXp(xp);
    updateStats();
  }

  private void handleMaxLevelUpdate(int maxLevel)
  {
    int maxInitialLevel=_legacy.getLegacy().getMaxInitialLevel();
    int unlockedLevels=maxLevel-maxInitialLevel;
    _legacy.setUnlockedLevels(unlockedLevels);
    int currentLevel=_legacy.getCurrentLevel();
    initLevelCombo(_currentLevel,1,maxLevel);
    if (maxLevel<currentLevel)
    {
      _currentLevel.selectItem(Integer.valueOf(maxLevel));
    }
    else
    {
      _currentLevel.selectItem(Integer.valueOf(currentLevel));
    }
  }

  private boolean hasLegacy()
  {
    return ((_legacy!=null) && (_legacy.getLegacy()!=null));
  }

  private void setLegacy(ImbuedLegacy legacy)
  {
    _legacy.setLegacy(legacy);
    initLevelCombos();
  }

  private void initLevelCombos()
  {
    ImbuedLegacy legacy=_legacy.getLegacy();
    if (legacy!=null)
    {
      int maxInitialLevel=legacy.getMaxInitialLevel();
      int maxLevel=legacy.getMaxLevel();
      initLevelCombo(_maxLevel,maxInitialLevel,maxLevel);
      initLevelCombo(_currentLevel,1,maxInitialLevel);
    }
    else
    {
      _currentLevel.removeAllItems();
      _maxLevel.removeAllItems();
    }
  }

  private void initLevelCombo(ComboBoxController<Integer> levelCombo, int min, int max)
  {
    Integer previousValue=levelCombo.getSelectedItem();
    ImbuedLegacy legacy=_legacy.getLegacy();
    if (legacy!=null)
    {
      // Push values from min to max
      levelCombo.removeAllItems();
      for(int level=min;level<=max;level++)
      {
        levelCombo.addItem(Integer.valueOf(level),String.valueOf(level));
      }
      levelCombo.selectItem(previousValue);
    }
  }

  /**
   * Get the managed legacy.
   * @return the managed legacy.
   */
  public ImbuedLegacyInstance getLegacyInstance()
  {
    return _legacy;
  }

  /**
   * Update UI to show the internal legacy data.
   */
  public void setUiFromLegacy()
  {
    // Update UI to reflect the internal legacy data
    if (hasLegacy())
    {
      // - Update max level
      _maxLevel.getComboBox().setEnabled(true);
      _maxLevel.selectItem(Integer.valueOf(_legacy.getCurrentMaxTier()));
      // - Update current level
      _currentLevel.getComboBox().setEnabled(true);
      int currentLevel=_legacy.getCurrentLevel();
      _currentLevel.selectItem(Integer.valueOf(currentLevel));
      // - Update icon
      updateIcon();
      // - Update stats
      updateStats();
    }
    else
    {
      // - Update max level
      _maxLevel.getComboBox().setEnabled(false);
      _maxLevel.selectItem(null);
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
    if (hasLegacy())
    {
      BasicStatsSet stats=_legacy.getStats();
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      _value.setText(lines);
    }
    else
    {
      _value.setText(new String[]{});
    }
  }

  private void updateIcon()
  {
    ImbuedLegacy legacy=_legacy.getLegacy();
    if (legacy!=null)
    {
      int iconId=legacy.getIconId();
      ImageIcon icon=LotroIconsManager.getLegacyIcon(iconId);
      _icon.setIcon(icon);
    }
    else
    {
      _icon.setIcon(null);
    }
  }

  /**
   * Get the icon gadget.
   * @return the icon gadget.
   */
  public JLabel getIcon()
  {
    return _icon;
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
   * Get the combo-box controller for the maximum level.
   * @return a combo-box controller.
   */
  public ComboBoxController<Integer> getMaxLevelCombo()
  {
    return _maxLevel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _legacy=null;
    _constraints=null;
    // Controllers
    _parent=null;
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
    if (_maxLevel!=null)
    {
      _maxLevel.dispose();
      _maxLevel=null;
    }
  }
}
