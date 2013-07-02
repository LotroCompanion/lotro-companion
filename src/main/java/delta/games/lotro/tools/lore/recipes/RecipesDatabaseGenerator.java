package delta.games.lotro.tools.lore.recipes;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.lore.recipes.Recipe;
import delta.games.lotro.lore.recipes.Recipe.CraftingResult;
import delta.games.lotro.lore.recipes.Recipe.Ingredient;
import delta.games.lotro.lore.recipes.Recipe.ItemReference;
import delta.games.lotro.lore.recipes.Recipe.RecipeVersion;
import delta.games.lotro.lore.recipes.index.RecipesIndex;
import delta.games.lotro.lore.recipes.io.web.RecipePageParser;
import delta.games.lotro.lore.recipes.io.xml.RecipeXMLWriter;
import delta.games.lotro.tools.lore.ItemsAndIconsManager;
import delta.games.lotro.utils.Escapes;
import delta.games.lotro.utils.LotroLoggers;

/**
 * @author DAM
 */
public class RecipesDatabaseGenerator
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private File _workDir;
  private File _recipesDir;
  private ItemsAndIconsManager _manager;

  /**
   * Constructor.
   */
  public RecipesDatabaseGenerator()
  {
    _workDir=FileSystem.getTmpDir();
    _recipesDir=new File(_workDir,"recipes");
    //_questsDir=Config.getInstance().getQuestsDir();
    _manager=new ItemsAndIconsManager(_workDir);
  }

  private void handleRecipe(String key)
  {
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
        }
        else
        {
          _logger.error("Write failed for recipe ["+name+"] (id="+id+")!");
        }

        // Ingredients
        List<Ingredient> ingredients=recipe.getIngredients();
        for(Ingredient ingredient : ingredients)
        {
          ItemReference item=ingredient.getItem();
          _manager.handleItem(item);
        }
        // Results
        List<RecipeVersion> versions=recipe.getVersions();
        for(RecipeVersion version : versions)
        {
          CraftingResult regular=version.getRegular();
          if (regular!=null)
          {
            handleResult(regular);
          }
          CraftingResult critical=version.getCritical();
          if (critical!=null)
          {
            handleResult(critical);
          }
        }
      }
    }
    else
    {
      _logger.error("No recipe for URL ["+url+"]");
    }
  }

  private void handleResult(CraftingResult result)
  {
    ItemReference item=result.getItem();
    _manager.handleItem(item);
  }

  /**
   * Perform quests database generation. 
   */
  public void doIt()
  {
    _manager.loadMaps();
    // 1 - get recipes index
    File tmpRecipesIndexFile=new File(_workDir,"tmpRecipesIndex.xml");
    RecipesIndexLoader indexLoader=new RecipesIndexLoader();
    RecipesIndex index=indexLoader.doIt(tmpRecipesIndexFile);
    String[] keys=index.getKeys();
    for(int i=0;i<keys.length;i++) {
      System.out.println("Recipe #"+i+" ***********************");
      String key=keys[i];
      handleRecipe(key);
      if (i%10==0) {
        _manager.saveMaps();
      }
    }
    /*
    boolean indexLoadingOK=indexLoader.doIt(tmpRecipesIndexFile);
    if (!indexLoadingOK)
    {
      _logger.error("Cannot get recipes index! Stopping.");
      return;
    }
    */
  }

  /**
   * Main method for quests database generator.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    RecipesDatabaseGenerator generator=new RecipesDatabaseGenerator();
    generator.doIt();
  }
}
