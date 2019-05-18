package delta.games.lotro.gui.common.requirements;

import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.requirements.UsageRequirement;

/**
 * @author dm
 */
public class RequirementsUtils
{
  /**
   * Build a requirement string.
   * @param requirements Requirements to use.
   * @return A string, empty if no requirement.
   */
  public static String buildRequirementString(UsageRequirement requirements)
  {
    CharacterClass requiredClass=requirements.getRequiredClass();
    Race requiredRace=requirements.getRequiredRace();
    Integer minLevel=requirements.getMinLevel();
    Integer maxLevel=requirements.getMaxLevel();
    StringBuilder sb=new StringBuilder();
    // Class
    if (requiredClass!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(requiredClass.getLabel());
    }
    // Race
    if (requiredRace!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(requiredRace.getLabel());
    }
    // Minimum level
    if (minLevel!=null)
    {
      if (sb.length()>0) sb.append(", ");
      if (minLevel.intValue()==1000)
      {
        sb.append("level cap");
      }
      else
      {
        sb.append("level>=").append(minLevel);
      }
    }
    // Maximum level
    if (maxLevel!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("level<=").append(maxLevel);
    }
    String ret=sb.toString();
    if (ret.isEmpty())
    {
      ret="-";
    }
    return ret;
  }


}
