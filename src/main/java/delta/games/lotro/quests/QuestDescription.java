package delta.games.lotro.quests;

import java.util.ArrayList;
import java.util.List;

/**
 * LOTRO quest description.
 * @author DAM
 */
public class QuestDescription
{
  private String _title;
  private String _category;
  private String _questArc;
  private Integer _minimumLevel;
  private String _description;
  private List<String> _prerequisiteQuests;
  private List<String> _nextQuests;

  /**
   * Constructor.
   */
  public QuestDescription()
  {
    _title="";
    _category="";
    _questArc="";
    _minimumLevel=null;
    _description="";
    _prerequisiteQuests=new ArrayList<String>();
    _nextQuests=new ArrayList<String>();
  }

  /**
   * Get the title of this quest.
   * @return the title of this quest.
   */
  public String getTitle()
  {
    return _title;
  }

  /**
   * Set the title of this quest.
   * @param title the title to set.
   */
  public void setTitle(String title)
  {
    _title=title;
  }

  /**
   * Get the category of this quest.
   * @return the category of this quest.
   */
  public String getCategory()
  {
    return _category;
  }

  /**
   * Set the category of this quest. 
   * @param category the category to set.
   */
  public void setCategory(String category)
  {
    _category=category;
  }

  /**
   * Get the quest arc of this quest.
   * @return the quest arc of this quest.
   */
  public String getQuestArc()
  {
    return _questArc;
  }

  /**
   * Set the quest arc of this quest.
   * @param questArc the quest arc to set.
   */
  public void setQuestArc(String questArc)
  {
    _questArc=questArc;
  }

  /**
   * Get the minimum level for this quest.
   * @return the minimumLevel for this quest.
   */
  public Integer getMinimumLevel()
  {
    return _minimumLevel;
  }

  /**
   * Set the minimum level for this quest.
   * @param minimumLevel the minimum level to set.
   */
  public void setMinimumLevel(Integer minimumLevel)
  {
    _minimumLevel=minimumLevel;
  }

  /**
   * Get the description of this quest.
   * @return the description of this quest.
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Set the description of this quest.
   * @param description the description to set.
   */
  public void setDescription(String description)
  {
    _description=description;
  }

  /**
   * Get the list of the 'pre-requisite' quests for this quest. 
   * @return a possibly empty list of quest names.
   */
  public List<String> getPrerequisiteQuests()
  {
    return _prerequisiteQuests;
  }

  /**
   * Add a 'pre-requisite' quest.
   * @param prerequisiteQuest name of quest to add as a 'pre-requisite' quest.
   */
  public void addPrerequisiteQuests(String prerequisiteQuest)
  {
    _prerequisiteQuests.add(prerequisiteQuest);
  }

  /**
   * Get the list of the 'next' quests for this quest. 
   * @return a possibly empty list of quest names.
   */
  public List<String> getNextQuests()
  {
    return _nextQuests;
  }

  /**
   * Add a 'next' quest.
   * @param nextQuest name of quest to add as a 'next' quest.
   */
  public void setNextQuests(String nextQuest)
  {
    _nextQuests.add(nextQuest);
  }

  @Override
  public String toString()
  {
    return _title;
  }
}
