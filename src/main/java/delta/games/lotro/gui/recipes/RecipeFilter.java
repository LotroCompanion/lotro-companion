package delta.games.lotro.gui.recipes;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeCategoryFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeNameFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeProfessionFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeTierFilter;

/**
 * Recipe filter.
 * @author DAM
 */
public class RecipeFilter implements Filter<Recipe>
{
  private Filter<Recipe> _filter;

  private RecipeNameFilter _nameFilter;
  private RecipeProfessionFilter _professionFilter;
  private RecipeTierFilter _tierFilter;
  private RecipeCategoryFilter _categoryFilter;

  /**
   * Constructor.
   */
  public RecipeFilter()
  {
    List<Filter<Recipe>> filters=new ArrayList<Filter<Recipe>>();
    // Name
    _nameFilter=new RecipeNameFilter();
    filters.add(_nameFilter);
    // Profession
    _professionFilter=new RecipeProfessionFilter(null);
    filters.add(_professionFilter);
    // Tier
    _tierFilter=new RecipeTierFilter(null);
    filters.add(_tierFilter);
    // Category
    _categoryFilter=new RecipeCategoryFilter(null);
    filters.add(_categoryFilter);
    _filter=new CompoundFilter<Recipe>(Operator.AND,filters);
  }

  /**
   * Get the filter on recipe name.
   * @return a recipe name filter.
   */
  public RecipeNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the filter on recipe category.
   * @return a recipe category filter.
   */
  public RecipeProfessionFilter getProfessionFilter()
  {
    return _professionFilter;
  }

  /**
   * Get the filter on recipe tier.
   * @return a recipe tier filter.
   */
  public RecipeTierFilter getTierFilter()
  {
    return _tierFilter;
  }

  /**
   * Get the filter on recipe category.
   * @return a recipe category filter.
   */
  public RecipeCategoryFilter getCategoryFilter()
  {
    return _categoryFilter;
  }

  @Override
  public boolean accept(Recipe item)
  {
    return _filter.accept(item);
  }
}
