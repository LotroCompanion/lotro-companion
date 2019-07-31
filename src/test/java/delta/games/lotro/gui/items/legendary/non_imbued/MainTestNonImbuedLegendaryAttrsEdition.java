package delta.games.lotro.gui.items.legendary.non_imbued;

import javax.swing.JFrame;

import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.gui.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryAttrs;

/**
 * Simple test class for the non-imbued legendary attributes edition panel.
 * @author DAM
 */
public class MainTestNonImbuedLegendaryAttrsEdition
{
  private NonImbuedLegendaryAttrs  buildTestAttrs()
  {
    ItemInstance<? extends Item> item=LegendariesTestUtils.loadItemInstance("CaptainEmblemSecondAge75NonImbued.xml");
    LegendaryInstanceAttrs attrs=LegendariesTestUtils.getLegendaryAttrs(item);
    NonImbuedLegendaryAttrs nonImbuedLegAttrs=attrs.getNonImbuedAttrs();
    return nonImbuedLegAttrs;
  }

  private void doIt()
  {
    NonImbuedLegendaryAttrs attrs=buildTestAttrs();
    ClassAndSlot constraints=new ClassAndSlot(CharacterClass.CAPTAIN,EquipmentLocation.CLASS_SLOT);
    NonImbuedAttrsEditionPanelController controller=new NonImbuedAttrsEditionPanelController(null,attrs,constraints);

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
    new MainTestNonImbuedLegendaryAttrsEdition().doIt();
  }
}
