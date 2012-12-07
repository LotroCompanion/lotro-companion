package delta.games.lotro.lore.quests;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.common.Rewards;
import delta.games.lotro.common.SIZE;

/**
 * LOTRO quest description.
 * @author DAM
 */
public class QuestDescription
{
  /**
   * Quest type.
   * @author DAM
   */
  public enum TYPE
  {
    /**
     * Standard non-epic quest.
     */
    STANDARD,
    /**
     * Epic quest.
     */
    EPIC
  }

  /**
   * Faction.
   * @author DAM
   */
  public enum FACTION
  {
    /**
     * Free peoples.
     */
    FREE_PEOPLES,
    /**
     * Monster play.
     */
    MONSTER_PLAY
  }

  private int _identifier;
  private String _key;
  private String _title;
  private String _category;
  private String _scope;
  private String _questArc;
  private Integer _minimumLevel;
  private Integer _maximumLevel;
  private List<String> _requiredClasses;
  private List<String> _requiredRaces;
  private TYPE _type;
  private SIZE _size;
  private FACTION _faction;
  private boolean _repeatable;
  private boolean _instanced;
  private String _description;
  private String _bestower;
  private String _bestowerText;
  private String _objectives;
  private List<String> _prerequisiteQuests;
  private List<String> _nextQuests;
  private Rewards _rewards;

  /**
   * Constructor.
   */
  public QuestDescription()
  {
    _identifier=0;
    _key=null;
    _title="";
    _category="";
    _scope="";
    _questArc="";
    _minimumLevel=null;
    _maximumLevel=null;
    _requiredClasses=null;
    _type=TYPE.STANDARD;
    _size=SIZE.SOLO;
    _faction=FACTION.FREE_PEOPLES;
    _repeatable=false;
    _instanced=false;
    _description="";
    _bestower="";
    _bestowerText="";
    _objectives="";
    _prerequisiteQuests=new ArrayList<String>();
    _nextQuests=new ArrayList<String>();
    _rewards=new Rewards();
  }

  /**
   * Get the identifier of this quest.
   * @return the identifier of this quest.
   */
  public int getIdentifier()
  {
    return _identifier;
  }

  /**
   * Set the identifier of this quest.
   * @param identifier the identifier to set.
   */
  public void setIdentifier(int identifier)
  {
    _identifier=identifier;
  }

  /**
   * Get the key of this quest.
   * @return the key of this quest.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Set the key of this quest.
   * @param key the key to set.
   */
  public void setKey(String key)
  {
    _key=key;
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
   * Get the scope of this quest.
   * @return the scope of this quest.
   */
  public String getQuestScope()
  {
    return _scope;
  }

  /**
   * Set the scope of this quest.
   * @param scope the scope to set.
   */
  public void setQuestScope(String scope)
  {
    _scope=scope;
  }

  /**
   * Get the arc of this quest.
   * @return the arc of this quest.
   */
  public String getQuestArc()
  {
    return _questArc;
  }

  /**
   * Set the arc of this quest.
   * @param questArc the arc to set.
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
   * Get the maximum level for this quest.
   * @return the maximumLevel for this quest.
   */
  public Integer getMaximumLevel()
  {
    return _maximumLevel;
  }

  /**
   * Set the maximum level for this quest.
   * @param maximumLevel the maxiimum level to set.
   */
  public void setMaximumLevel(Integer maximumLevel)
  {
    _maximumLevel=maximumLevel;
  }

  /**
   * Get a list of required classes.
   * @return a list of class names or <code>null</code> if none.
   */
  public List<String> getRequiredClasses()
  {
    return _requiredClasses;
  }

  /**
   * Add a required class.
   * @param className Name of class to add.
   */
  public void addRequiredClass(String className)
  {
    if (_requiredClasses==null)
    {
      _requiredClasses=new ArrayList<String>();
    }
    _requiredClasses.add(className);
  }

  /**
   * Get a list of required races.
   * @return a list of class names or <code>null</code> if none.
   */
  public List<String> getRequiredRaces()
  {
    return _requiredRaces;
  }

  /**
   * Add a required race.
   * @param race Name of race to add.
   */
  public void addRequiredRace(String race)
  {
    if (_requiredRaces==null)
    {
      _requiredRaces=new ArrayList<String>();
    }
    _requiredRaces.add(race);
  }

  /**
   * Get quest type.
   * @return the quest type.
   */
  public TYPE getType()
  {
    return _type;
  }

  /**
   * Set the quest type.
   * @param type the type to set.
   */
  public void setType(TYPE type)
  {
    _type=type;
  }

  /**
   * Get the quest size.
   * @return the quest size.
   */
  public SIZE getSize()
  {
    return _size;
  }

  /**
   * Set the quest size.
   * @param size the size to set.
   */
  public void setSize(SIZE size)
  {
    _size=size;
  }

  /**
   * Get the quest faction.
   * @return the quest faction.
   */
  public FACTION getFaction()
  {
    return _faction;
  }

  /**
   * Set the quest faction.
   * @param faction the faction to set.
   */
  public void setFaction(FACTION faction)
  {
    _faction=faction;
  }

  /**
   * Indicates if this quest is repeatable or not.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isRepeatable()
  {
    return _repeatable;
  }

  /**
   * Set the 'repeatable' flag.
   * @param repeatable value to set.
   */
  public void setRepeatable(boolean repeatable)
  {
    _repeatable=repeatable;
  }

  /**
   * Indicates if this quest is instanced or not.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isInstanced()
  {
    return _instanced;
  }

  /**
   * Set the 'instanced' flag.
   * @param instanced value to set.
   */
  public void setInstanced(boolean instanced)
  {
    _instanced=instanced;
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
   * Get the bestower of this quest.
   * @return the bestower of this quest.
   */
  public String getBestower()
  {
    return _bestower;
  }

  /**
   * Set the bestower of this quest.
   * @param bestower the bestower to set.
   */
  public void setBestower(String bestower)
  {
    _bestower=bestower;
  }

  /**
   * Get the bestower text of this quest.
   * @return the bestower text of this quest.
   */
  public String getBestowerText()
  {
    return _bestowerText;
  }

  /**
   * Set the bestower text of this quest.
   * @param bestowerText the bestower text to set.
   */
  public void setBestowerText(String bestowerText)
  {
    _bestowerText=bestowerText;
  }

  /**
   * Get the objectives of this quest.
   * @return the objectives of this quest.
   */
  public String getObjectives()
  {
    return _objectives;
  }

  /**
   * Set the objectives of this quest.
   * @param objectives the objectives to set.
   */
  public void setObjectives(String objectives)
  {
    _objectives=objectives;
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
  public void addPrerequisiteQuest(String prerequisiteQuest)
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
  public void addNextQuest(String nextQuest)
  {
    _nextQuests.add(nextQuest);
  }

  /**
   * Get the rewards for this quest.
   * @return the rewards.
   */
  public Rewards getQuestRewards()
  {
    return _rewards;
  }

  /**
   * Dump the contents of this quest as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Title: ").append(_title);
    if (_identifier!=0)
    {
      sb.append(" (");
      sb.append(_identifier);
      sb.append(')');
    }
    if (_key!=null)
    {
      sb.append(" (");
      sb.append(_key);
      sb.append(')');
    }
    if (_type!=TYPE.STANDARD)
    {
      sb.append(" (");
      sb.append(_type);
      sb.append(')');
    }
    if (_size!=SIZE.SOLO)
    {
      sb.append(" (");
      sb.append(_size);
      sb.append(')');
    }
    if (_faction!=FACTION.FREE_PEOPLES)
    {
      sb.append(" (");
      sb.append(_faction);
      sb.append(')');
    }
    if (_repeatable)
    {
      sb.append(" (repeatable)");
    }
    if (_instanced)
    {
      sb.append(" (instanced)");
    }
    sb.append(EndOfLine.NATIVE_EOL);
    if (_category.length()>0)
    {
      sb.append("Category: ").append(_category).append(EndOfLine.NATIVE_EOL);
    }
    if (_scope.length()>0)
    {
      sb.append("Scope: ").append(_scope).append(EndOfLine.NATIVE_EOL);
    }
    if (_questArc.length()>0)
    {
      sb.append("Arc: ").append(_questArc).append(EndOfLine.NATIVE_EOL);
    }
    if (_minimumLevel!=null)
    {
      sb.append("Minimum level: ").append(_minimumLevel).append(EndOfLine.NATIVE_EOL);
    }
    if (_maximumLevel!=null)
    {
      sb.append("Maximum level: ").append(_maximumLevel).append(EndOfLine.NATIVE_EOL);
    }
    if (_requiredClasses!=null)
    {
      sb.append("Required class(es): ").append(_requiredClasses).append(EndOfLine.NATIVE_EOL);
    }
    if (_requiredRaces!=null)
    {
      sb.append("Required race(s): ").append(_requiredRaces).append(EndOfLine.NATIVE_EOL);
    }
    if (_prerequisiteQuests.size()>0)
    {
      sb.append("Prerequisites: ").append(_prerequisiteQuests).append(EndOfLine.NATIVE_EOL);
    }
    if (_nextQuests.size()>0)
    {
      sb.append("Next quests: ").append(_nextQuests).append(EndOfLine.NATIVE_EOL);
    }
    sb.append("Rewards: ").append(_rewards).append(EndOfLine.NATIVE_EOL);
    sb.append("Description: ").append(_description).append(EndOfLine.NATIVE_EOL);
    sb.append("Bestower: ").append(_bestower).append(EndOfLine.NATIVE_EOL);
    sb.append("Bestower text: ").append(_bestowerText).append(EndOfLine.NATIVE_EOL);
    sb.append("Objectives: ").append(_objectives).append(EndOfLine.NATIVE_EOL);
    return sb.toString();
  }

  @Override
  public String toString()
  {
    return _title;
  }
}
