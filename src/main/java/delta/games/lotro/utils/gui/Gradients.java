package delta.games.lotro.utils.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import delta.common.ui.utils.gradients.GradientBuilder;

/**
 * Gradients.
 * @author DAM
 */
public class Gradients
{
  private static final Map<Integer,Color[]> _gradients=new HashMap<Integer,Color[]>();

  /**
   * Get orange to green gradient, using the specified number of steps.
   * @param nbSteps Number of steps.
   * @return A gradient or <code>null</code> if undefined.
   */
  public static Color[] getOrangeToGreen(int nbSteps)
  {
    Integer key=Integer.valueOf(nbSteps);
    Color[] ret=_gradients.get(key);
    if (ret==null)
    {
      ret=GradientBuilder.gradient(Color.ORANGE,Color.GREEN,nbSteps);
      _gradients.put(key,ret);
    }
    return ret;
  }
}
