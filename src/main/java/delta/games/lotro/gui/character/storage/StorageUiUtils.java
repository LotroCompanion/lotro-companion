package delta.games.lotro.gui.character.storage;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.achievables.Progress;

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
    Dimension size=new Dimension(200,25);
    return buildProgressBar(size);
  }

  /**
   * Build a progress bar to show storage capacity.
   * @param size Size of progress bar.
   * @return A progress bar.
   */
  public static JProgressBar buildProgressBar(Dimension size)
  {
    JProgressBar bar=new JProgressBar(SwingConstants.HORIZONTAL,0,100);
    bar.setBackground(GuiFactory.getBackgroundColor());
    bar.setBorderPainted(true);
    bar.setStringPainted(true);
    bar.setPreferredSize(size);
    bar.setMinimumSize(size);
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

  /**
   * Color provider.
   * @param progress Input progress.
   * @return A color.
   */
  public static Color getColor(Progress progress)
  {
    return getColor(progress.getCurrent(),progress.getMax());
  }

  private static Color getColor(int value, int maxValue)
  {
    if (value * 100 > maxValue * 80) return Color.RED; // > 80%
    if (value * 100 > maxValue * 50) return Color.YELLOW; // > 80%
    return Color.GREEN;
  }
}
