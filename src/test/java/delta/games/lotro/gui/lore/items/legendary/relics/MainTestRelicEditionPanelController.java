package delta.games.lotro.gui.lore.items.legendary.relics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.EquipmentLocations;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
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
    weapon.setEquipmentLocation(EquipmentLocations.MAIN_HAND);
    LegendaryWeaponInstance weaponInstance=new LegendaryWeaponInstance();
    weaponInstance.setReference(weapon);
    LegendaryInstanceAttrs attrs=weaponInstance.getLegendaryAttributes();
    EquipmentLocation slot=weapon.getEquipmentLocation();
    RelicsEditionPanelController panelCtrl=new RelicsEditionPanelController(null,slot,attrs.getRelicsSet());
    JPanel panel=panelCtrl.getPanel();
    JFrame frame=new JFrame();
    frame.add(panel);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    System.out.println(weapon);
  }
}
