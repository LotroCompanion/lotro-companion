package delta.games.lotro.utils.gui.text;

import javax.swing.JTextField;

import delta.common.utils.NumericTools;

/**
 * Integer edition controller using a text field.
 * @author DAM
 */
public class IntegerEditionController
{
  private JTextField _textField;
  private Integer _minValue;
  private Integer _maxValue;

  /**
   * Constructor.
   * @param textField Managed text field.
   */
  public IntegerEditionController(JTextField textField)
  {
    this(textField,5);
  }

  /**
   * Constructor.
   * @param textField Managed text field.
   * @param columns Width indication.
   */
  public IntegerEditionController(JTextField textField, int columns)
  {
    _textField=textField;
    _minValue=null;
    _maxValue=null;
    _textField.setColumns(columns);
  }

  /**
   * Set the allowed value range.
   * @param minValue Minimum value, if any.
   * @param maxValue Maximum value, if any.
   */
  public void setValueRange(Integer minValue, Integer maxValue)
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
  public void setValue(Integer value)
  {
    if (value==null)
    {
      _textField.setText("");
    }
    else
    {
      _textField.setText(String.valueOf(value.intValue()));
    }
  }

  /**
   * Get the edited value.
   * @return A value or <code>null</code> if not valid.
   */
  public Integer getValue()
  {
    Integer ret=null;
    String text=_textField.getText();
    Integer value=NumericTools.parseInteger(text);
    if (value!=null)
    {
      ret=value;
      // Check for min value
      if (_minValue!=null)
      {
        if (value.intValue()<_minValue.intValue())
        {
          ret=null;
        }
      }
      // Check for max value
      if (_maxValue!=null)
      {
        if (value.intValue()>_maxValue.intValue())
        {
          ret=null;
        }
      }
    }
    return ret;
  }
}
