package delta.games.lotro.gui.lore.items.legendary2;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.legendary2.traceries.chooser.TraceryChooser;
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
  // Controllers
  private WindowController _parent;
  // GUI
  private JLabel _icon;
  private MultilineLabel2 _value;
  private JButton _chooseButton;
  private JButton _deleteButton;
  // Current level
  private ComboBoxController<Integer> _currentLevel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param data Entry to edit.
   */
  public SingleTraceryEditionController(WindowController parent, SocketEntryInstance data)
  {
    _parent=parent;
    _data=data;
    // UI
    // - icon
    _icon=GuiFactory.buildTransparentIconlabel(32);
    // - value display
    _value=new MultilineLabel2();
    Dimension dimension=new Dimension(200,32);
    _value.setMinimumSize(dimension);
    _value.setSize(dimension);
    // - chooser button
    {
      _chooseButton=GuiFactory.buildButton("...");
      ActionListener listener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          handleChooseTracery((JButton)e.getSource());
        }
      };
      _chooseButton.addActionListener(listener);
    }
    // - delete button
    {
      _deleteButton=GuiFactory.buildIconButton("/resources/gui/icons/cross.png");
      ActionListener listener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          handleDeleteTracery((JButton)e.getSource());
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
    // - init combo contents
    initLevelCombos();
    // - init listeners
    _currentLevel.addListener(currentLevelListener);
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

  private void handleChooseTracery(JButton button)
  {
    SocketEntry template=_data.getTemplate();
    SocketType type=template.getType();
    Tracery tracery=TraceryChooser.selectTracery(_parent,_data.getTracery(),type);
    if (tracery!=null)
    {
      setTracery(tracery);
      setUiFromTracery();
    }
  }

  private void handleDeleteTracery(JButton button)
  {
    setTracery(null);
    _data.setItemLevel(1);
    setUiFromTracery();
  }

  private void handleCurrentLevelUpdate(int currentLevel)
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
    initLevelCombos();
  }

  private void initLevelCombos()
  {
    Tracery tracery=_data.getTracery();
    if (tracery!=null)
    {
      int minItemLevel=tracery.getMinItemLevel();
      int maxItemLevel=tracery.getMaxItemLevel();
      initLevelCombo(_currentLevel,minItemLevel,maxItemLevel);
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
      if ((previousValue.intValue()>=min) && (previousValue.intValue()<=max))
      {
        levelCombo.selectItem(previousValue);
      }
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
      BasicStatsSet stats=_data.getStats();
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      _value.setText(lines);
    }
    else
    {
      _value.setText(new String[]{});
    }
    updateWindow();
  }

  private void updateWindow()
  {
    if (_parent!=null)
    {
      _parent.getWindow().pack();
    }
  }

  private void updateIcon()
  {
    Tracery tracery=_data.getTracery();
    if (tracery!=null)
    {
      Item item=tracery.getItem();
      Icon icon=ItemUiTools.buildItemIcon(item);
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
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _data=null;
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
  }
}
