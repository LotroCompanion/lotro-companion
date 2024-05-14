package delta.games.lotro.gui.character.status.achievables.table;

import java.awt.Color;
import java.awt.Component;
import java.util.function.Function;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.table.DefaultTableCellRenderer;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.achievables.Progress;

/**
 * Cell renderer for progress.
 * @author DAM
 */
public class ProgressTableCellRenderer extends DefaultTableCellRenderer
{
  private JProgressBar _progressBar;
  private transient Function<Progress,Color> _colorFunction;
  private transient Function<Progress,String> _labelFunction;

  /**
   * Constructor.
   */
  public ProgressTableCellRenderer()
  {
    _progressBar=new JProgressBar();
    _progressBar.setBackground(GuiFactory.getBackgroundColor());
    _progressBar.setForeground(Color.BLACK);
    _progressBar.setStringPainted(true);
    _progressBar.setUI(new BasicProgressBarUI()
    {
      @Override
      protected Color getSelectionBackground() { return Color.black; }
      @Override
      protected Color getSelectionForeground() { return Color.black; }
    });
    _colorFunction=ProgressTableCellRenderer::color;
    _labelFunction=ProgressTableCellRenderer::label;
  }

  /**
   * Set the color function.
   * @param f Function to set.
   */
  public void setColorFunction(Function<Progress,Color> f)
  {
    _colorFunction=f;
  }

  /**
   * Set the label function.
   * @param f Function to set.
   */
  public void setLabelFunction(Function<Progress,String> f)
  {
    _labelFunction=f;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    if (value instanceof Progress)
    {
      Progress progress=(Progress)value;
      Color color=_colorFunction.apply(progress);
      _progressBar.setForeground(color);
      String label=_labelFunction.apply(progress);
      _progressBar.setString(label);
      int max=progress.getMax();
      _progressBar.setMaximum(max);
      int current=progress.getCurrent();
      _progressBar.setValue(current);
      return _progressBar;
    }
    return super.getTableCellRendererComponent(table,"-",isSelected,hasFocus,row,column);
  }

  @Override
  public void setValue(Object value)
  {
    super.setValue(value);
  }

  private static Color color(Progress progress)
  {
    int percentage=progress.getPercentage();
    Color color=Color.RED;
    if (percentage>80)
    {
      color=Color.GREEN; // > 80%
    }
    else if (percentage>50)
    {
      color=Color.YELLOW; // > 50%
    }
    return color;
  }

  private static String label(Progress progress)
  {
    int current=progress.getCurrent();
    int max=progress.getMax();
    if (current!=max)
    {
      return current+" / "+max;
    }
    return String.valueOf(max);
  }
}
