package delta.games.lotro.gui.utils;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.money.comparator.MoneyComparator;
import delta.games.lotro.gui.common.money.MoneyDisplayController;

/**
 * Renderer for money values.
 * @author DAM
 */
public class MoneyCellRenderer implements TableCellRenderer
{
  private MoneyDisplayController _moneyCtrl=new MoneyDisplayController();

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    Money money=(Money)value;
    _moneyCtrl.setMoney(money);
    JPanel panel=_moneyCtrl.getPanel();
    return panel;
  }

  /**
   * Get the preferred width for this renderer.
   * @return A width in pixels.
   */
  public int getPreferredWidth()
  {
    return _moneyCtrl.getPanel().getPreferredSize().width;
  }

  /**
   * Configure a column to display a money value.
   * @param column Column to configure.
   */
  public static void configureColumn(DefaultTableColumnController<?,Money> column)
  {
    MoneyCellRenderer renderer=new MoneyCellRenderer();
    column.setCellRenderer(renderer);
    int width=renderer.getPreferredWidth();
    column.setWidthSpecs(width,width,width);
    column.setComparator(new MoneyComparator());
  }
}

