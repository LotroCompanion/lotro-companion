package delta.games.lotro.gui.recipes;

import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Profession;

/**
 * Access to recipe icons.
 * @author DAM
 */
public class RecipeIcons
{
  /**
   * Get the icon path for a given profession and tier.
   * @param professionName Profession.
   * @param tier Tier, starting at 1.
   * @return An icon or <code>null</code> if not found.
   */
  public static String getIcon(String professionName, int tier)
  {
    String ret=null;
    CraftingData crafting=CraftingSystem.getInstance().getData();
    Profession profession=crafting.getProfessionsRegistry().getProfessionByName(professionName);
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
