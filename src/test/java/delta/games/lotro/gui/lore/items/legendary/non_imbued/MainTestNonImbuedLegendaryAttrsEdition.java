package delta.games.lotro.gui.lore.items.legendary.non_imbued;

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
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryInstanceAttrs;

/**
 * Simple test class for the non-imbued legendary attributes edition panel.
 * @author DAM
 */
public class MainTestNonImbuedLegendaryAttrsEdition
{
  private void doIt()
  {
    //doIt("CaptainEmblemSecondAge75NonImbued.xml");
    doIt("HunterCrossbowFirstAge75NonImbued.xml");
  }

  private void doIt(String sampleName)
  {
    ItemInstance<? extends Item> itemInstance=LegendariesTestUtils.loadItemInstance(sampleName);
    LegendaryInstanceAttrs attrs=LegendariesTestUtils.getLegendaryAttrs(itemInstance);
    final NonImbuedLegendaryInstanceAttrs nonImbuedLegAttrs=attrs.getNonImbuedAttrs();
    ClassDescription captain=ClassesManager.getInstance().getCharacterClassByKey(WellKnownCharacterClassKeys.CAPTAIN);
    ClassAndSlot constraints=new ClassAndSlot(captain,EquipmentLocations.CLASS_SLOT);
    final NonImbuedLegendaryAttrsEditionPanelController controller=new NonImbuedLegendaryAttrsEditionPanelController(null,nonImbuedLegAttrs,constraints);
    int itemLevel=itemInstance.getApplicableItemLevel();
    Item item=itemInstance.getReference();
    controller.setReferenceData(itemLevel,item);

    DefaultFormDialogController<NonImbuedLegendaryInstanceAttrs> dialog=new DefaultFormDialogController<NonImbuedLegendaryInstanceAttrs>(null,nonImbuedLegAttrs)
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
        controller.getData(nonImbuedLegAttrs);
      }
    };
    NonImbuedLegendaryInstanceAttrs result=dialog.editModal();
    System.out.println("Result: "+result);
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
