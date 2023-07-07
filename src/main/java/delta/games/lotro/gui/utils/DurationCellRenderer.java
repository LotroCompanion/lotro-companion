package delta.games.lotro.gui.utils;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import delta.games.lotro.common.Duration;

/**
 * Renderer for durations.
 * @author DAM
 */
public class DurationCellRenderer extends DefaultTableCellRenderer
{
  @Override
  public void setValue(Object value)
  {
    setHorizontalAlignment(SwingConstants.CENTER);
    setText((value == null) ? "" : Duration.getDurationString(((Integer)value).intValue())); // I18n
  }
}
