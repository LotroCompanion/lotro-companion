package delta.games.lotro.gui.lore.items.legendary.passives;

import javax.swing.JFrame;

import delta.games.lotro.gui.lore.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;

/**
 * Simple test class for the passives edition panel.
 * @author DAM
 */
public class MainTestPassivesEdition
{
  private void doIt()
  {
    ItemInstance<? extends Item> testItem=LegendariesTestUtils.loadItemInstance("CaptainGreatSwordFirstAgeImbued.xml");
    if (testItem instanceof LegendaryInstance)
    {
      LegendaryInstance legInstance=(LegendaryInstance)testItem;
      LegendaryInstanceAttrs legAttrs=legInstance.getLegendaryAttributes();
      Integer level=testItem.getEffectiveItemLevel();
      int itemId=testItem.getIdentifier();
      PassivesEditionPanelController controller=new PassivesEditionPanelController(null,legAttrs,itemId);
      int passivesLevel=legAttrs.findLevelForPassives(level.intValue());
      controller.setLevel(passivesLevel);

      JFrame f=new JFrame();
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.getContentPane().add(controller.getPanel());
      f.pack();
      f.setVisible(true);
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestPassivesEdition().doIt();
  }
}
