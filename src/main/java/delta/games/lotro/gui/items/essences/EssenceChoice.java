package delta.games.lotro.gui.items.essences;

import java.util.List;

import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.items.ItemChoiceWindowController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Essence choosing utility.
 * @author DAM
 */
public class EssenceChoice
{
  private static EssenceItemFilter _filter=new EssenceItemFilter();

  /**
   * Choose an essence.
   * @param parent Parent controller.
   * @return An essence item or <code>null</code>.
   */
  public static Item chooseEssence(WindowController parent)
  {
    List<Item> essences=ItemsManager.getInstance().getEssences();
    EssenceFilterController filterController=new EssenceFilterController(_filter);
    TypedProperties prefs=null;
    if (parent!=null)
    {
      prefs=parent.getUserProperties(ItemChoiceWindowController.ESSENCE_CHOOSER_PROPERTIES_ID);
    }
    ItemChoiceWindowController choiceCtrl=new ItemChoiceWindowController(parent,prefs,essences,_filter,filterController);
    Item ret=choiceCtrl.editModal();
    return ret;
  }
}
