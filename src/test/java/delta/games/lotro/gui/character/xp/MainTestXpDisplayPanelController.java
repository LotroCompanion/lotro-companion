package delta.games.lotro.gui.character.xp;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author dm
 */
public class MainTestXpDisplayPanelController
{

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    long[] xpValues=new long[] {0, 1242277, 371153271, 549062588L, 532832186L};
    //long[] xpValues=new long[] {1242277};
    for(long xpValue : xpValues)
    {
      JFrame frame=new JFrame();
      XpDisplayPanelController ctrl=new XpDisplayPanelController();
      ctrl.setXP(xpValue);
      frame.add(ctrl.getPanel());
      frame.pack();
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.setVisible(true);
    }
  }
}
