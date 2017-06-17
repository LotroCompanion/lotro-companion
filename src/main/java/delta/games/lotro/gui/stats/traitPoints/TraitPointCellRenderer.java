package delta.games.lotro.gui.stats.traitPoints;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.utils.gui.CheckButtonController;

/**
 * @author DAM
 */
public class TraitPointCellRenderer implements TableCellRenderer
{
  private CheckButtonController _buttonController;
  private JPanel _panel;

  /**
   * Constructor.
   */
  public TraitPointCellRenderer()
  {
    String[] icons={"delete32","check32"};
    _buttonController=new CheckButtonController(icons);
    _panel=buildCellPanel();
  }

  private JPanel buildCellPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    JButton button=_buttonController.getButton();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(button,c);
    return panel;
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    Boolean acquired=(Boolean)value;
    _buttonController.setState(acquired.booleanValue()?1:0);
    int height=_panel.getPreferredSize().height;
    table.setRowHeight(row,height);
    return _panel;
  }
}
