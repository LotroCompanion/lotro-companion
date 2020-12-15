package delta.games.lotro.gui.stats.deeds.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import delta.common.ui.swing.checkbox.ThreeState;
import delta.common.ui.swing.checkbox.ThreeStateCheckbox;
import delta.games.lotro.character.achievables.AchievableElementState;

/**
 * Cell renderer for achievable element states.
 * @author DAM
 */
public class AchievableElementStateTableCellRenderer extends DefaultTableCellRenderer
{
  private ThreeStateCheckbox _checkbox;

  /**
   * Constructor.
   */
  public AchievableElementStateTableCellRenderer()
  {
    _checkbox=new ThreeStateCheckbox();
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    if (value instanceof AchievableElementState)
    {
      AchievableElementState state=(AchievableElementState)value;
      if (state==AchievableElementState.UNDEFINED)
      {
        _checkbox.setState(ThreeState.NOT_SELECTED);
      }
      else if (state==AchievableElementState.UNDERWAY)
      {
        _checkbox.setState(ThreeState.HALF_SELECTED);
      }
      else if (state==AchievableElementState.COMPLETED)
      {
        _checkbox.setState(ThreeState.SELECTED);
      }
    }
    return _checkbox;
  }

  @Override
  public void setValue(Object value)
  {
    System.out.println("Set value: "+value);
  }
}
