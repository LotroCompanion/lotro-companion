package delta.games.lotro.gui.rewards;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;

/**
 * Test class to display rewards.
 * @author DAM
 */
public class MainTestRewardsDisplay
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    DeedsManager deedsManager=DeedsManager.getInstance();
    List<DeedDescription> deeds=deedsManager.getAll();
    for(int i=0;i<50;i++)
    {
      DeedDescription deed=deeds.get(i);
      Rewards rewards=deed.getRewards();
      ObjectsSet objects=rewards.getObjects();
      int lotroPoints=rewards.getLotroPoints();
      if ((objects.getNbObjectItems()>0) || (lotroPoints>0))
      {
        RewardsPanelController panelCtrl=new RewardsPanelController(rewards);
        JPanel panel=panelCtrl.getPanel();
        JFrame frame=new JFrame();
        JPanel background=GuiFactory.buildBackgroundPanel(new BorderLayout());
        background.add(panel,BorderLayout.CENTER);
        frame.add(background);
        frame.pack();
        frame.setVisible(true);
      }
    }
  }
}