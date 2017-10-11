package delta.games.lotro.gui.stats.crafting.synopsis;

import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.crafting.Profession;
import delta.games.lotro.crafting.ProfessionFilter;

/**
 * Filter for crafting synopsis items.
 * @author DAM
 */
public class CraftingSynopsisItemFilter implements Filter<CraftingSynopsisItem>
{
  private ProfessionFilter _professionFilter;

  /**
   * Constructor.
   * @param professionFilter Profession filter to use.
   */
  public CraftingSynopsisItemFilter(ProfessionFilter professionFilter)
  {
    _professionFilter=professionFilter;
  }

  public boolean accept(CraftingSynopsisItem item)
  {
    Profession profession=item.getProfession();
    return _professionFilter.accept(profession);
  }
}
