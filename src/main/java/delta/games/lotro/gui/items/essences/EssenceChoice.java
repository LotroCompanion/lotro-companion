package delta.games.lotro.gui.items.essences;

import java.util.List;

import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.gui.items.chooser.ItemChoiceWindowController;
import delta.games.lotro.gui.items.chooser.ItemFilterConfiguration;
import delta.games.lotro.gui.items.chooser.ItemFilterController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Essence choosing utility.
 * @author DAM
 */
public class EssenceChoice
{
  /**
   * Choose an essence.
   * @param parent Parent controller.
   * @param character Targeted character.
   * @return An essence item or <code>null</code>.
   */
  public static Item chooseEssence(WindowController parent, CharacterSummary character)
  {
    List<Item> essences=ItemsManager.getInstance().getEssences();
    ItemFilterConfiguration cfg=new ItemFilterConfiguration();
    cfg.initFromItems(essences);
    cfg.forEssenceFilter();
    TypedProperties filterProps=null;
    if (parent!=null)
    {
      filterProps=parent.getUserProperties("EssenceFilter");
    }
    else
    {
      filterProps=new TypedProperties();
    }
    ItemFilterController filterController=new ItemFilterController(cfg,character,filterProps);
    TypedProperties prefs=null;
    if (parent!=null)
    {
      prefs=parent.getUserProperties(ItemChoiceWindowController.ESSENCE_CHOOSER_PROPERTIES_ID);
    }
    Filter<Item> filter=filterController.getFilter();
    ItemChoiceWindowController choiceCtrl=new ItemChoiceWindowController(parent,prefs,essences,filter,filterController);
    Item ret=choiceCtrl.editModal();
    return ret;
  }
}
