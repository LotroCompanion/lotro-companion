package delta.games.lotro.gui.items.legendary.imbued;

import javax.swing.JFrame;

import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegendaryAttrs;

/**
 * @author DAM
 */
public class MainTestImbuedLegendaryAttrsEdition
{
  private ImbuedLegendaryAttrs buildTestAttrs()
  {
    ImbuedLegendaryAttrs attrs=new ImbuedLegendaryAttrs();
    return attrs;
  }

  private void doIt()
  {
    ImbuedLegendaryAttrs attrs=buildTestAttrs();
    ClassAndSlot constraints=new ClassAndSlot(CharacterClass.CHAMPION,EquipmentLocation.CLASS_SLOT);
    ImbuedLegacyInstanceEditionPanelController controller=new ImbuedLegacyInstanceEditionPanelController(null,attrs,constraints);

    JFrame f=new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(controller.getPanel());
    f.pack();
    f.setVisible(true);
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    new MainTestImbuedLegendaryAttrsEdition().doIt();
  }

}
