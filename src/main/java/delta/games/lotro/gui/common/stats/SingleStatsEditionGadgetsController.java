package delta.games.lotro.gui.common.stats;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.text.FloatEditionController;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for the UI items of a single stat.
 * @author DAM
 */
public class SingleStatsEditionGadgetsController
{
  private FloatEditionController _floatEditor;
  private ComboBoxController<StatDescription> _statChooser;
  private JLabel _unit;
  private JButton _deleteButton;

  /**
   * Constructor.
   */
  public SingleStatsEditionGadgetsController()
  {
    JTextField value=GuiFactory.buildTextField("");
    _floatEditor=new FloatEditionController(value);
    _floatEditor.setFormat("#.#");
    _statChooser=ItemUiTools.buildStatChooser();
    _unit=GuiFactory.buildLabel("");
    // Delete button
    ImageIcon icon=IconsManager.getIcon("/resources/gui/icons/cross.png");
    _deleteButton=GuiFactory.buildButton("");
    _deleteButton.setIcon(icon);
    _deleteButton.setMargin(new Insets(1,1,1,1));
    _deleteButton.setContentAreaFilled(false);
    _deleteButton.setBorderPainted(false);
    ActionListener listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        clear();
      }
    };
    _deleteButton.addActionListener(listener);
  }

  /**
   * Set stat value.
   * @param stat Stat to set.
   * @param value Value to set.
   */
  public void setStat(StatDescription stat, FixedDecimalsInteger value)
  {
    _statChooser.selectItem(stat);
    _floatEditor.setValue(Float.valueOf(value.floatValue()));
    boolean isPercentage=stat.isPercentage();
    String label=isPercentage?"%":" ";
    _unit.setText(label);
  }

  /**
   * Reset contents.
   */
  public void clear()
  {
    _statChooser.selectItem(null);
    _floatEditor.setValue(null);
    _unit.setText("");
  }

  /**
   * Get the managed text field.
   * @return the managed text field.
   */
  public JTextField getValueField()
  {
    return _floatEditor.getTextField();
  }

  /**
   * Get the value editor.
   * @return the value editor.
   */
  public FloatEditionController getValueEditor()
  {
    return _floatEditor;
  }

  /**
   * Get the current value in the value field.
   * @return A value or <code>null</code> if parsing fails.
   */
  public Float getValue()
  {
    return _floatEditor.getValue();
  }

  /**
   * Get the stat combo-box controller.
   * @return a combo-box controller.
   */
  public ComboBoxController<StatDescription> getStatComboController()
  {
    return _statChooser;
  }

  /**
   * Get the currently selected stat.
   * @return a stat or <code>null</code> if none.
   */
  public StatDescription getStat()
  {
    return _statChooser.getSelectedItem();
  }

  /**
   * Get the label for the stat unit.
   * @return a label.
   */
  public JLabel getUnit()
  {
    return _unit;
  }

  /**
   * Get the delete button associated with this stat.
   * @return a button.
   */
  public JButton getDeleteButton()
  {
    return _deleteButton;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_floatEditor!=null)
    {
      _floatEditor.dispose();
      _floatEditor=null;
    }
    if (_statChooser!=null)
    {
      _statChooser.dispose();
      _statChooser=null;
    }
    _unit=null;
    _deleteButton=null;
  }
}
