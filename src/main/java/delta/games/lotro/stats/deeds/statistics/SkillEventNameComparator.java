package delta.games.lotro.stats.deeds.statistics;

import java.util.Comparator;

/**
 * Comparator for SkillEvent that uses the skill name.
 * @author DAM
 */
public class SkillEventNameComparator implements Comparator<SkillEvent>
{
  @Override
  public int compare(SkillEvent o1, SkillEvent o2)
  {
    String skill1=o1.getSkill();
    String skill2=o2.getSkill();
    return skill1.compareTo(skill2);
  }
}
