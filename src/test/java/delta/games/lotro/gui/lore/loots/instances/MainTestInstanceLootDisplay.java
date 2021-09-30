package delta.games.lotro.gui.lore.loots.instances;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.gui.lore.loot.instances.InstanceLootDisplayPanelController;
import delta.games.lotro.lore.instances.PrivateEncounter;
import delta.games.lotro.lore.instances.PrivateEncountersManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.utils.ContextPropertyNames;

/**
 * Test class for the iinstance loot display panel.
 * @author DAM
 */
public class MainTestInstanceLootDisplay
{
  private void doIt()
  {
    DefaultWindowController window=new DefaultWindowController();

    // Instance
    int instanceId=1879184525; // Glinghant
    PrivateEncounter pe=PrivateEncountersManager.getInstance().getPrivateEncounterById(instanceId);
    window.setContextProperty(ContextPropertyNames.PRIVATE_ENCOUNTER,pe);
    // Chest
    int chestId=1879092875; // 
    Item item=ItemsManager.getInstance().getItem(chestId);
    window.setContextProperty(ContextPropertyNames.ITEM,item);
    InstanceLootDisplayPanelController panelCtrl=new InstanceLootDisplayPanelController(window);
    JPanel panel=panelCtrl.getPanel();
    if (panel!=null)
    {
      JFrame frame=window.getFrame();
      Container contentPane=frame.getContentPane();
      contentPane.add(panel,BorderLayout.CENTER);
      frame.pack();
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.setVisible(true);
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestInstanceLootDisplay().doIt();
  }
}
