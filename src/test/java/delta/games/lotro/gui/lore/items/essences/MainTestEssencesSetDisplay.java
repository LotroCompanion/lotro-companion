package delta.games.lotro.gui.lore.items.essences;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.gui.lore.items.ItemsTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.essences.EssencesSet;

/**
 * Test class for the essences set display panel.
 * @author DAM
 */
public class MainTestEssencesSetDisplay
{
  private void doIt()
  {
    ItemInstance<? extends Item> item=ItemsTestUtils.loadItemInstance(MainTestEssencesSetDisplay.class,"helmWithEssences.xml");
    EssencesSet essences=item.getEssences();
    EssencesSetDisplayController controller=new EssencesSetDisplayController(essences);
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
    new MainTestEssencesSetDisplay().doIt();
  }
}
