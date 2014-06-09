package delta.games.lotro.lore.recipes;

import java.io.File;

import org.apache.log4j.Logger;

import delta.common.utils.cache.WeakReferencesCache;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.Config;
import delta.games.lotro.lore.recipes.io.xml.RecipeXMLParser;
import delta.games.lotro.lore.recipes.io.xml.RecipeXMLWriter;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Facade for recipes access.
 * @author DAM
 */
public class RecipesManager
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static RecipesManager _instance=new RecipesManager();
  
  private WeakReferencesCache<Integer,Recipe> _cache;
  
  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static RecipesManager getInstance()
  {
    return _instance;
  }

  /**
   * Private constructor.
   */
  private RecipesManager()
  {
    _cache=new WeakReferencesCache<Integer,Recipe>(100);
  }

  /**
   * Get a recipe using its identifier.
   * @param id Recipe identifier.
   * @return A recipe description or <code>null</code> if not found.
   */
  public Recipe getRecipe(Integer id)
  {
    Recipe ret=null;
    if (id!=null)
    {
      ret=(_cache!=null)?_cache.getObject(id):null;
      if (ret==null)
      {
        ret=loadRecipe(id.intValue());
        if (ret!=null)
        {
          if (_cache!=null)
          {
            _cache.registerObject(id,ret);
          }
        }
      }
    }
    return ret;
  }

  private Recipe loadRecipe(int id)
  {
    Recipe ret=null;
    File recipeFile=getRecipeFile(id);
    if (recipeFile.exists())
    {
      if (recipeFile.length()>0)
      {
        RecipeXMLParser parser=new RecipeXMLParser();
        ret=parser.parseXML(recipeFile);
        if (ret!=null)
        {
          ret.setIdentifier(id);
        }
        else
        {
          _logger.error("Cannot load recipe file ["+recipeFile+"]!");
        }
      }
    }
    return ret;
  }

  /**
   * Write a recipe file.
   * @param recipe Recipe to write.
   * @return A recipe.
   */
  public Recipe writeItemFile(Recipe recipe)
  {
    RecipeXMLWriter writer=new RecipeXMLWriter();
    int id=recipe.getIdentifier();
    File recipeFile=getRecipeFile(id);
    boolean ok=writer.write(recipeFile,recipe,EncodingNames.UTF_8);
    if (!ok)
    {
      String name=recipe.getName();
      _logger.error("Write failed for recipe ["+name+"]!");
      recipe=null;
    }
    else
    {
      // Reload recipe to cleanup memory retention of web-loaded strings
      recipe=loadRecipe(id);
    }
    return recipe;
  }

  private File getRecipeFile(int id)
  {
    File recipesDir=Config.getInstance().getRecipesDir();
    String fileName=id+".xml";
    File ret=new File(recipesDir,fileName);
    return ret;
  }
}
