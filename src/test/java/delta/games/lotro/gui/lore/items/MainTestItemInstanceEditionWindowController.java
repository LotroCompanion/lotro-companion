package delta.games.lotro.gui.lore.items;

import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.classes.AbstractClassDescription;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.classes.WellKnownCharacterClassKeys;
import delta.games.lotro.gui.lore.items.legendary.shared.LegendariesTestUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Test for item instance edition window.
 * @author DAM
 */
public class MainTestItemInstanceEditionWindowController
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    for(String sample : ItemsTestUtils.TEST_SAMPLES)
    {
      ItemInstance<? extends Item> itemInstance=ItemsTestUtils.loadItemInstance(ItemsTestUtils.class,"samples/"+sample);
      doSample(itemInstance);
    }
    for(String sample : LegendariesTestUtils.TEST_SAMPLES)
    {
      ItemInstance<? extends Item> itemInstance=LegendariesTestUtils.loadItemInstance(sample);
      doSample(itemInstance);
    }
  }

  private static void doSample(ItemInstance<? extends Item> itemInstance)
  {
    CharacterSummary character=new CharacterSummary();
    character.setLevel(120);
    AbstractClassDescription requiredClass=getRequiredClass(itemInstance);
    ClassDescription toUse=(ClassDescription)requiredClass;
    if (toUse==null)
    {
      toUse=ClassesManager.getInstance().getCharacterClassByKey(WellKnownCharacterClassKeys.RUNE_KEEPER);
    }
    character.setCharacterClass(toUse);
    ItemInstanceEditionWindowController ctrl=new ItemInstanceEditionWindowController(null,character,itemInstance);
    ctrl.show(false);
  }

  private static AbstractClassDescription getRequiredClass(ItemInstance<? extends Item> itemInstance)
  {
    Item item=itemInstance.getReference();
    return item.getRequiredClass();
  }
}
