package delta.games.lotro.gui.lore.quests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.common.Repeatability;
import delta.games.lotro.common.RepeatabilityComparator;
import delta.games.lotro.common.enums.QuestCategory;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryNameComparator;
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
  public static List<QuestCategory> getCategories()
  {
    Set<QuestCategory> categories=new HashSet<QuestCategory>(); 
    List<QuestDescription> quests=QuestsManager.getInstance().getAll();
    for(QuestDescription quest : quests)
    {
      QuestCategory questCategory=quest.getCategory();
      categories.add(questCategory);
    }
    List<QuestCategory> ret=new ArrayList<QuestCategory>(categories);
    ret.remove(null);
    Collections.sort(ret,new LotroEnumEntryNameComparator<QuestCategory>());
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

  /**
   * Load available repeatabilities from the quests manager.
   * @return A sorted list of repeatabilities.
   */
  public static List<Repeatability> getRepeatabilities()
  {
    Set<Repeatability> repeatabilities=new HashSet<Repeatability>(); 
    List<QuestDescription> quests=QuestsManager.getInstance().getAll();
    for(QuestDescription quest : quests)
    {
      Repeatability repeatability=quest.getRepeatability();
      repeatabilities.add(repeatability);
    }
    List<Repeatability> ret=new ArrayList<Repeatability>(repeatabilities);
    ret.remove(null);
    Collections.sort(ret,new RepeatabilityComparator());
    return ret;
  }
}
