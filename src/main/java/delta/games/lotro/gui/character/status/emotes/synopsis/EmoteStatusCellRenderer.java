package delta.games.lotro.gui.character.status.emotes.synopsis;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import delta.common.ui.swing.CheckButtonController;
import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.emotes.EmoteStatus;

/**
 * Renderer for a emote status cell.
 * @author DAM
 */
public class EmoteStatusCellRenderer implements TableCellRenderer
{
  private CheckButtonController _buttonController;
  private JPanel _panel;

  /**
   * Constructor.
   */
  public EmoteStatusCellRenderer()
  {
    String[] icons={"/resources/gui/icons/delete32.png","/resources/gui/icons/check32.png"};
    _buttonController=new CheckButtonController(icons);
    _panel=buildCellPanel();
  }

  private JPanel buildCellPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    JButton button=_buttonController.getButton();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(button,c);
    return panel;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    EmoteStatus status=(EmoteStatus)value;
    boolean acquired=status.isAvailable();
    _buttonController.setState(acquired?1:0);
    int height=_panel.getPreferredSize().height;
    if (table.getRowHeight(row)!=height)
    {
      table.setRowHeight(row,height);
    }
    return _panel;
  }
}
