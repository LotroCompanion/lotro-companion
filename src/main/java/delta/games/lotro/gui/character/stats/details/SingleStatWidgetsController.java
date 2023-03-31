package delta.games.lotro.gui.character.stats.details;

import java.awt.Color;

import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.utils.NumericUtils;

/**
 * Controller for the widgets associated to a single stat.
 * @author DAM
 */
public class SingleStatWidgetsController
{
  private boolean _isPercentage;
  private StatDescription _stat;
  private JLabel _value;
  private JLabel _deltaValue;

  /**
   * Constructor.
   * @param stat Stat to use.
   */
  public SingleStatWidgetsController(StatDescription stat)
  {
    _stat=stat;
    _value=GuiFactory.buildLabel("");
    _deltaValue=GuiFactory.buildLabel("");
  }

  /**
   * Get the value label.
   * @return a label.
   */
  public JLabel getValueLabel()
  {
    return _value;
  }

  /**
   * Get the delta value label.
   * @return a label.
   */
  public JLabel getDeltaValueLabel()
  {
    return _deltaValue;
  }

  /**
   * Update stats.
   * @param reference Reference values (may be <code>null</code>).
   * @param current Current values.
   */
  public void updateStats(BasicStatsSet reference, BasicStatsSet current)
  {
    // Update current value
    Number currentValue=current.getStat(_stat);
    setValue(_value,currentValue,_isPercentage);
    // Handle reference
    Number referenceValue=null;
    if (reference!=null)
    {
      referenceValue=reference.getStat(_stat);
    }
    if (referenceValue!=null)
    {
      Number delta=delta(currentValue,referenceValue);
      setValue(_deltaValue,delta,_isPercentage);
      Color color=Color.BLACK;
      int sign=NumericUtils.sign(delta);
      if (sign==1) // Positive
      {
        _deltaValue.setText("+"+_deltaValue.getText());
        color=new Color(0,153,0); // Greenish
      }
      else if (sign==-1) // Negative
      {
        color=Color.RED;
      }
      else
      {
        _deltaValue.setText("");
      }
      _deltaValue.setForeground(color);
    }
  }

  private Number delta(Number value, Number reference)
  {
    if (_isPercentage)
    {
      float currentValue=(value!=null)?value.floatValue():0;
      float diff=currentValue-reference.floatValue();
      return Float.valueOf(diff);
    }
    int currentValue=(value!=null)?Math.round(value.floatValue()):0;
    int diff=currentValue-Math.round(reference.floatValue());
    return Integer.valueOf(diff);
  }

  private void setValue(JLabel label, Number value, boolean percentage)
  {
    String valueStr=StatUtils.getStatDisplay(value,_stat);
    label.setText(valueStr);
  }
}
