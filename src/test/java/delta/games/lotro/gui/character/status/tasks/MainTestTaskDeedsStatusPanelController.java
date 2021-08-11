package delta.games.lotro.gui.character.status.tasks;

import delta.common.ui.swing.windows.DefaultWindowController;

/**
 * Test for the reputation synopsis.
 * @author DAM
 */
public class MainTestTaskDeedsStatusPanelController
{
  private void doIt(int tasksCount)
  {
    TaskDeedsStatusPanelController panelCtrl=new TaskDeedsStatusPanelController();
    panelCtrl.update(tasksCount);
    DefaultWindowController w=new DefaultWindowController();
    w.getFrame().add(panelCtrl.getPanel());
    w.getFrame().pack();
    w.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    MainTestTaskDeedsStatusPanelController test=new MainTestTaskDeedsStatusPanelController();
    test.doIt(0);
    test.doIt(99);
    test.doIt(100);
    test.doIt(101);
    test.doIt(500);
    test.doIt(1000);
  }
}
