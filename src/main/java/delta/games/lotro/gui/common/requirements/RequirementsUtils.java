package delta.games.lotro.gui.common.requirements;

import java.util.List;

import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.requirements.ClassRequirement;
import delta.games.lotro.common.requirements.RaceRequirement;
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
    StringBuilder sb=new StringBuilder();
    // Class
    ClassRequirement classRequirements=requirements.getClassRequirement();
    if (classRequirements!=null)
    {
      if (sb.length()>0) sb.append(", ");
      List<CharacterClass> characterClasses=classRequirements.getAllowedClasses();
      for(int i=0;i<characterClasses.size();i++)
      {
        if (i>0)
        {
          sb.append('/');
        }
        sb.append(characterClasses.get(i).getLabel());
      }
    }
    // Race
    RaceRequirement raceRequirements=requirements.getRaceRequirement();
    if (raceRequirements!=null)
    {
      if (sb.length()>0) sb.append(", ");
      List<Race> races=raceRequirements.getAllowedRaces();
      for(int i=0;i<races.size();i++)
      {
        if (i>0)
        {
          sb.append('/');
        }
        sb.append(races.get(i).getLabel());
      }
    }
    // Minimum level
    Integer minLevel=requirements.getMinLevel();
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
    Integer maxLevel=requirements.getMaxLevel();
    if (maxLevel!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("level<=").append(maxLevel);
    }
    String ret=sb.toString().trim();
    return ret;
  }
}
