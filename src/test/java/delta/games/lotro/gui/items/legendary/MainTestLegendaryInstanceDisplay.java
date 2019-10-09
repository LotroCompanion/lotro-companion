package delta.games.lotro.gui.items.legendary;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.gui.items.ItemsTestUtils;
import delta.games.lotro.gui.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;

/**
 * Test class for the legendary attributes display panel.
 * @author DAM
 */
public class MainTestLegendaryInstanceDisplay
{
  private void doIt()
  {
    // Build item instance
    ItemInstance<? extends Item> item=ItemsTestUtils.loadItemInstance(LegendariesTestUtils.class,"CaptainEmblemSecondAge75NonImbued.xml");
    LegendaryInstanceAttrs legendaryAttrs=LegendariesTestUtils.getLegendaryAttrs(item);

    // Build controller
    LegendaryInstanceDisplayPanelController controller=new LegendaryInstanceDisplayPanelController();
    // Set data
    Integer itemLevelInt=item.getEffectiveItemLevel();
    int itemLevel=(itemLevelInt!=null)?itemLevelInt.intValue():1;
    controller.setData(itemLevel,legendaryAttrs);

    // Show window
    JFrame frame=new JFrame();
    frame.add(controller.getPanel());
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestLegendaryInstanceDisplay().doIt();
  }
}
