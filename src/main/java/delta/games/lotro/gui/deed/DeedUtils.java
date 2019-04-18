package delta.games.lotro.gui.deed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.lore.deeds.DeedDescription;
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
    return getCategories(false);
  }

  /**
   * Load available categories from deeds manager.
   * @param strict Do not include parent categories.
   * @return A sorted list of deed categories.
   */
  public static List<String> getCategories(boolean strict)
  {
    Set<String> categories=new HashSet<String>(); 
    List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
    for(DeedDescription deed : deeds)
    {
      String deedCategory=deed.getCategory();
      if (!strict)
      {
        if (!categories.contains(deedCategory))
        {
          categories.add(deedCategory);
          if (deedCategory!=null)
          {
            while(true)
            {
              int index=deedCategory.lastIndexOf(':');
              if (index==-1)
              {
                break;
              }
              deedCategory=deedCategory.substring(0,index);
              categories.add(deedCategory);
            }
          }
        }
      }
      else
      {
        categories.add(deedCategory);
      }
    }
    List<String> ret=new ArrayList<String>(categories);
    ret.remove(null);
    Collections.sort(ret);
    return ret;
  }
}
