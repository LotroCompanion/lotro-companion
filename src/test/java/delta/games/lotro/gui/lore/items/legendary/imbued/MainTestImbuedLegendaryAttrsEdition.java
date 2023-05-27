package delta.games.lotro.gui.lore.items.legendary.imbued;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.classes.WellKnownCharacterClassKeys;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.gui.lore.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.EquipmentLocations;
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
    ClassDescription captain=ClassesManager.getInstance().getCharacterClassByKey(WellKnownCharacterClassKeys.CAPTAIN);
    ClassAndSlot constraints=new ClassAndSlot(captain,EquipmentLocations.MAIN_HAND);
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
