package delta.games.lotro.gui.character.storage;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;

/**
 * Utility methods used in storage-related UIs.
 * @author DAM
 */
public class StorageUiUtils
{
  /**
   * Build a progress bar to show storage capacity.
   * @return A progress bar.
   */
  public static JProgressBar buildProgressBar()
  {
    JProgressBar bar=new JProgressBar(SwingConstants.HORIZONTAL,0,100);
    bar.setBackground(GuiFactory.getBackgroundColor());
    bar.setBorderPainted(true);
    bar.setStringPainted(true);
    bar.setPreferredSize(new Dimension(200,25));
    bar.setMinimumSize(new Dimension(200,25));
    return bar;
  }

  /**
   * Update the display of a progress bar.
   * @param bar Targeted bar.
   * @param value Current capacity.
   * @param maxValue Max capacity.
   */
  public static void updateProgressBar(JProgressBar bar, Integer value, Integer maxValue)
  {
    if ((value!=null) && (maxValue!=null))
    {
      Color color=getColor(value.intValue(),maxValue.intValue());
      bar.setForeground(color);
      bar.setString(value+" / "+maxValue);
      bar.setMaximum(maxValue.intValue());
      bar.setValue(value.intValue());
    }
    else
    {
      bar.setForeground(Color.LIGHT_GRAY);
      bar.setString("(unknown)"); // I18n
      bar.setMaximum(100);
      bar.setValue(100);
    }
  }

  private static Color getColor(int value, int maxValue)
  {
    if (value * 100 > maxValue * 80) return Color.RED; // > 80%
    if (value * 100 > maxValue * 50) return Color.YELLOW; // > 80%
    return Color.GREEN;
  }
}
