package delta.games.lotro.gui.character.storage.wallet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.common.enums.PaperItemCategory;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryNameComparator;
import delta.games.lotro.lore.items.paper.PaperItem;
import delta.games.lotro.lore.items.paper.PaperItemsManager;

/**
 * Wallet-related utility methods.
 * @author DAM
 */
public class WalletUtils
{
  /**
   * Load available categories for paper items.
   * @return A sorted list of categories.
   */
  public static List<PaperItemCategory> getCategories()
  {
    Set<PaperItemCategory> categories=new HashSet<PaperItemCategory>(); 
    List<PaperItem> paperItems=PaperItemsManager.getInstance().getAll();
    for(PaperItem paperItem : paperItems)
    {
      PaperItemCategory category=paperItem.getCategory();
      categories.add(category);
    }
    List<PaperItemCategory> ret=new ArrayList<PaperItemCategory>(categories);
    ret.remove(null);
    Collections.sort(ret,new LotroEnumEntryNameComparator<PaperItemCategory>());
    return ret;
  }
}
