package delta.games.lotro.gui.character.status.crafting.synopsis;

import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.ProfessionFilter;

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

  /**
   * Indicates if this filter accepts the given item.
   * @return <code>true</code> if the profession filter accepts the profession of the given item,
   * <code>false</code> otherwise.
   */
  @Override
  public boolean accept(CraftingSynopsisItem item)
  {
    Profession profession=item.getProfession();
    return _professionFilter.accept(profession);
  }
}
