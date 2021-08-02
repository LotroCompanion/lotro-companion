package delta.games.lotro.gui.lore.items.legendary.relics;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import delta.games.lotro.gui.lore.items.ItemsTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.LegendaryWeaponInstance;
import delta.games.lotro.lore.items.legendary.relics.RelicsSet;

/**
 * Test class for the relics set display panel.
 * @author DAM
 */
public class MainTestRelicsSetDisplay
{
  private void doIt()
  {
    ItemInstance<? extends Item> item=ItemsTestUtils.loadItemInstance(MainTestRelicsSetDisplay.class,"legendaryWeapon.xml");
    LegendaryWeaponInstance weapon=(LegendaryWeaponInstance)item;
    LegendaryInstanceAttrs legendaryAttrs=weapon.getLegendaryAttributes();
    RelicsSet relics=legendaryAttrs.getRelicsSet();
    RelicsSetDisplayController controller=new RelicsSetDisplayController();
    controller.setData(relics);
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
    new MainTestRelicsSetDisplay().doIt();
  }
}
