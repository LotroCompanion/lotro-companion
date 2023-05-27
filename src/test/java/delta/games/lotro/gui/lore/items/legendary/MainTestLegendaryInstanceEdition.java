package delta.games.lotro.gui.lore.items.legendary;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.classes.WellKnownCharacterClassKeys;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.gui.lore.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.EquipmentLocations;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;

/**
 * Simple test class for the legendary instance edition panel.
 * @author DAM
 */
public class MainTestLegendaryInstanceEdition
{
  ItemInstance<? extends Item> buildTestItemInstance()
  {
    //String name="CaptainEmblemSecondAge75NonImbued.xml";
    String name="CaptainGreatSwordFirstAgeImbued.xml";
    ItemInstance<? extends Item> item=LegendariesTestUtils.loadItemInstance(name);
    return item;
  }

  ItemInstance<? extends Item> buildEmptyTestItemInstance()
  {
    ItemsManager itemsMgr=ItemsManager.getInstance();
    // Hunter's Crossbow of the Third Age (level 55, min level 51)
    Item item=itemsMgr.getItem(1879132909);
    ItemInstance<? extends Item> itemInstance=ItemFactory.buildInstance(item);
    return itemInstance;
  }

  private void doIt()
  {
    final ItemInstance<? extends Item> itemInstance=buildTestItemInstance();
    edit(itemInstance);
  }

  private void edit(ItemInstance<? extends Item> itemInstance)
  {
    LegendaryInstance legendaryInstance=(LegendaryInstance)itemInstance;
    final LegendaryInstanceAttrs legendaryAttrs=legendaryInstance.getLegendaryAttributes();
    ClassDescription captain=ClassesManager.getInstance().getCharacterClassByKey(WellKnownCharacterClassKeys.CAPTAIN);
    ClassAndSlot constraints=new ClassAndSlot(captain,EquipmentLocations.CLASS_SLOT);
    final LegendaryInstanceEditionPanelController controller=new LegendaryInstanceEditionPanelController(null,itemInstance,constraints);

    DefaultFormDialogController<LegendaryInstanceAttrs> dialog=new DefaultFormDialogController<LegendaryInstanceAttrs>(null,legendaryAttrs)
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
        controller.getData(legendaryAttrs);
      }
    };
    LegendaryInstanceAttrs result=dialog.editModal();
    System.out.println("Result: "+result);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestLegendaryInstanceEdition().doIt();
  }
}
