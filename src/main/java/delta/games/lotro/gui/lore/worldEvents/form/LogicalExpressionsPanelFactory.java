package delta.games.lotro.gui.lore.worldEvents.form;

import delta.common.utils.expressions.logical.AbstractLogicalExpression;
import delta.common.utils.expressions.logical.CompoundLogicalExpression;
import delta.common.utils.expressions.logical.SimpleLogicalExpression;

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
  public static PanelProvider buildPanelController(AbstractLogicalExpression<String> data)
  {
    PanelProvider ctrl=null;
    if (data instanceof SimpleLogicalExpression)
    {
      SimpleLogicalExpression<String> simple=(SimpleLogicalExpression<String>)data;
      ctrl=new SimpleLogicalExpressionPanelController(simple);
    }
    else if (data instanceof CompoundLogicalExpression)
    {
      CompoundLogicalExpression<String> compound=(CompoundLogicalExpression<String>)data;
      ctrl=new CompoundLogicalExpressionPanelController(compound);
    }
    return ctrl;
  }
}
