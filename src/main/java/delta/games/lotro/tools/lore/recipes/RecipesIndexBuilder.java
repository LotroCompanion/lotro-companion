package delta.games.lotro.tools.lore.recipes;

import java.io.File;

import delta.common.utils.files.filter.ExtensionPredicate;
import delta.common.utils.text.EncodingNames;
import delta.games.lotro.lore.recipes.Recipe;
import delta.games.lotro.lore.recipes.index.RecipesIndex;
import delta.games.lotro.lore.recipes.index.io.xml.RecipesIndexWriter;
import delta.games.lotro.lore.recipes.io.xml.RecipeXMLParser;

/**
 * Builds a recipes index for a series of recipe definition files. 
 * @author DAM
 */
public class RecipesIndexBuilder
{
  private File _recipesDir;
  private File _indexFile;

  /**
   * Constructor.
   * @param recipesDir Recipes directory. 
   * @param indexFile Index file.
   */
  public RecipesIndexBuilder(File recipesDir, File indexFile)
  {
    _recipesDir=recipesDir;
    _indexFile=indexFile;
  }

  /**
   * Do build recipes index.
   * @return <code>true</code> if it was done, <code>false</code> otherwise.
   */
  public boolean doIt()
  {
    boolean ret=false;
    if (_recipesDir.exists())
    {
      RecipesIndex index=new RecipesIndex();
      ExtensionPredicate extFilter=new ExtensionPredicate(".xml");
      File[] recipeFiles=_recipesDir.listFiles(extFilter);
      if (recipeFiles!=null)
      {
        RecipeXMLParser parser=new RecipeXMLParser();
        for(File recipeFile : recipeFiles)
        {
          Recipe recipe=parser.parseXML(recipeFile);
          String profession=recipe.getProfession();
          String key=recipe.getKey();
          String name=recipe.getName();
          //int id=recipe.getIdentifier();
          int tier=recipe.getTier();
          index.addRecipe(key,name,profession,tier);
        }
        RecipesIndexWriter writer=new RecipesIndexWriter();
        ret=writer.write(_indexFile,index,EncodingNames.UTF_8);
      }
    }
    return ret;
  }
}
