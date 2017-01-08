package delta.games.lotro.gui.items.essences;

import java.util.List;

import delta.games.lotro.gui.character.ItemSelection;
import delta.games.lotro.gui.items.ItemChoiceWindowController;
import delta.games.lotro.lore.items.Item;

/**
 * Test class for the essence choice UI.
 * @author DAM
 */
public class MainTestEssenceChoice
{
  private void doIt()
  {
    ItemSelection selection=new ItemSelection();
    List<Item> items=selection.getEssences();
    EssenceItemFilter filter=new EssenceItemFilter();
    EssenceFilterController filterController=new EssenceFilterController(filter);
    ItemChoiceWindowController choiceCtrl=new ItemChoiceWindowController(null,items,filter,filterController);
    choiceCtrl.show(true);
    Item ret=choiceCtrl.getSelectedItem();
    choiceCtrl.dispose();
    System.out.println(ret);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestEssenceChoice().doIt();
  }
}
