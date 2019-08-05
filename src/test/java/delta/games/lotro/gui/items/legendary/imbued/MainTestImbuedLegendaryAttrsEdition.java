package delta.games.lotro.gui.items.legendary.imbued;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
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
    final ImbuedLegendaryInstanceAttrs attrs=buildTestAttrs();
    ClassAndSlot constraints=new ClassAndSlot(CharacterClass.CAPTAIN,EquipmentLocation.MAIN_HAND);
    final ImbuedLegendaryAttrsEditionPanelController controller=new ImbuedLegendaryAttrsEditionPanelController(null,attrs,constraints);

    DefaultFormDialogController<ImbuedLegendaryInstanceAttrs> dialog=new DefaultFormDialogController<ImbuedLegendaryInstanceAttrs>(null,attrs)
    {
      @Override
      protected JPanel buildFormPanel()
      {
        return controller.getPanel();
      }

      @Override
      protected void okImpl()
      {
        super.okImpl();
        controller.getData(attrs);
      }
    };
    ImbuedLegendaryInstanceAttrs result=dialog.editModal();
    System.out.println("Result: "+result);
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
