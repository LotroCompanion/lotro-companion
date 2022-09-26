package delta.games.lotro.gui.character.storage.wallet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  public static List<String> getCategories()
  {
    Set<String> categories=new HashSet<String>(); 
    List<PaperItem> paperItems=PaperItemsManager.getInstance().getAll();
    for(PaperItem paperItem : paperItems)
    {
      String category=paperItem.getCategory();
      categories.add(category);
    }
    List<String> ret=new ArrayList<String>(categories);
    ret.remove(null);
    Collections.sort(ret);
    return ret;
  }
}
