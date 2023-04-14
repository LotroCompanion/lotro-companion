package delta.games.lotro.gui.lore.hobbies.form;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import delta.common.utils.l10n.L10n;

/**
 * Percentage renderer for number columns.
 * @author DAM
 */
public class PercentageRenderer extends DefaultTableCellRenderer
{
  private int _digits;

  /**
   * Constructor.
   * @param digits Digits to use.
   */
  public PercentageRenderer(int digits)
  {
    _digits=digits;
    setHorizontalAlignment(SwingConstants.RIGHT);
  }

  @Override
  public void setValue(Object value)
  {
    String label="";
    if (value instanceof Number)
    {
      Number numberValue=(Number)value;
      label=L10n.getString(numberValue.doubleValue(),_digits)+"%";
    }
    setText(label);
  }
}
