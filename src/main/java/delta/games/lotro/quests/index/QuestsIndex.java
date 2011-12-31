package delta.games.lotro.quests.index;

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
   * @param name Quest name.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean addQuest(String category, String identifier, String name)
  {
    QuestCategory c=_categories.get(category);
    if (c==null)
    {
      c=new QuestCategory(category);
      _categories.put(category,c);
    }
    boolean ok=c.addQuest(identifier,name);
    return ok;
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
