package delta.games.lotro.utils.gui;

import java.awt.Color;

/**
 * Gradients.
 * @author DAM
 */
public class Gradients
{
  private static final Color[] STEPS_4={
    Color.ORANGE,
    new Color(215,219,0),
    new Color(159,237,0),
    Color.GREEN
  };

  private static final Color[] STEPS_5={
    Color.ORANGE,
    new Color(226,214,0),
    new Color(190,228,0),
    new Color(139,242,0),
    Color.GREEN
  };

  private static final Color[] STEPS_7={
    Color.ORANGE,
    new Color(236,209,0),
    new Color(215,219,0),
    new Color(190,228,0),
    new Color(159,237,0),
    new Color(116,246,0),
    Color.GREEN
  };

  private static final Color[] STEPS_8={
    Color.ORANGE,
    new Color(239,207,0),
    new Color(221,216,0),
    new Color(201,224,0),
    new Color(177,232,0),
    new Color(148,240,0),
    new Color(108,247,0),
    Color.GREEN
  };

  private static final Color[] STEPS_11={
    Color.ORANGE,
    new Color(244,205,0),
    new Color(232,211,0),
    new Color(219,217,0),
    new Color(205,222,0),
    new Color(190,228,0),
    new Color(172,233,0),
    new Color(151,239,0),
    new Color(126,244,0),
    new Color(91,249,0),
    Color.GREEN
  };

  /**
   * Get orange to green gradient, using the specified number of steps.
   * @param nbSteps Number of steps.
   * @return A gradient or <code>null</code> if undefined.
   */
  public static Color[] getOrangeToGreen(int nbSteps)
  {
    if (nbSteps==4) return STEPS_4;
    if (nbSteps==5) return STEPS_5;
    if (nbSteps==7) return STEPS_7;
    if (nbSteps==8) return STEPS_8;
    if (nbSteps==11) return STEPS_11;
    return null;
  }
}
