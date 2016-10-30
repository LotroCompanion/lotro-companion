package delta.games.lotro.gui.character.stats;

import java.awt.Color;

import javax.swing.JLabel;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for the widgets associated to a single stat.
 * @author DAM
 */
public class SingleStatWidgetsController
{
  private boolean _isPercentage;
  private STAT _stat;
  private JLabel _label;
  private JLabel _value;
  private JLabel _deltaValue;

  /**
   * Constructor.
   * @param label Stat label.
   * @param stat Stat to use.
   * @param isPercentage Stat is a percentage or not.
   */
  public SingleStatWidgetsController(String label, STAT stat, boolean isPercentage)
  {
    _stat=stat;
    _isPercentage=isPercentage;
    _label=GuiFactory.buildLabel(label+":");
    _value=GuiFactory.buildLabel("");
    _deltaValue=GuiFactory.buildLabel("");
  }

  /**
   * Get the stat label.
   * @return a label.
   */
  public JLabel getLabel()
  {
    return _label;
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
    FixedDecimalsInteger currentValue=current.getStat(_stat);
    setValue(_value,currentValue,_isPercentage);
    // Handle reference
    FixedDecimalsInteger referenceValue=null;
    if (reference!=null)
    {
      referenceValue=reference.getStat(_stat);
    }
    if (referenceValue!=null)
    {
      FixedDecimalsInteger delta=delta(currentValue,referenceValue);
      setValue(_deltaValue,delta,_isPercentage);
      Color color=Color.BLACK;
      int internalDeltaValue=delta.getInternalValue();
      if (internalDeltaValue>0)
      {
        _deltaValue.setText("+"+_deltaValue.getText());
        //color=Color.GREEN;
        color=new Color(0,153,0);
      }
      else if (internalDeltaValue<0)
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

  private FixedDecimalsInteger delta(FixedDecimalsInteger value, FixedDecimalsInteger reference)
  {
    if (_isPercentage)
    {
      double currentValue=(value!=null)?value.doubleValue():0;
      double diff=currentValue-reference.doubleValue();
      return new FixedDecimalsInteger((float)diff);
    }
    int currentValue=(value!=null)?value.intValue():0;
    int diff=currentValue-reference.intValue();
    return new FixedDecimalsInteger(diff);
  }

  private void setValue(JLabel label, FixedDecimalsInteger value, boolean percentage)
  {
    String valueStr;
    if (value!=null)
    {
      if (percentage)
      {
        valueStr=String.format("%.1f%%",Double.valueOf(value.doubleValue()));
      }
      else
      {
        valueStr=String.format("%d",Integer.valueOf(value.intValue()));
      }
    }
    else
    {
      valueStr="-";
    }
    label.setText(valueStr);
  }
}
