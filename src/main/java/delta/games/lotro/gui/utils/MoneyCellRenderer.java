package delta.games.lotro.gui.utils;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.games.lotro.common.money.Money;
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
    if (money==null) money=new Money();
    _moneyCtrl.setMoney(money);
    JPanel panel=_moneyCtrl.getPanel();
    return panel;
  }
}

