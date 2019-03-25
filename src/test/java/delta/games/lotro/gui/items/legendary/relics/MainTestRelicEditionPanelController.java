package delta.games.lotro.gui.items.legendary.relics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.lore.items.legendary.LegendaryAttrs;
import delta.games.lotro.lore.items.legendary.LegendaryWeapon;
import delta.games.lotro.lore.items.legendary.LegendaryWeaponInstance;

/**
 * Test for the relic edition panel controller.
 * @author DAM
 */
public class MainTestRelicEditionPanelController
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LegendaryWeapon weapon=new LegendaryWeapon();
    LegendaryWeaponInstance weaponInstance=new LegendaryWeaponInstance();
    weaponInstance.setReference(weapon);
    LegendaryAttrs attrs=weaponInstance.getLegendaryAttributes();
    RelicsEditionPanelController panelCtrl=new RelicsEditionPanelController(null,attrs.getRelicsSet());
    JPanel panel=panelCtrl.getPanel();
    JFrame frame=new JFrame();
    frame.add(panel);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    System.out.println(weapon);
  }
}
