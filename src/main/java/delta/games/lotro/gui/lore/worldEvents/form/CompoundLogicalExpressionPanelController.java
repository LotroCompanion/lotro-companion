package delta.games.lotro.gui.lore.worldEvents.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.collections.filters.Operator;
import delta.common.utils.expressions.logical.CompoundLogicalTreeNode;
import delta.common.utils.expressions.logical.LogicalTreeNode;

/**
 * Controller for a panel to show a compound logical expression.
 * @author DAM
 */
public class CompoundLogicalExpressionPanelController implements PanelProvider
{
  // Controllers
  private List<PanelProvider> _childPanels;
  // UI
  private JPanel _panel;
  // Data
  private CompoundLogicalTreeNode<String> _data;

  /**
   * Constructor.
   * @param data Data to show.
   */
  public CompoundLogicalExpressionPanelController(CompoundLogicalTreeNode<String> data)
  {
    _data=data;
    _childPanels=new ArrayList<PanelProvider>();
    _panel=buildPanel();
  }

  @Override
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Operator
    Operator operator=_data.getOperator();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    JLabel operatorLabel=GuiFactory.buildLabel(operator.name());
    ret.add(operatorLabel,c);
    // Filler
    JPanel filler=GuiFactory.buildPanel(null);
    filler.setMinimumSize(new Dimension(5,0));
    filler.setPreferredSize(new Dimension(5,0));
    filler.setMaximumSize(new Dimension(5,Short.MAX_VALUE));
    filler.setBackground(Color.BLACK);
    filler.setOpaque(true);
    c=new GridBagConstraints(1,0,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(5,5,5,0),0,0);
    ret.add(filler,c);
    // Child elements
    JPanel childElementsPanel=buildChildElementsPanel();
    c=new GridBagConstraints(2,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(childElementsPanel,c);
    return ret;
  }

  private JPanel buildChildElementsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    for(LogicalTreeNode<String> childItem : _data.getItems())
    {
      PanelProvider ctrl=LogicalExpressionsPanelFactory.buildPanelController(childItem);
      _childPanels.add(ctrl);
      ret.add(ctrl.getPanel(),c);
      c.gridy++;
    }
    return ret;
  }

  @Override
  public void dispose()
  {
    // Controllers
    if (_childPanels!=null)
    {
      for(PanelProvider ctrl : _childPanels)
      {
        ctrl.dispose();
      }
      _childPanels=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
