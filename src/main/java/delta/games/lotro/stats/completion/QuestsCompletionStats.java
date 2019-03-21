package delta.games.lotro.stats.completion;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;
import delta.games.lotro.lore.quests.index.QuestCategory;
import delta.games.lotro.lore.quests.index.QuestSummary;
import delta.games.lotro.lore.quests.index.QuestsIndex;

/**
 * Statistics on quests completion for a single category on a single toon.
 * @author DAM
 */
public class QuestsCompletionStats
{
  private static final Logger LOGGER=Logger.getLogger(QuestsCompletionStats.class);

  private static final boolean USE_CLASS_RESTRICTIONS=true;
  private static final boolean USE_RACE_RESTRICTIONS=true;
  private static final boolean USE_INSTANCES=false;
  private String _name;
  private String _category;
  private CharacterSummary _character;
  private int _nbExpectedQuests;
  private int _nbQuestsDone;
  private List<Integer> _expectedIds;
  private HashMap<Integer,Integer> _nbTimesPerQuest;

  /**
   * Constructor.
   * @param category Category to use.
   * @param summary Targeted character.
   * @param log Character log to use.
   */
  public QuestsCompletionStats(String category, CharacterSummary summary, CharacterLog log)
  {
    _name=log.getName();
    _category=category;
    _character=summary;
    reset();
    List<CharacterLogItem> items=getQuestItems(log);
    loadQuestIdentifiers();
    parseQuestItems(items);
  }

  private void reset()
  {
    _nbExpectedQuests=0;
    _nbQuestsDone=0;
    _expectedIds=new ArrayList<Integer>();
    _nbTimesPerQuest=new HashMap<Integer,Integer>();
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
        for(QuestSummary summary : summaries)
        {
          int id=summary.getIdentifier();
          QuestDescription q=qm.getQuest(id);
          if (q!=null)
          {
            boolean useIt=true;
            if (USE_CLASS_RESTRICTIONS)
            {
              List<String> classes=q.getRequiredClasses();
              if ((classes!=null) && (classes.size()>0))
              {
                CharacterClass cClass=_character.getCharacterClass();
                String className=cClass.getKey();
                if (classes.contains(className))
                {
                  useIt=true;
                }
                else
                {
                  String key=q.getKey();
                  if (LOGGER.isInfoEnabled())
                  {
                    LOGGER.info("Ignored quest ["+key+"]. Class="+className+", Required:"+classes);
                  }
                  useIt=false;
                }
              }
            }
            if (USE_RACE_RESTRICTIONS)
            {
              List<String> races=q.getRequiredRaces();
              if ((races!=null) && (races.size()>0))
              {
                Race cRace=_character.getRace();
                String raceName=cRace.getLabel();
                if (races.contains(raceName))
                {
                  useIt=true;
                }
                else
                {
                  String key=q.getKey();
                  if (LOGGER.isInfoEnabled())
                  {
                    LOGGER.info("Ignored quest ["+key+"]. Race="+raceName+", Required:"+races);
                  }
                  useIt=false;
                }
              }
            }
            if (!USE_INSTANCES)
            {
              boolean instanced=q.isInstanced();
              if (instanced)
              {
                useIt=false;
              }
            }
            if (useIt)
            {
              _expectedIds.add(Integer.valueOf(id));
            }
          }
        }
        _nbExpectedQuests=_expectedIds.size();
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
      Integer id=item.getResourceIdentifier();
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
      QuestsManager qm=QuestsManager.getInstance();
      for(Integer id : _expectedIds)
      {
        int nb=0;
        Integer n=_nbTimesPerQuest.get(id);
        if (n!=null)
        {
          nb=n.intValue();
        }
        QuestDescription q=qm.getQuest(id.intValue());
        String name=null;
        if (q!=null)
        {
          name=q.getKey();
        }
        ps.println(nb+": "+name);
      }
    }
  }
}
