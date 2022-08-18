package delta.games.lotro.gui.lore.worldEvents.form;

import delta.common.utils.expressions.logical.CompoundLogicalTreeNode;
import delta.common.utils.expressions.logical.LogicalTreeNode;
import delta.common.utils.expressions.logical.SimpleLogicalTreeNode;

/**
 * Factory for logical expressions panel controllers.
 * @author DAM
 */
public class LogicalExpressionsPanelFactory
{
  /**
   * Build a panel controller to show the given data.
   * @param data Data to display.
   * @return A panel controller.
   */
  public static PanelProvider buildPanelController(LogicalTreeNode<String> data)
  {
    PanelProvider ctrl=null;
    if (data instanceof SimpleLogicalTreeNode)
    {
      SimpleLogicalTreeNode<String> simple=(SimpleLogicalTreeNode<String>)data;
      ctrl=new SimpleLogicalExpressionPanelController(simple);
    }
    else if (data instanceof CompoundLogicalTreeNode)
    {
      CompoundLogicalTreeNode<String> compound=(CompoundLogicalTreeNode<String>)data;
      ctrl=new CompoundLogicalExpressionPanelController(compound);
    }
    return ctrl;
  }
}
