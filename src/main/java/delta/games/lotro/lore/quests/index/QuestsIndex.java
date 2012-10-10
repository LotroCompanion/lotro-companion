package delta.games.lotro.lore.quests.index;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Index for quests.
 * @author DAM
 */
public class QuestsIndex
{
  private HashMap<String,QuestCategory> _categories;

  /**
   * Constructor.
   */
  public QuestsIndex()
  {
    _categories=new HashMap<String,QuestCategory>();
  }

  /**
   * Get a category by its name. 
   * @param name Name of category to get.
   * @return A category or <code>null</code> if not found.
   */
  public QuestCategory getCategory(String name)
  {
    return _categories.get(name);
  }

  /**
   * Add a quest.
   * @param category Quest category.
   * @param identifier Quest identifier.
   * @param key Quest key.
   * @param name Quest name.
   */
  public void addQuest(String category, int identifier, String key, String name)
  {
    QuestCategory c=_categories.get(category);
    if (c==null)
    {
      c=new QuestCategory(category);
      _categories.put(category,c);
    }
    c.addQuest(identifier,key,name);
  }

  /**
   * Get all the categories.
   * @return A possibly empty array of categories.
   */
  public String[] getCategories()
  {
    String[] ret=null;
    Set<String> ids=_categories.keySet();
    if (ids!=null)
    {
      ret=ids.toArray(new String[ids.size()]);
      Arrays.sort(ret);
    }
    else
    {
      ret=new String[0];
    }
    return ret;
  }
}
