package delta.games.lotro.gui.quests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Quest-related utility methods.
 * @author DAM
 */
public class QuestsUtils
{
  /**
   * Load available categories from the quests manager.
   * @return A sorted list of quest categories.
   */
  public static List<String> getCategories()
  {
    return getCategories(false);
  }

  /**
   * Load available categories from the quests manager.
   * @param strict Do not include parent categories.
   * @return A sorted list of quest categories.
   */
  public static List<String> getCategories(boolean strict)
  {
    Set<String> categories=new HashSet<String>(); 
    List<QuestDescription> quests=QuestsManager.getInstance().getAll();
    for(QuestDescription quest : quests)
    {
      String questCategory=quest.getCategory();
      if (!strict)
      {
        if (!categories.contains(questCategory))
        {
          categories.add(questCategory);
          if (questCategory!=null)
          {
            while(true)
            {
              int index=questCategory.lastIndexOf(':');
              if (index==-1)
              {
                break;
              }
              questCategory=questCategory.substring(0,index);
              categories.add(questCategory);
            }
          }
        }
      }
      else
      {
        categories.add(questCategory);
      }
    }
    List<String> ret=new ArrayList<String>(categories);
    ret.remove(null);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Load available quest arcs from the quests manager.
   * @return A sorted list of quest arcs.
   */
  public static List<String> getQuestArcs()
  {
    Set<String> questArcs=new HashSet<String>(); 
    List<QuestDescription> quests=QuestsManager.getInstance().getAll();
    for(QuestDescription quest : quests)
    {
      String questArc=quest.getQuestArc();
      questArcs.add(questArc);
    }
    List<String> ret=new ArrayList<String>(questArcs);
    ret.remove(null);
    Collections.sort(ret);
    return ret;
  }
}
