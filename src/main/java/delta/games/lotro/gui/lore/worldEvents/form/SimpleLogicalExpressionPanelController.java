package delta.games.lotro.gui.lore.worldEvents.form;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.expressions.logical.SimpleLogicalTreeNode;

/**
 * Controller for a panel to show a simple logical expression.
 * @author DAM
 */
public class SimpleLogicalExpressionPanelController implements PanelProvider
{
  // UI
  private JPanel _panel;
  // Data
  private SimpleLogicalTreeNode<String> _data;

  /**
   * Constructor.
   * @param data Data to show.
   */
  public SimpleLogicalExpressionPanelController(SimpleLogicalTreeNode<String> data)
  {
    _data=data;
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return A panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    String label=_data.getValue();
    ret.add(GuiFactory.buildLabel(label));
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
