package delta.games.lotro.lore.quests.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Quest category.
 * @author DAM
 */
public class QuestCategory
{
  private String _name;
  private HashMap<String,List<QuestSummary>> _idsToSummary;

  /**
   * Constructor.
   * @param name Category name.
   */
  public QuestCategory(String name)
  {
    _name=name;
    _idsToSummary=new HashMap<String,List<QuestSummary>>();
  }
  
  /**
   * Get the name of this category.
   * @return a category name.
   */
  public String getName()
  {
    return _name;
  }
  
  /**
   * Add a quest.
   * @param identifier Quest identifier.
   * @param key Quest key.
   * @param name Quest name.
   */
  public void addQuest(int identifier, String key, String name)
  {
    List<QuestSummary> list=_idsToSummary.get(key);
    if (list==null)
    {
      list=new ArrayList<QuestSummary>();
      _idsToSummary.put(key,list);
    }
    QuestSummary summary=new QuestSummary(identifier,key,name);
    list.add(summary);
  }

  /**
   * Get all the quests of this category.
   * @return A possibly empty array of quest summaries.
   */
  public QuestSummary[] getQuests()
  {
    List<String> ids=new ArrayList<String>(_idsToSummary.keySet());
    Collections.sort(ids);
    List<QuestSummary> summaries=new ArrayList<QuestSummary>();
    for(String id : ids)
    {
      summaries.addAll(_idsToSummary.get(id));
    }
    QuestSummary[] ret=summaries.toArray(new QuestSummary[summaries.size()]);
    return ret;
  }

  /**
   * Get all the keys of this category.
   * @return A possibly empty array of quest keys.
   */
  public String[] getKeys()
  {
    List<String> ids=new ArrayList<String>(_idsToSummary.keySet());
    Collections.sort(ids);
    String[] ret=ids.toArray(new String[ids.size()]);
    return ret;
  }
  
  @Override
  public String toString()
  {
    return _name;
  }
}
