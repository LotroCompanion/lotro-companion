package delta.games.lotro.gui.items.essences;

import java.util.List;

import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.gui.items.chooser.ItemChooser;
import delta.games.lotro.gui.items.chooser.ItemFilterConfiguration;
import delta.games.lotro.gui.items.chooser.ItemFilterController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Essence choosing utility.
 * @author DAM
 */
public class EssenceChoice
{
  /**
   * Preference file for the columns of the essence chooser.
   */
  public static final String ESSENCE_CHOOSER_PROPERTIES_ID="EssenceChooserColumn";

  /**
   * Choose an essence.
   * @param parent Parent controller.
   * @param attrs Attributes of toon to use.
   * @return An essence item or <code>null</code>.
   */
  public static Item chooseEssence(WindowController parent, BasicCharacterAttributes attrs)
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
    ItemFilterController filterController=new ItemFilterController(cfg,attrs,filterProps);
    TypedProperties prefs=null;
    if (parent!=null)
    {
      prefs=parent.getUserProperties(ESSENCE_CHOOSER_PROPERTIES_ID);
    }
    Filter<Item> filter=filterController.getFilter();
    ObjectChoiceWindowController<Item> chooser=ItemChooser.buildChooser(parent,prefs,essences,filter,filterController);
    Item ret=chooser.editModal();
    chooser.dispose();
    return ret;
  }
}
