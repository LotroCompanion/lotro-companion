package delta.games.lotro.gui.items.legendary.imbued;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.gui.character.stats.StatDisplayUtils;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacy;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacyInstance;
import delta.games.lotro.utils.FixedDecimalsInteger;

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
  private MultilineLabel2 _value;
  private JButton _chooseButton;
  // Current level
  private ComboBoxController<Integer> _currentLevel;
  // Max level
  private ComboBoxController<Integer> _maxLevel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param legacy Legacy to edit.
   * @param constraints Constraints.
   */
  public SingleImbuedLegacyEditionController(WindowController parent, ImbuedLegacyInstance legacy, ClassAndSlot constraints)
  {
    _parent=parent;
    _legacy=legacy;
    _constraints=constraints;
    // UI
    // - value display
    _value=new MultilineLabel2();
    Dimension dimension=new Dimension(200,32);
    _value.setMinimumSize(dimension);
    _value.setSize(dimension);
    // - chooser button
    _chooseButton=GuiFactory.buildButton("...");
    ActionListener listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleButtonClick((JButton)e.getSource());
      }
    };
    _chooseButton.addActionListener(listener);
    // - current level
    _currentLevel=new ComboBoxController<Integer>();
    initLevelCombos();
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
    _currentLevel.addListener(currentLevelListener);
    // - max level
    _maxLevel=new ComboBoxController<Integer>();
    initLevelCombos();
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
    _maxLevel.addListener(maxLevelListener);
  }

  /**
   * Extract data from UI to the given storage.
   * @param storage Storage to use.
   */
  public void getData(ImbuedLegacyInstance storage)
  {
    // Put UI data into the given storage
  }

  private void handleButtonClick(JButton button)
  {
    CharacterClass characterClass=_constraints.getCharacterClass();
    EquipmentLocation location=_constraints.getSlot();
    ImbuedLegacy legacy=ImbuedLegacyChooser.selectImbuedLegacy(_parent,characterClass,location,_legacy.getLegacy());
    if (legacy!=null)
    {
      setLegacy(legacy);
      setUiFromLegacy();
    }
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
      // - Update stats
      updateStats();
    }
    else
    {
      // - Update max level
      _maxLevel.getComboBox().setEnabled(false);
      // - Update current level
      _currentLevel.getComboBox().setEnabled(false);
      // - Update stats
      updateStats();
    }
  }

  private void updateStats()
  {
    if (hasLegacy())
    {
      BasicStatsSet stats=_legacy.getStats();
      List<StatDescription> statDescriptions=stats.getSortedStats();
      int nbStats=statDescriptions.size();
      String[] lines=new String[nbStats];
      for(int i=0;i<nbStats;i++)
      {
        StatDescription stat=statDescriptions.get(i);
        String statName=stat.getName();
        FixedDecimalsInteger value=stats.getStat(stat);
        String valueStr=StatDisplayUtils.getStatDisplay(value,stat.isPercentage());
        String line=valueStr+" "+statName;
        lines[i]=line;
      }
      _value.setText(lines);
    }
    else
    {
      _value.setText(new String[]{});
    }
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
   * Get the managed choose button.
   * @return the managed choose button.
   */
  public JButton getChooseButton()
  {
    return _chooseButton;
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
    _value=null;
    _chooseButton=null;
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
