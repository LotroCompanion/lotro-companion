package delta.games.lotro.crafting;

/**
 * Crafting levels utility methods.
 * @author DAM
 */
public class CraftingLevels
{
  private static String[] PROFICIENCY_LABELS = {
    "(none)",
    "Apprentice",
    "Journeyman",
    "Expert",
    "Artisan",
    "Master",
    "Supreme",
    "Westfold" 
  };
  
  private static String[] MASTERY_LABELS = {
    "(none)",
    "Master Apprentice",
    "Master Journeyman",
    "Master Expert",
    "Master Artisan",
    "Grand Master",
    "Supreme Master",
    "Westfold Master" 
  };

  /**
   * Get the label for a given proficiency tier.
   * @param tier Proficiency tier (starting at 0: vocation learned).
   * @return A label.
   */
  public static String getProficiencyLabel(int tier)
  {
    if ((tier>=0) && (tier<PROFICIENCY_LABELS.length)) return PROFICIENCY_LABELS[tier];
    return "???";
  }


  /**
   * Get the label for a given mastery tier.
   * @param tier Mastery tier (starting at 0: vocation learned).
   * @return A label.
   */
  public static String getMasteryLabel(int tier)
  {
    if ((tier>=0) && (tier<MASTERY_LABELS.length)) return MASTERY_LABELS[tier];
    return "???";
  }
}
