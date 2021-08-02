package delta.games.lotro.gui.lore.crafting.recipes;

import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.Profession;

/**
 * Access to recipe icons.
 * @author DAM
 */
public class RecipeIcons
{
  /**
   * Get the icon path for a given profession and tier.
   * @param profession Profession.
   * @param tier Tier, starting at 1.
   * @return An icon or <code>null</code> if not found.
   */
  public static String getIcon(Profession profession, int tier)
  {
    String ret=null;
    if (profession!=null)
    {
      CraftingLevel level=profession.getByTier(tier);
      if (level!=null)
      {
        ret=level.getIcon();
      }
    }
    return ret;
  }
}
