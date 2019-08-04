package delta.games.lotro.gui.items.legendary.imbued;

import javax.swing.JFrame;

import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.gui.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegendaryInstanceAttrs;

/**
 * Simple test class for the imbued legendary attributes edition panel.
 * @author DAM
 */
public class MainTestImbuedLegendaryAttrsEdition
{
  private ImbuedLegendaryInstanceAttrs  buildTestAttrs()
  {
    ItemInstance<? extends Item> item=LegendariesTestUtils.loadItemInstance("CaptainGreatSwordFirstAgeImbued.xml");
    LegendaryInstanceAttrs attrs=LegendariesTestUtils.getLegendaryAttrs(item);
    ImbuedLegendaryInstanceAttrs imbuedLegAttrs=attrs.getImbuedAttrs();
    return imbuedLegAttrs;
  }

  private void doIt()
  {
    ImbuedLegendaryInstanceAttrs attrs=buildTestAttrs();
    ClassAndSlot constraints=new ClassAndSlot(CharacterClass.CAPTAIN,EquipmentLocation.MAIN_HAND);
    ImbuedLegendaryAttrsEditionPanelController controller=new ImbuedLegendaryAttrsEditionPanelController(null,attrs,constraints);

    JFrame f=new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(controller.getPanel());
    f.pack();
    f.setVisible(true);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestImbuedLegendaryAttrsEdition().doIt();
  }

}
