package delta.games.lotro.stats.completion;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.quests.QuestsManager;
import delta.games.lotro.quests.index.QuestCategory;
import delta.games.lotro.quests.index.QuestSummary;
import delta.games.lotro.quests.index.QuestsIndex;

/**
 * Statistics on quests completion for a single category on a single toon.
 * @author DAM
 */
public class QuestsCompletionStats
{
  private String _name;
  private String _category;
  private int _nbExpectedQuests;
  private int _nbQuestsDone;
  private List<String> _expectedIds;
  private List<String> _gotIds;
  private HashMap<String,Integer> _nbTimesPerQuest;

  /**
   * Constructor.
   * @param category Category to use.
   * @param log Character log to use.
   */
  public QuestsCompletionStats(String category, CharacterLog log)
  {
    _name=log.getName();
    _category=category;
    reset();
    List<CharacterLogItem> items=getQuestItems(log);
    loadQuestIdentifiers();
    parseQuestItems(items);
  }

  private void reset()
  {
    _nbExpectedQuests=0;
    _nbQuestsDone=0;
    _expectedIds=new ArrayList<String>();
    _gotIds=new ArrayList<String>();
    _nbTimesPerQuest=new HashMap<String,Integer>();
  }

  private void loadQuestIdentifiers()
  {
    QuestsManager qm=QuestsManager.getInstance();
    QuestsIndex index=qm.getIndex();
    if (index!=null)
    {
      QuestCategory category=index.getCategory(_category);
      if (category!=null)
      {
        QuestSummary[] summaries=category.getQuests();
        _nbExpectedQuests=summaries.length;
        for(QuestSummary summary : summaries)
        {
          String id=summary.getId();
          _expectedIds.add(id);
        }
      }
    }
  }

  private List<CharacterLogItem> getQuestItems(CharacterLog log)
  {
    List<CharacterLogItem> ret=new ArrayList<CharacterLogItem>();
    int nb=log.getNbItems();
    for(int i=0;i<nb;i++)
    {
      CharacterLogItem item=log.getLogItem(i);
      if ((item!=null) && (item.getLogItemType()==LogItemType.QUEST))
      {
        ret.add(item);
      }
    }
    return ret;
  }

  private void parseQuestItems(List<CharacterLogItem> items)
  {
    for(CharacterLogItem item : items)
    {
      String id=item.getIdentifier();
      if (_expectedIds.contains(id))
      {
        Integer nb=_nbTimesPerQuest.get(id);
        if (nb==null)
        {
          nb=Integer.valueOf(1);
        }
        else
        {
          nb=Integer.valueOf(nb.intValue()+1);
        }
        _nbTimesPerQuest.put(id,nb);
      }
    }
    _nbQuestsDone=_nbTimesPerQuest.size();
    _gotIds.addAll(_nbTimesPerQuest.keySet());
  }

  /**
   * Get completion percentage.
   * @return the completion percentage.
   */
  public double getPercentage()
  {
    return (_nbQuestsDone*100.0)/_nbExpectedQuests;
  }

  /**
   * Dump the contents of this object to the given stream.
   * @param ps Output stream to use.
   * @param verbose Verbose output or not.
   */
  public void dump(PrintStream ps, boolean verbose)
  {
    ps.println("Quest completion for ["+_name+"], category ["+_category+"]");
    ps.println("Nb quests: expected="+_nbExpectedQuests+", done="+_nbQuestsDone+": "+getPercentage()+"%");
    if (verbose)
    {
      for(String id : _expectedIds)
      {
        int nb=0;
        Integer n=_nbTimesPerQuest.get(id);
        if (n!=null)
        {
          nb=n.intValue();
        }
        System.out.println(nb+": "+id);
      }
    }
  }
}
