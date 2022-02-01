package delta.games.lotro.gui.character.status.achievables.table;

import java.awt.Color;
import java.awt.Component;

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
      protected Color getSelectionBackground() { return Color.black; }
      protected Color getSelectionForeground() { return Color.black; }
    });
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    if (value instanceof Progress)
    {
      Progress state=(Progress)value;
      int percentage=state.getPercentage();
      int current=state.getCurrent();
      int max=state.getMax();
      Color color=Color.RED;
      if (percentage>80)
      {
        color=Color.GREEN; // > 80%
      }
      else if (percentage>50)
      {
        color=Color.YELLOW; // > 50%
      }
      _progressBar.setForeground(color);
      if (current!=max)
      {
        _progressBar.setString(current+" / "+max);
      }
      else
      {
        _progressBar.setString(String.valueOf(max));
      }
      _progressBar.setMaximum(max);
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
}
