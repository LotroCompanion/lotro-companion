package delta.games.lotro.crafting;

import java.util.HashMap;

/**
 * Represents a level in a crafting profession.
 * @author DAM
 */
public class CraftingLevel
{
  private static HashMap<Integer,CraftingLevel> _registry=new HashMap<Integer,CraftingLevel>();

  /**
   * Beginner.
   */
  public static final CraftingLevel BEGINNER=new CraftingLevel(0,"Beginner",0,"Beginner",0);
  /**
   * Apprentice.
   */
  public static final CraftingLevel APPRENTICE=new CraftingLevel(1,"Apprentice",200,"Master Apprentice",400);
  /**
   * Journeyman.
   */
  public static final CraftingLevel JOURNEYMAN=new CraftingLevel(2,"Journeyman",280,"Master Journeyman",560);
  /**
   * Expert.
   */
  public static final CraftingLevel EXPERT=new CraftingLevel(3,"Expert",360,"Master Expert",720);
  /**
   * Artisan.
   */
  public static final CraftingLevel ARTISAN=new CraftingLevel(4,"Artisan",440,"Master Artisan",880);
  /**
   * Master.
   */
  public static final CraftingLevel MASTER=new CraftingLevel(5,"Master",520,"Grand Master",1040);
  /**
   * Supreme.
   */
  public static final CraftingLevel SUPREME=new CraftingLevel(6,"Supreme",600,"Supreme Master",1200);
  /**
   * Westfold.
   */
  public static final CraftingLevel WESTFOLD=new CraftingLevel(7,"Westfold",680,"Westfold Master",1360);
  /**
   * Eastemnet.
   */
  public static final CraftingLevel EASTEMNET=new CraftingLevel(8,"Eastemnet",760,"Eastemnet Master",1520);

  private int _tier;
  private String _label;
  private String _masteryLabel;
  private int _proficiencyXP;
  private int _masteryXP;

  private CraftingLevel(int tier, String label, int proficiencyXP, String masteryLabel, int masteryXP)
  {
    _tier=tier;
    _label=label;
    _proficiencyXP=proficiencyXP;
    _masteryLabel=masteryLabel;
    _masteryXP=masteryXP;
    _registry.put(Integer.valueOf(tier),this);
  }

  /**
   * Get the associated tier value.
   * @return A tier number.
   */
  public int getTier()
  {
    return _tier;
  }

  /**
   * Get the title for proficiency in this level.
   * @return A title.
   */
  public String getProficiencyLabel()
  {
    return _label;
  }
  
  /**
   * Get the XP for proficiency.
   * @return an XP value.
   */
  public int getProficiencyXP()
  {
    return _proficiencyXP;
  }
  
  /**
   * Get the title for proficiency in this level.
   * @return A title.
   */
  public String getMasteryLabel()
  {
    return _masteryLabel;
  }
  
  /**
   * Get the XP for mastery.
   * @return an XP value.
   */
  public int getMasteryXP()
  {
    return _masteryXP;
  }

  @Override
  public String toString()
  {
    return _label;
  }

  /**
   * Get a crafting level instance by tier.
   * @param tier Tier of the crafting level to get.
   * @return A crafting level instance or <code>null</code> if <code>tier</code> is not known.
   */
  public static CraftingLevel getByTier(int tier)
  {
    CraftingLevel level=_registry.get(Integer.valueOf(tier));
    return level;
  }
  
  /**
   * Get the maximum level.
   * @return the maximum level.
   */
  public static CraftingLevel getMaximumLevel()
  {
    return EASTEMNET;
  }
}
