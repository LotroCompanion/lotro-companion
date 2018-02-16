package delta.games.lotro.gui.deed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedRewardsExplorer;
import delta.games.lotro.lore.deeds.DeedsManager;

/**
 * Deed-related utility methods.
 * @author DAM
 */
public class DeedUtils
{
  /**
   * Load available categories from deeds manager.
   * @return A sorted list of deed categories.
   */
  public static List<String> getCategories()
  {
    Set<String> categories=new HashSet<String>(); 
    List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
    for(DeedDescription deed : deeds)
    {
      categories.add(deed.getCategory());
    }
    List<String> ret=new ArrayList<String>(categories);
    ret.remove(null);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Load available titles from deeds manager.
   * @return A sorted list of deed categories.
   */
  public static List<String> getTitles()
  {
    List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
    DeedRewardsExplorer explorer=new DeedRewardsExplorer();
    explorer.doIt(deeds);
    return explorer.getTitles();
  }
}
