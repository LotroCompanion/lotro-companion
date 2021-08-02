package delta.games.lotro.gui.lore.items;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import delta.games.lotro.gui.lore.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Test class for the item instance attributes display panel.
 * @author DAM
 */
public class MainTestItemInstanceDisplay
{
  private static final Logger LOGGER=Logger.getLogger(MainTestItemInstanceDisplay.class);

  private void doIt()
  {
    // Regular
    for(String sample : ItemsTestUtils.TEST_SAMPLES)
    {
      try
      {
        ItemInstance<? extends Item> itemInstance=ItemsTestUtils.loadItemInstance(ItemsTestUtils.class,"samples/"+sample);
        doItem(sample,itemInstance);
      }
      catch(Throwable t)
      {
        LOGGER.error("Failed to handle sample: "+sample,t);
      }
    }
    // Legendaries
    for(String sample : LegendariesTestUtils.TEST_SAMPLES)
    {
      try
      {
        ItemInstance<? extends Item> itemInstance=ItemsTestUtils.loadItemInstance(LegendariesTestUtils.class,sample);
        doItem(sample,itemInstance);
      }
      catch(Throwable t)
      {
        LOGGER.error("Failed to handle sample: "+sample,t);
      }
    }
  }

  private void doItem(String instanceName, ItemInstance<? extends Item> itemInstance)
  {
    // Build controller
    ItemInstanceDisplayPanelController controller=new ItemInstanceDisplayPanelController(null,itemInstance);

    // Show window
    JFrame frame=new JFrame();
    frame.setTitle(instanceName);
    frame.add(controller.getPanel());
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestItemInstanceDisplay().doIt();
  }
}
