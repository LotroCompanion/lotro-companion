package delta.games.lotro.gui.pets;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.collections.pets.CosmeticPetDescription;
import delta.games.lotro.lore.collections.pets.filters.PetNameFilter;

/**
 * Pet filter.
 * @author DAM
 */
public class PetFilter implements Filter<CosmeticPetDescription>
{
  private Filter<CosmeticPetDescription> _filter;

  private PetNameFilter _nameFilter;

  /**
   * Constructor.
   */
  public PetFilter()
  {
    List<Filter<CosmeticPetDescription>> filters=new ArrayList<Filter<CosmeticPetDescription>>();
    // Name
    _nameFilter=new PetNameFilter();
    filters.add(_nameFilter);
    _filter=new CompoundFilter<CosmeticPetDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on pet name.
   * @return a pet name filter.
   */
  public PetNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  @Override
  public boolean accept(CosmeticPetDescription item)
  {
    return _filter.accept(item);
  }
}
