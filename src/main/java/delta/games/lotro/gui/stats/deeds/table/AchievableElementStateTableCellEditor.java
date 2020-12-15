package delta.games.lotro.gui.stats.deeds.table;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import delta.common.ui.swing.checkbox.ThreeState;
import delta.common.ui.swing.checkbox.ThreeStateCheckbox;
import delta.games.lotro.character.achievables.AchievableElementState;

/**
 * Table cell editor for achievable state.
 * @author DAM
 */
public class AchievableElementStateTableCellEditor extends DefaultCellEditor
{
  private ThreeStateCheckbox _editor;

  /**
   * Constructor.
   */
  public AchievableElementStateTableCellEditor()
  {
    super(new ThreeStateCheckbox());
    _editor=(ThreeStateCheckbox)getComponent();
    _editor.setHorizontalAlignment(JCheckBox.CENTER);
  }

  public Object getCellEditorValue()
  {
    ThreeStateCheckbox checkBox=(ThreeStateCheckbox)getComponent();
    ThreeState state=checkBox.getState();
    if (state==ThreeState.SELECTED)
    {
      return AchievableElementState.COMPLETED;
    }
    else if (state==ThreeState.HALF_SELECTED)
    {
      return AchievableElementState.UNDERWAY;
    }
    return AchievableElementState.UNDEFINED;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
  {
    if (value instanceof AchievableElementState)
    {
      AchievableElementState state=(AchievableElementState)value;
      if (state==AchievableElementState.COMPLETED)
      {
        _editor.setState(ThreeState.SELECTED);
      }
      else if (state==AchievableElementState.UNDERWAY)
      {
        _editor.setState(ThreeState.HALF_SELECTED);
      }
      else
      {
        _editor.setState(ThreeState.NOT_SELECTED);
      }
    }
    return _editor;
  }
}
