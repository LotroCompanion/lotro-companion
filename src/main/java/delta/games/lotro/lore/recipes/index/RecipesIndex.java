package delta.games.lotro.lore.recipes.index;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Index for recipes.
 * @author DAM
 */
public class RecipesIndex
{
  private HashMap<String,RecipeSummary> _recipes;

  /**
   * Constructor.
   */
  public RecipesIndex()
  {
    _recipes=new HashMap<String,RecipeSummary>();
  }

  /**
   * Get a category by its name. 
   * @param name Name of category to get.
   * @return A category or <code>null</code> if not found.
   */
  public RecipeSummary getRecipe(String name)
  {
    return _recipes.get(name);
  }

  /**
   * Add a recipe.
   * @param key Recipe key.
   * @param name Recipe name.
   */
  public void addRecipe(String key, String name, String profession, int tier)
  {
    RecipeSummary s=_recipes.get(key);
    if (s==null)
    {
      s=new RecipeSummary(key, name, profession, tier);
      _recipes.put(key,s);
    }
  }

  /**
   * Get all the recipe keys.
   * @return A possibly empty array of recipe keys.
   */
  public String[] getKeys()
  {
    String[] ret=null;
    Set<String> ids=_recipes.keySet();
    if (ids!=null)
    {
      ret=ids.toArray(new String[ids.size()]);
      Arrays.sort(ret);
    }
    else
    {
      ret=new String[0];
    }
    return ret;
  }
}
