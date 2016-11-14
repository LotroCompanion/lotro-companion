package delta.games.lotro.utils.gui.text;

import javax.swing.JTextField;

import delta.common.utils.NumericTools;

/**
 * Float edition controller using a text field.
 * @author DAM
 */
public class FloatEditionController
{
  private JTextField _textField;
  private Float _minValue;
  private Float _maxValue;

  /**
   * Constructor.
   * @param textField Managed text field.
   */
  public FloatEditionController(JTextField textField)
  {
    _textField=textField;
    _minValue=null;
    _maxValue=null;
    _textField.setColumns(5);
  }

  /**
   * Set the allowed value range.
   * @param minValue Minimum value, if any.
   * @param maxValue Maximum value, if any.
   */
  public void setValueRange(Float minValue, Float maxValue)
  {
    _minValue=minValue;
    _maxValue=maxValue;
    configureTextField();
  }

  /**
   * Get the managed text field.
   * @return the managed text field.
   */
  public JTextField getTextField()
  {
    return _textField;
  }

  /**
   * Configure the text field.
   */
  private void configureTextField()
  {
    // TODO add Document model to check for typed chars
  }

  /**
   * Set the given value into the managed text field.
   * @param value Value to set.
   */
  public void setValue(Float value)
  {
    if (value==null)
    {
      _textField.setText("");
    }
    else
    {
      _textField.setText(String.valueOf(value.floatValue()));
    }
  }

  /**
   * Get the edited value.
   * @return A value or <code>null</code> if not valid.
   */
  public Float getValue()
  {
    Float ret=null;
    String text=_textField.getText();
    Float value=NumericTools.parseFloat(text);
    if (value!=null)
    {
      ret=value;
      // Check for min value
      if (_minValue!=null)
      {
        if (value.floatValue()<_minValue.floatValue())
        {
          ret=null;
        }
      }
      // Check for max value
      if (_maxValue!=null)
      {
        if (value.floatValue()>_maxValue.floatValue())
        {
          ret=null;
        }
      }
    }
    return ret;
  }
}
