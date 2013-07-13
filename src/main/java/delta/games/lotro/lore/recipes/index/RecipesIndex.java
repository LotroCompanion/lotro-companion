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
  private HashMap<String,HashMap<Integer,HashMap<String,RecipeSummary>>> _sortedRecipes;

  /**
   * Constructor.
   */
  public RecipesIndex()
  {
    _recipes=new HashMap<String,RecipeSummary>();
    _sortedRecipes=new HashMap<String,HashMap<Integer,HashMap<String,RecipeSummary>>>();
  }

  /**
   * Get a recipe by its key. 
   * @param key Key of recipe to get.
   * @return A recipe summary or <code>null</code> if not found.
   */
  public RecipeSummary getRecipe(String key)
  {
    return _recipes.get(key);
  }

  /**
   * Add a recipe.
   * @param key Recipe key.
   * @param name Recipe name.
   * @param profession Associated profession.
   * @param tier Recipe tier.
   */
  public void addRecipe(String key, String name, String profession, int tier)
  {
    RecipeSummary s=_recipes.get(key);
    if (s==null)
    {
      s=new RecipeSummary(key, name, profession, tier);
      _recipes.put(key,s);
      HashMap<Integer,HashMap<String,RecipeSummary>> professionRecipes=_sortedRecipes.get(profession);
      if (professionRecipes==null)
      {
        professionRecipes=new HashMap<Integer,HashMap<String,RecipeSummary>>();
        _sortedRecipes.put(profession,professionRecipes);
      }
      Integer tierKey=Integer.valueOf(tier);
      HashMap<String,RecipeSummary> tierRecipes=professionRecipes.get(tierKey);
      if (tierRecipes==null)
      {
        tierRecipes=new HashMap<String,RecipeSummary>();
        professionRecipes.put(tierKey,tierRecipes);
      }
      tierRecipes.put(key,s);
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

  /**
   * Get all recipe keys for a given profession and tier.
   * @param profession Targeted profession.
   * @param tier Targeted tier.
   * @return An array of keys or <code>null</code> if no such profession/tier.
   */
  public String[] getKeysForProfessionAndTier(String profession, int tier)
  {
    String[] ret=null;
    HashMap<Integer,HashMap<String,RecipeSummary>> professionRecipes=_sortedRecipes.get(profession);
    if (professionRecipes!=null)
    {
      Integer tierKey=Integer.valueOf(tier);
      HashMap<String,RecipeSummary> tierRecipes=professionRecipes.get(tierKey);
      if (tierRecipes!=null)
      {
        Set<String> keys=tierRecipes.keySet();
        ret=keys.toArray(new String[keys.size()]);
        Arrays.sort(ret);
      }
    }
    return ret;
  }

  /**
   * Get all professions.
   * @return An array of professions.
   */
  public String[] getProfessions()
  {
    Set<String> keys=_sortedRecipes.keySet();
    String[] ret=keys.toArray(new String[keys.size()]);
    Arrays.sort(ret);
    return ret;
  }

  /**
   * Get recipe tiers for a given profession.
   * @param profession Targeted profession.
   * @return An array of recipe tiers or <code>null</code> if no such profession.
   */
  public Integer[] getTiersForProfession(String profession)
  {
    Integer[] ret=null;
    HashMap<Integer,HashMap<String,RecipeSummary>> professionRecipes=_sortedRecipes.get(profession);
    if (professionRecipes!=null)
    {
      Set<Integer> keys=professionRecipes.keySet();
      ret=keys.toArray(new Integer[keys.size()]);
      Arrays.sort(ret);
    }
    return ret;
  }
}
