package delta.games.lotro.tools.lore.recipes;

import java.io.File;

import org.apache.log4j.Logger;

import delta.common.utils.environment.FileSystem;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Recipes database generator.
 * @author DAM
 */
public class RecipesDatabaseGenerator
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private File _workDir;
  private File _recipesDir;
  //private ItemsAndIconsManager _manager;

  /**
   * Constructor.
   */
  public RecipesDatabaseGenerator()
  {
    _workDir=FileSystem.getTmpDir();
    _recipesDir=new File(_workDir,"recipes");
    //_manager=new ItemsAndIconsManager(_workDir);
  }

  /*
  private void handleRecipe(String key)
  {
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

  private void handleResult(CraftingResult result)
  {
    ItemReference item=result.getItem();
    _manager.handleItem(item);
  }
  */

  /**
   * Perform recipes database generation. 
   */
  public void doIt()
  {
    //_manager.loadMaps();
    // 1 - get recipes index
    File tmpRecipesIndexFile=new File(_workDir,"tmpRecipesIndex.xml");
    RecipesIndexLoader indexLoader=new RecipesIndexLoader();
    boolean indexLoadingOK=indexLoader.doIt(tmpRecipesIndexFile);
    if (!indexLoadingOK)
    {
      _logger.error("Cannot get recipes index! Stopping.");
      return;
    }

    // 2 - load recipes
    RecipesLoader recipesLoader=new RecipesLoader(_recipesDir,tmpRecipesIndexFile);
    boolean recipesLoadingOK=recipesLoader.doIt();
    if (!recipesLoadingOK)
    {
      _logger.error("Cannot load recipes! Stopping.");
      return;
    }

    // 3 - build recipes index
    File recipesIndexFile=new File(_workDir,"recipesIndex.xml");
    RecipesIndexBuilder recipesIndexBuilder=new RecipesIndexBuilder(_recipesDir,recipesIndexFile);
    boolean indexOK=recipesIndexBuilder.doIt();
    if (!indexOK)
    {
      _logger.error("Cannot build recipes index! Stopping.");
      return;
    }
    // 4 - archive recipes
    File archiveFile=new File(_workDir,"recipes.zip");
    RecipesArchiveBuilder archiveBuilder=new RecipesArchiveBuilder(_recipesDir,archiveFile);
    boolean archiveOK=archiveBuilder.doIt();
    if (!archiveOK)
    {
      _logger.error("Cannot build recipes archive! Stopping.");
      return;
    }
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
