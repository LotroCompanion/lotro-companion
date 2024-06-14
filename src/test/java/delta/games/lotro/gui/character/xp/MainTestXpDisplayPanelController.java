package delta.games.lotro.gui.character.xp;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Test for the XP display panel controller.
 * @author DAM
 */
public class MainTestXpDisplayPanelController
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    long[] xpValues=new long[] {0, 1242277, 371153271, 549062588L, 532832186L, 0 };
    int[] charLevels=new int[] {1, 41, 142, 150, 149, 75 };
    int nb=xpValues.length;
    for(int i=0;i<nb;i++)
    {
      long xpValue=xpValues[i];
      int charLevel=charLevels[i];
      JFrame frame=new JFrame();
      XpDisplayPanelController ctrl=new XpDisplayPanelController();
      ctrl.setXP(xpValue,charLevel);
      frame.add(ctrl.getPanel());
      frame.pack();
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.setVisible(true);
    }
  }
}
