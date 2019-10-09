package delta.games.lotro.gui.items.legendary.non_imbued;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.gui.items.ItemsTestUtils;
import delta.games.lotro.gui.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryInstanceAttrs;

/**
 * Test class for the non-imbued legendary attributes display panel.
 * @author DAM
 */
public class MainTestNonImbuedLegendaryAttrsDisplay
{
  private void doIt()
  {
    ItemInstance<? extends Item> item=ItemsTestUtils.loadItemInstance(LegendariesTestUtils.class,"CaptainEmblemSecondAge75NonImbued.xml");
    LegendaryInstanceAttrs legendaryAttrs=LegendariesTestUtils.getLegendaryAttrs(item);
    NonImbuedLegendaryInstanceAttrs nonImbuedAttrs=legendaryAttrs.getNonImbuedAttrs();
    NonImbuedLegendaryAttrsDisplayPanelController controller=new NonImbuedLegendaryAttrsDisplayPanelController();
    controller.setData(nonImbuedAttrs);
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
    new MainTestNonImbuedLegendaryAttrsDisplay().doIt();
  }
}
