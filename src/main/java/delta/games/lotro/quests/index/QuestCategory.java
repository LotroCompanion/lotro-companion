package delta.games.lotro.quests.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.utils.LotroLoggers;

/**
 * Quest category.
 * @author DAM
 */
public class QuestCategory
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private String _name;
  private HashMap<String,QuestSummary> _idsToSummary;

  /**
   * Constructor.
   * @param name Category name.
   */
  public QuestCategory(String name)
  {
    _name=name;
    _idsToSummary=new HashMap<String,QuestSummary>();
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
   * @param name Quest name.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean addQuest(String identifier, String name)
  {
    boolean ret;
    QuestSummary old=_idsToSummary.get(identifier);
    if (old==null)
    {
      QuestSummary summary=new QuestSummary(identifier,name);
      _idsToSummary.put(identifier,summary);
      ret=true;
    }
    else
    {
      _logger.warn("Duplicate quest identifier ["+identifier+"] in category ["+_name+"]!");
      ret=false;
    }
    return ret;
  }

  /**
   * Get all the quests of this category.
   * @return A possibly empty array of quest summaries.
   */
  public QuestSummary[] getQuests()
  {
    List<String> ids=new ArrayList<String>(_idsToSummary.keySet());
    Collections.sort(ids);
    QuestSummary[] ret=new QuestSummary[ids.size()];
    int index=0;
    for(String id : ids)
    {
      ret[index]=_idsToSummary.get(id);
      index++;
    }
    return ret;
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
