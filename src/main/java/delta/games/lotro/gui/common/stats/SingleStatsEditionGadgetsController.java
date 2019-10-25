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
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.gui.items.ItemUiTools;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for the UI items of a single stat.
 * @author DAM
 */
public class SingleStatsEditionGadgetsController
{
  private JTextField _value;
  private ComboBoxController<StatDescription> _statChooser;
  private JLabel _unit;
  private JButton _deleteButton;

  /**
   * Constructor.
   */
  public SingleStatsEditionGadgetsController()
  {
    _value=GuiFactory.buildTextField("");
    _value.setColumns(6);
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
    _value.setText(value.toString());
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
    _value.setText("");
    _unit.setText("");
  }

  /**
   * Get the managed text field.
   * @return the managed text field.
   */
  public JTextField getValue()
  {
    return _value;
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
    _value=null;
    if (_statChooser!=null)
    {
      _statChooser.dispose();
      _statChooser=null;
    }
    _unit=null;
    _deleteButton=null;
  }
}
