package delta.games.lotro.tools.lore.recipes;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.files.FilesDeleter;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.lore.recipes.Recipe;
import delta.games.lotro.lore.recipes.index.RecipesIndex;
import delta.games.lotro.lore.recipes.index.io.xml.RecipesIndexXMLParser;
import delta.games.lotro.lore.recipes.io.web.RecipePageParser;
import delta.games.lotro.lore.recipes.io.xml.RecipeXMLWriter;
import delta.games.lotro.utils.Escapes;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Recipes loader.
 * @author DAM
 */
public class RecipesLoader
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();
  
  private File _recipesDir;
  private File _indexFile;
  private RecipesIndex _index;
  private int _totalNbRecipesToLoad;
  private int _totalNbRecipesLoaded;

  /**
   * Constructor.
   * @param recipesDir Directory to write recipes to.
   * @param indexFile Index file.
   */
  public RecipesLoader(File recipesDir, File indexFile)
  {
    _recipesDir=recipesDir;
    _indexFile=indexFile;
  }

  /**
   * Do load recipes.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt()
  {
    boolean ok=init();
    if (ok)
    {
      loadRecipes();
      System.out.println("Recipes loaded: "+_totalNbRecipesLoaded+"/"+_totalNbRecipesToLoad+".");
    }
    return ok;
  }

  private boolean init()
  {
    // Build work directory
    if (_recipesDir.exists())
    {
      FilesDeleter deleter=new FilesDeleter(_recipesDir,null,true);
      deleter.doIt();
    }
    boolean ret=_recipesDir.mkdirs();
    if (!ret)
    {
      _logger.error("Cannot empty work directory ["+_recipesDir+"]!");
    }
    // Load recipes index
    RecipesIndexXMLParser parser=new RecipesIndexXMLParser();
    _index=parser.parseXML(_indexFile);
    if (_index==null)
    {
      _logger.error("Cannot read recipes index!");
    }
    ret=(_index!=null);
    return ret;
  }

  private void loadRecipes()
  {
    String[] professions=_index.getProfessions();
    for(String profession : professions)
    {
      loadRecipesForProfession(profession);
    }
  }

  private void loadRecipesForProfession(String profession)
  {
    System.out.println("Profession ["+profession+"]");
    Integer[] tiers=_index.getTiersForProfession(profession);
    for(Integer tier : tiers)
    {
      String[] keys=_index.getKeysForProfessionAndTier(profession,tier.intValue());
      int nbRecipesToLoad=keys.length;
      int nbRecipesLoaded=0;
      for(String key : keys)
      {
        int nbRecipes=loadRecipeDefinition(key);
        nbRecipesLoaded+=nbRecipes;
      }
      _totalNbRecipesToLoad+=nbRecipesToLoad;
      _totalNbRecipesLoaded+=nbRecipesLoaded;
      System.out.println("Profession ["+profession+"], tier "+tier+": "+nbRecipesLoaded+"/"+nbRecipesToLoad+".");
    }
  }

  /**
   * Load recipe definition.
   * @param key Recipe key.
   * @return Number of loaded recipes.
   */
  private int loadRecipeDefinition(String key)
  {
    int nbRecipes=0;
    RecipePageParser parser=new RecipePageParser();
    String escapedKey=Escapes.escapeIdentifier(key);
    String url="http://lorebook.lotro.com/wiki/Recipe:"+escapedKey;
    List<Recipe> recipes=parser.parseRecipePage(url);
    if ((recipes!=null) && (recipes.size()>0))
    {
      RecipeXMLWriter writer=new RecipeXMLWriter();
      for(Recipe recipe : recipes)
      {
        // Write recipe
        int id=recipe.getIdentifier();
        String fileName=String.valueOf(id)+".xml";
        File recipeFile=new File(_recipesDir,fileName);
        if (!recipeFile.getParentFile().exists())
        {
          recipeFile.getParentFile().mkdirs();
        }
        String name=recipe.getName();
        boolean ok=writer.write(recipeFile,recipe,EncodingNames.UTF_8);
        if (ok)
        {
          System.out.println("Wrote recipe ["+name+"]");
          nbRecipes++;
        }
        else
        {
          _logger.error("Write failed for recipe ["+name+"] (id="+id+")!");
        }
      }
    }
    else
    {
      _logger.error("No recipe for URL ["+url+"]");
    }
    return nbRecipes;
  }
}
