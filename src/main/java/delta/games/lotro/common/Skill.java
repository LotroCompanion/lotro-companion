package delta.games.lotro.common;

import delta.common.utils.text.EndOfLine;

/**
 * Skill.
 * @author DAM
 */
public class Skill
{
  /**
   * Skill type.
   * @author DAM
   */
  public enum SkillType
  {
    /**
     * Passive.
     */
    PASSIVE,
    /**
     * Active.
     */
    ACTIVE
  }

  private SkillType _type;
  private String _identifier;
  private String _name;

  /**
   * Constructor.
   * @param type Skill type.
   * @param identifier Internal identifier.
   * @param name Skill's name.
   */
  public Skill(SkillType type, String identifier, String name)
  {
    _type=type;
    _identifier=identifier;
    _name=name;
  }
  
  /**
   * Get the internal identifier.
   * @return the internal identifier.
   */
  public String getIdentifier()
  {
    return _identifier;
  }

  /**
   * Get the skill's name.
   * @return the skill's name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get skill type.
   * @return the skill type.
   */
  public SkillType getType()
  {
    return _type;
  }

  /**
   * Dump the contents of this trait as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Skill: ").append(_name);
    sb.append(" (").append(_type).append(')');
    sb.append(" (").append(_identifier).append(')');
    sb.append(EndOfLine.NATIVE_EOL);
    return sb.toString();
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
